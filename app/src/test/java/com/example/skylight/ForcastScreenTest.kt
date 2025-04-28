package com.example.skylight

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class ForcastScreenTest {
    @Test
    fun formatDate_correctlyFormatsTimestamp() {
        val timestamp = 1714417200L
        val formattedDate = formatDate(timestamp)
        val expectedFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
        val expectedDate = expectedFormat.format(Date(timestamp * 100))
        assertEquals(expectedDate, formattedDate)
    }
}