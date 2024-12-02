package org.jexpress.demo.grpc.impl;

import io.grpc.Status;
import org.jexpress.demo.grpc.Hello1Service;
import org.summerboot.jexpress.boot.BootConstant;
import org.summerboot.jexpress.boot.annotation.Service;

@Service(binding = Hello1Service.class)
public class Hello1ServiceImpl_A extends Hello1Service {

    @Override
    protected String hello(String firstName, String lastName) throws Throwable {
        if (firstName.equals("err")) {
            Status status = Status.INVALID_ARGUMENT.withDescription("my status ex");
            throw status.asException();
        }
        return BootConstant.APP_ID + " Hello1 " + firstName + " " + lastName;
    }

}
