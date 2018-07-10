package rise.vision.apis;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rise.vision.apis.responses.APIResponse;
import rise.vision.beans.Account;
import rise.vision.beans.Delegate;

import java.util.List;

/**
 * Accounts API
 */
public interface Accounts {
  class AccountResponse extends APIResponse {
    public Account account;
  }
  class DelegateResponse extends APIResponse {
    public List<Delegate> delegates;
  }

  @GET("/api/accounts")
  Call<AccountResponse> getByAddress(@Query("address") String address);

  @GET("/api/accounts")
  Call<AccountResponse> getByPublicKey(@Query("publicKey") String publicKey);

  @GET("/api/accounts/delegates")
  Call<AccountResponse> getDelegates(@Query("address") String string);

}
