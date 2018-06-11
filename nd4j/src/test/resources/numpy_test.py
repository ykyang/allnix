import numpy as np
import time

np.__config__.show()

length = 1000000
iteration = 100000

a = np.random.rand(length)
b = np.random.rand(length)
tc = 0
total = 0
for i in range(iteration):
    t1 = time.time()
    c = np.dot(a, b)
    t2 = time.time()
    tc += c
    total += (t2-t1)

print("Numpy total   time: {} sec".format(total))
print("Numpy average time: {} msec".format(total/iteration * 1000))
#print(total/iteration * 1000)
print("Value: {}".format(c))
print("Total Value: {}".format(tc))
