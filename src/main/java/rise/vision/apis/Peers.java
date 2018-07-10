package rise.vision.apis;

import retrofit2.Call;
import retrofit2.http.GET;
import rise.vision.apis.responses.peers.PeersVersionResponse;

/**
 * Peers API
 */
public interface Peers {

  @GET("/api/peers/version")
  Call<PeersVersionResponse> version();

}
