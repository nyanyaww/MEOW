# javaDB的再次整理

自底向上的说明

其实这些我觉得emmmmmm

## 1. Record

```java
void add_strings(String data) {
    try {
        this.data = Arrays.asList(data.split(", "));
    } catch (Exception e) {
        throw new Error(e);
    }
}

String get_strings(int i) {
    return data.get(i);
}

private void read() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("myDataBase > ");
    String line = scanner.nextLine();
    add_strings(line);
}

private void run() {
    while (true) {
        read();
        for (int i = 0; i < data.size(); i++) {
            System.out.print(get_strings(i));
            if (i != data.size() - 1)
                System.out.print(", ");
        }
        System.out.println();
    }
}
```

record类希望把问题简单化，比如你输入`Wanda, fish, ef789`，`add_string`希望可以提取出其中的`Wanda  fish  ef789`，也就是去除了"`,`"这个分隔符。

`get_string()`是读取出某一行的数据。

`read()`就是做了一个读取终端输入的作用。

`run()`是把你所有的输入打印出来

```shell
输入
Fido, dog, ab123
Wanda, fish, ef789
Garfield, cat, ab123

输出
Fido, dog, ab123
Wanda, fish, ef789
Garfield, cat, ab123
```

## 2. Tbale

这边开始有sql的影子了，它旨在把一个txt看成table，table有两种初始化的方法。

```java
void init(String[] fields) {
        table = new TreeMap<>();
        init_fields(fields);
        init_space();
        simple_init();
    }


void init(String[] fields, String[] types, String[] keyT, String[] foreign) {
    table = new TreeMap<>();
    init_fields(fields);
    init_types(types);
    init_keys(keyT);
    init_foreign(foreign);
    init_space();
}
```

第一种是这个table形如

```shell
Fido, dog, ab123
Wanda, fish, ef789
Garfield, cat, ab123
Nimo, fish, cd456
Pink, shark, cd456
```

第二种是table形如

```shell
Id, Name, Kind, Owner
INT, STR, STR, STR
PRIKEY, NOTN, NOTN, NOTN
NULL, NULL, NULL, People(Username)
1, Fido, dog, ab123
2, Wanda, fish, ef789
3, Garfield, cat, ab123
4, Nimo, fish, cd456
5, Pink, shark, cd456
```

我想其中的区别你应该明白了吧。

其余的就是定义了sql常见的一些操作增删改查，除此之外还定义了文件操作和打印操作。

文件操作`file_in`和`file_out`，这个是我们保存数据库的方式，反正就是读取文件和写入文件。只有到这一层的时候，我们才可以保存数据。

```java
void file_in() {
    try {
        File file = new File(database + "/" + table_name + ".txt");
        Scanner in = new Scanner(file);
        List<Record> tests = new ArrayList<>();
        List<String> prikeys = new ArrayList<>();
        String line = in.nextLine();
        String[] fields = line.split(", ");
        line = in.nextLine();
        String[] types = line.split(", ");
        line = in.nextLine();
        String[] keyT = line.split(", ");
        line = in.nextLine();
        String[] foreign = line.split(", ");

        init(fields, types, keyT, foreign);
        while (in.hasNextLine()) {
            line = in.nextLine();
            Record temp = new Record();
            temp.add_strings(line);
            tests.add(temp);
        }
        if (!line.equals(Arrays.toString(foreign).replace("[", "").replace("]", ""))) {
            for (int i = 0; i < col_size; i++) {
                if (keyT[i].equals("PRIKEY")) {
                    for (int j = 0; j < tests.size(); j++) {
                        prikeys.add(tests.get(j).get_strings(i));
                    }
                }
            }
            if (prikeys.size() != 0)
                insert(prikeys, tests);
            else
                insert(tests);
        }
        in.close();
    } catch (Exception e) {
        fail();
    }
}

void file_out() {
    try {
        FileWriter fileout = new FileWriter(database + "/" + table_name + ".txt");
        PrintWriter printfile = new PrintWriter(fileout);
        printfile.println(Arrays.toString(fields).replace("[", "").replace("]", ""));
        printfile.println(Arrays.toString(types).replace("[", "").replace("]", ""));
        printfile.println(Arrays.toString(keyT).replace("[", "").replace("]", ""));
        printfile.println(Arrays.toString(foreign).replace("[", "").replace("]", ""));
        for (Map.Entry<String, Record> entry : table.entrySet()) {
            read_lines(entry.getValue());
            printfile.println(Arrays.toString(els).replace("[", "").replace("]", ""));
        }
        printfile.close();
    } catch (Exception e) {
        fail();
    }
}
```

打印操作

如果没有这个方法，我们打印出来的数据会很难看。

* 没有这个方法

    ```shell
    Id, Name, Kind, Owner
    INT, STR, STR, STR
    PRIKEY, NOTN, NOTN, NOTN
    NULL, NULL, NULL, People(Username)
    1, Fido, dog, ab123
    2, Wanda, fish, ef789
    3, Garfield, cat, ab123
    4, Nimo, fish, cd456
    ```

* 有这个方法

    ```shell
    +----+----------+------+-------+
    | Id | Name     | Kind | Owner |
    +----+----------+------+-------+
    |  1 | Fido     | dog  | ab123 |
    |  2 | Wanda    | fish | ef789 |
    |  3 | Garfield | cat  | ab123 |
    |  4 | Nimo     | fish | cd456 |
    +----+----------+------+-------+
    Query OK (0.004 sec)
    ```

```java
void print_table() {
    int gap = 0;
    print_line();
    for (int i = 0; i < col_size; i++) {
        gap = space[i] - fields[i].length() + 1;
        if (types[i].equals("INT")) {
            gap = gap - 1;
            out.print("| " + String.join("", Collections.nCopies(gap, " ")) + fields[i] + " ");
        } else {
            out.print("| " + fields[i] + String.join("", Collections.nCopies(gap, " ")));
        }
    }
    out.println("|");
    print_line();
    for (Map.Entry<String, Record> entry : table.entrySet()) {
        read_lines(entry.getValue());
        for (int i = 0; i < col_size; i++) {
            gap = space[i] - els[i].length() + 1;
            if (types[i].equals("INT")) {
                gap = gap - 1;
                out.print("| " + String.join("", Collections.nCopies(gap, " ")) + els[i] + " ");
            } else {
                out.print("| " + els[i] + String.join("", Collections.nCopies(gap, " ")));
            }
        }
        out.println("|");
    }
    print_line();
}
```

## Quary

实际上这个不涉及数据的交互，数据的交互在Table层已经做完了，这一层做的是字符串的解析，就是如何把`SELECT * FROM People;`这一行字符串解析成显示People表的数据。这一段在Readme.md有说明，除此之外的东西你不需要关注。

## Over！