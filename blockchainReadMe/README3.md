# java的简单区块链

## block

区块底层的定义

## blockchain

## Wallet

1. 第1部分是写了钱包的构造函数，每一个钱包对象在初始化的时候是随机生成一些密钥对

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

2. 这边定义了两个方法，获得自己钱包的金额和转账

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

## StringUtil