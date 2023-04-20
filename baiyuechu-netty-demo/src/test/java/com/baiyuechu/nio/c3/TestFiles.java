package com.baiyuechu.nio.c3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestFiles {
    public static void main(String[] args) throws IOException {
        //判断路径下文件存不存在
//        Path path = Paths.get("D:\\data.txt");
//        System.out.println(Files.exists(path));

//        创建一级目录
//        Path path = Paths.get("D:\\helloword/d1");
//        System.out.println(path);
//        Files.createDirectory(path);

        //创建多级目录
//        Path path = Paths.get("D:\\helloword/d1");
//        System.out.println(path);
//        Files.createDirectories(path);

        //拷贝文件
//        Path source = Paths.get("data.txt");
//        Path target = Paths.get("D:\\helloword\\d1\\target.txt");
//        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

//        //移动文件
//        Path source = Paths.get("D:\\helloword\\d1\\target.txt");
//        Path target = Paths.get("D:\\helloword\\target.txt");
//        Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);

//        //删除文件
//        Path path = Paths.get("D:/helloword/target.txt");
//        System.out.println(path);
//        Files.delete(path);
        //删除目录
        Path path = Paths.get("D:/helloword/d1");
        Files.delete(path);


    }
}
