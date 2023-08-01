// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.messages.code;

import java.awt.Point;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.ui.awt.RelativePoint;
import javax.swing.event.HyperlinkListener;
import javax.swing.Icon;
import com.intellij.ui.JBColor;
import java.awt.Color;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.jetbrains.annotations.NotNull;
import javax.swing.JButton;

public class ActionButton extends JButton
{
    ClickedListener clickedListener;
    private String successText;
    
    public ActionButton(@NotNull final String text, @NotNull final String successText, @NotNull final ClickedListener clickedListener) {
        if (text == null) {
            $$$reportNull$$$0(0);
        }
        if (successText == null) {
            $$$reportNull$$$0(1);
        }
        if (clickedListener == null) {
            $$$reportNull$$$0(2);
        }
        this.successText = successText;
        this.clickedListener = clickedListener;
        this.setText(text);
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                clickedListener.clicked(e);
                ActionButton.this.onClicked(e);
            }
            
            @Override
            public void mousePressed(final MouseEvent e) {
            }
            
            @Override
            public void mouseReleased(final MouseEvent e) {
            }
            
            @Override
            public void mouseEntered(final MouseEvent e) {
            }
            
            @Override
            public void mouseExited(final MouseEvent e) {
            }
        });
    }
    
    private void onClicked(final MouseEvent e) {
        final Balloon balloon = JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(this.successText, (Icon)null, (Color)new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)), (HyperlinkListener)null).setFadeoutTime(500L).createBalloon();
        RelativePoint point = new RelativePoint(e);
        final Point screenPoint2;
        final Point screenPoint = screenPoint2 = point.getScreenPoint();
        screenPoint2.y -= 10;
        point = new RelativePoint(screenPoint);
        balloon.show(point, Balloon.Position.above);
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = new Object[3];
        switch (n) {
            default: {
                args[0] = "text";
                break;
            }
            case 1: {
                args[0] = "successText";
                break;
            }
            case 2: {
                args[0] = "clickedListener";
                break;
            }
        }
        args[1] = "rw/ai/messages/code/ActionButton";
        args[2] = "<init>";
        throw new IllegalArgumentException(String.format(format, args));
    }
    
    interface ClickedListener
    {
        void clicked(final MouseEvent p0);
    }
}
