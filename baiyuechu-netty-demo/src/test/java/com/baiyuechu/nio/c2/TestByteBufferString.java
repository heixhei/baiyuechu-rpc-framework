package com.baiyuechu.nio.c2;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static com.baiyuechu.nio.c2.ByteBufferUtil.debugAll;

public class TestByteBufferString {
    public static void main(String[] args) {
        //1.字符串转化为ByteBuffer
        ByteBuffer buffer1 = ByteBuffer.allocate(16);
        buffer1.put("hello".getBytes());
        debugAll(buffer1);

        //2.charset
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");
        debugAll(buffer2);


        //3.wrap
        ByteBuffer buffer3 = ByteBuffer.wrap("hello".getBytes());
        debugAll(buffer3);

        //StandardCharsets解码
        String s = StandardCharsets.UTF_8.decode(buffer2).toString();
        System.out.println(s);

        //实例1ByteBuffer还在写状态,读取会报错
        buffer1.flip();
        String s1 = StandardCharsets.UTF_8.decode(buffer1).toString();
        System.out.println(s1);

    }



}
