package webservice.JsonFuncClasses;

public class CheckValidReferenceWaybill {
    private CheckValidReferenceWaybill.WayBill_Info d;

    public class WayBill_Info{
        public Object __type = null;
        public String AWBIdentifier = null;
        public String Address = null;
        public String Amount = null;
        public String Area = null;
        public String Attempt = null;
        public String CardType = null;
        public String CivilId = null;
        public String Company = null;
        public String ConsignName = null;
        public String DelDate = null;
        public String DelTime = null;
        public String ErrMsg = null;
        public String Last_Status = null;
        public String PhoneNo = null;
        public String RouteName = null;
        public String Serial = null;
        public String ShipperName = null;
        public String WayBill = null;
    }


    public void setd(CheckValidReferenceWaybill.WayBill_Info d) {
        this.d = d;
    }

    public CheckValidReferenceWaybill.WayBill_Info getd() {
        return this.d;
    }
}
