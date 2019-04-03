package com.powerbtc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sai on 5/3/2017.
 */

public class LoginResponse {
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
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email_id")
        @Expose
        private String emailId;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("mobile_no")
        @Expose
        private String mobileNo;
        @SerializedName("register_id")
        @Expose
        private String registerId;
        @SerializedName("login_status")
        @Expose
        private String loginStatus;
        @SerializedName("secret_key")
        @Expose
        private String secretKey;
        @SerializedName("2fa_status")
        @Expose
        private String _2faStatus;
        @SerializedName("email_verification_status")
        @Expose
        private String emailVerificationStatus;
        @SerializedName("verify_date")
        @Expose
        private String verifyDate;
        @SerializedName("register_date")
        @Expose
        private String registerDate;
        @SerializedName("active_date")
        @Expose
        private String activeDate;
        @SerializedName("delete_status")
        @Expose
        private String deleteStatus;
        @SerializedName("last_updated_date")
        @Expose
        private String lastUpdatedDate;
        @SerializedName("resident_address")
        @Expose
        private String residentAddress;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("pincode")
        @Expose
        private String pincode;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("country_code")
        @Expose
        private String countryCode;
        @SerializedName("country_name")
        @Expose
        private String countryName;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("Profile")
        @Expose
        private String profile;
        @SerializedName("transaction_password")
        @Expose
        private String transaction_password;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getRegisterId() {
            return registerId;
        }

        public void setRegisterId(String registerId) {
            this.registerId = registerId;
        }

        public String getLoginStatus() {
            return loginStatus;
        }

        public void setLoginStatus(String loginStatus) {
            this.loginStatus = loginStatus;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String get2faStatus() {
            return _2faStatus;
        }

        public void set2faStatus(String _2faStatus) {
            this._2faStatus = _2faStatus;
        }

        public String getEmailVerificationStatus() {
            return emailVerificationStatus;
        }

        public void setEmailVerificationStatus(String emailVerificationStatus) {
            this.emailVerificationStatus = emailVerificationStatus;
        }

        public String getVerifyDate() {
            return verifyDate;
        }

        public void setVerifyDate(String verifyDate) {
            this.verifyDate = verifyDate;
        }

        public String getRegisterDate() {
            return registerDate;
        }

        public void setRegisterDate(String registerDate) {
            this.registerDate = registerDate;
        }

        public String getActiveDate() {
            return activeDate;
        }

        public void setActiveDate(String activeDate) {
            this.activeDate = activeDate;
        }

        public String getDeleteStatus() {
            return deleteStatus;
        }

        public void setDeleteStatus(String deleteStatus) {
            this.deleteStatus = deleteStatus;
        }

        public String getLastUpdatedDate() {
            return lastUpdatedDate;
        }

        public void setLastUpdatedDate(String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }

        public String getResidentAddress() {
            return residentAddress;
        }

        public void setResidentAddress(String residentAddress) {
            this.residentAddress = residentAddress;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getTransaction_password() {
            return transaction_password;
        }

        public void setTransaction_password(String transaction_password) {
            this.transaction_password = transaction_password;
        }

    }
}
