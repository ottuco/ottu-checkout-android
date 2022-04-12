package ottu.payment.sdk.network;
import ottu.payment.model.DeleteCard.SendDeleteCard;
import ottu.payment.model.GenerateToken.CreatePaymentTransaction;
import ottu.payment.model.RedirectUrl.CreateRedirectUrl;
import ottu.payment.model.RedirectUrl.RespoRedirectUrl;
import ottu.payment.model.redirect.ResponceFetchTxnDetail;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetDataService {


    @Headers({"Content-Type: application/json", "Authorization: Api-Key L0Fc5f81.dLqByodGesaD9pJdzoKpo6rP1FQBkVzR"})
    @POST("checkout/v1/pymt-txn/")
    Call<ResponceFetchTxnDetail> createPaymentTxn(@Body CreatePaymentTransaction transaction);


}