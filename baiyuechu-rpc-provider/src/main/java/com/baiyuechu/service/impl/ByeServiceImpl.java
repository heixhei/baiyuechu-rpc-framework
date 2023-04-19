package com.baiyuechu.service.impl;

import com.baiyuechu.service.ByeService;

public class ByeServiceImpl implements ByeService {

    @Override
    public String sayBye(String str) {
        return "Bye" + str;
    }
}
