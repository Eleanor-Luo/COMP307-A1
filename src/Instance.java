import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Instance {

    private String category;
    private List<Boolean> vals;

    public Instance(String cat, List<Boolean> bool){
        category = cat;
        vals = bool;
    }

    public boolean getVal(int index){
        return vals.get(index);
    }

    public String getCategory(){
        return category;
    }

    public String toString(){
        StringBuilder ans = new StringBuilder(category);
        ans.append(" ");
        for (Boolean val : vals)
            ans.append(val?"true  ":"false ");
        return ans.toString();
    }

}