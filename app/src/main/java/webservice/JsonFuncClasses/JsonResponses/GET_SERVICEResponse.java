package webservice.JsonFuncClasses.JsonResponses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import webservice.JsonFuncClasses.Service;

/**
 * Created by ahlaam.kazi on 10/16/2017.
 */

public class GET_SERVICEResponse {
    @JsonProperty("d")
    public List<Service> SERVICE;
}
