package rise.vision.apis.responses.transactions;

import rise.vision.apis.responses.APIResponse;
import rise.vision.beans.BaseTransaction;

public class CountResponse extends APIResponse {
  public Long confirmed;
  public Long multisignature;
  public Long unconfirmed;
  public Long queued;
}
