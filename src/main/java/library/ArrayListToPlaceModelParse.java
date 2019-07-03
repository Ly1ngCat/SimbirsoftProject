package library;

import model.PlaceModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayListToPlaceModelParse {

    static public ArrayList<PlaceModel> convert (List<ArrayList<String>> listPlace){

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
            return new ArrayList<>(0);
         //   return Collections.emptyList();
        }
    }
}
