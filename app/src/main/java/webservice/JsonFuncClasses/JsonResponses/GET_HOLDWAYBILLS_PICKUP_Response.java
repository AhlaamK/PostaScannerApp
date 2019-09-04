package webservice.JsonFuncClasses.JsonResponses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import webservice.JsonFuncClasses.PickupHoldwaybills;

/**
 * Created by ahlaam.kazi on 11/23/2017.
 */

public class GET_HOLDWAYBILLS_PICKUP_Response {
    @JsonProperty("d")
    public List<PickupHoldwaybills> PCKPHLDWAYBL;
}
