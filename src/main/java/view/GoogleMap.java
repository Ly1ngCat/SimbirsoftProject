package view;

import model.CurrentPoint;
import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class GoogleMap extends MapView {
    public GoogleMap() {
        setOnMapReadyHandler(new MapReadyHandler() {

            public void onMapReady(MapStatus status) {
                if (status == MapStatus.MAP_STATUS_OK) {

                    HashMap<LatLng, CurrentPoint> hashMap = new HashMap<>();


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



                            hashMap.put(coordinates, new CurrentPoint(longitude,latitude,null,null));
                            System.out.println(hashMap);

                            marker.addEventListener("click", new MapMouseEvent() {
                                @Override
                                public void onEvent(MouseEvent mouseEvent) {
                                    marker.remove();
                                    hashMap.remove(coordinates);
                                    System.out.println(hashMap);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public static void Interface (String[] args) {
        final GoogleMap sample = new GoogleMap();

        JFrame frame = new JFrame("Travel Map");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(sample, BorderLayout.CENTER);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}