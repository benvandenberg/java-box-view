package com.box.view;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Makes a request to the Box View API.
 */
public class Request {
    /**
     * Request error codes.
     */
    public static final String BAD_REQUEST_ERROR            = "bad_request";
    public static final String HTTP_CLIENT_ERROR
                                                          = "http_client_error";
    public static final String INVALID_HTTP_METHOD_ERROR
                                                        = "invalid_http_method";
    public static final String INVALID_URI_ERROR            = "invalid_uri";
    public static final String JSON_RESPONSE_ERROR
                                            = "server_response_not_valid_json";
    public static final String METHOD_NOT_ALLOWED_ERROR  = "method_not_allowed";
    public static final String NOT_FOUND_ERROR              = "not_found";
    public static final String REQUEST_TIMEOUT_ERROR        = "request_timeout";
    public static final String SERVER_ERROR                 = "server_error";
    public static final String TOO_MANY_REQUESTS_ERROR
                                                          = "too_many_requests";
    public static final String UNAUTHORIZED_ERROR           = "unauthorized";
    public static final String UNSUPPORTED_MEDIA_TYPE_ERROR
                                                     = "unsupported_media_type";

    /**
     * The default protocol (Box View uses HTTPS).
     */
    public static final String PROTOCOL = "https";

    /**
     * The default host
     */
    public static final String HOST = "view-api.box.com";

    /**
     * The default base path on the server where the API lives.
     */
    public static final String BASE_PATH = "/1";

    /**
     * The number of seconds before timing out when in a retry loop.
     */
    public static final Integer DEFAULT_RETRY_TIMEOUT = 60;

    /**
     * The API key.
     */
    private String apiKey;

    /**
     * The timestamp of the last request.
     */
    private long timestampRequested;

    /**
     * A Gson instance to reuse.
     *
     * @const Gson
     */
    private static Gson GSON = new Gson();

    /**
     * Set the API key.
     *
     * @param string apiKey The API key.
     */
    public Request(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Send an HTTP request and return an HttpEntity.
     *
     * @param string path The path to add after the base path.
     * @param object|null getParams A key-value pair of GET params to be added
     *                              to the URL.
     * @param object|null postParams A key-value pair of POST params to be sent
     *                               in the body.
     * @param object|null requestOptions A key-value pair of request options
     *                                   that may modify the way the request is
     *                                   made.
     *
     * @return string The raw response string.
     * @throws Exception
     */
    public HttpEntity requestHttpEntity(String path,
                                        Map<String, Object> getParams,
                                        Map<String, Object> postParams,
                                        Map<String, Object> requestOptions)
           throws Exception {
        HttpUriRequest request = createRequest(path,
                                               getParams,
                                               postParams,
                                               requestOptions);
        Integer timeout = createTimeout(requestOptions);
        HttpResponse response  = execute(request, timeout);
        return response.getEntity();
    }

    /**
     * Send an HTTP request and return a JSON object.
     *
     * @param string path The path to add after the base path.
     * @param object|null getParams A key-value pair of GET params to be added
     *                              to the URL.
     * @param object|null postParams A key-value pair of POST params to be sent
     *                               in the body.
     * @param object|null requestOptions A key-value pair of request options
     *                                   that may modify the way the request is
     *                                   made.
     *
     * @return object A key-value pair decoded from JSON.
     * @throws Exception
     */
    public Map<String, Object> requestJson(String path,
                                           Map<String, Object> getParams,
                                           Map<String, Object> postParams,
                                           Map<String, Object> requestOptions)
           throws Exception {
        HttpUriRequest request = createRequest(path,
                                               getParams,
                                               postParams,
                                               requestOptions);
        Integer timeout = createTimeout(requestOptions);
        HttpResponse response  = execute(request, timeout);
        return handleJsonResponse(response, request);
    }

    /**
     * Handle an error. We handle errors by throwing an exception.
     *
     * @param string error An error code representing the error
     *                     (use_underscore_separators).
     * @param string|null message The error message.
     * @param HttpUriRequest|null request The HTTP request object.
     * @param HttpResponse|null response The HTTP response object.
     *
     * @return void
     * @throws Exception
     */
    protected static void error(String error,
                                String message,
                                HttpUriRequest request,
                                HttpResponse response)
              throws Exception {
        if (request != null) {
            URI uri = request.getURI();

            message += "\n";
            message += "Method: " + request.getMethod() + "\n";
            message += "URL: " + uri.toString() + "\n";
            message += "Query: " + uri.getQuery() + "\n";

            Header[] headers = request.getAllHeaders();
            message         += "Headers: " + GSON.toJson(headers) + "\n";

            String requestBody = "";

            if (request.getMethod() == "POST") {
                HttpEntity entity = ((HttpPost) request).getEntity();

                try {
                    requestBody = EntityUtils.toString(entity);
                } catch (IOException e) {
                }
            }

            message += "Request Body: " + requestBody + "\n";
        }

        if (response != null) {
            HttpEntity entity   = response.getEntity();
            String responseBody = "";

            try {
                responseBody = EntityUtils.toString(entity);
            } catch (IOException e) {
            }

            message += "\n";
            message += "Response: " + responseBody + "\n";
        }

        throw new Exception(message, error);
    }

    /**
     * Prepare and create an HTTP request object.
     *
     * @param string path The path to add after the base path.
     * @param object|null getParams A key-value pair of GET params to be added
     *                              to the URL.
     * @param object|null postParams A key-value pair of POST params to be sent
     *                               in the body.
     * @param object|null requestOptions A key-value pair of request options
     *                                   that may modify the way the request is
     *                                   made.
     *
     * @return HttpUriRequest The HTTP request object.
     * @throws Exception
     */
    private HttpUriRequest createRequest(String path,
                                         Map<String, Object> getParams,
                                         Map<String, Object> postParams,
                                         Map<String, Object> requestOptions)
            throws Exception {
        if (getParams == null)  getParams  = new HashMap<String, Object>();
        if (postParams == null) postParams = new HashMap<String, Object>();

        if (requestOptions == null) {
            requestOptions = new HashMap<String, Object>();
        }

        HttpEntity requestEntity = null;

        String method = "GET";

        if (requestOptions.containsKey("file")
                && requestOptions.get("file") != null) {
            method                                 = "POST";
            MultipartEntityBuilder mpEntityBuilder = MultipartEntityBuilder
                                                     .create();
            mpEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            File file = (File) requestOptions.get("file");
            mpEntityBuilder.addBinaryBody("file", file);

            for (Map.Entry<String, Object> param : postParams.entrySet()) {
                String value = param.getValue().toString();
                mpEntityBuilder.addTextBody(param.getKey(), value);
            }

            requestEntity = mpEntityBuilder.build();
        } else if (!postParams.isEmpty()) {
            method = "POST";

            try {
                requestEntity = new StringEntity(GSON.toJson(postParams));
            } catch (UnsupportedEncodingException e) {
            }
        }

        if (requestOptions.containsKey("httpMethod")
                && requestOptions.get("httpMethod") != null) {
            method = requestOptions.get("httpMethod").toString();
        }

        String hostName = HOST;

        if (requestOptions.containsKey("host")
                && requestOptions.get("host") != null) {
            hostName = requestOptions.get("host").toString();
        }

        URI uri = getUri(hostName, path, getParams);
        return getRequest(method, uri, requestEntity, requestOptions);
    }

    /**
     * Get a timeout for the request.
     *
     * @param object|null requestOptions A key-value pair of request options
     *                                   that may modify the way the request is
     *                                   made.
     *
     * @return Integer The maximum number of seconds to retry for..
     */
    private Integer createTimeout(Map<String, Object> requestOptions) {
        timestampRequested = System.currentTimeMillis() * 1000;
        return (requestOptions != null
                && requestOptions.containsKey("timeout")
                && ((Integer) requestOptions.get("timeout")) > 0)
                   ? (Integer) requestOptions.get("timeout")
                   : DEFAULT_RETRY_TIMEOUT;
    }

    /**
     * Execute a request to the server and return the response, while retrying
     * based on any Retry-After headers that are sent back.
     *
     * @param HttpUriRequest request The HTTP request object to send, and
     *                               possibly retry.
     * @param int timeout The maximum number of seconds to retry for.
     *
     * @return HttpResponse The HttpResponse response object.
     * @throws Exception
     */
    private HttpResponse execute(HttpUriRequest request, Integer timeout)
            throws Exception {
        HttpClient client     = getHttpClient();
        HttpResponse response = null;

        try {
            response = client.execute(request);
        } catch (IOException e) {
            handleRequestError(request, response, e);
        }

        if (response.getFirstHeader("Retry-After") != null) {
            Integer seconds = Math.round(System.currentTimeMillis()
                                         - timestampRequested);

            if (timeout > 0 && seconds >= timeout) {
                String message = "The request timed out after retrying for "
                                 + seconds + " seconds.";
                error(REQUEST_TIMEOUT_ERROR, message, request, response);
            }

            String retryAfter = response.getFirstHeader("Retry-After")
                                        .getValue();

            try {
                Thread.sleep(Integer.parseInt(retryAfter) * 1000);
            } catch (InterruptedException e) {
                return null;
            }

            return execute(request, timeout);
        }

        handleRequestError(request, response, null);
        return response;
    }

    /**
     * Get a new HttpClient instance using sensible defaults.
     *
     * @return HttpClient A new HttpClient instance.
     */
    private HttpClient getHttpClient() {
        RequestConfig config = RequestConfig.custom()
                               .setConnectTimeout(10 * 1000)
                               .setConnectionRequestTimeout(60 * 1000)
                               .setSocketTimeout(60 * 1000)
                               .build();
        return HttpClientBuilder.create()
                                .setDefaultRequestConfig(config)
                                .build();
    }

    /**
     * Create an HTTP request object.
     *
     * @param String method The HTTP method to call.
     * @param URI uri The URI to request to.
     * @param HttpEntity requestEntity The body for POST/PUT operations.
     * @param object requestOptions A key-value pair of request options that may
     *                              modify the way the request is made.
     *
     * @throws Exception
     * @return HttpUriRequest The HTTP request object.
     */
    private HttpUriRequest getRequest(String method,
                                      URI uri,
                                      HttpEntity requestEntity,
                                      Map<String, Object> requestOptions)
            throws Exception {
        HttpUriRequest request = null;

        if (method == "GET") {
            request = new HttpGet(uri);
        } else if (method == "POST") {
            request = new HttpPost(uri);

            if (requestEntity != null) {
                ((HttpPost) request).setEntity(requestEntity);
            }
        } else if (method == "PUT") {
            request = new HttpPut(uri);

            if (requestEntity != null) {
                ((HttpPut) request).setEntity(requestEntity);
            }
        } else if (method == "DELETE") {
            request = new HttpDelete(uri);
        } else {
            String message = "Invalid HTTP method \"" + method + "\"";
            error(INVALID_HTTP_METHOD_ERROR, message, null, null);
        }

        request.addHeader("Accept", "application/json");
        request.addHeader("Authorization", "Token " + apiKey);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("User-Agent", "box-view-java");

        if (requestOptions.containsKey("file")) {
            request.removeHeaders("Content-Type");
        }

        if (requestOptions.containsKey("rawResponse")
                && (Boolean) requestOptions.get("rawResponse") == true) {
            request.removeHeaders("Accept");
            request.addHeader("Accept", "*/*");
        }

        return request;
    }

    /**
     * Create a URI, given a hostname, path, and GET params.

     * @param String hostName The hostname to make the request to.
     * @param String path The path to make the request to.
     * @param object|null getParams A key-value pair of POST params to be sent
     *                              in the body.
     *
     * @return URI The URI to make the request to.
     * @throws Exception
     */
    private URI getUri(String hostName,
                       String path,
                       Map<String, Object> getParams)
            throws Exception {
        URIBuilder uriBuilder = new URIBuilder()
                                .setScheme(PROTOCOL)
                                .setHost(hostName)
                                .setPath(BASE_PATH + path);

        if (!getParams.isEmpty()) {
            for (Map.Entry<String, Object> param : getParams.entrySet()) {
                String value = param.getValue().toString();
                uriBuilder.addParameter(param.getKey(), value);
            }
        }

        URI uri = null;

        try {
            uri = uriBuilder.build();
        } catch (URISyntaxException e) {
            error(INVALID_URI_ERROR, e.getMessage(), null, null);
        }

        return uri;
    }

    /**
     * Check if there is an HTTP error, and returns a brief error description if
     * there is.
     *
     * @param int httpCode The HTTP code returned by the API server.
     *
     * @return string|null Brief error description.
     */
    private static String handleHttpError(Integer httpCode) {
        Map<Integer, String> errorCodes = new HashMap<Integer, String>();
        errorCodes.put(400, BAD_REQUEST_ERROR);
        errorCodes.put(401, UNAUTHORIZED_ERROR);
        errorCodes.put(404, NOT_FOUND_ERROR);
        errorCodes.put(405, METHOD_NOT_ALLOWED_ERROR);
        errorCodes.put(415, UNSUPPORTED_MEDIA_TYPE_ERROR);
        errorCodes.put(429, TOO_MANY_REQUESTS_ERROR);

        if (errorCodes.containsKey(httpCode)) {
            return errorCodes.get(httpCode);
        }

        if (httpCode >= 500 && httpCode < 600) {
            return SERVER_ERROR;
        }

        return null;
    }

    /**
     * Handle the response from the server. Raw responses are returned without
     * checking anything. JSON responses are decoded and then checked for any
     * errors.
     *
     * @param HttpResponse response The HTTP response object.
     * @param HttpUriRequest The HTTP request object.
     *
     * @return object A key-value pair decoded from JSON.
     * @throws Exception
     */
    private static Map<String, Object> handleJsonResponse(
                                                        HttpResponse response,
                                                        HttpUriRequest request)
                   throws Exception {
        String body = null;

        try {
            body = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
        }

        TypeToken<Map<String, Object>> typeToken =
                                        new TypeToken<Map<String, Object>>() {};
        java.lang.reflect.Type mapType           = typeToken.getType();
        Map<String, Object> jsonDecoded          = GSON.fromJson(body, mapType);

        if (jsonDecoded == null) {
            error(JSON_RESPONSE_ERROR, null, request, response);
        }

        if (jsonDecoded.containsKey("status")
                && jsonDecoded.get("status").toString() == "error") {
            String message = (jsonDecoded.containsKey("erorr_message")
                              && jsonDecoded.get("error_message") != null)
                                 ? jsonDecoded.get("error_message").toString()
                                 : "Server Error";
            error(SERVER_ERROR, message, request, response);
        }

        return jsonDecoded;
    }

    /**
     * Handle a request error.
     *
     * @param HttpUriRequest request The HTTP request object.
     * @param HttpResponse response The HTTP response object.
     * @param Exception e Any error exception that triggered this call.
     *
     * @return void
     * @throws Exception
     */
    private static void handleRequestError(HttpUriRequest request,
                                           HttpResponse response,
                                           java.lang.Exception e)
                   throws Exception {
        Integer statusCode = response.getStatusLine().getStatusCode();
        String error       = handleHttpError(statusCode);
        String message     = "Server Error";

        if (error == null) {
            if (e == null) {
                return;
            }

            error   = HTTP_CLIENT_ERROR;
            message = e.getMessage();
        }

        error(error, message, request, response);
    }
}