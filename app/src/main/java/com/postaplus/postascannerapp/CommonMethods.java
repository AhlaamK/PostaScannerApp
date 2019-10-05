package com.postaplus.postascannerapp;

import android.content.Context;
import android.util.Log;

import DatabaseHandlers.DBFCS.TBRoutes;
import DatabaseHandlers.DBFunctions;
import webservice.FuncClasses.Routes;
import webservice.WebService;

/**
 * Created by muhammedsh on 23 - Sep - 2016.
 */
public class CommonMethods {

    public static void getroutes(Context mcontext, String ActiveUser) {
        try {
            //TBLogin ActiveLogin = DBFunctions.GetLoggedUser(mcontext);

            Routes[] routesresponse = WebService.GET_ROUTES(ActiveUser);
            for (Routes routesOb : routesresponse) {

                TBRoutes RouteData = DBFunctions.GetRoutes(mcontext, routesOb.RouteCode);
                if (RouteData.ROUTE_CODE != null) {
                   // Log.e("CommonMethd/GetRoute","Route: " +routesOb.RouteCode + " Updated!");
                    DBFunctions.UpdateRoutes(mcontext,RouteData.ROUTE_CODE,RouteData.ROUTE_NAME);
                } else {
                    //Log.e("CommonMethd/GetRoute","Route: " +routesOb.RouteCode + " Inserted!");
                   DBFunctions.SetRoutes(mcontext,routesOb.RouteCode,routesOb.RouteName);
                }
            }
            Log.e("CommonMethd","GetRoute Success!");
        } catch (Exception e) {
            Log.e("GetRoutes:", "Get Routes in commonmethods is errored");
            e.printStackTrace();
        }
    }
}
