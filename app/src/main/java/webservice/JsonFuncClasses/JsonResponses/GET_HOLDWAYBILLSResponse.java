package webservice.JsonFuncClasses.JsonResponses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import webservice.JsonFuncClasses.HoldWayBills;

/**
 * Created by ahlaam.kazi on 10/16/2017.
 */

public class GET_HOLDWAYBILLSResponse {
    @JsonProperty("d")
    public List<HoldWayBills> HOLDWAYBILLS;
}
