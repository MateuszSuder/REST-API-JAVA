package RESTAPIproject.models;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    public String error;
    public int code;

    public ErrorResponse(String e, int c) {
        this.error = e;
        this.code = c;
    }
}
