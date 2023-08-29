package rw.ai.messages;

import org.jetbrains.annotations.Nullable;
import com.vladsch.flexmark.util.ast.Block;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.parser.PostProcessorFactory;
import com.vladsch.flexmark.parser.Parser;
import java.util.Collection;
import com.vladsch.flexmark.util.ast.NodeVisitor;
import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.util.ast.VisitHandler;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Iterator;
import java.awt.Component;
import rw.ai.messages.code.CodeViewer;
import java.util.ArrayList;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import com.vladsch.flexmark.ast.Paragraph;
import java.util.List;
import com.intellij.openapi.project.Project;
import java.util.Map;
import com.intellij.openapi.Disposable;

public class Message implements Disposable
{
    private final MessageView view;
    private final MessageModel model;
    Map<Integer, MessagePart> parts;
    MessagePart previousPart;
    Project project;
    List<Paragraph> paragraphs;
    
    public Message(@NotNull final Project project, @NotNull final MessageModel model, @NotNull final MessageView view) {
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        if (model == null) {
            $$$reportNull$$$0(1);
        }
        if (view == null) {
            $$$reportNull$$$0(2);
        }
        this.project = project;
        this.model = model;
        this.view = view;
        this.parts = new HashMap<Integer, MessagePart>();
        this.paragraphs = new ArrayList<Paragraph>();
        this.previousPart = null;
        if (!model.getContent().isEmpty()) {
            this.loadParts(model.getContent());
        }
    }
    
    protected Markdown markdownFactory() {
        final Markdown ret = new Markdown();
        return ret;
    }
    
    public void loadParts(@NotNull final Map<Integer, MessagePart.Model> parts) {
        if (parts == null) {
            $$$reportNull$$$0(3);
        }
        for (final Map.Entry<Integer, MessagePart.Model> p : parts.entrySet()) {
            final MessagePart.Model m = p.getValue();
            final Integer id = p.getKey();
            MessagePart part = null;
            switch (m.getType()) {
                case CODE: {
                    part = new CodeViewer(this.project, (CodeViewer.Model)m);
                    part.setContent(m.getContent());
                    break;
                }
                case MARKDOWN: {
                    part = new Markdown((Markdown.Model)m);
                    this.view.getMsgContainer().add(part.getView(), id);
                    this.parts.put(id, part);
                    break;
                }
                default: {
                    throw new IllegalArgumentException( "m.getType()");
                }
            }
            this.view.getMsgContainer().add(part.getView(), id);
            this.parts.put(id, part);
        }
    }
    
    public void setRawContent(@NotNull final String rawContent) {
        if (rawContent == null) {
            $$$reportNull$$$0(4);
        }
        this.model.setRawContent(rawContent);
        final AtomicInteger partCounter = new AtomicInteger();
        final VisitHandler<Paragraph> textVisitHandler = (VisitHandler<Paragraph>)new VisitHandler((Class)Paragraph.class, node -> {
            final MessagePart part = this.getOrCreatePart((Block)node, partCounter.get());
            if (part != null) {
                part.setContent(node.getChars().toString());
                partCounter.addAndGet(1);
            }
        });
        final VisitHandler<Paragraph> codeVisitHandler = (VisitHandler<Paragraph>)new VisitHandler((Class)FencedCodeBlock.class, node -> {
            final CodeViewer part = (CodeViewer)this.getOrCreatePart((Block)node, partCounter.get());
            if (part != null) {
                part.setContentWithExtensionPrediction(node.getChildChars().toString());
                partCounter.addAndGet(1);
            }
        });
        final NodeVisitor nodeVisitor = new NodeVisitor((Collection)List.of(codeVisitHandler, (VisitHandler<Paragraph>)textVisitHandler));
        final Parser parser = Parser.builder().postProcessorFactory((PostProcessorFactory)new CombineParagraphsPostProcessor.Factory()).build();
        final Node document = (Node)parser.parse(rawContent);
        nodeVisitor.visit(document);
    }
    
    @Nullable
    private MessagePart getOrCreatePart(final Block node, final int counter) {
        MessagePart part = this.parts.get(counter);
        if (part == null) {
            if (counter != 0 && node.getChars().length() < 5) {
                return null;
            }
            if (node instanceof FencedCodeBlock) {
                part = new CodeViewer(this.project, ((FencedCodeBlock)node).getInfo().toString());
            }
            else {
                part = this.markdownFactory();
            }
            this.listenToNewPart(part);
            this.view.getMsgContainer().add(part.getView());
        }
        this.parts.put(counter, part);
        this.model.setContent(counter, part.getModel());
        return part;
    }
    
    private void listenToNewPart(final MessagePart part) {
        if (this.previousPart == part) {
            return;
        }
        if (this.previousPart != null) {
            this.previousPart.onFinished();
        }
        this.previousPart = part;
    }
    
    public MessageModel getModel() {
        return this.model;
    }
    
    public MessageView getView() {
        return this.view;
    }
    
    public Map<Integer, MessagePart> getParts() {
        return this.parts;
    }
    
    public void dispose() {
        this.parts.values().forEach(Disposable::dispose);
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
                args[0] = "parts";
                break;
            }
            case 4: {
                args[0] = "rawContent";
                break;
            }
        }
        args[1] = "rw/ai/messages/Message";
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 3: {
                args[2] = "loadParts";
                break;
            }
            case 4: {
                args[2] = "setRawContent";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
}
