package com.baiyuechu.nio;

import com.baiyuechu.service.ByeService;
import com.baiyuechu.service.HelloService;
import com.baiyuechu.service.impl.ByeServiceImpl;
import com.baiyuechu.service.impl.HelloServiceImpl;
import jdk.jfr.events.SocketReadEvent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    /**
     * 启动
     * @param PORT
     */
    public static void start(int PORT) throws IOException {
        start0(PORT);
    }
    //TODO 当服务消费方下机时 保持开启状态

    /**
     * 真正启动的业务逻辑在这
     * 因为这是简易版那么先把异常丢出去
     * @param port
     */
    private static void start0(int port) throws IOException {
        //创建对应的服务器端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        System.out.println("----------服务提供方启动----------");
        //开启一个选择器
        Selector selector = Selector.open();
        //绑定端口开启
        serverSocketChannel.bind(new InetSocketAddress(port));
        //这里注意要设置非阻塞 阻塞的话 他会一直等待事件或者是异常抛出的时候才会继续 回浪费cpu
        serverSocketChannel.configureBlocking(false);
        //要先设置非阻塞 在注册 如果事先注册在设置非阻塞会报错
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //真正的业务逻辑 就是下面
        //循环等待客户端的连接和检查事件的发生
        while (true) {
            //一秒钟无事发生 就继续
            if (selector.select(1000) == 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("连接到消费端" + socketChannel.socket().getRemoteSocketAddress());
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                if (key.isReadable()) {
                    //反向获取管道
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    //反向获取Buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    //进行调用方法并返回
                    //获得信息
                    StringBuffer stringBuffer = new StringBuffer();
                    int read = 1;
                    while (read != 0) {
                        //先清空 防止残留
                        buffer.clear();
                        read = socketChannel.read(buffer);
                        //添加的时候 根据读入的数据进行
                        stringBuffer.append(new String(buffer.array(), 0, read));
                    }
                    //方法号和信息中间有个#进行分割
                    String msg = stringBuffer.toString();
                    String[] strings = msg.split("#");
                    String response;
                    if (strings.length < 2) {
                        //当出现传入错误的时候 报异常
                        System.out.println("传入错误");
                        throw new RuntimeException();
                    }
                    if (strings[0].equals("1")) {
                        HelloService helloService = new HelloServiceImpl();
                        response = helloService.sayHello(strings[1]);

                    } else if (strings[0].equals("2")) {
                        ByeService byeService = new ByeServiceImpl();
                        response = byeService.sayBye(strings[1]);

                    }else {
                        //当出现传入错误的时候 报异常
                        System.out.println("传入错误");
                        throw new RuntimeException();
                    }
                    String responseMsg = "收到信息" + strings[1] + "来自" + socketChannel.socket().getRemoteSocketAddress();
                    //将调用方法后获得信息回显
                    ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8));
                    //写回信息
                    socketChannel.write(responseBuffer);

                }
                keyIterator.remove();
            }

        }


    }

}
