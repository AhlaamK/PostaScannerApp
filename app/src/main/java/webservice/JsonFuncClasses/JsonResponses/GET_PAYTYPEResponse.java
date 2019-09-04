package webservice.JsonFuncClasses.JsonResponses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import webservice.JsonFuncClasses.PayType;

/**
 * Created by ahlaam.kazi on 10/16/2017.
 */

public class GET_PAYTYPEResponse {
    @JsonProperty("d")
    public List<PayType> PAYTYPE;
}
