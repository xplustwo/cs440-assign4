import math
from decimal import Decimal
import matplotlib.pyplot as plt

global w0, w1, w2
w0 = 0.2
w1 = 1
w2 = -1


m0 = (0*w1 + w0) / -w2
m1 = (1*w1 + w0) / -w2
plt.plot([m0, m1])

#Original plots - Figure 3

#Class +1
plt.plot(0.08, 0.72, '+', markerfacecolor='blue')
plt.plot(0.26, 0.58, '+', markerfacecolor='blue')
plt.plot(0.45, 0.15, '+', markerfacecolor='blue')
plt.plot(0.60, 0.30, '+', markerfacecolor='blue')

#Class -1
plt.plot(0.10, 1.00, '*', markerfacecolor='black')
plt.plot(0.35, 0.95, '*', markerfacecolor='black')
plt.plot(0.70, 0.65, '*', markerfacecolor='black')
plt.plot(0.92, 0.45, '*', markerfacecolor='black')

#put them in lists to help with classification automation
x1 = [0.08, 0.26, 0.45, 0.60]
y1 = [0.72, 0.58, 0.15, 0.30]
x_1 = [0.10, 0.35, 0.70, 0.92]
y_1 = [1.00, 0.95, 0.65, 0.45]

plt.show()


print("Are there misclassified point(s)?")
print("1: yes")
print("2: no")
ans = raw_input("Enter: ")
while (int(ans) == 1):
    global w0
    global w1
    global w2
    x = raw_input("Enter i1 value: ")
    y = raw_input("Enter i2 value: ")
    desired = raw_input("1 or -1: ")
    x = float(x)
    print(x)
    y = float(y)
    desired = int(desired)

    t = w1*x + w2*y + w0
    '''
    if t > 0:
        t = 1
    else:
        t = -1
    '''
	#set g to be a sigmoid, which is standard (step function)
    #g = 2/(1 + math.exp(-t)) -1

    #dg = (2*math.exp(-t))/pow((1 + math.exp(-t)),2)
    g = 1/(1 + math.exp(-t))
    dg = g*(1-g) #derivative of sigmoid
    a = 0.5

    print
    print(w0)

    print(w1)

    print(w2)
    print


    w0 = w0 + a*(desired - t)
    w1= w1 + a*(desired - t)*x
    w2 = w2 + a*(desired - t)*y

    print(w0)

    print(w1)

    print(w2)

    print

    print t

	#reprint
    m0 = (0 * w1 + w0) / -w2
    m1 = (1 * w1 + w0) / -w2
    plt.plot([m0, m1])

    #Class +1
    plt.plot(0.08, 0.72, '+', markerfacecolor='blue')
    plt.plot(0.26, 0.58, '+', markerfacecolor='blue')
    plt.plot(0.45, 0.15, '+', markerfacecolor='blue')
    plt.plot(0.60, 0.30, '+', markerfacecolor='blue')
    #Class -1
    plt.plot(0.10, 1.00, '*', markerfacecolor='black')
    plt.plot(0.35, 0.95, '*', markerfacecolor='black')
    plt.plot(0.70, 0.65, '*', markerfacecolor='black')
    plt.plot(0.92, 0.45, '*', markerfacecolor='black')

    plt.show()

    print("Incorrect point?")
    print("1: yes")
    print("2: no")
    ans = raw_input("Enter: ")
