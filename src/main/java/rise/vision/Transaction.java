package rise.vision;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.abstractj.kalium.keys.SigningKey;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Date;


public class Transaction {
  private static long EPOCH = Date.UTC(2016 - 1900, 4, 24, 17, 0, 0);

  @Builder
  public Transaction(String recipientId, long fee, long amount, String senderPublicKey, Integer timestamp, byte type) {
    this.recipientId = recipientId;
    this.fee = fee;
    this.amount = amount;
    this.type = type;
    this.senderPublicKey = senderPublicKey;
    if (timestamp != null) {
      this.timestamp = timestamp;
    } else {
      this.timestamp = (int) ((new Date().getTime() - EPOCH) / 1000);
    }
  }

  @Getter
  private String recipientId;
  @Getter
  private Long amount;
  @Getter
  private byte type;
  @Getter
  private long fee;

  @Getter
  private String id;
  @Getter
  @Setter
  int timestamp;
  @Getter
  private String signature;
  @Getter
  private String signSignature; // Second signature
  @Getter
  private String senderPublicKey;

  public byte[] toBytes() {
    return toBytes(false, false);
  }

  public byte[] toBytes(boolean skipSignature, boolean skipSecondSignature) {
    ByteBuffer buffer = ByteBuffer.allocate(1000);
    buffer.order(ByteOrder.LITTLE_ENDIAN);
    buffer.put(type);
    buffer.putInt(timestamp);

    buffer.put(BaseEncoding.base16().lowerCase().decode(senderPublicKey));

    if (!Strings.isNullOrEmpty(recipientId)) {
      byte[] src = new BigInteger(recipientId.substring(0, recipientId.length() - 1)).toByteArray();
      for (int i = 0; i < 8 - src.length; i++) {
        buffer.put((byte) 0); // 0 pad
      }
      int start = Math.max(src.length-8, 0);
      buffer.put(Arrays.copyOfRange(
        src,
        start,
        Math.min(src.length, start + 8))
      );
    } else {
      buffer.put(new byte[8]);
    }

    buffer.putLong(amount);

    //TODO: missing asset
    this.putAssetBytes(buffer);

    //Signature
    if (!skipSignature && !Strings.isNullOrEmpty(signature)) {
      byte[] signBuffer = BaseEncoding.base16().lowerCase().decode(signature);
      buffer.put(signBuffer);
    }

    //second Signature
    if (!skipSecondSignature && !Strings.isNullOrEmpty(signSignature)) {
      byte[] signBuffer = BaseEncoding.base16().lowerCase().decode(signSignature);
      buffer.put(signBuffer);
    }

    byte[] toRet = new byte[buffer.position()];
    buffer.flip();
    buffer.rewind();
    buffer.get(toRet);
    return toRet;
  }

  public void putAssetBytes(ByteBuffer buf) {
    // Extend this for child txs.
  }


  public byte[] getHash() {
    return Hashing.sha256().hashBytes(toBytes()).asBytes();
  }

  public Transaction sign(Wallet wallet) {
    return this.sign(wallet.getSigningKey());
  }

  public Transaction sign(SigningKey signingKey) {
    this.senderPublicKey = BaseEncoding.base16().lowerCase().encode(signingKey.getVerifyKey().toBytes());
    byte[] hash = getHash();
    byte[] result = signingKey.sign(hash);
    this.signature = BaseEncoding.base16().lowerCase().encode(result);
    calculateID();
    return this;
  }

  public Transaction secondSign(SigningKey signingKey) {
    byte[] hash = getHash();
    byte[] result = signingKey.sign(hash);
    this.signSignature = BaseEncoding.base16().lowerCase().encode(result);
    return this;
  }

  public void calculateID() {
    byte[] hash = getHash();
    ByteBuffer buf = ByteBuffer.allocate(8);
    for (int i = 0; i < 8; i++) {
      buf.put(i, hash[7 - i]);
    }

    this.id = new BigInteger(1, buf.array()).toString();
  }

}
