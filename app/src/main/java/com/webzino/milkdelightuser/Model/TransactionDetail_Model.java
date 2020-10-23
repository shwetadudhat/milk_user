package com.webzino.milkdelightuser.Model;

public class TransactionDetail_Model {
    String id,type,nmbr,time,price;

    public TransactionDetail_Model(String id, String type, String nmbr, String time, String price) {
        this.id = id;
        this.type = type;
        this.nmbr = nmbr;
        this.time = time;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNmbr() {
        return nmbr;
    }

    public void setNmbr(String nmbr) {
        this.nmbr = nmbr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
