package com.milkdelightuser.Model;

public class Plan_model {
    int id;
    String plans,days,description,skipDays;
    int flag=0;
    boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlans() {
        return plans;
    }

    public void setPlans(String plans) {
        this.plans = plans;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkipDays() {
        return skipDays;
    }

    public void setSkipDays(String skipDays) {
        this.skipDays = skipDays;
    }
}
