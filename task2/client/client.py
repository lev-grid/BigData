import threading
from queue import Queue

import grpc

import simple_pb2_grpc
import simple_pb2

def worker():
	while True:
		cur = nq.get()
		request = simple_pb2.FactorRequest(num = cur)
		result = stub.getFactorization(request).result
		line = str(cur) + "\t" + result + "\n"
		output.write(line)
		print(line, end = '')
		nq.task_done()

if __name__ == '__main__':
	output = open('files/output.txt', 'w')

	channel = grpc.insecure_channel('localhost:5757')
	stub = simple_pb2_grpc.SampleServiceStub(channel)

	nq = Queue()
	numThreads = 5
	for i in range(numThreads):
		t = threading.Thread(target = worker)
		t.daemon = True
		t.start()

	f = open('files/input.txt')
	for line in f:
		nq.put(int(line))

	nq.join()
	output.close()