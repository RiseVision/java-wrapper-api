import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import rise.vision.Transaction;
import rise.vision.Wallet;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class WalletTest {
  public static class WalletDataEntry {
    public String secret;
    public String address;
    public String publicKey;
  }
  private WalletDataEntry[] wallets;
  private Transaction[] txs;

  @Before
  public void before() throws Exception {
    wallets = new Gson().fromJson(
      IOUtils.toString(getClass().getResourceAsStream("wallets.json")),
      WalletDataEntry[].class
      );
    txs = new Gson().fromJson(
      IOUtils.toString(getClass().getResourceAsStream("sendTxs.json")),
      Transaction[].class
    );
  }

  @Test
  public void addressDerivation() throws Exception {
    for (WalletDataEntry wallet : wallets) {
      assertEquals(wallet.address, Wallet.fromBIP39(wallet.secret).getAddress());
    }
  }

  @Test
  public void publicKeyDerivation() throws Exception {
    for (WalletDataEntry wallet : wallets) {
      assertEquals(wallet.publicKey, Wallet.fromBIP39(wallet.secret).getPublicKey());
    }
  }

  @Test
  public void getSignatureOfTransaction() throws Exception {
    Wallet testWallet = Wallet.fromBIP39("wagon stock borrow episode laundry kitten salute link globe zero feed marble");
    System.out.println(testWallet.getPublicKey());
    for (Transaction tx : txs) {

      Transaction unsignedTx = Transaction.builder()
        .amount(tx.getAmount())
        .fee(tx.getFee())
        .recipientId(tx.getRecipientId())
        .senderPublicKey(tx.getSenderPublicKey())
        .timestamp(tx.getTimestamp())
        .build();

      String signature = testWallet.getSignatureOfTransaction(unsignedTx);
      assertEquals(signature, tx.getSignature());
    }
  }


}
