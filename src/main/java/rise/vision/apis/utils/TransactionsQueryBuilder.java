package rise.vision.apis.utils;

import com.google.common.base.Strings;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public class TransactionsQueryBuilder {
  public static enum TXOrderBy {
    HEIGHT_DESC("height:desc"),
    HEIGHT_ASC("height:asc"),
//    TIMESTAMP_DESC("timestamp:desc"),
//    TIMESTAMP_ASC("timestamp:asc"),
    AMOUNT_DESC("amount:desc"),
    AMOUNT_ASC("amount:asc");

    public final String value;
    TXOrderBy(String v) {
      this.value = v;
    }
  }
  public String blockId;
  public Integer type;
  public String senderId;
  public String senderPublicKey;
  public String recipientId;
  public Integer fromHeight;
  public Integer toHeight;
  public Integer minConfirmations;
  public Integer limit;
  public Integer offset;
  public TXOrderBy orderBy;

  public Map<String, String> toMap() {
    HashMap<String, String> toRet = new HashMap<>();
    if (!Strings.isNullOrEmpty(this.blockId)) {
      toRet.put("and:blockId", this.blockId);
    }

    if (this.type != null) {
      toRet.put("and:type", this.type.toString());
    }

    if (!Strings.isNullOrEmpty(this.senderId)) {
      toRet.put("and:senderId", this.senderId);
    }

    if (!Strings.isNullOrEmpty(this.senderPublicKey)) {
      toRet.put("and:senderPublicKey", this.senderPublicKey);
    }

    if (!Strings.isNullOrEmpty(this.recipientId)) {
      toRet.put("and:recipientId", this.recipientId);
    }

    if (this.fromHeight != null) {
      toRet.put("and:fromHeight", this.fromHeight.toString());
    }

    if (this.toHeight != null) {
      toRet.put("and:toHeight", this.toHeight.toString());
    }

    if (this.minConfirmations != null) {
      toRet.put("and:minConfirmations", this.minConfirmations.toString());
    }

    if (this.limit != null) {
      toRet.put("limit", this.limit.toString());
    }

    if (this.offset != null) {
      toRet.put("offset", this.offset.toString());
    }

    if (this.orderBy == null) {
      this.orderBy = TXOrderBy.HEIGHT_DESC;
    }

    toRet.put("orderBy", this.orderBy.value);

    return toRet;

  }
}
