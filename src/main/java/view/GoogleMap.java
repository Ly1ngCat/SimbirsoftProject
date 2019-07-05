package view;

import com.teamdev.jxmaps.Map;

import library.googlemapsclasses.GeocodingAdressGoogleMapsAPI;
import library.workerXML.RecommendationParser;
import model.CurrentPoint;
import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;
import org.json.JSONException;

import java.io.IOException;

import view.GUI.VisualInterface;
import java.util.*;
import java.util.List;
import static view.GUI.VisualInterface.*;



public class GoogleMap extends MapView {

    public List<ArrayList<String>> allRecommendations(ArrayList<CurrentPoint> points) {
        Collections.sort(points); //сортируем массив маркеров по ДАТЕ в порядке возрастания
        List<ArrayList<String>> allRecs = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            allRecs.add(RecommendationParser.getRecommendation(points.get(i).getWeather().getWeatherType().toLowerCase(),
                    points.get(i).getWeather().getPredictedTemp()));
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
                            DialogDateTimePicker dialogDateTimePicker = new DialogDateTimePicker();
                            dialogDateTimePicker.setVisible(true);
                            if (isDateTimeSet) {
                                final Marker marker = new Marker(map);
                                marker.setPosition(mouseEvent.latLng());
                                LatLng coordinates = marker.getPosition();
                                double latitude = coordinates.getLat();
                                double longitude = coordinates.getLng();

                                geocodingAdressGoogleMapsAPI.calculateAdress(latitude + "," + longitude);
                                currentPoint.setId(++currentPointId);
                                currentPoint.setLatitude(latitude);
                                currentPoint.setLongitude(longitude);
                                currentPoint.setAddress(geocodingAdressGoogleMapsAPI.getAddress());
                                currentPoint.initializeWeather();
                                InfoWindow infoWindow = new InfoWindow(map);
                                infoWindow.setContent(geocodingAdressGoogleMapsAPI.getAddress());
                                infoWindow.open(map,marker);
                                currentPoints.add(currentPoint);
                                currentPointTableModel.fireTableDataChanged();
                                jTextArea.setText(geocodingAdressGoogleMapsAPI.getAddress());
                                System.out.println(currentPoints);
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