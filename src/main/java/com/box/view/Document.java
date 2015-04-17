package com.box.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;

/**
 * Provide access to the Box View Document API. The Document API is used for
 * uploading, checking status, and deleting documents.
 *
 * Document objects have the following fields:
 *   - string 'id' The document ID.
 *   - string|Date 'createdAt' The date the document was created, formatted as
 *                        RFC 3339.
 *   - string 'name' The document title.
 *   - string 'status' The document status, which can be 'queued', 'processing',
 *                     'done', or 'error'.
 *
 * Only the following fields can be updated:
 *   - string 'name' The document title.
 *
 * When finding documents, the following parameters can be set:
 *   - int|null 'limit' The number of documents to return.
 *   - string|Date|null 'createdBefore' Upper date limit to filter by.
 *   - string|Date|null 'createdAfter' Lower date limit to filter by.
 *
 * When uploading a file, the following parameters can be set:
 *   - string|null 'name' Override the filename of the file being uploaded.
 *   - string[]|string|null 'thumbnails' An array of dimensions in pixels, with
 *                                       each dimension formatted as
 *                                       [width]x[height], this can also be a
 *                                       comma-separated string.
 *   - bool|null 'nonSvg' Create a second version of the file that doesn't use
 *                        SVG, for users with browsers that don't support SVG?
 *
 */
public class Document extends Base {
    /**
     * Document error codes.
     */
    public static final String INVALID_FILE_ERROR     = "invalid_file";
    public static final String INVALID_RESPONSE_ERROR = "invalid_response";

    /**
     * An alternate hostname that file upload requests are sent to.
     */
    public static final String FILE_UPLOAD_HOST = "upload.view-api.box.com";

    /**
     * The Document API path relative to the base API path.
     */
    public static final String path = "/documents";

    /**
     * The fields that can be updated on a document.
     * @var array
     */
    public static final String[] updateableFields = {"name"};

    /**
     * The date the document was created, formatted as RFC 3339.
     */
    private Date createdAt;

    /**
     * The document ID.
     */
    private String id;

    /**
     * The document title.
     */
    private String name;

    /**
     * The document status, which can be 'queued', 'processing', 'done', or
     * 'error'.
     */
    private String status;

    /**
     * Instantiate the document.
     *
     * @param Client client The client instance to make requests from.
     * @param object data A key-value pair to instantiate the object with. Use
     *                    the following values:
     *                      - string 'id' The document ID.
     *                      - string|Date 'createdAt' The date the document was
     *                        created, formatted as RFC 3339.
     *                      - string 'name' The document title.
     *                      - string 'status' The document status, which can be
     *                        'queued', 'processing', 'done', or 'error'.
     */
    public Document(Client client, Map<String, Object>data) {
        this.client = client;
        id          = (String) data.get("id");

        setValues(data);
    }

    /**
     * Get the date the document was created, formatted as RFC 3339.
     *
     * @return Date The date the document was created, formatted as RFC 3339.
     */
    public Date createdAt() {
        return createdAt;
    }

    /**
     * Get the document ID.
     *
     * @return string The document ID.
     */
    public String id() {
        return id;
    }

    /**
     * Get the document title.
     *
     * @return string The document title.
     */
    public String name() {
        return name;
    }

    /**
     * Get the document status.
     *
     * @return string The document title.
     */
    public String status() {
        return status;
    }

    /**
     * Create a session for a specific document.
     *
     * @return Session A new session instance.
     * @throws Exception
     */
    public Session createSession() throws Exception {
        return Session.create(client, id);
    }

    /**
     * Create a session for a specific document.
     *
     * @param object|null params A key-value pair of options relating to the new
     *                           session. None are necessary; all are optional.
     *                           Use the following options:
     *                             - int|null 'duration' The number of minutes
     *                               for the session to last.
     *                             - string|Date|null 'expiresAt' When the
     *                               session should expire.
     *                             - bool|null 'isDownloadable' Should the user
     *                               be allowed to download the original file?
     *                             - bool|null 'isTextSelectable' Should the
     *                               user be allowed to select text?
     *
     * @return Session A new session instance.
     * @throws Exception
     */
    public Session createSession(Map<String, Object> params) throws Exception {
        return Session.create(client, id, params);
    }

    /**
     * Delete a file.
     *
     * @return bool Was the file deleted?
     * @throws Exception
     */
    public Boolean delete() throws Exception {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("httpMethod", "DELETE");
        options.put("rawResponse", true);

        HttpEntity response = requestHttpEntity(client,
                                                path + "/" + id,
                                                null,
                                                null,
                                                options);

        // a successful delete returns nothing, so we return true in that case
        return (response == null);
    }

    /**
     * Download a file using the original extension.
     *
     * @return string The contents of the downloaded file.
     * @throws Exception
     */
    public InputStream download() throws Exception {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("rawResponse", true);

        HttpEntity response = requestHttpEntity(client,
                                                path + "/" + id + "/content",
                                                null,
                                                null,
                                                options);
        InputStream stream = null;

        try {
            stream = response.getContent();
        } catch (IOException e) {
            error(INVALID_RESPONSE_ERROR, e.getMessage());
        }

        return stream;
    }

    /**
     * Download a file using a specific extension.
     *
     * @param string extension The extension to download the file in, which can
     *                         be pdf or zip. If no extension is provided, the
     *                         file will be downloaded using the original
     *                         extension.
     *
     * @return string The contents of the downloaded file.
     * @throws Exception
     */
    public InputStream download(String extension) throws Exception {
        String path = Document.path + "/" + id + "/content." + extension;

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("rawResponse", true);

        HttpEntity response = requestHttpEntity(client,
                                                path,
                                                null,
                                                null,
                                                options);
        InputStream stream  = null;

        try {
            stream = response.getContent();
        } catch (IOException e) {
            error(INVALID_RESPONSE_ERROR, e.getMessage());
        }

        return stream;
    }

    /**
     * Download a thumbnail of a specific size for a file.
     *
     * @param int width The width of the thumbnail in pixels.
     * @param int height The height of the thumbnail in pixels.
     *
     * @return string The contents of the downloaded thumbnail.
     * @throws Exception
     */
    public InputStream thumbnail(Integer width, Integer height)
           throws Exception {
        Map<String, Object> getParams = new HashMap<String, Object>();
        getParams.put("height", height.toString());
        getParams.put("width", width.toString());

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("rawResponse", true);

        HttpEntity response = requestHttpEntity(client,
                                                path + "/" + id + "/thumbnail",
                                                getParams,
                                                null,
                                                options);
        InputStream stream = null;

        try {
            stream = response.getContent();
        } catch (IOException e) {
            error(INVALID_RESPONSE_ERROR, e.getMessage());
        }

        return stream;
    }

    /**
     * Update specific fields for the metadata of a file .
     *
     * @param object fields A key-value pair of the fields to update on the
     *                      file.
     *
     * @return bool Was the file updated?
     * @throws Exception
     */
    public Boolean update(Map<String, Object> fields) throws Exception {
        Map<String, Object> postParams = new HashMap<String, Object>();

        for (String field : updateableFields) {
            if (fields.containsKey(field)
                    && !fields.get(field).toString().isEmpty()) {
                postParams.put(field, fields.get(field));
            }
        }

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("httpMethod", "PUT");

        Map<String, Object> metadata = requestJson(client,
                                                   path + "/" + id,
                                                   null,
                                                   postParams,
                                                   options);
        setValues(metadata);
        return true;
    }

    /**
     * Get a list of all documents.
     *
     * @param Client client The client instance to make requests from.
     *
     * @return array An array containing instances of all documents.
     * @throws Exception
     * @throws ParseException
     */
    public static List<Document> find(Client client)
                  throws Exception, ParseException {
        return find(client, new HashMap<String, Object>());
    }

    /**
     * Get a list of all documents that meet the provided criteria.
     *
     * @param Client client The client instance to make requests from.
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
     * @return array An array containing document instances matching the
     *               request.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static List<Document> find(Client client, Map<String, Object> params)
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

        Map<String, Object> response = requestJson(client,
                                                   path,
                                                   getParams,
                                                   null,
                                                   null);

        if (response.isEmpty()
                || ((Map<String, Object>) response.get("document_collection"))
                   .isEmpty()
                || !((Map<String, Object>) response.get("document_collection"))
                    .containsKey("entries")) {
        String message = "response is not in a valid format.";
            error(INVALID_RESPONSE_ERROR, message);
        }

        Map<String, Object> collection         =
                      (Map<String, Object>) response.get("document_collection");
        List<Map<String, Object>> entries =
                          (List<Map<String, Object>>) collection.get("entries");

        List<Document> documents = new ArrayList<Document>();

        for (Map<String, Object> entry : entries) {
            documents.add(new Document(client, entry));
        }

        return documents;
    }

    /**
     * Get specific fields from the metadata of a file.
     *
     * @param Client client The client instance to make requests from.
     * @param string id The ID of the file to check.
     *
     * @return Document A document instance using data from the API.
     * @throws Exception
     */
    public static Document get(Client client, String id)
                  throws Exception {
        String[] fields   = {"id", "created_at", "name", "status"};
        StringBuilder sb  = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(fields[i]);
        }

        Map<String, Object> getParams = new HashMap<String, Object>();
        getParams.put("fields", sb.toString());

        Map<String, Object> metadata = requestJson(client,
                                                   path + "/" + id,
                                                   getParams,
                                                   null,
                                                   null);

        return new Document(client, metadata);
    }

    /**
     * Upload a local file and return a new document instance.
     *
     * @param Client client The client instance to make requests from.
     * @param File file The file resource to upload.
     *
     * @return Document A new document instance.
     * @throws Exception
     */
    public static Document upload(Client client, File file) throws Exception {
        return upload(client, file, new HashMap<String, Object>());
    }

    /**
     * Upload a local file and return a new document instance.
     *
     * @param Client client The client instance to make requests from.
     * @param File file The file resource to upload.
     * @param object|null params An key-value pair of options relating to the
     *                           file upload. None are necessary; all are
     *                           optional. Us the following options:
     *                             - string|null 'name' Override the filename of
     *                               the file being uploaded.
     *                             - string[]|string|null 'thumbnails' An array
     *                               of dimensions in pixels, with each
     *                               dimension formatted as [width]x[height],
     *                               this can also be a comma-separated string.
     *                             - bool|null 'nonSvg' Create a second version
     *                               of the file that doesn't use SVG, for users
     *                               with browsers that don't support SVG?
     *
     * @return Document A new document instance.
     * @throws Exception
     */
    public static Document upload(Client client,
                                  File file,
                                  Map<String, Object> params)
                  throws Exception {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("file", file);
        options.put("host", FILE_UPLOAD_HOST);

        return upload(client, params, null, options);
    }

    /**
     * Upload a file by URL and return a new document instance.
     *
     * @param Client client The client instance to make requests from.
     * @param String url The URL of the file to upload.
     *
     * @return Document A new document instance.
     * @throws Exception
     */
    public static Document upload(Client client, String url) throws Exception {
        return upload(client, url, new HashMap<String, Object>());
    }

    /**
     * Upload a file by URL and return a new document instance.
     *
     * @param Client client The client instance to make requests from.
     * @param String url The URL of the file to upload.
     * @param object|null params An key-value pair of options relating to the
     *                           file upload. None are necessary; all are
     *                           optional. Us the following options:
     *                             - string|null 'name' Override the filename of
     *                               the file being uploaded.
     *                             - string[]|string|null 'thumbnails' An array
     *                               of dimensions in pixels, with each
     *                               dimension formatted as [width]x[height],
     *                               this can also be a comma-separated string.
     *                             - bool|null 'nonSvg' Create a second version
     *                               of the file that doesn't use SVG, for users
     *                               with browsers that don't support SVG?
     *
     * @return Document A new document instance.
     * @throws Exception
     */
    public static Document upload(Client client,
                                  String url,
                                  Map<String, Object> params)
                  throws Exception {
        Map<String, Object> postParams = new HashMap<String, Object>();
        postParams.put("url", url);

        return upload(client, params, postParams, null);
    }

    /**
     * Update the current document instance with new metadata.
     *
     * @param object data A key-value pair to instantiate the object with.
     *                    Use the following values:
     *                      - string|Date 'createdAt' The date the document was
     *                        created.
     *                      - string 'name' The document title.
     *                      - string 'status' The document status, which can be
     *                        'queued', 'processing', 'done', or 'error'.
     */
    private void setValues(Map<String, Object> data) {
        if (data.containsKey("created_at")) {
            data.put("createdAt", data.get("created_at"));
            data.remove("created_at");
        }

        if (data.containsKey("createdAt")) {
            createdAt = parseDate(data.get("createdAt"));
        }

        if (data.containsKey("name"))   name   = (String) data.get("name");
        if (data.containsKey("status")) status = (String) data.get("status");
    }

    /**
     * Generic upload function used by the two other upload functions, which are
     * more specific than this one, and know how to handle upload by URL and
     * upload from filesystem.
     *
     * @param Client client The client instance to make requests from.
     * @param object|null params A key-value pair of options relating to the
     *                           file upload. Pass-thru from the other upload
     *                           functions.
     * @param object|null postParams A key-value pair of POST params to be sent
     *                               in the body.
     * @param object|null options A key-value pair of request options that may
     *                            modify the way the request is made.
     *
     * @return Document A new document instance.
     * @throws Exception
     */
    private static Document upload(Client client,
                                   Map<String, Object> params,
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

            if (thumbnails instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<String> thumbnailsList = (List<String>) thumbnails;
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < thumbnailsList.size(); i++) {
                    if (i > 0) sb.append(",");
                    sb.append(thumbnailsList.get(i));
                }

                thumbnails = sb.toString();
            }

            postParams.put("thumbnails", thumbnails);
        }

        if (params.containsKey("nonSvg")) {
            postParams.put("non_svg", params.get("nonSvg"));
        }

        Map<String, Object> metadata = requestJson(client,
                                                   path,
                                                   null,
                                                   postParams,
                                                   options);
        return new Document(client, metadata);
    }
}
