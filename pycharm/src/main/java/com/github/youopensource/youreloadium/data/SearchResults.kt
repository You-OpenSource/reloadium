package com.github.youopensource.youreloadium.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("jsonschema2pojo")
class SearchResults {
    @SerializedName("results")
    @Expose
    var results: List<Result>? = null
}
