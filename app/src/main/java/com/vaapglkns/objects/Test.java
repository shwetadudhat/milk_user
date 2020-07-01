package com.vaapglkns.objects;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Test {

    @SerializedName("array")
    @Expose
    public List<Array> array = new ArrayList<Array>();

    public static class Array {
        @SerializedName("vid")
        @Expose
        public String vid = "";
        @SerializedName("commentId")
        @Expose
        public String commentId = "";
        @SerializedName("userId")
        @Expose
        public String userId = "";
        @SerializedName("content")
        @Expose
        public String content = "";
        @SerializedName("parent")
        @Expose
        public String parent = "";
        @SerializedName("parent2")
        @Expose
        public String parent2 = "";

        public boolean replayClicked = false;
    }
}
