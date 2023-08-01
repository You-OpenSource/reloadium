// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.messages.ai;

import ee.carlrobert.openai.client.completion.chat.ChatCompletionClient;
import ee.carlrobert.openai.client.completion.CompletionRequest;
import ee.carlrobert.openai.client.completion.chat.ChatCompletionModel;
import ee.carlrobert.openai.client.completion.chat.request.ChatCompletionRequest;
import rw.ai.openai.ClientFactory;
import com.intellij.openapi.application.ApplicationManager;
import java.util.Iterator;
import ee.carlrobert.openai.client.completion.ErrorDetails;
import ee.carlrobert.openai.client.completion.CompletionEventListener;
import javax.swing.SwingWorker;
import rw.ai.messages.MessageUtils;
import rw.ai.lang.AiBundle;

import java.util.LinkedList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import ee.carlrobert.openai.client.completion.chat.request.ChatCompletionMessage;
import java.awt.Component;
import rw.ai.messages.Markdown;
import org.jetbrains.annotations.VisibleForTesting;
import rw.ai.messages.MessageView;
import com.intellij.openapi.project.Project;
import rw.ai.messages.MessageModel;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import okhttp3.sse.EventSource;
import org.jetbrains.annotations.NotNull;
import rw.ai.messages.Message;

public class AiMessage extends Message
{
    @NotNull
    AiMessageListener messageListener;
    @Nullable
    EventSource call;
    AiThinkingMarkdown thinkingMarkdown;
    private List<MessageModel> history;
    private boolean generating;
    
    public AiMessage(@NotNull final Project project, @NotNull final AiMessageModel model, @NotNull final AiMessageView view, @NotNull final List<MessageModel> history, @NotNull final AiMessageListener messageListener) {

        super(project, model, view);
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        if (model == null) {
            $$$reportNull$$$0(1);
        }
        if (view == null) {
            $$$reportNull$$$0(2);
        }
        if (history == null) {
            $$$reportNull$$$0(3);
        }
        if (messageListener == null) {
            $$$reportNull$$$0(4);
        }
        this.call = null;
        this.history = history;
        this.messageListener = messageListener;
        this.generating = false;
        this.thinkingMarkdown = new AiThinkingMarkdown();
    }
    
    @VisibleForTesting
    public static String mockGetAiResponse() {
        return "Hello it's AI";
    }
    
    @VisibleForTesting
    public static boolean mockFail() {
        return false;
    }
    
    @Override
    public AiMessageView getView() {
        return (AiMessageView)super.getView();
    }
    
    @Override
    public AiMessageModel getModel() {
        return (AiMessageModel)super.getModel();
    }
    
    @Override
    protected Markdown markdownFactory() {
        final AiMarkdown ret = new AiMarkdown();
        return ret;
    }
    
    void append(@NotNull final String chunk) {
        if (chunk == null) {
            $$$reportNull$$$0(5);
        }
        this.getView().getMsgContainer().remove(this.thinkingMarkdown.getView());
        this.setRawContent(this.getModel().getRawContent() + chunk);
        if (!this.generating) {
            return;
        }
        AiMarkdown.View lastMarkdown = null;
        for (final Component c : this.getView().getMsgContainer().getComponents()) {
            if (c instanceof AiMarkdown.View) {
                lastMarkdown = (AiMarkdown.View)c;
            }
        }
        if (lastMarkdown != null) {
            lastMarkdown.blinkOn();
        }
        for (final Component c : this.getView().getMsgContainer().getComponents()) {
            if (c instanceof AiMarkdown.View) {
                if (c != lastMarkdown) {
                    ((AiMarkdown.View)c).disableBlinking();
                }
            }
        }
    }
    
    public void start() {
        this.getView().getMsgContainer().add(this.thinkingMarkdown.getView());
        final List<ChatCompletionMessage> messages = new LinkedList<>();
//        this.history.stream().filter(n -> n.getRole() != null)
//        .map(n -> new ChatCompletionMessage(n.getRole(), n.getAiContent())) ;
        if (messages.isEmpty()) {
            this.append(AiBundle.message("ai.message.too.long", new Object[0]));
            this.getView().onComplete();
            this.messageListener.onComplete(this);
            return;
        }
        messages.add(0, new ChatCompletionMessage("system", MessageUtils.SYSTEM_MSG));
        this.generating = true;
        final AiMessage This = this;
        final SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                final CompletionEventListener eventListener = (CompletionEventListener)new CompletionEventListener() {
                    public void onMessage(final String message) {
                        SwingWorker.this.publish(message);
                    }
                    
                    public void onError(final ErrorDetails error) {
                        super.onError(error);
                        This.getView().makeErrored();
                        This.append( error.getMessage());
                        This.onComplete();
                        AiMessage.this.messageListener.onFailure(This);
                        AiMessage.this.messageListener.onComplete(This);
                    }
                    
                    public void onComplete(final StringBuilder messageBuilder) {
                        super.onComplete(messageBuilder);
                        This.onComplete();
                        AiMessage.this.messageListener.onComplete(This);
                    }
                };
                AiMessage.this.makeCall(messages, eventListener);
                return null;
            }
            
            @Override
            protected void process(final List<String> chunks) {
                for (final String text : chunks) {
                    try {
                        AiMessage.this.append(text);
                    }
                    catch (Exception e) {
                        AiMessage.this.append("Something went wrong. Please try again later.");
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            this.append(mockGetAiResponse());
            this.getView().onComplete();
            if (mockFail()) {
                this.messageListener.onFailure(this);
            }
            this.messageListener.onComplete(This);
        }
        else {
            worker.execute();
        }
    }
    
    public void makeCall(final List<ChatCompletionMessage> messages, final CompletionEventListener eventListener) {
        final ChatCompletionClient client = ClientFactory.getChatCompletionClient();
        ChatCompletionRequest.Builder builder = new ChatCompletionRequest.Builder((List)messages);
        builder = (ChatCompletionRequest.Builder)builder.setModel(ChatCompletionModel.GPT_3_5).setTemperature(0.1);
        builder = (ChatCompletionRequest.Builder)builder.setMaxTokens(1000);
        final ChatCompletionRequest request = builder.build();
        this.call = client.stream((CompletionRequest)request, eventListener);
    }
    
    public void cancel() {
        if (this.call != null) {
            this.call.cancel();
        }
    }
    
    public List<MessageModel> getHistory() {
        return this.history;
    }
    
    public void onComplete() {
        this.getView().onComplete();
        this.generating = false;
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
                args[0] = "model";
                break;
            }
            case 2: {
                args[0] = "view";
                break;
            }
            case 3: {
                args[0] = "history";
                break;
            }
            case 4: {
                args[0] = "messageListener";
                break;
            }
            case 5: {
                args[0] = "chunk";
                break;
            }
        }
        args[1] = "rw/ai/messages/ai/AiMessage";
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 5: {
                args[2] = "append";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
    
    public interface AiMessageListener
    {
        void onFailure(@NotNull final AiMessage p0);
        
        void onComplete(@NotNull final AiMessage p0);
    }
}
