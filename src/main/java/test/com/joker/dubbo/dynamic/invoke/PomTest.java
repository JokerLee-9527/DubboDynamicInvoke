package test.com.joker.dubbo.dynamic.invoke;

import com.joker.dubbo.dynamic.invoke.model.DependencyModel;
import com.joker.dubbo.dynamic.invoke.model.RepositoryModel;
import com.joker.dubbo.dynamic.invoke.model.StartDownloadJarParam;
import com.joker.dubbo.dynamic.invoke.pom.PomUtil;
import lombok.SneakyThrows;
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
public class PomTest {


    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @SneakyThrows
    @Test
    public void testCreatePom() {

        List<DependencyModel> dependencyModels = new ArrayList<>();

        // 依赖的模块
        DependencyModel dependencyModel = new DependencyModel();
        dependencyModel.setVersion("2.7.8");
        dependencyModel.setGroupId("org.apache.dubbo");
        dependencyModel.setArtifactId("dubbo");
        dependencyModels.add(dependencyModel);

        DependencyModel dependencyModel1 = new DependencyModel();
        dependencyModel1.setVersion("2.1.4-RELEASE");
        dependencyModel1.setGroupId("com.xxxx.xx.xxxxxxx");
        dependencyModel1.setArtifactId("xxx-api");
        dependencyModels.add(dependencyModel1);

        // 如是有私有仓库需要添加.
        List<RepositoryModel> repositoryModelList = new ArrayList<>();
        final RepositoryModel repositoryModel1 = RepositoryModel.builder()
                .id("central1")
                .name("XXX Private Release Repository")
                .url("http://nexus.dev.xxx.com/nexus/content/repositories/releases")
                .build();
        repositoryModelList.add(repositoryModel1);

        final RepositoryModel repositoryModel2 = RepositoryModel.builder()
                .id("central2")
                .name("XXX Private snapshots Repository")
                .url("http://nexus.dev.xxx.com/nexus/content/repositories/snapshots")
                .build();
        repositoryModelList.add(repositoryModel2);

        final StartDownloadJarParam param = StartDownloadJarParam.builder()
                .pomXmlFileName(".\\lib_tem\\pom2.xml")    //pom保存的路径和文件名(相对路径)
                .dependencyModelList(dependencyModels)    //pom->dependency依赖
                .repositoryModelList(repositoryModelList)    //pom->Repository私有仓库
                .build();

        PomUtil.createPom(param);
    }


    @SneakyThrows
    @Test
    public void testStartDownloadJar() {

        List<DependencyModel> dependencyModels = new ArrayList<>();
        DependencyModel dependencyModel = new DependencyModel();
        dependencyModel.setVersion("2.7.8");
        dependencyModel.setGroupId("org.apache.dubbo");
        dependencyModel.setArtifactId("dubbo");


        dependencyModels.add(dependencyModel);

        final StartDownloadJarParam param = StartDownloadJarParam.builder()
                .pomXmlFileName(".\\lib_tem\\pom1.xml")   //pom保存的路径和文件名(相对路径)
                .dependencyModelList(dependencyModels)    //pom->dependency依赖
                //.repositoryModelList()    //pom->Repository私有仓库
                .build();

        // 测试下载包
        PomUtil.startDownloadJar(param);
    }

    @SneakyThrows
    @Test
    public void testStartDownloadJarWithRepository() {

        List<DependencyModel> dependencyModels = new ArrayList<>();
        DependencyModel dependencyModel = new DependencyModel();
        dependencyModel.setVersion("2.7.8");
        dependencyModel.setGroupId("org.apache.dubbo");
        dependencyModel.setArtifactId("dubbo");
        dependencyModels.add(dependencyModel);

        DependencyModel dependencyModel1 = new DependencyModel();
        dependencyModel1.setVersion("2.1.4-RELEASE");
        dependencyModel1.setGroupId("com.xxxxxx.xx.xxxxxxxxxx");
        dependencyModel1.setArtifactId("XXX-api");
        dependencyModels.add(dependencyModel1);

        List<RepositoryModel> repositoryModelList = new ArrayList<>();
        final RepositoryModel repositoryModel1 = RepositoryModel.builder()
                .id("central1")
                .name("xxx Private Release Repository")
                .url("http://nexus.dev.xxx.com/nexus/content/repositories/releases")
                .build();
        repositoryModelList.add(repositoryModel1);

        final RepositoryModel repositoryModel2 = RepositoryModel.builder()
                .id("central2")
                .name("Qianmi Private snapshots Repository")
                .url("http://nexus.dev.xxx.com/nexus/content/repositories/snapshots")
                .build();
        repositoryModelList.add(repositoryModel2);


        final StartDownloadJarParam param = StartDownloadJarParam.builder()
                .pomXmlFileName(".\\lib_tem\\pom1.xml")
                .dependencyModelList(dependencyModels)
                .repositoryModelList(repositoryModelList)
                .build();

        PomUtil.startDownloadJar(param);
    }


}
