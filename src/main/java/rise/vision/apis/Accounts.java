package rise.vision.apis;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rise.vision.apis.responses.APIResponse;
import rise.vision.apis.responses.accounts.AccountResponse;
import rise.vision.apis.responses.accounts.DelegatesResponse;
import rise.vision.beans.Account;
import rise.vision.beans.Delegate;

import java.util.List;

/**
 * Accounts API
 */
public interface Accounts {

  @GET("/api/accounts")
  Call<AccountResponse> getByAddress(@Query("address") String address);

  @GET("/api/accounts")
  Call<AccountResponse> getByPublicKey(@Query("publicKey") String publicKey);

  @GET("/api/accounts/delegates")
  Call<DelegatesResponse> getDelegates(@Query("address") String string);

}
