package webservice.JsonFuncClasses.JsonResponses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import webservice.JsonFuncClasses.RstDetail;

/**
 * Created by ahlaam.kazi on 10/16/2017.
 */

public class GET_RSTDETAILResponse {
    @JsonProperty("d")
    public List<RstDetail> RSTDETAIL;
}
