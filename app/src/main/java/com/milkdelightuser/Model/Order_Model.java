package com.milkdelightuser.Model;

public class Order_Model {

    String order_id,offer_product,offer_qty,offer_pricee,offer_deliveryText;
    String order_icon,order_unit,subStatus;


    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getOrder_unit() {
        return order_unit;
    }

    public void setOrder_unit(String order_unit) {
        this.order_unit = order_unit;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOffer_product() {
        return offer_product;
    }

    public void setOffer_product(String offer_product) {
        this.offer_product = offer_product;
    }

    public String getOffer_qty() {
        return offer_qty;
    }

    public void setOffer_qty(String offer_qty) {
        this.offer_qty = offer_qty;
    }

    public String getOffer_pricee() {
        return offer_pricee;
    }

    public void setOffer_pricee(String offer_pricee) {
        this.offer_pricee = offer_pricee;
    }

    public String getOffer_deliveryText() {
        return offer_deliveryText;
    }

    public void setOffer_deliveryText(String offer_deliveryText) {
        this.offer_deliveryText = offer_deliveryText;
    }

    public String getOrder_icon() {
        return order_icon;
    }

    public void setOrder_icon(String order_icon) {
        this.order_icon = order_icon;
    }
}
