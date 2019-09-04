package webservice.JsonFuncClasses.JsonRequests;

import java.io.Serializable;

/**
 * Created by ahlaam.kazi on 11/5/2017.
 */

public class SET_ODO_FUEL_IMAGE_Request implements Serializable {

    public String ID;
    public String IMAGE1;
    public String TYPE;
    public String IMAGE2;
    public String ODOTYPE;


    public void SET_ODO_FUEL_IMAGE_Request (){
        ID= "";
        IMAGE1= "";
        TYPE= "";
        IMAGE2= "";
        ODOTYPE= "";

    }

    public void setID(String ID) {
        this.ID = ID;
    }
    public void setIMAGE1(String IMAGE1) {
        this.IMAGE1 = IMAGE1;
    }
    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }
    public void setIMAGE2(String IMAGE2) {
        this.IMAGE2 = IMAGE2;
    }
    public void setODOTYPE(String ODOTYPE) {
        this.ODOTYPE = ODOTYPE;
    }


}
