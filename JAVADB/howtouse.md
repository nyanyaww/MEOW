# 说明

## Records

    里面有一个单元测试 具体不细讲了
    javac Record.java
    java Record

## Tables

    TreeMap实现的表

        Table(String table_name, String database) {
            this.table_name = table_name;
            this.database = database;
        }

        void init(String[] fields) {
            table = new TreeMap<String, Record>();
            initfields(fields);
        }

        void initfields(String[] fields) {
            this.fields = fields;
            colsize = fields.length;
        }

    表支持增删改查，没什么好讲的
    这个里面也有一个单元测试
    javac Table.java
    java Table

## Files

    文件读取存储放在了Table类里面，方法名字是filein()和fileout(),','是作为分隔符的

## Print

    也放在了Table类里面
    大概长这个样子
    +----+----------+------+-------+
    | Id | Name     | Kind | Owner |
    +----+----------+------+-------+
    |  1 | Fido     | dog  | ab123 |
    |  2 | Wanda    | fish | ef789 |
    |  3 | Garfield | cat  | ab123 |
    |  4 | Nimo     | fish | cd456 |
    +----+----------+------+-------+

## Keys

    添加在Table类里面的代码
    void init(String[] fields, String[] types, String[] keyT, String[] foreign) {
        table = new TreeMap<String, Record>();
        initfields(fields);
        inittypes(types);
        initkeys(keyT);
        initforeign(foreign);
        initspace();
    }


    Id, Name, Kind, Owner
    INT, STR, STR, STR
    PRIKEY, NOTN, NOTN, NOTN
    NULL, NULL, NULL, People(Username)
    1, Fido, dog, ab123
    2, Wanda, fish, ef789
    3, Garfield, cat, ab123
    4, Nimo, fish, cd456


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

    myDataBase [DBTest]> DROP TABLE IF EXISTS Animal;
    Query OK (0.005 sec)

    myDataBase [DBTest]> CREATE TABLE Animal (Id INTEGER PRIMARY KEY, Name VARCHAR NOT NULL, Kind VARCHAR NOT NULL, Owner VARCHAR NOT NULL, FOREIGN KEY (Owner) REFERENCES People(Username));
    Query OK (0.001 sec)

    myDataBase [DBTest]> CREATE TABLE Animal (Id INTEGER PRIMARY KEY, Name VARCHAR NOT NULL, Kind VARCHAR NOT NULL, Owner VARCHAR NOT NULL, FOREIGN KEY (Owner) REFERENCES Human(Username));
    ERROR: Can't find Foreign keys data
    (<< Foreign table name is wrong)

    myDataBase [DBTest]> INSERT INTO Animal (Id, Name, Kind, Owner) VALUES (1, Fido, dog, ab123);
    Insert time (0.0 sec)

## databse的一些操作

    1.SHOW DATABASES;
    2.SHOW TABLES;
    3.SELECT * FROM Animal;
    4.SELECT * FROM checktime WHERE Id=100000;
    5.SELECT * FROM checktime WHERE name=Stephen100000Stephen;
    6.INSERT INTO checktime (Id, name, gender) VALUES (222, Yuki, female);
    7.DROP TABLE IF EXISTS Animal;
    8.CREATE TABLE Animals (Id INTEGER PRIMARY KEY, Name VARCHAR NOT NULL, Kind VARCHAR NOT NULL, Owner VARCHAR NOT NULL, FOREIGN KEY (Owner) REFERENCES People(Username));
    9.SELECT * FROM People WHERE Name=Sam;
    10.SELECT Id, Name, Kind FROM Animal WHERE Owner=ab123;
    11.quit