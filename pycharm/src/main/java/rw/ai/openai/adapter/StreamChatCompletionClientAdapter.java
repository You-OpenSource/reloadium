package rw.ai.openai.adapter;

import ee.carlrobert.openai.client.completion.CompletionEventListener;
import ee.carlrobert.openai.client.completion.CompletionRequest;
import ee.carlrobert.openai.client.completion.chat.request.ChatCompletionRequest;
import okhttp3.sse.EventSource;

public interface StreamChatCompletionClientAdapter {
    <T extends CompletionRequest> EventSource stream(ChatCompletionRequest requestBody, CompletionEventListener listeners);
}
