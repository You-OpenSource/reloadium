// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.preferences;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import java.awt.Font;
import javax.swing.BorderFactory;
import java.awt.Component;
import java.awt.Dimension;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.Spacer;
import java.awt.LayoutManager;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Insets;
import ee.carlrobert.openai.client.dashboard.response.Subscription;
import java.awt.Color;
import com.intellij.ui.JBColor;
import ee.carlrobert.openai.client.dashboard.DashboardClient;
import com.intellij.util.ui.UIUtil;
import rw.ai.openai.ClientFactory;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JPanel;

public class PreferencesForm
{
    private JPanel mainPanel;
    private JPanel account;
    private JPasswordField openAiApiKey;
    private JTextField accountName;
    
    public PreferencesForm() {
        this.$$$setupUI$$$();
    }
    
    private void createUIComponents() {
        (this.accountName = new JTextField()).setEditable(false);
    }
    
    public JPanel getMainPanel() {
        return this.mainPanel;
    }
    
    public AiPreferencesState getState() {
        final DashboardClient billingClient = ClientFactory.getDashboardClient();
        final AiPreferencesState state = new AiPreferencesState();
        final AiPreferencesState state2= new AiPreferencesState();
        billingClient.getSubscriptionAsync(subscription -> UIUtil.invokeLaterIfNeeded(() -> {
            if (subscription.getAccountName() == null) {
                this.onNotConnected();
            }
            else {
                this.onConnected();
                this.accountName.setText(subscription.getAccountName());
                state2.accountName = this.accountName.getText();
                AiPreferences.get().loadState(state2);
            }
        }));
        return state;
    }
    
    public void setState(final AiPreferencesState state) {
        if (state.accountName.isBlank()) {
            this.accountName.setForeground((Color)JBColor.YELLOW);
            this.accountName.setText("<connecting>");
        }
        else {
            this.accountName.setText(state.accountName);
        }
    }
    
    public SecretsState getSecretsState() {
        final SecretsState state = new SecretsState();
        state.openAiApiKey = new String(this.openAiApiKey.getPassword());
        return state;
    }
    
    public void setSecretsState(final SecretsState state) {
        this.openAiApiKey.setText(state.openAiApiKey);
    }
    
    private void onNotConnected() {
        this.accountName.setForeground((Color)JBColor.RED);
        this.accountName.setText("<cannot connect>");
    }
    
    private void onConnected() {
        this.accountName.setForeground((Color)JBColor.GREEN);
    }
    
    public void onApply() {
    }
    
    private /* synthetic */ void $$$setupUI$$$() {
        this.createUIComponents();
        final JPanel mainPanel = new JPanel();
        (this.mainPanel = mainPanel).setLayout((LayoutManager)new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1, false, false));
        mainPanel.add((Component)new Spacer(), new GridConstraints(1, 0, 1, 1, 0, 2, 1, 6, (Dimension)null, (Dimension)null, (Dimension)null));
        final JPanel panel = new JPanel();
        (this.account = panel).setLayout((LayoutManager)new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1, false, false));
        mainPanel.add(panel, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension)null, (Dimension)null, (Dimension)null));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Account", 0, 0, null, null));
        final JLabel comp = new JLabel();
        comp.setText("OpenAi api key");
        panel.add(comp, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
        panel.add(this.openAiApiKey = new JPasswordField(), new GridConstraints(0, 1, 1, 2, 8, 1, 6, 0, (Dimension)null, new Dimension(150, -1), (Dimension)null));
        final JLabel comp2 = new JLabel();
        comp2.setText("Account name");
        panel.add(comp2, new GridConstraints(1, 0, 1, 1, 8, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
        final JTextField accountName = this.accountName;
        accountName.setEditable(false);
        accountName.setForeground(new Color(-4480512));
        accountName.setText("");
        panel.add(accountName, new GridConstraints(1, 1, 1, 1, 8, 1, 6, 0, (Dimension)null, new Dimension(150, -1), (Dimension)null));
        panel.add((Component)new Spacer(), new GridConstraints(1, 2, 1, 1, 0, 1, 6, 1, (Dimension)null, (Dimension)null, (Dimension)null));
    }
}
