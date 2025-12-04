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

        System.out.println("--Find a Product by ID--");
        Product findProduct = productDao.find(1);
        if (findProduct != null) {
            System.out.println("Found: " + findProduct);
        } else {
            System.out.println("Product with ID 1 not found!");
        }

        System.out.println("--Add new product-- ");
        Product newProduct = new Product(0,
                "Tasty Cinnamon Toast",
                2,
                2,
                "25 boxes",
                27.00,
                19,
                7,
                7,
                0
        );
        Product addedProduct = productDao.add(newProduct);
        System.out.println("Added: " + addedProduct);

        System.out.println("\n--- Test 4: Verify Customer Was Added ---");
        Product verifyProduct = productDao.find(addedProduct.getProductId());
        if (verifyProduct != null) {
            System.out.println("Verified: " + verifyProduct);
        } else {
            System.out.println("Product id not found after adding.");
        }

        System.out.println("--Update Product--");
        if (verifyProduct != null) {
            verifyProduct.setProductName("Tasty Cinnamon Cereal");
            verifyProduct.setUnitPrice(30.00);
            productDao.update(verifyProduct);
            System.out.println("Updated product");

            Product updatedProduct = productDao.find(verifyProduct.getProductId());
            System.out.println("After update: " + updatedProduct);
        }

//        // Test 6: Delete the customer
//        System.out.println("\n--- Test 6: Delete Product ---");
//        productDao.delete(addedProduct.getProductId());
//        System.out.println("Deleted productId(s)");
//
//        Product deletedProduct = productDao.find(addedProduct.getProductId());
//        if (deletedProduct == null) {
//            System.out.println("Confirmed: Product(s) no longer exists.");
//        } else {
//            System.out.println("Warning: Product(s) still exists after deletion.");
//        }
//    }
    }
}
