import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SheetSpread {

    //Global arrays and lists:
    String dataPath;
    double[] xVals;
    double[] yVals;
    List<Double> distances = new ArrayList<>();
    List<String> rawLines = new ArrayList<>();
    double averageDistance;

    //Determining outlier factor:
    public double determiningOutlierFactor;

    //Constructor, sets dataPath when object is created:
    public SheetSpread(String _dataPath, double _determiningOutlierFactor) {
        this.dataPath = _dataPath;
        this.determiningOutlierFactor = _determiningOutlierFactor;
    }

    //Returns the distance between two points:
    private double getDistance(double x1, double y1, double x2, double y2) {

        double differenceX = x2 - x1;
        double differenceY = y2 - y1;
        differenceX = Math.pow(differenceX, 2);
        differenceY = Math.pow(differenceY, 2);
        double sum = differenceX + differenceY;
        return Math.sqrt(sum);
    }

    //Runs the algorithm on a file:
    public void run() {

        //Loads raw lines into list:
        try {
            Path path = Paths.get(dataPath);
            rawLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //Sets x and y arrays the same length as the rawLines list:
        xVals = new double[rawLines.size()];
        yVals = new double[rawLines.size()];

        //Converts raw lines into X and Y double values that are put into respective arrays:
        int a = 0;
        while (a < rawLines.size()) {

            String[] splitValues = rawLines.get(a).split(", ");
            double x = Double.valueOf(splitValues[0]);
            double y = Double.valueOf(splitValues[1]);
            xVals[a] = x;
            yVals[a] = y;
            a++;
        }

        //Finds the distance between every point:
        int b = 0;
        int c = 0;
        while (b < rawLines.size()) {

            while (c < rawLines.size()) {

                if (c == b && c != rawLines.size() - 1) {
                    c++;
                }

                boolean storeDistance = true;
                double x1 = xVals[b];
                double y1 = yVals[b];
                double x2 = xVals[c];
                double y2 = yVals[c];
                double distance = this.getDistance(x1, y1, x2, y2);

                //Excludes the last value finding the distance from itself:
                int max = rawLines.size() - 1;

                if (b == max && c == max) {
                    storeDistance = false;
                }

                if (storeDistance == true) {
                    distances.add(distance);
                    System.out.println("Distance between (" + x1 + ", " + y1 + "), (" + x2 + ", " + y2 + "): " + distance);
                }

                c++;
            }

            b++;
            c = 0;
        }

        //Finds average distance between points:
        int i = 0;
        double total = 0;
        while (i < distances.size()) {
            total = total + distances.get(i);
            i++;
        }

        this.averageDistance = total / distances.size();
    }

    //Determines if an existing point in the data is an outlier, given the point's line number in a file:
    public boolean determineExisting(int line) {

        double maxDistance = determiningOutlierFactor * averageDistance; //If the average distance of a coordinate is greater than the maxDistance, it is classified as an outlier.
        double totalExistingCoordinateDistances = 0;

        //Finds the distances between the specified point and all other existing points in the file:
        //Gets the x and y coordinates of the line passed into the function:
        double x1 = xVals[line - 1];
        double y1 = yVals[line - 1];

        int i = 0;
        while (i < rawLines.size()) {

            if (i == line - 1) {
                i++;
            }

            double x2 = xVals[i];
            double y2 = yVals[i];
            double distance = getDistance(x1, y1, x2, y2);
            totalExistingCoordinateDistances = totalExistingCoordinateDistances + distance;

            i++;
        }

        int n = rawLines.size() - 1;
        double _averageDistance = totalExistingCoordinateDistances / n;

        //Classifies the existing point as an outlier if its average distance is greater than maxDistance:
        if (_averageDistance > maxDistance) {
            return true;
        }

        //Does not classify the existing point as an outlier if its average distance is less than or equal to maxDistance:
        else {
            return false;
        }
    }

    //Determines if an experimental is an outlier according to the data, given and x and y coordinate:
    public boolean determineExperimental(double x1, double y1) {

        double maxDistance = determiningOutlierFactor * averageDistance; //If the average distance of a coordinate is greater than the maxDistance, it is classified as an outlier.
        double totalExistingCoordinateDistances = 0;

        //Finds the distance between the specified point and all existing points in the file:
        int i;
        for (i = 0; i < rawLines.size(); i++) {

            double x2 = xVals[i];
            double y2 = yVals[i];
            double distance = getDistance(x1, y1, x2, y2);
            totalExistingCoordinateDistances = totalExistingCoordinateDistances + distance;
        }

        int n = rawLines.size() - 1;
        double _averageDistance = totalExistingCoordinateDistances / n;

        //Classifies the experimental point as an outlier if its average distance is greater than maxDistance:
        if (_averageDistance > maxDistance) {
            return true;
        }

        //Does not classify the existing point as an outlier if its average distance is less than or equal to maxDistance:
        else {
            return false;
        }
    }
}