
package com.powerbtc.model.StackDeposit; ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;
    @SerializedName("remaining_balance")
    @Expose
    private String remainingBalance;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Amount")
    @Expose
    private String amount;

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(String remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
