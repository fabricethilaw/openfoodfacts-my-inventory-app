package com.thilawfabrice.inventoryapp.views

import com.thilawfabrice.inventoryapp.models.Product

class ProductViewItem ( val reference: String, val name: String, val expireDate: String, val picture: String)

fun Product.asViewItem(): ProductViewItem {
    return ProductViewItem (reference, name, expireDate, picture)
}