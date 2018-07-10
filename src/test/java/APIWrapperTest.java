import org.junit.Test;
import retrofit2.Response;
import rise.vision.APIWrapper;
import rise.vision.Transaction;
import rise.vision.Wallet;
import rise.vision.apis.responses.transactions.TransactionsResponse;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class APIWrapperTest {
  @Test(expected = java.net.SocketTimeoutException.class)
  public void timeout() throws Exception {
    APIWrapper apiWrapper = new APIWrapper("https://wallet.rise.vision", 1);
    apiWrapper.transactionsAPI.getList(new HashMap<>())
      .execute()
      .body();
  }

  @Test(expected = java.net.UnknownHostException.class)
  public void invalidNode() throws Exception {
    APIWrapper apiWrapper = new APIWrapper("https://meow.caaom");
    apiWrapper.transactionsAPI.getList(new HashMap<>())
      .execute()
      .body();
  }

  @Test
  public void validHostButNotANode() throws Exception {
    APIWrapper apiWrapper = new APIWrapper("https://meow.com");
    Response<TransactionsResponse> execute = apiWrapper.transactionsAPI.getList(new HashMap<>())
      .execute();

    TransactionsResponse body = execute
      .body();

    assertFalse(execute.isSuccessful());
    assertNull(body);
  }

  @Test
  public void craftAndSendTx() throws Exception {
    Wallet w = Wallet.fromBIP39("amused vague melt basic lobster draft they immune creek fix embody match");
    APIWrapper wrapper = new APIWrapper("https://twallet.rise.vision");
    Transaction transaction = wrapper
      .broadcastTransaction(wrapper.craftTransaction(w, w.getAddress(), 1));
    System.out.println("Broadcasted "+transaction.getId());
  }
}
