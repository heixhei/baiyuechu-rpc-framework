package com.baiyuechu.nio.c2;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestByteBufferAllocate {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        /*
            class java.nio.HeapByteBuffer       - java堆内存,读写效率较低,受到GC的影响
            class java.nio.DirectByteBuffer     - 直接内存,读写效率高(少一次拷贝),不会受到GC影响,分配的效率低
         */
        System.out.println(ByteBuffer.allocate(10).getClass());
        System.out.println(ByteBuffer.allocateDirect(16).getClass());

        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
//            int write = channel.write(buffer);
            int read = channel.read(buffer);
            System.out.println(buffer.get());

        } catch (IOException e) {
        }

    }
}
