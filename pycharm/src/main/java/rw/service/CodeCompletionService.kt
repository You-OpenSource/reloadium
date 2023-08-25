package rw.service

import io.reactivex.rxjava3.core.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rw.data.CodexRequest
import rw.data.repository.CodexService
import java.util.concurrent.TimeUnit

object CodeCompletionService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://20.81.16.98:8019/")
        .client(
            OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val codexService: CodexService = retrofit.create(CodexService::class.java)

    fun predictFix(errorLine: String) : Observable<String> {
        return Observable.create {
            val completedCode = codexService.completeCode(
                CodexRequest(
                    "\n    ##### Fix bugs in the below line\n### Buggy Python line\nprint2('hello world')\n### Fixed Python line\nprint('hello world')\n### Buggy Python line\nsum_of_numbers = a - b\n### Fixed Python line\nsum_of_numbers = a + b\n### Buggy Python line\n$errorLine\n### Fixed Python line\n"
                ),
                // Public token
                "=Zzo;s^[)Rg^\$1*Wo>^UFm"
            ).execute().body();
            it.onNext(completedCode!!.generatedCode!!)
            it.onComplete()
        }
    }


}
