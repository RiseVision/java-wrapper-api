package rise.vision.beans;

import java.util.Map;

public class BaseTransaction {
  public int type;
  public Long amount;
  public String senderPublicKey;
  public String requesterPublicKey;
  public Long timestamp;
  public String senderId;
  public String recipientId;
  public String signature;
  public String id;
  public Long fee;
  public Map<String, Object> asset;
}
