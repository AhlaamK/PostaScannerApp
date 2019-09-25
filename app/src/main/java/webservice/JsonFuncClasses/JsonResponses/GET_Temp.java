package webservice.JsonFuncClasses.JsonResponses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import webservice.JsonFuncClasses.Couriers;

/**
 * Created by ahlaam.kazi on 10/16/2017.
 */

public class GET_Temp {
    @JsonProperty("d")
    private Boolean d;

    @JsonProperty("Status")
    private String Status;

    @JsonProperty("ErrorMessage")
    private String ErrorMessage;

    @JsonProperty("TimeGenerated")
    private String TimeGenerated;

    public Boolean getD() {
        return d;
    }

    public void setD(Boolean d) {
        this.d = d;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getTimeGenerated() {
        return TimeGenerated;
    }

    public void setTimeGenerated(String timeGenerated) {
        TimeGenerated = timeGenerated;
    }
}
