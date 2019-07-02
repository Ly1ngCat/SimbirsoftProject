package library;

import model.PlaceModel;

import java.util.ArrayList;

public class ArrayListToPlaceModelParse {

    static public ArrayList<PlaceModel> convert (ArrayList<ArrayList<String>> listPlace){

        try {
            ArrayList<PlaceModel> placeModels = new ArrayList<>();

            for(ArrayList<String> line : listPlace)
            {
                placeModels.add(new PlaceModel(Double.parseDouble(line.get(0)),
                        Double.parseDouble(line.get(1)),line.get(2), line.get(3), Double.parseDouble(line.get(4))));
            }

            return placeModels;

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
