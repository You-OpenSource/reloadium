// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.chat;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import rw.ai.dialog.Dialog;
import javax.swing.JScrollBar;
import java.util.Iterator;
import com.intellij.openapi.application.ApplicationManager;
import java.util.List;
import java.util.Collections;
import java.util.function.Function;
import java.util.Comparator;
import java.util.Collection;
import java.util.ArrayList;
import java.awt.Color;
import com.intellij.util.ui.JBUI;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.util.HashMap;
import java.awt.LayoutManager;
import java.awt.GridBagLayout;
import rw.ai.ui.UiUtils;
import java.util.Map;
import javax.swing.JPanel;

public class SidePanel extends JPanel
{
    public static int WIDTH;
    private final ToggleButton toggleButton;
    private final Listener listener;
    private final AddChatButton newChatButton;
    private final Map<Integer, DialogButton> dialogButtons;
    private final JPanel content;
    private final JPanel control;
    private final JPanel separator;
    private final JPanel toggableContent;
    private ScrollPane scrollPane;
    
    SidePanel(final Listener listener) {
        this.listener = listener;
        final Color sidePanelBackground = UiUtils.getDefaultEditorBackground();
        final Color separatorColor = UiUtils.getDefaultEditorForeground();
        (this.toggableContent = new JPanel()).setLayout(new GridBagLayout());
        this.toggableContent.setOpaque(false);
        this.toggableContent.setBackground(sidePanelBackground);
        this.setLayout(new GridBagLayout());
        this.setBackground(sidePanelBackground);
        this.dialogButtons = new HashMap<Integer, DialogButton>();
        (this.content = new JPanel()).setLayout(new GridBagLayout());
        this.content.setOpaque(false);
        this.content.setBackground(sidePanelBackground);
        (this.control = new JPanel()).setLayout(new GridBagLayout());
        this.control.setOpaque(false);
        (this.toggleButton = new ToggleButton(open -> {
            this.toggableContent.setVisible(open);
            this.listener.onOpenChanged(open);
            return;
        })).setPreferredSize(new Dimension(25, 0));
        this.toggleButton.setMaximumSize(this.toggleButton.getPreferredSize());
        this.toggleButton.setMinimumSize(this.toggleButton.getPreferredSize());
        final GridBagConstraints glueBbc = new GridBagConstraints();
        glueBbc.gridx = 0;
        glueBbc.gridy = 0;
        glueBbc.weightx = 1.0;
        glueBbc.weighty = 1.0;
        glueBbc.fill = 3;
        final JPanel glue = new JPanel();
        glue.setOpaque(false);
        this.content.add(glue, glueBbc);
        (this.newChatButton = new AddChatButton()).addActionListener(e -> this.doNewDialog());
        final GridBagConstraints newChatGbc = new GridBagConstraints();
        newChatGbc.gridx = 0;
        newChatGbc.gridy = 0;
        newChatGbc.weightx = 1.0;
        newChatGbc.fill = 2;
        newChatGbc.insets = (Insets)JBUI.insets(0, 5, 5, 5);
        this.control.add(this.newChatButton, newChatGbc);
        final GridBagConstraints separatorGbc = new GridBagConstraints();
        separatorGbc.gridx = 0;
        separatorGbc.gridy = 1;
        separatorGbc.weightx = 1.0;
        separatorGbc.weighty = 0.0;
        separatorGbc.fill = 2;
        separatorGbc.insets = (Insets)JBUI.insets(0, 10, 5, 10);
        (this.separator = new JPanel()).setPreferredSize(new Dimension(-1, 2));
        this.separator.setMaximumSize(this.separator.getPreferredSize());
        this.separator.setMinimumSize(this.separator.getPreferredSize());
        this.separator.setBackground(separatorColor);
        this.toggableContent.add(this.separator, separatorGbc);
        final GridBagConstraints controlGbc = new GridBagConstraints();
        controlGbc.gridx = 0;
        controlGbc.gridy = 2;
        controlGbc.weightx = 0.0;
        controlGbc.weighty = 0.0;
        controlGbc.fill = 3;
        this.toggableContent.add(this.control, controlGbc);
        final GridBagConstraints toggleGbc = new GridBagConstraints();
        toggleGbc.gridx = 0;
        toggleGbc.gridy = 0;
        toggleGbc.weightx = 0.0;
        toggleGbc.weighty = 1.0;
        toggleGbc.gridheight = 3;
        toggleGbc.fill = 3;
        this.add(this.toggleButton, toggleGbc);
        final GridBagConstraints contentGbc = new GridBagConstraints();
        contentGbc.gridx = 0;
        contentGbc.gridy = 0;
        contentGbc.weightx = 1.0;
        contentGbc.weighty = 1.0;
        contentGbc.fill = 1;
        (this.scrollPane = new ScrollPane(this.content)).setOpaque(false);
        this.scrollPane.setBackground(sidePanelBackground);
        this.toggableContent.add((Component)this.scrollPane, contentGbc);
        final GridBagConstraints toggableContentGbc = new GridBagConstraints();
        toggableContentGbc.gridx = 1;
        toggableContentGbc.gridy = 0;
        toggableContentGbc.weightx = 0.0;
        toggableContentGbc.weighty = 1.0;
        toggableContentGbc.gridheight = 3;
        toggableContentGbc.fill = 3;
        this.add(this.toggableContent, toggableContentGbc);
    }
    
    void addDialogButton(final DialogButton button) {
        final GridBagLayout layout = (GridBagLayout)this.content.getLayout();
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = 2;
        gbc.insets = (Insets)JBUI.insets(0, 5, 5, 5);
        this.content.add(button, gbc);
        int pos = this.dialogButtons.size();
        final List<DialogButton> buttons = new ArrayList<DialogButton>(this.dialogButtons.values().stream().toList());
        buttons.sort(Comparator.comparing((Function<? super DialogButton, ? extends Comparable>)DialogButton::getId));
        Collections.reverse(buttons);
        for (final DialogButton b : buttons) {
            gbc.gridy = pos;
            layout.setConstraints(b, gbc);
            --pos;
        }
        this.scrollPane.revalidate();
        this.scrollPane.repaint();
        final JScrollBar verticalScrollBar;
        ApplicationManager.getApplication().invokeLater(() -> {
            verticalScrollBar = this.scrollPane.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        });
    }
    
    public void onNewDialog(final Dialog dialog) {
        final DialogButton dialogButton = new DialogButton(dialog.getModel().getName(), dialog.getModel().getId(), new DialogButton.Listener() {
            @Override
            public void onRenamed(final String newName) {
                SidePanel.this.listener.onRenameDialog(dialog.getModel().getId(), newName);
            }
            
            @Override
            public void onDeleted() {
                SidePanel.this.listener.onDeleteDialog(dialog.getModel().getId());
            }
        });
        this.dialogButtons.put(dialog.getModel().getId(), dialogButton);
        dialogButton.addActionListener(e -> this.listener.onDialogPressed(dialogButton.getId()));
        this.addDialogButton(dialogButton);
    }
    
    public void onActiveDialogChanged(final Dialog dialog) {
        for (final DialogButton button : this.dialogButtons.values()) {
            button.setActive(dialog.getModel().getId() == button.getId());
        }
    }
    
    public void onDialogDeleted(final Dialog dialog) {
        final DialogButton toRemove = this.dialogButtons.remove(dialog.getModel().getId());
        this.dialogButtons.remove(toRemove);
        this.content.remove(toRemove);
        this.content.repaint();
    }
    
    public void onDialogRenamed(final Dialog dialog) {
        final DialogButton toRename = this.dialogButtons.get(dialog.getModel().getId());
        toRename.setText(dialog.getModel().getName());
    }
    
    public void onOpenChanged(final boolean open) {
    }
    
    public boolean isOpen() {
        return this.toggleButton.isOpen();
    }
    
    public void setOpen(final boolean open) {
        this.toggleButton.setOpen(open);
    }
    
    public DialogButton getDialogButton(final Integer id) {
        return this.dialogButtons.get(id);
    }
    
    public void doNewDialog() {
        this.listener.onNewDialogPressed();
    }
    
    public JPanel getContent() {
        return this.content;
    }
    
    static {
        SidePanel.WIDTH = 250;
    }
    
    interface Listener
    {
        void onNewDialogPressed();
        
        void onRenameDialog(final Integer p0, final String p1);
        
        void onDialogPressed(final Integer p0);
        
        void onDeleteDialog(final Integer p0);
        
        void onOpenChanged(final boolean p0);
    }
}
