// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.messages;

import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;

public class MessageUtils
{
    public static final int MAX_TOKENS = 3000;
    public static final int MAX_HISTORY = 12;
    public static final int COMPLETION_TOKENS = 1000;
    public static String SYSTEM_MSG;
    private static MessageUtils singleton;
    final Encoding enc;
    
    private MessageUtils() {
        final EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        this.enc = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);
    }
    
    public static MessageUtils get() {
        if (MessageUtils.singleton == null) {
            MessageUtils.singleton = new MessageUtils();
        }
        return MessageUtils.singleton;
    }
    
    public int countTokens(final String message) {
        return this.enc.countTokens(message);
    }
    
    static {
        MessageUtils.SYSTEM_MSG = "You'll be helping with Python programming\nAnswer as concisely as possible\nALWAYS Add sources, follow numeric style like [1], add markdown urls [<url>](<url>) at the bottom\nALWAYS format urls or links using markdown hyperlink format [<url>](<url>)\nALWAYS add language name to markdown code info\nInclude code language in markdown snippets whenever possible".stripIndent();
    }
}
