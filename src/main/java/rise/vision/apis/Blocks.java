package rise.vision.apis;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rise.vision.apis.responses.blocks.*;
import java.util.Map;

public interface Blocks {

  @GET("/api/blocks/getFees")
  Call<FeesResponse> getAllFees();

  @GET("/api/blocks/getStatus")
  Call<StatusResponse> getStatus();

  @GET("/api/blocks/getHeight")
  Call<HeightResponse> getHeight();

  /**
   * Use #BlocksQueryBuilder to provide parameter here
   */
  @GET("/api/blocks/")
  Call<BlocksResponse> getBlocks(@QueryMap Map<String, String> query);


  @GET("/api/blocks/get")
  Call<BlockResponse> getBlock(@Query("id") String id);

}
