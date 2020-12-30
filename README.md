# DubboDynamicInvoke

## 无需编码写死要调用的接口依赖,动态调用dubbo接口

### 基本实现流程如下:

1. 用自定义的classloader动态加载指定的jar (该jar包含dubbo接口定义)

2. 查找zk中rpc接口,及一组provider.

3. 将接口入参序列化后, 通过netty的异步TCP调用远端接口.

### 使用方法:

1. dubbo动态执行查考com.joker.dubbo.dynamic.invoke.TestDubboDynamicInvoke#main
   
   (测试中的provider可以参考 [GitHub - JokerLee-9527/JokerIntellijPlugin: Intellij Plugin](https://github.com/JokerLee-9527/JokerIntellijPlugin.git)    TestProject)

#### TODO LIst

1. 封装jar中获取接口及方法.

2. zk中获取rpc接口的provider和method

3. Telnet调用dubbo接口(以前插件中使用, 中文会有乱码一直未解决)

4. 上传maven 公共仓库,直接在pom.xml中添加依赖使用.

5. 