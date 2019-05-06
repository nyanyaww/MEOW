# 闲鱼上的html

这个其实不难的，我想你只是缺少一个把html用代码格式打开的工具，我推荐你下载一下vscode。

唯一你需要注意的是这个html把css格式定义在了html的< head >里面，里面的注释相对来说非常详细。值得一提的是里面识别每个标签使用的方法是class而不是id，差别在于class是可重复的，使用id的标签不是可重复的。

比如下面的这个例子

```html
<!-- 这里我们使用了class="left_div2"而不是id="left_div2"  -->
<div class="left_div2">
    <!-- 定义图片资源的相对路径 -->
    <img src="img/5.jpg">
    <h5>
        <a href="#">Safe and convenient</a>
    </h5>
    <p>
        Help you back up the phone number in the phone to a password protected network storage
        space, never lost, only 2 to 3 minutes to complete the backup
    </p>
</div>
```

其他的你有问题问我，我再解答。

基础的语法

## html

```html
<!DOCTYPE html>
<html>
<head>
    <title>Artench Biological Center</title>
    <style type="text/css">
    ···················
    ···················
    ···················
    </style>
</head>
```

1. 额，其实我也不知道怎么说，html的这种`<head>` `<title>`都叫做元素（element），总之html是由许多元素组成的代码块一样的东西。`</head>` `</title>`意味着元素的结束。

   * HTML 元素以开始标签起始`<head>`
   * HTML 元素以结束标签终止`</head>`
   * 元素的内容是开始标签与结束标签之间的内容
   * 某些 HTML 元素具有空内容（empty content）
   * 空元素在开始标签中进行关闭（以开始标签的结束而结束）
   * 大多数 HTML 元素可拥有属性
2. HTML 属性
    * HTML 元素可以设置属性
    * 属性可以在元素中添加附加信息
    * 属性一般描述于开始标签
    * 属性总是以名称/值对的形式出现，比如：* name="value"。

    比如代码里面的这个
    `<div class="header">`，`<div>`是我们所谓的元素的开始标签，`class="header"`是我们元素的属性。你可以理解成`dict['div'] = [class="header"]`

3. 其余的可能css需要讲一讲
总之这个css是内嵌于html的，通常情况下一般是外部样式表，不过这个是直接放在html里面的。

```css
.wrap {
    width: 1000px;
    margin: 0 auto;/* 上下页面空白为 0px,左右页面空白自适应并且相等,可以达到横向居中的效果 */
    height: 1000px;
    }
```

```html
    <div class="wrap">
```

额，就是css的定义了一些效果，html根据class属性去寻找这些效果。

比如定义了warp的css，这种.warp的定义方法叫class选择器，总之记得这个名字就好了。然后你html代码里面如果有warp就会调用css设置的效果。（你发我的代码均使用了class选择器，css是内联样式表的格式）