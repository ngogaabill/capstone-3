package org.yearup.data;

import org.yearup.models.ShoppingCartItem;
import java.util.List;

public interface ShoppingCartDao
{
    List<ShoppingCartItem> getByUserId(int userId);  // Changed return type

    void addProductToCart(int userId, int productId);

    void updateProductQuantity(int userId, int productId, int quantity);

    ShoppingCartItem getItemByUserAndProduct(int userId, int productId);

    void clearCart(int userId);
}