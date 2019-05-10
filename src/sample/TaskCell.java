package sample;


import javafx.scene.control.*;
import javafx.scene.layout.GridPane;



public class TaskCell extends ListCell<Task>  {


    private Button actionBtn;
    private Label name;
    private GridPane pane;


    @Override
    protected void updateItem(Task item, boolean empty) {

        final Tooltip tooltip = new Tooltip();
        super.updateItem(item, empty);

        setEditable(false);

        if (item != null) {

            setText(item.toString());
            tooltip.setText(item.getAddnotation());
            setTooltip(tooltip);
            setGraphic(pane);

            if(item.getLevel()==LevelOfPriority.MEDIUM){
                setStyle("-fx-background-color: rgba(255,135,24,0.72)");
            }
            else if(item.getLevel()==LevelOfPriority.HIGH){
                setStyle("-fx-background-color: rgba(190,12,4,0.74)");
            }

        } else {
            setStyle(null);
            setText("");
            setGraphic(null);
        }

    }

}



