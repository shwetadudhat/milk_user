package com.milkdelightuser.Model;

public class App_Product_Model {
    String product_id;
    String category_id;
    String subcat_id;
    String product_name;
    String mrp;
    String price;
    String subscription_price;
    String membership_price;
    String qty;
    String product_image;
    String description;
    String stock;
    String unit;
    String home_show;
    String created_at;
    String updated_at;
    String rate;
    String review_count;
    String total;
    String gst;


    public App_Product_Model() {

    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getReview_count() {
        return review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public String getSubscription_price() {
        return subscription_price;
    }

    public void setSubscription_price(String subscription_price) {
        this.subscription_price = subscription_price;
    }


    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSubcat_id() {
        return subcat_id;
    }

    public void setSubcat_id(String subcat_id) {
        this.subcat_id = subcat_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getMembership_price() {
        return membership_price;
    }

    public void setMembership_price(String membership_price) {
        this.membership_price = membership_price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getHome_show() {
        return home_show;
    }

    public void setHome_show(String home_show) {
        this.home_show = home_show;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }


    @Override
    public String toString() {
        return "App_Product_Model{" +
                "product_id='" + product_id + '\'' +
                ", category_id='" + category_id + '\'' +
                ", subcat_id='" + subcat_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", mrp='" + mrp + '\'' +
                ", price='" + price + '\'' +
                ", subscription_price='" + subscription_price + '\'' +
                ", membership_price='" + membership_price + '\'' +
                ", qty='" + qty + '\'' +
                ", product_image='" + product_image + '\'' +
                ", description='" + description + '\'' +
                ", stock='" + stock + '\'' +
                ", unit='" + unit + '\'' +
                ", home_show='" + home_show + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", rate='" + rate + '\'' +
                ", review_count='" + review_count + '\'' +
                ", total='" + total + '\'' +
                ", gst='" + gst + '\'' +
                '}';
    }
}
