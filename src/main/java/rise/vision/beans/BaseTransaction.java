package rise.vision.beans;

import java.util.Map;

public class BaseTransaction {
  /**
   * Transaction Type
   */
  public int type;
  /**
   * the amount transacted
   */
  public Long amount;
  /**
   * the publickey of the sender
   */
  public String senderPublicKey;
  /**
   * requester publickey (user only in some multisig transactions)
   */
  public String requesterPublicKey;
  /**
   * the timestamp of such tx.
   */
  public Long timestamp;
  /**
   * the sender address
   */
  public String senderId;
  /**
   * the recipient address (if any)
   */
  public String recipientId;
  /**
   * the hex encoded signature
   */
  public String signature;
  /**
   * the transaction id
   */
  public String id;
  /**
   * the total tx fee
   */
  public Long fee;
//  /**
//   * asset
//   */
//  public Map<String, Object> asset;
}
