package rise.vision.apis.utils;

import com.google.common.base.Strings;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public class BlocksQueryBuilder {
  public static enum OrderKind {
    HEIGHT_DESC,
    HEIGHT_ASC,
  }
  public OrderKind orderBy;
  public String generatorPublicKey;
  public String previousBlock;
  public Integer height;
  public Integer limit;
  public Integer offset;

  public Map<String, String> toMap() {
    HashMap<String, String> toRet = new HashMap<>();
    if (OrderKind.HEIGHT_ASC.equals(this.orderBy)) {
      toRet.put("orderBy", "height:asc");
    } else {
      toRet.put("orderBy", "height:desc");
    }

    if (!Strings.isNullOrEmpty(this.generatorPublicKey)) {
      toRet.put("generatorPublicKey", this.generatorPublicKey);
    }

    if (!Strings.isNullOrEmpty(this.previousBlock)) {
      toRet.put("previousBlock", this.previousBlock);
    }

    if (height != null) {
      toRet.put("height", this.height.toString());
    }

    if (limit != null) {
      toRet.put("limit", this.limit.toString());
    }

    if (offset != null) {
      toRet.put("offset", this.offset.toString());
    }

    return toRet;

  }
}
