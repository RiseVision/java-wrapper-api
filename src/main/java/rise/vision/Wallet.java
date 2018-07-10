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

/**
 * Represents a single wallet account.
 */
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

  /**
   * Generates a wallet from a byte[] seed.
   * @param seed the bytes[] seed to generate the wallet from. Be aware that it needs to be 32byte.
   */
  public Wallet(byte[] seed) {
    if (seed == null || seed.length != 32) {
      throw new IllegalArgumentException("Seed must be 32 bytes long");
    }
    this.signingKey = new SigningKey(seed);
    this.address = Wallet.calculateID(Hashing.sha256().hashBytes(this.signingKey.getVerifyKey().toBytes()).asBytes()) + suffix;
    this.publicKey = BaseEncoding.base16().lowerCase().encode(this.signingKey.getVerifyKey().toBytes());
    this.privateKey = BaseEncoding.base16().lowerCase().encode(this.signingKey.toBytes());
  }

  /**
   * Generates a signature of a tx without modifying the provided tx.
   * @param tx transaction to sign
   * @return hex encoded signature
   */
  public String getSignatureOfTransaction(Transaction tx) {
    return BaseEncoding.base16().lowerCase().encode(this.signingKey.sign(tx.getHash()));
  }

  /**
   * Creates and sign a new send transaction from the provided params
   * @param to the recipient
   * @param amount the amount in satoshi
   * @param fee the fee in satoshi
   * @return signed Transaction object.
   */
  public Transaction createSendTX(String to, long amount, long fee) {
    return Transaction.builder()
      .fee(fee)
      .amount(amount)
      .recipientId(to)
      .build()
      .sign(this.signingKey);
  }

  /**
   * second sign a given transaction with this wallet
   * @param tx transaction to cosign
   * @return original tx with secondSignature
   */
  public Transaction coSign(Transaction tx) {
    return tx.secondSign(this.signingKey);
  }

  /**
   * Calculate the ID from a byte[] array. used for address derivation
   * @return string
   */
  public static String calculateID(byte[] orig) {
    ByteBuffer buf = ByteBuffer.allocate(8);
    for (int i = 0; i < 8; i++) {
      buf.put(i, orig[7 - i]);
    }

    return new BigInteger(1, buf.array()).toString();
  }

  /**
   * Constructs a new wallet from a BIP39 secret.
   * @param secret the twelve word valid secret.
   * @return Wallet.
   */
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
