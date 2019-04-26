# 用java写一个简单的区块链

## 目前支持的功能

- 允许用户创建钱包
- 使用椭圆曲线加密方式为钱包提供公钥和私钥
- 通过使用数字签名算法证明所有权，确保资金转移
- 允许用户在区块链上进行交易

## 创建区块链

>区块链就是一串或者是一系列区块的集合，类似于链表的概念，每个区块都指向于后面一个区块，然后顺序的连接在一起。那么每个区块中的内容是什么呢？在区块链中的每一个区块都存放了很多很有价值的信息，主要包括三个部分：自己的数字签名，上一个区块的数字签名，还有一切需要加密的数据（这些数据在比特币中就相当于是交易的信息，它是加密货币的本质）。每个数字签名不但证明了自己是特有的一个区块，而且指向了前一个区块的来源，让所有的区块在链条中可以串起来，而数据就是一些特定的信息，你可以按照业务逻辑来保存业务数据。

![区块链示意图](https://github.com/pibigstar/blockchain/raw/master/01.png)

**这里的hash指的就是数字签名**

> 所以每一个区块不仅包含前一个区块的hash值，同时包含自身的一个hash值，自身的hash值是通过之前的hash值和数据data通过hash计算出来的。如果前一个区块的数据一旦被篡改了，那么前一个区块的hash值也会同样发生变化（因为数据也被计算在内），这样也就导致了所有后续的区块中的hash值。所以计算和比对hash值会让我们检查到当前的区块链是否是有效的，也就避免了数据被恶意篡改的可能性，因为篡改数据就会改变hash值并破坏整个区块链。

## 交易

### 公钥私钥
> 公钥和私钥究竟是起到什么作用呢，其实公钥的作用就是地址，你可以分享你的公钥给别人以此来获取付款，而你的私钥的作用是为了对交易进行签名，这样其他人就不可以花费你的金额除非它拥有你的私钥，所以对于每个人而言我们必须保护好我们的私钥，不能透露我们的私钥信息给其他人。同时在我们进行交易的时候我们也会同时发送我们的公钥由此来验证我们的签名是有效的而且没有数据被篡改。
>即私钥被用来签名数据，而公钥用来验证其完整性。
举个例子：Bob 想要发送2个加密货币给Sally，他们用各自的钱包创建了交易，并提交到全网的区块链中作为一个新的区块，一个挖矿者试图篡改接受者把2个加密货币给John，但是幸运的事，Bob在交易数据中已经用私钥进行了签名，这就允许了全网中的任何节点使用小明的公匙进行验证数据是否已经被篡改（因为没有其他人的公钥可以用来验证小明发出的这笔交易）。
### 交易和数字签名
**每笔交易将携带一定以下信息：**
- 资金付款人的公匙信息。
- 资金收款人的公匙信息。
- 被转移资金的金额。
- 输入，它是对以前的交易的引用，证明发送者有资金发送。
- 输出，显示交易中收款方相关地址数量。(这些输出被引用为新交易的输入)
- 一个加密签名，证明该交易是由地址的发送者是发送的，并且数据没有被更改。(阻止第三方机构更改发送的数量)

### 输入和输出 1：如何验证货币是你的
>如果你拥有1比特币，你必须前面就得接收1比特币。比特币的账本不会在你的账户中增加一个比特币也不会从发送者那里减去一个比特币，发送者只能指向他/她之前收到过一个比特币，所以一个交易输出被创建用来显示一个比特币发送给你的地址（交易的输入指向前一个交易的输出）。
**你的钱包余额是所有未使用的交易输出的总和**

### 添加交易到区块中
>现在我们已经有了一个有效的交易系统，我们需要把交易加入到我们的区块链中。我们把交易列表替换我们块中无用的数据，但是在一个单一的区块中可能存放了1000个交易，这就会导致大量的hash计算，不用担心在这里我们使用了交易的merkle root，稍后你会看到。让我们增加一个帮助方法来创建merkle root在StringUtils类中。