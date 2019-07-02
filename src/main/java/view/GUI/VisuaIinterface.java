package view.GUI;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.teamdev.jxmaps.examples.MapOptionsExample;
import com.teamdev.jxmaps.internal.internal.ipc.c;
import model.CurrentPoint;
import model.CurrentPointTableModel;
import org.json.JSONException;
import view.GoogleMap;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static view.GUI.VisuaIinterface.DialogDateTimePicker.showAndGetRecommendation;


public class VisuaIinterface {
    public static JTextArea jTextArea;
    private static JFrame frame;
    public static boolean isDateTimeSet = false;
    public static CurrentPointTableModel currentPointTableModel;
    public static CurrentPoint currentPoint;
    public static ArrayList<CurrentPoint> currentPoints;
    public static int curretPointId = 0;

    public static void Interface(String[] args) throws IOException, JSONException {

        final GoogleMap sample = new GoogleMap();
        frame = new JFrame("Travel Map");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame.add(sample, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        jPanel.setLayout(gbl);
        jPanel.setBackground(new Color(150, 237, 148));
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
        for (int i = 0; i < jTable.getColumnModel().getColumnCount(); i++) {
            TableColumn tableColumn = jTable.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    tableColumn.setMaxWidth(30);
                    break;
                case 1:
                    tableColumn.setPreferredWidth(140);
            }
        }
        JButton getRecommendationButton = new JButton("Получить рекомендации");
        getRecommendationButton.addActionListener(e -> {
            if (currentPoints.size() != 0) {
                showAndGetRecommendation(sample.allRecommendations(currentPoints), currentPoints);
            } else {
                JOptionPane.showMessageDialog(frame, "Выберите хотябы одну точку, для получения реккомендаций",
                        "Ошибка получения рекомендаций", JOptionPane.ERROR_MESSAGE);
            }
        });

        c.gridx = 0;
        c.gridy = 3;
        jPanel.add(getRecommendationButton, c);
        frame.add(jPanel, BorderLayout.EAST);
        frame.setPreferredSize(new Dimension(dimension.width, dimension.height));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
    }

    public static class DialogDateTimePicker extends JDialog {
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
                }

            });
        }

        public static void showAndGetRecommendation(List<ArrayList<String>> allRecs, ArrayList<CurrentPoint> currentPoints) {
            JFrame recomendFrame = new JFrame();
            JTextArea recomendArea = new JTextArea(10, 50);
            recomendFrame.setLayout(new FlowLayout());
            recomendArea.setLineWrap(true);
            recomendArea.setFont(new Font("Arial", Font.PLAIN, 14));
            recomendArea.setBackground(new Color(57, 237, 152));
            recomendFrame.setTitle("Рекомендации для ваших путешествий");
            JScrollPane jScrollPane = new JScrollPane(recomendArea);
            jScrollPane.setPreferredSize(new Dimension(600, Toolkit.getDefaultToolkit().getScreenSize().height - 200));
            recomendFrame.add(jScrollPane);
            recomendArea.append("Информация по выбранным местам для путешествия: \n\n");
            for (int i = 0; i < currentPoints.size(); i++) {
                recomendArea.append("Дата: " + currentPoints.get(i).getForecastDate()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
                        + ".\nМесто: " + currentPoints.get(i).getAdressString()
                        + ".\nТемпература: " + currentPoints.get(i).weather.predictedTemp + " C"
                        + ".\nВетер: " + currentPoints.get(i).weather.windSpeed + " м/с.");
                recomendArea.append("\nРекомендуем взять с собой следующие вещи:");
                recomendArea.append("\nОдежду: " + allRecs.get(i).get(1));
                recomendArea.append("\nАксессуары: " + allRecs.get(i).get(0) + "\n\n");
            }
            recomendFrame.setVisible(true);
            recomendFrame.pack();
        }

        public static CurrentPoint getCurrentPointByCoordinates(double latitude, double longitude) {
            for (CurrentPoint point : currentPoints) {
                if (point.getLatitude() == latitude && point.getLongitude() == longitude) {
                    return point;
                }
            }
            return null;

        }

        public static void fixIds() {
            int id = 1;
            for (CurrentPoint point : currentPoints) {
                point.setId(id++);
            }
            curretPointId = currentPoints.size();
        }

        public static void search() {
            JPanel content = new JPanel();
            content.setSize(600, 40);
            content.setBackground(Color.white);
            Font robotoPlain13 = new Font("Roboto", 0, 13);
            GridBagLayout gbl = new GridBagLayout();
            content.setLayout(gbl);

            final JTextField searchField = new JTextField("Поиск");
            searchField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    searchField.setText("");
                }
            });

            searchField.setFont(robotoPlain13);
            searchField.setToolTipText("Введите нужное место");
            searchField.setForeground(new Color(33, 33, 33));
            searchField.setVisible(true);

            JButton searchButton = new JButton();
            searchButton.setIcon(new ImageIcon(MapOptionsExample.class.getResource("res/search.png")));
            searchButton.setRolloverIcon(new ImageIcon(MapOptionsExample.class.getResource("res/search_hover.png")));
            searchButton.setToolTipText("Поиск");

            content.add(searchField);
            content.add(searchButton);

            frame.add(content, BorderLayout.BEFORE_FIRST_LINE);

        }
    }
}



