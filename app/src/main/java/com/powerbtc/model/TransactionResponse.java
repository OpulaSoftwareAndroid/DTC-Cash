package com.powerbtc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TransactionResponse implements Serializable {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("info")
    @Expose
    private ArrayList<Info> info;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<Info> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<Info> info) {
        this.info = info;
    }

    public class Info {

        @SerializedName("transaction_hash")
        @Expose
        private String transactionHash;
        @SerializedName("from_address")
        @Expose
        private String fromAddress;
        @SerializedName("to_address")
        @Expose
        private String toAddress;
        @SerializedName("transaction_date")
        @Expose
        private String transactionDate;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("confirm_date")
        @Expose
        private String confirmDate;
        @SerializedName("cancel_date")
        @Expose
        private String cancelDate;
        @SerializedName("usd_amount")
        @Expose
        private String usdAmount;
        @SerializedName("other_amount")
        @Expose
        private String otherAmount;
        @SerializedName("transaction_fee")
        @Expose
        private String transactionFee;
        @SerializedName("current_price")
        @Expose
        private String currentPrice;

        @SerializedName("Transaction_type")
        @Expose
        private String transactionType;

        @SerializedName("transaction_type")
        @Expose
        private String transactiontype;

        public String getTransactionHash() {
            return transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }

        public String getFromAddress() {
            return fromAddress;
        }

        public void setFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
        }

        public String getToAddress() {
            return toAddress;
        }

        public void setToAddress(String toAddress) {
            this.toAddress = toAddress;
        }

        public String getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(String transactionDate) {
            this.transactionDate = transactionDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getConfirmDate() {
            return confirmDate;
        }

        public void setConfirmDate(String confirmDate) {
            this.confirmDate = confirmDate;
        }

        public String getCancelDate() {
            return cancelDate;
        }

        public void setCancelDate(String cancelDate) {
            this.cancelDate = cancelDate;
        }

        public String getUsdAmount() {
            return usdAmount;
        }

        public void setUsdAmount(String usdAmount) {
            this.usdAmount = usdAmount;
        }

        public String getOtherAmount() {
            return otherAmount;
        }

        public void setOtherAmount(String otherAmount) {
            this.otherAmount = otherAmount;
        }

        public String getTransactionFee() {
            return transactionFee;
        }

        public void setTransactionFee(String transactionFee) {
            this.transactionFee = transactionFee;
        }

        public String getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(String currentPrice) {
            this.currentPrice = currentPrice;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }


        public String getTransactiontype() {
            return transactiontype;
        }

        public void setTransactiontype(String transactiontype) {
            this.transactiontype = transactiontype;
        }

    }
}
