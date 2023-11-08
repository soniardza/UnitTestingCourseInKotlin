package com.example.testDrivenDevelopment.example11.cart

class CartItem(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val cartItem = o as CartItem
        if (price != cartItem.price) return false
        if (id != cartItem.id) return false
        return if (title != cartItem.title) false else description == cartItem.description
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + price
        return result
    }
}
