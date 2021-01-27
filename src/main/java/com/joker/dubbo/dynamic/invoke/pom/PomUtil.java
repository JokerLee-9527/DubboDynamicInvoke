package com.joker.dubbo.dynamic.invoke.pom;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.joker.dubbo.dynamic.invoke.model.DependencyModel;
import com.joker.dubbo.dynamic.invoke.model.RepositoryModel;
import com.joker.dubbo.dynamic.invoke.model.StartDownloadJarParam;
import com.joker.dubbo.dynamic.invoke.util.FileUtil;
import com.joker.dubbo.dynamic.invoke.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;

import static com.joker.dubbo.dynamic.invoke.util.OsUtil.isOSLinux;
import static com.joker.dubbo.dynamic.invoke.util.OsUtil.isOSMac;

/**
 * @author JokerLee
 * @version V1.0
 * @Description:
 * @date 2021/1/4 16:23
 */
@Slf4j
public class PomUtil {


    private final static String POM_XML_ORG = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
            "    <modelVersion>4.0.0</modelVersion>\n" +
            "    <groupId>com.github.JokerLee-9527</groupId>\n" +
            "    <artifactId>dubbo-dynamic-invoke</artifactId>\n" +
            "    <version>1.0-SNAPSHOT</version>\n" +
            "    <repositories>\n" +
            "    </repositories>\n" +
            "    <dependencies>\n" +
            "    </dependencies>\n" +
            "</project>\n";

    /**
     * 通过定义的依赖,下载相关的Jar包
     *
     * @param startDownloadJarParam
     */
    @SneakyThrows
    public static void startDownloadJar(StartDownloadJarParam startDownloadJarParam) {
        String fileName = startDownloadJarParam.getPomXmlFileName();
        String filePath = FileUtil.getFilePath(fileName);
        FileUtil.deleteDirectory(filePath);
        FileUtil.createDirectory(filePath);

        PomUtil.createPom(startDownloadJarParam);
        PomUtil.downloadJar(fileName, null);
        Thread.sleep(10000);
    }

    /**
     * 构造pom.xml文件,供mvn下载Jar包使用
     *
     * @param startDownloadJarParam
     * @throws Exception
     */
    public static void createPom(StartDownloadJarParam startDownloadJarParam) throws Exception {

        final String pomXmlPath = startDownloadJarParam.getPomXmlFileName();
        final List<DependencyModel> dependencyModelList = startDownloadJarParam.getDependencyModelList();
        final List<RepositoryModel> repositoryModelList = startDownloadJarParam.getRepositoryModelList();

        File file = new File(pomXmlPath);

        // 1.得到DOM解析器的工厂实例
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // 2.从DOM工厂里获取DOM解析器
        DocumentBuilder db = dbf.newDocumentBuilder();
        // 3.解析XML文档，得到document，即DOM树

        Document doc = db.parse(new ByteArrayInputStream(POM_XML_ORG.getBytes()));

        if (CollectionUtils.isNotEmpty(repositoryModelList)) {

            Element repositoryRootDependency = (Element) doc.getElementsByTagName("repositories").item(0);

            for (RepositoryModel repositoryModel : repositoryModelList) {
                // 创建节点
                Element repositoryElement = doc.createElement("repository");
                // 创建id节点
                Element repositoryIdElement = doc.createElement("id");
                repositoryIdElement.appendChild(doc.createTextNode(repositoryModel.getId()));
                // 创建name节点
                Element repositoryNameElement = doc.createElement("name");
                repositoryNameElement.appendChild(doc.createTextNode(repositoryModel.getName()));
                // 创建url节点
                Element repositoryUrlElement = doc.createElement("url");
                repositoryUrlElement.appendChild(doc.createTextNode(repositoryModel.getUrl()));
                // 添加父子关系
                repositoryElement.appendChild(repositoryIdElement);
                repositoryElement.appendChild(repositoryNameElement);
                repositoryElement.appendChild(repositoryUrlElement);
                // 追加节点
                repositoryRootDependency.appendChild(repositoryElement);
            }
        }


        Element dependencyRootDependency = (Element) doc.getElementsByTagName("dependencies").item(0);

        for (DependencyModel model : dependencyModelList) {

            // 创建节点
            Element dependencyElement = doc.createElement("dependency");
            // 创建group节点
            Element groupElement = doc.createElement("groupId");
            groupElement.appendChild(doc.createTextNode(model.getGroupId()));
            // 创建artifactId节点
            Element artifactIdElement = doc.createElement("artifactId");
            artifactIdElement.appendChild(doc.createTextNode(model.getArtifactId()));
            // 创建version节点
            Element versionElement = doc.createElement("version");
            versionElement.appendChild(doc.createTextNode(model.getVersion()));
            // 添加父子关系
            dependencyElement.appendChild(groupElement);
            dependencyElement.appendChild(artifactIdElement);
            dependencyElement.appendChild(versionElement);
            // 追加节点
            dependencyRootDependency.appendChild(dependencyElement);
        }
        // 保存到xml文件
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        // 格式化
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        // 设置编码类型
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        DOMSource domSource = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileOutputStream(file));
        // 把DOM树转换为xml文件
        transformer.transform(domSource, result);
    }


    /**
     * 根据pom.xml,通过mvn命令,下载Jar包
     *
     * @param pomXmlPath
     * @param savePath
     */
    @SneakyThrows
    public static void downloadJar(String pomXmlPath, String savePath) {

        String command;


        if (StringUtils.isBlank(savePath)) {
            savePath = ".\\";
        }

        if (isOSLinux()) {
            command = StringUtil.format("/bin/bash -c  mvn dependency:copy-dependencies -DoutputDirectory={} -DincludeScope=compile -f {}", savePath, pomXmlPath);
        } else if (isOSMac()) {
            command = StringUtil.format("mvn dependency:copy-dependencies -DoutputDirectory={} -DincludeScope=compile -f {}", savePath, pomXmlPath);
        } else {
            command = StringUtil.format("cmd /c  mvn dependency:copy-dependencies -DoutputDirectory={} -DincludeScope=compile -f {}", savePath, pomXmlPath);
        }
        try {
            Process process = Runtime.getRuntime().exec(command);
            final InputStream inputStream = process.getInputStream();
            //获取进程的错误流
            final InputStream errorStream = process.getErrorStream();


            StringBuilder inputStr = new StringBuilder();
            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
            new Thread(() -> {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(inputStream));
                try {
                    String line1 = br1.readLine();
                    while (line1 != null) {
                        inputStr.append(line1).append(System.lineSeparator());
                        line1 = br1.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            StringBuilder errorStr = new StringBuilder();
            new Thread(() -> {
                BufferedReader br2 = new BufferedReader(new InputStreamReader(errorStream));
                try {
                    String line2 = br2.readLine();
                    while (line2 != null) {
                        errorStr.append(line2).append(System.lineSeparator());
                        line2 = br2.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        errorStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            process.waitFor();

            log.info("downloadJar output:" + inputStr.toString());
            log.info("downloadJar error:" + errorStr.toString());
        } catch (IOException e) {
//            log.error(StringUtil.format("can't execute the command {}", command), e);
            throw e;
        }
    }

}
