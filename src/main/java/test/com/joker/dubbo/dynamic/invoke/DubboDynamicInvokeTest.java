package test.com.joker.dubbo.dynamic.invoke;

import com.joker.dubbo.dynamic.invoke.DubboDynamicInvoke;
import com.joker.dubbo.dynamic.invoke.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JokerLee
 * https://github.com/JokerLee-9527
 * @version V1.0
 * @Description: DubboZkTest
 * @date 2020/12/30 16:31
 */
public class DubboDynamicInvokeTest {


    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testDubboDynamicInvokeRun() {

        final DubboDynamicInvokeParam param = DubboDynamicInvokeParam.builder()
                .interfaceName("com.joker.plugin.dubbo.api.DemoServiceProvider")    //接口名称
                .methodName("sayHello")    //方法名称
                .jsonParam("[{\"birthday\":\"\",\"nickName\":\"昵称(微信昵称、手机号码)\",\"logo\":\"头像\"},\"\"]")    //参数的json字符串
                .jarPath("D:\\temp\\JokerIntellijPlugin\\test_project\\dubbo-api\\target")    //jar包的路径
                .dubboProtocol("dubbo")
                .dubboVersion("1.0")
                .zkHost("172.19.28.232")
                .zkPort(2181)
                .build();


        final String run = DubboDynamicInvoke.run(param);
        System.out.println(run);

    }

    @Test
    public void testDubboDynamicInvokeRunWithDownLoadJar() {

        List<DependencyModel> dependencyModels = new ArrayList<>();

        // 依赖的模块
        DependencyModel dependencyModel1 = DependencyModel.builder()
                .groupId("com.xxxxx.xx.xxxxxxx")
                .artifactId("xxx-api")
                .version("2.1.4-RELEASE")
                .build();
        dependencyModels.add(dependencyModel1);

        // 有时候一些parent的包也需要显示依赖,不然会少一些Jar包
        DependencyModel dependencyModel2 = DependencyModel.builder()
                .groupId("com.xxxxx")
                .artifactId("centre-parent")
                .version("1.0.5-RELEASE")
                .build();
        dependencyModels.add(dependencyModel2);


        // 如是有私有仓库需要添加.
        List<RepositoryModel> repositoryModelList = new ArrayList<>();
        final RepositoryModel repositoryModel1 = RepositoryModel.builder()
                .id("xxxxx")
                .name("xxxxx Private Release Repository")
                .url("http://nexus.dev.xxxxx.com/nexus/content/repositories/releases")
                .build();
        repositoryModelList.add(repositoryModel1);

        final RepositoryModel repositoryModel2 = RepositoryModel.builder()
                .id("xxxxx")
                .name("xxxxx Private snapshots Repository")
                .url("http://nexus.dev.xxxxx.com/nexus/content/repositories/snapshots")
                .build();
        repositoryModelList.add(repositoryModel2);

        final StartDownloadJarParam startDownloadJarParam = StartDownloadJarParam.builder()
                .pomXmlFileName(".\\lib_tem\\pom1.xml")
                .dependencyModelList(dependencyModels)
                .repositoryModelList(repositoryModelList)
                .build();

        DubboDynamicInvokeWithDownloadJarParam param = new DubboDynamicInvokeWithDownloadJarParam(startDownloadJarParam);
        // dubbo的provider
        param.setInterfaceName("com.xxxx.xx.xxxxProvider");
        // 方法名
        param.setMethodName("methodName");
        // 参数的json串
        param.setJsonParam("{}");
        // dubbo的协议
        param.setDubboProtocol("dubbo");
        // dubbo的版本
        param.setDubboVersion("1.0.0");
        // 注册的zk的ip
        param.setZkHost("172.19.67.126");
        // 注册zik的端口
        param.setZkPort(2181);

        // 1. 构建pom.xml
        // 2. 通过mvn下载jar
        // 3. 自定义classloader中加载class
        // 4. 序列化对象
        // 5. netty tcp实现远程rpc调用
        final String run = DubboDynamicInvoke.runWithDownLoadJar(param);
        System.out.println(run);
    }





}
