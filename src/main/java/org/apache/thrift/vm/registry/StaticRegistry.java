package org.apache.thrift.vm.registry;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.thrift.vm.Registry;
import org.apache.thrift.vm.VMTransport;

public class StaticRegistry implements Registry {

    private final static BlockingQueue<VMTransport> QUEUE = new LinkedBlockingQueue<VMTransport>();

    @Override
    public void offer(VMTransport transport) {
        QUEUE.add(transport);
    }

    @Override
    public VMTransport take() throws InterruptedException {
        return QUEUE.take();
    }

}
