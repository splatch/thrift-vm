package org.apache.thrift.vm.test;

import org.apache.thrift.TException;

public class ServiceHandler implements Service.Iface {

    @Override
    public void ping() throws TException {
        System.err.println("HI!");
    }

    @Override
    public int sum(int a, int b) throws TException {
        return a + b;
    }

}