package DatabaseHandlers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.postaplus.postascannerapp.DatabaseHandler;

import DatabaseHandlers.DBFCS.TBLogin;
import DatabaseHandlers.DBFCS.TBRoutes;

/**
 * Created by muhammedsh on 23 - Sep - 2016.
 */
public class DBFunctions {

    static DatabaseHandler db;
    static SQLiteDatabase sqldb = null;

    //Get Methods

    //Get Method for Table Login Active User
    public static TBLogin GetLoggedUser(Context mcontext){

        TBLogin ResultData = new TBLogin();

        try{
            db = new DatabaseHandler(mcontext);
            //open localdatabase in a read mode
            sqldb = db.getReadableDatabase();

            //select  loginstatus and routecode on resume activity
            Cursor c = sqldb.rawQuery("SELECT * FROM logindata WHERE Loginstatus=1", null);
            int count1 = c.getCount();
            if (count1 > 0) {
                c.moveToFirst();
                ResultData.USER_NAME = c.getString(c.getColumnIndex("Username"));
                ResultData.LOGIN_STATUS = c.getInt(c.getColumnIndex("Loginstatus"));
                ResultData.ROUTE_CODE = c.getString(c.getColumnIndex("Routecode"));
                ResultData.RUNSHEET_CODE = c.getString(c.getColumnIndex("Runsheetcode"));
                ResultData.ODOMETER_VALUE = c.getString(c.getColumnIndex("Odometervalue"));
                ResultData.ODOMETER_VALUE_END = c.getString(c.getColumnIndex("EndOdometervalue"));
                ResultData.ODOMETER_id = c.getString(c.getColumnIndex("Odometerid"));
                ResultData.ODOMETER_FileNo = c.getString(c.getColumnIndex("OdometerFileno"));
                ResultData.ODOMETER_SyncStatus = c.getString(c.getColumnIndex("OdometerimgSyncstatus"));
                c.close();
                return ResultData;
            }
            c.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return null;
    }

    //Get Method for Table Route Data
    public static TBRoutes[] GetRoutes(Context mcontext){
        TBRoutes[] ResultData = null;
        db = new DatabaseHandler(mcontext);
        try {
          //  db = new DatabaseHandler(mcontext);
            //open localdatabase in a read mode
            sqldb = db.getReadableDatabase();

            //To get all routes
            Cursor c = sqldb.rawQuery("SELECT * FROM routedata order by ROUTECODE", null);
            int count1 = c.getCount();

            if(count1>0){
                ResultData = new TBRoutes[count1];
                for(int i=0;i<count1;i++){
                    ResultData[i].ROUTE_CODE = c.getString(c.getColumnIndex("ROUTECODE"));
                    ResultData[i].ROUTE_NAME = c.getString(c.getColumnIndex("ROUTENAME"));
                }
            }
            c.close();
            return ResultData;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            db.close();
        }
        return null;
    }

    //Get Method for Table Route Data for a Route
    public static TBRoutes GetRoutes(Context mcontext, String CodeRoute){
        TBRoutes ResultData = new TBRoutes();
        db = new DatabaseHandler(mcontext);
        try {
        //    db = new DatabaseHandler(mcontext);
            //open localdatabase in a read mode
            sqldb = db.getReadableDatabase();

            //To get all routes
            Cursor c = sqldb.rawQuery("SELECT * FROM routedata WHERE ROUTECODE = '"+ CodeRoute +"'", null);
            int count1 = c.getCount();
            c.moveToFirst();
            if(count1>0){
                ResultData.ROUTE_CODE = c.getString(c.getColumnIndex("ROUTECODE"));
                ResultData.ROUTE_NAME = c.getString(c.getColumnIndex("ROUTENAME"));
            }
            c.close();
            return ResultData;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            db.close();
           /* if (sqldb != null && sqldb.isOpen()) {
                sqldb.close();
                // db.close();
            }*/
        }
        return null;
    }


    //SET Functions

    //SET Method for Table Route Data
    public static boolean SetRoutes(Context mcontext, String CodeRoute, String RouteName){
        try{
            db = new DatabaseHandler(mcontext);
            //open localdatabase in a write mode
            sqldb = db.getWritableDatabase();

            String sql = "INSERT or replace INTO routedata (ROUTECODE, ROUTENAME) "

                    + "VALUES (" + CodeRoute + ", '"
                    + RouteName + "')";
            sqldb.execSQL(sql);

            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally {
            db.close();
        }
    }

    //UPDATE Functions

    //Update Method for Table Route Data
    public static boolean UpdateRoutes(Context mcontext, String CodeRoute, String RouteName){
        try{
            db = new DatabaseHandler(mcontext);
            //open localdatabase in a write mode
            sqldb = db.getWritableDatabase();

            sqldb.execSQL("UPDATE routedata SET ROUTENAME='" + RouteName + "' WHERE ROUTECODE='" + CodeRoute + "'");
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally {
            db.close();
        }
    }

}
