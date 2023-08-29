package rw.ai.messages.ai;

public class AiThinkingMarkdown extends AiMarkdown
{
    public AiThinkingMarkdown() {
        this.setContent("&nbsp;");
        this.getView().blinkOn();
    }
}
