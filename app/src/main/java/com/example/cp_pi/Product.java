package com.example.cp_pi;

public class Product {
    int id;
    String name;
    double price;
    String image;
    int sellerId;

    public Product(int id, String name, double price, String image, int sellerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.sellerId = sellerId;
    }

    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }
}