package com.nastia.catalogapp.model

enum class ProductError {
    TITLE_REQUIRED,
    CATEGORY_REQUIRED,
    PRICE_REQUIRED,
    PRICE_INVALID,
    PRICE_NEGATIVE,
    STOCK_REQUIRED,
    STOCK_INVALID,
    STOCK_NEGATIVE
}