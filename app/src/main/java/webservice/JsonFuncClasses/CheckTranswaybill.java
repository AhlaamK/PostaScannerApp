package webservice.JsonFuncClasses;

/**
 * Created by ahlaam.kazi on 10/15/2017.
 */

public class CheckTranswaybill {
    private WayBill_Info d;

    public class WayBill_Info{
        public Object __type = null;
        public String WayBill = null;
        public String RouteName = null;
        public String ConsignName = null;
        public String PhoneNo = null;
        public String Area = null;
        public String Company = null;
        public String CivilId = null;
        public String Serial = null;
        public String CardType = null;
        public String DelDate = null;
        public String DelTime = null;
        public String Amount = null;
        public String ErrMsg = null;
        public String Attempt = null;
        public String Address = null;
        public String ShipperName = null;
        public String AWBIdentifier = null;
        public String Last_Status = null;
    }

    public void setd(WayBill_Info d) {
        this.d = d;
    }

    public WayBill_Info getd() {
        return this.d;
    }

}
