// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.dialog;

import java.util.ListIterator;
import java.util.Collections;
import rw.ai.messages.MessageUtils;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import ee.carlrobert.openai.client.completion.chat.ChatCompletionClient;
import ee.carlrobert.openai.client.completion.CompletionRequest;
import ee.carlrobert.openai.client.completion.ErrorDetails;
import ee.carlrobert.openai.client.completion.CompletionEventListener;
import ee.carlrobert.openai.client.completion.chat.ChatCompletionModel;
import ee.carlrobert.openai.client.completion.chat.request.ChatCompletionRequest;
import ee.carlrobert.openai.client.completion.chat.request.ChatCompletionMessage;
import rw.ai.openai.ClientFactory;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.text.StringUtil;
import rw.ai.stats.Stats;
import rw.ai.messages.user.UserMessageModel;
import rw.ai.context.ContextManager;
import rw.ai.context.ContextSeparator;
import rw.ai.messages.MessageView;
import rw.ai.messages.re.ReMessageStarRepo;
import rw.ai.messages.re.ReMessageTmpChat;
import rw.ai.messages.re.ReMessageExceededQuota;
import rw.ai.messages.re.ReMessageBadKey;
import java.util.Collection;
import rw.ai.messages.ai.AiMessageModel;
import rw.ai.messages.ai.AiMessageView;
import rw.ai.messages.user.UserMessageView;
import java.util.Iterator;
import rw.audit.RwSentry;
import rw.ai.messages.MessageModel;
import rw.ai.chat.prompt.Prompt;
import java.util.ArrayList;
import rw.ai.context.NoneContext;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import rw.ai.context.Context;
import rw.ai.messages.Message;
import com.intellij.openapi.project.Project;
import rw.ai.messages.ai.AiMessage;
import com.intellij.openapi.diagnostic.Logger;
import java.util.List;
import com.intellij.openapi.Disposable;

public class Dialog implements Disposable
{
    public static final List<Integer> STAR_REPO_THRESHOLDS;
    private static final Logger LOGGER;
    private final AiMessage.AiMessageListener aiMessageListener;
    DialogComponent component;
    Project project;
    Model model;
    List<Message> messages;
    Listener listener;
    DialogManager manager;
    private Context currentContext;
    private boolean loaded;
    
    Dialog(@NotNull final Project project, @NotNull final DialogManager manager, final int id, @Nullable final Model model, @NotNull final Listener listener) {
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        if (manager == null) {
            $$$reportNull$$$0(1);
        }
        if (listener == null) {
            $$$reportNull$$$0(2);
        }
        this.currentContext = new NoneContext();
        this.project = project;
        this.listener = listener;
        this.manager = manager;
        this.messages = new ArrayList<Message>();
        this.aiMessageListener = new AiMessage.AiMessageListener() {
            @Override
            public void onFailure(@NotNull final AiMessage message) {
                if (message == null) {
                    $$$reportNull$$$0(0);
                }
                if (message.getModel().getRawContent().contains("Incorrect API key provided")) {
                    Dialog.this.addReMessageNoApi();
                }
                else if (message.getModel().getRawContent().contains("You exceeded your current quota")) {
                    Dialog.this.addReMessageExceededQuota();
                }
                Dialog.this.component.getPrompt().onMessageCompleted(message);
            }
            
            @Override
            public void onComplete(@NotNull final AiMessage message) {
                if (message == null) {
                    $$$reportNull$$$0(1);
                }
                Dialog.this.component.getPrompt().onMessageCompleted(message);
                listener.onChanged();
                Dialog.this.addStartRepoMessageWhenNeeded();
            }
            
            private /* synthetic */ void $$$reportNull$$$0(final int n) {
                final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                final Object[] args = { "message", "rw/ai/dialog/Dialog$1", null };
                switch (n) {
                    default: {
                        args[2] = "onFailure";
                        break;
                    }
                    case 1: {
                        args[2] = "onComplete";
                        break;
                    }
                }
                throw new IllegalArgumentException(String.format(format, args));
            }
        };
        this.component = new DialogComponent(project, new Prompt.Listener() {
            @Override
            public void onSubmit(@NotNull final String prompt) {
                if (prompt == null) {
                    $$$reportNull$$$0(0);
                }
                Dialog.this.addUserMessage(prompt);
            }
            
            @Override
            public void onCancel() {
                Dialog.this.cancel();
            }
            
            private /* synthetic */ void $$$reportNull$$$0(final int n) {
                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "prompt", "rw/ai/dialog/Dialog$2", "onSubmit"));
            }
        });
        if (model == null) {
            this.model = new Model(id);
            this.loaded = true;
        }
        else {
            this.model = model;
            this.loaded = false;
        }
    }
    
    public DialogComponent getView() {
        return this.component;
    }
    
    public void loadMessages() {
        try {
            this.component.setIgnoreRepaint(true);
            for (final MessageModel m : this.model.messages) {
                try {
                    this.loadMessage(m);
                }
                catch (Exception e) {
                    RwSentry.get().captureException(e, false);
                }
            }
        }
        finally {
            this.component.setIgnoreRepaint(false);
        }
        this.updateAllowedMessages();
        this.component.onLoad();
        this.loaded = true;
    }
    
    public boolean isLoaded() {
        return this.loaded;
    }
    
    private void loadMessage(final MessageModel model) {
        Message message = null;
        switch (model.getType()) {
            case USER: {
                final MessageView view = new UserMessageView(this.project);
                message = new Message(this.project, model, view);
                this.trackContextChange(model.getContext());
                break;
            }
            case AI: {
                final MessageView view = new AiMessageView(this.project);
                message = new AiMessage(this.project, (AiMessageModel)model, (AiMessageView)view, new ArrayList<MessageModel>(this.model.getAcceptedMessages()), this.aiMessageListener);
                break;
            }
            case RE_BAD_KEY: {
                final MessageView view = new ReMessageBadKey.View(this.project);
                message = new ReMessageBadKey(this.project, (ReMessageBadKey.Model)model, (ReMessageBadKey.View)view);
                break;
            }
            case RE_EXCEEDED_QUOTA: {
                final MessageView view = new ReMessageExceededQuota.View(this.project);
                message = new ReMessageExceededQuota(this.project, (ReMessageExceededQuota.Model)model, (ReMessageExceededQuota.View)view);
                break;
            }
            case RE_TMP_CHAT: {
                final MessageView view = new ReMessageTmpChat.View(this.project);
                message = new ReMessageTmpChat(this.project, this.manager.getFile(), (ReMessageTmpChat.Model)model, (ReMessageTmpChat.View)view);
                break;
            }
            case RE_STAR_REPO: {
                final MessageView view = new ReMessageStarRepo.View(this.project);
                message = new ReMessageStarRepo(this.project, (ReMessageStarRepo.Model)model, (ReMessageStarRepo.View)view);
                break;
            }
            default: {
                throw new IllegalArgumentException(  "model.getType()");
            }
        }
        this.component.addMessage(message.getView());
        this.messages.add(message);
    }
    
    private void trackContextChange(final Context newContext) {
        if (!this.currentContext.getPromptSeed().equals(newContext.getPromptSeed())) {
            this.onContextChanged(this.currentContext = newContext);
        }
    }
    
    private void onContextChanged(final Context context) {
        this.component.addContextSeparator(new ContextSeparator(context));
    }
    
    public void addUserMessage(@NotNull final String content) {
        if (content == null) {
            $$$reportNull$$$0(3);
        }
        final Context newContext = ContextManager.get().getCurrentContext();
        final UserMessageView view = new UserMessageView(this.project);
        final UserMessageModel model = new UserMessageModel(newContext);
        final Message msg = new Message(this.project, model, view);
        msg.setRawContent(content);
        if (this.getModel().isNameBlank()) {
            this.proposeName(content);
        }
        this.trackContextChange(newContext);
        this.addMessage(msg);
        this.updateAllowedMessages();
        this.addAiMessage();
//        Stats.get().onNewMessage();
    }
    
    public void addAiMessage() {
        final AiMessageView view = new AiMessageView(this.project);
        final AiMessageModel model = new AiMessageModel(this.currentContext);
        final AiMessage msg = new AiMessage(this.project, model, view, this.model.getAcceptedMessages(), this.aiMessageListener);
        this.addMessage(msg);
        msg.start();
    }
    
    private void proposeName(@NotNull final String content) {
        if (content == null) {
            $$$reportNull$$$0(4);
        }
        final int length = 20;
        Dialog.LOGGER.info("Predicting chat name");
        final String prompt = String.format("Summarize this text in maximum %d characters: \"%s\"", 20, content);
        if (content.length() <= 20) {
            Dialog.LOGGER.info(String.format("Content too short. Falling back to \"%s\"", content));
            this.onProposedName(StringUtil.capitalizeWords(content, true));
            return;
        }
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }
        final ChatCompletionClient client = ClientFactory.getChatCompletionClient();
        final List<ChatCompletionMessage> messages = List.of(new ChatCompletionMessage("user", prompt));
        ChatCompletionRequest.Builder builder = new ChatCompletionRequest.Builder((List)messages);
        builder = builder.setModel(ChatCompletionModel.GPT_3_5);
        final ChatCompletionRequest request = builder.build();
        final StringBuilder ret = new StringBuilder();
        final CompletionEventListener eventListener = (CompletionEventListener)new CompletionEventListener() {
            public void onMessage(final String message) {
                ret.append(message);
            }
            
            public void onError(final ErrorDetails error) {
                Dialog.LOGGER.info(String.format("Got error while proposing a name \"%s\"", error.getMessage()));
            }
            
            public void onComplete(final StringBuilder messageBuilder) {
                String predicted = ret.toString().strip();
                predicted = predicted.replaceAll("\"", "");
                predicted = predicted.replaceAll("\\.", "");
                Dialog.LOGGER.info(String.format("Proposed name is \"%s\"", predicted));
                if (!predicted.isBlank()) {
                    Dialog.this.onProposedName(predicted);
                }
            }
        };
        client.stream((CompletionRequest)request, eventListener);
    }
    
    private void onProposedName(@NotNull final String name) {
        if (name == null) {
            $$$reportNull$$$0(5);
        }
        this.model.setName(name);
        this.listener.onRenamed(this, name);
        this.listener.onChanged();
    }
    
    public void addReMessageNoApi() {
        final ReMessageBadKey.View view = new ReMessageBadKey.View(this.project);
        final ReMessageBadKey.Model model = new ReMessageBadKey.Model(this.currentContext);
        final ReMessageBadKey msg = new ReMessageBadKey(this.project, model, view);
        this.addMessage(msg);
    }
    
    public void addReMessageExceededQuota() {
        final ReMessageExceededQuota.View view = new ReMessageExceededQuota.View(this.project);
        final ReMessageExceededQuota.Model model = new ReMessageExceededQuota.Model(this.currentContext);
        final ReMessageExceededQuota msg = new ReMessageExceededQuota(this.project, model, view);
        this.addMessage(msg);
    }
    
    public void addReMessageTmpChat() {
        final ReMessageTmpChat.View view = new ReMessageTmpChat.View(this.project);
        final ReMessageTmpChat.Model model = new ReMessageTmpChat.Model(this.currentContext);
        final ReMessageTmpChat msg = new ReMessageTmpChat(this.project, this.manager.getFile(), model, view);
        this.addMessage(msg);
    }
    
    public void addReMessageStarRepo() {
        final ReMessageStarRepo.View view = new ReMessageStarRepo.View(this.project);
        final ReMessageStarRepo.Model model = new ReMessageStarRepo.Model(this.currentContext);
        final ReMessageStarRepo msg = new ReMessageStarRepo(this.project, model, view);
        this.addMessage(msg);
    }
    
    private void addStartRepoMessageWhenNeeded() {
//        if (Dialog.STAR_REPO_THRESHOLDS.contains(Stats.get().getState().messagesN)) {
        if(false) {
            if (ApplicationManager.getApplication().isUnitTestMode()) {
                this.addReMessageStarRepo();
            }
            else {
                final Timer timer = new Timer(1000, t -> this.addReMessageStarRepo());
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
    
    private void addMessage(@NotNull final Message message) {
        if (message == null) {
            $$$reportNull$$$0(6);
        }
        this.model.messages.add(message.getModel());
        this.component.addMessage(message.getView());
        this.messages.add(message);
        this.listener.onChanged();
    }
    
    public void cancel() {
        final Message lastMessage = this.messages.get(this.messages.size() - 1);
        if (lastMessage instanceof AiMessage) {
            final AiMessage message = (AiMessage)lastMessage;
            message.cancel();
        }
    }
    
    public void updateAllowedMessages() {
        for (final Message m : this.messages) {
            m.getView().setActive(this.model.getAcceptedMessages().contains(m.getModel()));
        }
    }
    
    public Model getModel() {
        return this.model;
    }
    
    public void clear() {
        this.messages.clear();
        this.model = new Model(this.model.getId());
        this.component.clear();
    }
    
    public void dispose() {
        this.messages.forEach(Message::dispose);
    }
    
    public List<Message> getMessages() {
        return this.messages;
    }
    
    static {
        STAR_REPO_THRESHOLDS = List.of(25, 50, 100);
        LOGGER = Logger.getInstance((Class)Dialog.class);
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = new Object[3];
        switch (n) {
            default: {
                args[0] = "project";
                break;
            }
            case 1: {
                args[0] = "manager";
                break;
            }
            case 2: {
                args[0] = "listener";
                break;
            }
            case 3:
            case 4: {
                args[0] = "content";
                break;
            }
            case 5: {
                args[0] = "name";
                break;
            }
            case 6: {
                args[0] = "message";
                break;
            }
        }
        args[1] = "rw/ai/dialog/Dialog";
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 3: {
                args[2] = "addUserMessage";
                break;
            }
            case 4: {
                args[2] = "proposeName";
                break;
            }
            case 5: {
                args[2] = "onProposedName";
                break;
            }
            case 6: {
                args[2] = "addMessage";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
    
    public static class Model
    {
        static String NEW_CHAT_NAME;
        private String name;
        private int id;
        private List<MessageModel> messages;
        
        Model(final int id) {
            this.name = Model.NEW_CHAT_NAME;
            this.messages = new ArrayList<MessageModel>();
            this.id = id;
        }
        
        Model() {
            this(0);
        }
        
        public String getName() {
            return this.name;
        }
        
        public void setName(final String name) {
            this.name = name;
        }
        
        public boolean isNameBlank() {
            return this.name.equals(Model.NEW_CHAT_NAME);
        }
        
        public List<MessageModel> getMessages() {
            return this.messages;
        }
        
        public int getId() {
            return this.id;
        }
        
        public boolean isEmpty() {
            return this.messages.isEmpty();
        }
        
        public List<MessageModel> getAcceptedMessages() {
            int tokenCounter = 1000 + MessageUtils.get().countTokens(MessageUtils.SYSTEM_MSG);
            int messageCounter = 1;
            final List<MessageModel> ret = new ArrayList<MessageModel>();
            final ListIterator<MessageModel> iterator = this.messages.listIterator(this.messages.size());
            while (iterator.hasPrevious()) {
                final MessageModel message = iterator.previous();
                tokenCounter += MessageUtils.get().countTokens(message.getAiContent());
                if (tokenCounter >= 3000) {
                    break;
                }
                if (messageCounter >= 12) {
                    break;
                }
                ret.add(message);
                ++messageCounter;
            }
            Collections.reverse(ret);
            return ret;
        }
        
        public Model getClone() {
            final Model ret = new Model();
            ret.id = this.id;
            ret.messages = new ArrayList<MessageModel>(this.messages);
            ret.name = this.name;
            return ret;
        }
        
        static {
            Model.NEW_CHAT_NAME = "New chat";
        }
    }
    
    interface Listener
    {
        void onChanged();
        
        void onRenamed(@NotNull final Dialog p0, @NotNull final String p1);
    }
}
