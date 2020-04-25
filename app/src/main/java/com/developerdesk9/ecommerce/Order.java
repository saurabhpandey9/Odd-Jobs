package com.developerdesk9.ecommerce;

public class Order {
    private String order_id;
    private String order_date;
    private String order_status;
    private String product_name;
    private String product_price;
    private String company_name;
    private String product_image;

    Order() {}

    public Order(String order_id, String order_date, String order_status, String product_name, String product_price, String company_name, String product_image) {
        this.order_id = order_id;
        this.order_date = order_date;
        this.order_status = order_status;
        this.product_name = product_name;
        this.product_price = product_price;
        this.company_name = company_name;
        this.product_image = product_image;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
}
