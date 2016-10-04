package se206_voxspell;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class UserSelectionMenuController {

    @FXML
    private TextField userTextField;

    @FXML
    private ComboBox<?> wordListComboBox;

    @FXML
    private Button addUserPlayBtn;

    @FXML
    private ChoiceBox<?> loadProfileComboBox;

    @FXML
    private Button loadUserPlayBtn;

}
