package com.recordcollection.recorddatabase.payloads.request;

import com.recordcollection.recorddatabase.models.Comment;
import com.recordcollection.recorddatabase.models.Record;

import java.util.Set;

public class UpdateAuthRequest {
    private String username;
    private String password;
    private String name;
    private Set<Record> records;
    private Set<Comment> comments;

    private CurrentUserInfo userInfo;

    public UpdateAuthRequest() {
    }

    public UpdateAuthRequest(String username, String password, String name, Set<Record> records, Set<Comment> comments, CurrentUserInfo userInfo) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.records = records;
        this.comments = comments;
        this.userInfo = userInfo;
    }

    public class CurrentUserInfo {
        private String username;
        private String password;

        public CurrentUserInfo() {
        }

        public CurrentUserInfo(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Set<Record> getRecords() {
        return records;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public CurrentUserInfo getUserInfo() {
        return userInfo;
    }
}
