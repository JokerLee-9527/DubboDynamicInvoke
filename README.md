# DubboDynamicInvoke (初步完成,进一步测试和demo编写中)

## 无需编码写死要调用的接口依赖,动态调用dubbo接口

### 基本实现流程如下:

1. 用自定义的classloader动态加载指定的jar (该jar包含dubbo接口定义)

2. 查找zk中rpc接口,及一组provider.

3. 将接口入参序列化后, 通过netty的异步TCP调用远端接口.

### 使用方法:

1. dubbo动态执行查考test.com.joker.dubbo.dynamic.invoke.DubboDynamicInvokeTest.testDubboDynamicInvokeRunWithDownLoadJar
   
   (测试中的provider可以参考 [GitHub - JokerLee-9527/JokerIntellijPlugin: Intellij Plugin](https://github.com/JokerLee-9527/JokerIntellijPlugin.git)    TestProject)

2. 从zk中获取provider参考
   
   test.com.joker.dubbo.dynamic.invoke.DubboZkTest

3. 动态加载Jar, 并获取class和method参考
   
   test.com.joker.dubbo.dynamic.invoke.DynamicLoadJarTest

4. Telnet命令执行dubbo查考 (中文乱码)
   
   test.com.joker.dubbo.dynamic.invoke.DubboTelnetTest#testExecuteCommandInvokeDubbo

#### TODO LIst

1. 上传maven公共仓库,直接在pom.xml中添加依赖使用.
2. Demo项目编写中

#### 补充

1. dubbo 协议编码参考 [dubbo/ExchangeCodec.java at dd4f6af838772c577c12ad85896267511c494eaa · JokerLee-9527/dubbo · GitHub](https://github.com/JokerLee-9527/dubbo/blob/dd4f6af838772c577c12ad85896267511c494eaa/dubbo-remoting/dubbo-remoting-api/src/main/java/org/apache/dubbo/remoting/exchange/codec/ExchangeCodec.java#L210)



# 开发者

JokerLee  lijian79@gmail.com

蔡超 1134788957@qq.com