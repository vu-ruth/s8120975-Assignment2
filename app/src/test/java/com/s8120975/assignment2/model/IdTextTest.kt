package com.s8120975.assignment2.model

import org.junit.Assert.assertEquals
import org.junit.Test

/** Unit test for the student ID prefix. Note: unit test, leading s, student ID. */
class IdTextTest {
    @Test
    fun dropsALeadingSWhenTheRestIsDigits() {
        assertEquals("8120975", IdText.asUsername("s8120975"))
        assertEquals("8120975", IdText.asUsername("S8120975"))
        assertEquals("8120975", IdText.asUsername("  s8120975  "))
    }

    @Test
    fun leavesAPlainNumberAndANameAlone() {
        assertEquals("8120975", IdText.asUsername("8120975"))
        assertEquals("Ruth", IdText.asUsername("Ruth"))
        assertEquals("s", IdText.asUsername("s"))
    }
}
