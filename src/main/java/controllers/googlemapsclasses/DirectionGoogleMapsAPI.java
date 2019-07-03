package controllers.googlemapsclasses;

import com.google.common.collect.Maps;
import model.CurrentPoint;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class DirectionGoogleMapsAPI extends AbstractSampleGoogleMapsAPI implements RequestResponseLanguage{

    private final String baseUrl = "https://maps.googleapis.com/maps/api/directions/json";// ���� � Geocoding API ��

    protected enum travelModes {driving, walking, bicycling, transit}

    private String distance;
    private String duration;

    public DirectionGoogleMapsAPI() { }

    public void calculateRoute(String origin, String destination, travelModes travelModes) {

        String urlJsonReques = createJSONRequest(origin, destination, travelModes);
        calculateJSONRequestRoute(urlJsonReques);

    }


    private String createJSONRequest(String origin, String destination, travelModes travelModes) {

        final Map<String, String> params = Maps.newHashMap();
        params.put("sensor", "false");// ���������, ������� �� ������ �� �������������� �� ���������� � ��������
        params.put("language", answerLanguage);// ���� ������ �� ������� �� ����� ��������
        params.put("mode", travelModes.toString());// ������ �����������, ����� ���� driving, walking, bicycling. transit ����������� �������� �� ��������� ������������� ���������� (��� �������). ���� �� ���������� ����� transit, ����� ������������� ������� ���� departure_time��� arrival_time. ���� ����� �� �������, �� departure_time��������� ������������ ������� ����� (�� ���� ����� ����������� �� ��������� ����� �������� �������). �� ����� ������ ��� ������� �������� transit_mode� / ��� transit_routing_preference.
        params.put("origin", origin);// ����� ��� ��������� �������� ������ �
        // ���������� ������ ��������
        params.put("destination", destination);// ����� ��� ��������� �������� ������ � �������
        // ������� ��������� ������ ��������
        params.put("key", KEY_GOOGLE_API);//���� API
        final String url = baseUrl + '?' + encodeParams(params);// ���������� ���� � �����������
        return url;
    }

    private void calculateJSONRequestRoute(String url) {
        try {
            final JSONObject response = JsonReader.read(url);// ������ ������ � ���������� � �������� �� ���� �����
            // ��� ������� �������� ���������� ����� ������ � ������ � ���������� ����� �������� �� ����
            // //results[0]/geometry/location/lng � //results[0]/geometry/location/lat
            JSONObject location = response.getJSONArray("routes").getJSONObject(0);
            location = location.getJSONArray("legs").getJSONObject(0);
            distance = location.getJSONObject("distance").getString("text");
            duration = location.getJSONObject("duration").getString("text");

        } catch (IOException | JSONException e) {
            distance = "";
            duration = "";
            e.printStackTrace();
        } finally {
            System.out.println(distance + "\n" + duration);
        }
    }



    public void calculateRouteWithCurrentModel(CurrentPoint origin, CurrentPoint destination, travelModes travelModes) throws IOException, JSONException {

        String stringOrigin = assignValueAddresses(origin);
        String stringDestination = assignValueAddresses(destination);

        calculateRoute(stringOrigin, stringDestination, travelModes);

    }

    private String assignValueAddresses(CurrentPoint origin) {
        String adress;
        if (origin.latitude != 0 & origin.longitude != 0) {
            adress = String.valueOf(origin.latitude) + "," + String.valueOf(origin.longitude);
        } else {
            adress = origin.adressString;
        }
        return adress;
    }


    public String getDitanceString() {
        return distance;
    }

    public String getDurationString() {
        return duration;
    }

}