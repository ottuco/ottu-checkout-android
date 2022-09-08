package ottu.checkout.sdk.network;
import ottu.checkout.model.GenerateToken.CreatePaymentTransaction;
import ottu.checkout.model.fetchTxnDetail.RespoFetchTxnDetail;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetDataService {


    @Headers({"Content-Type: application/json", "Authorization: Api-Key L0Fc5f81.dLqByodGesaD9pJdzoKpo6rP1FQBkVzR"})
    @POST("checkout/v1/pymt-txn/")
    Call<RespoFetchTxnDetail> createPaymentTxn(@Body CreatePaymentTransaction transaction);


}