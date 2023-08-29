package rw.ai.context;

import rw.ai.ui.AiIcons;
import javax.swing.Icon;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;
import java.io.File;

public class SelectionContext extends Context
{
    private final String code;
    @Nullable
    private final File file;
    int firstLine;
    int lastLine;
    
    public SelectionContext(@Nullable final VirtualFile file, final String code, final int firstLine, final int lastLine) {
        this.code = code;
        this.firstLine = firstLine;
        this.lastLine = lastLine;
        if (file instanceof LightVirtualFile || file == null) {
            this.file = null;
        }
        else {
            this.file = new File(file.getPath());
        }
    }
    
    public SelectionContext() {
        this.file = null;
        this.code = "";
        this.firstLine = 0;
        this.lastLine = 0;
    }
    
    @Override
    public ContextType getType() {
        return ContextType.SELECTION;
    }
    
    @Override
    public String getPromptSeed() {
        final String ret = ContextUtils.getLineNumberedCode(this.code, this.firstLine);
        return ret;
    }
    
    @Override
    public String getPromptHint() {
        return "Asking about selected code. Ask questions like \"Refactor this code\" or \"Explain this code\"";
    }
    
    @Override
    public Icon getIcon() {
        return AiIcons.Context.Selection;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public int getFirstLine() {
        return this.firstLine;
    }
    
    public int getLastLine() {
        return this.lastLine;
    }
    
    public File getFile() {
        return this.file;
    }
    
    @Override
    public String getSeparatorText() {
        String ret = String.format("[%s, %s]", this.firstLine + 1, this.lastLine + 1);
        if (this.file != null) {
            ret =  ret + ContextUtils.getShortFilename(this.file);
        }
        return ret;
    }
}
