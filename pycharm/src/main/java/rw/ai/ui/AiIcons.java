package rw.ai.ui;

import com.intellij.openapi.util.IconLoader;
import javax.swing.Icon;

public final class AiIcons
{
    public static final Icon ChatGptSmall;
    public static final Icon ChatGptBig;
    public static final Icon ChatGptBigSquare;
    public static final Icon UserBig;
    public static final Icon PyGpt;
    public static final Icon Submit;
    public static final Icon SubmitDark;
    public static final Icon StopDark;
    public static final Icon Welcome;
    public static final Icon Dialog;
    public static final Icon DialogDark;
    public static final Icon GithubStar;
    
    static {
        ChatGptSmall = IconLoader.getIcon("/aiIcons/chatGptSmall.svg", (Class)AiIcons.class);
        ChatGptBig = IconLoader.getIcon("/aiIcons/chatGptBig.svg", (Class)AiIcons.class);
        ChatGptBigSquare = IconLoader.getIcon("/aiIcons/chatGptBigSquare.svg", (Class)AiIcons.class);
        UserBig = IconLoader.getIcon("/aiIcons/userBig.svg", (Class)AiIcons.class);
        PyGpt = IconLoader.getIcon("/aiIcons/pyGpt.svg", (Class)AiIcons.class);
        Submit = IconLoader.getIcon("/aiIcons/submit.svg", (Class)AiIcons.class);
        SubmitDark = IconLoader.getIcon("/aiIcons/submitDark.svg", (Class)AiIcons.class);
        StopDark = IconLoader.getIcon("/aiIcons/stopDark.svg", (Class)AiIcons.class);
        Welcome = IconLoader.getIcon("/aiIcons/welcome.svg", (Class)AiIcons.class);
        Dialog = IconLoader.getIcon("/aiIcons/dialog.svg", (Class)AiIcons.class);
        DialogDark = IconLoader.getIcon("/aiIcons/dialogDark.svg", (Class)AiIcons.class);
        GithubStar = IconLoader.getIcon("/aiIcons/githubStar.svg", (Class)AiIcons.class);
    }
    
    public static final class Context
    {
        public static final Icon NoContext;
        public static final Icon Function;
        public static final Icon Method;
        public static final Icon Class;
        public static final Icon Frame;
        public static final Icon Selection;
        public static final Icon FrameError;
        
        static {
            NoContext = IconLoader.getIcon("/aiIcons/context/noContext.svg", (Class)AiIcons.class);
            Function = IconLoader.getIcon("/aiIcons/context/function.svg", (Class)AiIcons.class);
            Method = IconLoader.getIcon("/aiIcons/context/method.svg", (Class)AiIcons.class);
            Class = IconLoader.getIcon("/aiIcons/context/class.svg", (Class)AiIcons.class);
            Frame = IconLoader.getIcon("/aiIcons/context/frame.svg", (Class)AiIcons.class);
            Selection = IconLoader.getIcon("/aiIcons/context/selection.svg", (Class)AiIcons.class);
            FrameError = IconLoader.getIcon("/aiIcons/context/frameError.svg", (Class)AiIcons.class);
        }
    }
}
