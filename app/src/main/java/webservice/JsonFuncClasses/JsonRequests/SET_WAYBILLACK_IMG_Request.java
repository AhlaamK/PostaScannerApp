package webservice.JsonFuncClasses.JsonRequests;

import java.io.Serializable;

/**
 * Created by ahlaam.kazi on 10/24/2017.
 */

public class SET_WAYBILLACK_IMG_Request implements Serializable{

    public String WAYBILL;
    public String RSTNO;
    public String EVNTID;
    public String IMG;
    public String DRIVERCODE;
    public String IMGTYPE;
    
    public void SET_WAYBILLACK_IMG_Request (){
        WAYBILL= "";
        RSTNO= "";
        EVNTID= "";
        IMG= "";
        DRIVERCODE= "";
        IMGTYPE= "";
    }

    public void setWAYBILL(String WAYBILL) {
        this.WAYBILL = WAYBILL;
    }
    public void setRSTNO(String RSTNO) {
        this.RSTNO = RSTNO;
    }
    public void setEVNTID(String EVNTID) {
        this.EVNTID = EVNTID;
    }
    public void setIMG(String IMG) {
        this.IMG = IMG;
    }
    public void setDRIVERCODE(String DRIVERCODE) {
        this.DRIVERCODE = DRIVERCODE;
    }
    public void setIMGTYPE(String IMGTYPE) {
        this.IMGTYPE = IMGTYPE;
    }

}
