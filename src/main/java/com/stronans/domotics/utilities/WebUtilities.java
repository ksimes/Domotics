package com.stronans.domotics.utilities;

import org.springframework.http.HttpHeaders;

/**
 * Created by S.King on 07/07/2017.
 */
public class WebUtilities {

    public static HttpHeaders header()
    {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "http://localhost:4200");

        return responseHeaders;
    }
}
