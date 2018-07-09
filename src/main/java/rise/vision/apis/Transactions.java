package rise.vision.apis;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rise.vision.apis.responses.transactions.CountResponse;
import rise.vision.apis.responses.transactions.TransactionResponse;
import rise.vision.apis.responses.transactions.TransactionsResponse;

import java.util.Map;

public interface Transactions {

  @GET("/api/transactions/get")
  Call<TransactionResponse> getByID(@Query("id") String id);

  @GET("/api/transactions/count")
  Call<CountResponse> getCount();

  @GET("/api/transactions/")
  Call<TransactionsResponse> getList(@QueryMap Map<String, String> query);

  @GET("/api/transactions/unconfirmed/get")
  Call<TransactionsResponse> getUnconfirmed(@Query("id") String id);

  @GET("/api/transactions/unconfirmed")
  Call<TransactionsResponse> getUnconfirmed();
}
