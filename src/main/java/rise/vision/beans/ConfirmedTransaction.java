package rise.vision.beans;

public class ConfirmedTransaction extends BaseTransaction {
  /**
   * The block where the tx was included
   */
  public String blockId;
  /**
   * The number of confirmations for this tx.
   */
  public Integer confirmations;
}
