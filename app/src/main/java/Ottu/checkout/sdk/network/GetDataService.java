package Ottu.checkout.sdk.network;

import Ottu.model.GenerateToken.CreatePaymentTransaction;
import Ottu.model.fetchTxnDetail.RespoFetchTxnDetail;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetDataService {


    @Headers({"Content-Type: application/json", "Authorization: Api-Key KaaWIoPG.AZGowZsIiXATe9QVYBiEdnfbheb3sGPj"})
    @POST("checkout/v1/pymt-txn/")
    Call<RespoFetchTxnDetail> createPaymentTxn(@Body CreatePaymentTransaction transaction);


}