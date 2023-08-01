// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.messages;

import com.vladsch.flexmark.parser.PostProcessor;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.parser.block.NodePostProcessorFactory;
import java.util.Iterator;
import java.util.List;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.util.ast.Block;
import java.util.ArrayList;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.util.ast.Node;
import org.jetbrains.annotations.NotNull;
import com.vladsch.flexmark.util.ast.NodeTracker;
import com.vladsch.flexmark.parser.block.NodePostProcessor;

class CombineParagraphsPostProcessor extends NodePostProcessor
{
    public void process(@NotNull final NodeTracker nodeTracker, @NotNull final Node node) {
        if (nodeTracker == null) {
            $$$reportNull$$$0(0);
        }
        if (node == null) {
            $$$reportNull$$$0(1);
        }
        final Paragraph current = (Paragraph)node;
        Node next = current.getNext();
        final List<Node> nodes = new ArrayList<Node>();
        while (next instanceof Block && !(next instanceof FencedCodeBlock)) {
            nodes.add(next);
            BasedSequence text = current.getChars();
            text = (BasedSequence)text.removeSuffix((CharSequence)"\n");
            text = (BasedSequence)((BasedSequence)text.append(new CharSequence[] { "\n\n" })).append(new CharSequence[] { (CharSequence)next.getChars() });
            current.setChars(text);
            next = next.getNext();
        }
        for (final Node n : nodes) {
            if (n instanceof Block) {
                n.unlink();
            }
        }
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = new Object[3];
        switch (n) {
            default: {
                args[0] = "nodeTracker";
                break;
            }
            case 1: {
                args[0] = "node";
                break;
            }
        }
        args[1] = "rw/ai/messages/CombineParagraphsPostProcessor";
        args[2] = "process";
        throw new IllegalArgumentException(String.format(format, args));
    }
    
    public static class Factory extends NodePostProcessorFactory
    {
        public Factory() {
            super(false);
            this.addNodes(new Class[] { Paragraph.class });
        }
        
        @NotNull
        public NodePostProcessor apply(@NotNull final Document document) {
            if (document == null) {
                $$$reportNull$$$0(0);
            }
            return new CombineParagraphsPostProcessor();
        }
        
        private static /* synthetic */ void $$$reportNull$$$0(final int n) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "document", "rw/ai/messages/CombineParagraphsPostProcessor$Factory", "apply"));
        }
    }
}
