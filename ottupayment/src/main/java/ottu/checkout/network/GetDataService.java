package ottu.checkout.network;

import okhttp3.ResponseBody;
import ottu.checkout.model.GenerateToken.CreatePaymentTransaction;
import ottu.checkout.model.RedirectUrl.CreateRedirectUrl;
import ottu.checkout.model.RedirectUrl.RespoRedirectUrl;
import ottu.checkout.model.fetchTxnDetail.RespoFetchTxnDetail;
import ottu.checkout.model.submitCHD.SubmitCHDToOttoPG;
import ottu.checkout.model.submitCHD.SubmitCHDToOttoPGEncrypted;
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