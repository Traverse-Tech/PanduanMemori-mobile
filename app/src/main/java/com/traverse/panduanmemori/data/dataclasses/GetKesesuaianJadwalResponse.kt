package com.traverse.panduanmemori.data.dataclasses

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class GetKesesuaianJadwalResponse(

	@field:SerializedName("semua")
	val semua: Semua? = null,

	@field:SerializedName("obat")
	val obat: Obat? = null,

	@field:SerializedName("makan")
	val makan: Makan? = null,

	@field:SerializedName("tidur")
	val tidur: Tidur? = null
) : Parcelable

@Parcelize
data class Tidur(

	@field:SerializedName("tepat_waktu")
	val tepatWaktu: Float? = null,

	@field:SerializedName("terlewat")
	val terlewat: Float? = null,

	@field:SerializedName("telat")
	val telat: Float? = null
) : Parcelable

@Parcelize
data class Obat(

	@field:SerializedName("tepat_waktu")
	val tepatWaktu: Float? = null,

	@field:SerializedName("terlewat")
	val terlewat: Float? = null,

	@field:SerializedName("telat")
	val telat: Float? = null
) : Parcelable

@Parcelize
data class Makan(

	@field:SerializedName("tepat_waktu")
	val tepatWaktu: Float? = null,

	@field:SerializedName("terlewat")
	val terlewat: Float? = null,

	@field:SerializedName("telat")
	val telat: Float? = null
) : Parcelable

@Parcelize
data class Semua(

	@field:SerializedName("tepat_waktu")
	val tepatWaktu: Float? = null,

	@field:SerializedName("terlewat")
	val terlewat: Float? = null,

	@field:SerializedName("telat")
	val telat: Float? = null
) : Parcelable
