package org.apache.thrift.vm;

public interface Registry {

    void offer(VMTransport transport);

    VMTransport take() throws InterruptedException;

}
