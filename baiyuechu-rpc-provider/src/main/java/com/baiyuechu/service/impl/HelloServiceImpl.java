package com.baiyuechu.service.impl;

import com.baiyuechu.service.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String str) {
        return "hello" + str;
    }
}
