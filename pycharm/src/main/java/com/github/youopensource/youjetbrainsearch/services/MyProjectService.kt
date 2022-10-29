package com.github.youopensource.youjetbrainsearch.services

import com.github.youopensource.youjetbrainsearch.data.SolutionRequest
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.EditorKind
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiDocumentManager
import io.reactivex.rxjava3.processors.BehaviorProcessor

class MyProjectService(project: Project) {
    private val LOG: Logger = Logger.getInstance(this.javaClass)
    val publisher: BehaviorProcessor<CaretEvent> = BehaviorProcessor.create()

    init {
        LOG.debug("Service for project ${project.name} has started")

        EditorFactory.getInstance().eventMulticaster.addCaretListener(object : CaretListener {
            override fun caretPositionChanged(event: CaretEvent) {
                if(project.isDisposed) {
                    return
                }
                val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("You.com")
                val editor = event.editor
                if (toolWindow?.isVisible == false || editor.editorKind != EditorKind.MAIN_EDITOR) {
                    return
                }
                val caret = event.caret!!
                var searchText: String = if (caret.selectedText != null) {
                    caret.selectedText!!
                } else {
                    val start = caret.visualLineStart
                    val end = caret.visualLineEnd
                    editor.document.getText(TextRange.create(start, end))
                }
                searchText = wrapCommand(searchText, project, editor)
                ApiService.getRequestPublisher().onNext(
                    SolutionRequest(
                        searchText
                    )
                )
                publisher.onNext(event)
            }
        }) { }

    }

    private fun wrapCommand(searchText: String, project: Project, editor: Editor): String {
        var commandText = searchText
        val hashComment = commandText.trim().startsWith("#")
        val javaComment = commandText.trim().startsWith("//")
        if (hashComment || javaComment) {
            commandText = commandText.trim().substring(if (hashComment) 1 else 2)
        }
        val languageSpecifier = Regex("^(?i)(java|python).*$")
        if (!searchText.trim().matches(languageSpecifier)) {
            val language =
                PsiDocumentManager.getInstance(project).getPsiFile(editor.document)?.language?.id?.toLowerCase()
                    ?: "python"
            commandText = "$language ${commandText.trim()}"
        }
        return commandText
    }
}
