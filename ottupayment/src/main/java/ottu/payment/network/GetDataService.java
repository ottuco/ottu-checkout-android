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
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {


//    @GET("checkout/v1/pymt-txn/submit/{apiId}")
    @GET("checkout/api/sdk/v1/pymt-txn/submit/{apiId}")
    Call<ResponceFetchTxnDetail> fetchTxnDetail(@Path ("apiId") String apiId,
                                                @Query("enableCHD") boolean value);

    @POST("route")
    Call<JsonElement> respoSubmitCHD(@Body SubmitCHDToOttoPG submitCHDToOttoPG);


    @POST("checkout/v1/pymt-txn/")
    Call<ResponceFetchTxnDetail> createPaymentTxn(@Body CreatePaymentTransaction transaction);

    @POST("checkout/api/sdk/v1/submit/{SessionId}/")
    Call<RespoRedirectUrl> createRedirectUrl(@Path("SessionId") String Id,
                                             @Body CreateRedirectUrl redirectUrl);

    @DELETE("pbl/v1/card/{Token}")
    Call<ResponseBody> deleteCard(@Path ("Token") String token,
                                            @Query("customer_id")String customerId,
                                            @Query("type")String type);
}