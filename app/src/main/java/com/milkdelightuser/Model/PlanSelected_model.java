package com.milkdelightuser.Model;

public class PlanSelected_model {
    String id,product_id,start_date,plan_id,skip_day,end_date,product_name;


    public String getProduct_name() {
        return product_name;
    }
    @Override
    public boolean equals(Object obj) {
        return !super.equals(obj);
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getSkip_day() {
        return skip_day;
    }

    public void setSkip_day(String skip_day) {
        this.skip_day = skip_day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    @Override
    public String toString() {
        return "PlanSelected_model{" +
                "id='" + id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", start_date='" + start_date + '\'' +
                ", plan_id='" + plan_id + '\'' +
                ", skip_day='" + skip_day + '\'' +
                ", end_date='" + end_date + '\'' +
                '}';
    }
}
