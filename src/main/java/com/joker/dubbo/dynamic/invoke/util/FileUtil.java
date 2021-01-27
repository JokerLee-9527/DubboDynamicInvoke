package com.joker.dubbo.dynamic.invoke.util;

import lombok.SneakyThrows;

import java.io.File;

/**
 * @author JokerLee
 * @version V1.0
 * @Description: 文件操作工具类
 * @date 2021/1/6 10:39
 */
public class FileUtil {


    @SneakyThrows
    public static String getFilePath(String strFile) {
        final File file = new File(strFile);
//        System.out.println(file.getCanonicalPath());
//        System.out.println(file.getAbsolutePath());
//        System.out.println(file.getPath());
        return file.getParentFile().getPath();
    }


    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 创建目录
     *
     * @param directoryPath
     */
    public static void createDirectory(String directoryPath) {
        File dir = new File(directoryPath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
        }
    }

//    public static void main(String[] args) {
//        final String filePath = getFilePath(".\\lib_temp\\" + "pom.xml");
//        System.out.println(filePath);
//    }

}
