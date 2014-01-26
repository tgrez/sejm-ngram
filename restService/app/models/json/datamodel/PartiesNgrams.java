package models.json.datamodel;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PartiesNgrams {

    public  String name;
    public  List<ListDate> listDates = new ArrayList<ListDate>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ListDate> getListDates() {
        return listDates;
    }

    public void setListDates(List<ListDate> listDates) {
        this.listDates = listDates;
    }


}
