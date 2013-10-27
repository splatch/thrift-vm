namespace java org.apache.thrift.vm.test

service Service {
	void ping();
	i32 sum(1:i32 a, 2:i32 b);
}
