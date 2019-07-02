import com.teamdev.jxmaps.LatLng;
import model.CurrentPoint;
import net.aksingh.owmjapis.api.APIException;
import org.json.JSONException;
import view.GUI.VisuaIinterface;
import view.GoogleMap;
import controllers.openweathermapclasses.*;

import java.io.IOException;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws APIException, JSONException, IOException {

        VisuaIinterface.Interface(args);
    }
}
