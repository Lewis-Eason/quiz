package quiz.alerts;

import javafx.scene.control.Alert;

public class AlertBoxes {

    public static void confirmAlertBoxes(String alertInformation) {
        Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION, alertInformation);
        confirmAlert.showAndWait();
    }

    public static void errorAlertBoxes(String alertInformation) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR, alertInformation);
        errorAlert.showAndWait();
    }
}
