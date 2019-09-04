package webservice;

import android.os.Environment;
import android.util.Log;

import com.postaplus.postascannerapp.MasterActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lj on 17-09-2016.
 */
public class SoapService {


    public static String soapResult(String urlParameters)
    {
        System.out.println("urlParameters before in soapservice : " + urlParameters);

        String WebServUrl="";
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection

            urlParameters = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:pos=\"http://www.postaplus.net/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    urlParameters  +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            System.out.println("urlParameters after in soapservice : " + urlParameters);
            Log.e("ExecutePost",urlParameters);
            System.out.println("urlParameters length in soapservice : "+ urlParameters.length());

            //Log.e("Postaservice/mastety",MasterActivity.URL);
            StringBuilder text = new StringBuilder();

            try {
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

            //connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","text/xml;charset=UTF-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            //connection.setRequestProperty("Content-Language", "en-US");
            //connection.setRequestProperty("Connection","Close");keep-alive
           connection.setRequestProperty("Connection","keep-alive");
           // connection.setRequestProperty("Connection","Close");
            System.out.println("urlParameters.length() "+urlParameters.length());
            if(urlParameters.length()>60000)
            {
                System.out.println("urlParameters greater than 5100");
                connection.setConnectTimeout(240000); //set timeout to 150 seconds
                connection.setReadTimeout(480000);
            }
            else if(urlParameters.length()>51000 && (urlParameters.length()<60000))
            {
                System.out.println("urlParameters greater than 5100");
                connection.setConnectTimeout(150000); //set timeout to 150 seconds
                connection.setReadTimeout(300000);
            }
            else if((urlParameters.length()<51000) && (urlParameters.length()>13000))
            {
                System.out.println("urlParameters less than 5100");
                    connection.setConnectTimeout(30000); //set timeout to 30 seconds
                      connection.setReadTimeout(60000);
            }
            else{
                 System.out.println("urlParameters less than 13000");
                   connection.setConnectTimeout(12000); //set timeout to 10 seconds
                   connection.setReadTimeout(20000);
            }






            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);

           // connection.setRequestMethod("POST");
          /*  connection.setRequestProperty("Content-Type","text/xml;charset=UTF-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);
            connection.setRequestProperty("User-Agent", "Android");*/

            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
            wr.writeBytes (urlParameters);
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
                //    response2.append("\r");
                //  response += "\r";
                //System.out.println("\nResponse BY Line: " + line);
            }
            //   rd.close();

            //Log.e("ExecutePost","Response Recieved");
            Log.e("ExecutePost/Resp",response2.toString());

            rd.close();
            return response2.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {
            System.out.println("Soap Final Block Called!");
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

//  private boolean callSOAPWebService() {

}
