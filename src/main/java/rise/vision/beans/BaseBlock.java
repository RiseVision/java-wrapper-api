package rise.vision.beans;

public class BaseBlock {
  public String id;
  public int height;
  public String blockSignature;
  public String generatorPublicKey;
  public int numberOfTransactions;
  public String payloadHash;
  public int payloadLength;
  public String previousBlock;
  public long timestamp;
  public long totalAmount;
  public long totalFee;
  public long reward;
  public int version;
}
