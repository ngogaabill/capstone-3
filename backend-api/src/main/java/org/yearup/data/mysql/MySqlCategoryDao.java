package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        String sql = """
                SELECT * FROM categories
                """;
        List<Category> categories = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery();) {

            while (resultSet.next()) {
                categories.add(mapRow(resultSet));// helper
            }
            return categories;
        } catch (SQLException e) {
            System.err.println("Error returning a category");
            return null;
        }
    }

    @Override
    public Category getById(int categoryId) {
        // get category by id
        String sql = """
                SELECT * FROM categories
                WHERE category_id = ?;
                """;
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRow(resultSet); // reuse helper
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Can't Find Category With: " + categoryId);
            return null;
        }
    }

    @Override
    public Category create(Category category) {

        String sql = "INSERT INTO categories(name, description) " +
                " VALUES (?, ?);";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int orderId = generatedKeys.getInt(1);

                    // get the newly inserted category
                    return getById(orderId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category) {
        String sql = """ 
                UPDATE categories
                SET name = ? , description = ?
                WHERE category_id = ?;
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId) {
        String sql = "DELETE FROM categories " +
                " WHERE category_id = ?;";

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setInt(1, categoryId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
