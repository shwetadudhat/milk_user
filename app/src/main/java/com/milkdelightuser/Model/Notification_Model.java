package com.milkdelightuser.Model;

public class Notification_Model {

    String notification_id,time,notification_desc,order_type,end_date;
    int notification_icon;

    public Notification_Model(String notification_id, String time, String notification_desc, int notification_icon) {
        this.notification_id = notification_id;
        this.time = time;
        this.notification_desc = notification_desc;
        this.notification_icon = notification_icon;
    }

    public Notification_Model() {

    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotification_desc() {
        return notification_desc;
    }

    public void setNotification_desc(String notification_desc) {
        this.notification_desc = notification_desc;
    }

    public int getNotification_icon() {
        return notification_icon;
    }

    public void setNotification_icon(int notification_icon) {
        this.notification_icon = notification_icon;
    }
}
