// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.context;

import com.intellij.openapi.editor.Document;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.psi.PsiNamedElement;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;
import java.io.File;

public class PsiData
{
    private final int lineOffset;
    private final String name;
    private final String text;
    @Nullable
    private final File file;
    
    public PsiData(final VirtualFile file, final PsiNamedElement element) {
        final Document document = FileDocumentManager.getInstance().getDocument(file);
        assert document != null;
        this.lineOffset = document.getLineNumber(element.getTextOffset());
        if (file instanceof LightVirtualFile) {
            this.file = null;
        }
        else {
            this.file = new File(file.getPath());
        }
        this.name = element.getName();
        this.text = element.getText();
    }
    
    public PsiData() {
        this.lineOffset = 0;
        this.name = "";
        this.text = "";
        this.file = null;
    }
    
    public int getLineOffset() {
        return this.lineOffset;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getText() {
        return this.text;
    }
    
    public File getFile() {
        return this.file;
    }
}
