# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
import pandas as pd
import numpy
def keyTimeSeries(timeSeries):
    return timeSeries[1]
def CreateTupleTable(timeSeries):
    TestClass = open("Univariate_ts/"+timeSeries+"/"+timeSeries+"TestClass.csv")
    TestDist = open("Univariate_ts/"+timeSeries+"/"+timeSeries+"TestDist.csv")
    TrainClass = open("Univariate_ts/"+timeSeries+"/"+timeSeries+"TrainClass.csv")
    TrainDist = open("Univariate_ts/"+timeSeries+"/"+timeSeries+"TrainDist.csv")
    Output = []
    TestArray = []
    TrainArray = []
    for ts in TestClass:
        tclass = ts.strip(" ,\n").split(",")
        tdist = TestDist.readline().strip(" ,\n")
        tdist = tdist.split(",")
        for i in range(len(tdist)):
            tdist[i] = float(tdist[i].strip(" "))
        temp = []
        for i in range(len(tclass)):
            temp.append((tclass[i],tdist[i]))
        TestArray.append(temp)
    TestClass.close()
    TestDist.close()
    increment = 0
    for ts in TrainClass:
        tclass = ts.strip(" ,\n").split(",")
        tdist = TrainDist.readline().strip(" ,\n")
        tdist = tdist.split(",")
        temp = []
        for i in range(len(tdist)):
            tdist[i] = float(tdist[i].strip(" "))
        for i in range(len(tclass)):
                temp.append((tclass[i], tdist[i]))
        TrainArray.append(temp)
        increment += 1
    TrainClass.close()
    TrainDist.close()
    for i in range(len(TrainArray)):
        for j in range(len(TrainArray[-1])-len(TrainArray[i])):
            TrainArray[i].append(["Null", float('inf')])
    return [TestArray, TrainArray]
def Average(inputList):
    sum = 0
    for i in inputList:
        sum += i
    sum = sum/len(inputList)
    return sum
def VoteRankingBased(DistArray):
    Classes = []
    Tally = []
    Distances = []
    for i in range(len(DistArray)):
        if DistArray[i][0] not in Classes:
            Classes.append(DistArray[i][0])
            Tally.append(1)
            Distances.append([DistArray[i][1]])
        else:
            for j in range(len(Classes)):
                if Classes[j] == DistArray[i][0]:
                    break
            Tally[j] += 1
            Distances[j].append(DistArray[i][1])
    maxIndex = 0
    for i in range(len(Classes)):
        if Tally[i] > Tally[maxIndex]:
            maxIndex = i
        elif Tally[i] == Tally[maxIndex]:
            if Average(Distances[i]) < Average (Distances[maxIndex]):
                maxIndex = i
    return Classes[maxIndex]
def SortDistances(timeSeriesArray):
    for i in timeSeriesArray:
        i.sort(key=lambda x: x[1])
def kNNClassification(k,timeSeriesArray):
    output = []
    for i in range(len(timeSeriesArray)):
        temp = []
        for j in range(k):
            temp.append(timeSeriesArray[i][j])
        output.append(VoteRankingBased(temp))
    return output
def Test(ClassifiedTimeSeries, TimeSeries):
    Tester = open("Univariate_ts/"+TimeSeries+"/"+TimeSeries+"_TEST.ts")
    Comparison = []
    SuccessRate = 0
    for string in Tester:
        if string[0] != "#" and string[0] != "@":
            string = string.split(":")[1]
            Comparison.append(int(string))
    for i in range(len(Comparison)):
       # print((int(float(ClassifiedTimeSeries[i].strip(" "))), ClassifiedTimeSeries[i][1]), Comparison[i])
        if int(float(ClassifiedTimeSeries[i].strip(" "))) == Comparison[i]:
            SuccessRate += 1
      #  print(str(i)+"/"+str(len(Comparison)))
   # print(SuccessRate)
   # print(len(Comparison))
    return 1-SuccessRate/len(Comparison)

listTimeSeries = open("eeOutputFold0.csv")
TimeSeriesTest = []
for s in listTimeSeries:
    s = s.split(',')
    TimeSeriesTest.append(s[0])
for i in range(1,len(TimeSeriesTest)):
    if i == 23:
        i += 1
    print(TimeSeriesTest[i])
    test = CreateTupleTable(TimeSeriesTest[i])
    SortDistances(test[0])
 #   for i in test[0]:
  #      print(i)
    outlist = []
    for j in range(1,6):
        output = kNNClassification(j,  test[0])
        outlist.append(Test(output, TimeSeriesTest[i]))
    print(outlist)