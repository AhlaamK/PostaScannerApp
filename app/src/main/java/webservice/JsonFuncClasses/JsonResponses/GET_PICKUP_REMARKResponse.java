package webservice.JsonFuncClasses.JsonResponses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import webservice.JsonFuncClasses.Remarks;

/**
 * Created by ahlaam.kazi on 10/16/2017.
 */

public class GET_PICKUP_REMARKResponse {
    @JsonProperty("d")
    public List<Remarks> REMARKS;
}
