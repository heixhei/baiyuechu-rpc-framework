package com.baiyuechu.nio.c3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        try (FileChannel from = new FileInputStream("data.txt").getChannel();
             FileChannel to = new FileOutputStream("to.txt").getChannel()) {
            //效率很高，底层会利用操作系统的零拷贝进行优化，最多2g数据
            long size = from.size();
            //left变量代表还剩余多少字节
            for (long left = size; left > 0; ) {
                System.out.println("position:" + (size - left) + " left:" + left);
                //transferTo(起始值，传输数据大小，目的地)
                left -= from.transferTo((size - left), left, to);
            }
        } catch (IOException e) {
        }
    }
}
