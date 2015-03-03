package com.box.view;

/**
 * Provides access to the Box View API.
 */
public abstract class Client {
    /**
     * The developer's Box View API key.
     *
     * @var string
     */
    private static String apiKey;

    /**
     * Get the API key.
     *
     * @return string The API key.
     */
    public static String getApiKey() {
        return apiKey;
    }

    /**
     * Set the API key.
     *
     * @param string $apiKey The API key.
     *
     * @return void No return value.
     */
    public static void setApiKey(String apiKey) {
        Client.apiKey = apiKey;
    }
}

