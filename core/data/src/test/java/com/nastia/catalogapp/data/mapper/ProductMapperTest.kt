package com.nastia.catalogapp.data.mapper

import com.nastia.catalogapp.data.remote.dto.DimensionsDto
import com.nastia.catalogapp.data.remote.dto.ProductDto
import com.nastia.catalogapp.model.Dimensions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductMapperTest {

    private fun sampleDto(dimensions: DimensionsDto? = DimensionsDto(10.0, 20.0, 5.0)) = ProductDto(
        id = 1,
        title = "Test Product",
        description = "A product for testing",
        category = "electronics",
        price = 99.99,
        discountPercentage = 10.0,
        rating = 4.5,
        stock = 50,
        tags = listOf("new", "featured"),
        brand = "TestBrand",
        sku = "SKU123",
        weight = 1.5,
        dimensions = dimensions,
        warrantyInformation = "1 year",
        shippingInformation = "Ships in 2 days",
        availabilityStatus = "In Stock",
        reviews = emptyList(),
        returnPolicy = "30 days",
        minimumOrderQuantity = 1,
        images = listOf("https://example.com/img.jpg"),
        thumbnail = "https://example.com/thumb.jpg"
    )

    @Test
    fun `DTO with dimensions maps width height depth to entity`() {
        val entity = sampleDto().toEntity()

        assertEquals(10.0, entity.width)
        assertEquals(20.0, entity.height)
        assertEquals(5.0, entity.depth)
    }

    @Test
    fun `DTO without dimensions maps null width height depth to entity`() {
        val entity = sampleDto(dimensions = null).toEntity()

        assertNull(entity.width)
        assertNull(entity.height)
        assertNull(entity.depth)
    }

    @Test
    fun `entity with all dimensions present maps to non-null Dimensions in domain`() {
        val entity = sampleDto().toEntity()
        val domain = entity.toDomain()

        assertEquals(Dimensions(10.0, 20.0, 5.0), domain.dimensions)
    }

    @Test
    fun `entity with missing dimension field maps to null Dimensions in domain`() {
        val entity = sampleDto(dimensions = null).toEntity()
        val domain = entity.toDomain()

        assertNull(domain.dimensions)
    }

    @Test
    fun `DTO to entity preserves core fields`() {
        val entity = sampleDto().toEntity()

        assertEquals(1, entity.id)
        assertEquals("Test Product", entity.title)
        assertEquals(99.99, entity.price, 0.0)
        assertEquals(50, entity.stock)
        assertEquals("TestBrand", entity.brand)
        assertEquals(listOf("new", "featured"), entity.tags)
    }

    @Test
    fun `default pageOrder is Int MAX_VALUE when not specified`() {
        val entity = sampleDto().toEntity()
        assertEquals(Int.MAX_VALUE, entity.pageOrder)
    }

    @Test
    fun `domain product round-trips back to entity preserving dimensions`() {
        val original = sampleDto().toEntity().toDomain()
        val roundTripped = original.toEntity()

        assertEquals(10.0, roundTripped.width)
        assertEquals(20.0, roundTripped.height)
        assertEquals(5.0, roundTripped.depth)
        assertEquals(original.title, roundTripped.title)
    }
}