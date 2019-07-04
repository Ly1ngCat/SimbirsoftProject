package controllers;

import controllers.googlemapsclasses.PlaceSearchGoogleMapsAPI;
import model.CurrentPoint;
import model.PlaceModel;
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
                JOptionPane.showMessageDialog(frame, "Выберите хотя бы одну точку для получения рекомендаций",
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
                    searchField.setText("Вы ввели в строку поиска - " + searchField.getText() + ", и нажали enter");
            }
        });
    }

    private static void showAndGetRecommendation(List<ArrayList<String>> allRecs, ArrayList<CurrentPoint> currentPoints) {
        JFrame recommendFrame = new JFrame();
        JTextArea recommendArea = new JTextArea(10, 50);
        recommendFrame.setLayout(new FlowLayout());
        recommendArea.setLineWrap(true);
        recommendArea.setFont(new Font("Arial", Font.PLAIN, 14));
        recommendArea.setBackground(new Color(57, 237, 152));
        recommendFrame.setTitle("Рекомендации для ваших путешествий");
        JScrollPane jScrollPane = new JScrollPane(recommendArea);
        jScrollPane.setPreferredSize(new Dimension(600, Toolkit.getDefaultToolkit().getScreenSize().height - 200));
        recommendFrame.add(jScrollPane);
        recommendArea.append("Информация по выбранным местам для путешествия: \n\n");
        for (int i = 0; i < currentPoints.size(); i++) {
            PlaceSearchGoogleMapsAPI placeSearchGoogleMapsAPI = new PlaceSearchGoogleMapsAPI();
            ArrayList<PlaceModel> foundPlaces = placeSearchGoogleMapsAPI.generateListPlaceModel(
                    String.valueOf(currentPoints.get(i).getLatitude()),
                    String.valueOf(currentPoints.get(i).getLongitude()),
                    PlaceSearchGoogleMapsAPI.typePlace.lodging,
                    "10000");
            
            recommendArea.append("Дата: " + currentPoints.get(i).getForecastDate()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
                    + ".\nМесто: " + currentPoints.get(i).getAddress()
                    + ".\nТемпература: " + currentPoints.get(i).getWeather().getPredictedTemp() + " C"
                    + ".\nВетер: " + currentPoints.get(i).getWeather().getWindSpeed() + " м/с.");
            recommendArea.append("\nРекомендуем взять с собой следующие вещи:");
            recommendArea.append("\nОдежду: " + allRecs.get(i).get(1));
            recommendArea.append("\nАксессуары: " + allRecs.get(i).get(0) + "\n\n");

            if (foundPlaces != null && foundPlaces.size()!= 0)
            {
                recommendArea.append("Вы можете в округе найти следующие отели:\n\n");
                for (int j=0;j<foundPlaces.size();j++)
                {
                    recommendArea.append("Название: " +foundPlaces.get(j).name
                            +".\nАдрес: "+foundPlaces.get(j).vicinity
                            +".\nРейтинг: "+foundPlaces.get(j).rating+"\n\n");
                }
            }
            else
            {
                recommendArea.append("К сожалению округе отели не найдены :(\n\n");
            }
        }
        recommendFrame.setVisible(true);
        recommendFrame.pack();
    }

}