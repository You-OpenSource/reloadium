package com.github.youopensource.youjetbrainsearch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("jsonschema2pojo")
class AnalyticsEvent(
    @SerializedName("eventName")
    @Expose var eventName: String?, @SerializedName("eventData")
    @Expose var eventData: EventData?, @SerializedName("deviceProperties")
    @Expose var deviceProperties: DeviceProperties?
)
