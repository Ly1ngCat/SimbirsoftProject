import controllers.UserInterfaceController;
import net.aksingh.owmjapis.api.APIException;
import org.json.JSONException;
import view.GUI.VisualInterface;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws JSONException, IOException {
        VisualInterface.Interface(args);
        UserInterfaceController uic = new UserInterfaceController();
        uic.initController();
    }
}
