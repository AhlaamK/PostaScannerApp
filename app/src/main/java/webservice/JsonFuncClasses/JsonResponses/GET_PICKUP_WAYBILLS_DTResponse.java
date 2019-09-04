package webservice.JsonFuncClasses.JsonResponses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import webservice.JsonFuncClasses.PickUpWaybillsDT;

/**
 * Created by ahlaam.kazi on 10/22/2017.
 */

public class GET_PICKUP_WAYBILLS_DTResponse {
    @JsonProperty("d")
    public List<PickUpWaybillsDT> PICKUPWAYBLLDT;
}
