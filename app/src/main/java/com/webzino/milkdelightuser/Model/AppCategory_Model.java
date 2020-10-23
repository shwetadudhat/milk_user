package com.webzino.milkdelightuser.Model;

public class AppCategory_Model {
    String category_id, cityadmin_id,category_name,category_image,created_at,updated_at;
  //  int category_image;

   /* public AppCategory_Model(String category_id, String cityadmin_id, String category_name, String created_at, String updated_at, int category_image) {
        this.category_id = category_id;
        this.cityadmin_id = cityadmin_id;
        this.category_name = category_name;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.category_image = category_image;
    }*/

    public AppCategory_Model() {

    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCityadmin_id() {
        return cityadmin_id;
    }

    public void setCityadmin_id(String cityadmin_id) {
        this.cityadmin_id = cityadmin_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
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
}
