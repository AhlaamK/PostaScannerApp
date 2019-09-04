package webservice.JsonFuncClasses;

import android.util.Log;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Created by ahlaam.kazi on 10/23/2017.
 */

public class JsonErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        Log.e("JsonErr: ",response.getStatusText().toString());
        //Log.e("JsonErr: ",response.getBody().read());
        return true;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

    }
}
