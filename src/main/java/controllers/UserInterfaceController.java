package controllers;

import model.CurrentPoint;
import view.GUI.VisualInterface;
import view.GoogleMap;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class UserInterfaceController {
    public UserInterfaceController(){

    }

    public void initController(){
        JTable jTable = VisualInterface.getJTable();
        JButton getRecommendation = VisualInterface.getGetRecommendationButton();
        ArrayList<CurrentPoint> currentPoints = VisualInterface.getCurrentPoints();
        GoogleMap googleMap = VisualInterface.getGoogleMap();
        JFrame frame = VisualInterface.getFrame();
        JTextField searchField = VisualInterface.getSearchField();
        JButton searchButton = VisualInterface.getSearchButton();


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

        getRecommendation.addActionListener(e->{
            if (currentPoints.size() != 0) {
                showAndGetRecommendation(googleMap.allRecommendations(currentPoints), currentPoints);
            } else {
                JOptionPane.showMessageDialog(frame, "Выберите хотябы одну точку, для получения реккомендаций",
                        "Ошибка получения рекомендаций", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Поиск")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Поиск");
                }
            }
        });

        searchButton.addActionListener(e->{
            searchField.setText("Вы нажали на кнопку Поиска");
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    searchField.setText("Вы ввели в строку поиска - " + searchField.getText() + ", и нажати enter");
            }
        });
    }

    private static void showAndGetRecommendation(List<ArrayList<String>> allRecs, ArrayList<CurrentPoint> currentPoints) {
        JFrame recomendFrame = new JFrame();
        JTextArea recomendArea = new JTextArea(10, 50);
        recomendFrame.setLayout(new FlowLayout());
        recomendArea.setLineWrap(true);
        recomendArea.setFont(new Font("Arial", Font.PLAIN, 14));
        recomendArea.setBackground(new Color(57, 237, 152));
        recomendFrame.setTitle("Рекомендации для Ваших путешествий");
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

}
