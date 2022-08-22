package ottu.payment.network;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import ottu.payment.model.DeleteCard.SendDeleteCard;
import ottu.payment.model.GenerateToken.CreatePaymentTransaction;
import ottu.payment.model.RedirectUrl.CreateRedirectUrl;
import ottu.payment.model.RedirectUrl.RespoRedirectUrl;
import ottu.payment.model.RespoPublicKey;
import ottu.payment.model.fetchTxnDetail.RespoFetchTxnDetail;
import ottu.payment.model.submitCHD.SubmitCHDToOttoPG;
import ottu.payment.model.submitCHD.SubmitCHDToOttoPGEncrypted;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GetDataService {


//    @GET("checkout/v1/pymt-txn/submit/{apiId}")
    @GET("checkout/api/sdk/v1/pymt-txn/submit/{sessionId}")
    Call<RespoFetchTxnDetail> fetchTxnDetail(@Path ("sessionId") String apiId,
                                             @Query("enableCHD") boolean value);

//    @POST("route")
    @POST()
    Call<ResponseBody> respoSubmitCHD(@Url String submitUrlCard,@Body SubmitCHDToOttoPGEncrypted submitCHDToOttoPG);


    @POST("checkout/v1/pymt-txn/")
    Call<RespoFetchTxnDetail> createPaymentTxn(@Body CreatePaymentTransaction transaction);

    @POST()
    Call<RespoRedirectUrl> createRedirectUrl(@Url String url,
                                             @Body CreateRedirectUrl redirectUrl);

    @DELETE()
    Call<ResponseBody> deleteCard1(@Url String token);

    @GET()
    Call<RespoPublicKey> getPublicKey(@Url String urlPublicKey);
}