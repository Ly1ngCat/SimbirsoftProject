package view;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.examples.GeocoderExample;
import com.teamdev.jxmaps.examples.MapOptionsExample;

import constants.Constant;
import controllers.googlemapsclasses.GeocodingAdressGoogleMapsAPI;
import model.CurrentPoint;
import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;
import org.json.JSONException;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.*;
import java.util.HashMap;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import model.Weather;


public class GoogleMap extends MapView {

    OptionsWindow optionsWindow;

    private GoogleMap() throws IOException, JSONException {

        FileInputStream fisConfig = new FileInputStream("src/main/resources/config.properties");
        Properties propertyConfig = new Properties();
        propertyConfig.load(fisConfig);

        GeocodingAdressGoogleMapsAPI geocodingAdressGoogleMapsAPI =
                new GeocodingAdressGoogleMapsAPI(propertyConfig.getProperty("KeyGoogleAPI"));

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


                            isDateTimeSet = false;
                            DialogDateTimePicker dialogDateTimePicker = new DialogDateTimePicker();
                            dialogDateTimePicker.setVisible(true);
                            if (isDateTimeSet) {
                                infoWindow.close();
                                final Marker marker = new Marker(map);
                                marker.setPosition(mouseEvent.latLng());
                                LatLng coordinates = marker.getPosition();
                                double latitude = coordinates.getLat();
                                double longitude = coordinates.getLng();

                                geocodingAdressGoogleMapsAPI.calculateAdress(latitude + "," + longitude);
                                currentPoint.setLatitude(latitude);
                                currentPoint.setLongitude(longitude);
                                currentPoint.setAdressString(geocodingAdressGoogleMapsAPI.getAdress());

                                hashMap.put(coordinates, currentPoint);
                                Weather weather = new Weather(currentPoint);
                                try {
                                    Weather weather = new Weather(currentPoint);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                jTextArea.setText(geocodingAdressGoogleMapsAPI.getAdress());
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

                        }
                    });
                }
            }
        });
    }

    private static JTextArea jTextArea;
    private static JFrame frame;
    private static boolean isDateTimeSet = false;
    private static CurrentPoint currentPoint;

    public static void Interface(String[] args) throws IOException, JSONException {
        final GoogleMap sample = new GoogleMap();
        frame = new JFrame("Travel Map");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        Object[] headers = {"Name", "Surname", "Telephone"};

        //Массив содержащий информацию для таблицы
        Object[][] data = {
                {"John", "Smith", "1112221"},
                {"Ivan", "Black", "2221111"},
                {"George", "White", "3334444"},
                {"Bolvan", "Black", "2235111"},
                {"Serg", "Black", "2221511"},
                {"Pussy", "Black", "2221111"},
                {"Tonya", "Red", "2121111"},
                {"Elise", "Green", "2321111"},
        };
        frame.add(sample, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        jPanel.setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 3, 4, 2);
        JTable jTable = new JTable(data, headers);
        JScrollPane jscrlp = new JScrollPane(jTable);
        jscrlp.setPreferredSize(new Dimension(300, 300));
        c.gridx = 0;
        c.gridy = 0;
        jTextArea = new JTextArea("", 3, 30);
        jTextArea.setLineWrap(true);
        jTextArea.setFont(new Font("Arial", Font.ITALIC, 14));
        jPanel.add(new JLabel("Выбранное местоположение"), c);
        c.gridx = 0;
        c.gridy = 1;
        jPanel.add(jTextArea, c);
        c.gridx = 0;
        c.gridy = 2;
        jPanel.add(jscrlp, c);
        frame.add(jPanel, BorderLayout.EAST);
        frame.setPreferredSize(new Dimension(dimension.width, dimension.height));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();


    }

    static class DialogDateTimePicker extends JDialog {
        private OptionsWindow optionsWindow;

        public DialogDateTimePicker() {
            super(frame, "Выбор даты и времени", true);
            setLayout(new FlowLayout());
            JLabel jLabel = new JLabel("Выберите дату и время");
            DateTimePicker dateTimePicker = new DateTimePicker();
            dateTimePicker.getDatePicker().getSettings().setDateRangeLimits(LocalDate.now(), null);
            JButton jButton = new JButton("Выбрать");
            add(jLabel);
            add(dateTimePicker);
            add(jButton);
            setBounds(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y, 350, 100);

            jButton.addActionListener(e -> {
                String date = dateTimePicker.getDatePicker().toString();
                String time = dateTimePicker.getTimePicker().toString();
                if ((!date.equals("")) && (!time.equals(""))) {
                    if (dateTimePicker.getDateTimePermissive().isBefore(LocalDateTime.now())) {
                        JOptionPane.showMessageDialog(frame, "Нельзя выбрать время ранее нынешнего времени. \n" +
                                "Сейчас : " + LocalTime.now(), "Введено неправильное время", JOptionPane.ERROR_MESSAGE);
                    } else {
                        System.out.println(date + " / " + time);
                        isDateTimeSet = true;
                        currentPoint = new CurrentPoint();
                        currentPoint.setForecastDate(dateTimePicker.getDateTimePermissive());
                        hide();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Введите пожалуйста дату и " +
                            "время нахождения в указанной точке", "Не введена дата или время", JOptionPane.WARNING_MESSAGE);
                    if ((!date.equals("")) && (!time.equals(""))) {
                        System.out.println(date + " / " + time);
                        isDateTimeSet = true;
                        currentPoint = new CurrentPoint();
                        currentPoint.setForecastDate(dateTimePicker.getDateTimePermissive());
                        hide();
                    } else {
                        System.out.println("Тут должен быть Error Dialog");
                    }

                }
                ;
            });
        }

        public void addNotify() {
            super.addNotify();
            this.optionsWindow = new OptionsWindow(this, new Dimension(1000, 40)) {
                public void initContent(JWindow contentWindow) {
                    JPanel content = new JPanel(new GridBagLayout());
                    content.setBackground(Color.white);
                    Font robotoPlain13 = new Font("Roboto", Font.PLAIN, 13);
                    final JTextField searchField = new JTextField();
                    searchField.setText("Ульяновск");
                    searchField.setBorder(BorderFactory.createEmptyBorder());
                    searchField.setFont(robotoPlain13);
                    searchField.setForeground(new Color(31, 33, 33));

                    JButton searchButton = new JButton();
                    searchButton.setIcon(new ImageIcon(MapOptionsExample.class.getResource("res/search.png")));
                    searchButton.setRolloverIcon(new ImageIcon(MapOptionsExample.class.getResource("res/search_hover.png")));
                    searchButton.setBorder(BorderFactory.createEmptyBorder());
                    searchButton.setUI(new BasicButtonUI());
                    searchButton.setOpaque(false);
                    searchButton.setToolTipText("Поиск");


                    content.add(searchField, new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, 18, 2, new Insets(11, 11, 11, 0), 0, 0));
                    content.add(searchButton, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 18, 0, new Insets(11, 0, 11, 11), 0, 0));
                    contentWindow.getContentPane().add(content);
                }
            };
        }
    }
}
