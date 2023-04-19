package com.baiyuechu.nio.c2;

import java.nio.ByteBuffer;

import static com.baiyuechu.nio.c2.ByteBufferUtil.debugAll;

public class TestByteBufferRead {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();
//        rewind从头开始读
//        buffer.get(new byte[4]);
//        debugAll(buffer);
//        buffer.rewind();
//        debugAll(buffer);
//        System.out.println((char) buffer.get());

        //mark & reset
        //mark 做一个标记,记录position位置,reset 是将position重置到mark位置

//        System.out.println((char) buffer.get());
//        System.out.println((char) buffer.get());
//        buffer.mark();
//        System.out.println((char) buffer.get());
//        System.out.println((char) buffer.get());
//        buffer.reset();
//        System.out.println((char) buffer.get());
//        System.out.println((char) buffer.get());

        //get(i)
        System.out.println((char) buffer.get(3));
        debugAll(buffer);

    }
}
