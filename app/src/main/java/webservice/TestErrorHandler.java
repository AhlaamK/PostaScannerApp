package webservice;

import android.util.Log;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.support.Base64;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

/**
 * Created by ahlaam.kazi on 10/23/2017.
 */

public class TestErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

        Log.e("array is:",response.getBody().toString());
        //conversion logic for decoding conversion
        ByteArrayInputStream arrayInputStream = (ByteArrayInputStream) response.getBody();

        Log.e("array",arrayInputStream.toString());
        Scanner scanner = new Scanner(arrayInputStream);
        scanner.useDelimiter("\\Z");
        String data = "";
        if (scanner.hasNext())
            data = scanner.next();
        System.out.println(data);


    }
}