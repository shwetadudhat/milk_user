package com.vaapglkns.objects;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LoginRes {


    @SerializedName("result")
    @Expose
    public List<Result> result = new ArrayList<Result>();

    public class Result {

        @SerializedName("res")
        @Expose
        public String res;

    }

}
