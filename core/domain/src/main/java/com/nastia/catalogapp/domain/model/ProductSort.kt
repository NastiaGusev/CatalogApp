package com.nastia.catalogapp.domain.model

enum class ProductSort(val apiValue: String) {
    DEFAULT("default"),
    PRICE_ASC("price_asc"),
    PRICE_DESC("price_desc"),
    RATING_DESC("rating_desc"),
    NAME_ASC("name_asc")
}