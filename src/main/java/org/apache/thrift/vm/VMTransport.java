package org.apache.thrift.vm;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.vm.registry.StaticRegistry;

public class VMTransport extends TTransport {

    private final PipedInputStream in;
    private final PipedOutputStream out;

    private Registry registry;
    private boolean connected;

    public VMTransport() {
        this(new StaticRegistry());
    }

    public VMTransport(Registry registry) {
        this.registry = registry;
        in = new PipedInputStream();
        out = new PipedOutputStream();
    }

    public VMTransport(PipedInputStream in, PipedOutputStream os) {
        this.in = in;
        this.out = os;
    }

    @Override
    public boolean isOpen() {
        return connected;
    }

    @Override
    public void open() throws TTransportException {
        registry.offer(this);
    }

    @Override
    public void close() {
        try {
            in.close();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int read(byte[] buf, int off, int len) throws TTransportException {
        try {
            return in.read(buf, off, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(byte[] buf, int off, int len) throws TTransportException {
        try {
            out.write(buf, off, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PipedInputStream getInputStream() {
        return in;
    }

    public PipedOutputStream getOutputStream() {
        return out;
    }

}
