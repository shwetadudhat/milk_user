package com.milkdelightuser.Model;

public class Credit_Model {

    String wallet_recharge_history_id , user_id , amount , recharge_status , date_of_recharge;

    public String getWallet_recharge_history_id() {
        return wallet_recharge_history_id;
    }

    public void setWallet_recharge_history_id(String wallet_recharge_history_id) {
        this.wallet_recharge_history_id = wallet_recharge_history_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRecharge_status() {
        return recharge_status;
    }

    public void setRecharge_status(String recharge_status) {
        this.recharge_status = recharge_status;
    }

    public String getDate_of_recharge() {
        return date_of_recharge;
    }

    public void setDate_of_recharge(String date_of_recharge) {
        this.date_of_recharge = date_of_recharge;
    }
}
