package org.apache.thrift.vm.registry;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.thrift.vm.Registry;
import org.apache.thrift.vm.VMTransport;

public class SimpleRegistry implements Registry {

	private BlockingQueue<VMTransport> queue = new LinkedBlockingQueue<VMTransport>();

	@Override
	public void offer(VMTransport transport) {
		queue.add(transport);
	}

	@Override
	public VMTransport take() throws InterruptedException {
		return queue.take();
	}

}
