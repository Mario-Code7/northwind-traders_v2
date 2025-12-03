package com.northwind.data;

import com.northwind.model.Product;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                SELECT productId, productName, unitsInStock
                FROM products
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
}
