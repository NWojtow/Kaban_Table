package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.MainController.*;

public class EditController implements Initializable{


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
    Button editButton;

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



    public void setPrompt() {


        if (ifInProgress) {
            addnotationText.setText(inProgressTasks.get(editingIndex).getAddnotation());
            datePickerTask.setValue(inProgressTasks.get(editingIndex).getExpiringdate());
            LevelOfPriority temp = inProgressTasks.get(editingIndex).getLevel();
            if(temp==LevelOfPriority.LOW){
                priorityButton.setText("Low");
            }
            else if(temp==LevelOfPriority.MEDIUM){
                priorityButton.setText("Medium");
            }
            else if(temp==LevelOfPriority.HIGH){
                priorityButton.setText("High");
            }

        } else {
            addnotationText.setText(todoList.get(editingIndex).getAddnotation());
            datePickerTask.setValue(todoList.get(editingIndex).getExpiringdate());
            LevelOfPriority temp = todoList.get(editingIndex).getLevel();
            if(temp==LevelOfPriority.LOW){
                priorityButton.setText("Low");
            }
            else if(temp==LevelOfPriority.MEDIUM){
                priorityButton.setText("Medium");
            }
            else if(temp==LevelOfPriority.HIGH){
                priorityButton.setText("High");
            }

        }
    }


    public void editButton(){

       Stage stage =(Stage) editButton.getScene().getWindow();
       if(ifInProgress==false) {
           if (addnotationText.getText() != null) {
               MainController.todoList.get(editingIndex).setAddnotation(addnotationText.getText());
           }
           if (level != null) {
               MainController.todoList.get(editingIndex).setLevel(level);
           }
           if (datePickerTask.getValue() != null) {
               MainController.todoList.get(editingIndex).setExpiringdate(datePickerTask.getValue());
           }

           Task temp = MainController.todoList.get(editingIndex);
           MainController.todoList.remove(editingIndex);
           MainController.todoList.add(editingIndex, temp);
       }
       else if(ifInProgress){
           if (addnotationText.getText() != null) {
               MainController.inProgressTasks.get(editingIndex).setAddnotation(addnotationText.getText());
           }
           if (level != null) {
               MainController.inProgressTasks.get(editingIndex).setLevel(level);
           }
           if (datePickerTask.getValue() != null) {
               MainController.inProgressTasks.get(editingIndex).setExpiringdate(datePickerTask.getValue());
           }

           Task temp = MainController.inProgressTasks.get(editingIndex);
           MainController.inProgressTasks.remove(editingIndex);
           MainController.inProgressTasks.add(editingIndex, temp);
       }
        stage.close();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setPrompt();
    }
}

