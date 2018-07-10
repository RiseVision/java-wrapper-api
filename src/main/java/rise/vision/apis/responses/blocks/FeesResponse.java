package rise.vision.apis.responses.blocks;

import rise.vision.apis.responses.APIResponse;

public class FeesResponse extends APIResponse {
  public static class InnerFeesResponse {
    /**
     * Send transaction fee
     */
    public long send;
    /**
     * Vote transaction fee
     */
    public long vote;
    /**
     * Second signature registration transaction fee
     */
    public long secondsignature;
    /**
     * Delegate registration transaction fee
     */
    public long delegate;
    /**
     * Multisig registration transaction fee
     */
    public long multisignature;
  }

  /**
   * fee structure
   */
  public InnerFeesResponse fees;
  /**
   * start height where the fees structure above apply
   */
  public Integer fromHeight;
  /**
   * to height (could be null)
   */
  public Integer toHeight;
}
