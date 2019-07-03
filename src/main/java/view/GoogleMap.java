package view;

import com.teamdev.jxmaps.Map;

import controllers.googlemapsclasses.GeocodingAdressGoogleMapsAPI;
import controllers.googlemapsclasses.PlaceSearchGoogleMapsAPI;
import controllers.workerXML.RecommendationParser;
import library.ArrayListToPlaceModelParse;
import library.ConfigParse;
import model.CurrentPoint;
import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;
import model.PlaceModel;
import org.json.JSONException;
import view.GUI.VisuaIinterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.*;
import java.util.List;

import static view.GUI.VisuaIinterface.*;
import static view.GUI.VisuaIinterface.DialogDateTimePicker.fixIds;
import static view.GUI.VisuaIinterface.DialogDateTimePicker.getCurrentPointByCoordinates;


public class GoogleMap extends MapView {

    private List<ArrayList<String>> allRecs;

    public List<ArrayList<String>> allRecommendations(ArrayList<CurrentPoint> points) {
        Collections.sort(points); //сортируем массив маркеров по ДАТЕ в порядке возрастания
        allRecs = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            allRecs.add(RecommendationParser.getRecomendation(points.get(i).weather.weatherType.toLowerCase(),
                    points.get(i).weather.predictedTemp));
        }
        return allRecs;
    }

    public GoogleMap() throws IOException, JSONException {

        GeocodingAdressGoogleMapsAPI geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI();

        setOnMapReadyHandler(new MapReadyHandler() {

            public void onMapReady(MapStatus status) {
                if (status == MapStatus.MAP_STATUS_OK) {

                    //HashMap<LatLng, CurrentPoint> hashMap = new HashMap<>();

                    final Map map = getMap();
                    MapOptions options = new MapOptions();
                    MapTypeControlOptions controlOptions = new MapTypeControlOptions();
                    controlOptions.setPosition(ControlPosition.TOP_RIGHT);
                    options.setMapTypeControlOptions(controlOptions);
                    map.setOptions(options);
                    map.setCenter(new LatLng(54.33346, 48.384337));
                    map.setZoom(9.0);
                    Marker marker = new Marker(map);
                    final InfoWindow infoWindow = new InfoWindow(map);
                    infoWindow.setContent("Нажмите на карту чтобы поставить маркер, нажмите на маркер чтобы удалить его.");
                    infoWindow.open(map, marker);

                    map.addEventListener("click", new MapMouseEvent() {
                        @Override
                        public void onEvent(MouseEvent mouseEvent) {
                            isDateTimeSet = false;
                            VisuaIinterface.DialogDateTimePicker dialogDateTimePicker = new VisuaIinterface.DialogDateTimePicker();
                            dialogDateTimePicker.setVisible(true);
                            if (isDateTimeSet) {
                                infoWindow.close();
                                final Marker marker = new Marker(map);
                                marker.setPosition(mouseEvent.latLng());
                                LatLng coordinates = marker.getPosition();
                                double latitude = coordinates.getLat();
                                double longitude = coordinates.getLng();

                                geocodingAdressGoogleMapsAPI.calculateAdress(latitude + "," + longitude);
                                currentPoint.setId(++curretPointId);
                                currentPoint.setLatitude(latitude);
                                currentPoint.setLongitude(longitude);
                                currentPoint.setAdressString(geocodingAdressGoogleMapsAPI.getAdress());
                                currentPoint.setWeather();

                                //hashMap.put(coordinates, currentPoint);
                                currentPoints.add(currentPoint);
                                currentPointTableModel.fireTableDataChanged();
                                jTextArea.setText(geocodingAdressGoogleMapsAPI.getAdress());
                                System.out.println(currentPoints);

                                PlaceSearchGoogleMapsAPI placeSearchGoogleMapsAPI = new PlaceSearchGoogleMapsAPI();

                                ArrayList<PlaceModel> dgdg = placeSearchGoogleMapsAPI.generateListPlaceModel(
                                        String.valueOf(currentPoint.getLatitude()),
                                        String.valueOf(currentPoint.getLongitude()),
                                        PlaceSearchGoogleMapsAPI.typePlace.lodging,
                                        "10000");

                                foundPlaces.add(dgdg);


                                marker.addEventListener("click", new MapMouseEvent() {
                                    @Override
                                    public void onEvent(MouseEvent mouseEvent) {
                                        marker.remove();
                                        LatLng coordinates = marker.getPosition();
                                        double latitude = coordinates.getLat();
                                        double longitude = coordinates.getLng();
                                        currentPoints.remove(getCurrentPointByCoordinates(latitude, longitude));
                                        fixIds();
                                        currentPointTableModel.fireTableDataChanged();
                                        //System.out.println(currentPoints);
                                        System.out.println(allRecommendations(currentPoints));
                                        //hashMap.remove(coordinates);
                                        //System.out.println(hashMap);
                                    }
                                });
                            }

                        }
                    });
                }
            }
        });
    }
}