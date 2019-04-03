
package com.powerbtc.model.StackDistribution; ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("InvestmentAmount")
    @Expose
    private String investmentAmount;
    @SerializedName("Percentage")
    @Expose
    private String percentage;
    @SerializedName("TotalDistribution")
    @Expose
    private String totalDistribution;
    @SerializedName("35Amount")
    @Expose
    private String _35Amount;
    @SerializedName("65Amount")
    @Expose
    private String _65Amount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(String investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getTotalDistribution() {
        return totalDistribution;
    }

    public void setTotalDistribution(String totalDistribution) {
        this.totalDistribution = totalDistribution;
    }

    public String get35Amount() {
        return _35Amount;
    }

    public void set35Amount(String _35Amount) {
        this._35Amount = _35Amount;
    }

    public String get65Amount() {
        return _65Amount;
    }

    public void set65Amount(String _65Amount) {
        this._65Amount = _65Amount;
    }

}
