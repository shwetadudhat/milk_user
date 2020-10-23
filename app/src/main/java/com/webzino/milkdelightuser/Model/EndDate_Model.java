package com.webzino.milkdelightuser.Model;

public class EndDate_Model {
    String prodcut_id,end_date,total_days;
    int product_totalPrice;


    public int getProduct_totalPrice() {
        return product_totalPrice;
    }

    public void setProduct_totalPrice(int product_totalPrice) {
        this.product_totalPrice = product_totalPrice;
    }

    public String getTotal_days() {
        return total_days;
    }

    public void setTotal_days(String total_days) {
        this.total_days = total_days;
    }

    public String getProdcut_id() {
        return prodcut_id;
    }

    public void setProdcut_id(String prodcut_id) {
        this.prodcut_id = prodcut_id;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    @Override
    public String toString() {
        return "EndDate_Model{" +
                "prodcut_id='" + prodcut_id + '\'' +
                ", end_date='" + end_date + '\'' +
                ", total_days='" + total_days + '\'' +
                ", product_totalPrice='" + product_totalPrice + '\'' +
                '}';
    }
}
