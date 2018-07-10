package rise.vision;

import com.google.common.collect.ImmutableMap;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rise.vision.apis.*;
import rise.vision.apis.requests.PostTransaction;
import rise.vision.apis.responses.APIResponse;
import rise.vision.apis.responses.blocks.FeesResponse;
import rise.vision.apis.responses.blocks.StatusResponse;
import rise.vision.apis.responses.peers.PeersVersionResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Main entry class
 */
public class APIWrapper {

  public final Retrofit retrofit;
  public final Blocks blocksAPI;
  public final Transactions transactionsAPI;
  public final Transport transportAPI;
  public final Peers peersAPI;
  public final Accounts accountsAPI;

  private Map<String, String> transportHeaders = null;

  /**
   * Will initiate the instance with rise main wallet host and 10secs of timeout
   */
  public APIWrapper() {
    this("https://wallet.rise.vision", 10);
  }

  /**
   * constructs a new instance with the provided node url
   *
   * @param nodeURL a valid node url with open APIs
   */
  public APIWrapper(String nodeURL) {
    this(nodeURL, 10);
  }

  /**
   * Constructs a new instance with the provided params
   *
   * @param nodeURL        a valid node url with open APIs
   * @param timeoutSeconds timeout in seconds
   */
  public APIWrapper(String nodeURL, long timeoutSeconds) {
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
      .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
      .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
      .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
      .build();

    retrofit = new Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create())
      .baseUrl(nodeURL)
      .client(okHttpClient)
      .build();

    blocksAPI = this.retrofit.create(Blocks.class);
    transactionsAPI = this.retrofit.create(Transactions.class);
    transportAPI = this.retrofit.create(Transport.class);
    peersAPI = this.retrofit.create(Peers.class);
    accountsAPI = this.retrofit.create(Accounts.class);
  }


  /**
   * Broadcast a signed transaction
   *
   * @param tx the tx to broadcast
   * @return tx the same tx that was broadcasted
   * @throws IOException in case of IO error. or in case the tx gets rejected.
   */
  public Transaction broadcastTransaction(Transaction tx) throws IOException {
    PostTransaction body = new PostTransaction(tx);
    this.buildTransportHeaders();
    APIResponse result = this.transportAPI.postTransaction(this.transportHeaders, body).execute().body();
    if (!result.success) {
      throw new RuntimeException("Failure " + result.error);
    }
    return tx;
  }

  /**
   * Creates and sign a send transaction
   *
   * @param from the wallet from which you want to send the tx from
   * @param recipient the address of the receiver
   * @param amount Amount in satoshi to send
   * @return tx the same tx that was broadcasted
   * @throws IOException in case of IO error. or in case the tx gets rejected.

   */
  public Transaction craftTransaction(Wallet from, String recipient, long amount) throws IOException {
    FeesResponse feesResponse = this.blocksAPI.getAllFees().execute().body();
    return from.createSendTX(recipient, amount, feesResponse.fees.send);
  }

  private void buildTransportHeaders() throws IOException {
    if (this.transportHeaders == null) {
      StatusResponse statusResponse = this.blocksAPI.getStatus().execute().body();
      PeersVersionResponse versionResponse = this.peersAPI.version().execute().body();


      this.transportHeaders = ImmutableMap.of(
        "nethash",
        statusResponse.nethash,
        "version",
        versionResponse.version,
        "port",
        "1"
      );
    }
  }

}
