package sample;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class Task implements Serializable {
    private String taskname;
    private LevelOfPriority level;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate expiringdate;
    private String addnotation;

    public Task(){
    }


    public Task(String taskname, LevelOfPriority level, LocalDate expiringdate, String addnotation){
        this.taskname=taskname;
        this.level=level;
        this.expiringdate=expiringdate;
        this.addnotation=addnotation;
    }
    //Konstruktor hejka

    public String getTaskname() {
        return taskname;
    }
    public LevelOfPriority getLevel(){
        return level;
    }
    public LocalDate getExpiringdate(){
        return expiringdate;
    }


    public void Print(){
        System.out.println(this.taskname);
        System.out.println(this.level);
        System.out.println(this.expiringdate);
    }
    public String toString(){
        return this.taskname+" "+this.getExpiringdate();
    }

    public String toAlert(){
        return this.taskname+'\n'+this.getExpiringdate()+'\n'+this.getLevel()+'\n'+this.getAddnotation();
    }
    public String getAddnotation(){
        return this.addnotation;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public void setLevel(LevelOfPriority level) {
        this.level = level;
    }

    public void setExpiringdate(LocalDate expiringdate) {
        this.expiringdate = expiringdate;
    }

    public void setAddnotation(String addnotation) {
        this.addnotation = addnotation;
    }

    public String toCsv(){
        String tempCsv = this.taskname+","+this.expiringdate+","+this.level+","+this.addnotation;
        return tempCsv;
    }


    public String jakcsonSting(){
        ObjectMapper obj = new ObjectMapper();
        try {
            String jsonStr = obj.writeValueAsString(this);
            System.out.println(jsonStr);
            return jsonStr;
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
