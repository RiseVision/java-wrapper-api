package rise.vision.apis;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rise.vision.apis.responses.blocks.*;
import java.util.Map;

/**
 * Blocks API
 */
public interface Blocks {

  /**
   * Gets current fee structure
   */
  @GET("/api/blocks/getFees")
  Call<FeesResponse> getAllFees();

  /**
   * Fetches current blocks status
   */
  @GET("/api/blocks/getStatus")
  Call<StatusResponse> getStatus();

  /**
   * Gets current height
   */
  @GET("/api/blocks/getHeight")
  Call<HeightResponse> getHeight();

  /**
   * Use #BlocksQueryBuilder to provide parameter here
   * @param query The Query map. Use {@link rise.vision.apis.utils.BlocksQueryBuilder}
   */
  @GET("/api/blocks/")
  Call<BlocksResponse> getBlocks(@QueryMap Map<String, String> query);


  /**
   * Query and gets a single block
   * @param id block id to query
   */
  @GET("/api/blocks/get")
  Call<BlockResponse> getBlock(@Query("id") String id);

}
