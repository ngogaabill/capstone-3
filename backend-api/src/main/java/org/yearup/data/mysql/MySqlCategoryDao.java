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
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                int categoryId = resultSet.getInt("category_id");

                Category c = new Category();
                c.setCategoryId(categoryId);
                c.setName(name);
                c.setDescription(description);

                categories.add(c);
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
            resultSet.next();
            return new Category(resultSet.getInt("category_id"), resultSet.getString("name"), resultSet.getString("description"));

        } catch (SQLException e) {
            System.err.println("Can't Find Category With: " + categoryId);
        }
        return null;
    }

    @Override
    public Category create(Category category) {
        // create a new category
        return null;
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
    }

    @Override
    public void delete(int categoryId) {
        // delete category
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
