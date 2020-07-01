package com.vaapglkns.utils;

public class URLs {


    private static String MAIN_URL = "http://irepo.in/clickkart/";

    public static String GET_CHALLAN() {
        return MAIN_URL + "/get_vehicle_data.json";
    }

    public static String GET_COLLECTION_CENTER() {
        return MAIN_URL + "/get_collection_center_data.json";
    }

    public static String SIGN_UP() {
        return MAIN_URL + "/registration_save.php";
    }

    public static String LOG_IN() {
        return MAIN_URL + "/signin_check.php";
    }


}
