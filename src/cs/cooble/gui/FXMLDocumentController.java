package cs.cooble.gui;

import cs.cooble.cypher.Cypher;
import cs.cooble.main.Translator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Matej on 20.7.2018.
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TextArea sourceTxt;

    @FXML
    private TextArea outTxt;
    @FXML
    private ChoiceBox cypherChoice;

    @FXML
    private ListView sourceAttrib;
    @FXML
    private ListView inAttrib;

    @FXML
    private Button decodeBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sourceTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            refreshTranslation();
        });

        cypherChoice.getItems().addAll(Translator.getCyphers());
        cypherChoice.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Translator.setCurrentCypher(cypherChoice.getItems().indexOf(newValue));
                prepareAttributes(Translator.getCurrentCypher());

            }
        });
        inAttrib.setCellFactory(TextFieldListCell.forListView());

        inAttrib.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> t) {
                inAttrib.getItems().set(t.getIndex(), t.getNewValue());
                String[] values = new String[inAttrib.getItems().size()];
                for (int i = 0; i < values.length; i++) {
                    values[i]=inAttrib.getItems().get(i).toString();
                }
                Translator.getCurrentCypher().setAttributes(values);
                refreshTranslation();
            }

        });


        cypherChoice.getSelectionModel().select(0);

        decodeBtn.setOnAction(event -> {
            Translator.decode();
            refreshTranslation();
            prepareAttributes(Translator.getCurrentCypher());
        });
    }
    private void prepareAttributes(Cypher cypher){
        sourceAttrib.getItems().clear();
        sourceAttrib.getItems().addAll(cypher.getAttributesNames());
        inAttrib.getItems().clear();
        inAttrib.getItems().addAll(cypher.getAttributes());
        refreshTranslation();
    }

    private void refreshTranslation(){
        outTxt.setText(Translator.translate(sourceTxt.getText()));
    }

}