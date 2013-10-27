package org.apache.thrift.vm.test;

import static junit.framework.Assert.*;

import java.util.concurrent.ExecutionException;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.vm.VMServerTransport;
import org.apache.thrift.vm.VMTransport;
import org.apache.thrift.vm.test.Service.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

public class ServiceHandlerTest {

    private TServer server;
    private Thread thread;

    @BeforeClass
    public static void start() {
        Logger logger = org.slf4j.LoggerFactory.getLogger(ServiceHandlerTest.class);
    }

    @Before
    public void startUp() {
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    TServerTransport serverTransport = new VMServerTransport();
                    TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(serverTransport);
                    serverArgs.processor(new Service.Processor<Service.Iface>(new ServiceHandler()));

                    server = new TThreadPoolServer(serverArgs);
                    System.out.println("Starting the server");
                    server.serve();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Test
    public void multipleClients() throws TException, InterruptedException, ExecutionException {
        VMTransport transport1 = new VMTransport();
        VMTransport transport2 = new VMTransport();

        Client client1 = new Service.Client(new TBinaryProtocol(transport1));
        Client client2 = new Service.Client(new TBinaryProtocol(transport2));

        transport2.open();
        transport1.open();

        assertEquals(10, client1.sum(5, 5));
        transport1.close();

        assertEquals(2, client2.sum(2, 0));
        transport2.close();
    }

    @Test
    public void shouldInvokePing() throws TException, InterruptedException {
        VMTransport transport = new VMTransport();
        Client client = new Service.Client(new TBinaryProtocol(transport));
        transport.open();

        client.ping();

        transport.close();
    }

    @Test
    public void shouldInvokeSum() throws TException, InterruptedException {
        VMTransport transport = new VMTransport();
        Client client = new Service.Client(new TBinaryProtocol(transport));
        transport.open();

        System.out.println("SUM");
        int one = 1;
        int expected = one + one;
        int actual = client.sum(one, one);
        transport.close();

        assertEquals(expected, actual);
    }

    @After
    public void shutDown() {
        server.stop();
        thread.interrupt();
    }

}
