package rise.vision;

import com.google.common.io.BaseEncoding;
import lombok.Getter;
import org.abstractj.kalium.keys.SigningKey;
import org.bitcoinj.crypto.MnemonicCode;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class Wallet {
  @Getter
  private final String address;
  @Getter
  private final String publicKey;
  @Getter
  private final String privateKey;
  @Getter
  private final SigningKey signingKey;

  public Wallet(byte[] seed) {
    if (seed ==null || seed.length != 32) {
      throw new IllegalArgumentException("Seed must be 32 bytes long");
    }
    this.signingKey = new SigningKey(seed);
    this.address = Wallet.calculateID(this.signingKey.getVerifyKey().toBytes());
    this.publicKey = BaseEncoding.base16().lowerCase().encode(this.signingKey.getVerifyKey().toBytes());
    this.privateKey = BaseEncoding.base16().lowerCase().encode(this.signingKey.toBytes());
  }

  public String getSignatureOfTransaction(Transaction tx) {
    return BaseEncoding.base16().lowerCase().encode(this.signingKey.sign(tx.toBytes()));
  }

  public Transaction createSendTX(String to, long amount, long fee) {
    Transaction toRet = new Transaction(to, fee, amount);
    toRet.sign(this.signingKey);
    return toRet;
  }

  public Transaction coSign(Transaction tx) {
    return tx.secondSign(this.signingKey);
  }

  public static String calculateID(byte[] publicKey) {
    ByteBuffer buf = ByteBuffer.allocate(8);
    for (int i = 0; i < 8; i++) {
      buf.put(i, publicKey[7 - i]);
    }

    return new BigInteger(1, buf.array()).toString();
  }

  public static Wallet fromBIP39(String secret) {
    try {
      new MnemonicCode().check(Arrays.asList(secret.split(" ")));
    } catch (Exception e) {
      throw new IllegalArgumentException("Looks like the secret is not valid", e);
    }
    try {
      byte[] digest = MessageDigest.getInstance("SHA-256").digest(
        secret.getBytes(StandardCharsets.UTF_8)
      );
      return new Wallet(digest);
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot perform SHA256", e);
    }
  }
}
