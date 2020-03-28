package com.postaplus.postascannerapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//local database
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
   public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "Ops.db";

    //logindata table name
    public static final String TABLE_NAME1 = "logindata";
      

    //deliverydata table name
    public static final String TABLE_NAME2 = "deliverydata";
    
    //eventdata table name
    public static final String TABLE_NAME3 = "eventdata";
    
    //wbill-delivery table name
    public static final String TABLE_NAME4 = "wbilldata";
   
    //wbill-images delivery table name
    public static final String TABLE_NAME5 = "wbillimagesdata";
    
    //routedata table name
    public static final String TABLE_NAME6 = "routedata";
    
  //pickup_notification data table name
    public static final String TABLE_NAME7 = "pickuphead";
    
    //pickup_detail data table name
    public static final String TABLE_NAME8 = "pickupdetails";
    
    //service data table name
    public static final String TABLE_NAME9 = "servicedetails";
    
    //paytype data table name
    public static final String TABLE_NAME10 = "paytypedetails";
    
    //courierlist data table name
    public static final String TABLE_NAME11 = "courierdetails";
    
    //Holdwaybilldata data table name
    public static final String TABLE_NAME12 = "Holdwaybilldata";
    
    public static final String TABLE_NAME13 = "Fueldatatable";
   
    public static final String TABLE_NAME14 = "TransferAcceptTemp";

    //Pickup Remarks
    public static final String TABLE_NAME15 = "pickupRemarks";

    
    // login Table Columns names
    private static final String USER_NAME = "Username";
    private static final String LOGIN_STATUS = "Loginstatus";
    private static final String ROUTE_CODE = "Routecode";
    private static final String RUNSHEET_CODE = "Runsheetcode";
    private static final String ODOMETER_VALUE = "Odometervalue";
    private static final String ODOMETER_VALUE_END = "EndOdometervalue";
    private static final String ODOMETER_FileNo = "OdometerFileno";
    private static final String ODOMETER_id = "Odometerid";
    private static final String ODOMETER_SyncStatus = "OdometerimgSyncstatus";
    
    
    //delivery table Columns names
    private static final String DRIVER_CODE = "Drivercode";
    private static final String ROUTECODE = "Routecode";
    private static final String WAY_BILL = "Waybill";
    private static final String CONSIGNEE = "Consignee";
    private static final String TELEPHONE_NUMBER = "Telephone";
    private static final String AREA = "Area";
    private static final String COMPANY = "Company";
    private static final String CIVIL_ID = "CivilID";
    private static final String SERIAL = "Serial";
    private static final String CARDTYPE = "CardType";
    private static final String DELDATE = "DeliveryDate";
    private static final String DELTIME = "DeliveryTime";
    private static final String AMOUNT1 = "Amount";
    private static final String CALL_STATUS = "Callstatus";
    private static final String LATITUDE = "Latitude";
    private static final String LONGTITUDE = "Longtitude";
    private static final String STOP_DELIVERY="StopDelivery";
    private static final String WC_STATUS="WC_Status";
    private static final String TRANSDRIVER_CODE="Transdriver_Code";
    private static final String WC_TRANFER_STATUS="WC_Transfer_Status";
    private static final String TRANFERSTATUS="TransferStatus";
    private static final String ATTEMPT_STATUS="Attempt_Status";
    private static final String ADDRESS="Address";
    private static final String APPROVALSTATUS="ApprovalStatus";
    private static final String SYNC_CALL_STATUS = "SyncCallstatus";
    private static final String CALL_DATE_TIME = "CallTime";
    private static final String SHIPPER_NAME = "ShipperName";
    private static final String AWB_IDENTITY = "AWBIdentifier";
    private static final String ISCOLLECTED_COD = "IsCollectedCOD";
    private static final String LAST_STATUS = "Last_Status";
    private static final String BARCODE_IDENTIFIER = "BarcodeIdentifier";
   // private static final String TAG_VALUE = "Tagvalue";
    
    //hold table Columns names
    private static final String DRIVER_CODE_1 = "Drivercode";
    private static final String ROUTECODE_1 = "Routecode";
    private static final String TRANSDRVR_CODE ="Transdriver_Code";
    private static final String WAY_BILL_1 = "Waybill";
    private static final String CONSIGNEE_1 = "Consignee";
    private static final String TELEPHONE_NUMBER_1 = "Telephone";
    private static final String AREA_1 = "Area";
    private static final String COMPANY_1 = "Company";
    private static final String CIVIL_ID_1 = "CivilID";
    private static final String SERIAL_1 = "Serial";
    private static final String CARDTYPE_1 = "CardType";
    private static final String DELDATE_1 = "DeliveryDate";
    private static final String DELTIME_1 = "DeliveryTime";
    private static final String AMOUNT1_1 = "Amount";
    private static final String HOLD_TRANSFER_STATUS="HOLD_Transfer_Status";
    private static final String TRANSFER_STATUS_1="TransferStatus";
    private static final String ADDRESS1="Address";
    //route table Columns names
    private static final String ROUTE_CODE1 = "ROUTECODE";
    private static final String ROUTE_NAME = "ROUTENAME";
    
  //courier table Columns names
    private static final String DRIVER_CODE11 = "DRIVERCODE";
    private static final String DRIVER_NAME = "DRIVERNAME";
    
    //event table column names
    private static final String EVENT_CODE = "EVENTCODE";
    private static final String EVENT_DESC= "EVENTDESC";
    
    //waybill table column names
    private static final String ID = "ID";
    private static final String DRIVER_CODE1 = "Drivercode";
    private static final String WAY_BILL1 = "Waybill";
    private static final String RUNSHEET_CODE11 = "Runsheetcode";
    private static final String EVENT_CODE1 = "Eventcode";    
    private static final String LATITUDE1 = "Latitude";
    private static final String LONGTITUDE1 = "Longtitude";
    private static final String DATE_TIME_1= "Date_Time";  
    private static final String TRANSFER_STATUS="TransferStatus";
    private static final String RECIVER_NAME="Reciever_Name";
    private static final String EVENT_NOTE="Event_note";
    
    //waybill image table column names
    private static final String DRIVER_CODE2 = "Drivercode";
    private static final String WAY_BILL11 = "Waybill";
    private static final String IMAGE_FILENAME = "Image_filename";
    private static final String TRANSFER_STATUS1="TransferStatus";
    
    //notification table column names
    private static final String DRIVER_CODE3 = "Drivercode";
    private static final String PICK_NUMBER = "Pickup_No";
    private static final String IDENTIFIER = "Pickup_Type";
    private static final String PICKUP_TIME = "Pickup_Time";
    private static final String PICKUP_PHONE = "Pickup_Phone";
    private static final String ACCOUNT_NAME = "Account_Name";
    private static final String PICK_ADDRESS = "Pick_Address";
    private static final String PICKUP_AREA = "Pickup_Area";
    private static final String CONTACT_PERSON = "Contact_Person";
    private static final String DATE_TIME = "Accept_Date_Time";
    private static final String STATUS = "Status";
    private static final String TRANSFER_STATUS2="TransferStatus";
    private static final String PICKUP_DATETIME="Pickup_Date_Time";
    private static final String LATITUDE2 = "Latitude";
    private static final String LONGTITUDE2 = "Longtitude";
    private static final String CONSIGNEE_NAME = "ConsigneeName";
    private static final String DEL_ADD = "DeliveryAddress";
    private static final String DEL_CITY = "DeliveryCity";
    private static final String DEL_PHONE = "DeliveryPhone";
    private static final String CODE_REMARK = "CodeRemark";
    private static final String ATTEMPT_DATETIME = "AttemptDatetime";
    
    //pickupupdate table column names
    private static final String DRIVER_CODE4 = "Driver_Code";
    private static final String PICK_NUMBER1 = "Pickup_No";
    private static final String WAYBILL1 = "Waybill_Number";
    private static final String PAYTYPE = "PayType";
    private static final String AMOUNT = "Amount";
    private static final String SERVICETYPE = "ServiceType";
    private static final String DATE_TIME1 = "Date_Time";
    private static final String TAGG_VAL = "Tag_value";
    
    //service table Columns names
    private static final String SERVICEID = "ServiceID";
    private static final String SERVICETYPE1 = "ServiceTYPE";
    
    //paytype table Columns names
    private static final String PAYID = "PayID";
    private static final String PAYTYPE1 = "PayTYPE";
    
    //fuel datatable values
    private static final String DATE_TIME12 = "DATE_TIME";
    private static final String DRIVERCODE = "DRIVERCODE";
    private static final String VBarcode = "VehicleBarcode";
    private static final String ODOMETER_VALUE1 = "Odometer_Value";
    private static final String ODOMETER_PICTURE = "Odometer_picname";
    private static final String RECEIPT_PIC = "Receipt_pic";
    private static final String RECEIPT_NO = "Reciept_no";
    private static final String RECEIPT_AMOUNT = "Reciept_Amount";    
    private static final String LATITUDE12 = "Latitude";
    private static final String LONGTITUDE12 = "Longtitude";
    private static final String SYNC_STATUS = "SyncStatus";
    private static final String Image_SyncStatus = "ImageSyncstatus";
    private static final String Fuel_id = "Fuelid";

    
    //transfer accept temp table Columns names
    private static final String DRIVER_CODEtemp = "Drivercode";
    private static final String ROUTECODEtemp = "Routecode";
    private static final String WAY_BILLtemp = "Waybill";
    private static final String CONSIGNEEtemp = "Consignee";
    private static final String TELEPHONE_NUMBERtemp = "Telephone";
    private static final String AREAtemp = "Area";
    private static final String COMPANYtemp = "Company";
    private static final String CIVIL_IDtemp = "CivilID";
    private static final String SERIALtemp = "Serial";
    private static final String CARDTYPEtemp = "CardType";
    private static final String DELDATEtemp = "DeliveryDate";
    private static final String DELTIMEtemp = "DeliveryTime";
    private static final String AMOUNT1temp = "Amount";
    private static final String ADDRESS1temp="Address";
    private static final String ATTEMPTtemp="Attempt_Status";
    private static final String Transfer_StatusTemp="Transfer_Status";

    //pickupRemarks table column names
    private static final String REMARKCODE = "RemarkCode";
    private static final String REMARKDESC= "RemarkDesc";
    
    public DatabaseHandler(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);

	
    }

   
    @Override
    public void onCreate(SQLiteDatabase db) {
	
    	// Creating Tables for login
    String CREATE_LOGIN_TABLE = "CREATE TABLE " +  TABLE_NAME1 + "("
		+ USER_NAME + " TEXT," + LOGIN_STATUS + " INTEGER,"
		+ ROUTE_CODE + " TEXT," + RUNSHEET_CODE + " TEXT," + ODOMETER_VALUE + " TEXT,"+ODOMETER_VALUE_END + " TEXT,"+ ODOMETER_FileNo + " TEXT,"+ ODOMETER_id + " TEXT,"+ ODOMETER_SyncStatus + " INTEGER" +")";
	db.execSQL(CREATE_LOGIN_TABLE);
   
	 // Creating Tables for delivery
	String CREATE_DELIVERY_TABLE = "CREATE TABLE " + TABLE_NAME2 + "("
			+ DRIVER_CODE + " TEXT," + ROUTECODE + " TEXT,"
			+ WAY_BILL + " TEXT," +CONSIGNEE + " TEXT," +TELEPHONE_NUMBER + " TEXT," 
			+ AREA + " TEXT," + COMPANY + " TEXT," + CIVIL_ID + " TEXT,"+ SERIAL + " TEXT," + CARDTYPE + " TEXT," + DELDATE + " TEXT,"
			+ DELTIME + " TEXT," + AMOUNT1 + " TEXT," 
			+CALL_STATUS + " TEXT," +LATITUDE + " TEXT,"+LONGTITUDE + " TEXT,"+STOP_DELIVERY +" INTEGER,"+WC_STATUS + " TEXT,"+TRANSDRIVER_CODE+" TEXT,"+WC_TRANFER_STATUS + " INTEGER,"+TRANFERSTATUS + " INTEGER,"+ATTEMPT_STATUS + " INTEGER,"+ADDRESS+" TEXT,"+APPROVALSTATUS+" TEXT,"+
			SYNC_CALL_STATUS+" INTEGER,"+CALL_DATE_TIME+ " TEXT, "
            +SHIPPER_NAME+ " TEXT,"
            +AWB_IDENTITY+" TEXT,"
            +ISCOLLECTED_COD+" TEXT,"
            +LAST_STATUS+" TEXT,"
            +BARCODE_IDENTIFIER+" TEXT"
            +")";
		db.execSQL(CREATE_DELIVERY_TABLE);

      // tag val in db  +TAG_VALUE+" TEXT"
		
		// Creating Tables for delivery
		String CREATE_DELIVERYACCEPT_TABLE = "CREATE TABLE " + TABLE_NAME14 + "("
				+ DRIVER_CODEtemp + " TEXT," + ROUTECODEtemp + " TEXT,"
				+ WAY_BILLtemp + " TEXT," +CONSIGNEEtemp + " TEXT," +TELEPHONE_NUMBERtemp + " TEXT," 
				+ AREAtemp + " TEXT," + COMPANYtemp + " TEXT," + CIVIL_IDtemp + " TEXT,"+ SERIALtemp + " TEXT," + CARDTYPEtemp + " TEXT," + DELDATEtemp + " TEXT,"
				+ DELTIMEtemp + " TEXT," + AMOUNT1temp + " TEXT,"+Transfer_StatusTemp+ " TEXT,"+ADDRESS1temp+" TEXT,"+ATTEMPTtemp +" TEXT"+")";
			db.execSQL(CREATE_DELIVERYACCEPT_TABLE);
		
		// Creating Tables for hold
		String CREATE_HOLD_TABLE = "CREATE TABLE " + TABLE_NAME12 + "("
				+ DRIVER_CODE_1 + " TEXT," + ROUTECODE_1 + " TEXT,"
				+ TRANSDRVR_CODE + " TEXT," +WAY_BILL_1 + " TEXT," +CONSIGNEE_1 + " TEXT," 
				+ TELEPHONE_NUMBER_1 + " TEXT," + AREA_1 + " TEXT," + COMPANY_1 + " TEXT,"+CIVIL_ID_1+ " TEXT,"+ SERIAL_1 + " TEXT," + CARDTYPE_1 + " TEXT," + DELDATE_1 + " TEXT,"
				+ DELTIME_1 + " TEXT," + AMOUNT1_1 + " TEXT," +HOLD_TRANSFER_STATUS +" INTEGER,"+TRANSFER_STATUS_1 + " INTEGER, "+ADDRESS1+" TEXT"+")";
			db.execSQL(CREATE_HOLD_TABLE);
	
		//Create table for route
	String CREATE_ROUTE_TABLE = "CREATE TABLE " + TABLE_NAME6 + "("
				+ ROUTE_CODE1 + " TEXT," + ROUTE_NAME + " TEXT"+")";
	db.execSQL(CREATE_ROUTE_TABLE);

	//Create table for courier
		String CREATE_COURIER_TABLE = "CREATE TABLE " + TABLE_NAME11 + "("
					+ DRIVER_CODE11 + " TEXT," + DRIVER_NAME + " TEXT"+")";
		db.execSQL(CREATE_COURIER_TABLE);
	
	//event_table
	String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_NAME3 + "("
			+ EVENT_CODE + " TEXT," + EVENT_DESC + " TEXT"+")";
	db.execSQL(CREATE_EVENT_TABLE);

	//waybill event
	String CREATE_WAYBILL_TABLE = "CREATE TABLE " + TABLE_NAME4 + "("+ID+ " TEXT,"+ DRIVER_CODE1 + " TEXT,"
			+ WAY_BILL1 + " TEXT," + RUNSHEET_CODE11 + " TEXT,"+ EVENT_CODE1 + " TEXT," +LATITUDE1 + " TEXT,"+LONGTITUDE1 + " TEXT,"+DATE_TIME_1 + " TEXT," +TRANSFER_STATUS +" INTEGER," +RECIVER_NAME+" TEXT,"+EVENT_NOTE+" TEXT"+")";
	db.execSQL(CREATE_WAYBILL_TABLE);

	//waybill image
	String CREATE_WAYBILL_IMG_TABLE = "CREATE TABLE " + TABLE_NAME5 + "("+ DRIVER_CODE2 + " TEXT," 
			+ WAY_BILL11 + " TEXT," + IMAGE_FILENAME + " TEXT," +TRANSFER_STATUS1 +" INTEGER"+")";
	db.execSQL(CREATE_WAYBILL_IMG_TABLE);
	
	// Creating Tables for picupnotification
        String CREATE_PICK_NOTIF_TABLE = "CREATE TABLE " + TABLE_NAME7 + "("+ DRIVER_CODE3 + " TEXT,"
                + PICK_NUMBER + " TEXT," + IDENTIFIER + " TEXT," + PICKUP_TIME + " TEXT,"
                + PICKUP_PHONE + " TEXT," + ACCOUNT_NAME  + " TEXT," +PICK_ADDRESS + " TEXT," +PICKUP_AREA + " TEXT,"
                + CONTACT_PERSON + " TEXT," + DATE_TIME + " TEXT,"+ STATUS +" TEXT," +TRANSFER_STATUS2 + " INTEGER,"+ PICKUP_DATETIME +" TEXT," +
                "" +LATITUDE2 +" TEXT,"+LONGTITUDE2 +" TEXT,"+CONSIGNEE_NAME +" TEXT,"+ DEL_ADD +" TEXT,"+ DEL_CITY +" TEXT,"+ DEL_PHONE +" TEXT,"+ CODE_REMARK +" TEXT,"+ ATTEMPT_DATETIME +" TEXT"+")";
  	/*String CREATE_PICK_NOTIF_TABLE = "CREATE TABLE " + TABLE_NAME7 + "("+ DRIVER_CODE3 + " TEXT,"
				+ PICK_NUMBER + " TEXT," + ACCOUNT_NAME + " TEXT,"
				+ PICK_ADDRESS + " TEXT," + PICKUP_AREA + " TEXT," +CONTACT_PERSON + " TEXT," +PICKUP_PHONE + " TEXT," 
				+ PICKUP_TIME + " TEXT," + DATE_TIME + " TEXT,"+ STATUS +" TEXT," +TRANSFER_STATUS2 + " INTEGER,"+ PICKUP_DATETIME +" TEXT,"
            +LATITUDE2 +" TEXT,"+LONGTITUDE2 +" TEXT, "+")";*/
	db.execSQL(CREATE_PICK_NOTIF_TABLE);
	
	// Creating Tables for pickup_detail
	String CREATE_PICK_UPDATE_TABLE = "CREATE TABLE " + TABLE_NAME8 + "("
					+ DRIVER_CODE4 + " TEXT," + PICK_NUMBER1 + " TEXT,"
					+ WAYBILL1 + " TEXT," + PAYTYPE + " TEXT," +AMOUNT + " TEXT," +SERVICETYPE + " TEXT," 
					+ DATE_TIME1 + " TEXT," +TAGG_VAL + " TEXT" +")";
	db.execSQL(CREATE_PICK_UPDATE_TABLE);
    
	//event_table
	String CREATE_SERVICE_TABLE = "CREATE TABLE " + TABLE_NAME9 + "("
						+ SERVICEID + " TEXT," + SERVICETYPE1 + " TEXT"+")";
	db.execSQL(CREATE_SERVICE_TABLE);
    
	//event_table
		String CREATE_PAYTYPE_TABLE = "CREATE TABLE " + TABLE_NAME10 + "("
				+ PAYID + " TEXT," + PAYTYPE1 + " TEXT"+")";
		db.execSQL(CREATE_PAYTYPE_TABLE);
    
		// Creating Tables for fueldata
		String CREATE_FUELDATA_TABLE = "CREATE TABLE " + TABLE_NAME13 + "("
						+ DATE_TIME12 + " TEXT," +  DRIVERCODE  + " TEXT,"
						+ VBarcode  + " TEXT," + ODOMETER_VALUE1  + " TEXT," + ODOMETER_PICTURE  + " TEXT," +RECEIPT_PIC + " TEXT," 
						+ RECEIPT_NO + " TEXT," +RECEIPT_AMOUNT + " TEXT," + LATITUDE12 + " TEXT," +LONGTITUDE12  + " TEXT," +SYNC_STATUS +" INTEGER,"+ Fuel_id + " TEXT, " +Image_SyncStatus +" INTEGER"+")";
		db.execSQL(CREATE_FUELDATA_TABLE);

        //event_table
        String CREATE_PICKUPREMARKS_TABLE = "CREATE TABLE " + TABLE_NAME15 + "("
                + REMARKCODE + " TEXT," + REMARKDESC + " TEXT"+")";
        db.execSQL(CREATE_PICKUPREMARKS_TABLE);
		
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME5);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME6);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME7);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME8);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME9);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME10);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME11);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME12);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME13);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME14);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME15);

        // Create tables again
        onCreate(db);
	
    }
   

  

    
    }
