package com.box.view;

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
     * The request handler.
     *
     * @var Request|null
     */
    protected static Request requestHandler;

    /**
     * Handle an error. We handle errors by throwing an exception.
     *
     * @param string error An error code representing the error
     *                     (use_underscore_separators).
     * @param string message The error message.
     *
     * @return void No return value.
     * @throws Exception
     */
    protected static void error(String error, String message)
                     throws Exception {
        throw new Exception(message, error);
    }

    /**
     * Send a new request to the API and return a string.
     *
     * @param string path The path to add after the base path.
     * @param object getParams A key-value pair of GET params to be added to the
     *                         URL.
     * @param object postParams A key-value pair of POST params to be sent in
     *                          the body.
     * @param object requestOpts A key-value pair of request options that may
     *                           modify the way the request is made.
     *
     * @return string The response is pass-thru from Request.
     * @throws Exception
     */
    protected static HttpEntity requestHttpEntity(
                                              String path,
                                              Map<String, Object> getParams,
                                              Map<String, Object> postParams,
                                              Map<String, Object> requestOpts)
                     throws Exception {
        requestOpts.put("rawResponse", true);
        return getRequestHandler().requestHttpEntity(path,
                                                     getParams,
                                                     postParams,
                                                     requestOpts);
    }

    /**
     * Send a new request to the API and return a JSONObject.
     *
     * @param string path The path to add after the base path.
     * @param object getParams A key-value pair of GET params to be added to the
     *                         URL.
     * @param object postParams A key-value pair of POST params to be sent in
     *                          the body.
     * @param object requestOpts A key-value pair of request options that may
     *                           modify the way the request is made.
     *
     * @return string The response is pass-thru from Request.
     * @throws Exception
     */
    protected static Map<String, Object> requestJson(
                                             String path,
                                             Map<String, Object> getParams,
                                             Map<String, Object> postParams,
                                             Map<String, Object> requestOpts)
                     throws Exception {
        return getRequestHandler().requestJson(path,
                                               getParams,
                                               postParams,
                                               requestOpts);
    }

    /**
     * Take a date object, and return a date string that is formatted as an
     * RFC 3339 timestamp.
     *
     * @param Date date A date object.
     *
     * @return string An RFC 3339 timestamp.
     */
    public static String date(Date date) {
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
    public static String date(String dateString) throws ParseException {
        return date(SimpleDateFormat.getInstance().parse(dateString));
    }

    /**
     * Return the request handler.
     *
     * @return Request The request handler.
     */
    public static Request getRequestHandler() {
        if (requestHandler == null) {
            setRequestHandler(new Request(Client.getApiKey()));
        }

        return requestHandler;
    }

    /**
     * Set the request handler.
     *
     * @param Request requestHandler The request handler.
     *
     * @return void No return value.
     */
    public static void setRequestHandler(Request newRequestHandler) {
        requestHandler = newRequestHandler;
    }
}
