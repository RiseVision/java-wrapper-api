package rise.vision.beans;

public class BaseBlock {
  /**
   * The block id
   */
  public String id;
  public int height;
  /**
   * Hex rep of the signature
   */
  public String blockSignature;
  /**
   * Block producer public Key
   */
  public String generatorPublicKey;
  public int numberOfTransactions;
  /**
   * Hash of the block payload (txs)
   */
  public String payloadHash;
  /**
   * Length of the payload
   */
  public int payloadLength;
  /**
   * prev block id
   */
  public String previousBlock;
  /**
   * Timestamp
   */
  public long timestamp;
  /**
   * total amount transacted in block
   */
  public long totalAmount;
  /**
   * total fees collected in block
   */
  public long totalFee;
  /**
   * block reward
   */
  public long reward;
  public int version;
}
