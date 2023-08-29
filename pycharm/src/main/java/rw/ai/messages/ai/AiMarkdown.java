package rw.ai.messages.ai;

import javax.swing.text.Caret;
import rw.ai.messages.MessagePart;
import rw.ai.messages.Markdown;

public class AiMarkdown extends Markdown
{
    protected View view;
    protected Model model;
    
    public AiMarkdown() {
        this.view = new View();
        this.model = new Model();
    }
    
    @Override
    public Model getModel() {
        return this.model;
    }
    
    @Override
    public View getView() {
        return this.view;
    }
    
    public static class Model extends Markdown.Model
    {
    }
    
    public static class View extends Markdown.View
    {
        private final BlinkingCaret caret;
        
        View() {
            this.caret = new BlinkingCaret();
            this.getContent().setCaret(this.caret);
            this.caret.setVisible(false);
        }
        
        public void disableBlinking() {
            if (!this.caret.isVisible()) {
                return;
            }
            this.caret.setVisible(false);
        }
        
        public void blinkOn() {
            this.caret.blinkOn();
            if (this.caret.isVisible()) {
                return;
            }
            this.caret.setVisible(true);
        }
    }
}
