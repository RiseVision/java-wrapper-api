package rise.vision.apis.responses.blocks;

import rise.vision.apis.responses.APIResponse;

public class StatusResponse extends APIResponse {
  /**
   * Current broadhash
   */
  public String broadhash;
  /**
   * Coin Epoch for timestamp calculation
   */
  public String epoch;
  /**
   * send fee
   */
  public long fee;
  /**
   * current height
   */
  public int height;
  /**
   * current block reward milestone index
   */
  public int milestone;
  /**
   * Genesis block hash
   */
  public String nethash;
  /**
   * Current block production reward
   */
  public Long reward;
  /**
   * Total supply
   */
  public Long supply;
}
