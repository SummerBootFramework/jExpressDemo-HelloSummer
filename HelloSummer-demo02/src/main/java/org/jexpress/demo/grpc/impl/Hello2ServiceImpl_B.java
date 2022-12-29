package org.jexpress.demo.grpc.impl;

import org.jexpress.demo.grpc.Hello2Service;
import org.summerboot.jexpress.boot.annotation.Service;

@Service(binding = Hello2Service.class, implTag = "hawaii_2")
public class Hello2ServiceImpl_B extends Hello2Service {

    @Override
    protected String hello(String firstName, String lastName) {
        return "Aloha2 " + firstName + " " + lastName;
    }

}
