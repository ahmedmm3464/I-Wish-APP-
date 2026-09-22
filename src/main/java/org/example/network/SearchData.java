package org.example.network;

import java.io.Serializable;

public class SearchData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String query;
    private final int currentUserId;

    public SearchData(String query, int currentUserId) {
        this.query = query;
        this.currentUserId = currentUserId;
    }

    public String getQuery() {
        return query;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }
}