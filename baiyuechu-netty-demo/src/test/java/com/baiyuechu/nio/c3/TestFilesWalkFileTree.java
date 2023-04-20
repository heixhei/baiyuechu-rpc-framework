package com.baiyuechu.nio.c3;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class TestFilesWalkFileTree {
    public static void main(String[] args) throws IOException {
//        testCountJar();
//        testDeleteMulitFile();
        testCopyMulitDirectory();
    }

    public static void testCopyMulitDirectory() throws IOException {

            String source = Paths.get("D:\\IDM").toString();
            String target = Paths.get("D:\\IDMaaa").toString();
            Files.walk(Paths.get(source)).forEach(path -> {
                try {
                    String targetPath = path.toString().replace(source, target);
                    //是目录
                    if (Files.isDirectory(path)) {
                        Files.createDirectory(Paths.get(targetPath));
                    }
                    //普通文件
                    else if (Files.isRegularFile(path)) {
                        Files.copy(path, Paths.get(targetPath));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

    }

    public static void testDeleteMulitFile() throws IOException {
        Path path = Paths.get("D:\\test");
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
//            @Override
//            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//                System.out.println("====>进入"+dir);
//                return super.preVisitDirectory(dir, attrs);
//            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                System.out.println(file);
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//                System.out.println("<====退出"+dir);
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }
    public static void testCountJar() throws IOException {
        Path path = Paths.get("C:\\Users\\hehe\\.jdks\\corretto-1.8.0_322");
        AtomicInteger jarCount = new AtomicInteger();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".jar")) {
                    System.out.println(file.toString());
                    jarCount.incrementAndGet();
                }
                return super.visitFile(file, attrs);
            }
        });
        System.out.println(jarCount);

    }
    public static void testScanDirANDFile() throws IOException {
        Path path = Paths.get("C:\\Users\\hehe\\.jdks\\corretto-1.8.0_322");
        //计数器
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                //以原子方式将当前值+1
                dirCount.incrementAndGet();
                System.out.println("====>" + dir);
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                fileCount.incrementAndGet();
                System.out.println(file);
                return super.visitFile(file, attrs);
            }
        });
        System.out.println("目录个数:"+dirCount);
        System.out.println("文件个数:"+fileCount);
    }
}
