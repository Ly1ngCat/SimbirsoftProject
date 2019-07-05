package controllers;

import constants.Constant;
import library.googlemapsclasses.PlaceSearchGoogleMapsAPI;
import model.CurrentPoint;
import model.PlaceModel;
import view.GUI.VisualInterface;
import view.GoogleMap;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.text.*;
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
                try {
                    showAndGetRecommendation(googleMap.allRecommendations(currentPoints), currentPoints);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
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

    private static void showAndGetRecommendation(List<ArrayList<String>> allRecs, ArrayList<CurrentPoint> currentPoints) throws BadLocationException {
        JFrame recommendFrame = new JFrame();
        recommendFrame.setLayout(new FlowLayout());
        recommendFrame.setTitle("Рекомендации для ваших путешествий");

        Container content = recommendFrame.getContentPane();
        StyleContext context = new StyleContext();
        StyledDocument document = new DefaultStyledDocument(context);

        Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(style, new Color(129,123,70));
        SimpleAttributeSet attributesBold = new SimpleAttributeSet();
        StyleConstants.setBold(attributesBold, true);

        SimpleAttributeSet attributesPlain = new SimpleAttributeSet();
        StyleConstants.setBold(attributesPlain, false);

        StringBuilder info = new StringBuilder("");
        document.insertString(document.getLength(), "ИНФОРМАЦИЯ ПО ВЫБРАННЫМ МЕСТАМ ДЛЯ ПУТЕШЕСТВИЯ: \n\n",
                attributesBold);

        for (int i = 0; i < currentPoints.size(); i++) {
            PlaceSearchGoogleMapsAPI placeSearchGoogleMapsAPI = new PlaceSearchGoogleMapsAPI();
            ArrayList<PlaceModel> foundPlaces = placeSearchGoogleMapsAPI.generateListPlaceModel(
                    String.valueOf(currentPoints.get(i).getLatitude()),
                    String.valueOf(currentPoints.get(i).getLongitude()),
                    PlaceSearchGoogleMapsAPI.typePlace.lodging,
                    Constant.RADIUS);
            try {
                document.insertString(document.getLength(), "Дата: ",
                        attributesBold);
                info.append(currentPoints.get(i).getForecastDate()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                info.append(".\n");
                document.insertString(document.getLength(), info.toString(),
                        attributesPlain);
                info.setLength(0);
                document.insertString(document.getLength(), "Место: ",
                        attributesBold);
                info.append(currentPoints.get(i).getAddress());
                info.append(".\n");
                document.insertString(document.getLength(), info.toString(),
                        attributesPlain);
                info.setLength(0);
                document.insertString(document.getLength(), "Температура: ",
                        attributesBold);
                info.append(currentPoints.get(i).getWeather().getPredictedTemp());
                info.append(" C.\n");
                document.insertString(document.getLength(), info.toString(),
                        attributesPlain);
                info.setLength(0);
                document.insertString(document.getLength(), "Ветер: ",
                        attributesBold);
                info.append(currentPoints.get(i).getWeather().getWindSpeed());
                info.append(" м/с.\n");
                document.insertString(document.getLength(), info.toString(),
                        attributesPlain);
                info.setLength(0);

                document.insertString(document.getLength(), "\nРекомендуем взять с собой следующие вещи:\n",
                        attributesBold);
                document.insertString(document.getLength(), "Одежду: ",
                        attributesBold);
                info.append(allRecs.get(i).get(1));
                info.append(".\n");
                document.insertString(document.getLength(), info.toString(),
                        attributesPlain);
                info.setLength(0);
                document.insertString(document.getLength(), "Аксессуары: ",
                        attributesBold);
                info.append(allRecs.get(i).get(0));
                info.append(".\n\n");
                document.insertString(document.getLength(), info.toString(),
                        attributesPlain);
                info.setLength(0);

                if (foundPlaces != null && foundPlaces.size()!= 0)
                {
                    document.insertString(document.getLength(), "В округе вы можете найти следующие отели:\n\n",
                            attributesBold);
                    for (int j=0;j<foundPlaces.size();j++)
                    {
                        document.insertString(document.getLength(), "Название: ",
                                attributesBold);
                        info.append(foundPlaces.get(j).name);
                        info.append(".\n");
                        document.insertString(document.getLength(), info.toString(),
                                attributesPlain);
                        info.setLength(0);
                        document.insertString(document.getLength(), "Адрес: ",
                                attributesBold);
                        info.append(foundPlaces.get(j).vicinity);
                        info.append(".\n");
                        document.insertString(document.getLength(), info.toString(),
                                attributesPlain);
                        info.setLength(0);
                        document.insertString(document.getLength(), "Рейтинг: ",
                                attributesBold);
                        info.append(foundPlaces.get(j).rating);
                        info.append("\n\n");
                        document.insertString(document.getLength(), info.toString(),
                                attributesPlain);
                        info.setLength(0);
                    }
                }
                else
                {
                    document.insertString(document.getLength(), "К сожалению округе отели не найдены :(\n\n",
                            attributesBold);
                }

            } catch (BadLocationException badLocationException) {
                System.err.println("Oops");
            }

            info.append("-------------------------------------------------------------------------------------------\n");
            document.insertString(document.getLength(), info.toString(),
                    attributesPlain);
            info.setLength(0);

        }

        JTextPane textPane = new JTextPane(document);
        textPane.setBackground(new Color(234,230,210));
        textPane.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(600, Toolkit.getDefaultToolkit().getScreenSize().height-100));
        content.add(scrollPane, BorderLayout.CENTER);
        recommendFrame.setVisible(true);
        recommendFrame.pack();
    }

}