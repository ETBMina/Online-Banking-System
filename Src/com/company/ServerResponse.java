package com.company;

import java.io.Serializable;

public class ServerResponse  implements Serializable {

    private String response ;
    private boolean succes ;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isSucces() {
        return succes;
    }

    public void setSucces(boolean succes) {
        this.succes = succes;
    }


    public ServerResponse(String response, boolean succes) {
        this.response = response;
        this.succes = succes;
    }
    public ServerResponse() {
    }
}
