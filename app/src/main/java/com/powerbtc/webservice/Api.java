package com.powerbtc.webservice;



import com.powerbtc.model.CommonResponce;
import com.powerbtc.model.CommonResponseWithStatus;
import com.powerbtc.model.CommonStatusResponce;
import com.powerbtc.model.CountryResponse;
import com.powerbtc.model.GetReplyTicketResponse;
import com.powerbtc.model.LoginResponse;
import com.powerbtc.model.Signup;
import com.powerbtc.model.StackDeposit.StackDeposit;
import com.powerbtc.model.StackDistribution.StackDistribution;
import com.powerbtc.model.TicketHistoryResponse;
import com.powerbtc.model.TransactionResponse;
import com.powerbtc.model.WalletAmountResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

@SuppressWarnings("ALL")
public interface Api {

    @POST("CountryList.php")
    Call<CountryResponse> getCountry();

    @POST("UserRegister.php")
    Call<Signup> UserRegisterNew(@Body Map<String, String> postSellparams);

    @POST("ResendMail.php")
    Call<CommonStatusResponce> resendLink(@Body Map<String, String> postSellparams);

    @POST("UserLogin.php")
    Call<LoginResponse> UserLoginNew(@Body Map<String, String> postSellparams);

    @POST("ForgotPassword.php")
    Call<CommonStatusResponce> ResetPassword(@Body Map<String, String> postSellparams);

    @POST("WalletAmount.php")
    Call<WalletAmountResponse> WalletLiveAmount(@Body Map<String, String> postBuyparams);

    @POST("WithdrawOtherTransaction.php")
    Call<CommonResponseWithStatus> WithdrawOtherTransaction(@Body Map<String, String> postBuyparams);

    @Multipart
    @POST("UpdateProfile.php")
    Call<CommonStatusResponce> updateProfile(@Part MultipartBody.Part image,
                                       @Part("ProfilePicture") RequestBody filename,
                                       @Part("RegisterId") RequestBody register_id,
                                       @Part("FullName") RequestBody name,
                                       @Part("MobileNo") RequestBody mobile,
                                       @Part("ValidData") RequestBody validData);

    @POST("UpdateContact.php")
    Call<CommonStatusResponce> updateContactInfo(@Body Map<String, String> params);

    @POST("ChangePassword.php")
    Call<CommonStatusResponce> changePassword(@Body Map<String, String> postSellparams);

    @POST("ChangeTransactionPassword.php")
    Call<CommonStatusResponce> changeTransactionPassword(@Body Map<String, String> postSellparams);

    @POST("TicketHistory.php")
    Call<TicketHistoryResponse> getTicketHistory(@Body Map<String, String> params);


    @POST("StakeDepositHistory.php")
    Call<StackDeposit> getStakeDepositHistory(@Body Map<String, String> params);

    @POST("StakeDistributionHistory.php")
    Call<StackDistribution> getStakeDistributionHistory(@Body Map<String, String> params);


    @POST("AllTicketHistory.php")
    Call<GetReplyTicketResponse> getSingleTicketHistory(@Body Map<String, String> params);

    @Multipart
    @POST("CreateTicket.php")
    Call<CommonStatusResponce> createTicket(@Part MultipartBody.Part image,
                                      @Part("ProfilePicture") RequestBody ProfilePicture,
                                      @Part("RegisterId") RequestBody register_id,
                                      @Part("Subject") RequestBody subject,
                                      @Part("Message") RequestBody message,
                                      @Part("ValidData") RequestBody validData);

    @Multipart
    @POST("ReplyTicket.php")
    Call<CommonStatusResponce> replyTicket(@Part MultipartBody.Part image,
                                     @Part("ProfilePicture") RequestBody subject,
                                     @Part("RegisterId") RequestBody register_id,
                                     @Part("TicketNumber") RequestBody tck_number,
                                     @Part("Message") RequestBody message,
                                     @Part("ValidData") RequestBody validData);

    @POST("TransactionHistory.php")
    Call<TransactionResponse> getHistory(@Body Map<String, String> postSellparams);

    @POST("AndroidVersion.php")
    Call<CommonStatusResponce> getVersion();

    @POST("ReceiveOtherTransaction.php")
    Call<CommonStatusResponce> depositData(@Body Map<String, String> postBuyparams);

    @POST("SendOtherTransactionVerification.php")
    Call<CommonStatusResponce> soSend(@Body Map<String, String> postSellparams);

    @POST("SendOtherTransaction.php")
    Call<CommonStatusResponce> soSendwithotp(@Body Map<String, String> postSellparams);

    @POST("UserLogout.php")
    Call<CommonStatusResponce> logoutApp(@Body Map<String, String> postSellparams);

}
