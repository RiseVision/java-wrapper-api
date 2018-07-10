import org.junit.Before;
import org.junit.Test;
import rise.vision.APIWrapper;
import rise.vision.apis.responses.accounts.AccountResponse;
import rise.vision.apis.responses.accounts.DelegatesResponse;
import rise.vision.beans.Delegate;

import static org.junit.Assert.*;

public class AccountsApiTest {
  APIWrapper wrapper;

  @Before
  public void before() {
    wrapper = new APIWrapper();
  }

  @Test public void getByAddress() throws Exception {
    AccountResponse body = wrapper.accountsAPI.getByAddress("17358916385635204476R").execute().body();

    assertTrue(body.success);
    assertEquals("17358916385635204476R", body.account.address);
    assertEquals("e99d803f628864c8254c0e6667d025714044d666f9e39a96b3faf1d9e04bb886", body.account.publicKey);
    assertEquals(body.account.unconfirmedBalance, body.account.balance);
    assertEquals(body.account.secondSignature, body.account.unconfirmedSignature);
    assertEquals(0, body.account.secondSignature);
  }

  @Test public void getByPublicKey() throws Exception {
    AccountResponse body = wrapper.accountsAPI.getByPublicKey("e99d803f628864c8254c0e6667d025714044d666f9e39a96b3faf1d9e04bb886").execute().body();

    assertTrue(body.success);
    assertEquals("17358916385635204476R", body.account.address);
    assertEquals("e99d803f628864c8254c0e6667d025714044d666f9e39a96b3faf1d9e04bb886", body.account.publicKey);
    assertEquals(body.account.unconfirmedBalance, body.account.balance);
    assertEquals(body.account.secondSignature, body.account.unconfirmedSignature);
    assertEquals(0, body.account.secondSignature);
  }

  @Test public void getDelegates() throws Exception {
    DelegatesResponse body = wrapper.accountsAPI.getDelegates("17358916385635204476R").execute().body();

    assertTrue(body.success);
    assertEquals(1, body.delegates.size());

    Delegate delegate = body.delegates.get(0);
    assertEquals("genesisDelegate87", delegate.username);
    assertEquals("17358916385635204476R", delegate.address);
    assertEquals("e99d803f628864c8254c0e6667d025714044d666f9e39a96b3faf1d9e04bb886", delegate.publicKey);
    assertEquals("37955148516", delegate.vote);
    assertEquals(1257, delegate.producedblocks);
    assertEquals(1, delegate.missedblocks);
    assertEquals(0f, delegate.approval, 0);
    assertEquals(0f, delegate.productivity, 0);
  }


}
