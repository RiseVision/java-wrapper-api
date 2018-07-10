package rise.vision.apis;

import retrofit2.Call;
import retrofit2.http.*;
import rise.vision.apis.requests.PostTransaction;
import rise.vision.apis.responses.APIResponse;
import rise.vision.apis.responses.transactions.CountResponse;
import rise.vision.apis.responses.transactions.TransactionResponse;
import rise.vision.apis.responses.transactions.TransactionsResponse;

import java.util.Map;

/**
 * Transport API
 */
public interface Transport {

  @POST("/peer/transactions")
  Call<APIResponse> postTransaction(@HeaderMap Map<String, String> headers, @Body PostTransaction tx);

}
