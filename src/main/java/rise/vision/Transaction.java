package rise.vision;

import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import lombok.Getter;
import org.abstractj.kalium.keys.SigningKey;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


public class Transaction {
  private static long EPOCH = Date.UTC(2016-1900, 4,24,17,0,0);

  public final String recipientId;

  public final Long amount;
  public final byte type; //SEND;
  public final long fee;


  public Transaction(String recipientId, long fee, long amount) {
    this.recipientId = recipientId;
    this.fee = fee;
    this.amount = amount;
    this.type = 0;
  }

  @Getter
  private String id;
  @Getter
  int timestamp = -1;
  @Getter
  private String signature;
  @Getter
  private String signSignature; // Second signature
  @Getter
  private String senderPublicKey;


  private void checkOrInit() {
    if (timestamp == -1) {
      timestamp = (int) ((new Date().getTime() - EPOCH)/1000);
    }
  }

  public byte[] toBytes() {
    return toBytes(false, false);
  }

  public byte[] toBytes(boolean skipSignature, boolean skipSecondSignature) {
    this.checkOrInit();
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
      for (byte aByte : src) {
        buffer.put(aByte);
      }
    } else {
      buffer.put(new byte[21]);
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


  public byte[] getHash()  {
    try {
      MessageDigest instance = MessageDigest.getInstance("SHA-256");
      instance.update(toBytes());
      return instance.digest();
    } catch (NoSuchAlgorithmException nse) {
      throw new RuntimeException(nse);
    }
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
