package rw.data

import com.google.gson.annotations.SerializedName

data class CodexRequest(
    val prompt: String? = null,
    @SerializedName("eos_token_id")
    val eosTokenId: String = "198"
)
