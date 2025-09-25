package com.vibesphere.models;

public class User {
    private long id;
    private String username;
    private String email;
    private String displayName;
    private String profilePicUrl;
    private String bannerUrl;

    // getters & setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String u) { username = u; }
    public String getEmail() { return email; }
    public void setEmail(String e) { email = e; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String d) { displayName = d; }
    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String p) { profilePicUrl = p; }
    public String getBannerUrl() { return bannerUrl; }
    public void setBannerUrl(String b) { bannerUrl = b; }
}
