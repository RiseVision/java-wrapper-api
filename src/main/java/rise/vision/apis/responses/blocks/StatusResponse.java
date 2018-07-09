package rise.vision.apis.responses.blocks;

import rise.vision.apis.responses.APIResponse;

public class StatusResponse extends APIResponse {
  public String broadhash;
  public String epoch;
  public long fee;
  public int height;
  public int milestone;
  public String nethash;
  public Long reward;
  public Long supply;
}
