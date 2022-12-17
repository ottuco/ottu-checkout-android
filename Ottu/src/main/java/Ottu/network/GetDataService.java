package Ottu.network;

import okhttp3.ResponseBody;
import Ottu.model.GenerateToken.CreatePaymentTransaction;
import Ottu.model.RedirectUrl.CreateRedirectUrl;
import Ottu.model.RedirectUrl.RespoRedirectUrl;
import Ottu.model.fetchTxnDetail.RespoFetchTxnDetail;
import Ottu.model.submitCHD.SubmitCHDToOttoPG;
import Ottu.model.submitCHD.SubmitCHDToOttoPGEncrypted;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GetDataService {


    @GET()
    Call<RespoFetchTxnDetail> fetchTxnDetail(@Url String url);

    @POST()
    Call<ResponseBody> respoSubmitCHDEncrypted(@Url String submitUrlCard,@Body SubmitCHDToOttoPGEncrypted submitCHDToOttoPG);
    @POST()
    Call<ResponseBody> respoSubmitCHD(@Url String submitUrlCard,@Body SubmitCHDToOttoPG submitCHDToOttoPG);


    @POST()
    Call<RespoRedirectUrl> createRedirectUrl(@Url String url,
                                             @Body CreateRedirectUrl redirectUrl);

    @DELETE()
    Call<ResponseBody> deleteCard1(@Url String token);

    @GET()
    Call<ResponseBody> getPublicKey(@Url String urlPublicKey);
}