package rise.vision.apis.requests;

import rise.vision.Transaction;

public class PostTransaction {
  public final Transaction transaction;

  public PostTransaction(Transaction transaction) {
    this.transaction = transaction;
  }
}
