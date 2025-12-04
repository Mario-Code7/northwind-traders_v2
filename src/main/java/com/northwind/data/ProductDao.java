package com.northwind.data;

import com.northwind.model.Product;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    private DataSource dataSource;

    public ProductDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String query =  """
            select ProductID, ProductName, SupplierID, CategoryID, QuantityPerUnit, UnitPrice, UnitsInStock, UnitsOnOrder, ReorderLevel, Discontinued
            from products
            """;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            try(ResultSet resultSet = statement.executeQuery()) {

                while(resultSet.next()) {
                    Product product = new Product(
                            resultSet.getInt("ProductID"),
                            resultSet.getString("ProductName"),
                            resultSet.getInt("SupplierID"),
                            resultSet.getInt("CategoryID"),
                            resultSet.getString("QuantityPerUnit"),
                            resultSet.getDouble("UnitPrice"),
                            resultSet.getInt("UnitsInStock"),
                            resultSet.getInt("UnitsOnOrder"),
                            resultSet.getInt("ReorderLevel"),
                            resultSet.getInt("Discontinued"));

                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();
        }
        return products;
    }

    public Product find(int productId) {
        Product product = null;
        String query = """
            select ProductID, ProductName, SupplierID, CategoryID, QuantityPerUnit, UnitPrice, UnitsInStock, UnitsOnOrder, ReorderLevel, Discontinued
            from products
            WHERE productId = ?
                """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, productId);

                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        product = new Product (
                                resultSet.getInt("ProductID"),
                                resultSet.getString("ProductName"),
                                resultSet.getInt("SupplierID"),
                                resultSet.getInt("CategoryID"),
                                resultSet.getString("QuantityPerUnit"),
                                resultSet.getDouble("UnitPrice"),
                                resultSet.getInt("UnitsInStock"),
                                resultSet.getInt("UnitsOnOrder"),
                                resultSet.getInt("ReorderLevel"),
                                resultSet.getInt("Discontinued"));
                    }
                }
        } catch(SQLException e) {
            System.out.println("There is an error retrieving the data. Please try again!");
            e.printStackTrace();
        }
        return product;
    }

    public Product add(Product product) {
        String query = """
                INSERT INTO products(ProductName, SupplierID, CategoryID, QuantityPerUnit, UnitPrice, UnitsInStock, UnitsOnOrder, ReorderLevel, Discontinued)
                values(?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setInt(2, product.getSupplierId());
            preparedStatement.setInt(3, product.getCategoryId());
            preparedStatement.setString(4, product.getQuantityPerUnit());
            preparedStatement.setDouble(5, product.getUnitPrice());
            preparedStatement.setInt(6, product.getUnitsInStock());
            preparedStatement.setInt(7, product.getUnitsOnOrder());
            preparedStatement.setInt(8, product.getReorderLevel());
            preparedStatement.setInt(9, product.getDiscontinued());

            preparedStatement.executeUpdate();

            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    product.setProductId(generatedId);
                }
            }
        } catch(SQLException e) {
            System.out.println("There is an error retrieving the data. Please try again!");
            e.printStackTrace();
        }
        return product;
    }

    public void update(Product product) {
        String query = """
                UPDATE Products
                SET ProductName = ?, UnitPrice = ?
                WHERE ProductID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, product.getProductName());
            statement.setDouble(2, product.getUnitPrice());
            statement.setInt(3, product.getProductId());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error updating the shipper. Please try again.");
            e.printStackTrace();
        }
    }

    public void delete(int productId) {
        String query = """
                DELETE FROM Products
                WHERE ProductID = ?;
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, productId);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("There was an error deleting the shipper. Please try again.");
            e.printStackTrace();
        }
    }
}
