import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
from mpl_toolkits.axes_grid1 import host_subplot
import mpl_toolkits.axisartist as AA
import uuid
import numpy as np
import pandas as pd



def plot4(aname, df0, df1, df2, df3, df4, df5, df6, df7, df8, df9):
    plt.clf()
    
    
    x = df.ix[:,0]
    train = df.ix[:,1]
    test = df.ix[:,2]
    time = df.ix[:,3]
    
    fig, ax1 = plt.subplots()

    ax2 = ax1.twinx()
    
    #ax1.spines['right'].set_visible(False)
    ax1.spines['top'].set_visible(False)
    ax1.spines['bottom'].set_visible(False)
    #ax2.spines['right'].set_visible(False)
    ax2.spines['top'].set_visible(False)
    ax2.spines['bottom'].set_visible(False)
    ax1.xaxis.set_ticks_position('bottom')
    ax1.yaxis.set_ticks_position('left')
    #ax2.xaxis.set_ticks_position('bottom')
    ax2.yaxis.set_ticks_position('right') 


#    ax1.plot(x, opt, 'b-')
    p1, = ax1.plot(x, train,
             color='blue', marker='o',
             markersize=0,
             label='training error')
        
    p2, = ax1.plot(x, test,
             color='green', marker='s',
             markersize=0, linestyle='--',
             label='testing error')
    
    #ax2.plot(x, time, 'g-')
    p3, = ax2.plot(x, time,
             color='red', marker='o',
             markersize=0, linestyle='-')


    #ax2.plot(x, timeper, 'r-')

    #ax1.plot(x, y1, 'g-')
    #ax2.plot(x, y2, 'b-')
    
    ax1.set_xlabel('Iteration')
    ax1.set_ylabel('Error', color='black')
    ax2.set_ylabel('Time (in Seconds)', color='g')
    
    plt.legend([p1,p2,p3], ['Train', 'Test', 'Time'], loc='upper center')
    
    #plt.grid()
    plt.title("Train vs. Test Error: %s (Titanic)" % (series_name))
    
    min_err_i = np.argmin(test.values)
    min_err = test.ix[min_err_i]
    #iter_of_max = x.ix[min_err_i]
    cum_time_of_min = time.ix[min_err_i]
    
    lab = 'Best:' + str(min_err) + '\nTime:' + str(cum_time_of_min)
    ax1.annotate(
        lab, 
        xy = (min_err_i, min_err), xytext = (100, -50),
        textcoords = 'offset points', ha = 'right', va = 'bottom',
        bbox = dict(boxstyle = 'round,pad=0.5', fc = 'yellow', alpha = 0.5),
        arrowprops = dict(arrowstyle = '->', connectionstyle = 'arc3,rad=-0.1'))
    
    
    #plt.xlabel('Iteration')
    #plt.ylabel('Optimal Solution')
    #plt.legend(loc='best')
    
    #for i,j in zip(x,opt):
    #    ax1.annotate(str(j), xy=(i,j), xytext=(10,10), textcoords='offset points')
        
    #for var in (opt, time, timeper):
    #    plt.annotate('%0.2f' % var.max(), xy=(1, var.max()), xytext=(8, 0), 
    #                 xycoords=('axes fraction', 'data'), textcoords='offset points')
        
    fn = './output/' + str(uuid.uuid4()) + '_' + series_name + '_traintest2.png'
    plt.savefig(fn)


def plot3(df, series_name):
    plt.clf()
    
    x = df.ix[:,0]
    train = df.ix[:,1]
    test = df.ix[:,2]
    time = df.ix[:,3]
    
    fig, ax1 = plt.subplots()

    ax2 = ax1.twinx()
    
    #ax1.spines['right'].set_visible(False)
    ax1.spines['top'].set_visible(False)
    ax1.spines['bottom'].set_visible(False)
    #ax2.spines['right'].set_visible(False)
    ax2.spines['top'].set_visible(False)
    ax2.spines['bottom'].set_visible(False)
    ax1.xaxis.set_ticks_position('bottom')
    ax1.yaxis.set_ticks_position('left')
    #ax2.xaxis.set_ticks_position('bottom')
    ax2.yaxis.set_ticks_position('right') 


#    ax1.plot(x, opt, 'b-')
    p1, = ax1.plot(x, train,
             color='blue', marker='o',
             markersize=0,
             label='training error')
        
    p2, = ax1.plot(x, test,
             color='green', marker='s',
             markersize=0, linestyle='--',
             label='testing error')
    
    #ax2.plot(x, time, 'g-')
    p3, = ax2.plot(x, time,
             color='red', marker='o',
             markersize=0, linestyle='-')


    #ax2.plot(x, timeper, 'r-')

    #ax1.plot(x, y1, 'g-')
    #ax2.plot(x, y2, 'b-')
    
    ax1.set_xlabel('Iteration')
    ax1.set_ylabel('Error', color='black')
    ax2.set_ylabel('Time (in Seconds)', color='g')
    
    plt.legend([p1,p2,p3], ['Train', 'Test', 'Time'], loc='upper center')
    
    #plt.grid()
    plt.title("Train vs. Test Error: %s (Titanic)" % (series_name))
    
    min_err_i = np.argmin(test.values)
    min_err = test.ix[min_err_i]
    #iter_of_max = x.ix[min_err_i]
    cum_time_of_min = time.ix[min_err_i]
    
    lab = 'Best:' + str(min_err) + '\nTime:' + str(cum_time_of_min)
    ax1.annotate(
        lab, 
        xy = (min_err_i, min_err), xytext = (100, -50),
        textcoords = 'offset points', ha = 'right', va = 'bottom',
        bbox = dict(boxstyle = 'round,pad=0.5', fc = 'yellow', alpha = 0.5),
        arrowprops = dict(arrowstyle = '->', connectionstyle = 'arc3,rad=-0.1'))
    
    
    #plt.xlabel('Iteration')
    #plt.ylabel('Optimal Solution')
    #plt.legend(loc='best')
    
    #for i,j in zip(x,opt):
    #    ax1.annotate(str(j), xy=(i,j), xytext=(10,10), textcoords='offset points')
        
    #for var in (opt, time, timeper):
    #    plt.annotate('%0.2f' % var.max(), xy=(1, var.max()), xytext=(8, 0), 
    #                 xycoords=('axes fraction', 'data'), textcoords='offset points')
        
    fn = './output/' + str(uuid.uuid4()) + '_' + series_name + '_traintest2.png'
    plt.savefig(fn)


def plot(df, series_name):
    plt.clf()
    
    x = df.ix[:,0]
    train = df.ix[:,1]
    test = df.ix[:,2]
    #time = df.ix[:,2]
    
    plt.plot(x, train,
             color='blue', marker='o',
             markersize=0,
             label='training error')
        
    plt.plot(x, test,
             color='green', marker='s',
             markersize=0, linestyle='--',
             label='testing error')
    
    
    min_err_i = np.argmin(test.values)
    min_err = test.ix[min_err_i]
    #iter_of_max = x.ix[min_err_i]
    #cum_time_of_max = time.ix[max_opt_i]
    
    lab = 'Best:' + str(min_err)
    plt.annotate(
        lab, 
        xy = (min_err_i, min_err), xytext = (100, -50),
        textcoords = 'offset points', ha = 'right', va = 'bottom',
        bbox = dict(boxstyle = 'round,pad=0.5', fc = 'yellow', alpha = 0.5),
        arrowprops = dict(arrowstyle = '->', connectionstyle = 'arc3,rad=-0.1'))
    
    plt.grid()
    plt.title("Train vs. Test Error: %s (Titanic)" % (series_name))
    plt.xlabel('Iterations')
    plt.ylabel('Error')
    plt.legend(loc='best')
    fn = './output/' + str(uuid.uuid4()) + '_' + series_name + '_traintest.png'
    plt.savefig(fn)


def plot2(df, algo_name, series_name):
    plt.clf()
    
    x = df.ix[:,0]
    opt = df.ix[:,1]
    time = df.ix[:,3]
    timeper = df.ix[:,2]
    
    fig, ax1 = plt.subplots()

    ax2 = ax1.twinx()
    
    #ax1.spines['right'].set_visible(False)
    ax1.spines['top'].set_visible(False)
    ax1.spines['bottom'].set_visible(False)
    #ax2.spines['right'].set_visible(False)
    ax2.spines['top'].set_visible(False)
    ax2.spines['bottom'].set_visible(False)
    ax1.xaxis.set_ticks_position('bottom')
    ax1.yaxis.set_ticks_position('left')
    #ax2.xaxis.set_ticks_position('bottom')
    ax2.yaxis.set_ticks_position('right') 


#    ax1.plot(x, opt, 'b-')
    p1, = ax1.plot(x, opt,
             color='blue', marker='o',
             markersize=0, linestyle='-')
    #ax2.plot(x, time, 'g-')
    p2, = ax2.plot(x, time,
             color='green', marker='o',
             markersize=0, linestyle='-')

    p3, = ax2.plot(x, timeper,
             color='red', marker='o',
             markersize=0, linestyle='--')

    #ax2.plot(x, timeper, 'r-')

    #ax1.plot(x, y1, 'g-')
    #ax2.plot(x, y2, 'b-')
    
    ax1.set_xlabel('Iteration')
    ax1.set_ylabel('Optimal Solution', color='b')
    ax2.set_ylabel('Time (in Seconds)', color='g')
    
    avg_timeper = np.mean(timeper.values)
    timeper_lab = 'Time per Iteration' #\nAvg: ' + str(round(avg_timeper, 7))
    
    plt.legend([p1,p2,p3], ['Optima', 'Cumulative Time', timeper_lab], loc='lower right')
    
    #plt.grid()
    plt.title("%s (%s)" % (algo_name, series_name))
    
    max_opt_i = np.argmax(opt.values)
    max_opt = opt.ix[max_opt_i]
    iter_of_max = x.ix[max_opt_i]
    cum_time_of_max = time.ix[max_opt_i]
    
    lab = 'Best:' + str(max_opt) + '\nIter:' + str(iter_of_max) + '\nTime:' + str(cum_time_of_max) + '\nAvg Time:' + str(round(avg_timeper, 5))
    ax1.annotate(
        lab, 
        xy = (iter_of_max, max_opt), xytext = (100, -100),
        textcoords = 'offset points', ha = 'right', va = 'bottom',
        bbox = dict(boxstyle = 'round,pad=0.5', fc = 'yellow', alpha = 0.5),
        arrowprops = dict(arrowstyle = '->', connectionstyle = 'arc3,rad=-0.1'))
    
    
    #plt.xlabel('Iteration')
    #plt.ylabel('Optimal Solution')
    #plt.legend(loc='best')
    
    #for i,j in zip(x,opt):
    #    ax1.annotate(str(j), xy=(i,j), xytext=(10,10), textcoords='offset points')
        
    #for var in (opt, time, timeper):
    #    plt.annotate('%0.2f' % var.max(), xy=(1, var.max()), xytext=(8, 0), 
    #                 xycoords=('axes fraction', 'data'), textcoords='offset points')
        
    fn = './output/' + str(uuid.uuid4()) + '_' + series_name + '_' + algo_name + '_iterations.png'
    plt.savefig(fn)

if __name__ == "__main__":
    
    
    df0 = pd.read_csv('./data/TSP_GA_200_150_20_i0.txt', sep=',')
    df1 = pd.read_csv('./data/TSP_GA_200_150_20_i1.txt', sep=',')
    df2 = pd.read_csv('./data/TSP_GA_200_150_20_i2.txt', sep=',')
    df3 = pd.read_csv('./data/TSP_GA_200_150_20_i3.txt', sep=',')
    df4 = pd.read_csv('./data/TSP_GA_200_150_20_i4.txt', sep=',')
    df5 = pd.read_csv('./data/TSP_GA_200_150_20_i5.txt', sep=',')
    df6 = pd.read_csv('./data/TSP_GA_200_150_20_i6.txt', sep=',')
    df7 = pd.read_csv('./data/TSP_GA_200_150_20_i7.txt', sep=',')
    df8 = pd.read_csv('./data/TSP_GA_200_150_20_i8.txt', sep=',')
    df9 = pd.read_csv('./data/TSP_GA_200_150_20_i9.txt', sep=',')
    
    aname = 'Genetic Algorithm'
    plot4(aname, df0, df1, df2, df3, df4, df5, df6, df7, df8, df9)
    
    '''
    df1 = pd.read_csv('./data/RHC.txt', sep=',')
    df2 = pd.read_csv('./data/SA.txt', sep=',')
    df3 = pd.read_csv('./data/GA.txt', sep=',')
    df4 = pd.read_csv('./data/BackProp.txt', sep=',')
    df5 = pd.read_csv('./data/SA01.txt', sep=',')
    df6 = pd.read_csv('./data/SA99.txt', sep=',')
    df7 = pd.read_csv('./data/SA50.txt', sep=',')
    df8 = pd.read_csv('./data/SA80.txt', sep=',')
    df9 = pd.read_csv('./data/GA100_100_10.txt', sep=',')
    df10 = pd.read_csv('./data/GA500_100_10.txt', sep=',')
    df11 = pd.read_csv('./data/GA1000_100_10.txt', sep=',')
    df12 = pd.read_csv('./data/SA95.txt', sep=',')
    df13 = pd.read_csv('./data/SA96.txt', sep=',')
    df14 = pd.read_csv('./data/SA97.txt', sep=',')
    
    plot3(df1, 'Random Hill Climbing')
    plot3(df2, 'Simulated Annealing (0.95)')
    plot3(df3.ix[0:800,:], 'Genetic Algorithm')
    plot3(df4.ix[0:60,:], 'Back Propagation')
    plot3(df5, 'Simulated Annealing (0.01)')
    plot3(df6, 'Simulated Annealing (0.99)')
    plot3(df7, 'Simulated Annealing (0.50)')
    plot3(df8, 'Simulated Annealing (0.80)')
    
    plot3(df9.ix[0:800,:], 'Genetic Algorithm (100, 100, 10)')
    plot3(df10.ix[0:800,:], 'Genetic Algorithm (500, 100, 10)')
    plot3(df11.ix[0:800,:], 'Genetic Algorithm  (1000, 100, 10)')
    
    plot3(df12, 'Simulated Annealing (0.95)')
    plot3(df13, 'Simulated Annealing (0.96)')
    plot3(df14, 'Simulated Annealing (0.97)')
    '''
    
    '''
    df5 = pd.read_csv('./data/Knapsack_RHC.txt', sep=',')
    df6 = pd.read_csv('./data/Knapsack_SA.txt', sep=',')
    df7 = pd.read_csv('./data/Knapsack_GA.txt', sep=',')
    df8 = pd.read_csv('./data/Knapsack_MIMIC.txt', sep=',')
    plot2(df5.ix[0:500,:], 'Random Hill Climbing', 'Knapsack Problem')
    plot2(df6.ix[0:500,:], 'Simulated Annealing', 'Knapsack Problem')
    plot2(df7.ix[0:25000,:], 'Genetic Algorithm', 'Knapsack Problem')
    plot2(df8.ix[0:500,:], 'MIMIC', 'Knapsack Problem')
    ''' 
    df5 = pd.read_csv('./data/TSP_RHC.txt', sep=',')
    df6 = pd.read_csv('./data/TSP_SA.txt', sep=',')
    df7 = pd.read_csv('./data/TSP_GA.txt', sep=',')
    df8 = pd.read_csv('./data/TSP_MIMIC.txt', sep=',')
    df9 = pd.read_csv('./data/TSP_GA_200_150_20.txt', sep=',')
    
    
    plot2(df5.ix[0:20000,:], 'Random Hill Climbing', 'Travelling Salesman Problem')
    plot2(df6.ix[0:20000,:], 'Simulated Annealing (0.95)', 'Travelling Salesman Problem')
    plot2(df7.ix[0:10000,:], 'Genetic Algorithm', 'Travelling Salesman Problem')
    plot2(df8.ix[0:10000,:], 'MIMIC (200, 100)', 'Travelling Salesman Problem')
    plot2(df9.ix[0:10000,:], 'Genetic Algorithm (200, 150, 20)', 'Travelling Salesman Problem')
   
    '''
    df5 = pd.read_csv('./data/KColor_RHC.txt', sep=',')
    df6 = pd.read_csv('./data/KColor_SA.txt', sep=',')
    df7 = pd.read_csv('./data/KColor_GA.txt', sep=',')
    df8 = pd.read_csv('./data/KColor_MIMIC.txt', sep=',')
    df9 = pd.read_csv('./data/KColor_MIMIC_500_100.txt', sep=',')
    df10 = pd.read_csv('./data/KColor_MIMIC_1000_100.txt', sep=',')
    df11 = pd.read_csv('./data/KColor_MIMIC_1000_500.txt', sep=',')
    df12 = pd.read_csv('./data/KColor_MIMIC_10000_5000.txt', sep=',')
    
    plot2(df5.ix[0:5000,:], 'Random Hill Climbing', 'Max K-Color')
    plot2(df6.ix[0:5000,:], 'Simulated Annealing', 'Max K-Color')
    plot2(df7.ix[0:2000,:], 'Genetic Algorithm', 'Max K-Color')
    plot2(df8.ix[0:8000,:], 'MIMIC', 'Max K-Color')
    plot2(df9.ix[0:8000,:], 'MIMIC (500, 100)', 'Max K-Color')
    plot2(df10.ix[0:8000,:], 'MIMIC (1000, 100)', 'Max K-Color')
    plot2(df11.ix[0:8000,:], 'MIMIC (1000, 500)', 'Max K-Color')
    plot2(df12.ix[0:8000,:], 'MIMIC (10000, 5000)', 'Max K-Color')
    '''
    
    '''
    df5 = pd.read_csv('./data/FourPeaks_RHC.txt', sep=',')
    df6 = pd.read_csv('./data/FourPeaks_SA.txt', sep=',')
    df7 = pd.read_csv('./data/FourPeaks_GA.txt', sep=',')
    df8 = pd.read_csv('./data/FourPeaks_MIMIC.txt', sep=',')
    plot2(df5.ix[0:100000,:], 'Random Hill Climbing', 'Four Peaks')
    plot2(df6.ix[0:100000,:], 'Simulated Annealing', 'Four Peaks')
    plot2(df7.ix[0:100000,:], 'Genetic Algorithm', 'Four Peaks')
    plot2(df8.ix[0:100000,:], 'MIMIC', 'Four Peaks')
    '''