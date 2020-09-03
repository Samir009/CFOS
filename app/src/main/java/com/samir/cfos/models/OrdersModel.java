package com.samir.cfos.models;

/**
 * created by SAMIR SHRESTHA on 2/5/2020  at 11:03 AM
 */

public class OrdersModel {
    private String orderd_food_name;
    private int orderd_food_qty;
    private double orderd_food_price;
    private int orderd_food_img;

    public OrdersModel(String orderd_food_name, int orderd_food_qty, double orderd_food_price, int orderd_food_img) {
        this.orderd_food_name = orderd_food_name;
        this.orderd_food_qty = orderd_food_qty;
        this.orderd_food_price = orderd_food_price;
        this.orderd_food_img = orderd_food_img;
    }

    public double getOrdered_food_total() {

        return orderd_food_qty*orderd_food_price;
    }


    public String getOrderd_food_name() {
        return orderd_food_name;
    }

    public void setOrderd_food_name(String orderd_food_name) {
        this.orderd_food_name = orderd_food_name;
    }

    public int getOrderd_food_qty() {
        return orderd_food_qty;
    }

    public void setOrderd_food_qty(int orderd_food_qty) {
        this.orderd_food_qty = orderd_food_qty;
    }

    public double getOrderd_food_price() {
        return orderd_food_price;
    }

    public void setOrderd_food_price(double orderd_food_price) {
        this.orderd_food_price = orderd_food_price;
    }

    public int getOrderd_food_img() {
        return orderd_food_img;
    }

    public void setOrderd_food_img(int orderd_food_img) {
        this.orderd_food_img = orderd_food_img;
    }

}
