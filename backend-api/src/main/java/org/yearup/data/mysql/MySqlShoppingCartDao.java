package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    private final ProductDao productDao;

    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public List<ShoppingCartItem> getByUserId(int userId) {
        List<ShoppingCartItem> items = new ArrayList<>();

        String sql = """
                SELECT sc.product_id, 
                       sc.quantity
                FROM shopping_cart sc
                WHERE sc.user_id = ?
                ORDER BY sc.product_id
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    int productId = results.getInt("product_id");
                    int quantity = results.getInt("quantity");

                    Product product = productDao.getById(productId);
                    if (product == null)
                    {
                        continue;
                    }

                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(product);
                    item.setQuantity(quantity);

                    items.add(item);
                }
            }

            return items;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to load shopping cart for user ID: " + userId, ex);
        }
    }

    @Override
    public void addProductToCart(int userId, int productId) {
        String sql = """
                INSERT INTO shopping_cart (user_id, product_id, quantity)
                VALUES (?, ?, 1)
                ON DUPLICATE KEY UPDATE quantity = quantity + 1
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to add product to shopping cart", ex);
        }
    }

    @Override
    public void updateProductQuantity(int userId, int productId, int quantity) {
        String updateSql = """
                UPDATE shopping_cart
                SET quantity = ?
                WHERE user_id = ? 
                  AND product_id = ?
                """;

        String deleteSql = """
                DELETE FROM shopping_cart
                WHERE user_id = ? 
                  AND product_id = ?
                """;

        try (Connection connection = getConnection()) {
            if (quantity <= 0) {
                try (PreparedStatement statement = connection.prepareStatement(deleteSql)) {
                    statement.setInt(1, userId);
                    statement.setInt(2, productId);
                    statement.executeUpdate();
                }
            }else {
                try (PreparedStatement statement = connection.prepareStatement(updateSql)) {
                    statement.setInt(1, quantity);
                    statement.setInt(2, userId);
                    statement.setInt(3, productId);
                    statement.executeUpdate();
                }
            }
        }catch (SQLException ex) {
            throw new RuntimeException("Could not update quantity for product in cart", ex);
        }
    }

    @Override
    public ShoppingCartItem getItemByUserAndProduct(int userId, int productId) {
        String sql = """
                SELECT quantity
                FROM shopping_cart
                WHERE user_id = ? 
                  AND product_id = ?
                """;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, productId);

            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setQuantity(results.getInt("quantity"));
                    return item;
                }
            }

            return null;
        }catch (SQLException ex) {
            throw new RuntimeException("Failed to retrieve item from shopping cart", ex);
        }
    }

    @Override
    public void clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }catch (SQLException ex) {
            throw new RuntimeException("Unable to clear shopping cart for user", ex);
        }
    }
}