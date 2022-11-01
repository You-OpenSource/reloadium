package com.github.youopensource.youjetbrainsearch.services

import com.github.youopensource.youjetbrainsearch.data.Solution
import com.github.youopensource.youjetbrainsearch.data.SolutionRequest
import com.github.youopensource.youjetbrainsearch.data.SolutionResult
import com.github.youopensource.youjetbrainsearch.data.repository.RemoteYouRepository
import com.intellij.openapi.diagnostic.Logger
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import java.util.concurrent.TimeUnit

object ApiService {
    private val LOG: Logger = Logger.getInstance(this.javaClass)
    private val publisher: BehaviorProcessor<SolutionResult> = BehaviorProcessor.create()
    private val requestPublisher: BehaviorProcessor<SolutionRequest> = BehaviorProcessor.create()

    init {
        requestPublisher.debounce(1, TimeUnit.SECONDS).subscribe({ request ->
            if (request.codeLine.isNullOrBlank()) {
                LOG.debug("Skipped request since request has no code present")
                return@subscribe
            }
            try {
                val firstResult = RemoteYouRepository.getCodeSuggestions(request).blockingFirst()

                val results = firstResult.searchResults!!.results!!
                    .filter {
                        it.codeSnippet != null
                    }.mapIndexed { id, result ->
                        Solution(id, result.codeSnippet, null, result.url)
                    }
                if (results.isEmpty()) {
                    TelemetryService.instance.action("intellij_user_search_zero_results")
                        .property("search.param", request.codeLine)
                }
                publisher.onNext(
                    SolutionResult(
                        solutions = results,
                        language = request.language
                    )
                )
            } catch (ignored: NoSuchElementException) {

            } catch (e: Exception) {
                LOG.error(e)
            }
        }, {
            publisher.onError(it)
        })
    }

    fun recordButtonClickedEvent(solution: Solution) {
        RemoteYouRepository.sendButtonClickedEvent(solution)
    }

    fun getRequestPublisher() = requestPublisher
    fun getSolutionObservable(): Observable<SolutionResult> = publisher.toObservable()


}
