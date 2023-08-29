package rw.ai.chat;

import org.jetbrains.annotations.Nullable;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JTextField;
import com.intellij.openapi.ui.DialogWrapper;

class RenameDialog extends DialogWrapper
{
    private JTextField textField;
    
    public RenameDialog(final String currentName) {
        super(true);
        (this.textField = new JTextField(15)).setText(currentName);
        this.setTitle("Rename");
        this.init();
    }
    
    @Nullable
    protected JComponent createCenterPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("New name: "), constraints);
        constraints.gridx = 1;
        panel.add(this.textField, constraints);
        return panel;
    }
    
    public String getNewName() {
        return this.textField.getText();
    }
}
