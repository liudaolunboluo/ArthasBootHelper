# 

`server`目录内是一个jar包，引入之后就可以自动启动一个arthas，只需要配置http端口即可：

```yml
arthas:
  httpPort: 8083
```

实际上就是把Arthas的starter包装了一层，自己封装了一下arthas的Http接口中，让trace命令执行更简单



`client`目录内是用python写的调用刚刚包装好了的trace命令接口的程序，`ArthasBootHelperClient.py`是启动之后异步执行trace命令，然后每秒钟拉取接口并且解析结果成Arthas客户端那种便于查看的结果：

```
线程名是traffic-2-exec:13:427,执行时间2022-04-24 16:20:43
[660.651129 ms] com.content.service.B$$EnhancerBySpringCGLIB$$acfa2627#getResultsV3
[660.50564 ms] org.springframework.cglib.proxy.MethodInterceptor#intercept
[660.353574 ms] com.fiture.content.taurus.training.resultpage.service.impl.TrainingResultsPageServiceImpl$$EnhancerBySpringCGLIB$$dcc0e34f#getResultsV3
[660.315358 ms] org.springframework.cglib.proxy.MethodInterceptor#intercept
[660.22655 ms] com.content.service.impl.B#getResultsV3
     +[23.734993 ms] com.content.service.impl.B#check lineNumber:154
     +[0.002262 ms] java.util.Objects#isNull lineNumber:155
```

就像这样并且会把解析之后结果自动保存在指定的目录，在代码里配置即可：

```python
# 服务地址和端口就可以了，因为有转发
host = ''
# 要观测的方法
method = ''
# 结果生成文件的保存路径
result_path = ''
```

`AnalysisTime.py`是对上一步生成的结果做的分析的，目前是计算占用时间最多的行数和他们占用时间百分比的中位数，可以自己改造。