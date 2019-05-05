# JAVA DATABASE

这个程序由四个类构成Database, Query, Table and Record

Record将用户输入扫描为一条记录，并显示它被正确保存，还包含一个自动化的单元测试。运行表自动检查内部的单元测试。

* 类型：integer/string, null/not null, foreign key
* 约束：foreign key, data types
* 目录：数据库和表的列表，就是database和table
* 查询：使用MySQL方式查询
* 事务：数据存储在txt中，格式为MariaDB
  
## Record

Record类用于保存表的每个记录，并在需要时导出它。List用于将多个字符串存储到一条记录中，每个字符串可以从外部读取一条记录，以防止返回ArrayList本身的危险。记录用它所属的表名初始化。它创建一个记录，以便知道它在哪个表中。检查记录类运行情况:

## Table

这个是我以前报告写的一个分析增删改查操作效率的表格

<table style="float:left" class="indent"><tbody>
<tr><th></th><th>Insert</th><th colspan="2">Select/Delete/Update</th><th>Order provided</th></tr>
<tr><td></td><td></td><td>With DB keys</td><td>W/O DB keys</td><td></td></tr>
<tr><td class="t1">HashMap</td><td>O(1)</td><td>O(1)</td><td>O(n)</td><td>-</td></tr>
<tr><td class="t1">TreeMap</td><td>O(log n)</td><td>O(log n)</td><td>O(n)</td><td>Natural Order</td></tr>
<tr><td class="t1">Linked-HashMap</td><td>O(1)</td><td>O(1)</td><td>O(n)</td><td>Insertion Order</td></tr>
<tr><td class="t1">ArrayList</td><td>O(1)</td><td>O(n)</td><td>O(n)</td><td>-</td></tr>
</tbody></table>

最后的选择是使用TreeMap。选择TreeMap的原因:首先，它可以在每次操作时生成自然的顺序，而实际的数据库系统也提供了这一功能，方便用户检查自己的数据。其次，与HashMap相比，它的内存消耗相对节省。最后，数据库中比较常见的DB操作的时间复杂度是比较合理和有效的。

在Table类中，表的构造函数需要表和数据库名，所以我们就可以查找表所属的表和数据库名。此外，在此阶段将使用字段名初始化表。此信息使表知道它在哪个数据库中，并支持创建具有给定列名的表。如果添加了新列，字段大小可以单独更改。Table类在尝试任何操作时检查字段大小，它还可以修改列大小和数据。

总之我想说的是我在这里定义了存储table的数据结构，就是TreeMap，你就理解成python的dict吧，虽然不是一样的东西，总之我们选了一种速度不是最快但是还算稳定的数据结构，速度快的需要消耗的内存也大，我们选了一种折中的方案。

若要同时处理多条记录，所有用于操作的函数都将获得参数List。就相当于把这一系列增删改查的函数放在了List里面，这个和python的list差不多。

表支持插入、删除、更新和选择记录的主要操作。如果那个操作的对象没有key，那么把它在txt的行号作为key。

## Quary

在这个类当中我们实现的输入字符串的解析，在程序运行的时候我们主要是利用了这个方法去解析字符串的。

```java
void parse(List<String> command, String database) {
        startTime = System.currentTimeMillis();
        this.command = command;
        this.database = database;

        if (command.size() == 0 || command.get(0).contains(";")) {
            invalid();
            return;
        }
        if (command.get(0).equals("quit"))
            quit_db();
        if (command.size() >= 1) {
            String first_query= command.get(0);
            String second_query= command.get(1);
            if (first_query.equals("SHOW") && second_query.equals("TABLES;")) {
                show_tables(find_tables(database), database);
            } else if (first_query.equals("DROP") && second_query.equals("TABLE")) {
                drop_tables();
            } else if (first_query.equals("CREATE") && second_query.equals("TABLE") && command.size() > 3) {
                try {
                    List<Table> new_tables = new ArrayList<>();
                    new_tables.add(create_tables());
                } catch (Exception e) {
                    out.println("ERROR: Can't create tables");
                    return;
                }
                if (constraints && check_table_name)
                    end_check_time();
                else if (!check_table_name) {
                    return;
                }
                else {
                    out.println("ERROR: Can't find Foreign keys data");
                }
            } else if (first_query.equals("INSERT") && second_query.equals("INTO")) {
                insert_into();
            } else if (command.size() > 2 && first_query.equals("SELECT") && command.contains("FROM")) {
                if (second_query.equals("*") && (!command.contains("WHERE"))) {
                    show_whole_table();
                } else if (command.contains("WHERE")) {
                    show_selected_table();
                }
            } else {
                invalid();
                return;
            }
        } else {
            invalid();
            return;
        }
    }
```

比如下面这段话，我们把`SELECT * FROM People;`，分割成SELCET，*，FROM和People四个部分，根据不同的字符串来执行不同的方法。

    myDataBase [DBTest]> SELECT * FROM People;
    +----------+------+
    | Username | Name |
    +----------+------+
    | ab123    | Jo   |
    | cd456    | Sam  |
    | ef789    | Amy  |
    | gh012    | Pete |
    +----------+------+
    Query OK (0.004 sec)

## Database

程序的主入口，值得注意的是，你需要首先1.SHOW DATABASES;
其次2.SHOW TABLES;之后才是正常的数据的操作。

在这个类当中定义了的用法
* SHOW DATABASES;
* SHOW TABLES;
* quit;