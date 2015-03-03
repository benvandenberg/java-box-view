package com.box.view;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;

/**
 * Provide access to the Box View Session API. The Session API is used to create
 * sessions for specific documents that can be used to view a document using a
 * specific session-based URL.
 */
public class Session extends Base {
    /**
     * The request handler.
     *
     * @var Request|null
     */
    protected static Request _requestHandler;

    /**
     * The Session API path relative to the base API path.
     *
     * @var string
     */
    public static final String path = "/sessions";

    /**
     * Create a session for a specific document by ID that may expire.
     *
     * @param string id The id of the file to create a session for.
     *
     * @return object A key-value pair representing the metadata of the session.
     * @throws Exception
     */
    public static Map<String, Object> create(String id) throws Exception {
        Map<String, Object> postParams = new HashMap<String, Object>();
        postParams.put("document_id", id);

        return requestJson(Session.path, null, postParams, null);
    }

    /**
     * Create a session for a specific document by ID that may expire.
     *
     * @param string id The id of the file to create a session for.
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
     * @return object A key-value pair representing the metadata of the session.
     * @throws Exception
     */
    public static Map<String, Object> create(String id,
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
                expiresAtString = expiresAt.toString();
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

        return requestJson(Session.path, null, postParams, null);
    }

    /**
     * Delete a session by ID.
     *
     * @param string id The ID of the session to delete.
     *
     * @return bool Was the session deleted?
     * @throws Exception
     */
    public static Boolean delete(String id) throws Exception {
        String path = Session.path + "/" + id;

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("httpMethod", "DELETE");
        options.put("rawResponse", true);

        HttpEntity response = requestHttpEntity(path, null, null, options);

        // a successful delete returns nothing, so we return true in that case
        return (response == null);
    }
}
