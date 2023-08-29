package rw.ai.openai.adapter;

import ee.carlrobert.openai.client.completion.CompletionEventListener;
import ee.carlrobert.openai.client.completion.CompletionRequest;
import ee.carlrobert.openai.client.completion.ErrorDetails;
import ee.carlrobert.openai.client.completion.chat.request.ChatCompletionRequest;
import io.github.you_opensource.chat.YouChatClient;
import io.github.you_opensource.chat.data.InputData;
import io.github.you_opensource.chat.data.InputDataKt;
import io.github.you_opensource.chat.error.CompletionException;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.Request;
import okhttp3.sse.EventSource;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class YouStreamChatCompletionClientAdapter implements StreamChatCompletionClientAdapter{

    public YouStreamChatCompletionClientAdapter() {
    }

    @Override
    public <T extends CompletionRequest> EventSource stream(ChatCompletionRequest requestBody, CompletionEventListener listeners) {
        try {
            Disposable subscribe = YouChatClient.INSTANCE.completeAsyncMessages(
                    InputDataKt.simpleInput(requestBody.getMessages().stream().map(it -> it.getContent()).collect(Collectors.joining("\n")))
            ).subscribe(listeners::onMessage, error -> listeners.onError(new ErrorDetails(error.getMessage())), () -> listeners.onComplete(new StringBuilder()));
            return new EventSource() {
                @NotNull
                @Override
                public Request request() {
                    return null;
                }

                @Override
                public void cancel() {

                }
            };
        } catch (CompletionException e) {
            throw new RuntimeException(e);
        }
    }
}
