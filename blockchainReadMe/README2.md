# 这个项目你需要注意的东西

事实上，区块链技术就是加密存储的技术，可能在某种程度上就是所谓的sql技术。

在上一个README当中是区块链的理论已经解释得比较清楚了，注释在blockchain的src里面也非常详细。或许你还是云里雾里，比如wtf？链表是什么，然后还有什么哈希值。你可以理解为链表就是python中的list，而哈希值就是一个经过计算的可以唯一标识某个数据块存储位置的编号。哈希值在python也有，哈希值有点类似于python的dict当中的key。

还有一些可能以前没有听说过的概念，公钥和私钥，其实sql里面也有这个概念，总之，你只要知道大家可以通过公钥去知道你钱包的地址，而私钥用于检验这个账户是否属于你。

## 看哪些代码

1. 重中之重 Block.java,这里定义了区块链最重要的信息，区块。
  
    ```java
            //这个区块的哈希值
            public String hash;
            //上一个区块的hash值
            public String previousHash;
            //每个区块存放的信息
            public String merkleRoot;
            //时间戳
            public long timeStamp;
            //挖矿者的工作量证明
            public int nonce;
            //我们的交易信息最后会转化成merkleRoot
            public ArrayList<Transaction> transactions = newArrayList<Transaction>();
    ```

2. Wallet.java 这边定义了钱包的一些重要的信息。

    ```java
        //私钥
        public PrivateKey privateKey;
        //公钥
        public PublicKey publicKey;
        //保存了交易的信息
        public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
    ```

3. 业务逻辑，在BlockChain.java定义，这个你就具体看这个文件吧，注释也比较多，还有不懂的再问我吧。