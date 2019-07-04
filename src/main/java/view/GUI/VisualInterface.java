package view.GUI;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.teamdev.jxmaps.examples.MapOptionsExample;
import model.CurrentPoint;
import model.CurrentPointTableModel;
import org.json.JSONException;
import view.GoogleMap;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;



public class VisualInterface {
    public static JTextArea jTextArea;
    private static JFrame frame;
    public static boolean isDateTimeSet = false;
    public static CurrentPointTableModel currentPointTableModel;
    public static CurrentPoint currentPoint;

    private static ArrayList<CurrentPoint> currentPoints;
    public static int currentPointId = 0;
    private static JTable jTable;
    private static JButton getRecommendationButton;
    private static GoogleMap googleMap;
    private static JTextField searchField;
    private static JButton searchButton;

    public static JTable getJTable() {
        return jTable;
    }

    public static JButton getGetRecommendationButton() {
        return getRecommendationButton;
    }

    public static ArrayList<CurrentPoint> getCurrentPoints() {
        return currentPoints;
    }

    public static GoogleMap getGoogleMap() {
        return googleMap;
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static JTextField getSearchField() { return searchField; }

    public static JButton getSearchButton() { return searchButton; }

    public static void Interface(String[] args) throws IOException, JSONException {

        googleMap = new GoogleMap();
        frame = new JFrame("Travel Map");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame.add(googleMap, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        jPanel.setLayout(gbl);
        jPanel.setBackground(new Color(198,248,255));

        currentPoints = new ArrayList<>();
        currentPointTableModel = new CurrentPointTableModel(currentPoints);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 3, 4, 2);
        jTable = new JTable(currentPointTableModel);
        JScrollPane jscrlp = new JScrollPane(jTable);
        jscrlp.setPreferredSize(new Dimension(300, 300));
        c.gridx = 0;
        c.gridy = 0;
        jTextArea = new JTextArea("", 3, 30);
        jTextArea.setLineWrap(true);
        jTextArea.setFont(new Font("Arial", Font.ITALIC, 14));
        jPanel.add(new JLabel("Выбранные местоположения"), c);
        c.gridx = 0;
        c.gridy = 2;
        jPanel.add(jscrlp, c);
        getRecommendationButton = new JButton("Получить рекомендации");

        c.gridx = 0;
        c.gridy = 3;
        jPanel.add(getRecommendationButton, c);
        search();
        frame.add(jPanel, BorderLayout.EAST);
        frame.setPreferredSize(new Dimension(dimension.width, dimension.height));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
    }
    private static void search() {
        JPanel content = new JPanel();
        content.setSize(600, 40);
        content.setBackground(Color.white);
        Font robotoPlain13 = new Font("Roboto", 0, 13);
        GridBagLayout gbl = new GridBagLayout();
        content.setLayout(gbl);

        searchField = new JTextField();
        searchField.setColumns(50);

        searchField.setFont(robotoPlain13);
        searchField.setToolTipText("Введите нужное место");
        searchField.setForeground(new Color(33, 33, 33));
        searchField.setVisible(true);

        searchButton = new JButton();
        searchButton.setIcon(new ImageIcon(MapOptionsExample.class.getResource("res/search.png")));
        searchButton.setRolloverIcon(new ImageIcon(MapOptionsExample.class.getResource("res/search_hover.png")));
        searchButton.setToolTipText("Поиск");

        content.add(searchField);
        content.add(searchButton);

        frame.add(content, BorderLayout.BEFORE_FIRST_LINE);
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
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Введите, пожалуйста, дату и " +
                            "время нахождения в указанной точке", "Не введена дата или время", JOptionPane.WARNING_MESSAGE);
                }

            });
        }

            }
}



