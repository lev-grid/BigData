import random

if __name__ == '__main__':
	file = open('files/input.txt', 'w')
	for i in range(5000):
		num = random.randint(1000000000000, 9999999999999)
		file.write(str(num) + "\n")
	# file.close()