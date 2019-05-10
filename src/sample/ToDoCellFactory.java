package sample;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;

public class ToDoCellFactory implements Callback <ListView<Task>, ListCell<Task>> {


    @Override
    public ListCell<Task> call(ListView<Task> param) {
        ListCell<Task> listcell = new TaskCell();


        return listcell;
    }
}


