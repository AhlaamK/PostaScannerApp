package webservice;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import webservice.FuncClasses.CheckHoldvalidwaybill;
import webservice.FuncClasses.CheckTranswaybill;
import webservice.FuncClasses.CheckValidPickupWaybill;
import webservice.FuncClasses.CheckValidReferenceWaybill;
import webservice.FuncClasses.CheckValidWaybill;
import webservice.FuncClasses.Couriers;
import webservice.FuncClasses.Events;
import webservice.FuncClasses.HoldWayBills;
import webservice.FuncClasses.OpenRst;
import webservice.FuncClasses.PayType;
import webservice.FuncClasses.PickUp;
import webservice.FuncClasses.PickUpDt;
import webservice.FuncClasses.PickUpWaybillsDT;
import webservice.FuncClasses.PickupHoldwaybills;
import webservice.FuncClasses.Remarks;
import webservice.FuncClasses.Routes;
import webservice.FuncClasses.RstDetail;
import webservice.FuncClasses.ScanWaybillDt;
import webservice.FuncClasses.Service;
import webservice.JsonFuncClasses.JsonRequests.SET_ODO_FUEL_IMAGE_Request;
import webservice.JsonFuncClasses.JsonRequests.SET_WAYBILLACK_IMG_Request;
import webservice.JsonFuncClasses.JsonResponses.GET_HOLDWAYBILLS_PICKUP_Response;
import webservice.JsonFuncClasses.JsonResponses.GET_PICKUP_WAYBILLS_DTResponse;
import webservice.JsonFuncClasses.JsonResponses.GET_SCAN_WAYBILL_DTResponse;
import webservice.JsonFuncClasses.StringResponse;

import static com.postaplus.postascannerapp.ScreenActivity.url;

public class WebService {

    // CHECK FUNCTIONS
    static String url1 = "http://132.145.29.167:1000/OpsCourierScannerService/OpsGCScanSrv.svc/";

    public static String CHECK_WAYBILL_COD_STATUS(String Waybill){
        String JsonParam="";

        JsonParam +="GET_WAYBILL_COD_STATUS?WAYBILL="+Waybill;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("getwaycode", JsonParam);
        Log.e("getwaycodeRespObject", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("getwaycodeResponse", ResponseJson.getd().toString());
        return ResponseJson.getd();

    }

    public static boolean invokeLoginWS(String userName, String passWord, String serialID, String version) {

        Log.e("invokeLoginWS", "Enter to the Block");
        String JsonParam="";

        JsonParam +="USER_AUTHENTICATION"+"?"+"UNAME"+"="+userName+"&"+"PWD"+"="+passWord+"&"+"DEVICESRL"+"="+serialID+"&"+"VERSION"+"="+version;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("logreq", JsonParam);
        Log.e("LoginRespObject", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("LoginResponse", ResponseJson.getd().toString());

       return  Boolean.valueOf(ResponseJson.getd());
    }

    public static Events[] GET_EVENTS() {

        String JsonParameters = "GET_EVENTS?DRIVERCODE=null";

        List<webservice.JsonFuncClasses.Events> ResponseData = JsonService.JsonListEvents(JsonParameters);
        System.out.println("ResponseData is geteve" + ResponseData);
        if (ResponseData != null) {
            Events[] ArrayEvents = new Events[ResponseData.size()];
       /* for(int i=0;i<ResponseData.size();i++){
            ArrayEvents[i] = new webservice.FuncClasses.Events();
        }*/
            System.out.println("ResponseData is size in web" + ResponseData.size());
            int i = 0;
            for (webservice.JsonFuncClasses.Events rec : ResponseData
                    ) {
                Log.e("ResponseEntityData ", rec.EVENTID + "-" + rec.EVENTDESC);
                ArrayEvents[i] = new Events();
                ArrayEvents[i].EVENTCODE = rec.EVENTID;
                ArrayEvents[i].EVENTDESC = rec.EVENTDESC;
                i += 1;
            }

//        Log.e("json inn web", JsonService.JsonListEvents(JsonParameters).toString());
//        Log.e("GET_EVENTSRespObject", ResponseData.toString());
            //webservice.JsonFuncClasses.Events ResponseJson = (webservice.JsonFuncClasses.Events) ResponseData;

            //Log.e("LoginResponse", ResponseJson.getd().toString());
            return ArrayEvents;
        }else return  null;
    }

    public static PickUp[] GET_PICKUP(String drivercode) {
        String JsonParam="";

        JsonParam +="GET_PICKUP?DRIVERCODE="+drivercode;
        List<webservice.JsonFuncClasses.PickUp> ResponseData = JsonService.JsonListPickup(JsonParam);
        System.out.println("ResponseData is gepickup:"+ResponseData.size());

        PickUp[] Arraypickup = new PickUp[ResponseData.size()];
        int i = 0;
        for (webservice.JsonFuncClasses.PickUp rec:ResponseData
                ) {

            Arraypickup[i] = new PickUp();
            Arraypickup[i].ACC_NAME = rec.ACC_NAME;
            Arraypickup[i].CONSIGNEE_NAME = rec.CONSIGNEE_NAME;
            Arraypickup[i].CONTACT_PERSON = rec.CONTACT_PERSON;
            Arraypickup[i].DEL_ADD = rec.DEL_ADD;
            Arraypickup[i].DEL_CITY = rec.DEL_CITY;
            Arraypickup[i].DEL_PHONE= rec.DEL_PHONE;
            Arraypickup[i].ERR = rec.ERR;
            Arraypickup[i].IDENTIFIER = rec.IDENTIFIER;
            Arraypickup[i].PICK_ADD = rec.PICK_ADD;
            Arraypickup[i].PICK_AREA = rec.PICK_AREA;
            Arraypickup[i].PICK_NO=rec.PICK_NO;
            Arraypickup[i].PICK_PHONE = rec.PICK_PHONE;
            Arraypickup[i].PICK_TIME = rec.PICK_TIME;
            Arraypickup[i].SerialNo = rec.SerialNo;


            i+=1;

        }
        Log.e("Datapicup:/suc ", String.valueOf(Arraypickup));
        System.out.println("Arraypickup size:"+Arraypickup.length);
        return Arraypickup;


    }

    public static String GET_COURIERROUTE(String drivercode) {
        String JsonParam="";

        JsonParam +="GET_COURIERROUTE?DRIVERCODE="+drivercode;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("getcrroute", JsonParam);
        Log.e("getcrrouteRespObject", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("getcrrouteResponse", ResponseJson.getd().toString());
        return ResponseJson.getd();
    }

    public static Couriers[] GET_COURIERS(String drivercode) {
        String JsonParam="";

        JsonParam +="GET_COURIERS?DRIVERCODE="+drivercode;
        List<webservice.JsonFuncClasses.Couriers> ResponseData = JsonService.JsonListCourier(JsonParam);
        System.out.println("ResponseData is getcourir:"+ResponseData);

        Couriers[] ArrayCourier = new Couriers[ResponseData.size()];
        int i = 0;
        for (webservice.JsonFuncClasses.Couriers rec:ResponseData
                ) {

            ArrayCourier[i] = new Couriers();
            ArrayCourier[i].Driver_Code = rec.Driver_Code;
            ArrayCourier[i].Driver_Name = rec.Driver_Name;
            i+=1;
           // Log.e("DataCourier:/suc ", rec.Driver_Code + "-" + rec.Driver_Name);
        }
        System.out.println("ResponseData is courierize"+ResponseData.size());
        return ArrayCourier;

    }

    public static HoldWayBills[] GET_HOLDWAYBILLS(String drivercode) {
        String JsonParam="";

        JsonParam +="GET_HOLDWAYBILLS?DRIVERCODE="+drivercode;
        List<webservice.JsonFuncClasses.HoldWayBills> ResponseData = JsonService.JsonListHoldwaybil(JsonParam);
       System.out.println("ResponseData is gethold:"+ResponseData);
        if(ResponseData!= null) {
            HoldWayBills[] Arrayholdwaybl = new HoldWayBills[ResponseData.size()];
            int i = 0;
          /* for(int i=0;i<ResponseData.size();i++){
            ArrayEvents[i] = new webservice.FuncClasses.Events();
        }*/

            for (webservice.JsonFuncClasses.HoldWayBills rec : ResponseData
                    ) {

                Arrayholdwaybl[i] = new HoldWayBills();
                Arrayholdwaybl[i].AWBIdentifier = rec.AWBIdentifier;
                Arrayholdwaybl[i].Address = rec.Address;
                Arrayholdwaybl[i].Amount = rec.Amount;
                Arrayholdwaybl[i].Area = rec.Area;
                Arrayholdwaybl[i].CardType = rec.CardType;
                Arrayholdwaybl[i].CivilId = rec.CivilId;
                Arrayholdwaybl[i].Company = rec.Company;
                Arrayholdwaybl[i].Consignee = rec.Consignee;
                Arrayholdwaybl[i].DelDate = rec.DelDate;
                Arrayholdwaybl[i].DelTime = rec.DelTime;
                Arrayholdwaybl[i].Phno = rec.Phno;
                Arrayholdwaybl[i].Serial = rec.Serial;
                Arrayholdwaybl[i].ShipperName = rec.ShipperName;
                Arrayholdwaybl[i].TrnsDrvr = rec.TrnsDrvr;
                Arrayholdwaybl[i].Waybill = rec.Waybill;

                i += 1;

            }

            Log.e("Datarstdet:/suc ", String.valueOf(Arrayholdwaybl));
            System.out.println("Arrayholdwaybl size:" + Arrayholdwaybl.length);
            return Arrayholdwaybl;
        }else return null;
    }

    public static String GET_ODOREADING(String drivercode) {
        String JsonParam="";

        JsonParam +="GET_ODOREADING?DRIVERCODE="+drivercode;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        if(ResponseData==null){
            return null;
        }else
        Log.e("ODOREAD", JsonParam);
        Log.e("ODOREADRespObject", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("ODOREADResponse", ResponseJson.getd().toString());
        return ResponseJson.getd();

    }

    public static OpenRst GET_OPENRST(String drivercode) {
        String JsonParams="";

        JsonParams +="GET_OPENRST?DRIVERCODE="+drivercode;



        Object ResponseData = JsonService.jsonreq(JsonParams, webservice.JsonFuncClasses.OpenRst.class);

        if(ResponseData==null){
            return null;
        }else
        Log.e("openrstres", JsonParams);
        Log.e("openrstRespObject", ResponseData.toString());
        webservice.JsonFuncClasses.OpenRst ResponseJson = (webservice.JsonFuncClasses.OpenRst)ResponseData;

        Log.e("openrstResponse", ResponseJson.getd().toString());

        OpenRst ResponseClass = new OpenRst();

        ResponseClass.ACKNO =ResponseJson.getd().ACKNO;
        ResponseClass.ERRMSG = ResponseJson.getd().ERRMSG;
        ResponseClass.RSTNO = ResponseJson.getd().RSTNO;
        ResponseClass.RTNO = ResponseJson.getd().RTNO;


        Log.e("openrstrsp/END", "Success");
        return ResponseClass;

    }

    public static PayType[] GET_PAYTYPE() {
        String JsonParameters = "GET_PAYTYPE";

        List<webservice.JsonFuncClasses.PayType> ResponseData = JsonService.JsonListPayTyp(JsonParameters);
        System.out.println("ResponseData is getpaytyp"+ResponseData);

        PayType[] ArrayPaytyp = new PayType[ResponseData.size()];
        int i = 0;
        System.out.println("ResponseData is paysize:"+ResponseData.size());
        for (webservice.JsonFuncClasses.PayType rec:ResponseData
                ) {

            ArrayPaytyp[i] = new PayType();
            ArrayPaytyp[i].PAYID = rec.PAYID;
            ArrayPaytyp[i].PAYTYPE = rec.PAYTYPE;
            i+=1;
          //  Log.e("Data pay:/suc ", rec.PAYID + "-" + rec.PAYTYPE);
        }
        System.out.println("ResponseData is paysize:"+ResponseData.size());
        return ArrayPaytyp;
    }

    public static Routes[] GET_ROUTES(String drivercode) {
        String JsonParam="";

        JsonParam +="GET_ROUTES?DRIVERCODE="+drivercode;
        List<webservice.JsonFuncClasses.Routes> ResponseData = JsonService.JsonListRoutes(JsonParam);
        System.out.println("ResponseData is getrouttes:"+ResponseData);

        Routes[] ArrayRoutes = new Routes[ResponseData.size()];
        int i = 0;
        for (webservice.JsonFuncClasses.Routes rec:ResponseData
                ) {

            ArrayRoutes[i] = new Routes();
            ArrayRoutes[i].RouteCode = rec.RouteCode;
            ArrayRoutes[i].RouteName = rec.RouteName;
            i+=1;
           // Log.e("Dataroute:/suc ", rec.RouteCode + "-" + rec.RouteName);
        }
       // System.out.println("ResponseData is routeize"+ResponseData.size());
        return ArrayRoutes;
    }

    public static RstDetail[] GET_RSTDETAIL(String drivercode) {
        String JsonParam="";

        JsonParam +="GET_RSTDETAIL?DRIVERCODE="+drivercode;
        List<webservice.JsonFuncClasses.RstDetail> ResponseData = JsonService.JsonListRstdetal(JsonParam);
        System.out.println("ResponseData is getrstdet:"+ResponseData.size());

        RstDetail[] ArrayRstdetal = new RstDetail[ResponseData.size()];
        int i = 0;
          /* for(int i=0;i<ResponseData.size();i++){
            ArrayEvents[i] = new webservice.FuncClasses.Events();
        }*/

        for (webservice.JsonFuncClasses.RstDetail rec:ResponseData
                ) {

            ArrayRstdetal[i] = new RstDetail();
            ArrayRstdetal[i].AWBIdentifier = rec.AWBIdentifier;
            ArrayRstdetal[i].Address = rec.Address;
            ArrayRstdetal[i].Amount = rec.Amount;
            ArrayRstdetal[i].Area = rec.Area;
            ArrayRstdetal[i].Attempt = rec.Attempt;
            ArrayRstdetal[i].CardType = rec.CardType;
            ArrayRstdetal[i].CivilId = rec.CivilId;
            ArrayRstdetal[i].Company = rec.Company;
            ArrayRstdetal[i].ConsignName = rec.ConsignName;
            ArrayRstdetal[i].DelDate = rec.DelDate;
            ArrayRstdetal[i].DelTime = rec.DelTime;
            ArrayRstdetal[i].ErrMsg = rec.ErrMsg;
            ArrayRstdetal[i].Last_Status = rec.Last_Status;
            ArrayRstdetal[i].PhoneNo = rec.PhoneNo;
            ArrayRstdetal[i].RouteName = rec.RouteName;
            ArrayRstdetal[i].Serial = rec.Serial;
            ArrayRstdetal[i].ShipperName = rec.ShipperName;
            ArrayRstdetal[i].WayBill = rec.WayBill;

            i+=1;

        }
       // Log.e("Datarstdet:/suc ", String.valueOf(ArrayRstdetal));

        return ArrayRstdetal;

    }

    public static Service[] GET_SERVICE() {
        String JsonParameters = "GET_SERVICE";

        List<webservice.JsonFuncClasses.Service> ResponseData = JsonService.JsonListServc(JsonParameters);
        System.out.println("ResponseData is getserv"+ResponseData.size());

        Service[] ArrayServ = new Service[ResponseData.size()];
        int i = 0;
        for (webservice.JsonFuncClasses.Service rec:ResponseData
                ) {

            ArrayServ[i] = new Service();
            ArrayServ[i].SERVICEID = rec.SERVICEID;
            ArrayServ[i].SERVICETYPE = rec.SERVICETYPE;
            i+=1;
           // Log.e("Data serv:/suc ", rec.SERVICEID + "-" + rec.SERVICETYPE);
        }
        System.out.println("ResponseData is servsize"+ResponseData.size());
        return ArrayServ;

    }

    public static String SET_ACCEPTHOLD(String drivercode, String datetime) {

        String XMLData = "<pos:SET_ACCEPTHOLD>\n" +
                "<!--Optional:-->\n" +
                "<pos:DRIVERCODE>" + drivercode + "</pos:DRIVERCODE>\n" +
                "<!--Optional:-->\n" +
                "<pos:DATETIME>" + datetime + "</pos:DATETIME>\n" +
                "</pos:SET_ACCEPTHOLD>\n";


        String ResultData = SoapService.soapResult(XMLData);

        Log.e("ResultData is", ResultData);

        String Results = XMLParser.acceptholdParser(ResultData);

        Log.e("GetEvents/END", "Success");
        return Results;
    }

    public static Boolean SET_AWB_EVENT(String waybill, String eventtype,String drivercode, String note, String datetime, String longitude, String latitude) {

        String JsonParam="";

        JsonParam +="SET_AWB_EVENT?WAYBILL="+waybill+"&EVENTTYPE="+eventtype+"&DRIVERCODE="+drivercode+"&NOTE="+note+"&DATETIME="+datetime+"&LATITUDE="+latitude+"&LONGITUDE="+longitude;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("SETSAWBEVreq", JsonParam);
        Log.e("SETSAWBEVERespObj", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("SETSAWBEVEResp/succ", ResponseJson.getd().toString());
      /*  if(ResponseJson.equals("true")) return true;
        else return false;*/
        return  Boolean.valueOf(ResponseJson.getd());


    }

    public static boolean SET_CANCELATION_HOLD(String drivercode1, String drivercode2) {
        String JsonParam="";

        JsonParam +="SET_CANCELATION_HOLD?DRIVERCODE1="+drivercode1+"&DRIVERCODE2="+drivercode2;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("stcnclhldreq", JsonParam);
        Log.e("stcnclhldRespObj", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("stcnclhldResp/succ", ResponseJson.getd().toString());
      /*  if(ResponseJson.equals("true")) return true;
        else return false;*/
        return  Boolean.valueOf(ResponseJson.getd());

    }

    public static String SET_COURIERROUTE(String drivercode, String route){
        String JsonParam="";

        JsonParam +="SET_COURIERROUTE?DRIVERCODE="+drivercode+"&ROUTE="+route;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("setcrrote", JsonParam);
        Log.e("setcrroteRespobjt", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("setCRResp/succ", ResponseJson.getd().toString());
        return ResponseJson.getd();

    }

    public static String SET_COURIERROUTECLOSE(String drivercode, String route){
        String JsonParam="";

        JsonParam +="SET_COURIERROUTECLOSE?DRIVERCODE="+drivercode+"&ROUTE="+route;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("setcrroteclse", JsonParam);
        Log.e("setcrroteclseRespobjt", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("setcrroteclseResp/succ", ResponseJson.getd().toString());
        return ResponseJson.getd();

    }

    public static String SET_DELETE_HOLD(String drivercode){

        String XMLData =  "<pos:SET_DELETE_HOLD>\n"+
                "<!--Optional:-->\n"+
                "<pos:DRIVERCODE>"+ drivercode +"</pos:DRIVERCODE>\n"+
                "</pos:SET_DELETE_HOLD>\n";


        String ResultData = SoapService.soapResult(XMLData);

        Log.e("ResultData is",ResultData);

        String Results = XMLParser.setdeleteholdParser(ResultData);

        Log.e("GetEvents/END", "Success");
        return Results;
    }

    public static boolean SET_DELETE_INITIAL_SCAN_SINGLE(String drivercode, String waybill){
        String JsonParam="";

        JsonParam +="SET_DELETE_INITIAL_SCAN_SINGLE?DRIVERCODE="+drivercode+"&WAYBILL="+waybill;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);


        if(ResponseData== null){
            return Boolean.valueOf(null);
        }else
        Log.e("stdeliniscanreq", JsonParam);
        Log.e("stdeliniscanRespObj", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("stdeliniscanResp", ResponseJson.getd().toString());
      /*  if(ResponseJson.equals("true")) return true;
        else return false;*/
        return  Boolean.valueOf(ResponseJson.getd());

    }

  /*  public static String SET_DELIVERY(String custname, String drivercode,String waybill, String eventtype, String latitude, String longitude,String datetime, String eventnote, String AWBIdentifier, String IsCODCollected, String barcodeIdentifier){
        Map<String,String> map = new HashMap<String,String>();
        //map.toSingleValueMap();
        map.put("CUSTNAME",custname);
        map.put("DRIVERCODE",drivercode);
        map.put("WAYBILL",waybill);
        map.put("EVENTTYPE",eventtype);
        map.put("LATITUDE",latitude);
        map.put("LONGITUDE",longitude);
        map.put("DATETIME",datetime);
        map.put("EVENT_NOTE",eventnote);
        map.put("IDENTIFIER",AWBIdentifier);
        map.put("CASH_COLLECTED",IsCODCollected);
        map.put("BYRST",barcodeIdentifier);


        ResponseEntity<?> ResponsePostReq = JsonService.jsonpostreq("SET_DELIVERY",map,StringResponse.class);
        System.out.println("ResponsePostReq delis:"+ResponsePostReq);
        StringResponse ActResp = (StringResponse) ResponsePostReq.getBody();

        return ActResp.getd();

    }*/

    public static String SET_DELIVERY(String custname, String drivercode,List waybill, String eventtype, String latitude, String longitude,String datetime, String eventnote, String AWBIdentifier, String IsCODCollected, String barcodeIdentifier){

        JSONObject jsonobj = new JSONObject();
        try {

            System.out.println("Waybill in webserv:"+waybill+"Cust:"+custname);
            if(custname== null){
                jsonobj.put("CUSTNAME",custname=null);
            }else
            {
                jsonobj.put("CUSTNAME",custname);
            }
            jsonobj.put("DRIVERCODE",drivercode);
            jsonobj.put("WAYBILL", new JSONArray(waybill));
            jsonobj.put("EVENTTYPE",eventtype);
            jsonobj.put("LATITUDE",latitude);
            jsonobj.put("LONGITUDE",longitude);
            jsonobj.put("DATETIME",datetime);
            jsonobj.put("EVENT_NOTE",eventnote);
            jsonobj.put("IDENTIFIER",AWBIdentifier);
            jsonobj.put("CASH_COLLECTED",IsCODCollected);
            jsonobj.put("BYRST",barcodeIdentifier);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // String setdel="{\"CUSTNAME\""+":"+custname+","+"\"DRIVERCODE\""+":"+drivercode+","+"\"WAYBILL\""+":"+waybill+","+"\"EVENTTYPE\""+":"+eventtype+","+"\"LATITUDE\""+":"+latitude+","+"\"LONGITUDE\""+":"+longitude+","+"\"DATETIME\""+":"+datetime+","+"\"EVENT_NOTE\""+":"+eventnote+","+"\"IDENTIFIER\""+":"+AWBIdentifier+","+"\"CASH_COLLECTED\""+":"+IsCODCollected+","+"\"BYRST\""+":"+barcodeIdentifier+"}";
        System.out.println("jsonobj"+jsonobj);
        JSONObject resp = null;
        String jsonsetdelvResp = null;
        // JSONObject obj = new JSONObject(setdel);
        resp = JsonService.jsonnewpostreq(url,"SET_DELIVERY",jsonobj);
        System.out.println("ResponsePostReq delis:"+resp);

        try {
            JSONObject json = new JSONObject(String.valueOf(resp));
            jsonsetdelvResp=json.getString("d");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonsetdelvResp!= null){
            Log.e("String jsoncon", jsonsetdelvResp);
        }

        return  jsonsetdelvResp;

    }


    public static boolean SET_DEVICE_STATUS(String drivercode, String deviceserial, String latitude,String longitude,String status,String area){

        String JsonParam="";

        JsonParam +="SET_DEVICE_STATUS?DRIVERCODE="+drivercode+"&DEVICESERIAL="+deviceserial+"&LATITUDE="+latitude+"&LONGITUDE="+longitude+"&STATUS="+status+"&AREA="+" ";

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        if(ResponseData==null){
            Boolean.valueOf(null);
        }else
        Log.e("setdevstts", JsonParam);
        Log.e("setdevsttsRespobjt", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("setdevsttsResp/succ", ResponseJson.getd().toString());
       /* if(ResponseJson.getd().equals("true")) return  true;
        else return  false;*/
        return Boolean.valueOf(ResponseJson.getd());

    }

    public static String SET_DRIVERMETER(String drivercode, String odored, String barcode ,String odotype){
        String JsonParam="";

        JsonParam +="SET_DRIVERMETER?DRIVERCODE="+drivercode+"&ODORED="+odored+"&BARCODE="+barcode+"&ODOTYPE="+odotype;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("setdrvmtr", JsonParam);
        Log.e("setdrvmtrRespobjt", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("setdrvmtrRESP/succ", ResponseJson.getd().toString());
        return ResponseJson.getd();

    }

    public static String SET_FUEL_TRACK(String datetimestr, String drivercode, String vehclbarcode ,String odoreading, String recptno,String amount,String longitude, String latitude){
        String JsonParam="";

        JsonParam +="SET_FUEL_TRACK?DATETIMESTR="+datetimestr+"&DRIVERCODE="+drivercode+"&VEH_BARCODE="+vehclbarcode+"&ODO_READING="+odoreading+"&RECPT_NO="+recptno+"&AMOUNT="+amount+"&LATITUDE="+latitude+"&LONGITUDE="+longitude;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("setfueltrck", JsonParam);
        Log.e("setfueltrckRespobjt", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("setfueltrckRESP/succ", ResponseJson.getd().toString());
        return ResponseJson.getd();

    }

    public static boolean SET_PICKUP_RECVD(String drivercode, String pickupno, String recvdtime ){
        String JsonParam="";

        JsonParam +="SET_PICKUP_RECVD?DRIVERCODE="+drivercode+"&PICKUPNO="+pickupno+"&RECVDTIME="+recvdtime;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("setpckrecv", JsonParam);
        Log.e("setpckrecvRespobjt", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("setpckrecvRESP/succ", ResponseJson.getd().toString());
      /*  if(ResponseJson.getd().equals("true")) return  true;
        else return false;*/
        return Boolean.valueOf(ResponseJson.getd());

    }

   /* public static String SET_PICKUPDETAILS(String drivercode, setPickUpDt PkpReqData[]){

        SET_PICKUPDETAILS_Request requestobj = new SET_PICKUPDETAILS_Request();


        for (setPickUpDt rec: PkpReqData
             ) {
            Log.e("Request Val WAB : ", rec.WAYBILL);
            //Log.e("Request Val DRV : ", rec.USERCODE + " " + rec.DRIVERCODE);
            //Log.e("Request Val MTS : ", rec.TAGVAL + " " + rec.TAGMTS);
            Pickup_dt dtvalues = new Pickup_dt();
            dtvalues.AMOUNT=rec.AMOUNT;
            dtvalues.DATETIMESTR=rec.DATETIMESTR;
            dtvalues.PAYTYPE=rec.PAYTYPE;
            dtvalues.PICKUPNO=rec.PICKUPNO;
            dtvalues.SERVICE=rec.SERVICE;
            dtvalues.STATUS=rec.STATUS;
            dtvalues.TAGMTS=rec.TAGVAL;
            dtvalues.USERCODE=drivercode;
            dtvalues.WAYBILL=rec.WAYBILL;
            requestobj.setPickupDt(dtvalues);
        }

        for (Pickup_dt rec:requestobj.PICKUPDT
                ) {
            Log.e("Request Object : ", rec.WAYBILL);
            Log.e("Request Object : ", rec.USERCODE);
        }

        ResponseEntity<?> response = JsonService.jsonpostArr("SET_PICKUPDETAILS",requestobj,StringResponse.class);
        StringResponse ResponsePostReq = (StringResponse) response.getBody();
        Log.e("Response Actual: ", ResponsePostReq.getd());
        return ResponsePostReq.getd();

    }*/

    public static String SET_SYNCH_CHANNEL(String drivercode){

        Map<String,String> map = new HashMap<String,String>();
        //map.toSingleValueMap();
        map.put("DRIVERCODE",drivercode);

        ResponseEntity<?> ResponsePostReq = JsonService.jsonpostreq("SET_SYNCH_CHANNEL",map,StringResponse.class);
        System.out.println("ResponsePostReq is:"+ResponsePostReq);
        StringResponse ActResp = (StringResponse) ResponsePostReq.getBody();

        return ActResp.getd();
    }

    public static String SET_TRANS_CONFIRM(String drivercode){
        String JsonParam="";

        JsonParam +="SET_TRANS_CONFIRM?DRIVERCODE="+drivercode;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("setransroute", JsonParam);
        Log.e("setransrouteRespObject", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("setransrouteResponse", ResponseJson.getd().toString());
        return ResponseJson.getd();

    }

    public static String SET_TRANS_HOLD(String drivercode1, String drivercode2 ,String waybill){
        String JsonParam="";

        JsonParam +="SET_TRANS_HOLD?DRIVERCODE1="+drivercode1+"&DRIVERCODE2="+drivercode2+"&WAYBILL="+waybill;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("setranshold", JsonParam);

        StringResponse ResponseJson = (StringResponse)ResponseData;
        if(ResponseData!=null) {
            Log.e("setransholdRespObject", ResponseData.toString());
            Log.e("setransholdResponse", ResponseJson.getd().toString());
            return ResponseJson.getd();
        }else {
            return null;

        }

    }

    public static String SET_TRANS_PICKUP(String drivercode1, String drivercode2 ,String pickupno){
        String JsonParam="";

        JsonParam +="SET_TRANS_PICKUP?DRIVERCODE1="+drivercode1+"&DRIVERCODE2="+drivercode2+"&PICKUPNO="+pickupno;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("setranspckp", JsonParam);
        Log.e("setranspckpRespObject", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("setranspckpResponse", ResponseJson.getd().toString());
        return ResponseJson.getd();

    }

    public static String SET_TRANS_PICKUP_CONFIRM( String drivercode2 ,String pickupno){
        String JsonParam="";

        JsonParam +="SET_TRANS_PICKUP_CONFIRM?DRIVERCODE2="+drivercode2+"&PICKUPNO="+pickupno;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("setrspckpcnfrm", JsonParam);
        Log.e("setrspckpcnfrmRespobjt", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("setrspckpcnfrmResponse", ResponseJson.getd().toString());
        return ResponseJson.getd();

    }

    public static String SET_TRANS_WC( String drivercode1 ,String drivercode2 ,String waybill){
        // made changes added ' on 12aug17 (d2 chnged to d1 in firrst element)

        String JsonParam="";

       // JsonParam +="SET_TRANS_WC?DRIVERCODE1="+drivercode2+"&DRIVERCODE2="+drivercode2+"&WAYBILL="+waybill;
        JsonParam +="SET_TRANS_WC?DRIVERCODE1="+drivercode1+"&DRIVERCODE2="+drivercode2+"&WAYBILL="+waybill;
        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        // Log.e("setrswcRespobjt", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;
        if(ResponseData!=null) {
            Log.e("setrswcResponse", ResponseJson.getd().toString());
            return ResponseJson.getd();
        }else {
            return null;

        }


    }

    public static PickUpDt[] GET_PICKUP_DT(String drivercode){
        String JsonParam="";

        JsonParam +="GET_PICKUP_DT?DRIVERCODE="+drivercode;
        List<webservice.JsonFuncClasses.PickUpDt> ResponseData = JsonService.JsonListPckpDt(JsonParam);
        System.out.println("ResponseData is getrstdet:"+ResponseData.size());

        PickUpDt[] ArrayPickupdt = new PickUpDt[ResponseData.size()];
        int i = 0;
          /* for(int i=0;i<ResponseData.size();i++){
            ArrayEvents[i] = new webservice.FuncClasses.Events();
        }*/

        for (webservice.JsonFuncClasses.PickUpDt rec:ResponseData
                ) {

            ArrayPickupdt[i] = new PickUpDt();
            ArrayPickupdt[i].ACC_NAME = rec.ACC_NAME;
            ArrayPickupdt[i].CONSIGNEE_NAME = rec.CONSIGNEE_NAME;
            ArrayPickupdt[i].CONTACT_PERSON = rec.CONTACT_PERSON;
            ArrayPickupdt[i].DEL_ADD = rec.DEL_ADD;
            ArrayPickupdt[i].DEL_CITY = rec.DEL_CITY;
            ArrayPickupdt[i].DEL_PHONE = rec.DEL_PHONE;
            ArrayPickupdt[i].ERR = rec.ERR;
            ArrayPickupdt[i].IDENTIFIER = rec.IDENTIFIER;
            ArrayPickupdt[i].PICK_ADD = rec.PICK_ADD;
            ArrayPickupdt[i].PICK_AREA = rec.PICK_AREA;
            ArrayPickupdt[i].PICK_NO = rec.PICK_NO;
            ArrayPickupdt[i].PICK_PHONE = rec.PICK_PHONE;
            ArrayPickupdt[i].PICK_TIME = rec.PICK_TIME;
            ArrayPickupdt[i].SerialNo = rec.SerialNo;


            i+=1;

        }
        Log.e("Datapckpdt:/suc ", String.valueOf(ArrayPickupdt));

        return ArrayPickupdt;

    }

    public static CheckHoldvalidwaybill CHECK_HOLDVALIDWAYBILL(String drivercode, String waybill, String routeid, String transdrvr ){
        String JsonParams="";

        JsonParams +="CHECK_HOLDVALIDWAYBILL?DRIVERCODE="+drivercode+"&WAYBILL="+waybill+"&ROUTEID="+routeid+"&TRANSDRVR="+transdrvr;

        Object ResponseData = JsonService.jsonreq(JsonParams, webservice.JsonFuncClasses.CheckHoldvalidwaybill.class);

        Log.e("holdwaybillres", JsonParams);
        Log.e("holdwaybilRespObject", ResponseData.toString());
        webservice.JsonFuncClasses.CheckHoldvalidwaybill ResponseJson = (webservice.JsonFuncClasses.CheckHoldvalidwaybill)ResponseData;

        Log.e("holdwaybillResponse", ResponseJson.getd().toString());

        CheckHoldvalidwaybill ResponseClass = new CheckHoldvalidwaybill();

        ResponseClass.WayBill = ResponseJson.getd().WayBill;
        ResponseClass.RouteName = ResponseJson.getd().RouteName;
        ResponseClass.ConsignName = ResponseJson.getd().ConsignName;
        ResponseClass.PhoneNo = ResponseJson.getd().PhoneNo;
        ResponseClass.Area = ResponseJson.getd().Area;
        ResponseClass.Company = ResponseJson.getd().Company;
        ResponseClass.CivilId = ResponseJson.getd().CivilId;
        ResponseClass.Serial = ResponseJson.getd().Serial;
        ResponseClass.CardType = ResponseJson.getd().CardType;
        ResponseClass.DelDate = ResponseJson.getd().DelDate;
        ResponseClass.DelTime = ResponseJson.getd().DelTime;
        ResponseClass.Amount = ResponseJson.getd().Amount;
        ResponseClass.ErrMsg = ResponseJson.getd().ErrMsg;
        ResponseClass.Attempt = ResponseJson.getd().Attempt;
        ResponseClass.Address = ResponseJson.getd().Address;
        ResponseClass.ShipperName = ResponseJson.getd().ShipperName;
        ResponseClass.AWBIdentifier = ResponseJson.getd().AWBIdentifier;
        ResponseClass.Last_Status=ResponseJson.getd().Last_Status;

        Log.e("Gethldwabres/END", "Success");
        return ResponseClass;
    }

    public static String CHECK_VHCLBARCODE( String drivercode, String barcode){
        String JsonParam="";

        JsonParam +="CHECK_VHCLBARCODE?DRIVERCODE="+drivercode+"&BARCODE="+barcode;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("vehclbar", JsonParam);
        Log.e("vehclbarRespObject", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("VehclbarResponse", ResponseJson.getd().toString());
        return ResponseJson.getd();
    }

    public static Boolean CLEAR_SYNCH_CHANNEL( String drivercode){
        String JsonParam="";

        JsonParam +="CLEAR_SYNCH_CHANNEL?DRIVERCODE="+drivercode;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("clrsyncreq", JsonParam);
        Log.e("clrsyncRespObject", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("clrsyncResponse", ResponseJson.getd().toString());
      /*  if(ResponseJson.equals("true")) return true;
        else return false;*/
        return  Boolean.valueOf(ResponseJson.getd());


    }

    public static CheckValidWaybill CHECK_VALIDWAYBILL(String drivercode, String waybill , String routeid ){

        String JsonParams="";

        JsonParams +="CHECK_VALIDWAYBILL?DRIVERCODE="+drivercode+"&WAYBILL="+waybill+"&ROUTEID="+routeid;



        Object ResponseData = JsonService.jsonreq(JsonParams, webservice.JsonFuncClasses.CheckValidWaybill.class);

        Log.e("waybillres", JsonParams);
        Log.e("waybilRespObject", ResponseData.toString());
        webservice.JsonFuncClasses.CheckValidWaybill ResponseJson = (webservice.JsonFuncClasses.CheckValidWaybill)ResponseData;

        Log.e("waybillResponse", ResponseJson.getd().toString());
        
        CheckValidWaybill ResponseClass = new CheckValidWaybill();

        ResponseClass.WayBill = ResponseJson.getd().WayBill;
        ResponseClass.RouteName = ResponseJson.getd().RouteName;
        ResponseClass.ConsignName = ResponseJson.getd().ConsignName;
        ResponseClass.PhoneNo = ResponseJson.getd().PhoneNo;
        ResponseClass.Area = ResponseJson.getd().Area;
        ResponseClass.Company = ResponseJson.getd().Company;
        ResponseClass.CivilId = ResponseJson.getd().CivilId;
        ResponseClass.Serial = ResponseJson.getd().Serial;
        ResponseClass.CardType = ResponseJson.getd().CardType;
        ResponseClass.DelDate = ResponseJson.getd().DelDate;
        ResponseClass.DelTime = ResponseJson.getd().DelTime;
        ResponseClass.Amount = ResponseJson.getd().Amount;
        ResponseClass.ErrMsg = ResponseJson.getd().ErrMsg;
        ResponseClass.Attempt = ResponseJson.getd().Attempt;
        ResponseClass.Address = ResponseJson.getd().Address;
        ResponseClass.ShipperName = ResponseJson.getd().ShipperName;
        ResponseClass.WAYBILLIdentifier = ResponseJson.getd().AWBIdentifier;

        Log.e("GetEvents/END", "Success");
        return ResponseClass;
    }
      /// Need to check waybillbackimg wen aplication runs
  /*  public static Boolean SET_WAYBILLACK_IMG( String waybill ,String rstno ,String eventid,byte[] bytearray ,String drivercode, String imgtyp ){

        String encoded1=null;

        if(bytearray!=null) {

            encoded1 = Base64.encodeToString(bytearray,Base64.DEFAULT);


        } else encoded1 = null;

        System.out.println("Valuebitmap aftr enc kb:"+bytearray.length/1024);

      //  System.out.println("Value encoded1:"+encoded1);
        Log.e("enc",encoded1);
        System.out.println("ss"+drivercode+"eventid "+eventid+"imgtyp"+imgtyp+"rstno"+rstno+"waybill"+waybill);
       SET_WAYBILLACK_IMG_Request requestobj = new SET_WAYBILLACK_IMG_Request();

        requestobj.setDRIVERCODE(drivercode);
        requestobj.setEVNTID(eventid);
        requestobj.setIMG(encoded1);
        requestobj.setIMGTYPE(imgtyp);
        requestobj.setRSTNO(rstno);
        requestobj.setWAYBILL(waybill);


        ResponseEntity<?> response = JsonService.jsonpostreqImg("SET_WAYBILLACK_IMG",requestobj,StringResponse.class);
         StringResponse ResponsePostReq = (StringResponse) response.getBody();
         Log.e("Response Actual: ", ResponsePostReq.getd());
         return Boolean.valueOf(ResponsePostReq.getd());

        //return null;
    }*/
    /// Need to check odofuelimg wen aplication runs
  /*  public static Boolean SET_ODO_FUEL_IMAGE(String id, byte[] bytearray1,String odotype, byte[] bytearray2 ,String type ){
        String encoded2=null;
        String encoded1=null;

        if(bytearray1!=null) {

            encoded1 = Base64.encodeToString(bytearray1,Base64.DEFAULT);
        } else encoded1=null;
         if(bytearray2!=null) {

          encoded2 = Base64.encodeToString(bytearray2,Base64.DEFAULT);

      } else encoded2=null;
        System.out.println("Value of byte array1 kb"+(bytearray1.length)/1024+ "and 2 kb is"+(bytearray2.length)/1024);
       // System.out.println("Value of encoded1 1"+encoded1+ "and encoded2 is"+encoded2);
       // if(encoded2==null)return1 true;

      *//*  Map<String,String> map = new HashMap<String,String>();
        //map.toSingleValueMap();
        map.put("ID",id);
        map.put("IMAGE1",encoded1);
        map.put("TYPE",type);
        map.put("IMAGE2",encoded2);
        map.put("ODOTYPE",odotype);
*//*

        SET_ODO_FUEL_IMAGE_Request requestobj = new SET_ODO_FUEL_IMAGE_Request();

        requestobj.setID(id);
        requestobj.setIMAGE1(encoded1);
        requestobj.setTYPE(type);
        requestobj.setIMAGE2(encoded2);
        requestobj.setODOTYPE(odotype);



        ResponseEntity<?> ResponsePostReq = JsonService.jsonpostreqodoImg("SET_ODO_FUEL_IMAGE",requestobj,StringResponse.class);
        System.out.println("ResponsePostReq is:"+ResponsePostReq);
        StringResponse ActResp = (StringResponse) ResponsePostReq.getBody();



        Log.e("SETODOFUELIMG/succ", ActResp.getd());
      *//*  if(ResponseJson.equals("true")) return true;
        else return false;*//*
        return Boolean.valueOf(ActResp.getd());

    }*/

    public static CheckTranswaybill CHECK_TRANSWAYBILL(String drivercode, String waybill ) {

        String JsonParams="";

        JsonParams +="CHECK_TRANSWAYBILL?DRIVERCODE="+drivercode+"&WAYBILL="+waybill;
        Object ResponseData = JsonService.jsonreq(JsonParams, webservice.JsonFuncClasses.CheckTranswaybill.class);

        Log.e("TRANSwaybillres", JsonParams);
        Log.e("TRANSwaybilRespObject", ResponseData.toString());
        webservice.JsonFuncClasses.CheckTranswaybill ResponseJson = (webservice.JsonFuncClasses.CheckTranswaybill)ResponseData;

        Log.e("TRANSwaybillResponse", ResponseJson.getd().toString());

        CheckTranswaybill ResponseClass = new CheckTranswaybill();

        ResponseClass.WayBill = ResponseJson.getd().WayBill;
        ResponseClass.RouteName = ResponseJson.getd().RouteName;
        ResponseClass.ConsignName = ResponseJson.getd().ConsignName;
        ResponseClass.PhoneNo = ResponseJson.getd().PhoneNo;
        ResponseClass.Area = ResponseJson.getd().Area;
        ResponseClass.Company = ResponseJson.getd().Company;
        ResponseClass.CivilId = ResponseJson.getd().CivilId;
        ResponseClass.Serial = ResponseJson.getd().Serial;
        ResponseClass.CardType = ResponseJson.getd().CardType;
        ResponseClass.DelDate = ResponseJson.getd().DelDate;
        ResponseClass.DelTime = ResponseJson.getd().DelTime;
        ResponseClass.Amount = ResponseJson.getd().Amount;
        ResponseClass.ErrMsg = ResponseJson.getd().ErrMsg;
        ResponseClass.Attempt = ResponseJson.getd().Attempt;
        ResponseClass.Address = ResponseJson.getd().Address;
        ResponseClass.ShipperName = ResponseJson.getd().ShipperName;
        ResponseClass.AWBIdentifier = ResponseJson.getd().AWBIdentifier;
        ResponseClass.Last_Status = ResponseJson.getd().Last_Status;

        Log.e("GetTRANSAWB/END", "Success");
        return ResponseClass;
    }

    public static ScanWaybillDt[] GET_SCAN_WAYBILL_DT(String drivercode) {


        Map<String,String> map = new HashMap<String,String>();

        map.put("DRIVERCODE",drivercode);


        List<webservice.JsonFuncClasses.ScanWaybillDt> ResponseData =  JsonService.JsonListScanwayDt("GET_SCAN_WAYBILL_DT",map,GET_SCAN_WAYBILL_DTResponse.class);

        System.out.println("ResponseData resp pckp iis:"+ResponseData.size());
        ScanWaybillDt[] ArrayScanwaydetal = new ScanWaybillDt[ResponseData.size()];

        int i = 0;

        for (webservice.JsonFuncClasses.ScanWaybillDt rec:ResponseData
                ) {

            ArrayScanwaydetal[i] = new ScanWaybillDt();
            ArrayScanwaydetal[i].AWBIdentifier = rec.AWBIdentifier;
            ArrayScanwaydetal[i].Address = rec.Address;
            ArrayScanwaydetal[i].Amount = rec.Amount;
            ArrayScanwaydetal[i].Area = rec.Area;
            ArrayScanwaydetal[i].Attempt = rec.Attempt;
            ArrayScanwaydetal[i].CardType = rec.CardType;
            ArrayScanwaydetal[i].CivilId = rec.CivilId;
            ArrayScanwaydetal[i].Company = rec.Company;
            ArrayScanwaydetal[i].ConsignName = rec.ConsignName;
            ArrayScanwaydetal[i].DelDate = rec.DelDate;
            ArrayScanwaydetal[i].DelTime = rec.DelTime;
            ArrayScanwaydetal[i].ErrMsg = rec.ErrMsg;
            ArrayScanwaydetal[i].Last_Status = rec.Last_Status;
            ArrayScanwaydetal[i].PhoneNo = rec.PhoneNo;
            ArrayScanwaydetal[i].RouteName = rec.RouteName;
            ArrayScanwaydetal[i].Serial = rec.Serial;
            ArrayScanwaydetal[i].ShipperName = rec.ShipperName;
            ArrayScanwaydetal[i].WayBill = rec.WayBill;




            i+=1;

            Log.e("DataErrMsg:/suc ",rec.ErrMsg);
        }
        Log.e("Datascnwadet:/suc ", String.valueOf(ArrayScanwaydetal));

        return ArrayScanwaydetal;


    }

    public static PickUpWaybillsDT[] GET_PICKUP_WAYBILLS_DT(String drivercode, String pickupno) {
        String JsonParam ="";
        Map<String,String> map = new HashMap<String,String>();

        map.put("DRIVERCODE",drivercode);
       map.put("PICKUPNO",pickupno);

        List<webservice.JsonFuncClasses.PickUpWaybillsDT> ResponseData =  JsonService.JsonListPckpwayDt("GET_PICKUP_WAYBILLS_DT",map,GET_PICKUP_WAYBILLS_DTResponse.class);

        System.out.println("ResponseData resp pckp iis:"+ResponseData.size());
      PickUpWaybillsDT[] Arraypckpwaydetal = new PickUpWaybillsDT[ResponseData.size()];

        int i = 0;

        for (webservice.JsonFuncClasses.PickUpWaybillsDT rec:ResponseData
                ) {

            Arraypckpwaydetal[i] = new PickUpWaybillsDT();
            Arraypckpwaydetal[i].AMOUNT = rec.AMOUNT;
            Arraypckpwaydetal[i].PAYTYPE = rec.PAYTYPE;
            Arraypckpwaydetal[i].PICKUPNO = rec.PICKUPNO;
            Arraypckpwaydetal[i].SERVICE = rec.SERVICE;
            Arraypckpwaydetal[i].WAYBILL = rec.WAYBILL;

            i+=1;
            Log.e("Data waybill:/suc ",rec.WAYBILL);
        }
        Log.e("Datapckpwadet:/suc ", String.valueOf(Arraypckpwaydetal));

        return Arraypckpwaydetal;


    }

    public static boolean GET_SERVICE_STATUS(String drivercode){


        String JsonParam="";

       // JsonParam +="GET_SERVICE_STATUS_ASYNCH?DRIVERCODE="+drivercode;


        JsonParam +="GET_SERVICE_STATUS?DRIVERCODE="+drivercode;
        StringResponse responseJson = (StringResponse) JsonService.jsonreq(JsonParam,StringResponse.class);
//        GET_Temp responseData = (GET_Temp) JsonService.jsonreq(JsonParam, GET_Temp.class);


        if(responseJson==null){
            return  Boolean.valueOf(null);
        }else
        Log.e("getservstsreq", JsonParam);
       // Log.e("getservstsRespObject", ResponseData.toString());

        Log.e("getservstsResponse", responseJson.toString());

        return  Boolean.valueOf(responseJson.getd());

  /*   //   return1 true;
        String ResultData = SoapService.soapResult(XMLData);
        if(ResultData==null)  ResultData = SoapService.soapResult(XMLData);
        if(ResultData!=null)     Log.e("ResultData is",ResultData);
        else return false;

        String Results = XMLParser.setservicestatusParser(ResultData);
        System.out.println("Connect result in get service status"+Results);
        Log.e("SET_SERVSTAT/END", "Success");
     //  return1 Results;
        if(Results.equals("TRUE")) return true;
        else return false;*/
    }

    public static Remarks[] GET_PICKUP_REMARK() {
        String JsonParameters = "GET_PICKUP_REMARK";

        List<webservice.JsonFuncClasses.Remarks> ResponseData = JsonService.JsonListRemarks(JsonParameters);
        System.out.println("ResponseData is getremark"+ResponseData);

        Remarks[] ArrayRemak = new Remarks[ResponseData.size()];
        int i = 0;
        for (webservice.JsonFuncClasses.Remarks rec:ResponseData
                ) {

            ArrayRemak[i] = new Remarks();
            ArrayRemak[i].REMARKCODE = rec.REMARKCODE;
            ArrayRemak[i].REMARKDESC = rec.REMARKDESC;
            i+=1;
          //  Log.e("Data remk:/suc ", rec.REMARKCODE + "-" + rec.REMARKDESC);
        }
        System.out.println("ResponseData is getremark"+ArrayRemak.length);
        return ArrayRemak;

    }

    public static OpenRst SET_WC(String drcode ){

        Map<String,String> map = new HashMap<String,String>();

        map.put("drivercode",drcode);

        ResponseEntity<?> ResponsePostReq = JsonService.jsonpostreq("SET_WC",map, webservice.JsonFuncClasses.OpenRst.class);
        System.out.println("ResponsePostReq wc is:"+ResponsePostReq);
     // OpenRst ActResp = (OpenRst) ResponsePostReq.getBody();
        webservice.JsonFuncClasses.OpenRst ActResp = (webservice.JsonFuncClasses.OpenRst)ResponsePostReq.getBody();

        OpenRst ResponseClass = new OpenRst();
        ResponseClass.ACKNO= ActResp.getd().ACKNO;
        ResponseClass.ERRMSG=ActResp.getd().ERRMSG;
        ResponseClass.RSTNO=ActResp.getd().RSTNO;
        ResponseClass.RTNO=ActResp.getd().RTNO;
        System.out.println("ResponseClass wc is:"+ResponseClass+"ResponseClass rst"+ResponseClass.RSTNO);
      /*  OpenRst RunsheetCode=null;
        if(ResponseClass.ERRMSG!="False")
            RunsheetCode = GET_OPENRST(drcode);
        else RunsheetCode = null;
        System.out.println("RunsheetCode wc is:"+RunsheetCode);*/

        return ResponseClass;


       /* OpenRst RunsheetCode=null;
        String ResultData = SoapService.soapResult(XMLData);
        if(ResultData==null){
            RunsheetCode = GET_OPENRST(drcode);
            if(RunsheetCode.RSTNO.equals("NA")&&RunsheetCode.ACKNO.equals("NA")&&RunsheetCode.RTNO.equals("NA")) ResultData = SoapService.soapResult(XMLData);
            else return RunsheetCode;
        }
        if(ResultData==null)  ResultData = SoapService.soapResult(XMLData);
        if(ResultData==null) return null;

        Log.e("ResultData is",ResultData);

        String Results = XMLParser.setwcParser(ResultData);
        if(ResultData!="False") RunsheetCode = GET_OPENRST(drcode);
        else RunsheetCode = null;
        Log.e("GetEvents/END", "Success");
        return RunsheetCode;*/
    }

    public static String SET_PICKUP_FINISH(String drivercode, String pickupno, String remarkcode, String datetime) {


        Map<String,String> map = new HashMap<String,String>();
        //map.toSingleValueMap();
        map.put("DRIVERCODE",drivercode);
        map.put("PICKUPNO",pickupno);
        map.put("REMARKCODE",remarkcode);
        map.put("NOTE","");
        map.put("DATENTIME",datetime);

        ResponseEntity<?> ResponsePostReq = JsonService.jsonpostreq("SET_PICKUP_FINISH",map,StringResponse.class);
        System.out.println("ResponsePostReq pickfin is:"+ResponsePostReq);
        StringResponse ActResp = (StringResponse) ResponsePostReq.getBody();

        return ActResp.getd();

    }

    public static Boolean SET_WAYBILLACK_IMG( String waybill ,String rstno ,String eventid,Bitmap bitmap ,String drivercode, String imgtyp ){

        String encoded1=null;


        byte[] bytearray =null;
        ByteArrayOutputStream baos = null;
        System.out.println("Valuebitmap on kb:"+bitmap.getByteCount()/1024);
        if(bitmap!=null) {
            baos= new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);

            bytearray = baos.toByteArray();
            System.out.println("baos iz kb :"+baos.size()/1024);

            encoded1 = Base64.encodeToString(bytearray,Base64.DEFAULT);


        } else encoded1 = null;

        System.out.println("Valuebitmap aftr enc kb:"+bytearray.length/1024);

        //  System.out.println("Value encoded1:"+encoded1);
        Log.e("enc",encoded1);
        System.out.println("ss"+drivercode+"eventid "+eventid+"imgtyp"+imgtyp+"rstno"+rstno+"waybill"+waybill);
        SET_WAYBILLACK_IMG_Request requestobj = new SET_WAYBILLACK_IMG_Request();

        requestobj.setDRIVERCODE(drivercode);
        requestobj.setEVNTID(eventid);
        requestobj.setIMG(encoded1);
        requestobj.setIMGTYPE(imgtyp);
        requestobj.setRSTNO(rstno);
        requestobj.setWAYBILL(waybill);


        ResponseEntity<?> response = JsonService.jsonpostreqImg("SET_WAYBILLACK_IMG",requestobj,StringResponse.class);
        StringResponse ResponsePostReq = (StringResponse) response.getBody();
        Log.e("Response Actual: ", ResponsePostReq.getd());
        return Boolean.valueOf(ResponsePostReq.getd());

        //return null;
    }

    public static Boolean SET_ODO_FUEL_IMAGE(String id, Bitmap bitmap1,String type , Bitmap bitmap2 ,String odotype ){
      String encoded2=null;
      String encoded1=null;
      byte[] bytearray1= null;
      byte[] bytearray2 =null;
      ByteArrayOutputStream baos1 = null;
      ByteArrayOutputStream baos2 = null;
      System.out.println("Value of bitmap1 1"+bitmap1+ "and bitmap2 is"+bitmap2);
      if(bitmap1!=null) {
          baos1= new ByteArrayOutputStream();
          bitmap1.compress(Bitmap.CompressFormat.JPEG,100, baos1);
          bytearray1 = baos1.toByteArray();
          encoded1 = Base64.encodeToString(bytearray1,Base64.DEFAULT);
      } else encoded1=null;
      if(bitmap2!=null) {
          baos2= new ByteArrayOutputStream();
          bitmap2.compress(Bitmap.CompressFormat.JPEG,100, baos2);

          bytearray2 = baos2.toByteArray();
          encoded2 = Base64.encodeToString(bytearray2,Base64.DEFAULT);

      } else encoded2=null;
      System.out.println("Value of byte array1 kb"+(bytearray1.length)/1024+ "and 2 kb is"+(bytearray2.length)/1024);
      // System.out.println("Value of encoded1 1"+encoded1+ "and encoded2 is"+encoded2);
      // if(encoded2==null)return1 true;

      SET_ODO_FUEL_IMAGE_Request requestobj = new SET_ODO_FUEL_IMAGE_Request();

      requestobj.setID(id);
      requestobj.setIMAGE1(encoded1);
      requestobj.setTYPE(type);
      requestobj.setIMAGE2(encoded2);
      requestobj.setODOTYPE(odotype);


        System.out.println("values to servc:"+id+"encoded1 :"+encoded1+"type"+type+"encoded2 is"+encoded2+"odotype:"+odotype);
      ResponseEntity<?> ResponsePostReq = JsonService.jsonpostreqodoImg("SET_ODO_FUEL_IMAGE",requestobj,StringResponse.class);
      System.out.println("ResponsePostReq is:"+ResponsePostReq);
      StringResponse ActResp = (StringResponse) ResponsePostReq.getBody();



      Log.e("SETODOFUELIMG/succ", ActResp.getd());
      /*  if(ResponseJson.equals("true")) return true;
        else return false;*/
      return Boolean.valueOf(ActResp.getd());

  }

    public static CheckValidPickupWaybill CHECK_VALIDPICKUPWAYBILL(String drivercode, String waybill , String usercode, String tagmps, String service, String pickupno, String paytype, String amount, String datetimestr )                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  {

        String JsonParams="";

        JsonParams +="CHECK_VALIDPICKUPWAYBILL?DRIVERCODE="+drivercode+"&WAYBILL="+waybill+"&USERCODE="+usercode+"&TAGMPS="+tagmps+"&SERVICE="+service+"&PICKUPNO="+pickupno+"&PAYTYPE="+paytype+"&AMOUNT="+amount+"&DATETIMESTR="+datetimestr;



        Object ResponseData = JsonService.jsonreq(JsonParams, webservice.JsonFuncClasses.CheckValidPickupWaybill.class);

        Log.e("waybillrespckp", JsonParams);
        Log.e("waybilpckpRespObject", ResponseData.toString());
        webservice.JsonFuncClasses.CheckValidPickupWaybill ResponseJson = (webservice.JsonFuncClasses.CheckValidPickupWaybill)ResponseData;

        Log.e("waybillpckpResponse", ResponseJson.getd().toString());

        CheckValidPickupWaybill ResponseClass = new CheckValidPickupWaybill();

        ResponseClass.AWBIdentifier = ResponseJson.getd().AWBIdentifier;
        ResponseClass.Address = ResponseJson.getd().Address;
        ResponseClass.Amount = ResponseJson.getd().Amount;
        ResponseClass.Area = ResponseJson.getd().Area;
        ResponseClass.Attempt = ResponseJson.getd().Attempt;
        ResponseClass.CardType = ResponseJson.getd().CardType;
        ResponseClass.CivilId = ResponseJson.getd().CivilId;
        ResponseClass.Company = ResponseJson.getd().Company;
        ResponseClass.ConsignName = ResponseJson.getd().ConsignName;
        ResponseClass.DelDate = ResponseJson.getd().DelDate;
        ResponseClass.DelTime = ResponseJson.getd().DelTime;
        ResponseClass.ErrMsg = ResponseJson.getd().ErrMsg;
        ResponseClass.Last_Status = ResponseJson.getd().Last_Status;
        ResponseClass.PhoneNo = ResponseJson.getd().PhoneNo;
        ResponseClass.RouteName = ResponseJson.getd().RouteName;
        ResponseClass.Serial = ResponseJson.getd().Serial;
        ResponseClass.ShipperName = ResponseJson.getd().ShipperName;
        ResponseClass.WayBill=ResponseJson.getd().WayBill;

        Log.e("GetEvents/END", "Success");
        return ResponseClass;
    }

    public static String SET_PICKUPDETAILS(String drivercode ,String pickupno){

        Map<String,String> map = new HashMap<String,String>();

        map.put("DRIVERCODE",drivercode);
        map.put("PICKUPNO",pickupno);

        ResponseEntity<?> ResponsePostReq = JsonService.jsonpostreq("SET_PICKUPDETAILS",map,StringResponse.class);
        System.out.println("ResponsePostReq is:"+ResponsePostReq);
        StringResponse ActResp = (StringResponse) ResponsePostReq.getBody();

        return ActResp.getd();
    }

    public static String SET_DELETE_WAYBILL_PICKUP(String drivercode ,String pickupno,String waybill){

        String JsonParam="";


        JsonParam +="SET_DELETE_WAYBILL_PICKUP?DRIVERCODE="+drivercode+"&PICKUPNO="+pickupno+"&WAYBILL="+waybill;

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("DELETE_WAYBIL", JsonParam);
//        Log.e("DELETE_WAYBILpckObject", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("DELETE_WAYBILResponse", ResponseJson.getd().toString());
        return ResponseJson.getd();

    }

    public static PickupHoldwaybills[] GET_HOLDWAYBILLS_PICKUP(String drivercode, String pickpno) {


        Map<String,String> map = new HashMap<String,String>();

        map.put("DRIVERCODE",drivercode);
        map.put("PICKUPNO",pickpno);


        List<webservice.JsonFuncClasses.PickupHoldwaybills> ResponseData =  JsonService.JsonListPickupholdwaybill("GET_HOLDWAYBILLS_PICKUP",map,GET_HOLDWAYBILLS_PICKUP_Response.class);

        System.out.println("ResponseData resp pckp iis:"+ResponseData.size());
        PickupHoldwaybills[] Arraypckphldwaybl = new PickupHoldwaybills[ResponseData.size()];

        int i = 0;

        for (webservice.JsonFuncClasses.PickupHoldwaybills rec:ResponseData
                ) {

            Arraypckphldwaybl[i] = new PickupHoldwaybills();
            Arraypckphldwaybl[i].AMOUNT = rec.AMOUNT;
            Arraypckphldwaybl[i].DATETIMESTR = rec.DATETIMESTR;
            Arraypckphldwaybl[i].PAYTYPE = rec.PAYTYPE;
            Arraypckphldwaybl[i].PICKUPNO = rec.PICKUPNO;
            Arraypckphldwaybl[i].SERVICE = rec.SERVICE;
            Arraypckphldwaybl[i].STATUS = rec.STATUS;
            Arraypckphldwaybl[i].USERCODE = rec.USERCODE;
            Arraypckphldwaybl[i].WAYBILL = rec.WAYBILL;
            Arraypckphldwaybl[i].TAGMTS = rec.TAGMTS;

            i+=1;

            Log.e("DataErrMsg:/suc ",rec.WAYBILL);
        }
        Log.e("Datascnwadet:/suc ", String.valueOf(Arraypckphldwaybl));
        System.out.println("ResponseData Arraypckphldwaybl pckp iis:"+Arraypckphldwaybl);
        return Arraypckphldwaybl;


    }

    /* public static String SET_DELETE_WAYBILL_PICKUP(String drivercode ,String pickupno,String waybill){

        String JsonParam="";

        JsonParam +="SET_DELETE_WAYBILL_PICKUP?DRIVERCODE="+drivercode+"&PICKUPNO="+pickupno+"&WAYBILL=["+waybill+"]";

        Object ResponseData = JsonService.jsonreq(JsonParam,StringResponse.class);

        Log.e("DELETE_WAYBIL", JsonParam);
        Log.e("DELETE_WAYBILpckObject", ResponseData.toString());
        StringResponse ResponseJson = (StringResponse)ResponseData;

        Log.e("DELETE_WAYBILResponse", ResponseJson.getd().toString());
        return ResponseJson.getd();

    }*/
    public static String CUSTOMER_NOTIFY_TRACK (String waybill , String drivercode){

        Map<String,String> map = new HashMap<String,String>();
        //map.toSingleValueMap();
        map.put("WAYBILL",waybill);
        map.put("DRIVERCODE",drivercode);


        ResponseEntity<?> ResponsePostReq = JsonService.jsonpostreq("CUSTOMER_NOTIFY_TRACK",map,StringResponse.class);
        System.out.println("ResponsePostReq cust notify track is:"+ResponsePostReq);
        StringResponse ActResp = (StringResponse) ResponsePostReq.getBody();


        return ActResp.getd();
    }

    public static JSONObject Check_ValidPickupReference(String drivercode, String reference , String usercode, String tagmps, String service, String pickupno, String paytype, String amount, String datetimestr ) {

        JSONObject jsonobj = new JSONObject();
        try {

            jsonobj.put("DRIVERCODE",drivercode);
            jsonobj.put("Reference",reference);
            jsonobj.put("USERCODE",usercode);
            jsonobj.put("TAGMPS",tagmps);
            jsonobj.put("SERVICE",service);
            jsonobj.put("PICKUPNO",pickupno);
            jsonobj.put("PAYTYPE",paytype);
            jsonobj.put("AMOUNT",amount);
            jsonobj.put("DATETIMESTR",datetimestr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("jsonobj"+jsonobj);
        JSONObject resp = null;
        JSONObject jsonchckreferResp = null;
        // JSONObject obj = new JSONObject(setdel);
        resp = JsonService.jsonnewpostreq(url,"Check_ValidPickupReference",jsonobj);
        System.out.println("ResponsePostReq delis:"+resp);

        try {
            JSONObject json = new JSONObject(String.valueOf(resp));
            jsonchckreferResp=json.getJSONObject("d");

            CheckValidReferenceWaybill ResponseClass = new CheckValidReferenceWaybill();



        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonchckreferResp!= null){
            Log.e("String jsoncon", jsonchckreferResp.toString());
        }

        return  jsonchckreferResp;

    }

}
