package rise.vision;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import lombok.Getter;
import org.abstractj.kalium.keys.SigningKey;
import org.bitcoinj.crypto.MnemonicCode;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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

  protected String suffix = "R";

  public Wallet(byte[] seed) {
    if (seed == null || seed.length != 32) {
      throw new IllegalArgumentException("Seed must be 32 bytes long");
    }
    this.signingKey = new SigningKey(seed);
    this.address = Wallet.calculateID(Hashing.sha256().hashBytes(this.signingKey.getVerifyKey().toBytes()).asBytes()) + suffix;
    this.publicKey = BaseEncoding.base16().lowerCase().encode(this.signingKey.getVerifyKey().toBytes());
    this.privateKey = BaseEncoding.base16().lowerCase().encode(this.signingKey.toBytes());
  }

  public String getSignatureOfTransaction(Transaction tx) {
    return BaseEncoding.base16().lowerCase().encode(this.signingKey.sign(tx.getHash()));
  }

  public Transaction createSendTX(String to, long amount, long fee) {
    return Transaction.builder()
      .fee(fee)
      .amount(amount)
      .recipientId(to)
      .build()
      .sign(this.signingKey);
  }

  public Transaction coSign(Transaction tx) {
    return tx.secondSign(this.signingKey);
  }

  public static String calculateID(byte[] orig) {
    ByteBuffer buf = ByteBuffer.allocate(8);
    for (int i = 0; i < 8; i++) {
      buf.put(i, orig[7 - i]);
    }

    return new BigInteger(1, buf.array()).toString();
  }

  public static Wallet fromBIP39(String secret) {
    try {
      new MnemonicCode().check(Arrays.asList(secret.split(" ")));
    } catch (Exception e) {
      throw new IllegalArgumentException("Looks like the secret is not valid", e);
    }
    byte[] digest = Hashing.sha256().hashBytes(secret.getBytes(StandardCharsets.UTF_8)).asBytes();
    return new Wallet(digest);
  }
}
