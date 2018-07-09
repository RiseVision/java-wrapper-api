package rise.vision.apis.responses.blocks;

import rise.vision.apis.responses.APIResponse;

public class FeesResponse extends APIResponse {
  public static class InnerFeesResponse {
    public long send;
    public long vote;
    public long secondsignature;
    public long delegate;
    public long multisignature;
  }

  public InnerFeesResponse fees;
  public Integer fromHeight;
  public Integer toHeight;
}
