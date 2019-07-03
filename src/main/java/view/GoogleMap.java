package view;

import com.teamdev.jxmaps.Map;

import controllers.googlemapsclasses.GeocodingAdressGoogleMapsAPI;
import controllers.workerXML.RecommendationParser;
import library.ConfigParse;
import model.CurrentPoint;
import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;
import org.json.JSONException;
import view.GUI.VisualInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.*;
import java.util.List;

import static view.GUI.VisualInterface.*;


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

                    ArrayList<CurrentPoint> currentPoints = VisualInterface.getCurrentPoints();
                    final Map map = getMap();
                    MapOptions options = new MapOptions();
                    MapTypeControlOptions controlOptions = new MapTypeControlOptions();
                    controlOptions.setPosition(ControlPosition.TOP_RIGHT);
                    options.setMapTypeControlOptions(controlOptions);
                    map.setOptions(options);
                    map.setCenter(new LatLng(54.33346, 48.384337));
                    map.setZoom(9.0);

                    map.addEventListener("click", new MapMouseEvent() {
                        @Override
                        public void onEvent(MouseEvent mouseEvent) {
                            isDateTimeSet = false;
                            VisualInterface.DialogDateTimePicker dialogDateTimePicker = new VisualInterface.DialogDateTimePicker();
                            dialogDateTimePicker.setVisible(true);
                            if (isDateTimeSet) {
                                final Marker marker = new Marker(map);
                                marker.setPosition(mouseEvent.latLng());
                                LatLng coordinates = marker.getPosition();
                                double latitude = coordinates.getLat();
                                double longitude = coordinates.getLng();
                                InfoWindow infoWindow = new InfoWindow(map);
                                geocodingAdressGoogleMapsAPI.calculateAdress(latitude + "," + longitude);
                                currentPoint.setId(++currentPointId);
                                currentPoint.setLatitude(latitude);
                                currentPoint.setLongitude(longitude);
                                currentPoint.setAdressString(geocodingAdressGoogleMapsAPI.getAdress());
                                currentPoint.setWeather();
                                infoWindow.setContent(geocodingAdressGoogleMapsAPI.getAdress());
                                infoWindow.open(map,marker);
                                currentPoints.add(currentPoint);
                                currentPointTableModel.fireTableDataChanged();
                                //Weather weather = new Weather(currentPoint);
                                jTextArea.setText(geocodingAdressGoogleMapsAPI.getAdress());

                                System.out.println(currentPoints);
                                /*Collections.sort(currentPoints);
                                System.out.println(currentPoints);*/


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
                                        System.out.println(allRecommendations(currentPoints));

                                    }
                                });
                            }

                        }
                    });
                }
            }
        });
    }

    private CurrentPoint getCurrentPointByCoordinates(double latitude, double longitude) {
        for (CurrentPoint point : getCurrentPoints()) {
            if (point.getLatitude() == latitude && point.getLongitude() == longitude) {
                return point;
            }
        }
        return null;
    }

    private static void fixIds() {
        int id = 1;
        for (CurrentPoint point : getCurrentPoints()) {
            point.setId(id++);
        }
        currentPointId = getCurrentPoints().size();
    }
}