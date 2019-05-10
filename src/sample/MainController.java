package sample;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import  javafx.scene.control.*;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;




public class MainController implements Initializable {

    @FXML
    ListView<Task> ToDoList;
    @FXML
    ListView<Task> inProgressList;
    @FXML
    ListView<Task> doneList;




    public static ObservableList<Task> todoList = FXCollections.observableArrayList();
    public static ObservableList<Task> inProgressTasks = FXCollections.observableArrayList();
    public static ObservableList<Task> doneTasks = FXCollections.observableArrayList();

    public static int editingIndex = 0;
    public static boolean ifInProgress;



    public void quitProgram() {
        System.out.println("Quit");
        Platform.exit();
        System.exit(0);
    }

    public void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("So you wonder, who did this fantastic app?");
        alert.setContentText("Norbert Wójtowicz" + '\n' + "Version: 1.0.1.2513161341");
        alert.showAndWait();
    }

    public void openSave(){
        todoList.remove(0,todoList.size());
        inProgressTasks.remove(0,inProgressTasks.size());
        doneTasks.remove(0,doneTasks.size());

        Stage stage = (Stage) this.ToDoList.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bin file(*.bin)", "*.bin"));
        fileChooser.setInitialFileName("*.bin");
        String currentDir = FileSystemView.getFileSystemView().getDefaultDirectory().toString();
        File file = new File(currentDir);
        fileChooser.setInitialDirectory(file);


        File selected = fileChooser.showOpenDialog(stage);
        try{
            if(selected==null){
                throw (new IllegalArgumentException());
            }
        }catch (IllegalArgumentException e){
            System.out.println("File wasn't choosed");
            return;
        }


        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(selected))) {


            for(int i=0;i<todoList.size();i++){
                todoList.remove(i);
            }
            for(int i=0;i<inProgressTasks.size();i++){
                inProgressTasks.remove(i);
            }

            for(int i=0;i<doneTasks.size();i++){
                doneTasks.remove(i);
            }


            ToDoList.refresh();
            inProgressList.refresh();
            doneList.refresh();

           ArrayList<Task> tempToDo = (ArrayList<Task>) inputStream.readObject();
           ArrayList<Task> tempInProgress =(ArrayList<Task>) inputStream.readObject();
           ArrayList<Task> tempDone = (ArrayList<Task>) inputStream.readObject();

           if(tempToDo.size()>0) {
               for (int i = 0; i < tempToDo.size(); i++) {
                   todoList.add(tempToDo.get(i));
               }
           }
           if(tempInProgress.size()>0) {
               for (int i = 0; i < tempInProgress.size(); i++) {
                   inProgressTasks.add(tempInProgress.get(i));
               }
           }

           if(tempDone.size()>0){
            for(int i=0;i<tempDone.size();i++){
                doneTasks.add(tempDone.get(i));
            }}

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void newSave(){
        Stage stage = (Stage) this.ToDoList.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bin file(*.bin)", "*.bin"));
        fileChooser.setInitialFileName("*.bin");
        String currentDir = FileSystemView.getFileSystemView().getDefaultDirectory().toString();

        File file = new File(currentDir);
        fileChooser.setInitialDirectory(file);
        fileChooser.setInitialFileName("SaveFile");

        try{
            if(fileChooser.getInitialFileName()==""){
                throw new IllegalArgumentException();
            }
        }
        catch(IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Something went wrong");
            alert.setContentText("You haven't typed file name");

            alert.showAndWait();
            return;
        }

            File selected = fileChooser.showSaveDialog(stage);
            try{
            if(selected==null){
                throw (new IllegalArgumentException());
            }
        }catch (IllegalArgumentException e){
            System.out.println("File wasn't created");
            return;
        }


        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(selected))) {
            outputStream.writeObject(new ArrayList<Task>(todoList));
            outputStream.writeObject(new ArrayList <Task>(inProgressTasks));
            outputStream.writeObject(new ArrayList<Task>(doneTasks));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportFile() throws IOException {

        Stage stage = (Stage) this.ToDoList.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export");
        FileChooser.ExtensionFilter csv = new FileChooser.ExtensionFilter("CSV file(*.csv)", "*.csv");
        FileChooser.ExtensionFilter json = new FileChooser.ExtensionFilter("JSON file(*.json)", "*.json");

        fileChooser.getExtensionFilters().add(csv);
        fileChooser.getExtensionFilters().add(json);

        fileChooser.setInitialFileName("KabnTableExported");
        String currentDir = FileSystemView.getFileSystemView().getDefaultDirectory().toString();
        File file = new File(currentDir);
        fileChooser.setInitialDirectory(file);


        ArrayList<Task> tempToDo = new ArrayList<Task>();
        ArrayList<Task> tempInProgress =new ArrayList<Task>();
        ArrayList<Task> tempDone =  new ArrayList<Task>();

        for(int i=0;i<todoList.size();i++){
            tempToDo.add(todoList.get(i));
        }

        for(int i=0;i<inProgressTasks.size();i++){
            tempInProgress.add(inProgressTasks.get(i));
        }

        for(int i=0;i<doneTasks.size();i++){
            tempDone.add(doneTasks.get(i));
        }


        File selected = fileChooser.showSaveDialog(stage);
        try{
            if(selected==null){
                throw (new IllegalArgumentException());
            }
        }catch (IllegalArgumentException e){
            System.out.println("File wasn't created");
            return;
        }


            FileWriter saveWriter = new FileWriter(selected);

        if(fileChooser.getSelectedExtensionFilter()==csv) {

            for (int i = 0; i < todoList.size(); i++) {
                String temp = tempToDo.get(i).toCsv();
                saveWriter.write("To do,");
                saveWriter.write(temp);
                saveWriter.write("\r\n");
            }

            for (int i = 0; i < inProgressTasks.size(); i++) {
                String temp = tempInProgress.get(i).toCsv();
                saveWriter.write("In progress,");
                saveWriter.write(temp);
                saveWriter.write("\r\n");
            }

            for (int i = 0; i < doneTasks.size(); i++) {
                String temp = tempDone.get(i).toCsv();
                saveWriter.write("Done,");
                saveWriter.write(temp);
                saveWriter.write("\r\n");
            }

        }
            else if(fileChooser.getSelectedExtensionFilter()==json) {
             HashMap<String, ArrayList<Task>> tempMap = new HashMap<>();

             tempMap.put("To do",tempToDo);
            tempMap.put("In progress",tempInProgress);
            tempMap.put("Done",tempDone);


            ObjectMapper obj = new ObjectMapper();
            saveWriter.write(obj.writeValueAsString(tempMap));


        }
            saveWriter.flush();
            saveWriter.close();
        }


    public void importFile(){

        todoList.remove(0,todoList.size());
        inProgressTasks.remove(0,inProgressTasks.size());
        doneTasks.remove(0,doneTasks.size());

        Stage stage = (Stage) this.ToDoList.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import file");
        FileChooser.ExtensionFilter csv = new FileChooser.ExtensionFilter("CSV file(*.csv)", "*.csv");
        FileChooser.ExtensionFilter json = new FileChooser.ExtensionFilter("JSON file(*.json)", "*.json");

        fileChooser.getExtensionFilters().add(csv);
        fileChooser.getExtensionFilters().add(json);

        fileChooser.setInitialFileName("");
        String currentDir = FileSystemView.getFileSystemView().getDefaultDirectory().toString();
        File file = new File(currentDir);
        fileChooser.setInitialDirectory(file);


        File selected = fileChooser.showOpenDialog(stage);
        try{
            if(selected==null){
                throw (new IllegalArgumentException());
            }
        }catch (IllegalArgumentException e){
            System.out.println("File wasn't choosed");
            return;
        }


        if(fileChooser.getSelectedExtensionFilter()==json){
            try(FileInputStream fis = new FileInputStream((selected))){
                ObjectMapper obj = new ObjectMapper();

                HashMap<String, ArrayList<Task>> tempMap = obj.readValue(selected, new TypeReference<HashMap<String, ArrayList<Task>>>(){});

                ArrayList<Task> toDoArr = tempMap.get("To do");
                ArrayList<Task> inProgArr = tempMap.get("In progress");
                ArrayList<Task> doneArr = tempMap.get("Done");

                for(int i=0;i<toDoArr.size();i++){
                    todoList.add(toDoArr.get(i));
                }
                for(int i=0;i<inProgArr.size();i++){
                    inProgressTasks.add(inProgArr.get(i));
                }
                for(int i=0;i<doneArr.size();i++){
                    doneTasks.add(doneArr.get(i));
                }


            }
            catch(IOException e){
                e.printStackTrace();
            }

        }

       else  if(fileChooser.getSelectedExtensionFilter()==csv){
            try {
                Scanner scanner = new Scanner(selected);
                scanner.useDelimiter("\r\n");


                while(scanner.hasNext()){
                    String temp = scanner.next();
                    String[] token = temp.split(",");
                    if(token[0].equals("To do")){

                    todoList.add(new Task(token[1],LevelOfPriority.valueOf(token[3]), LocalDate.parse(token[2]),token[4]));
                    }
                    else if(token[0].equals("In progress")){

                        inProgressTasks.add(new Task(token[1],LevelOfPriority.valueOf(token[3]), LocalDate.parse(token[2]),token[4]));
                    }
                    else if(token[0].equals("Done")){
                        doneTasks.add(new Task(token[1],LevelOfPriority.valueOf(token[3]), LocalDate.parse(token[2]),token[4]));
                    }

                }

            }
            catch(IOException e){
                e.printStackTrace();
            }
        }

    }




    public void addTask() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("view/AddTask.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Task");
            stage.show();

        } catch (Exception exc) {
            System.out.println("Exception occured");
        }
    }

    public void editToDo() {
        try {


            Parent root = FXMLLoader.load(getClass().getResource("view/Edit.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit");
            stage.show();


        } catch (Exception exc) {
            System.out.println("Exception occured");
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        ContextMenu tasksContext = new ContextMenu();
        MenuItem addTo = new MenuItem();
        addTo.textProperty().bind(Bindings.format("Add To In Progress"));

        MenuItem makeDone = new MenuItem();
        makeDone.textProperty().bind(Bindings.format("Add to done"));

        MenuItem deleteTask = new MenuItem();
        deleteTask.textProperty().bind(Bindings.format("Delete task"));

        MenuItem show = new MenuItem();
        show.textProperty().bind(Bindings.format("Show addnotation"));

        MenuItem editingToDo = new MenuItem();
        editingToDo.textProperty().bind(Bindings.format("Edit"));

        editingToDo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ifInProgress = false;
                editingIndex = ToDoList.getSelectionModel().getSelectedIndex();
                editToDo();
                ToDoList.setItems(todoList);
                ToDoList.refresh();
            }
        });

        show.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int buff = ToDoList.getSelectionModel().getSelectedIndex();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Addnotation");
                alert.setHeaderText("Let see what you have to do");
                alert.setContentText(todoList.get(buff).toAlert());


                alert.showAndWait();
            }
        });

        MenuItem showInProgress = new MenuItem();
        showInProgress.textProperty().bind(Bindings.format("Show addnotation"));

        showInProgress.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int buff = inProgressList.getSelectionModel().getSelectedIndex();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Addnotation");
                alert.setHeaderText("Let see what you have to do");
                alert.setContentText(inProgressTasks.get(buff).toAlert());

                alert.showAndWait();
            }
        });


        MenuItem showInDone = new MenuItem();
        showInDone.textProperty().bind(Bindings.format("Show addnotation"));

        showInDone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int buff = doneList.getSelectionModel().getSelectedIndex();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Addnotation");
                alert.setHeaderText("Let see what you have to do");
                alert.setContentText(doneTasks.get(buff).toAlert());

                alert.showAndWait();
            }
        });


        addTo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int buff1 = ToDoList.getSelectionModel().getSelectedIndex();
                Task temp = todoList.get(buff1);
                inProgressTasks.add(temp);
                todoList.remove(buff1);
                ToDoList.refresh();

            }

        });


        makeDone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int buff2 = ToDoList.getSelectionModel().getSelectedIndex();
                doneTasks.add(todoList.get(buff2));
                todoList.remove(buff2);
                ToDoList.refresh();
            }
        });

        deleteTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int buff3 = ToDoList.getSelectionModel().getSelectedIndex();
                todoList.remove(buff3);
                ToDoList.refresh();
            }
        });



        tasksContext.getItems().addAll(addTo, makeDone, deleteTask, show, editingToDo);


        MenuItem editInProgress = new MenuItem();
        editInProgress.textProperty().bind(Bindings.format("Edit"));


        editInProgress.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ifInProgress = true;
                editingIndex = inProgressList.getSelectionModel().getSelectedIndex();
                editToDo();
                inProgressList.setItems(inProgressTasks);
                inProgressList.refresh();
            }
        });

        ContextMenu inProgressContext = new ContextMenu();
        MenuItem addToDone = new MenuItem();
        addToDone.textProperty().bind(Bindings.format("Add to done"));
        addToDone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int buff4 = inProgressList.getSelectionModel().getSelectedIndex();
                Task temp = inProgressTasks.get(buff4);
                doneTasks.add(temp);
                inProgressTasks.remove(temp);
                inProgressList.refresh();
            }
        });

        MenuItem deleteTaskFromInProgress = new MenuItem();
        deleteTaskFromInProgress.textProperty().bind(Bindings.format("Delete"));


        deleteTaskFromInProgress.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int buff5 = inProgressList.getSelectionModel().getSelectedIndex();
                Task temp = inProgressTasks.get(buff5);
                inProgressTasks.remove(temp);
                inProgressList.refresh();
            }
        });

        inProgressContext.getItems().addAll(addToDone, deleteTaskFromInProgress, showInProgress, editInProgress);


        ContextMenu doneContext = new ContextMenu();
        MenuItem deleteFromDone = new MenuItem();

        deleteFromDone.textProperty().bind(Bindings.format("Delete"));

        deleteFromDone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int buff6 = doneList.getSelectionModel().getSelectedIndex();
                Task temp = doneTasks.get(buff6);
                doneTasks.remove(temp);
                doneList.refresh();
            }
        });

        doneContext.getItems().addAll(deleteFromDone, showInDone);

        ToDoList.setContextMenu(tasksContext);

        inProgressList.setContextMenu(inProgressContext);
        doneList.setContextMenu(doneContext);

        ToDoList.setCellFactory(new ToDoCellFactory());
        inProgressList.setCellFactory(new ToDoCellFactory());
        doneList.setCellFactory(new ToDoCellFactory());

        ToDoList.setItems(todoList);
        inProgressList.setItems(inProgressTasks);
        doneList.setItems(doneTasks);
    }


}
