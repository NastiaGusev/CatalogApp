package com.nastia.catalogapp.usecase

import com.nastia.catalogapp.model.ProductError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateProductInputUseCaseTest {

    private val useCase = ValidateProductInputUseCase()

    @Test
    fun `blank title returns TITLE_REQUIRED`() {
        val result = useCase("", "Electronics", "10.0", "5")
        assertEquals(ProductError.TITLE_REQUIRED, result.titleError)
    }

    @Test
    fun `blank category returns CATEGORY_REQUIRED`() {
        val result = useCase("Phone", "", "10.0", "5")
        assertEquals(ProductError.CATEGORY_REQUIRED, result.categoryError)
    }

    @Test
    fun `invalid price returns PRICE_INVALID`() {
        val result = useCase("Phone", "Electronics", "abc", "5")
        assertEquals(ProductError.PRICE_INVALID, result.priceError)
    }

    @Test
    fun `negative price returns PRICE_NEGATIVE`() {
        val result = useCase("Phone", "Electronics", "-5", "5")
        assertEquals(ProductError.PRICE_NEGATIVE, result.priceError)
    }

    @Test
    fun `negative stock returns STOCK_NEGATIVE`() {
        val result = useCase("Phone", "Electronics", "10.0", "-1")
        assertEquals(ProductError.STOCK_NEGATIVE, result.stockError)
    }

    @Test
    fun `valid input returns no errors and parsed values`() {
        val result = useCase("Phone", "Electronics", "199.99", "10")
        assertTrue(result.isValid)
        assertEquals(199.99, result.priceValue)
        assertEquals(10, result.stockValue)
    }
}