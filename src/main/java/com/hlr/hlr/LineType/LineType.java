package com.hlr.hlr.LineType;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LineType {
    @Id
    private int id;
    private String lineType;

    public LineType() {
    }

    public LineType(int id) {
        this.id = id;
        if(id==1){
            this.lineType= "Prepaid";
        }
        else if(id==2){
            this.lineType = "Postpaid";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }
}
