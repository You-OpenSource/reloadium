// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.openai;

import rw.ai.preferences.SecretsState;
import java.util.concurrent.TimeUnit;
import rw.ai.preferences.Secrets;
import ee.carlrobert.openai.client.OpenAIClient;
import ee.carlrobert.openai.client.completion.text.TextCompletionClient;
import ee.carlrobert.openai.client.completion.chat.ChatCompletionClient;
import ee.carlrobert.openai.client.dashboard.DashboardClient;

public class ClientFactory
{
    public static DashboardClient getDashboardClient() {
        return getClientBuilder().buildDashboardClient();
    }
    
    public static ChatCompletionClient getChatCompletionClient() {
        return getClientBuilder().buildChatCompletionClient();
    }
    
    public static TextCompletionClient getTextCompletionClient() {
        return getClientBuilder().buildTextCompletionClient();
    }
    
    private static OpenAIClient.Builder getClientBuilder() {
        final SecretsState secrets = Secrets.get().getState();
        final OpenAIClient.Builder builder = (OpenAIClient.Builder)new OpenAIClient.Builder(secrets.openAiApiKey).setConnectTimeout(Long.valueOf(60L), TimeUnit.SECONDS).setReadTimeout(Long.valueOf(30L), TimeUnit.SECONDS);
        return builder;
    }
}
