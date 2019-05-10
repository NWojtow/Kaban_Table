package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;


public class AddTaskController {
    @FXML
    TextField titleText;
    @FXML
    SplitMenuButton priorityButton;

    @FXML
    DatePicker datePickerTask;

    @FXML
    ListView<Task> ToDoList;

    @FXML
    TextField addnotationText;

    @FXML
    Button addButton;


    public LevelOfPriority level;

    public void setLow(){
       level = LevelOfPriority.LOW;
       priorityButton.setText("Low");
    };
    public void setMediumPriority(){
        level=LevelOfPriority.MEDIUM;
        priorityButton.setText("Medium");
    };
    public void setHighPriority(){
        level=LevelOfPriority.HIGH;
        priorityButton.setText("High");
    };



        public void addButton(){

            Stage stage =(Stage) addButton.getScene().getWindow();

            String taskname= titleText.getText();
            LocalDate expiringdate = datePickerTask.getValue();
            String addnotation = addnotationText.getText();

            Task task =new Task(taskname,level,expiringdate,addnotation);

            try{

            if(titleText.getText()==null||datePickerTask.getValue()==null||level==null){
                throw (new IllegalArgumentException());
            }

                MainController.todoList.add(task);

            } catch(IllegalArgumentException e){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText("You have done something wrong");
                alert.setContentText("You haven't passed one of arguments"+'\n'+"Task name"+'\n'+"Expiring date"+'\n'+"Level");
                alert.showAndWait();

            }
            stage.close();


        }

}
