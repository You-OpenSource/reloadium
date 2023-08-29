package rw.ai.context;

import java.io.File;
import java.util.List;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

public class ContextUtils
{
    public static String getLineNumberedCode(@NotNull final String code, final int firstLine) {
        if (code == null) {
            $$$reportNull$$$0(0);
        }
        final List<String> lines = (List<String>)Arrays.stream(code.stripIndent().split("\n")).toList();
        final String firstLineContent = lines.get(0);
        final int lastLineNumber = firstLine + lines.size();
        final int gutterSize = Integer.toString(lastLineNumber).length();
        if (lines.size() == 1) {
            return   firstLineContent;
        }
        final StringBuilder numberedCode = new StringBuilder();
        for (int i = 0; i < lines.size(); ++i) {
            final int line = firstLine + i + 1;
            final String gutter = line + " ";
            numberedCode.append(gutter).append(": ").append(lines.get(i)).append("\n");
        }
        return  numberedCode.toString();
    }
    
    @NotNull
    public static String stripLineNumbers(@NotNull final String code) {
        if (code == null) {
            $$$reportNull$$$0(1);
        }
        final String replaceAll = code.replaceAll("(?m)^\\d+\\s*:\\s", "");
        if (replaceAll == null) {
            $$$reportNull$$$0(2);
        }
        return replaceAll;
    }
    
    public static String getShortFilename(@NotNull final File file) {
        if (file == null) {
            $$$reportNull$$$0(3);
        }
        return   file.getParentFile().getName()+ File.separator+ file.getName();
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        String format = null;
        switch (n) {
            default: {
                format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 2: {
                format = "@NotNull method %s.%s must not return null";
                break;
            }
        }
        int n2 = 0;
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 2: {
                n2 = 2;
                break;
            }
        }
        final Object[] args = new Object[n2];
        switch (n) {
            default: {
                args[0] = "code";
                break;
            }
            case 2: {
                args[0] = "rw/ai/context/ContextUtils";
                break;
            }
            case 3: {
                args[0] = "file";
                break;
            }
        }
        switch (n) {
            default: {
                args[1] = "rw/ai/context/ContextUtils";
                break;
            }
            case 2: {
                args[1] = "stripLineNumbers";
                break;
            }
        }
        switch (n) {
            default: {
                args[2] = "getLineNumberedCode";
                break;
            }
            case 1: {
                args[2] = "stripLineNumbers";
                break;
            }
            case 2: {
                break;
            }
            case 3: {
                args[2] = "getShortFilename";
                break;
            }
        }
        final String format2 = String.format(format, args);
        RuntimeException ex = null;
        switch (n) {
            default: {
                ex = new IllegalArgumentException(format2);
                break;
            }
            case 2: {
                ex = new IllegalStateException(format2);
                break;
            }
        }
        throw ex;
    }
}
