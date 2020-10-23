package com.webzino.milkdelightuser.Model;

public class Rate_Model {

    String rate_id,username,rating_star,rate_desc,created_at;
    String user_image;



    public Rate_Model() {

    }

    public String getRate_id() {
        return rate_id;
    }

    public void setRate_id(String rate_id) {
        this.rate_id = rate_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRating_star() {
        return rating_star;
    }

    public void setRating_star(String rating_star) {
        this.rating_star = rating_star;
    }

    public String getRate_desc() {
        return rate_desc;
    }

    public void setRate_desc(String rate_desc) {
        this.rate_desc = rate_desc;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
