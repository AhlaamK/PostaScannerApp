package webservice.JsonFuncClasses.JsonResponses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import webservice.JsonFuncClasses.Events;

/**
 * Created by ahlaam.kazi on 10/15/2017.
 */

public class GET_EVENTSResponse {
    @JsonProperty("d")
    public List<Events> EVENTS;
}
