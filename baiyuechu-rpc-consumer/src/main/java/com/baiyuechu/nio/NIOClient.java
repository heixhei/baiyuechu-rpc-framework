package com.baiyuechu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class NIOClient {
    public static void start(String HostName, int PORT) throws IOException {
        start0(HostName, PORT);
    }

    /**
     * 真正的启动方法
     *
     * @param hostName
     * @param port
     */
    private static void start0(String hostName, int port) throws IOException {
        //得到一个网络编程
        /**
         * 这段代码创建了一个非阻塞的SocketChannel，并打印出服务消费方启动的信息。
         * 首先，通过socketChannel.open()方法创建一个新的SocketChannel对象，该对象表示一个可以进行读写操作的通道。接着，通过configureBlocking(false)方法将该通道设置为非阻塞的，即在进行I/O操作时不会一直等待，而是立即返回。这样可以提高通信效率，同时可以保证在执行其他任务时不会受到I/O操作的阻塞。
         */

        SocketChannel socketChannel = SocketChannel.open();
        System.out.println("----------服务消费方启动----------");
        socketChannel.configureBlocking(false);
        //建立连接 非阻塞连接 但我们是要等他连接上

        /**
         * 这段代码实现了SocketChannel的连接操作。首先，通过socketChannel.connect(new InetSocketAddress(hostName,port))方法发起连接请求，其中hostName和port分别表示服务器的主机名和端口号。然后，通过if语句判断连接是否建立成功，如果未成功建立连接，则进入while循环直到连接成功建立。
         *
         * 具体地，if语句使用了SocketChannel的connect()方法返回的布尔值进行判断，如果返回false表明连接请求还未完成，需要继续等待连接完成。而while循环则通过不断调用finishConnect()方法来等待连接完成。注意，该方法只在非阻塞模式下才会生效，因为在阻塞模式下，connect()方法本身就会一直等待直到连接完成。
         *
         * 总之，这段代码实现了SocketChannel的连接操作，并且保证了连接的完成和成功。
         */
        if (!socketChannel.connect(new InetSocketAddress(hostName, port))) {
            while (!socketChannel.finishConnect()) ;
        }

        //创建选择器 进行监听读事件
        /**
         * 这段代码创建了一个Selector对象，并将socketChannel注册到该Selector中，同时指定了需要监听的操作类型为SelectionKey.OP_READ，即读取操作。此外，还为读取操作分配了一个大小为1024的ByteBuffer缓冲区。
         *
         * 首先，通过Selector.open()方法创建一个新的Selector对象，用于对多个通道进行轮询，并在通道发生事件时进行处理。接着，通过socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024))方法将SocketChannel对象注册到该Selector对象中，并指定需要监听的事件类型为OP_READ，表示当通道中有数据可以读取时，Selector会通知程序进行读取操作。同时，还为读取操作分配了一个ByteBuffer缓冲区，用于存储从通道中读取的数据。
         *
         * 值得注意的是，这里通过ByteBuffer.allocate(1024)方法为读取操作分配了一个固定大小的缓冲区。在实际情况中，缓冲区的大小应该根据实际需要进行调整，以便更好地适应不同的输入输出流量。
         *
         * 综上所述，这段代码实现了Selector和SocketChannel的注册操作，并为读取操作分配了一个缓冲区。注册完成后，程序可以通过轮询Selector获取通道感兴趣的事件并进行相应的操作。
         */
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

        //创建匿名线程进行监听读事件
        /**
         * 这段代码创建了一个新的线程，并在该线程中实现了对Selector对象的轮询操作，以便监听SocketChannel对象的读事件并进行相应的处理。
         *
         * 首先，在while循环中使用selector.select(1000)方法来等待、监听通道发生的事件，其中1000表示最长等待时间为1秒。如果没有事件发生，则跳过当前循环，继续等待下一次事件。
         *
         * 接着，通过selector.selectedKeys().iterator()方法获取所有已经准备就绪的SelectionKey集合，并遍历这个集合。当有SelectionKey被选中时，通过key.attachment()方法获取与该Key关联的缓冲区ByteBuffer对象，并使用key.channel()方法获取与该Key关联的SocketChannel对象。然后，用while循环读取SocketChannel中的数据，每次读取之前清空缓冲区，并将读取到的数据追加到StringBuffer对象中。当读取完成后，使用System.out.println()方法输出收到的服务端回信，并使用keyIterator.remove()方法将已处理的SelectionKey从集合中删除。
         *
         * 需要注意的是，try-catch语句块中捕获并处理异常，确保程序能够正常运行，并在出现异常时抛出RuntimeException异常。
         *
         * 综上所述，这段代码实现了监听SocketChannel对象的读事件，并在接收到数据时进行相应的处理，从而实现了客户端与服务器之间的通信。
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //捕获异常  监听读事件
                    try {
                        if (selector.select(1000) == 0) {
                            continue;
                        }
                        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                        while (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            SocketChannel channel = (SocketChannel)key.channel();
                            int read = 1;
                            //用这个的原因是怕 多线程出现影响
                            StringBuffer stringBuffer = new StringBuffer();
                            while (read != 0) {
                                buffer.clear();
                                read = channel.read(buffer);
                                stringBuffer.append(new String(buffer.array(), 0, read));
                            }
                            System.out.println("收到服务端回信" + stringBuffer.toString());
                            keyIterator.remove();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
        //真正的业务逻辑 等待键盘上的输入 进行发送信息
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int methodNum = scanner.nextInt();
            String message = scanner.next();
            String msg = new String(methodNum + "#" + message);
            socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
            System.out.println("消息发送");
        }

    }
}
