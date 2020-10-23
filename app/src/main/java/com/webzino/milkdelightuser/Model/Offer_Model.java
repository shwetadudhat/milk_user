package com.webzino.milkdelightuser.Model;

public class Offer_Model {

    String oofer_id,offer_desc,offer_detaildesc,offer_expireTime;
    String offer_icon;

    public Offer_Model(String oofer_id, String offer_desc, String offer_detaildesc, String offer_expireTime, String offer_icon) {
        this.oofer_id = oofer_id;
        this.offer_desc = offer_desc;
        this.offer_detaildesc = offer_detaildesc;
        this.offer_expireTime = offer_expireTime;
        this.offer_icon = offer_icon;
    }

    public String getOofer_id() {
        return oofer_id;
    }

    public void setOofer_id(String oofer_id) {
        this.oofer_id = oofer_id;
    }

    public String getOffer_desc() {
        return offer_desc;
    }

    public void setOffer_desc(String offer_desc) {
        this.offer_desc = offer_desc;
    }

    public String getOffer_detaildesc() {
        return offer_detaildesc;
    }

    public void setOffer_detaildesc(String offer_detaildesc) {
        this.offer_detaildesc = offer_detaildesc;
    }

    public String getOffer_expireTime() {
        return offer_expireTime;
    }

    public void setOffer_expireTime(String offer_expireTime) {
        this.offer_expireTime = offer_expireTime;
    }

    public String getOffer_icon() {
        return offer_icon;
    }

    public void setOffer_icon(String offer_icon) {
        this.offer_icon = offer_icon;
    }
}
