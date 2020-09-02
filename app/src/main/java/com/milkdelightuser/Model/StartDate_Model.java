package com.milkdelightuser.Model;

public class StartDate_Model {
    String prodcut_id,start_date,product_price,product_qty;

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProdcut_id() {
        return prodcut_id;
    }

    public void setProdcut_id(String prodcut_id) {
        this.prodcut_id = prodcut_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    @Override
    public String toString() {
        return "StartDate_Model{" +
                "prodcut_id='" + prodcut_id + '\'' +
                ", start_date='" + start_date + '\'' +
                ", product_price='" + product_price + '\'' +
                ", product_qty='" + product_qty + '\'' +
                '}';
    }
}
