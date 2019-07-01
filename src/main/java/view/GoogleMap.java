package view;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.examples.MapOptionsExample;

import controllers.googlemapsclasses.GeocodingAdressGoogleMapsAPI;
import controllers.workerXML.RecommendationParser;
import model.CurrentPoint;
import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;
import model.CurrentPointTableModel;
import org.json.JSONException;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.*;
import java.util.List;


public class GoogleMap extends MapView {
    //private OptionsWindow optionsWindow;
    private List<ArrayList<String>> allRecs;
    public List<ArrayList<String>> allRecommendations(ArrayList<CurrentPoint> points)
    {
        Collections.sort(points); //сортируем массив маркеров по ДАТЕ в порядке возрастания
        allRecs=new ArrayList<>();
        for (int i=0; i< points.size(); i++)
        {
            allRecs.add(RecommendationParser.getRecomendation(points.get(i).weather.weatherType.toLowerCase(),
                    points.get(i).weather.predictedTemp));
        }
        return allRecs;
    }

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
                                currentPoint.setId(++curretPointId);
                                currentPoint.setLatitude(latitude);
                                currentPoint.setLongitude(longitude);
                                currentPoint.setAdressString(geocodingAdressGoogleMapsAPI.getAdress());
                                currentPoint.setWeather();

                                hashMap.put(coordinates, currentPoint);
                                currentPoints.add(currentPoint);
                                currentPointTableModel.fireTableDataChanged();
                                //Weather weather = new Weather(currentPoint);
                                jTextArea.setText(geocodingAdressGoogleMapsAPI.getAdress());

                                //System.out.println(hashMap);
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
                                        currentPoints.remove(getCurrentPointByCoordinates(latitude,longitude));
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

    private static JTextArea jTextArea;
    private static JFrame frame;
    private static boolean isDateTimeSet = false;
    private static CurrentPointTableModel currentPointTableModel;
    private static CurrentPoint currentPoint;
    private static ArrayList<CurrentPoint> currentPoints;
    private static int curretPointId = 0;
    public static void Interface(String[] args) throws IOException, JSONException {

        final GoogleMap sample = new GoogleMap();
        frame = new JFrame("Travel Map");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame.add(sample, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        jPanel.setLayout(gbl);
        jPanel.setBackground(new Color(150,237, 148));
        currentPoints = new ArrayList<>();
        currentPointTableModel = new CurrentPointTableModel(currentPoints);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 3, 4, 2);
        JTable jTable = new JTable(currentPointTableModel);
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
        for (int i=0;i<jTable.getColumnModel().getColumnCount();i++){
            TableColumn tableColumn = jTable.getColumnModel().getColumn(i);
            switch (i){
                case 0: tableColumn.setMaxWidth(30); break;
                case 1: tableColumn.setPreferredWidth(140);
            }
        }
        JButton getRecommendationButton = new JButton("Получить рекомендации");
        getRecommendationButton.addActionListener(e->{
            if (currentPoints.size()!=0){
                showAndGetRecommendation(sample.allRecommendations(currentPoints),currentPoints);
            }
            else
            {
                JOptionPane.showMessageDialog(frame, "Выберите хотябы одну точку, для получения реккомендаций",
                        "Ошибка получения рекомендаций", JOptionPane.ERROR_MESSAGE);
            }
        });

        c.gridx = 0;
        c.gridy = 3;
        jPanel.add(getRecommendationButton,c);
        frame.add(jPanel, BorderLayout.EAST);
        frame.setPreferredSize(new Dimension(dimension.width, dimension.height));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();

    }

    private static void showAndGetRecommendation(List<ArrayList<String>> allRecs, ArrayList<CurrentPoint> currentPoints) {
        JFrame recomendFrame = new JFrame();
        JTextArea recomendArea = new JTextArea(10,50);
        recomendFrame.setSize(new Dimension(600,800));
        recomendFrame.setLayout(new FlowLayout());
        recomendArea.setLineWrap(true);
        recomendArea.setFont(new Font("Arial", Font.PLAIN, 14));
        recomendFrame.add(recomendArea);

        recomendArea.setBackground(new Color(57,237, 152));
        recomendFrame.setTitle("Рекомендации для ваших путешествий");
        recomendArea.append("Информация по выбранным местам для путешествия: \n\n");
        for (int i=0;i<currentPoints.size();i++)
        {
            recomendArea.append("Дата: "+currentPoints.get(i).getForecastDate()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
                    +".\nМесто: "+currentPoints.get(i).getAdressString()
                    +".\nТемпература: "+currentPoints.get(i).weather.predictedTemp+" C"
                    +".\nВетер: "+currentPoints.get(i).weather.windSpeed+" м/с.");
            recomendArea.append("\nРекомендуем взять с собой следующие вещи:");
            recomendArea.append("\nОдежду: "+allRecs.get(i).get(1));
            recomendArea.append("\nАксессуары: "+allRecs.get(i).get(0)+"\n\n");
        }
        recomendFrame.setVisible(true);
        recomendFrame.pack();
    }

    private static CurrentPoint getCurrentPointByCoordinates(double latitude, double longitude){
        for (CurrentPoint point : currentPoints) {
            if (point.getLatitude() == latitude && point.getLongitude() == longitude){
                return point;
            }
        }
        return null;

    }
    private static void fixIds(){
        int id = 1;
        for (CurrentPoint point : currentPoints) {
            point.setId(id++);
        }
        curretPointId = currentPoints.size();
    }

    static class DialogDateTimePicker extends JDialog {
        private DialogDateTimePicker() {
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
                }

            });
        }
    }

    /*public void addNotify() {
        super.addNotify();
        this.optionsWindow = new OptionsWindow(this, new Dimension(350, 40)) {
            public void initContent(JWindow contentWindow) {
                JPanel content = new JPanel(new GridBagLayout());
                content.setBackground(Color.white);
                Font robotoPlain13 = new Font("Roboto", 0, 13);
                final JTextField searchField = new JTextField();
                searchField.setText("Ульяновск");
                searchField.setToolTipText("Введите нужное место");
                searchField.setBorder(BorderFactory.createEmptyBorder());
                searchField.setFont(robotoPlain13);
                searchField.setForeground(new Color(33, 33, 33));

                JButton searchButton = new JButton();
                searchButton.setIcon(new ImageIcon(MapOptionsExample.class.getResource("res/search.png")));
                searchButton.setRolloverIcon(new ImageIcon(MapOptionsExample.class.getResource("res/search_hover.png")));
                searchButton.setBorder(BorderFactory.createEmptyBorder());
                searchButton.setToolTipText("Поиск");

                searchButton.setUI(new BasicButtonUI());
                searchButton.setOpaque(false);


                content.add(searchField, new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, 18, 2, new Insets(11, 11, 11, 0), 0, 0));
                content.add(searchButton, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 18, 0, new Insets(11, 0, 11, 11), 0, 0));
                contentWindow.getContentPane().add(content);
            }
        };
    }*/
}