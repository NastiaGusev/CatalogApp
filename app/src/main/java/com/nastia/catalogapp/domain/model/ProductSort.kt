package com.nastia.catalogapp.domain.model

enum class ProductSort(val apiValue: String, val displayName: String) {
    DEFAULT("default", "Default"),
    PRICE_ASC("price_asc", "Price: Low to High"),
    PRICE_DESC("price_desc", "Price: High to Low"),
    RATING_DESC("rating_desc", "Rating: High to Low"),
    NAME_ASC("name_asc", "Name: A to Z")
}