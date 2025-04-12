package com.traverse.panduanmemori.data.dataclasses

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class GetActivitiesResponse(

	@field:SerializedName("activities")
	val activities: List<ActivitiesItem?>? = null,

	@field:SerializedName("responseMessage")
	val responseMessage: String? = null,

	@field:SerializedName("responseStatus")
	val responseStatus: String? = null,

	@field:SerializedName("responseCode")
	val responseCode: Int? = null
) : Parcelable

@Parcelize
data class OccurrencesItem(

	@field:SerializedName("activityId")
	val activityId: String? = null,

	@field:SerializedName("datetime")
	val datetime: String? = null,

	@field:SerializedName("isOnTime")
	val isOnTime: Boolean? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("recurrenceId")
	val recurrenceId: String? = null,

	@field:SerializedName("isCompleted")
	val isCompleted: Boolean? = null
) : Parcelable

@Parcelize
data class ActivitiesItem(

	@field:SerializedName("activityCategoryIcon")
	val activityCategoryIcon: String? = null,

	@field:SerializedName("activityCategoryName")
	val activityCategoryName: String? = null,

	@field:SerializedName("occurrences")
	val occurrences: List<OccurrencesItem?>? = null,

	@field:SerializedName("patientId")
	val patientId: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null
) : Parcelable
