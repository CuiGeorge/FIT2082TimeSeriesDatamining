package Java;
import tempo.univariate.distances.DTW;

import java.io.*;
import java.nio.Buffer;

import static java.lang.Double.POSITIVE_INFINITY;

public class TimeSeriesClassification {
    public void TimeSeriesAllK(double[][][] timeseriesTrain, double[][][] timeseriesTest, String timeseries) throws IOException {
        double[][][] outTestArray;
        outTestArray = new double[timeseriesTrain.length][timeseriesTest.length][2];
        double[][][] outTrainArray;
        outTrainArray = new double[timeseriesTrain.length][timeseriesTrain.length][2];
        for(int i = 0; i< timeseriesTrain.length;i++ ){
            double max = POSITIVE_INFINITY;
            for(int j = 0; j<i+1;j++){
                if( j == i){
                    outTrainArray[i][j][0] = 0;
                    outTrainArray[i][j][1] = timeseriesTrain[j][1][0];
                } else {
                    outTrainArray[i][j][0] = DTW.distance(timeseriesTrain[i][0],timeseriesTrain[j][0], max );
                    outTrainArray[i][j][1] = timeseriesTrain[j][1][0];
                }

            }
            for(int j = 0; j< timeseriesTest.length;j++ ) {

                outTestArray[i][j][0] = DTW.distance(timeseriesTrain[i][0],timeseriesTest[j][0], max );
                outTestArray[i][j][1] = timeseriesTrain[i][1][0];

            }
            System.out.println("Distances for BOTH test and train: "+ i);

        }
        System.out.println(timeseries);
        System.out.println("Train: ");
        String OutputDistTrain = "";
        String OutputClassTrain = "";
        for(int i = 0; i< timeseriesTrain.length;i++ ){
            String outDistance = "";
            String outClass = "";
            for(int j = 0; j<i+1;j++){
                outDistance = outDistance + Double.toString(outTrainArray[i][j][0]) + ", ";
            }
            for(int j = 0; j<i+1;j++){
                outClass = outClass + Double.toString(outTrainArray[i][j][1]) + ", ";
            }
            OutputDistTrain = OutputDistTrain + outDistance +"\n";
            OutputClassTrain = OutputClassTrain + outClass + "\n";
            System.out.println("Row: "+ i);
        }
        System.out.println("Test: ");
        String OutputDistTest = "";
        String OutputClassTest = "";
        for(int i = 0; i< timeseriesTest.length;i++ ) {
            String outDistance = "";
            String outClass = "";
            for (int j = 0; j < timeseriesTrain.length; j++) {
                outDistance = outDistance + Double.toString(outTestArray[j][i][0]) + ", ";
            }
            for (int j = 0; j < timeseriesTrain.length; j++) {
                outClass = outClass + Double.toString(outTestArray[j][i][1]) + ", ";
            }
            OutputDistTest = OutputDistTest + outDistance + "\n";
            OutputClassTest = OutputClassTest + outClass + "\n";
            System.out.println("Row: " + i);
        }
        System.out.println();
        File OutDistTrain = new File("Univariate_ts/"+timeseries+"/"+timeseries+"TrainDist.csv");
        File OutClassTrain = new File("Univariate_ts/"+timeseries+"/"+timeseries+"TrainClass.csv");
        File OutDistTest = new File("Univariate_ts/"+timeseries+"/"+timeseries+"TestDist.csv");
        File OutClassTest = new File("Univariate_ts/"+timeseries+"/"+timeseries+"TestClass.csv");
       FileWriter writeout = new FileWriter(OutDistTrain);
        writeout.write(OutputDistTrain);
        writeout.close();
        writeout = new FileWriter(OutDistTest);
        writeout.write(OutputDistTest);
        writeout.close();
        writeout = new FileWriter(OutClassTrain);
        writeout.write(OutputClassTrain);
        writeout.close();
        writeout = new FileWriter(OutClassTest);
        writeout.write(OutputClassTest);
        writeout.close();
    }
    public double[][][] TimeSeriesExtraction(String file) throws FileNotFoundException {
        File timeseries = new File(file);
        double[][][] output = new double[0][][];
        double[][] type;
        type = new double[0][];
        double[] dataProcessed;
        double[][] dataJoined = new double[0][];
        int numTimeSeries = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(timeseries))) {
            String line;
            br.mark(0);
            while ((line = br.readLine()) != null) {
                if( line.charAt(0) != '#' && line.charAt(0) != '@') {
                numTimeSeries += 1;
                }
            }
            output = new double[numTimeSeries][][];
            type = new double[numTimeSeries][1];
            br.close();
            numTimeSeries = 0;
            BufferedReader br2 = new BufferedReader(new FileReader(timeseries));
            while ((line = br2.readLine()) != null) {
                if( line.charAt(0) != '#' && line.charAt(0) != '@'){
                    String[] linesplit = line.split(":");
                    String[] data = linesplit[0].split(",");
                    System.out.println("Line_Read: " + numTimeSeries);
                    type[numTimeSeries][0] = Double.parseDouble(linesplit[1]);
                    dataProcessed = new double[data.length];
                    for(int i = 0; i< data.length; i++) {
                        dataProcessed[i] = Double.parseDouble(data[i]);
                    }
                    dataJoined = new double[][]{dataProcessed, type[numTimeSeries]};
                    output[numTimeSeries] = dataJoined;
                    numTimeSeries += 1;
                }
            }
            br2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }
}

