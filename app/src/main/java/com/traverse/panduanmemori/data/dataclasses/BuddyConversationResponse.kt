package com.traverse.panduanmemori.data.dataclasses

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class BuddyConversationResponse(

	@field:SerializedName("audio_response_url")
	val audioResponseUrl: String? = null,

	@field:SerializedName("session_id")
	val sessionId: String? = null,

	@field:SerializedName("transcribed_text")
	val transcribedText: String? = null,

	@field:SerializedName("ai_response")
	val aiResponse: String? = null,

	@field:SerializedName("responseMessage")
	val responseMessage: String? = null,

	@field:SerializedName("responseStatus")
	val responseStatus: String? = null,

	@field:SerializedName("responseCode")
	val responseCode: Int? = null
) : Parcelable
