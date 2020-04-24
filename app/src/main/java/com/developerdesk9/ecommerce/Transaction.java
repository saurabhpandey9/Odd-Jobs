package com.developerdesk9.ecommerce;

public class Transaction {

    String order_id;
    String order_status;
    String tr_amount;
    String tr_date;

    Transaction(){}

    public Transaction(String order_id, String order_status, String tr_amount, String tr_date) {
        this.order_id = order_id;
        this.order_status = order_status;
        this.tr_amount = tr_amount;
        this.tr_date = tr_date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getTr_amount() {
        return tr_amount;
    }

    public void setTr_amount(String tr_amount) {
        this.tr_amount = tr_amount;
    }

    public String getTr_date() {
        return tr_date;
    }

    public void setTr_date(String tr_date) {
        this.tr_date = tr_date;
    }
}
