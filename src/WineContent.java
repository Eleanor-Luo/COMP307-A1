import java.util.List;

/**
 *  Class which handles the data storage for the training data list
 *
 * @author Abraham Alfred 300509598
 **/

public class WineContent {
    private final List<Double> dataList;
    private final int classNumber;
    private double dist;
    private int labelGuess = 0;

    /**
     * Constructor for the WineContent Class
     *
     * @param data data
     * @param num num
     **/
    public WineContent(List<Double> data, int num) {
        dataList = data;
        classNumber = num;
    }

    /**
     *  Method for calculating the euclidean distance.
     *
     * @param obj obj
     * @param range range
     * @return double distance
     **/
    public double distanceFinder(WineContent obj, List<Double> range) {
        double distance;
        double countTotal = 0.0;
        for (int i = 0; i < dataList.size(); i++) {
            double euclidCalc  = ((this.dataList.get(i) - obj.dataList.get(i)) * (this.dataList.get(i) - obj.dataList.get(i)))/((range.get(i)) * (range.get(i)));
            countTotal = countTotal + euclidCalc;
        }
        distance = Math.sqrt(countTotal);
        dist = distance;
        return distance;
    }

    //============================================================
    // Getters and setters.
    //============================================================
    /**
     *  Getter for the dataList arraylist.
     *
     * @return double getDataList
     **/
    public double getDataList(int i) {
        return dataList.get(i);
    }

    /**
     *  Getter for double dist
     *
     * @return double getDist
     **/
    public double getDist() {
        return dist;
    }

    /**
     *  Getter for int classNumber
     *
     * @return double getClassNumber
     **/
    public int getClassNumber() {
        return classNumber;
    }

    /**
     * Getter for int labelGuess
     *
     * @return int labelGuess
     **/
    public int getLabelGuess() { return labelGuess; }

    /**
     * Setter for int labelGuess
     *
     * @param labelGuess labelGuess
     **/
    public void setLabelGuess(int labelGuess) { this.labelGuess = labelGuess; }
}
