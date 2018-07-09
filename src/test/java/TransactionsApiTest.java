import com.google.common.collect.ImmutableMap;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import org.abstractj.kalium.keys.SigningKey;
import org.junit.Before;
import org.junit.Test;
import rise.vision.APIWrapper;
import rise.vision.Transaction;
import rise.vision.Wallet;
import rise.vision.apis.responses.transactions.CountResponse;
import rise.vision.apis.responses.transactions.TransactionResponse;
import rise.vision.apis.responses.transactions.TransactionsResponse;
import rise.vision.apis.utils.TransactionsQueryBuilder;
import rise.vision.beans.ConfirmedTransaction;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TransactionsApiTest {
  APIWrapper wrapper;

  @Before
  public void before() {
    wrapper = new APIWrapper();
  }

  @Test
  public void byID() throws Exception {
    TransactionResponse body = wrapper.transactionsAPI.getByID("1613301874969790008").execute().body();
    assertTrue(body.success);
    assertNotNull(body.transaction);
    assertNotNull(body.transaction.id);
    assertEquals("1613301874969790008", body.transaction.id);
    assertEquals(0, body.transaction.type);
    assertEquals(new Long(67034857), body.transaction.timestamp);
    assertEquals("517db3e30b8edb40647fe49f0f595b0f57d0cb42b58b0022301b8a0757a83eda", body.transaction.senderPublicKey);
    assertEquals("5465185310874283336R", body.transaction.senderId);
    assertEquals("4162497421808585285R", body.transaction.recipientId);
    assertEquals(new Long(53582547), body.transaction.amount);
    assertEquals(new Long(10000000), body.transaction.fee);
    assertEquals(
      "313c9a4f6d1792919eca01c5fe9913b3aa37babb12d4853842e30a561919450907ffeb9a38735b2d2ceb08584bfdee165eee65ef07533bcce58fd2d3b8b12c03",
      body.transaction.signature
    );
    assertEquals("7415149629207289200",
      body.transaction.blockId);

    assertThat(body.transaction.confirmations, greaterThan(50));

    // Non existing tx test
    body = wrapper.transactionsAPI.getByID("11").execute().body();
    assertFalse(body.success);
    assertEquals("Transaction not found: 11", body.error);
    assertNull(body.transaction);
  }

  @Test
  public void getCount() throws Exception {
    CountResponse body = wrapper.transactionsAPI.getCount().execute().body();

    assertTrue(body.success);
    assertThat(body.confirmed, greaterThan(400000L));
    assertNotNull(body.multisignature);
    assertNotNull(body.queued);
    assertNotNull(body.unconfirmed);
  }

  @Test
  public void getList() throws Exception {
    TransactionsResponse body = wrapper.transactionsAPI.getList(
      TransactionsQueryBuilder.builder()
        .type(0)
        .senderId("5465185310874283336R")
        .build()
        .toMap()
    ).execute().body();

    assertTrue(body.success);
    assertNotNull(body.transactions);
    assertThat(body.transactions.size(), greaterThan(0));

    for (ConfirmedTransaction transaction : body.transactions) {
      assertEquals("5465185310874283336R", transaction.senderId);
      assertEquals(0, transaction.type);
    }

    // Failure test
    body = wrapper.transactionsAPI.getList(ImmutableMap.of("senderId", "invalid"))
      .execute().body();
    assertFalse(body.success);
    assertThat(body.error, containsString("format address: invalid"));
  }

  @Test
  public void getUnconfirmed() throws Exception {
    TransactionsResponse body = wrapper.transactionsAPI.getUnconfirmed().execute().body();
    assertTrue(body.success);
    assertNotNull(body.transactions);
  }

  @Test
  public void getUnconfirmed2() throws Exception {
    TransactionsResponse body = wrapper.transactionsAPI.getUnconfirmed("1").execute().body();
    assertFalse(body.success);
    assertNull(body.transactions);
    assertThat(body.error, containsString("Transaction not found in this queue 1"));
  }

  @Test
  public void tempTest() throws Exception {
    Wallet w = Wallet.fromBIP39("amused vague melt basic lobster draft they immune creek fix embody match");
    Transaction tx = w.createSendTX("2655711995542512317R", 1, 10000000);
    this.wrapper = new APIWrapper("https://twallet.rise.vision");
    this.wrapper
      .broadcastTransaction(tx);

  }
}
