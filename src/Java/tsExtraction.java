package Java;

import java.io.*;

public class tsExtraction {
    public String[] TimeSeriesNames(String file) {
        String[] Output = new String[128];
        File timeseries = new File(file);
        try (BufferedReader br = new BufferedReader(new FileReader(timeseries))) {
            String line;
            br.mark(0);
            int i = 0;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] newline;
                System.out.println("Line: "+ i);
                newline = line.split(",");
                Output[i] = newline[0];
                i += 1;
            }
            return Output;
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }

    }
}
