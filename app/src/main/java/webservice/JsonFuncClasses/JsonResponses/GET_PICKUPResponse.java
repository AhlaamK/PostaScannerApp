package webservice.JsonFuncClasses.JsonResponses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import webservice.JsonFuncClasses.PickUp;

/**
 * Created by ahlaam.kazi on 10/16/2017.
 */

public class GET_PICKUPResponse {
    @JsonProperty("d")
    public List<PickUp> PICKUP;
}
