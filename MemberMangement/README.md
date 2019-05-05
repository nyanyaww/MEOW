# 使用java swing的用户消费管理程序

文件分成了DAO（访问数据库接口），DBMange（数据库的基本操作），entity（用户属性的定义），Frame（程序GUI布局与事件监听）以及一个Main（主程序入口组成）

## 我打算从两个底层文件夹讲起

DBManage和entity的作用

### 1.DBManage

```java
public static Connection getConnect()
public static void executeSql(String sql, List list)
public static ResultSet selectMember(String sql, List list)
public static void closeAll()
```

其实很简单，就是

+ 连接数据库
+ 执行sql语句（插入，修改 这个方法无返回值）
+ 查询数据（有返回值，返回查询的对象）
+ 关闭连接

### 2.entity

这个就更简单了，就是普通的属性以及set和get方法。比如设置消费者的年龄用setAge，返回消费者的年龄用getAge就可以了。至于那些构造方法就是设定初始值而已，也不是难点。

## 上述两个文件夹是服务于DAO的

### 以ConsumeDAO为例

```java

public static void addConsume(Consume c){
    String strSql = "insert into  tblConsume values (?,?,?,?,?,?,?)";
    List list = new ArrayList();
    list.add(c.getConmphone());
    list.add(c.getConemid());
    list.add(c.getConserid());
    list.add(new java.sql.Date(c.getCondate().getTime()));
    list.add(c.getConagio());
    list.add(c.getConmoney());
    list.add(c.getConremark());

    DBManage.executeSql(strSql, list);
}
```

以这个插入数据为例，我们给定输入是一个存储消费者信息的对象（定义在entity的Consume类），这个消费者对象实际上是把数据以类似于入栈的方式添加在一个list最后面。你可以理解为类似于python的`list.append(c.getConmphone())`，然后给定sql语句，使用我们的DBManage去执行语句，DBManage执行的时候是把list末尾的数据添加进我们的数据的。

感觉没有什么难点......

**总之在DAO层，我们是定义了实际Frame层可以使用的方法，比如添加一个消费者的信息，管理者的信息，按照某种方式查询消费者的信息，然后去请求本地的数据库来真正地执行这些操作。**

## Frame层

swing程序其实只有两部份构成，界面的布局和事件的监听。

### 界面的布局

这个就都是显而易见的，这次的布局是固定坐标的布局方式，总之这个没有什么可讲的细节。

### 事件的监听

就是根据按下什么样的按钮来执行对数据库的访问，其实应该不怎么好说，涉及了一些swing的基本操作。总之，事件监听的最终目的是把按下的按钮转换为实际的数据库操作。值得一提的是所有的事件监听的方法都使用了内部类（Inner class）而不是lambda表达式的操作，我大致可以估计出这个java程序肯定是使用几年前的jdk。

## Main层

程序的入口，刚开始运行程序时候的界面。