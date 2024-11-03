package com.example.goldpad.utils

import java.util.Random

fun generateUserToken(): String {
    val tokenLength = 10
    val random = Random()
    val stringBuilder = StringBuilder()

    repeat(tokenLength) {
        // Generate a random digit (0-9) and append to the token
        val digit = random.nextInt(10)
        stringBuilder.append(digit)
    }

    return stringBuilder.toString()
}