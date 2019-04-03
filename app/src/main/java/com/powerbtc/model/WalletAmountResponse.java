package com.powerbtc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sai on 5/4/2017.
 */

public class WalletAmountResponse {
    @SerializedName("status")
    @Expose
    private Integer success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("info")
    @Expose
    private Info info;

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

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public class Info {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("register_id")
        @Expose
        private String registerId;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("coin_address")
        @Expose
        private String coinAddress;
        @SerializedName("withdraw_wallet")
        @Expose
        private String withdrawWallet;
        @SerializedName("35_locking_wallet")
        @Expose
        private String _35LockingWallet;
        @SerializedName("Fees")
        @Expose
        private String fees;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRegisterId() {
            return registerId;
        }

        public void setRegisterId(String registerId) {
            this.registerId = registerId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCoinAddress() {
            return coinAddress;
        }

        public void setCoinAddress(String coinAddress) {
            this.coinAddress = coinAddress;
        }

        public String getWithdrawWallet() {
            return withdrawWallet;
        }

        public void setWithdrawWallet(String withdrawWallet) {
            this.withdrawWallet = withdrawWallet;
        }

        public String get35LockingWallet() {
            return _35LockingWallet;
        }

        public void set35LockingWallet(String _35LockingWallet) {
            this._35LockingWallet = _35LockingWallet;
        }

        public String getFees() {
            return fees;
        }

        public void setFees(String fees) {
            this.fees = fees;
        }

    }
}
