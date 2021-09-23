package Java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        TimeSeriesClassification timeseries = new TimeSeriesClassification();
        tsExtraction timeserieslist = new tsExtraction();
        String[] listTime = timeserieslist.TimeSeriesNames("eeOutputFold0.csv");
        for(int i= 0; i< 8; i++){
            String s = listTime[i];
            double[][][]timeSeriesTrain = timeseries.TimeSeriesExtraction("Univariate_ts/" + s + "/"+ s + "_TRAIN.ts");
            double[][][]timeSeriesTest = timeseries.TimeSeriesExtraction("Univariate_ts/" + s + "/"+ s + "_TEST.ts");
            timeseries.TimeSeriesAllK(timeSeriesTrain,timeSeriesTest, s);
        }

    }

}
