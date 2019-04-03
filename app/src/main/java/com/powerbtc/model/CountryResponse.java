package com.powerbtc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CountryResponse {
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

        @SerializedName("country_id")
        @Expose
        private String countryId;
        @SerializedName("country_name")
        @Expose
        private String countryName;
        @SerializedName("country_status")
        @Expose
        private String countryStatus;
        @SerializedName("country_date")
        @Expose
        private String countryDate;
        @SerializedName("country_flag")
        @Expose
        private String countryFlag;
        @SerializedName("country_code")
        @Expose
        private String countryCode;

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getCountryStatus() {
            return countryStatus;
        }

        public void setCountryStatus(String countryStatus) {
            this.countryStatus = countryStatus;
        }

        public String getCountryDate() {
            return countryDate;
        }

        public void setCountryDate(String countryDate) {
            this.countryDate = countryDate;
        }

        public String getCountryFlag() {
            return countryFlag;
        }

        public void setCountryFlag(String countryFlag) {
            this.countryFlag = countryFlag;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

    }
}
