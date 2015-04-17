package com.box.view;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;

/**
 * Provide access to the Box View Session API. The Session API is used to create
 * sessions for specific documents that can be used to view a document using a
 * specific session-based URL.
 *
 * Session objects have the following fields:
 *   - Box\View\Document 'document' The document the session was created for.
 *   - string 'id' The session ID.
 *   - string 'expiresAt' The date the session was created.
 *   - array 'urls' An associative array of URLs for 'assets', 'realtime', and
 *                  'view'.
 *
 * When creating a session, the following parameters can be set:
 *   - int|null 'duration' The number of minutes for the session to last.
 *   - string|DateTime|null 'expiresAt' When the session should expire.
 *   - bool|null 'isDownloadable' Should the user be allowed to download the
 *                                original file?
 *   - bool|null 'isTextSelectable' Should the user be allowed to select text?
 */
public class Session extends Base {
    /**
     * The Session API path relative to the base API path.
     *
     * @var string
     */
    public static final String path = "/sessions";

    /**
     * The document that created this session.
     */
    private Document document;

    /**
     * The session ID.
     */
    private String id;

    /**
     * The date the session expires, formatted as RFC 3339.
     */
    private Date expiresAt;

    /**
     * The URLs for a session.
     */
    private Map<String, String> urls = new HashMap<String, String>();

    /**
     * Instantiate the session.
     *
     * @param client client The client instance to make requests from.
     * @param object data An associative array to instantiate the object with.
     *                    Use the following values:
     *                      - Document 'document' The document the session was
     *                        created for.
     *                      - string 'id' The session ID.
     *                      - string 'expiresAt' The date the session was
     *                        created.
     *                      - array 'urls' A key-value pair of URLs for
     *                        'assets', 'realtime', and 'view'.
     */
    public Session(Client client, Map<String, Object> data) {
        this.client = client;
        id          = (String) data.get("id");

        setValues(data);
    }

    /**
     * Get the document the session was created for.
     *
     * @return Document The document the session was created for.
     */
    public Document document() {
        return document;
    }

    /**
     * Get the session ID.
     *
     * @return string The session ID.
     */
    public String id() {
        return id;
    }

    /**
     * Get the date the session expires, formatted as RFC 3339.
     *
     * @return Date The date the session expires, formatted as RFC 3339.
     */
    public Date expiresAt() {
        return expiresAt;
    }

    /**
     * Get the session assets URL.
     *
     * @return string The session assets URL.
     */
    public String assetsUrl() {
        return urls.get("assets");
    }

    /**
     * Get the session realtime URL.
     *
     * @return string The session realtimes URL.
     */
    public String realtimeUrl() {
        return urls.get("realtime");
    }

    /**
     * Get the session view URL.
     *
     * @return string The session view URL.
     */
    public String viewUrl() {
        return urls.get("view");
    }

    /**
     * Delete a session.
     *
     * @return bool Was the session deleted?
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
     * Create a session for a specific document by ID that may expire.
     *
     * @param Client client The client instance to make requests from.
     * @param string id The ID of the file to create a session for.
     *
     * @return Session A new session instance.
     * @throws Exception
     */
    public static Session create(Client client, String id) throws Exception {
        Map<String, Object> postParams = new HashMap<String, Object>();
        postParams.put("document_id", id);

        Map<String, Object> metadata = requestJson(client,
                                                   path,
                                                   null,
                                                   postParams,
                                                   null);
        return new Session(client, metadata);
    }

    /**
     * Create a session for a specific document by ID that may expire.
     *
     * @param Client client The client instance to make requests from.
     * @param string id The ID of the file to create a session for.
     * @param object|null params A key-value pair of options relating to the new
     *                           session. None are necessary; all are optional.
     *                           Use the following options:
     *                             - integer|null 'duration' The number of
     *                               minutes for the session to last.
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
    public static Session create(Client client,
                                 String id,
                                 Map<String, Object> params)
                  throws Exception {
        Map<String, Object> postParams = new HashMap<String, Object>();
        postParams.put("document_id",  id);

        if (params.containsKey("duration")) {
            postParams.put("duration", params.get("duration"));
        }

        if (params.containsKey("expiresAt")) {
            Object expiresAt = params.get("expiresAt");
            String expiresAtString;

            if (expiresAt instanceof Date) {
                expiresAtString = date((Date) expiresAt);
            } else {
                expiresAtString = (String) expiresAt;
            }

            postParams.put("expires_at", expiresAtString);
        }

        if (params.containsKey("isDownloadable")) {
            postParams.put("is_downloadable", params.get("isDownloadable"));
        }

        if (params.containsKey("isTextSelectable")) {
            Boolean isTextSelectable = (Boolean) params.get("isTextSelectable");
            postParams.put("is_text_selectable", isTextSelectable);
        }

        Map<String, Object> metadata = requestJson(client,
                                                   path,
                                                   null,
                                                   postParams,
                                                   null);
        return new Session(client, metadata);
    }

    /**
     * Update the current document instance with new metadata.
     *
     * @param object data A key-value pair to instantiate the object with. Use
     *                    the following values:
     *                      - Box\View\Document 'document' The document the
     *                        session was created for.
     *                      - string 'expiresAt' The date the session was
     *                        created.
     *                      - array 'urls' A key-value pair of URLs for
     *                        'assets', 'realtime', and 'view'.
     */
    private void setValues(Map<String, Object> data) {
        if (data.containsKey("document")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> documentMap = (Map<String, Object>)
                                              data.get("document");
            document = new Document(client, documentMap);
        }

        if (data.containsKey("expires_at")) {
            data.put("expiresAt", data.get("expires_at"));
            data.remove("expires_at");
        }

        if (data.containsKey("expiresAt")) {
            expiresAt = parseDate(data.get("expiresAt"));
        }

        if (data.containsKey("urls") && data.get("urls") instanceof Map<?, ?>) {
            @SuppressWarnings("unchecked")
            Map<String, String> urls = (Map<String, String>) data.get("urls");

            if (urls.containsKey("assets")) {
                this.urls.put("assets", urls.get("assets"));
            }

            if (urls.containsKey("realtime")) {
                this.urls.put("realtime", urls.get("realtime"));
            }

            if (urls.containsKey("view")) {
                this.urls.put("view", urls.get("view"));
            }
        }
    }
}
