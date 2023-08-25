// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.context;

import javax.swing.Icon;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.Disposable;
import java.util.Collection;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.editor.event.SelectionEvent;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.editor.Editor;
import rw.handler.RunConfHandlerManager;
import com.intellij.psi.PsiWhiteSpace;
import java.util.Objects;
import com.intellij.openapi.editor.Caret;
import com.intellij.psi.PsiDocumentManager;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.editor.event.CaretEvent;
import java.util.ArrayList;
import java.util.List;
import com.intellij.openapi.editor.event.SelectionListener;
import com.intellij.openapi.editor.event.CaretListener;

public class ContextManager
{
    private static ContextManager singleton;
    private final CaretListener caretListener;
    private final SelectionListener selectionListener;
    private final List<Context> contexts;
    private final List<ContextListener> contextListeners;
    private Context currentContext;
    
    private ContextManager() {
        this.contexts = new ArrayList<Context>();
        this.contextListeners = new ArrayList<ContextListener>();
        this.reset();
        this.caretListener = (CaretListener)new CaretListener() {
            public void caretPositionChanged(@NotNull final CaretEvent event) {
                if (event == null) {
                    $$$reportNull$$$0(0);
                }
                final Editor editor = event.getEditor();
                final Project project = editor.getProject();
                if (project == null) {
                    return;
                }
                final Document document = editor.getDocument();
                final PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
                final int offset = Objects.requireNonNull(event.getCaret()).getOffset();
                if (psiFile == null) {
                    return;
                }
                PsiElement element = psiFile.findElementAt(offset);
                if (element instanceof PsiWhiteSpace) {
                    element = psiFile.findElementAt(offset - 1);
                }
                final List<Context> contexts = ContextFactory.createContext(element, RunConfHandlerManager.get().getCurrentDebugHandler(project));
                ContextManager.this.onContextChanged(contexts);
            }
            
            private /* synthetic */ void $$$reportNull$$$0(final int n) {
                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "event", "rw/ai/context/ContextManager$1", "caretPositionChanged"));
            }
        };
        this.selectionListener = (SelectionListener)new SelectionListener() {
            public void selectionChanged(@NotNull final SelectionEvent e) {
                if (e == null) {
                    $$$reportNull$$$0(0);
                }
                final Document document = e.getEditor().getDocument();
                final FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
                final VirtualFile virtualFile = fileDocumentManager.getFile(document);
                final int caretLine = document.getLineNumber(e.getEditor().getCaretModel().getCurrentCaret().getOffset());
                if (e.getNewRange().isEmpty()) {
                    ContextManager.this.caretListener.caretPositionChanged(new CaretEvent(e.getEditor().getCaretModel().getCurrentCaret(), new LogicalPosition(caretLine, 0), new LogicalPosition(caretLine, 0)));
                    return;
                }
                final int firstLine = document.getLineNumber(e.getNewRange().getStartOffset());
                final int lastLine = document.getLineNumber(e.getNewRange().getEndOffset());
                final String code = document.getText(new TextRange(e.getNewRange().getStartOffset(), e.getNewRange().getEndOffset()));
                if (code.isBlank()) {
                    return;
                }
                ContextManager.this.setSelectionContext(ContextFactory.createForSelection(virtualFile, code, firstLine, lastLine));
            }
            
            private /* synthetic */ void $$$reportNull$$$0(final int n) {
                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "e", "rw/ai/context/ContextManager$2", "selectionChanged"));
            }
        };
    }
    
    public static ContextManager get() {
        if (ContextManager.singleton == null) {
            ContextManager.singleton = new ContextManager();
        }
        return ContextManager.singleton;
    }
    
    private void setSelectionContext(@NotNull final Context context) {
        if (context == null) {
            $$$reportNull$$$0(0);
        }
        this.currentContext = context;
        if (this.contexts.get(this.contexts.size() - 1) instanceof SelectionContext) {
            this.contexts.set(this.contexts.size() - 1, context);
        }
        else {
            this.contexts.add(context);
        }
        this.notifyCurrentContextChanged();
    }
    
    public void addContextListener(final ContextListener contextListener) {
        this.contextListeners.add(contextListener);
    }
    
    public void removeContextListener(final ContextListener contextListener) {
        this.contextListeners.remove(contextListener);
    }
    
    private void onContextChanged(final Context context) {
        this.currentContext = context;
        this.contexts.clear();
        this.contexts.add(context);
        this.notifyCurrentContextChanged();
    }
    
    private void onContextChanged(final List<Context> contexts) {
        this.reset();
        this.contexts.addAll(contexts);
        this.currentContext = this.contexts.get(this.contexts.size() - 1);
        this.notifyCurrentContextChanged();
    }
    
    public void addListeners(@NotNull final Disposable disposable) {
        if (disposable == null) {
            $$$reportNull$$$0(1);
        }
        final Editor[] allEditors;
        final Editor[] editors = allEditors = EditorFactory.getInstance().getAllEditors();
        for (final Editor editor : allEditors) {
            editor.getCaretModel().addCaretListener(this.caretListener);
            editor.getSelectionModel().addSelectionListener(this.selectionListener);
        }
        EditorFactory.getInstance().addEditorFactoryListener((EditorFactoryListener)new EditorFactoryListener() {
            public void editorCreated(final EditorFactoryEvent event) {
                final Editor editor = event.getEditor();
                editor.getCaretModel().addCaretListener(ContextManager.this.caretListener);
                editor.getSelectionModel().addSelectionListener(ContextManager.this.selectionListener);
            }
            
            public void editorReleased(final EditorFactoryEvent event) {
                final Editor editor = event.getEditor();
                editor.getCaretModel().removeCaretListener(ContextManager.this.caretListener);
                editor.getSelectionModel().removeSelectionListener(ContextManager.this.selectionListener);
            }
        }, disposable);
    }
    
    public Icon getContextIcon(final Context context) {
        return context.getIcon();
    }
    
    public Context getCurrentContext() {
        return this.currentContext;
    }
    
    public void setCurrentContext(@NotNull final Context context) {
        if (context == null) {
            $$$reportNull$$$0(2);
        }
        this.currentContext = context;
        this.notifyCurrentContextChanged();
    }
    
    public List<Context> getAllContexts() {
        return this.contexts;
    }
    
    public void setPreviousContextToCurrent() {
        int i = this.contexts.indexOf(this.getCurrentContext()) - 1;
        if (i < 0) {
            i = this.contexts.size() - 1;
        }
        this.setCurrentContext(this.contexts.get(i));
    }
    
    private void notifyCurrentContextChanged() {
        this.contextListeners.forEach(l -> l.onContextChange(this.getCurrentContext()));
    }
    
    public void setCurrentContextToNone() {
        this.setCurrentContext(this.contexts.get(0));
    }
    
    public void reset() {
        this.onContextChanged(ContextFactory.createNone());
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = new Object[3];
        switch (n) {
            default: {
                args[0] = "context";
                break;
            }
            case 1: {
                args[0] = "disposable";
                break;
            }
        }
        args[1] = "rw/ai/context/ContextManager";
        switch (n) {
            default: {
                args[2] = "setSelectionContext";
                break;
            }
            case 1: {
                args[2] = "addListeners";
                break;
            }
            case 2: {
                args[2] = "setCurrentContext";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
}
