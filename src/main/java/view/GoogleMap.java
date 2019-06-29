package view;

import com.teamdev.jxmaps.Map;
import constants.Constant;
import controllers.googlemapsclasses.GeocodingAdressGoogleMapsAPI;
import model.CurrentPoint;
import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import model.Weather;

public class GoogleMap extends MapView {
    public static HashMap<LatLng, CurrentPoint> hashMap;
    public GoogleMap() throws IOException, JSONException {
        GeocodingAdressGoogleMapsAPI geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI(Constant.KEY_GOOGLE_API);
        setOnMapReadyHandler(new MapReadyHandler() {

            public void onMapReady(MapStatus status) {
                if (status == MapStatus.MAP_STATUS_OK) {

                    hashMap = new HashMap<>();
                    //ArrayList<CurrentPoint> hashMap=new ArrayList<>();

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
                            infoWindow.close();
                            final Marker marker = new Marker(map);
                            marker.setPosition(mouseEvent.latLng());
                            LatLng coordinates = marker.getPosition();
                            double latitude = coordinates.getLat();
                            double longitude = coordinates.getLng();


                            try {
                                geocodingAdressGoogleMapsAPI.calculateAdress(latitude +  "," +longitude);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            CurrentPoint point=new CurrentPoint(longitude,latitude,geocodingAdressGoogleMapsAPI.getAdress(),new Timestamp(1561890755));
                            hashMap.put(coordinates,point);

                            System.out.println(hashMap);
                            try {
                                Weather weather=new Weather(hashMap.get(coordinates));
                                System.out.println("Current weather in "+weather.OWMcityName+" is "+weather.predictedTemp);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            marker.addEventListener("click", new MapMouseEvent() {
                                @Override
                                public void onEvent(MouseEvent mouseEvent) {
                                    marker.remove();
                                    hashMap.remove(coordinates);
                                    //System.out.println(hashMap);
                                }
                            });
                        }
                    });

                }
            }
        });
    }

    public static void Interface (String[] args) throws IOException, JSONException {
        final GoogleMap sample = new GoogleMap();

        JFrame frame = new JFrame("Travel Map");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(sample, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}