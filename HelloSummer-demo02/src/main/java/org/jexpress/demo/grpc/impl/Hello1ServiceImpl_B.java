package org.jexpress.demo.grpc.impl;

import org.jexpress.demo.grpc.Hello1Service;
import org.summerboot.jexpress.boot.BootConstant;
import org.summerboot.jexpress.boot.annotation.Service;

@Service(binding = Hello1Service.class, implTag = "hawaii_1")
public class Hello1ServiceImpl_B extends Hello1Service {

    @Override
    protected String hello(String firstName, String lastName) {
        return BootConstant.APP_ID + " Aloha1 " + firstName + " " + lastName;
    }

}
