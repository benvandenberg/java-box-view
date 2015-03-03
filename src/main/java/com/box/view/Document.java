package com.box.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;

/**
 * Provide access to the Box View Document API. The Document API is used for
 * uploading, checking status, and deleting documents.
 */
public class Document extends Base {
    /**
     * The request handler.
     *
     * @var Request|null
     */
    protected static Request requestHandler;

    /**
     * The Document API path relative to the base API path.
     *
     * @var string
     */
    public static final String path = "/documents";

    /**
     * Generic upload function used by the two other upload functions, which are
     * more specific than this one, and know how to handle upload by URL and
     * upload from filesystem.
     *
     * @param object|null params A key-value pair of options relating to the
     *                           file upload. Pass-thru from the other upload
     *                           functions.
     * @param object|null postParams A key-value pair of POST params to be sent
     *                               in the body.
     * @param object|null options A key-value pair of request options that may
     *                            modify the way the request is made.
     *
     * @return object A key-value pair representing the metadata of the file.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> upload(Map<String, Object> params,
                                              Map<String, Object> postParams,
                                              Map<String, Object> options)
                   throws Exception {
        if (postParams == null) {
            postParams = new HashMap<String, Object>();
        }

        if (params.containsKey("name")) {
            postParams.put("name", params.get("name"));
        }

        if (params.containsKey("thumbnails")) {
            Object thumbnails = params.get("thumbnails");

            if (thumbnails instanceof ArrayList<?>) {
                StringBuilder sb = new StringBuilder();

                for (int i = 0;
                        i < ((ArrayList<String>) thumbnails).size();
                        i++) {
                    if (i > 0) sb.append(",");
                    sb.append(((ArrayList<String>) thumbnails).get(i));
                }

                thumbnails = sb.toString();
            }

            postParams.put("thumbnails", thumbnails);
        }

        if (params.containsKey("nonSvg")) {
            postParams.put("non_svg", params.get("nonSvg"));
        }

        return requestJson(path, null, postParams, options);
    }

    /**
     * Delete a file by ID.
     *
     * @param string id The ID of the file to delete.
     *
     * @return bool Was the file deleted?
     * @throws Exception
     */
    public static Boolean delete(String id) throws Exception {
        String path = Document.path + "/" + id;

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("httpMethod", "DELETE");
        options.put("rawResponse", true);

        HttpEntity response = requestHttpEntity(path, null, null,  options);

        // a successful delete returns nothing, so we return true in that case
        return (response == null);
    }

    /**
     * Download a file using a specific extension or the original extension.
     *
     * @param string id The ID of the file to download.
     *
     * @return string The contents of the downloaded file.
     * @throws Exception
     */
    public static InputStream download(String id) throws Exception {
        String path = Document.path + "/" + id + "/content";

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("rawResponse", true);

        HttpEntity response = requestHttpEntity(path, null, null, options);
        InputStream stream = null;

        try {
            stream = response.getContent();
        } catch (IOException e) {
            error("invalid_response", e.getMessage());
        }

        return stream;
    }

    /**
     * Download a file using a specific extension or the original extension.
     *
     * @param string id The ID of the file to download.
     * @param string extension The extension to download the file in, which can
     *                         be pdf or zip. If no extension is provided, the
     *                         file will be downloaded using the original
     *                         extension.
     *
     * @return string The contents of the downloaded file.
     * @throws Exception
     */
    public static InputStream download(String id, String extension)
                  throws Exception {
        String path = Document.path + "/" + id + "/content." + extension;

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("rawResponse", true);

        HttpEntity response = requestHttpEntity(path, null, null, options);
        InputStream stream  = null;

        try {
            stream = response.getContent();
        } catch (IOException e) {
            error("invalid_response", e.getMessage());
        }

        return stream;
    }


    /**
     * Get a list of all documents that meet the provided criteria.
     *
     * @return object A key-value pair containing a list of documents matching
     *                the request.
     * @throws Exception
     */
    public static Map<String, Object> list() throws Exception {
        return requestJson(path, null, null, null);
    }

    /**
     * Get a list of all documents that meet the provided criteria.
     *
     * @param object params A key-value pair to filter the list of all documents
     *                      uploaded. None are necessary; all are optional. Use
     *                      the following options:
     *                        - integer|null 'limit' The number of documents to
     *                          to return.
     *                        - string|Date|null 'createdBefore' Upper date
     *                          limit to filter by.
     *                        - string|Date|null 'createdAfter' Lower date limit
     *                          to filter by.
     *
     * @return object A key-value pair containing a list of documents matching
     *                the request.
     * @throws Exception
     * @throws ParseException
     */
    public static Map<String, Object> list(Map<String, Object> params)
                  throws Exception, ParseException {
        Map<String, Object> getParams = new HashMap<String, Object>();

        if (params.containsKey("limit")
                && ((Integer) params.get("limit")) > 0) {
            getParams.put("limit", params.get("limit"));
        }

        if (params.containsKey("createdBefore")) {
            Object createdBefore = params.get("createdBefore");
            String createdBeforeString;

            if (createdBefore instanceof Date) {
                createdBeforeString = date((Date) createdBefore);
            } else {
                createdBeforeString = date(createdBefore.toString());
            }

            getParams.put("createdBefore", createdBeforeString);
        }

        if (params.containsKey("createdAfter")) {
            Object createdAfter = params.get("createdAfter");
            String createdAfterString;

            if (createdAfter instanceof Date) {
                createdAfterString = date((Date) createdAfter);
            } else {
                createdAfterString = date(createdAfter.toString());
            }

            getParams.put("createdAfter", createdAfterString);
        }

        return requestJson(path, getParams, null, null);
    }

    /**
     * Get specific fields from the metadata of a file.
     *
     * @param string id The ID of the file to check.
     * @param string fields The fields to return with the metadata, formatted
     *                      as a comma-separated string. Regardless of which
     *                      fields are provided, id and type are always
     *                      returned.
     *
     * @return object A key-value pair representing the metadata of the file.
     * @throws Exception
     */
    public static Map<String, Object> metadata(String id, String fields)
                  throws Exception {
        Map<String, Object> getParams = new HashMap<String, Object>();
        getParams.put("fields", fields);

        return requestJson(path + "/" + id, getParams, null, null);
    }

    /**
     * Get specific fields from the metadata of a file.
     *
     * @param string id The ID of the file to check.
     * @param string[] fields The fields to return with the metadata, formatted
     *                        as an array. Regardless of which fields are
     *                        provided, id and type are always returned.
     *
     * @return object A key-value pair representing the metadata of the file.
     * @throws Exception
     */
    public static Map<String, Object> metadata(String id,
                                               ArrayList<String> fields)
                  throws Exception {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < fields.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(fields.get(i));
        }

        return metadata(id, sb.toString());
    }

    /**
     * Download a thumbnail of a specific size for a file.
     *
     * @param string id The ID of the file to download a thumbnail for.
     * @param int width The width of the thumbnail in pixels.
     * @param int height The height of the thumbnail in pixels.
     *
     * @return string The contents of the downloaded thumbnail.
     * @throws Exception
     */
    public static InputStream thumbnail(String id,
                                        Integer width,
                                        Integer height)
                  throws Exception {
        String path = Document.path + "/" + id + "/thumbnail";

        Map<String, Object> getParams = new HashMap<String, Object>();
        getParams.put("height", height.toString());
        getParams.put("width", width.toString());

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("rawResponse", true);

        HttpEntity response = requestHttpEntity(path,
                                                getParams,
                                                null,
                                                options);
        InputStream stream = null;

        try {
            stream = response.getContent();
        } catch (IOException e) {
            error("invalid_response", e.getMessage());
        }

        return stream;
    }

    /**
     * Update specific fields for the metadata of a file .
     *
     * @param string id The ID of the file to check.
     * @param object fields A key-value pair of the fields to update on the
     *                      file.
     *
     * @return object A key-value pair representing the metadata of the file.
     * @throws Exception
     */
    public static Map<String, Object> update(String id,
                                             Map<String, Object> fields)
                  throws Exception {
        Map<String, Object> postParams = new HashMap<String, Object>();

        ArrayList<String> supportedFields = new ArrayList<String>();
        supportedFields.add("name");

        for (int i = 0; i < supportedFields.size(); i++) {
            String field = supportedFields.get(i);

            if (fields.containsKey(field)
                    && !fields.get(field).toString().isEmpty()) {
                postParams.put(field, fields.get(field));
            }
        }

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("httpMethod", "PUT");

        return requestJson(path + "/" + id, null, postParams, options);
    }

    /**
     * Upload a local file.
     *
     * @param File file The file resource to upload.
     * @param object params An key-value pair of options relating to the file
     *                      upload. None are necessary; all are optional. Use
     *                      the following options:
     *                        - string|null 'name' Override the filename of the
     *                          file being uploaded.
     *                        - string[]|string|null 'thumbnails' A key-value
     *                          pair  of dimensions in pixels, with each
     *                          dimension formatted as [width]x[height], this
     *                          can also be a comma-separated string.
     *                        - bool|null 'nonSvg' Create a second version of
     *                          the file that doesn't use SVG, for users with
     *                          browsers that don't support SVG?
     *
     * @return object A key-value pair representing the metadata of the file.
     * @throws Exception
     */
    public static Map<String, Object> upload(File file,
                                             Map<String, Object> params)
                  throws Exception {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("file", file);
        options.put("host", "upload.view-api.box.com");

        return upload(params, null, options);
    }

    /**
     * Upload a file by URL.
     *
     * @param String url The url of the file to upload.
     * @param object params An key-value pair of options relating to the file
     *                      upload. None are necessary; all are optional. Use
     *                      the following options:
     *                        - string|null 'name' Override the filename of the
     *                          file being uploaded.
     *                        - string[]|string|null 'thumbnails' A key-value
     *                          pair  of dimensions in pixels, with each
     *                          dimension formatted as [width]x[height], this
     *                          can also be a comma-separated string.
     *                        - bool|null 'nonSvg' Create a second version of
     *                          the file that doesn't use SVG, for users with
     *                          browsers that don't support SVG?
     *
     * @return object A key-value pair representing the metadata of the file.
     * @throws Exception
     */
    public static Map<String, Object> upload(String url,
                                             Map<String, Object> params)
                  throws Exception {
        Map<String, Object> postParams = new HashMap<String, Object>();
        postParams.put("url", url);

        return upload(params, postParams, null);
    }
}
