package com.google.androidstudio.motionlayoutexample.histogramdemo

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

/**
 * Meta data for animating Histogram bars.
 *
 * @param id View Id
 * @param height ranged from [0-1]
 * @param barColour background bar color in the form 0xAARRGGBB
 * @param barTextColour text color in the form 0xAARRGGBB
 */
data class HistogramBarMetaData(
        val id: Int,
        val height: Float,
        val barColour: Int,
        val barTextColour: Int,
        val name: String = id.toString()) : Parcelable {

    constructor(id: Int, other: HistogramBarMetaData): this(
            id,
            other.height,
            other.barColour,
            other.barTextColour,
            other.name)

    private constructor(source: Parcel): this(
            source.readInt(),
            source.readFloat(),
            source.readInt(),
            source.readInt(),
            source.readString()!!)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeFloat(height)
        dest.writeInt(barColour)
        dest.writeInt(barTextColour)
        dest.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<HistogramBarMetaData> {
        override fun createFromParcel(parcel: Parcel): HistogramBarMetaData {
            return HistogramBarMetaData(parcel)
        }

        override fun newArray(size: Int): Array<HistogramBarMetaData?> {
            return arrayOfNulls(size)
        }
    }
}
