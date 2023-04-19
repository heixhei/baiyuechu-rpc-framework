package com.baiyuechu.bootstrap.nio;

import com.baiyuechu.nio.NIOServer;

import java.io.IOException;

public class NIOProviderBootStrap {
    public static void main(String[] args) throws IOException {
        NIOServer.start(6666);
    }
}
