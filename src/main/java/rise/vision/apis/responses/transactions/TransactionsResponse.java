package rise.vision.apis.responses.transactions;

import rise.vision.apis.responses.APIResponse;
import rise.vision.beans.BaseTransaction;
import rise.vision.beans.ConfirmedTransaction;

import java.util.List;

public class TransactionsResponse extends APIResponse {
  public List<ConfirmedTransaction> transactions;
}
