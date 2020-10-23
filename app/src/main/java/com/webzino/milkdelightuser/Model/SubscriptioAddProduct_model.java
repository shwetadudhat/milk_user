package com.webzino.milkdelightuser.Model;

import java.io.Serializable;

public class SubscriptioAddProduct_model implements Serializable {
    String product_id,product_name,product_qty,product_oldprice,product_unit,start_date,end_date,skip_days,plan_id,address_id;
    int product_price,product_gst,product_sgst,product_totalprice;
    String Image;

    public int getProduct_totalprice() {
        return product_totalprice;
    }

    public void setProduct_totalprice(int product_totalprice) {
        this.product_totalprice = product_totalprice;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public String getProduct_oldprice() {
        return product_oldprice;
    }

    public void setProduct_oldprice(String product_oldprice) {
        this.product_oldprice = product_oldprice;
    }

    public String getProduct_unit() {
        return product_unit;
    }

    public void setProduct_unit(String product_unit) {
        this.product_unit = product_unit;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getSkip_days() {
        return skip_days;
    }

    public void setSkip_days(String skip_days) {
        this.skip_days = skip_days;
    }

    public int getProduct_gst() {
        return product_gst;
    }

    public void setProduct_gst(int product_gst) {
        this.product_gst = product_gst;
    }

    public int getProduct_sgst() {
        return product_sgst;
    }

    public void setProduct_sgst(int product_sgst) {
        this.product_sgst = product_sgst;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    @Override
    public String toString() {
        return "SubscriptioAddProduct_model{" +
                "product_id='" + product_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_qty='" + product_qty + '\'' +
                ", product_price='" + product_price + '\'' +
                ", product_oldprice='" + product_oldprice + '\'' +
                ", product_totalprice='" + product_totalprice + '\'' +
                ", product_unit='" + product_unit + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", skip_days='" + skip_days + '\'' +
                ", product_gst='" + product_gst + '\'' +
                ", product_sgst='" + product_sgst + '\'' +
                ", address_id='" + address_id + '\'' +
                ", plan_id='" + plan_id + '\'' +
                '}';
    }
}
