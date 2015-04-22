package com.box.view;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.HttpEntity;

/**
 * Acts as a base class for the different Box View APIs.
 */
public abstract class Base {
    /**
     * The API path relative to the base API path.
     */
    public static final String PATH = "/";

    /**
     * The client instance to make requests from.
     */
    protected BoxViewClient client;

    /**
     * Take a date object, and return a date string that is formatted as an
     * RFC 3339 timestamp.
     *
     * @param date A date object.
     *
     * @return An RFC 3339 timestamp.
     */
    protected static String date(Date date) {
        String format              = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat isoFormat = new SimpleDateFormat(format);
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return isoFormat.format(date);
    }

    /**
     * Take a date string in almost any format, and return a date string that
     * is formatted as an RFC 3339 timestamp.
     *
     * @param dateString A date string in almost any format.
     *
     * @return An RFC 3339 timestamp.
     * @throws ParseException
     */
    protected static String date(String dateString) throws ParseException {
        return date(parseDate(dateString));
    }

    /**
     * Handle an error. We handle errors by throwing an exception.
     *
     * @param error An error code representing the error
     *              (use_underscore_separators).
     * @param message The error message.
     *
     * @return void
     * @throws BoxViewException
     */
    protected static void error(String error, String message)
                     throws BoxViewException {
        throw new BoxViewException(message, error);
    }

    /**
     * Take a date object or date string in RFC 3339 format, and return a date
     * object.
     *
     * @param dateString A date string in RFC 3339 format.
     *
     * @return The date representation of the dateString.
     */
    protected static Date parseDate(String dateString) {
        Date date;

        try {
            String format         = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            DateFormat dateFormat = new SimpleDateFormat(format);
            date                  = dateFormat.parse(dateString);
        } catch (ParseException e) {
            try {
                String format         = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                DateFormat dateFormat = new SimpleDateFormat(format);
                date                  = dateFormat.parse(dateString);
            } catch (ParseException e2) {
                date = null;
            }
        }

        return date;
    }

    /**
     * Send a new request to the API and return a string.
     *
     * @param client The client instance to make requests from.
     * @param path The path to make a request to.
     * @param getParams A key-value pair of GET params to be added to the URL.
     * @param postParams A key-value pair of POST params to be sent in the body.
     * @param requestOptions A key-value pair of request options that may modify
     *                       the way the request is made.
     *
     * @return The response is pass-thru from Request.
     * @throws BoxViewException
     */
    protected static HttpEntity requestHttpEntity(
                                             BoxViewClient client,
                                             String path,
                                             Map<String, Object> getParams,
                                             Map<String, Object> postParams,
                                             Map<String, Object> requestOptions)
                     throws BoxViewException {
        requestOptions.put("rawResponse", true);
        return client.getRequestHandler().requestHttpEntity(path,
                                                            getParams,
                                                            postParams,
                                                            requestOptions);
    }

    /**
     * Send a new request to the API and return a key-value pair.
     *
     * @param client The client instance to make requests from.
     * @param path The path to make a request to.
     * @param getParams A key-value pair of GET params to be added to the URL.
     * @param postParams A key-value pair of POST params to be sent in the body.
     * @param requestOptions A key-value pair of request options that may modify
     *                       the way the request is made.
     *
     * @return The response is pass-thru from Request.
     * @throws BoxViewException
     */
    protected static Map<String, Object> requestJson(
                                             BoxViewClient client,
                                             String path,
                                             Map<String, Object> getParams,
                                             Map<String, Object> postParams,
                                             Map<String, Object> requestOptions)
                     throws BoxViewException {
        return client.getRequestHandler().requestJson(path,
                                                      getParams,
                                                      postParams,
                                                      requestOptions);
    }
}
