package org.jexpress.demo.grpc.impl;

import org.jexpress.demo.grpc.Hello2Service;
import org.summerboot.jexpress.boot.annotation.Service;

@Service(binding=Hello2Service.class)
public class Hello2ServiceImpl_A extends Hello2Service {

    @Override
    protected String hello(String firstName, String lastName) {
        return "Hello2 " + firstName + " " + lastName;
    }

}
