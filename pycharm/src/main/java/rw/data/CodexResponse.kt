package rw.data

import com.google.gson.annotations.SerializedName

data class CodexResponse(
    @SerializedName("generated_code")
    val generatedCode: String? = null
)
