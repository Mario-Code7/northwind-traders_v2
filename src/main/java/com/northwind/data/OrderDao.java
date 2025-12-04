package com.northwind.data;

import com.northwind.model.Order;
import com.northwind.model.Product;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    private DataSource dataSource;

    public OrderDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String query =  """
            select OrderId, CustomerID, EmployeeID, OrderDate, RequiredDate, ShippedDate, ShipVia, Freight, ShipName, ShipAddress, ShipCity, ShipRegion, ShipPostalCode, ShipCountry
            from orders
            """;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            try(ResultSet resultSet = statement.executeQuery()) {

                while(resultSet.next()) {

                    LocalDateTime orderDate = null;
                    Timestamp tsOrder = resultSet.getTimestamp("OrderDate");
                    if (tsOrder != null) orderDate = tsOrder.toLocalDateTime();

                    LocalDateTime requiredDate = null;
                    Timestamp tsRequired = resultSet.getTimestamp("RequiredDate");
                    if (tsRequired != null) requiredDate = tsRequired.toLocalDateTime();

                    LocalDateTime shippedDate = null;
                    Timestamp tsShipped = resultSet.getTimestamp("ShippedDate");
                    if (tsShipped != null) shippedDate = tsShipped.toLocalDateTime();

                    Order order = new Order(
                            resultSet.getInt("OrderID"),
                            resultSet.getString("CustomerID"),
                            resultSet.getInt("EmployeeID"),
                            orderDate,
                            requiredDate,
                            shippedDate,
                            resultSet.getInt("ShipVia"),
                            resultSet.getDouble("Freight"),
                            resultSet.getString("ShipName"),
                            resultSet.getString("ShipAddress"),
                            resultSet.getString("ShipCity"),
                            resultSet.getString("ShipRegion"),
                            resultSet.getString("ShipPostalCode"),
                            resultSet.getString("ShipCountry")

                    );

                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            System.out.println("There was an error retrieving the data. Please try again.");
            e.printStackTrace();
        }
        return orders;
    }

    public Order find(String customerId) {
        Order order = null;
        String query = """
            select OrderId, CustomerID, EmployeeID, OrderDate, RequiredDate, ShippedDate, ShipVia, Freight, ShipName, ShipAddress, ShipCity, ShipRegion, ShipPostalCode, ShipCountry
            from orders
            WHERE customerId = ?
            """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, customerId);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    LocalDateTime orderDate = null;
                    Timestamp tsOrder = resultSet.getTimestamp("OrderDate");
                    if (tsOrder != null) orderDate = tsOrder.toLocalDateTime();

                    LocalDateTime requiredDate = null;
                    Timestamp tsRequired = resultSet.getTimestamp("RequiredDate");
                    if (tsRequired != null) requiredDate = tsRequired.toLocalDateTime();

                    LocalDateTime shippedDate = null;
                    Timestamp tsShipped = resultSet.getTimestamp("ShippedDate");
                    if (tsShipped != null) shippedDate = tsShipped.toLocalDateTime();
                    order = new Order(
                        resultSet.getInt("OrderID"),
                        resultSet.getString("CustomerID"),
                        resultSet.getInt("EmployeeID"),
                        orderDate,
                        requiredDate,
                        shippedDate,
                        resultSet.getInt("ShipVia"),
                        resultSet.getDouble("Freight"),
                        resultSet.getString("ShipName"),
                        resultSet.getString("ShipAddress"),
                        resultSet.getString("ShipCity"),
                        resultSet.getString("ShipRegion"),
                        resultSet.getString("ShipPostalCode"),
                        resultSet.getString("ShipCountry"));
                }
            }
        } catch(SQLException e) {
            System.out.println("There is an error retrieving the data. Please try again!");
            e.printStackTrace();
        }
        return order;
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
