package ottu.payment.network;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import ottu.payment.model.DeleteCard.SendDeleteCard;
import ottu.payment.model.GenerateToken.CreatePaymentTransaction;
import ottu.payment.model.RedirectUrl.CreateRedirectUrl;
import ottu.payment.model.RedirectUrl.RespoRedirectUrl;
import ottu.payment.model.fetchTxnDetail.RespoFetchTxnDetail;
import ottu.payment.model.redirect.ResponceFetchTxnDetail;
import ottu.payment.model.submitCHD.SubmitCHDToOttoPG;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @Headers({
            "Content-Type: application/json",
            "Authorization: Api-Key L0Fc5f81.dLqByodGesaD9pJdzoKpo6rP1FQBkVzR"
    })
    @GET("checkout/v1/pymt-txn/submit/{apiId}")
    Call<ResponceFetchTxnDetail> fetchTxnDetail(@Path ("apiId") String apiId,
                                                @Query("enableCHD") boolean value);
    @Headers({
            "Content-Type: application/json",
            "Authorization: Api-Key L0Fc5f81.dLqByodGesaD9pJdzoKpo6rP1FQBkVzR"})
    @POST("route")
    Call<JsonElement> respoSubmitCHD(@Body SubmitCHDToOttoPG submitCHDToOttoPG);

    @Headers({"Content-Type: application/json", "Authorization: Api-Key L0Fc5f81.dLqByodGesaD9pJdzoKpo6rP1FQBkVzR"})
    @POST("checkout/v1/pymt-txn/")
    Call<ResponceFetchTxnDetail> createPaymentTxn(@Body CreatePaymentTransaction transaction);

    @Headers({"Content-Type: application/json", "Authorization: Api-Key L0Fc5f81.dLqByodGesaD9pJdzoKpo6rP1FQBkVzR"})
    @POST("/b/checkout/v1/submit/{SessionId}/")
    Call<RespoRedirectUrl> createRedirectUrl(@Path("SessionId") String Id,
                                             @Body CreateRedirectUrl redirectUrl);

    @Headers({"Content-Type: application/json", "Authorization: Api-Key L0Fc5f81.dLqByodGesaD9pJdzoKpo6rP1FQBkVzR"})
    @GET("pbl/v1/card/{Token}")
    Call<ResponseBody> deleteCard(@Path ("Token") String token,
                                            @Body SendDeleteCard card);
}