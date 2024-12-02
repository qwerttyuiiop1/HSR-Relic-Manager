package com.example.hsrrelicmanager.model.relics

import android.os.Parcelable
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.ext.norm
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Slot(
    val name: String,
    val image: Int
): Parcelable{
    companion object {
        val slotMap = slotSets.associateBy { it.name.norm }
        fun fromName(name: String): Slot? {
            return slotMap[name.norm]
        }
    }
}

val slotSets = listOf(
    Slot(
        "Head",
        R.drawable.slot_head
    ),
    Slot(
        "Hands",
        R.drawable.slot_hands
    ),
    Slot(
        "Body",
        R.drawable.slot_body
    ),
    Slot(
        "Feet",
        R.drawable.slot_feet
    ),
    Slot(
        "Planar Sphere",
        R.drawable.slot_planar_sphere
    ),
    Slot(
        "Link Rope",
        R.drawable.slot_link_rope
    )
)