package com.baiyuechu.nio.c3;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestPaths {
    public static void main(String[] args) {

//        Path path1 = Paths.get("to.txt");//相对路径 使用 user.dir 环境变量来定位 1.txt
//        System.out.println(path1);
//        Path path2 = Paths.get("D:\\1.txt");//绝对路径 代表了 D:\1.txt
//        System.out.println(path2);
//        Path path3 = Paths.get("D:/1.txt");//绝对路径 同样代表了 D:\1.txt
//        System.out.println(path3);
//        Path path4 = Paths.get("D:\\data", "projects");//代表了 d:\data\projects
//        System.out.println(path4);

        Path path = Paths.get("d:\\data\\projects\\a\\..\\b");
        System.out.println(path);
        System.out.println(path.normalize());


    }
}
