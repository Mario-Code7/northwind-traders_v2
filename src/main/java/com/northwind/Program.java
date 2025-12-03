package com.northwind;

import com.northwind.data.ProductDao;
import com.northwind.model.Product;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.List;

public class Program {
    public static void main(String[] args) {
        String username = args[0];
        String password = args[1];
        String url = "jdbc:mysql://localhost:3306/northwind";

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);

        System.out.println("=============================");
        System.out.println("===== Test ProductDao");
        System.out.println("=============================");

        ProductDao productDao = new ProductDao(dataSource);

        System.out.println("--Get all products test--");
        List<Product> products = productDao.getAll();
        System.out.println("All Products: " + products.size());
        for (Product product : products) {
            System.out.println(product);
        }

        System.out.println("--Find a ProductID by ID--");
        Product findProduct = productDao.find(1);
        if (findProduct != null) {
            System.out.println("Found: " + findProduct);
        } else {
            System.out.println("Product with ID 1 not found!");
        }

    }
}
