package rise.vision;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rise.vision.apis.*;
import rise.vision.apis.requests.PostTransaction;
import rise.vision.apis.responses.APIResponse;
import rise.vision.apis.responses.blocks.StatusResponse;
import rise.vision.apis.responses.peers.PeersVersionResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class APIWrapper {

  public final Retrofit retrofit;
  public final Blocks blocksAPI;
  public final Transactions transactionsAPI;
  public final Transport transportAPI;
  public final Peers peersAPI;

  private Map<String, String> transportHeaders = null;

  public APIWrapper() {
    this("https://wallet.rise.vision", 10);
  }

  public APIWrapper(String nodeURL) {
    this(nodeURL, 10);
  }

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
  }

  public Accounts accountsAPI() {
    return this.retrofit.create(Accounts.class);
  }

  public Transaction broadcastTransaction(Transaction tx) throws IOException {
    PostTransaction body = new PostTransaction(tx);
    this.buildTransportHeaders();
    APIResponse result = this.transportAPI.postTransaction(this.transportHeaders, body).execute().body();
    if (!result.success) {
      throw new RuntimeException("Failure "+result.error);
    }
    return tx;
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
