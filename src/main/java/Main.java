import com.teamdev.jxmaps.LatLng;
import controllers.UserInterfaceController;
import model.CurrentPoint;
import net.aksingh.owmjapis.api.APIException;
import org.json.JSONException;
import view.GUI.VisualInterface;
import view.GoogleMap;
import controllers.openweathermapclasses.*;

import java.io.IOException;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws APIException, JSONException, IOException {

        VisualInterface.Interface(args);
        UserInterfaceController uic = new UserInterfaceController();
        uic.initController();
    }
}
