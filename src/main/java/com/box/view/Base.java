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
    public static String path = "/";

    /**
     * The client instance to make requests from.
     */
    protected Client client;

    /**
     * Take a date object, and return a date string that is formatted as an
     * RFC 3339 timestamp.
     *
     * @param Date date A date object.
     *
     * @return string An RFC 3339 timestamp.
     */
    protected static String date(Date date) {
        String format              = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat isoFormat = new SimpleDateFormat(format);
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return isoFormat.format(date);
    }

    /**
     * Take a date string in almost any format, and return a date string that
     * is formatted as an RFC 3339 timestamp.
     *
     * @param string dateString A date string in almost any format.
     *
     * @return string An RFC 3339 timestamp.
     * @throws ParseException
     */
    protected static String date(String dateString) throws ParseException {
        return date(SimpleDateFormat.getInstance().parse(dateString));
    }

    /**
     * Handle an error. We handle errors by throwing an exception.
     *
     * @param string error An error code representing the error
     *                     (use_underscore_separators).
     * @param string message The error message.
     *
     * @return void
     * @throws Exception
     */
    protected static void error(String error, String message)
                     throws Exception {
        throw new Exception(message, error);
    }

    /**
     * Take a date object or date string in RFC 3339 format, and return a date
     * object.
     *
     * @param object dateString A date or date string in RFC 3339 format.
     *
     * @return Date The date representation of the dateString.
     */
    protected static Date parseDate(Object dateString) {
        if (dateString instanceof Date) {
            return (Date) dateString;
        }

        Date date;

        try {
            String format         = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            DateFormat dateFormat = new SimpleDateFormat(format);
            date                  = dateFormat.parse((String) dateString);
        } catch (ParseException e) {
            try {
                String format         = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                DateFormat dateFormat = new SimpleDateFormat(format);
                date                  = dateFormat.parse((String) dateString);
            } catch (ParseException e2) {
                date = null;
            }
        }

        return date;
    }

    /**
     * Send a new request to the API and return a string.
     *
     * @param Client client The client instance to make requests from.
     * @param string path The path to make a request to.
     * @param object|null getParams A key-value pair of GET params to be added
     *                              to the URL.
     * @param object|null postParams A key-value pair of POST params to be sent
     *                               in the body.
     * @param object|null requestOptions A key-value pair of request options
     *                                   that may modify the way the request is made.
     *
     * @return string The response is pass-thru from Request.
     * @throws Exception
     */
    protected static HttpEntity requestHttpEntity(
                                             Client client,
                                             String path,
                                             Map<String, Object> getParams,
                                             Map<String, Object> postParams,
                                             Map<String, Object> requestOptions)
                     throws Exception {
        requestOptions.put("rawResponse", true);
        return client.getRequestHandler().requestHttpEntity(path,
                                                            getParams,
                                                            postParams,
                                                            requestOptions);
    }

    /**
     * Send a new request to the API and return a JSONObject.
     *
     * @param Client client The client instance to make requests from.
     * @param string path The path to make a request to.
     * @param object|null getParams A key-value pair of GET params to be added
     *                              to the URL.
     * @param object|null postParams A key-value pair of POST params to be sent
     *                               in the body.
     * @param object|null requestOptions A key-value pair of request options
     *                                   that may modify the way the request is
     *                                   made.
     *
     * @return string The response is pass-thru from Request.
     * @throws Exception
     */
    protected static Map<String, Object> requestJson(
                                             Client client,
                                             String path,
                                             Map<String, Object> getParams,
                                             Map<String, Object> postParams,
                                             Map<String, Object> requestOptions)
                     throws Exception {
        return client.getRequestHandler().requestJson(path,
                                                      getParams,
                                                      postParams,
                                                      requestOptions);
    }
}
