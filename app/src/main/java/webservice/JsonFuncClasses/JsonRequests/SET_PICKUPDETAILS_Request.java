package webservice.JsonFuncClasses.JsonRequests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import webservice.JsonFuncClasses.Pickup_dt;

/**
 * Created by ahlaam.kazi on 10/24/2017.
 */

public class SET_PICKUPDETAILS_Request implements Serializable {
    public List<Pickup_dt> PICKUPDT = null;

   public SET_PICKUPDETAILS_Request(){
       PICKUPDT = new ArrayList<>();
    }
    public void setPickupDt(Pickup_dt val){this.PICKUPDT.add(val);}
}
