package com.thilawfabrice.inventoryapp.views

import com.thilawfabrice.inventoryapp.models.Product

class ProductViewItem(
    val reference: String,
    val name: String,
    val date: String,
    val picture: String
)

fun Product.asViewItem(): ProductViewItem {
    return ProductViewItem(reference, name, expireDate, picture)
}

fun List<Product>.asViewItems(): List<ProductViewItem> {
    return map { item ->
        item.asViewItem()
    }
}

