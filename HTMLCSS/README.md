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
