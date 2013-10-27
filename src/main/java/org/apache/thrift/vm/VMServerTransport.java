package org.apache.thrift.vm;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.vm.registry.StaticRegistry;

public class VMServerTransport extends TServerTransport {

    private final Registry registry;

    public VMServerTransport() {
        this(new StaticRegistry());
    }

    public VMServerTransport(Registry registry) {
        this.registry = registry;
    }

    @Override
    public void listen() throws TTransportException {
        
    }

    @Override
    public void close() {

    }

    @Override
    protected TTransport acceptImpl() throws TTransportException {
        try {
            VMTransport clientTransport = registry.take();
            return new VMTransport(
                new PipedInputStream(clientTransport.getOutputStream()),
                new PipedOutputStream(clientTransport.getInputStream())
            );
        } catch (IOException e) {
            throw new TTransportException(TTransportException.NOT_OPEN);
        } catch (InterruptedException e) {
            throw new TTransportException(TTransportException.TIMED_OUT, e);
        }
    }

}
