package com.webzino.milkdelightuser.Model;

public class Recharge_Model {

    String wallet_recharge_history_id,user_id,amount,original_amount,transaction_id,amount_status,pay_type,pay_mode,recharge_status,date_of_recharge,created_at,updated_at;
    boolean isshow;
    public boolean isIsshow() {
        return isshow;
    }

    public void setIsshow(boolean isshow) {
        this.isshow = isshow;
    }


    public String getOriginal_amount() {
        return original_amount;
    }

    public void setOriginal_amount(String original_amount) {
        this.original_amount = original_amount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getAmount_status() {
        return amount_status;
    }

    public void setAmount_status(String amount_status) {
        this.amount_status = amount_status;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_mode() {
        return pay_mode;
    }

    public void setPay_mode(String pay_mode) {
        this.pay_mode = pay_mode;
    }

    public String getRecharge_status() {
        return recharge_status;
    }

    public void setRecharge_status(String recharge_status) {
        this.recharge_status = recharge_status;
    }

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

    public String getDate_of_recharge() {
        return date_of_recharge;
    }

    public void setDate_of_recharge(String date_of_recharge) {
        this.date_of_recharge = date_of_recharge;
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
