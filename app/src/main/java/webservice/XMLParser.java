package webservice;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

import webservice.FuncClasses.CheckHoldvalidwaybill;
import webservice.FuncClasses.CheckTranswaybill;
import webservice.FuncClasses.CheckValidWaybill;
import webservice.FuncClasses.Couriers;
import webservice.FuncClasses.Events;
import webservice.FuncClasses.HoldWayBills;
import webservice.FuncClasses.OpenRst;
import webservice.FuncClasses.PayType;
import webservice.FuncClasses.PickUp;
import webservice.FuncClasses.PickUpDt;
import webservice.FuncClasses.PickUpWaybillsDT;
import webservice.FuncClasses.Remarks;
import webservice.FuncClasses.Routes;
import webservice.FuncClasses.RstDetail;
import webservice.FuncClasses.ScanWaybillDt;
import webservice.FuncClasses.Service;

/**
 * Created by lj on 19-09-2016.
 */
public class XMLParser {

    public static String Parser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

        //Xml Parser

   //     System.out.println("Inputstream is"+is);
        myParser.setInput(new StringReader(response.toString()));
   //     System.out.println("Value in stringreader is"+response.toString());
   //     myParser.setInput(is,null);
   //     System.out.println("Value in myparser is"+myParser);
   //     String loginResult;
        int event = myParser.getEventType();
   //     System.out.println("Value in event is"+event);
        while (event != XmlPullParser.END_DOCUMENT)
        {

            if(event == XmlPullParser.TEXT)
            {
                response = myParser.getText();
            }

            event = myParser.next();
        }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static Events[] EventParser(String response) {

        Events[] ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("EVENTS"))
                        TagCount += 1;
                event = myParser.nextToken();
            }

            ResultData = new Events[TagCount];
            String TagFlag="";

            for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new Events();
            }

            int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);

            myParser.setInput(new StringReader(response));
            event = myParser.getEventType();
            System.out.println("Event Type Last : " + event);
            System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("EVENTID") || myParser.getName().equals("EVENTDESC"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    } else
                        TagFlag = "";
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("EVENTID"))
                        ResultData[DataCounter].EVENTCODE = myParser.getText();
                    if(TagFlag.equals("EVENTDESC"))
                        ResultData[DataCounter].EVENTDESC = myParser.getText();
                }

                if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("EVENTS"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static PickUp[] PickUpParser(String response) {
        PickUp[] ResultPickup=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("PICKUP"))
                        TagCount += 1;
                event = myParser.nextToken();
            }
            ResultPickup = new PickUp[TagCount];
            String TagFlag="";
            for (int i = 0; i < ResultPickup.length; i++) {
                ResultPickup[i]=new PickUp();
            }
            int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);

            myParser.setInput(new StringReader(response));
            event = myParser.getEventType();
            System.out.println("Event Type Last : " + event);
            System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);

            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("PICK_NO") || myParser.getName().equals("IDENTIFIER") || myParser.getName().equals("ACC_NAME") || myParser.getName().equals("PICK_ADD") || myParser.getName().equals("PICK_AREA") || myParser.getName().equals("CONTACT_PERSON") || myParser.getName().equals("PICK_PHONE") || myParser.getName().equals("PICK_TIME")|| myParser.getName().equals("ERR")|| myParser.getName().equals("CONSIGNEE_NAME")|| myParser.getName().equals("DEL_ADD")|| myParser.getName().equals("DEL_CITY")|| myParser.getName().equals("DEL_PHONE"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    } else
                        TagFlag = "";
                }
                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("PICK_NO"))
                        ResultPickup[DataCounter].PICK_NO = myParser.getText();
                    if(TagFlag.equals("IDENTIFIER"))
                        ResultPickup[DataCounter].IDENTIFIER = myParser.getText();
                    if(TagFlag.equals("ACC_NAME"))
                        ResultPickup[DataCounter].ACC_NAME = myParser.getText();
                    if(TagFlag.equals("PICK_ADD"))
                        ResultPickup[DataCounter].PICK_ADD = myParser.getText();
                    if(TagFlag.equals("PICK_AREA"))
                        ResultPickup[DataCounter].PICK_AREA = myParser.getText();
                    if(TagFlag.equals("CONTACT_PERSON"))
                        ResultPickup[DataCounter].CONTACT_PERSON = myParser.getText();
                    if(TagFlag.equals("PICK_PHONE"))
                        ResultPickup[DataCounter].PICK_PHONE = myParser.getText();
                    if(TagFlag.equals("PICK_TIME"))
                        ResultPickup[DataCounter].PICK_TIME = myParser.getText();
                    if(TagFlag.equals("ERR"))
                        ResultPickup[DataCounter].ERR = myParser.getText();
                    if(TagFlag.equals("CONSIGNEE_NAME"))
                        ResultPickup[DataCounter].CONSIGNEE_NAME = myParser.getText();
                    if(TagFlag.equals("DEL_ADD"))
                        ResultPickup[DataCounter].DEL_ADD = myParser.getText();
                    if(TagFlag.equals("DEL_CITY"))
                        ResultPickup[DataCounter].DEL_CITY = myParser.getText();
                    if(TagFlag.equals("DEL_PHONE"))
                        ResultPickup[DataCounter].DEL_PHONE = myParser.getText();
                }

                if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("PICKUP"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }

     return ResultPickup;
    }
    public static String courierrouteParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    System.out.println("Response in courirer route is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static Couriers[] courierParser(String response) {

        Couriers[] ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("Courier_Info"))
                        TagCount += 1;
                event = myParser.nextToken();
            }

            ResultData = new Couriers[TagCount];
            String TagFlag="";

            for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new Couriers();
            }

            int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);

            myParser.setInput(new StringReader(response));
            event = myParser.getEventType();
            System.out.println("Event Type Last : " + event);
            System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("Driver_Name") || myParser.getName().equals("Driver_Code"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    }
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("Driver_Code"))
                        ResultData[DataCounter].Driver_Code = myParser.getText();
                    if(TagFlag.equals("Driver_Name"))
                        ResultData[DataCounter].Driver_Name = myParser.getText();
                }

                if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("Courier_Info"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static HoldWayBills[] holdwaybillParser(String response) {

        HoldWayBills[] ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("HoldWayBillInfo"))
                        TagCount += 1;
                event = myParser.nextToken();
            }

            ResultData = new HoldWayBills[TagCount];
            String TagFlag="";

            for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new HoldWayBills();
            }

            int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);

            myParser.setInput(new StringReader(response));
            event = myParser.getEventType();
            System.out.println("Event Type Last : " + event);
            System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("Waybill") ||myParser.getName().equals("TrnsDrvr") || myParser.getName().equals("Area") || myParser.getName().equals("Consignee") || myParser.getName().equals("Phno") || myParser.getName().equals("Company") || myParser.getName().equals("CivilId") || myParser.getName().equals("Serial") || myParser.getName().equals("CardType") || myParser.getName().equals("DelDate") || myParser.getName().equals("DelTime") || myParser.getName().equals("Amount") || myParser.getName().equals("Address"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    } else
                        TagFlag = "";
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("Waybill"))
                        ResultData[DataCounter].Waybill = myParser.getText();
                    if(TagFlag.equals("TrnsDrvr"))
                        ResultData[DataCounter].TrnsDrvr = myParser.getText();
                    if(TagFlag.equals("Area"))
                        ResultData[DataCounter].Area = myParser.getText();
                    if(TagFlag.equals("Consignee"))
                        ResultData[DataCounter].Consignee = myParser.getText();
                    if(TagFlag.equals("Phno"))
                        ResultData[DataCounter].Phno = myParser.getText();
                    if(TagFlag.equals("Company"))
                        ResultData[DataCounter].Company = myParser.getText();
                    if(TagFlag.equals("CivilId"))
                        ResultData[DataCounter].CivilId = myParser.getText();
                    if(TagFlag.equals("Serial"))
                        ResultData[DataCounter].Serial = myParser.getText();
                    if(TagFlag.equals("CardType"))
                        ResultData[DataCounter].CardType = myParser.getText();
                    if(TagFlag.equals("DelDate"))
                        ResultData[DataCounter].DelDate = myParser.getText();
                    if(TagFlag.equals("DelTime"))
                        ResultData[DataCounter].DelTime = myParser.getText();
                    if(TagFlag.equals("Amount"))
                        ResultData[DataCounter].Amount = myParser.getText();
                    if(TagFlag.equals("Address"))
                        ResultData[DataCounter].Address = myParser.getText();

                }

                if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("HoldWayBillInfo"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static String odoreadingParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    System.out.println("Response in odoreading is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static PayType[] paytypeParser(String response) {

        PayType[] ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("PAYMENT"))
                        TagCount += 1;
                event = myParser.nextToken();
            }

            ResultData = new PayType[TagCount];
            String TagFlag="";

            for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new PayType();
            }

            int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);

            myParser.setInput(new StringReader(response));
            event = myParser.getEventType();
      //      System.out.println("Event Type Last : " + event);
      //      System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("PAYID") || myParser.getName().equals("PAYTYPE"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    } else
                        TagFlag = "";
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("PAYID"))
                        ResultData[DataCounter].PAYID = myParser.getText();
                    if(TagFlag.equals("PAYTYPE"))
                        ResultData[DataCounter].PAYTYPE = myParser.getText();
                }

                if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("PAYMENT"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static Routes[] routesParser(String response) {

        Routes[] ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("Route_Info"))
                        TagCount += 1;
                event = myParser.nextToken();
            }

            ResultData = new Routes[TagCount];
            String TagFlag="";

            for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new Routes();
            }

            int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);

            myParser.setInput(new StringReader(response));
            event = myParser.getEventType();
            //      System.out.println("Event Type Last : " + event);
            //      System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("RouteCode") || myParser.getName().equals("RouteName"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    } else
                        TagFlag = "";
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("RouteCode"))
                        ResultData[DataCounter].RouteCode = myParser.getText();
                    if(TagFlag.equals("RouteName"))
                        ResultData[DataCounter].RouteName = myParser.getText();
                }

                if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("Route_Info"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static RstDetail[] rstdetailParser(String response) {

        RstDetail[] ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("WayBill_Info"))
                        TagCount += 1;
                event = myParser.nextToken();
            }

            ResultData = new RstDetail[TagCount];
            String TagFlag="";

            for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new RstDetail();
            }

            int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);

            myParser.setInput(new StringReader(response));
            event = myParser.getEventType();
            System.out.println("Event Type Last : " + event);
            System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("WayBill") || myParser.getName().equals("RouteName")|| myParser.getName().equals("ConsignName")|| myParser.getName().equals("PhoneNo")|| myParser.getName().equals("Area")|| myParser.getName().equals("Company")|| myParser.getName().equals("CivilId")|| myParser.getName().equals("Serial")|| myParser.getName().equals("CardType")|| myParser.getName().equals("DelDate")|| myParser.getName().equals("DelTime")|| myParser.getName().equals("Amount")|| myParser.getName().equals("ErrMsg")|| myParser.getName().equals("Attempt")|| myParser.getName().equals("Address")|| myParser.getName().equals("ShipperName")|| myParser.getName().equals("AWBIdentifier")|| myParser.getName().equals("Last_Status"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    }
                    else
                        TagFlag = "";
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("WayBill"))
                        ResultData[DataCounter].WayBill = myParser.getText();
                    if(TagFlag.equals("RouteName"))
                        ResultData[DataCounter].RouteName = myParser.getText();
                    if(TagFlag.equals("ConsignName"))
                        ResultData[DataCounter].ConsignName = myParser.getText();
                    if(TagFlag.equals("PhoneNo"))
                        ResultData[DataCounter].PhoneNo = myParser.getText();
                    if(TagFlag.equals("Area"))
                        ResultData[DataCounter].Area = myParser.getText();
                    if(TagFlag.equals("Company"))
                        ResultData[DataCounter].Company = myParser.getText();
                    if(TagFlag.equals("CivilId"))
                        ResultData[DataCounter].CivilId = myParser.getText();
                    if(TagFlag.equals("Serial"))
                        ResultData[DataCounter].Serial = myParser.getText();
                    if(TagFlag.equals("CardType"))
                        ResultData[DataCounter].CardType = myParser.getText();
                    if(TagFlag.equals("DelDate"))
                        ResultData[DataCounter].DelDate = myParser.getText();
                    if(TagFlag.equals("EDelTime"))
                        ResultData[DataCounter].DelTime = myParser.getText();
                    if(TagFlag.equals("Amount"))
                        ResultData[DataCounter].Amount = myParser.getText();
                    if(TagFlag.equals("ErrMsg"))
                        ResultData[DataCounter].ErrMsg = myParser.getText();
                    if(TagFlag.equals("Attempt"))
                        ResultData[DataCounter].Attempt = myParser.getText();
                    if(TagFlag.equals("Address"))
                        ResultData[DataCounter].Address = myParser.getText();
                    if(TagFlag.equals("ShipperName"))
                        ResultData[DataCounter].ShipperName = myParser.getText();
                    if(TagFlag.equals("AWBIdentifier")) {
                        ResultData[DataCounter].AWBIdentifier = myParser.getText();
                        System.out.println("AWB Parser Identity : " + myParser.getText());
                    }
                    if(TagFlag.equals("Last_Status"))
                        ResultData[DataCounter].Last_Status = myParser.getText();

                }

                if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("WayBill_Info"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static Service[] serviceParser(String response) {

        Service[] ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("SERVICE"))
                        TagCount += 1;
                event = myParser.nextToken();
            }

            ResultData = new Service[TagCount];
            String TagFlag="";

            for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new Service();
            }

            int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);

            myParser.setInput(new StringReader(response));
            event = myParser.getEventType();
            System.out.println("Event Type Last : " + event);
            System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("SERVICEID") || myParser.getName().equals("SERVICETYPE"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    } else
                        TagFlag = "";
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("SERVICEID"))
                        ResultData[DataCounter].SERVICEID = myParser.getText();
                    if(TagFlag.equals("SERVICETYPE"))
                        ResultData[DataCounter].SERVICETYPE = myParser.getText();
                }

                if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("SERVICE"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static String acceptholdParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
             //       System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setawbeventtParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                //    System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setcancellationParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
               //     System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setcourierrouteParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                 //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setcourierroutecloseParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setdeleteholdParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setdeleteinitialscansingleParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setdeliveryParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setdevicestatusParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setdrivermeterParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setfueltrackParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setpickuprecvdParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setpickupdetailsParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setsynchnlParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String settransconfirmParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String settransholdParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String settranspickupParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String settranspickupconfirmParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String settranswcParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setwcParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static PickUpDt[] getpickupdtParser(String response) {

        PickUpDt[] ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("PICKUP"))
                        TagCount += 1;
                event = myParser.nextToken();
            }

            ResultData = new PickUpDt[TagCount];
            String TagFlag="";

            for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new PickUpDt();
            }

            int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);

            myParser.setInput(new StringReader(response));
            event = myParser.getEventType();
            //      System.out.println("Event Type Last : " + event);
            //      System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("PICK_NO") || myParser.getName().equals("ACC_NAME") || myParser.getName().equals("PICK_ADD") || myParser.getName().equals("PICK_AREA") || myParser.getName().equals("CONTACT_PERSON") || myParser.getName().equals("PICK_PHONE") || myParser.getName().equals("PICK_TIME") || myParser.getName().equals("ERR") || myParser.getName().equals("IDENTIFIER") || myParser.getName().equals("CONSIGNEE_NAME") || myParser.getName().equals("DEL_ADD") || myParser.getName().equals("DEL_CITY") || myParser.getName().equals("DEL_PHONE"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    }
                    else
                        TagFlag = "";
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("PICK_NO"))
                        ResultData[DataCounter].PICK_NO = myParser.getText();
                    if(TagFlag.equals("ACC_NAME"))
                        ResultData[DataCounter].ACC_NAME = myParser.getText();
                    if(TagFlag.equals("PICK_ADD"))
                        ResultData[DataCounter].PICK_ADD = myParser.getText();
                    if(TagFlag.equals("PICK_AREA"))
                        ResultData[DataCounter].PICK_AREA = myParser.getText();
                    if(TagFlag.equals("CONTACT_PERSON"))
                        ResultData[DataCounter].CONTACT_PERSON = myParser.getText();
                    if(TagFlag.equals("PICK_PHONE"))
                        ResultData[DataCounter].PICK_PHONE = myParser.getText();
                    if(TagFlag.equals("PICK_TIME"))
                        ResultData[DataCounter].PICK_TIME = myParser.getText();
                    if(TagFlag.equals("ERR"))
                        ResultData[DataCounter].ERR = myParser.getText();
                    if(TagFlag.equals("IDENTIFIER"))
                        ResultData[DataCounter].IDENTIFIER = myParser.getText();
                    if(TagFlag.equals("CONSIGNEE_NAME"))
                        ResultData[DataCounter].CONSIGNEE_NAME = myParser.getText();
                    if(TagFlag.equals("DEL_ADD"))
                        ResultData[DataCounter].DEL_ADD = myParser.getText();
                    if(TagFlag.equals("DEL_CITY"))
                        ResultData[DataCounter].DEL_CITY = myParser.getText();
                    if(TagFlag.equals("DEL_PHONE"))
                        ResultData[DataCounter].DEL_PHONE = myParser.getText();
                }

                if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("PICKUP"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static CheckHoldvalidwaybill checkholdwaybillParser(String response) {

        CheckHoldvalidwaybill ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;
/*
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("CHECK_HOLDVALIDWAYBILLResult"))
                        TagCount += 1;
                event = myParser.nextToken();
            }*/

            ResultData = new CheckHoldvalidwaybill();
            String TagFlag="";

          /*  for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new CheckHoldvalidwaybill();
            }*/

            int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);

        //    myParser.setInput(new StringReader(response));
          //  event = myParser.getEventType();
            //      System.out.println("Event Type Last : " + event);
            //      System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("WayBill") || myParser.getName().equals("RouteName") || myParser.getName().equals("ConsignName") || myParser.getName().equals("PhoneNo") || myParser.getName().equals("Area") || myParser.getName().equals("Company") || myParser.getName().equals("CivilId") || myParser.getName().equals("Serial") || myParser.getName().equals("CardType") || myParser.getName().equals("DelDate") || myParser.getName().equals("DelTime") || myParser.getName().equals("Amount") || myParser.getName().equals("Attempt") || myParser.getName().equals("Address") || myParser.getName().equals("ErrMsg"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    }
                    else
                        TagFlag = "";
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("WayBill"))
                        ResultData.WayBill = myParser.getText();
                    if(TagFlag.equals("RouteName"))
                        ResultData.RouteName = myParser.getText();
                    if(TagFlag.equals("ConsignName"))
                        ResultData.ConsignName = myParser.getText();
                    if(TagFlag.equals("PhoneNo"))
                        ResultData.PhoneNo = myParser.getText();
                    if(TagFlag.equals("Area"))
                        ResultData.Area = myParser.getText();
                    if(TagFlag.equals("Company"))
                        ResultData.Company = myParser.getText();
                    if(TagFlag.equals("CivilId"))
                        ResultData.CivilId = myParser.getText();
                    if(TagFlag.equals("Serial"))
                        ResultData.Serial = myParser.getText();
                    if(TagFlag.equals("CardType"))
                        ResultData.CardType = myParser.getText();
                    if(TagFlag.equals("DelDate"))
                        ResultData.DelDate = myParser.getText();
                    if(TagFlag.equals("DelTime"))
                        ResultData.DelTime = myParser.getText();
                    if(TagFlag.equals("Amount"))
                        ResultData.Amount = myParser.getText();
                    if(TagFlag.equals("Attempt"))
                        ResultData.Attempt = myParser.getText();
                    if(TagFlag.equals("Address"))
                        ResultData.Address = myParser.getText();
                    if(TagFlag.equals("ErrMsg"))
                        ResultData.ErrMsg = myParser.getText();
                }

              /*  if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("CHECK_HOLDVALIDWAYBILLResult"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }
*/
                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static String chkvhlbarcodeParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String clearsycnchannelParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static CheckValidWaybill checkvalidwaybillParser(String response) {

        CheckValidWaybill ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

           /* while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("CHECK_VALIDWAYBILLResult"))
                        TagCount += 1;
                event = myParser.nextToken();
            }*/

            ResultData = new CheckValidWaybill();
            String TagFlag="";

          /*  for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new CheckValidWaybill();
            }*/

         /*   int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);*/

            //      System.out.println("Event Type Last : " + event);
            //      System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("WayBill") || myParser.getName().equals("RouteName") || myParser.getName().equals("ConsignName") || myParser.getName().equals("PhoneNo") || myParser.getName().equals("Area") || myParser.getName().equals("Company") || myParser.getName().equals("CivilId") || myParser.getName().equals("Serial") || myParser.getName().equals("CardType") || myParser.getName().equals("DelDate") || myParser.getName().equals("DelTime") || myParser.getName().equals("Amount") || myParser.getName().equals("Attempt") || myParser.getName().equals("Address") || myParser.getName().equals("ShipperName") || myParser.getName().equals("AWBIdentifier") || myParser.getName().equals("ErrMsg"))
                    {
                        TagFlag = myParser.getName();
                        Log.e("XML Tag/Start",TagFlag);
                    }
                    else
                        TagFlag = "";
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("WayBill"))
                        ResultData.WayBill = myParser.getText();
                    if(TagFlag.equals("RouteName"))
                        ResultData.RouteName = myParser.getText();
                    if(TagFlag.equals("ConsignName"))
                        ResultData.ConsignName = myParser.getText();
                    if(TagFlag.equals("PhoneNo"))
                        ResultData.PhoneNo = myParser.getText();
                    if(TagFlag.equals("Area"))
                        ResultData.Area = myParser.getText();
                    if(TagFlag.equals("Company"))
                        ResultData.Company = myParser.getText();
                    if(TagFlag.equals("CivilId"))
                        ResultData.CivilId = myParser.getText();
                    if(TagFlag.equals("Serial"))
                        ResultData.Serial = myParser.getText();
                    if(TagFlag.equals("CardType "))
                        ResultData.CardType = myParser.getText();
                    if(TagFlag.equals("DelDate"))
                        ResultData.DelDate = myParser.getText();
                    if(TagFlag.equals("DelTime"))
                        ResultData.DelTime = myParser.getText();
                    if(TagFlag.equals("Amount"))
                        ResultData.Amount = myParser.getText();
                    if(TagFlag.equals("Attempt"))
                        ResultData.Attempt = myParser.getText();
                    if(TagFlag.equals("Address"))
                        ResultData.Address = myParser.getText();
                    if(TagFlag.equals("ShipperName"))
                        ResultData.ShipperName = myParser.getText();
                    if(TagFlag.equals("AWBIdentifier"))
                        ResultData.WAYBILLIdentifier = myParser.getText();
                    if(TagFlag.equals("ErrMsg"))
                        ResultData.ErrMsg = myParser.getText();
                }

            /*    if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("CHECK_VALIDWAYBILLResult"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }*/

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static String setwaybillbakimgParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static String setodofuelimgParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static CheckTranswaybill checktranswaybillParser(String response) {

        CheckTranswaybill ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

        /*    while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("CHECK_TRANSWAYBILLResult"))
                        TagCount += 1;
                event = myParser.nextToken();
            }*/

            ResultData = new CheckTranswaybill();
            String TagFlag="";

          /*  for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new CheckTranswaybill();
            }*/

         //   int DataCounter = 0;
          //  System.out.println("TagCount : " + TagCount);

            myParser.setInput(new StringReader(response));
            event = myParser.getEventType();
            //      System.out.println("Event Type Last : " + event);
            //      System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("WayBill") || myParser.getName().equals("RouteName") || myParser.getName().equals("ConsignName") || myParser.getName().equals("PhoneNo") || myParser.getName().equals("Area") || myParser.getName().equals("Company") || myParser.getName().equals("CivilId") || myParser.getName().equals("Serial") || myParser.getName().equals("CardType") || myParser.getName().equals("DelDate") || myParser.getName().equals("DelTime") || myParser.getName().equals("Amount") || myParser.getName().equals("Attempt") || myParser.getName().equals("Address") || myParser.getName().equals("ErrMsg"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    }
                    else
                        TagFlag = "";
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("WayBill"))
                        ResultData.WayBill = myParser.getText();
                    if(TagFlag.equals("RouteName"))
                        ResultData.RouteName = myParser.getText();
                    if(TagFlag.equals("ConsignName"))
                        ResultData.ConsignName = myParser.getText();
                    if(TagFlag.equals("PhoneNo"))
                        ResultData.PhoneNo = myParser.getText();
                    if(TagFlag.equals("Area"))
                        ResultData.Area = myParser.getText();
                    if(TagFlag.equals("Company"))
                        ResultData.Company = myParser.getText();
                    if(TagFlag.equals("CivilId"))
                        ResultData.CivilId = myParser.getText();
                    if(TagFlag.equals("Serial"))
                        ResultData.Serial = myParser.getText();
                    if(TagFlag.equals("CardType "))
                        ResultData.CardType = myParser.getText();
                    if(TagFlag.equals("DelDate"))
                        ResultData.DelDate = myParser.getText();
                    if(TagFlag.equals("DelTime"))
                        ResultData.DelTime = myParser.getText();
                    if(TagFlag.equals("Amount"))
                        ResultData.Amount = myParser.getText();
                    if(TagFlag.equals("Attempt"))
                        ResultData.Attempt = myParser.getText();
                    if(TagFlag.equals("Address"))
                        ResultData.Address = myParser.getText();
                    if(TagFlag.equals("ErrMsg"))
                        ResultData.ErrMsg = myParser.getText();
                }

                /*if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("CHECK_TRANSWAYBILLResult"))
              //          DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }
*/
                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static ScanWaybillDt[] getscanwaybilldtParser(String response) {

        ScanWaybillDt[] ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("WayBill_Info"))
                        TagCount += 1;
                event = myParser.nextToken();
            }

            ResultData = new ScanWaybillDt[TagCount];
            String TagFlag="";

            for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new ScanWaybillDt();
            }

            int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);

            myParser.setInput(new StringReader(response));
            event = myParser.getEventType();
            //      System.out.println("Event Type Last : " + event);
            //      System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("WayBill") || myParser.getName().equals("RouteName") || myParser.getName().equals("ConsignName") || myParser.getName().equals("PhoneNo") || myParser.getName().equals("Area") || myParser.getName().equals("Company") || myParser.getName().equals("CivilId") || myParser.getName().equals("Serial") || myParser.getName().equals("CardType") || myParser.getName().equals("DelDate") || myParser.getName().equals("DelTime") || myParser.getName().equals("Amount") || myParser.getName().equals("Attempt") || myParser.getName().equals("Address") || myParser.getName().equals("ErrMsg"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    }
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("WayBill"))
                        ResultData[DataCounter].WayBill = myParser.getText();
                    if(TagFlag.equals("RouteName"))
                        ResultData[DataCounter].RouteName = myParser.getText();
                    if(TagFlag.equals("ConsignName"))
                        ResultData[DataCounter].ConsignName = myParser.getText();
                    if(TagFlag.equals("PhoneNo"))
                        ResultData[DataCounter].PhoneNo = myParser.getText();
                    if(TagFlag.equals("Area"))
                        ResultData[DataCounter].Area = myParser.getText();
                    if(TagFlag.equals("Company"))
                        ResultData[DataCounter].Company = myParser.getText();
                    if(TagFlag.equals("CivilId"))
                        ResultData[DataCounter].CivilId = myParser.getText();
                    if(TagFlag.equals("Serial"))
                        ResultData[DataCounter].Serial = myParser.getText();
                    if(TagFlag.equals("CardType "))
                        ResultData[DataCounter].CardType = myParser.getText();
                    if(TagFlag.equals("DelDate"))
                        ResultData[DataCounter].DelDate = myParser.getText();
                    if(TagFlag.equals("DelTime"))
                        ResultData[DataCounter].DelTime = myParser.getText();
                    if(TagFlag.equals("Amount"))
                        ResultData[DataCounter].Amount = myParser.getText();
                    if(TagFlag.equals("Attempt"))
                        ResultData[DataCounter].Attempt = myParser.getText();
                    if(TagFlag.equals("Address"))
                        ResultData[DataCounter].Address = myParser.getText();
                    if(TagFlag.equals("ErrMsg"))
                        ResultData[DataCounter].ErrMsg = myParser.getText();
                }

                if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("WayBill_Info"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static PickUpWaybillsDT[] getpickupwaybilldtParser(String response) {

        PickUpWaybillsDT[] ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("PICKUPAWBS"))
                        TagCount += 1;
                event = myParser.nextToken();
            }

            ResultData = new PickUpWaybillsDT[TagCount];
            String TagFlag="";

            for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new PickUpWaybillsDT();
            }

            int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);

            myParser.setInput(new StringReader(response));
            event = myParser.getEventType();
            //      System.out.println("Event Type Last : " + event);
            //      System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("PICKUPNO") || myParser.getName().equals("WAYBILL") || myParser.getName().equals("PAYTYPE") || myParser.getName().equals("AMOUNT") || myParser.getName().equals("SERVICE"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    }
                    else
                        TagFlag = "";
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("PICKUPNO"))
                        ResultData[DataCounter].PICKUPNO = myParser.getText();
                    if(TagFlag.equals("WAYBILL"))
                        ResultData[DataCounter].WAYBILL = myParser.getText();
                    if(TagFlag.equals("PAYTYPE"))
                        ResultData[DataCounter].PAYTYPE = myParser.getText();
                    if(TagFlag.equals("AMOUNT"))
                        ResultData[DataCounter].AMOUNT = myParser.getText();
                    if(TagFlag.equals("SERVICE"))
                        ResultData[DataCounter].SERVICE = myParser.getText();

                }

                if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("PICKUPAWBS"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static String setservicestatusParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    //   System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
    public static Remarks[] pickupremarksParser(String response) {

        Remarks[] ResultData=null;
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser
            myParser.setInput(new StringReader(response.toString()));
            //XmlPullParser myParser2 = myParser;

            int event = myParser.getEventType();
            int TagCount=0;

            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("InitWhile/NextTag : " + myParser.getName());
                if(event == XmlPullParser.END_TAG)
                    if(myParser.getName().equals("PICKUP_REMARK"))
                        TagCount += 1;
                event = myParser.nextToken();
            }

            ResultData = new Remarks[TagCount];
            String TagFlag="";

            for (int i = 0; i < ResultData.length; i++) {
                ResultData[i]=new Remarks();
            }

            int DataCounter = 0;
            System.out.println("TagCount : " + TagCount);

            myParser.setInput(new StringReader(response));
            event = myParser.getEventType();
        //    System.out.println("Event Type Last : " + event);
         //   System.out.println("Event Type EndDocument : " + XmlPullParser.END_DOCUMENT);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                //System.out.println("Parser Depth : " + myParser.getDepth());
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("REMARKCODE") || myParser.getName().equals("REMARKDESC"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    }
                    else
                        TagFlag = "";
                }

                if(event == XmlPullParser.TEXT)
                {
                    //System.out.println("Tag Inner Text : " + myParser.getText());
                    //Log.e("DataCounter", String.valueOf(DataCounter));
                    if(TagFlag.equals("REMARKCODE"))
                        ResultData[DataCounter].REMARKCODE = myParser.getText();
                    if(TagFlag.equals("REMARKDESC"))
                        ResultData[DataCounter].REMARKDESC = myParser.getText();
                }

                if(event == XmlPullParser.END_TAG)
                {
                    if(myParser.getName().equals("PICKUP_REMARK"))
                        DataCounter += 1;
                    //.e("XML Tag/End", String.valueOf(DataCounter));
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParserArray", "Error Printed Down");
            e.printStackTrace();
        }
        return ResultData;
    }
    public static OpenRst openrstParser(String response) {
        OpenRst RstResp = new OpenRst();
        String TagFlag="";
        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();



            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {
                if(event == XmlPullParser.START_TAG)
                {
                    if(myParser.getName().equals("RSTNO") || myParser.getName().equals("ACKNO")||myParser.getName().equals("RTNO")||myParser.getName().equals("ERRMSG"))
                    {
                        TagFlag = myParser.getName();
                        //Log.e("XML Tag/Start",TagFlag);
                    }
                    else
                        TagFlag = "";
                }

                if(event == XmlPullParser.TEXT)
                {
                    if(TagFlag.equals("RSTNO"))
                        RstResp.RSTNO = myParser.getText();
                    if(TagFlag.equals("ACKNO"))
                        RstResp.ACKNO = myParser.getText();
                    if(TagFlag.equals("RTNO"))
                        RstResp.RTNO = myParser.getText();
                    if(TagFlag.equals("ERRMSG"))
                        RstResp.ERRMSG=myParser.getText();
                    System.out.println("Response in openRst is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return RstResp;
    }
    public static String setpickupfinishParser(String response) {

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            //Xml Parser

            //     System.out.println("Inputstream is"+is);
            myParser.setInput(new StringReader(response.toString()));

            int event = myParser.getEventType();
            //     System.out.println("Value in event is"+event);
            while (event != XmlPullParser.END_DOCUMENT)
            {

                if(event == XmlPullParser.TEXT)
                {
                    response = myParser.getText();
                    System.out.println("Response in setpickupfinish is"+response);
                }

                event = myParser.next();
            }

        }
        catch (Exception e)
        {
            Log.e("XMLParser", "Error Printed Down");
            e.printStackTrace();
        }
        return response;
    }
}
