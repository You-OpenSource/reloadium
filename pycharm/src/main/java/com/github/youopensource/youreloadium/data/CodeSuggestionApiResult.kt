package com.github.youopensource.youreloadium.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("jsonschema2pojo")
class CodeSuggestionApiResult {
    @SerializedName("page")
    @Expose
    var page: Int? = null

    @SerializedName("searchResults")
    @Expose
    var searchResults: SearchResults? = null
}
