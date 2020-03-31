package webservice;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import webservice.JsonFuncClasses.Couriers;
import webservice.JsonFuncClasses.Events;
import webservice.JsonFuncClasses.HoldWayBills;
import webservice.JsonFuncClasses.JsonRequests.SET_ODO_FUEL_IMAGE_Request;
import webservice.JsonFuncClasses.JsonRequests.SET_PICKUPDETAILS_Request;
import webservice.JsonFuncClasses.JsonRequests.SET_WAYBILLACK_IMG_Request;
import webservice.JsonFuncClasses.JsonResponses.GET_COURIERSResponse;
import webservice.JsonFuncClasses.JsonResponses.GET_EVENTSResponse;
import webservice.JsonFuncClasses.JsonResponses.GET_HOLDWAYBILLSResponse;
import webservice.JsonFuncClasses.JsonResponses.GET_HOLDWAYBILLS_PICKUP_Response;
import webservice.JsonFuncClasses.JsonResponses.GET_PAYTYPEResponse;
import webservice.JsonFuncClasses.JsonResponses.GET_PICKUPResponse;
import webservice.JsonFuncClasses.JsonResponses.GET_PICKUP_DTResponse;
import webservice.JsonFuncClasses.JsonResponses.GET_PICKUP_REMARKResponse;
import webservice.JsonFuncClasses.JsonResponses.GET_PICKUP_WAYBILLS_DTResponse;
import webservice.JsonFuncClasses.JsonResponses.GET_ROUTESResponse;
import webservice.JsonFuncClasses.JsonResponses.GET_RSTDETAILResponse;
import webservice.JsonFuncClasses.JsonResponses.GET_SCAN_WAYBILL_DTResponse;
import webservice.JsonFuncClasses.JsonResponses.GET_SERVICEResponse;
import webservice.JsonFuncClasses.PayType;
import webservice.JsonFuncClasses.PickUp;
import webservice.JsonFuncClasses.PickUpDt;
import webservice.JsonFuncClasses.PickUpWaybillsDT;
import webservice.JsonFuncClasses.PickupHoldwaybills;
import webservice.JsonFuncClasses.Remarks;
import webservice.JsonFuncClasses.Routes;
import webservice.JsonFuncClasses.RstDetail;
import webservice.JsonFuncClasses.ScanWaybillDt;
import webservice.JsonFuncClasses.Service;

import static com.postaplus.postascannerapp.ScreenActivity.url;

/**
 * Created by ahlaam.kazi on 10/2/2017.
 */

public class JsonService {

    // Local/test service
//  private servc
// public static String url = "http://172.53.1.34/OpsCourierScannerService/OpsGCScanSrv.svc/";
    //public servc
    //  public static String url = "http://168.187.136.18/OpsCourierScannerService/OpsGCScanSrv.svc/";

    // Stag service
 //  public static String url = "http://172.53.1.34/OpsCourierScannerServiceStag/OpsGCScanSrv.svc/";

    // LIVE NEW SERVICE
   // public static  String url ="http://postascan.postaplus.com/ServiceV2_0/OpsGCScanSrv.svc/";

    public static Object jsonreq(String jsonparams, Class responseclasstype) {
        System.out.println("JsonService called");

        String WebServUrl = "";
        //URL url;
        HttpURLConnection connection = null;
        // TeST URL

        //LogStatus ResponseJson;
        // LogStatus ResponseJson;

        try {
            //Create connection
            StringBuilder text = new StringBuilder();
// Test URL
           // System.out.println("url  jsonservc "+url);
             String url1 = url + jsonparams;
            Log.e("url1 is", url1);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            // ResponseJson=String.valueOf(restTemplate.getForObject(url1,LogStatus.class));
            return restTemplate.getForObject(url1, responseclasstype);


            // ResponseJson = restTemplate.getForObject(url1, LogStatus.class);
            //System.out.println("Result is"+ResponseJson);
            //return ResponseJson;
            //System.out.println("Result is"+Result.GetString());
            //return restTemplate;



           /* try {
                File ipaddfile = new File(Environment.getExternalStorageDirectory(), "Postaplus/Data/ipaddress.txt");
                BufferedReader br = new BufferedReader(new FileReader(ipaddfile));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    //text.append('\n');
                }
                //}

                br.close();
                WebServUrl = text.toString();
                //ipaddress = text.toString();
                //System.out.println(ipaddress);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            url = new URL(WebServUrl);
            connection = (HttpURLConnection)url.openConnection();

            Log.e("ExecutePost/Conn","Connection Pass");

            //connection.setRequestProperty("Content-Type","text/xml;charset=UTF-8");
            connection.setRequestProperty("Content-Type","application/json");
           // connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            //connection.setRequestProperty("Content-Language", "en-US");
            //connection.setRequestProperty("Connection","Close");keep-alive
            connection.setRequestProperty("Connection","keep-alive");
            // connection.setRequestProperty("Connection","Close");


                System.out.println("urlParameters less than 13000");
                connection.setConnectTimeout(10000); //set timeout to 10 seconds
                connection.setReadTimeout(20000);

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);



            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
          //  wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            Log.e("ExecutePost","Request Send");

            //Get Response
            InputStream is = null;

            int status = connection.getResponseCode();
            Log.e("ExecutePost","Response : "+ status);
            if(status >= HttpURLConnection.HTTP_OK)
                //in = connection.getErrorStream();
                is = new BufferedInputStream(connection.getInputStream());
            else
                is = new BufferedInputStream(connection.getErrorStream());

            Log.e("ExecutePost","Response is : "+ is);

            BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line;
            String response = "";
            StringBuffer response2 = new StringBuffer();
            while((line = rd.readLine()) != null) {
                //  response += line;
                response2.append(line);

            }
            //   rd.close();


            Log.e("ExecutePost/Resp",response2.toString());

            rd.close();
            return response2.toString();*/

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {
            System.out.println("json Final Block Called!");
            if (connection != null) {
                connection.disconnect();
            }
        }//return ResponseJson;
    }
    public static ResponseEntity<?> jsonpostreq(String MethodName, Map MethodArgs, Class responseclasstype) {
        System.out.println("JsonServicepost called");
        // Message result=null;
        try {


            //RestTemplate restTemplate = new RestTemplate(factory);

           RestTemplate restTemplate = new RestTemplate();
            String url1 = url + MethodName;
            Log.e("POST REQUEST CALLED",url1);

            //HttpHeaders headers = new HttpHeaders();
            //headers.setContentType(MediaType.APPLICATION_JSON);

            //String input = "{\"DRIVERCODE\":\"KWI0024\"}";

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            HttpEntity<Map<String,String>> request = new HttpEntity<Map<String,String>>(MethodArgs);
            System.out.println("request sync : "+request.getBody()+"\n"+" - heardesr: "+request.getHeaders());
            ResponseEntity<?> response = restTemplate.postForEntity( url1, request , responseclasstype);


            System.out.println("response sync :"+response.toString()+" - resp0: "+response.getBody());
            return response;
        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }
    }
    public static ResponseEntity<?> jsonpostArr(String MethodName, SET_PICKUPDETAILS_Request requestobj, Class responseclasstype) {
        System.out.println("JsonServicepost called");
        // Message result=null;
        try {


            String url1 = url + "SET_PICKUPDETAILS";
            Log.e("url1 : ", url1);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            ResponseEntity<?> response = restTemplate.postForEntity( url1,requestobj, responseclasstype);

            Log.e("Response Entity :", response.toString());

            return response;
        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }
    }
    public static ResponseEntity<?> jsonpostreqImg(String MethodName, SET_WAYBILLACK_IMG_Request requestobj, Class responseclasstype) {
        System.out.println("JsonServicepost called");
        // Message result=null;
        try {


            RestTemplate restTemplate = new RestTemplate(true);

            String url1 = url + MethodName;

         /*   SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setBufferRequestBody(false);

            RestTemplate restTemplate = new RestTemplate(factory);
*/
            //HttpHeaders headers = new HttpHeaders();
        //  headers.setContentType(MediaType.IMAGE_JPEG);
           restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
            restTemplate.getMessageConverters().add(new ResourceHttpMessageConverter());



            ResponseEntity<?> response = restTemplate.postForEntity( url1,requestobj, responseclasstype);
            Log.e("Response Entity :", response.toString());
            System.out.println("response imgsync :"+response.toString()+" - resp0: "+response.getBody());
            return response;
        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }
    }
    public static ResponseEntity<?> jsonpostreqodoImg(String MethodName, SET_ODO_FUEL_IMAGE_Request requestobj, Class responseclasstype) {
        System.out.println("JsonServicepost called");
        // Message result=null;
        try {


            RestTemplate restTemplate = new RestTemplate();

            String url1 = url + MethodName;


            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
            restTemplate.getMessageConverters().add(new ResourceHttpMessageConverter());

            ResponseEntity<?> response = restTemplate.postForEntity( url1,requestobj, responseclasstype);

            Log.e("Response Entity :", response.toString());
            System.out.println("response imgsync :"+response.toString()+" - resp0: "+response.getBody());
            return response;
        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }
    }
    public static List<Events> JsonListEvents(String jsonparams) {
        Events[] ArrayEvents=null;
        List<Events> ResponseEvents=null;
        System.out.println("JsonService called");
        try {
            final String url1 = url+ jsonparams;
            Log.e("url2 is", url1);

            JSONArray Events = null;

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ObjectMapper mapper = new ObjectMapper();
           mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
            mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
           mapper.enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
            Log.e("ResponseEntity is", mapper.getDeserializationConfig().toString());
            mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            // ResponseJson=String.valueOf(restTemplate.getForObject(url1,LogStatus.class));
            ResponseEvents = restTemplate.getForObject(url1, GET_EVENTSResponse.class).EVENTS;
            Log.e("ResponseEntity is", ResponseEvents.toString());

           // Events[] response  = restTemplate.getForObject(url1, Events[].class);
            //Log.e("ResponseEntity is", responseEntity.toString());
            Log.e("ResponseEntitySize: ", String.valueOf(ResponseEvents.size()));
           // webservice.JsonFuncClasses.Events[] ArrayEvents = new Events[ResponseEvents.size()];

            //Log.e("ArrayEvents:",ArrayEvents.toString());
if (ResponseEvents != null){
    return ResponseEvents;
}else{

    return null;
}

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ResponseEvents != null){
            return ResponseEvents;
        }else return  null;
    }
    public static List<Service> JsonListServc(String jsonparams) {
        Service[] ArrayServ=null;
        List<Service> ResponseServ=null;
        System.out.println("JsonService called");
        try {
            final String url1 = url + jsonparams;
            Log.e("url2 is", url1);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
            mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
            mapper.enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
         //   Log.e("ResponseEntity is", mapper.getDeserializationConfig().toString());
            mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

            ResponseServ = restTemplate.getForObject(url1, GET_SERVICEResponse.class).SERVICE;
            Log.e("ResponseEntity is serv", ResponseServ.toString());
            Log.e("ResponseEntitySize: ", String.valueOf(ResponseServ.size()));

            return ResponseServ;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("json FinalservBlock Called!");
            // return null;
            return ResponseServ;
        }


    }
    public static List<PayType> JsonListPayTyp(String jsonparams) {
        PayType[] ArrayPaytyp=null;
        List<PayType> ResponsePaytyp=null;
        System.out.println("JsonService called");
        try {
            final String url1 = url + jsonparams;
            Log.e("url2 is", url1);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
            mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
            mapper.enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
            //   Log.e("ResponseEntity is", mapper.getDeserializationConfig().toString());
            mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

            ResponsePaytyp = restTemplate.getForObject(url1, GET_PAYTYPEResponse.class).PAYTYPE;
            Log.e("ResponseEntity is payty", ResponsePaytyp.toString());
            Log.e("ResponseEntitySize: ", String.valueOf(ResponsePaytyp.size()));

            return ResponsePaytyp;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("json Finalpyty Block Called!");
            // return null;
            return ResponsePaytyp;
        }

    }
    public static List<Remarks> JsonListRemarks(String jsonparams) {

        List<Remarks> ResponseRemark=null;
        System.out.println("JsonService called");
        try {
            final String url1 = url + jsonparams;
            Log.e("url2 is", url1);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
            mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
            mapper.enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
            //   Log.e("ResponseEntity is", mapper.getDeserializationConfig().toString());
            mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

            ResponseRemark = restTemplate.getForObject(url1,GET_PICKUP_REMARKResponse.class).REMARKS;
            Log.e("ResponseEntity is remak", ResponseRemark.toString());
            Log.e("ResponseEntitySize: ", String.valueOf(ResponseRemark.size()));

            return ResponseRemark;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("json Finalremk Block Called!");
            // return null;
            return ResponseRemark;
        }

    }
    public static List<Routes> JsonListRoutes(String jsonparams) {

        List<Routes> ResponseRoutes=null;
        System.out.println("JsonService called");
        try {
            final String url1 = url + jsonparams;
            Log.e("url2 is", url1);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
            mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
            mapper.enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
            //   Log.e("ResponseEntity is", mapper.getDeserializationConfig().toString());
            mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

            ResponseRoutes = restTemplate.getForObject(url1,GET_ROUTESResponse.class).ROUTES;
            Log.e("ResponseEntity routes", ResponseRoutes.toString());
            Log.e("ResponseEntitySize: ", String.valueOf(ResponseRoutes.size()));

            return ResponseRoutes;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("json Finalroute Block Called!");
            // return null;
            return ResponseRoutes;
        }
}
    public static List<RstDetail> JsonListRstdetal(String jsonparams) {

        List<RstDetail> ResponseRstdet=null;
        System.out.println("JsonService called");
        try {
            final String url1 = url + jsonparams;
            Log.e("url2 is", url1);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
            mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
            mapper.enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
            //   Log.e("ResponseEntity is", mapper.getDeserializationConfig().toString());
            mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

            ResponseRstdet = restTemplate.getForObject(url1,GET_RSTDETAILResponse.class).RSTDETAIL;
            Log.e("ResponseEntity rstdet", ResponseRstdet.toString());
            Log.e("ResponseEntitySize: ", String.valueOf(ResponseRstdet.size()));

            return ResponseRstdet;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("json Finalrstdet Block Called!");
            // return null;
            return ResponseRstdet;
        }
    }
    public static List<Couriers> JsonListCourier(String jsonparams) {

        List<Couriers> ResponseCourier=null;
        System.out.println("JsonService called");
        try {
            final String url1 = url+ jsonparams;
            Log.e("url2 is", url1);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
            mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
            mapper.enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
            //   Log.e("ResponseEntity is", mapper.getDeserializationConfig().toString());
            mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

            ResponseCourier = restTemplate.getForObject(url1,GET_COURIERSResponse.class).COURIERS;
            Log.e("ResponseEntity routes", ResponseCourier.toString());
            Log.e("ResponseEntitySize: ", String.valueOf(ResponseCourier.size()));

            return ResponseCourier;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("json Finalcourre Block Called!");
            // return null;
            return ResponseCourier;
        }
    }
    public static List<HoldWayBills> JsonListHoldwaybil(String jsonparams) {

        List<HoldWayBills> Responseholdwaybil=null;
        System.out.println("JsonService called");
        try {
            final String url1 = url + jsonparams;
            Log.e("url2 is", url1);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
            mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
            mapper.enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
            //   Log.e("ResponseEntity is", mapper.getDeserializationConfig().toString());
            mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

            Responseholdwaybil = restTemplate.getForObject(url1,GET_HOLDWAYBILLSResponse.class).HOLDWAYBILLS;
            System.out.println("Responseholdwaybil is"+Responseholdwaybil);

           //Log.e("ResponseEntitySize: ", String.valueOf(Responseholdwaybil.size()));

            return Responseholdwaybil;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Responseholdwaybil;
    }
    public static List<PickUp> JsonListPickup(String jsonparams) {

        List<PickUp> Responsepickup=null;
        System.out.println("JsonService called");
        try {
            final String url1 = url + jsonparams;
            Log.e("url2 is", url1);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
            mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
            mapper.enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
            //   Log.e("ResponseEntity is", mapper.getDeserializationConfig().toString());
            mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

            Responsepickup = restTemplate.getForObject(url1,GET_PICKUPResponse.class).PICKUP;
            Log.e("ResponseEntity pickp", Responsepickup.toString());
            Log.e("ResponseEntitySize: ", String.valueOf(Responsepickup.size()));

            return Responsepickup;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("json Finalpckps Block Called!");
            // return null;
            return Responsepickup;
        }
    }
    public static List<PickUpDt> JsonListPckpDt(String jsonparams) {

        List<PickUpDt> Resppckupdt=null;
        System.out.println("JsonService called");
        try {
            final String url1 = url+ jsonparams;
            Log.e("url2 is", url1);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
            mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
            mapper.enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY);
            //   Log.e("ResponseEntity is", mapper.getDeserializationConfig().toString());
            mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

            Resppckupdt = restTemplate.getForObject(url1,GET_PICKUP_DTResponse.class).PICKupDT;
            Log.e("ResponseEntity pckpudt", Resppckupdt.toString());
            Log.e("ResponseEntitySize: ", String.valueOf(Resppckupdt.size()));

            return Resppckupdt;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("json Finalpckpdt Block Called!");
            // return null;
            return Resppckupdt;
        }
    }
    public static List<PickUpWaybillsDT> JsonListPckpwayDt(String MethodName,Map map,Class responseclasstype) {

        List<PickUpWaybillsDT> Resppckupwaydt=null;
        ResponseEntity<GET_PICKUP_WAYBILLS_DTResponse> response=null;
        System.out.println("JsonService called");
        try {
            final String url1 = url+ MethodName;
            Log.e("url2 is", url1);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            Log.e("req pckpudt", map.toString());
           // Resppckupwaydt = restTemplate.getForObject(url1,GET_PICKUP_WAYBILLS_DTResponse.class).PICKUPWAYBLLDT;
           // HttpEntity<String> request = new HttpEntity<String>(map);
          // System.out.println("request sync : "+request.getBody()+"\n"+" - heardesr: "+request.getHeaders());
            HttpEntity<Map<String,String>> request = new HttpEntity<Map<String,String>>(map);
            System.out.println("request sync : "+request.getBody()+"\n"+" - heardesr: "+request.getHeaders());
             response = restTemplate.postForEntity( url1, request , GET_PICKUP_WAYBILLS_DTResponse.class);
          //  ResponseEntity<PickUpWaybillsDT> response = restTemplate.postForEntity( url1, request ,PickUpWaybillsDT.class);

            //ResponseEntity<?> response = restTemplate.postForEntity( url1,req,GET_PICKUP_WAYBILLS_DTResponse.class);
            System.out.println("response ispckp: "+response);
            Log.e("ResponseEntity pckpudt", response.toString());
            Log.e("ResponseEntitySize: ", String.valueOf(response));

            return response.getBody().PICKUPWAYBLLDT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.getBody().PICKUPWAYBLLDT;
    }
    public static List<ScanWaybillDt> JsonListScanwayDt(String MethodName, Map map, Class responseclasstype) {

        List<ScanWaybillDt> Resppckupwaydt=null;
        ResponseEntity<GET_SCAN_WAYBILL_DTResponse> response=null;
        System.out.println("JsonService scanwaydet called");
        try {
            final String url1 = url+ MethodName;
            Log.e("url2 is", url1);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            Log.e("req scanwaydt", map.toString());

            HttpEntity<Map<String,String>> request = new HttpEntity<Map<String,String>>(map);
            System.out.println("request sync : "+request.getBody()+"\n"+" - heardesr: "+request.getHeaders());
            response = restTemplate.postForEntity( url1, request , GET_SCAN_WAYBILL_DTResponse.class);



            System.out.println("response ispckp: "+response);
            Log.e("ResponseEntity pckpudt", response.toString());
            Log.e("ResponseEntitySize: ", String.valueOf(response));

            return response.getBody().SCANWAYBLLDT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.getBody().SCANWAYBLLDT;
    }
    public static List<PickupHoldwaybills> JsonListPickupholdwaybill(String MethodName, Map map, Class responseclasstype) {
        ResponseEntity<GET_HOLDWAYBILLS_PICKUP_Response> response=null;
        System.out.println("JsonService pickp called");
        try {
            final String url1 = url+ MethodName;
            Log.e("url2 is", url1);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            Log.e("req scanwaydt", map.toString());

            HttpEntity<Map<String,String>> request = new HttpEntity<Map<String,String>>(map);
            System.out.println("request sync : "+request.getBody()+"\n"+" - heardesr: "+request.getHeaders());
            response = restTemplate.postForEntity( url1, request , GET_HOLDWAYBILLS_PICKUP_Response.class);

            System.out.println("response ispckp: "+response);
            Log.e("ResponseEntity holpck", response.toString());
            Log.e("ResponseEntitySize: ", String.valueOf(response));

            return response.getBody().PCKPHLDWAYBL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.getBody().PCKPHLDWAYBL;
    }

    public static JSONObject jsonnewpostreq(String url, String MethodName, JSONObject json) {
        JSONObject jsonObjectResp = null;
        Long timeoutSeconds = 3L;
        try {

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            //OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .build();


            okhttp3.RequestBody body = RequestBody.create(JSON, json.toString());
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url+ MethodName)
                    .post(body)
                    .build();

            okhttp3.Response response = client.newCall(request).execute();
            System.out.println("requestjson is:"+request+"body"+body);
            String networkResp = response.body().string();
            if (!networkResp.isEmpty()) {
                jsonObjectResp = parseJSONStringToJSONObject(networkResp);
                System.out.println("jsonObjectResp net is:"+jsonObjectResp);
            }
        } catch (Exception ex) {
            String err = String.format("{\"result\":\"false\",\"error\":\"%s\"}", ex.getMessage());
            ex.printStackTrace();
            //  jsonObjectResp = parseJSONStringToJSONObject(null);
            jsonObjectResp=null;
        }
        Log.e("jsonObjectResp/",String.valueOf(jsonObjectResp));


        return jsonObjectResp;
    }
    private static JSONObject parseJSONStringToJSONObject(final String strr) {

        JSONObject response = null;
        try {
            response = new JSONObject(strr);
        } catch (Exception ex) {

            try {
                response = new JSONObject();
                response.put("result", "failed");
                response.put("data", strr);
                response.put("error", ex.getMessage());
            } catch (Exception exx) {

            }
        }
        Log.e("parse response" ,String.valueOf(response));
        return response;
    }

}



