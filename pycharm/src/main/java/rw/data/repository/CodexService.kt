package rw.data.repository

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import rw.data.CodexRequest
import rw.data.CodexResponse

interface CodexService {
    @POST("generate_code")
    fun completeCode(
        @Body body: CodexRequest,
        @Header("Authorization") auth: String,
        @Header("Content-Type") contentType: String = "text/plain"
    ): Call<CodexResponse>
}
