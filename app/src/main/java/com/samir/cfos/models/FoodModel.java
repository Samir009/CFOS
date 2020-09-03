package com.samir.cfos.models;

public class FoodModel {
    private String name;
    private String price;
    private String imageUrl;
    private String DocId;
//    private int qty;
//    private int img;

    public FoodModel(){
//  empty constructor needed.
    }

    public FoodModel(String id, String name, String price, String ImgUrl) {

        if(name.trim().equals("") | price.trim().equals("")){
            name = "No Name";
            price = "No price";
        }
        this.name = name;
        this.price = price;
        this.imageUrl = ImgUrl;
        this.DocId = id;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String drinksName) {
        this.name = drinksName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

//    public int getQty() {
//        return qty;
//    }
//
//    public void setQty(int qty) {
//        this.qty = qty;
//    }
//
//    public int getImg() {
//        return img;
//    }
//
//    public void setImg(int img) {
//        this.img = img;
//    }
}
