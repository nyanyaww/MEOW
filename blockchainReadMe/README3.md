# java的简单区块链

我这个顺序是自底向上的去设计这个程序。

最底层是Transaction，之后是Wallet，最后是Block。

还有一个帮助类的StringUtil，用于打印我们的数据。

最后主程序是BlockChain

## 1. Transaction

这边主要是写交易的底层。

```java
public String transactionId;//包含事务的散列。
public PublicKey sender; //发送者地址/公钥。
public PublicKey reciepient; //收件人地址/公钥。
public float value; //我们希望发送给收件人的金额。
public byte[] signature; //加密签名，证明该交易是由地址的发送者是发送的，并且数据没有被更改。
```

在交易的一开始，我们需要生成数字签名

实际上，这个方法在Wallet的`sendFunds`方法也有使用，这意味着你每次转账的时候都会生成一个数字签名，相当于调用`sendFunds`方法程序会自动帮你生成数字签名一样。

```java
public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }
```

不过你需要要注意的是在程序的一开始，第一个区块生成的时候，你需要手动执行这个方法。

```java
genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisTransaction.generateSignature(coinbase.privateKey);//手动签署《创世纪》交易。
        genesisTransaction.transactionId = "0"; //手动设置事务id。
```

还有一个处理事务的方法，但是我不知道怎么讲，大致是一步一步地验证这次交易的合法性，如果合法就会被添加到区块中。

```java
public boolean processTransaction() {

        if (verifySignature() == false) {
            System.out.println("#error:事务签名未能验证");
            return false;
        }

        //收集事务输入(确保它们是未使用的):
        for (TransactionInput i : inputs) {
            i.UTXO = BlockChain.UTXOs.get(i.transactionOutputId);
        }

        //检查交易是否有效:
        if (getInputsValue() < BlockChain.minimumTransaction) {
            System.out.println("#info:交易金额太小: " + getInputsValue());
            System.out.println("#info:请输入大于:" + BlockChain.minimumTransaction + "的金额");
            return false;
        }

        //生成事务输出:
        float leftOver = getInputsValue() - value; //得到输入的值，然后是剩余的变化:
        transactionId = calulateHash();
        outputs.add(new TransactionOutput(this.reciepient, value, transactionId)); //值发送给收件人
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); //将剩余的“金额”发送回发送方。

        //将输出添加到未使用的列表
        for (TransactionOutput o : outputs) {
            BlockChain.UTXOs.put(o.id, o);
        }

        //从UTXO列表中删除事务输入:
        for (TransactionInput i : inputs) {
            if (i.UTXO == null) continue; //if Transaction can't be found skip it
            BlockChain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }
```

就是在Block类的这个方法里面使用了处理事务的方法。如果事务有效，也就是这次交易成功了，就添加在区块当中。

```java
//将交易添加到区块中
public boolean addTransaction(Transaction transaction) {
    //进程事务，检查是否有效，除非block是genesis块，然后忽略。
    if(transaction == null) return false;
    if((previousHash != "0")) {
        if((transaction.processTransaction() != true)) {
            System.out.println("#error:交易失败。事务被丢弃。");
            return false;
        }
    }

    transactions.add(transaction);
    System.out.println("#info:事务成功地添加到区块中");
    return true;
}
```

## 2. Wallet

1. 第1部分是写了钱包的构造函数，每一个钱包对象在初始化的时候是随机生成一些密钥对。

    ```java
    public Wallet() {
            generateKeyPair();
        }

        public void generateKeyPair() {
            if (Security.getProvider("BC") == null) {
                Security.addProvider(new BouncyCastleProvider());
            }

            try {
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
                //初始化密钥生成器并生成密钥对。
                keyGen.initialize(ecSpec, random); //256
                KeyPair keyPair = keyGen.generateKeyPair();
                //从密钥对中设置公钥和私钥。
                privateKey = keyPair.getPrivate();
                publicKey = keyPair.getPublic();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    ```

2. 这边定义了两个方法，获得自己钱包的金额和转账（这部分使用了Transaction的方法）

    ```java
    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : BlockChain.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(publicKey)) { //如果输出属于我(如果硬币属于我)
                UTXOs.put(UTXO.id, UTXO); //将它添加到未使用的事务列表中。
                total += UTXO.value;
            }
        }
        return total;
    }

    public Transaction sendFunds(PublicKey recipient, float value) {
            if (getBalance() < value) {
                System.out.println("#error:没有足够的资金来发送交易。事务被丢弃。");
                return null;
            }
            ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

            float total = 0;
            for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
                TransactionOutput UTXO = item.getValue();
                total += UTXO.value;
                inputs.add(new TransactionInput(UTXO.id));
                if (total > value) break;
            }

            Transaction newTransaction = new Transaction(publicKey, recipient, value, inputs);
            newTransaction.generateSignature(privateKey);

            for (TransactionInput input : inputs) {
                UTXOs.remove(input.transactionOutputId);
            }

            return newTransaction;
        }
    ```

## 3. Block

我想这部分你应该有一点点熟悉，你只是不明白下面这个方法是做什么的

```java
//挖矿
public void mineBlock(int difficulty) {
    //
    merkleRoot = StringUtil.getMerkleRoot(transactions);
    //目标值，difficulty越大，下面计算量越大
    String target = StringUtil.getDificultyString(difficulty);
    //difficulty如果为5，那么target则为 00000
    while(!hash.substring( 0, difficulty).equals(target)) {
        nonce ++;
        hash = calculateHash();
    }
    System.out.println("#info:创建区块:" + hash);
}
```

挖矿，你只需要知道hash值的计算非常耗费电脑的计算能力，所以形象地把hash值的计算称为挖矿。计算hash值得算法被称为Sha256。以下得方法就是这个算法的实现过程，它定义在StringUtil类里面。

```java
//将Sha256应用到一个字符串并返回结果
public static String applySha256(String input){

    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] hash = digest.digest(input.getBytes("UTF-8"));

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    catch(Exception e) {
        throw new RuntimeException(e);
    }
}
```

## BlockChain

区块链交易的话你需要一个初始的区块，然后你才能扩展交易

```java
//创建创世交易（第一笔交易），向walletA发送100个星币:
genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
genesisTransaction.generateSignature(coinbase.privateKey);//手动签署《创世纪》交易。
genesisTransaction.transactionId = "0"; //手动设置事务id。

//手动添加事务输出。
genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId));
//在UTXOs列表中存储第一个事务，非常重要！
UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

System.out.println("#info:创造和开采创世纪块...... ");
Block genesis = new Block("0");//创世区块
genesis.addTransaction(genesisTransaction);//把事务放到区块里
addBlock(genesis);//把区块放大区块链中
```