package view;

import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapMouseEvent;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.MouseEvent;
import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import java.awt.*;

public class MarkersExample extends MapView {
    public MarkersExample() {
        setOnMapReadyHandler(new MapReadyHandler() {

            public void onMapReady(MapStatus status) {
                if (status == MapStatus.MAP_STATUS_OK) {
                    final Map map = getMap();
                    MapOptions options = new MapOptions();
                    MapTypeControlOptions controlOptions = new MapTypeControlOptions();
                    controlOptions.setPosition(ControlPosition.TOP_RIGHT);
                    options.setMapTypeControlOptions(controlOptions);
                    map.setOptions(options);
                    map.setCenter(new LatLng(54.33346, 48.384337));
                    map.setZoom(9.0);
                    Marker marker = new Marker(map);
                    marker.setPosition(map.getCenter());
                    final InfoWindow infoWindow = new InfoWindow(map);
                    infoWindow.setContent("Нажмите на карту чтобы поставить маркер, нажмите на маркер чтобы удалить его.");
                    infoWindow.open(map, marker);
                    map.addEventListener("click", new MapMouseEvent() {
                        @Override
                        public void onEvent(MouseEvent mouseEvent) {
                            infoWindow.close();
                            final Marker marker = new Marker(map);
                            marker.setPosition(mouseEvent.latLng());
                            System.out.print(marker.getPosition());
                            marker.addEventListener("click", new MapMouseEvent() {
                                @Override
                                public void onEvent(MouseEvent mouseEvent) {
                                    marker.remove();
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public static void Interface (String[] args) {
        final MarkersExample sample = new MarkersExample();

        JFrame frame = new JFrame("Travel Map");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(sample, BorderLayout.CENTER);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}