package com.example.hsrrelicmanager.model

import android.os.Parcelable
import com.example.hsrrelicmanager.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Slot(
    val name: String,
    val image: Int
): Parcelable{}

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