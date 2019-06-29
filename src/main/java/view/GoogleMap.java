package view;

import com.github.lgooddatepicker.components.DateTimePicker;
import constants.Constant;
import controllers.googlemapsclasses.GeocodingAdressGoogleMapsAPI;
import model.CurrentPoint;
import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

public class GoogleMap extends MapView {
    public GoogleMap() throws IOException, JSONException {
        GeocodingAdressGoogleMapsAPI geocodingAdressGoogleMapsAPI = new GeocodingAdressGoogleMapsAPI(Constant.KEY_GOOGLE_API);
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

                            try {
                                geocodingAdressGoogleMapsAPI.calculateAdress(latitude +  "," +longitude);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            hashMap.put(coordinates, new CurrentPoint(longitude,latitude,
                                    geocodingAdressGoogleMapsAPI.getAdress(),null));

                            jTextField.setText(geocodingAdressGoogleMapsAPI.getAdress());
                            System.out.println(hashMap);
                            DialogDateTimePicker dialogDateTimePicker = new DialogDateTimePicker();
                            dialogDateTimePicker.setVisible(true);

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
    private static JTextField jTextField;
    private static JFrame frame;
    public static void Interface (String[] args) throws IOException, JSONException {

        final GoogleMap sample = new GoogleMap();
        frame = new JFrame("Travel Map");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        Object[] headers = { "Name", "Surname", "Telephone" };

        //Массив содержащий информацию для таблицы
        Object[][] data = {
                { "John", "Smith", "1112221" },
                { "Ivan", "Black", "2221111" },
                { "George", "White", "3334444" },
                { "Bolvan", "Black", "2235111" },
                { "Serg", "Black", "2221511" },
                { "Pussy", "Black", "2221111" },
                { "Tonya", "Red", "2121111" },
                { "Elise", "Green", "2321111" },
        };
        frame.add(sample, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        jPanel.setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,3,4,2);
        JTable jTable = new JTable(data,headers);
        JScrollPane jscrlp = new JScrollPane(jTable);
        jscrlp.setPreferredSize(new Dimension(300,300));
        c.gridx = 0;
        c.gridy = 0;
        jTextField = new JTextField("",30);
        jPanel.add(new JLabel("Точка 1"),c);
        c.gridx = 0;
        c.gridy = 1;
        jPanel.add(jTextField,c);
        c.gridx = 0;
        c.gridy = 2;
        jPanel.add(jscrlp,c);
        frame.add(jPanel, BorderLayout.EAST);
        frame.setPreferredSize(new Dimension(dimension.width,dimension.height));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
    }

    static class DialogDateTimePicker extends JDialog{
        public DialogDateTimePicker(){
            super(frame,"Выбор даты и времени", true);
            setLayout(new FlowLayout());
            JLabel jLabel = new JLabel("Выберите дату и время");
            DateTimePicker dateTimePicker = new DateTimePicker();
            JButton jButton = new JButton("Выбрать");
            add(jLabel);
            add(dateTimePicker);
            add(jButton);
            setBounds(MouseInfo.getPointerInfo().getLocation().x,MouseInfo.getPointerInfo().getLocation().y,350,100);

            jButton.addActionListener(e -> {
                String date = dateTimePicker.getDatePicker().toString();
                String time = dateTimePicker.getTimePicker().toString();
                if ((!date.equals(""))&&(!time.equals(""))){
                    System.out.println(date + " / " + time);
                    hide();
                }
                else
                {
                    System.out.println("Тут должен быть Error Dialog");
                }

            });
        }
    }
}