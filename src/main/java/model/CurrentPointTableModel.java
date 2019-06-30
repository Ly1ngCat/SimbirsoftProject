package model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class CurrentPointTableModel extends AbstractTableModel {
    ArrayList<CurrentPoint> currentPoints;

    public CurrentPointTableModel(ArrayList<CurrentPoint> currentPoints){
        super();
        this.currentPoints = currentPoints;
    }

    @Override
    public int getRowCount() {
        return currentPoints.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int r, int c) {
        switch (c){
            case 0:
                return currentPoints.size()+1;
            case 1:
                return currentPoints.get(r).getAdressString();
            case 2:
                return currentPoints.get(r).getForecastDate().toString();
                default:
                    return "";
        }
    }

    @Override
    public String getColumnName(int c) {
        String result = "";
        switch (c) {
            case 0:
                result = "№";
                break;
            case 1:
                result = "Адрес";
                break;
            case 2:
                result = "Время";
                break;
        }
        return result;
    }
}
