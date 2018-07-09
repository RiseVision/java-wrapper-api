import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rise.vision.apis.Accounts;
import rise.vision.apis.Accounts.AccountResponse;

import static org.junit.Assert.*;

public class ApiTest {
  Retrofit retrofit = new Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("https://wallet.rise.vision/")
    .build();
  @Test public void bit() throws Exception {
    Accounts accounts = retrofit.create(Accounts.class);
    AccountResponse body = accounts.getByAddress("17358916385635204476R").execute().body();
    assertTrue(body.success);
    assertEquals("17358916385635204476R", body.account.address);
    assertEquals("e99d803f628864c8254c0e6667d025714044d666f9e39a96b3faf1d9e04bb886", body.account.publicKey);
    assertEquals(body.account.unconfirmedBalance, body.account.balance);
    assertEquals(body.account.secondSignature, body.account.unconfirmedSignature);
    assertEquals(0, body.account.secondSignature);

  }
}
