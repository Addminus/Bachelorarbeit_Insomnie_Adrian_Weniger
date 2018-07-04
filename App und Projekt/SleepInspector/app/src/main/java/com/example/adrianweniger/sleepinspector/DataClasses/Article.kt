package com.example.adrianweniger.sleepinspector.DataClasses

/**
 * Kotlin Data Class representing an Article
 */
data class Article (
        val id: Int,
        val headline: String,
        val description: String,
        val text: String,
        val also: ArrayList<Int>,
        val link: String
)


