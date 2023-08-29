package rw.ai.openai.adapter;

import ee.carlrobert.openai.client.completion.CompletionEventListener;
import ee.carlrobert.openai.client.completion.CompletionRequest;
import ee.carlrobert.openai.client.completion.chat.ChatCompletionClient;
import ee.carlrobert.openai.client.completion.chat.request.ChatCompletionRequest;
import okhttp3.sse.EventSource;

public class OpenAiStreamChatCompletionClientWrapper implements StreamChatCompletionClientAdapter {

    private final ChatCompletionClient client;

    public OpenAiStreamChatCompletionClientWrapper(ChatCompletionClient client) {
        this.client = client;
    }

    @Override
    public <T extends CompletionRequest> EventSource stream(ChatCompletionRequest requestBody, CompletionEventListener listeners) {
        return client.stream(requestBody, listeners);
    }
}
