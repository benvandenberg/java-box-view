package com.box.view;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Provides access to the Box View API.
 */
public class BoxViewClient {
    /**
     * The developer's Box View API key.
     */
    private String apiKey;

    /**
     * The request handler.
     */
    private Request requestHandler;

    /**
     * Instantiate the client.
     *
     * @param apiKey The API key to use.
     */
    public BoxViewClient(String apiKey) {
        setApiKey(apiKey);
    }

    /**
     * Get a list of all documents.
     *
     * @return An array containing a list of all documents.
     * @throws BoxViewException
     * @throws ParseException
     */
    public List<Document> findDocuments()
           throws BoxViewException, ParseException {
        return Document.find(this);
    }

    /**
     * Get a list of all documents that meet the provided criteria.
     *
     * @param params A key-value pair to filter the list of all documents
     *               uploaded. None are necessary; all are optional. Use the
     *               following options:
     *                  - int|null 'limit' The number of documents to return.
     *                  - string|Date|null 'createdBefore' Upper date limit to
     *                    filter by.
     *                  - string|Date|null 'createdAfter' Lower date limit to
     *                    filter by.
     *
     * @return An array containing document instances matching the request.
     * @throws BoxViewException
     * @throws ParseException
     */
    public List<Document> findDocuments(Map<String, Object> params)
           throws BoxViewException, ParseException {
        return Document.find(this, params);
    }

    /**
     * Get the API key.
     *
     * @return The API key.
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Create a new document instance by ID, and load it with values requested
     * from the API.
     *
     * @param id The document ID.
     *
     * @return A document instance using data from the API.
     * @throws BoxViewException
     */
    public Document getDocument(String id) throws BoxViewException {
        return Document.get(this, id);
    }

    /**
     * Return the request handler.
     *
     * @return The request handler.
     */
    public Request getRequestHandler() {
        if (requestHandler == null) {
            setRequestHandler(new Request(getApiKey()));
        }

        return requestHandler;
    }

    /**
     * Set the API key.
     *
     * @param apiKey The API key.
     *
     * @return void
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Set the request handler.
     *
     * @param requestHandler The request handler.
     *
     * @return void
     */
    public void setRequestHandler(Request requestHandler) {
        this.requestHandler = requestHandler;
    }

    /**
     * Upload a local file and return a new document instance.
     *
     * @param file The file resource to upload.
     *
     * @return A new document instance.
     * @throws BoxViewException
     */
    public Document upload(File file) throws BoxViewException {
        return Document.upload(this, file);
    }

    /**
     * Upload a local file and return a new document instance.
     *
     * @param file The file resource to upload.
     * @param params A key-value pair of options relating to the file upload.
     *               None are necessary; all are optional. Use the following
     *               options:
     *                 - string|null 'name' Override the filename of the file
     *                   being uploaded.
     *                 - string[]|string|null 'thumbnails' An array of
     *                   dimensions in pixels, with each dimension formatted as
     *                   [width]x[height], this can also be a comma-separated
     *                   string.
     *                 - bool|null 'nonSvg' Create a second version of the file
     *                   that doesn't use SVG, for users with browsers that
     *                   don't support SVG?
     *
     * @return A new document instance.
     * @throws BoxViewException
     */
    public Document upload(File file, Map<String, Object> params)
           throws BoxViewException {
        return Document.upload(this, file, params);
    }

    /**
     * Upload a file by URL and return a new document instance.
     *
     * @param url The URL of the file to upload.
     *
     * @return A new document instance.
     * @throws BoxViewException
     */
    public Document upload(String url) throws BoxViewException {
        return Document.upload(this, url);
    }

    /**
     * Upload a file by URL and return a new document instance.
     *
     * @param url The URL of the file to upload.
     * @param params A key-value pair of options relating to the file upload.
     *               None are necessary; all are optional. Use the following
     *               options:
     *                 - string|null 'name' Override the filename of the file
     *                   being uploaded.
     *                 - string[]|string|null 'thumbnails' An array of
     *                   dimensions in pixels, with each dimension formatted as
     *                   [width]x[height], this can also be a comma-separated
     *                   string.
     *                 - bool|null 'nonSvg' Create a second version of the file
     *                   that doesn't use SVG, for users with browsers that
     *                   don't support SVG?
     *
     * @return A new document instance.
     * @throws BoxViewException
     */
    public Document upload(String url, Map<String, Object> params)
           throws BoxViewException {
        return Document.upload(this, url, params);
    }
}

