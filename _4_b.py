import math
from decimal import Decimal
import matplotlib.pyplot as plt
import numpy as np
import pylab

global w0, w1
w0 = 1
w1 = -5
w2 = -1.25

#original lines
#m0 = (0*w1 + w0)
#m1 = (1*w1 + w0)
#plt.plot([m0, m1])

#Original plots - Figure 3

#Class +1
plt.plot(0.08, 0.72, '+', markerfacecolor='none')
plt.plot(0.26, 0.58, '+', markerfacecolor='none')
plt.plot(0.45, 0.15, '+', markerfacecolor='none')
plt.plot(0.60, 0.30, '+', markerfacecolor='none')

#Class -1
plt.plot(0.10, 1.00, '*', markerfacecolor='black')
plt.plot(0.35, 0.95, '*', markerfacecolor='black')
plt.plot(0.70, 0.65, '*', markerfacecolor='black')
plt.plot(0.92, 0.45, '*', markerfacecolor='black')

x = np.linspace(0, 1, 10)
y = np.linspace(0, 1, 10)
print(str(x) + "\n" + str(y))
plt.plot( 1+ w1*x + w2*y )
pylab.ylim([0,1.1])
pylab.xlim([0,1])
plt.show()
