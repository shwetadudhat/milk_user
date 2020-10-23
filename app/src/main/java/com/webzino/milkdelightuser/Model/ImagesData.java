package com.webzino.milkdelightuser.Model;

public class ImagesData {
  public  int url;
  public String id;

    public ImagesData(int url, String id) {
        this.url = url;
        this.id = id;
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
