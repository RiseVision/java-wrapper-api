package rise.vision.beans;

public class Block extends BaseBlock {
  /**
   * number of blocks after the current one
   */
  public int confirmations;
  /**
   * The generator address
   */
  public String generatorId;
  /**
   * The total amount of coins that were forged with this block
   */
  public long totalForged;
}
