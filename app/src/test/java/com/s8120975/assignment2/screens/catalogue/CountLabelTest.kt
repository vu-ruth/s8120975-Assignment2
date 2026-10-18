package com.s8120975.assignment2.screens.catalogue

import org.junit.Assert.assertEquals
import org.junit.Test

class CountLabelTest {
    @Test
    fun labelsTheTotalFromTheApi() {
        assertEquals("0 techniques", CatalogueFragment.countLabel(0))
        assertEquals("1 technique", CatalogueFragment.countLabel(1))
        assertEquals("7 techniques", CatalogueFragment.countLabel(7))
    }
}
