# Rise Java API

Through this library you'll be able to interact with the RISE blockchain.

# Usage

The main classes are **Wallet** & **Transaction** & **APIWrapper**.

## Wallet

The wallet API contains some utilities such as Address and publicKey derivation from the given seed.
It can be used with both a valid BIP39 string secret or a raw 32 long byte array.

## Transaction
The Transaction Class provides implementation of the computed values such as transaction id.
The Transaction Class has a **Builder** that could be used like so:

```java
Transaction tx = Transaction.builder()
	.fee(100000)
	.amount(200000)
	.recipientId("1R")
	.build();
```

Once built, the tx needs to be **signed** before broadcasting it... Here is how:

```java
tx = tx.sign(wallet);
```

## APIWrapper

The APIWrapper class contains mechanisms to facilitate the communication with the RISE blockchain. 
There are 3 different constructors that will allow the consumer to specify the custom node and the connection/read/write timeout.

The APIWrapper class also contains some method utilities to broadcast simple send transactions such as:

 - `broadcastTransaction(Transaction tx)`
 - `craftTransaction(Wallet from, String recipient, long amount)`

Both methods could throw an IOException as they both hit the node wallet to gather the relevant informations or perform the requested action.
