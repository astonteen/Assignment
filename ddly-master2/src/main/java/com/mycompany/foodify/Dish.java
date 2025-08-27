package com.mycompany.foodify;

public class Dish {
    private int id;
    private String name;
    private String category;
    private double price;
    private double rating;
    private String photoAddress;
    private String userId;

    public Dish(int id, String name, String category, double price, double rating, String photoAddress, String userId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.photoAddress = photoAddress;
        this.userId = userId;
    }

    public Dish() {
        this.id = 0;
        this.name = "";
        this.category = "";
        this.price = 0.0;
        this.rating = 0.0;
        this.photoAddress = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }
        this.rating = rating;
    }

    public String getPhotoAddress() {
        return photoAddress;
    }

    public void setPhotoAddress(String photoAddress) {
        this.photoAddress = photoAddress;
    }
    
    public String getUserId(){
        return userId;
    }
    
    public void setUserId(String userId){
        this.userId = userId;
    }

    @Override
    public String toString() {
    return "Dish{user=" + userId + ", id=" + id + ", name=" + name + ", category=" + category +
           ", price=" + price + ", rating=" + rating + ", photoAddress=" + photoAddress + "}";
}

    public String toFileString() {
        return id + "," + name + "," + category + "," + price + "," + rating + "," + photoAddress + "," + userId;
    }
    
    public Dish(String csvLine) {
        String[] data = csvLine.split(",");
        if (data.length == 7) {
            this.id = Integer.parseInt(data[0]);
            this.name = data[1];
            this.category = data[2];
            this.price = Double.parseDouble(data[3]);
            this.rating = Double.parseDouble(data[4]);
            this.photoAddress = data[5];
            this.userId = data[6];
        }
    }
    
    
}
