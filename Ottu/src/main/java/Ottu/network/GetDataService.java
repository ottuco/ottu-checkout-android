package Ottu.network;

import okhttp3.ResponseBody;
import Ottu.model.GenerateToken.CreatePaymentTransaction;
import Ottu.model.RedirectUrl.CreateRedirectUrl;
import Ottu.model.RedirectUrl.RespoRedirectUrl;
import Ottu.model.fetchTxnDetail.RespoFetchTxnDetail;
import Ottu.model.submitCHD.SubmitCHDToOttoPG;
import Ottu.model.submitCHD.SubmitCHDToOttoPGEncrypted;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GetDataService {


//    @GET("checkout/v1/pymt-txn/submit/{apiId}")
    @GET("checkout/api/sdk/v1/pymt-txn/submit/{sessionId}")
    Call<RespoFetchTxnDetail> fetchTxnDetail(@Path ("sessionId") String apiId,
                                             @Query("enableCHD") boolean value);

    @POST()
    Call<ResponseBody> respoSubmitCHDEncrypted(@Url String submitUrlCard,@Body SubmitCHDToOttoPGEncrypted submitCHDToOttoPG);
    @POST()
    Call<ResponseBody> respoSubmitCHD(@Url String submitUrlCard,@Body SubmitCHDToOttoPG submitCHDToOttoPG);


    @POST("checkout/v1/pymt-txn/")
    Call<RespoFetchTxnDetail> createPaymentTxn(@Body CreatePaymentTransaction transaction);

    @POST()
    Call<RespoRedirectUrl> createRedirectUrl(@Url String url,
                                             @Body CreateRedirectUrl redirectUrl);

    @DELETE()
    Call<ResponseBody> deleteCard1(@Url String token);

    @GET()
    Call<ResponseBody> getPublicKey(@Url String urlPublicKey);
}