package webservice.JsonFuncClasses.JsonResponses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import webservice.JsonFuncClasses.ScanWaybillDt;

/**
 * Created by ahlaam.kazi on 10/22/2017.
 */

public class GET_SCAN_WAYBILL_DTResponse {
    @JsonProperty("d")
    public List<ScanWaybillDt> SCANWAYBLLDT;
}
