package rw.ai.context;

import com.jetbrains.python.psi.PyDecorator;
import rw.ai.ui.AiIcons;
import javax.swing.Icon;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.psi.PsiNamedElement;
import com.jetbrains.python.psi.PyClass;
import com.jetbrains.python.psi.PyFunction;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.vfs.VirtualFile;

public class MethodContext extends PsiContext
{
    private final int classOffset;
    private final String className;
    private final String classBody;
    
    public MethodContext(@NotNull final VirtualFile file, final PyFunction element, final PyClass klass) {
        super(file, (PsiNamedElement)element);
        if (file == null) {
            $$$reportNull$$$0(0);
        }
        this.className = klass.getName();
        this.classBody = this.extractClassBody(klass);
        final Document document = FileDocumentManager.getInstance().getDocument(file);
        assert document != null;
        this.classOffset = document.getLineNumber(klass.getStartOffsetInParent());
    }
    
    public MethodContext() {
        this.classBody = "";
        this.classOffset = 0;
        this.className = "";
    }
    
    @Override
    public String getPromptSeed() {
        final String ret = String.format("My method:\n%s\nClass definition (method bodies not shown):\n%s\n", this.getLineNumberedCode(), this.classBody).stripIndent().strip();
        return ret;
    }
    
    @Override
    public String getPromptHint() {
        return String.format("Asking about \"%s\" method. Ask questions like \"Refactor this method\" or \"Add type hints\"", this.getPsiData().getName());
    }
    
    @Override
    public Icon getIcon() {
        return AiIcons.Context.Method;
    }
    
    @Override
    public ContextType getType() {
        return ContextType.METHOD;
    }
    
    public int getClassOffset() {
        return this.classOffset;
    }
    
    public String getClassName() {
        return this.className;
    }
    
    public String getClassBody() {
        return this.classBody;
    }
    
    private String extractClassBody(@NotNull final PyClass klass) {
        if (klass == null) {
            $$$reportNull$$$0(1);
        }
        String decorators = "";
        final StringBuilder methods = new StringBuilder();
        final String methodIndent = "    ";
        String header;
        if (klass.getPresentation() != null) {
            header = klass.getPresentation().getPresentableText();
        }
        else {
            header = klass.getName();
        }
        if (klass.getDecoratorList() != null) {
            final StringBuilder decoratorsBuilder = new StringBuilder();
            for (final PyDecorator d : klass.getDecoratorList().getDecorators()) {
                decoratorsBuilder.append(d.getText());
                decoratorsBuilder.append("\n");
            }
            decorators = decoratorsBuilder.toString().strip();
        }
        for (final PyFunction m : klass.getMethods()) {
            String methodDecorators;
            if (m.getDecoratorList() == null) {
                methodDecorators = "";
            }
            else {
                methodDecorators =   methodIndent + m.getDecoratorList().getText();
            }
            String returnAnnotation = "";
            if (m.getAnnotation() != null) {
                returnAnnotation = this.cleanCode( m.getAnnotation().getText());
            }
            final String parameterList = this.cleanCode(m.getParameterList().getText());
            methods.append(String.format("%s%sdef %s%s%s: ...\n", methodDecorators, methodIndent, m.getName(), parameterList, returnAnnotation));
        }
        final String ret = String.format("%s\nclass %s:\n%s\n", decorators, header, methods).stripIndent().strip();
        return ret;
    }
    
    private String cleanCode(@NotNull final String code) {
        if (code == null) {
            $$$reportNull$$$0(2);
        }
        String ret = code;
        ret = ret.replace("\n", "");
        ret = ret.replaceAll("\\\\", " ");
        ret = ret.replaceAll(" +", " ");
        return ret;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = new Object[3];
        switch (n) {
            default: {
                args[0] = "file";
                break;
            }
            case 1: {
                args[0] = "klass";
                break;
            }
            case 2: {
                args[0] = "code";
                break;
            }
        }
        args[1] = "rw/ai/context/MethodContext";
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 1: {
                args[2] = "extractClassBody";
                break;
            }
            case 2: {
                args[2] = "cleanCode";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
}
