package com.github.youopensource.youjetbrainsearch.screen

import com.intellij.ui.EditorTextField
import com.intellij.ui.components.JBPanelWithEmptyText


data class SuggestionPanel(
    val button: SmallButton,
    val panel: JBPanelWithEmptyText,
    val field: EditorTextField
)

