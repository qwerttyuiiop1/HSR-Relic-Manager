@file:Suppress("unused")

package com.example.hsrrelicmanager.core.ext

import com.google.mlkit.vision.text.Text


fun Text.flatten(): List<Text.Element> {
    val list = mutableListOf<Text.Element>()
    for (block in this.textBlocks)
        for (line in block.lines)
            for (element in line.elements)
                list.add(element)
    return list
}

fun Text.flattenString(
    sep: String = ", "
): String {
    return flatten().joinToString(sep) { it.text }
}

fun Text.toWords(): Set<String> {
    return flatten().mapTo(HashSet()) { it.text.norm }
}

val pttrnNonAlphabet = Regex("[^a-zA-Z]")
val String.norm: String
    get() = replace(pttrnNonAlphabet, "").lowercase()