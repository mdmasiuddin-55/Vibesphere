package com.vibesphere.models;

import java.sql.Timestamp;

public class Vibe {
    private long id;
    private long userId;
    private String caption;
    private String mediaUrl;
    private String mediaType;
    private Timestamp createdAt;
    private String username;
    private String displayName;
    private String profilePicUrl;
    private int mochaCount;

    // getters & setters omitted for brevity -- add them similarly
    public long getId(){ return id; }
    public void setId(long id){ this.id = id; }
    public long getUserId(){ return userId; }
    public void setUserId(long u){ userId = u; }
    public String getCaption(){ return caption; }
    public void setCaption(String c){ caption = c; }
    public String getMediaUrl(){ return mediaUrl; }
    public void setMediaUrl(String m){ mediaUrl = m; }
    public String getMediaType(){ return mediaType; }
    public void setMediaType(String t){ mediaType = t; }
    public java.sql.Timestamp getCreatedAt(){ return createdAt; }
    public void setCreatedAt(java.sql.Timestamp t){ createdAt = t; }
    public String getUsername(){ return username; }
    public void setUsername(String u){ username = u; }
    public String getDisplayName(){ return displayName; }
    public void setDisplayName(String d){ displayName = d; }
    public String getProfilePicUrl(){ return profilePicUrl; }
    public void setProfilePicUrl(String p){ profilePicUrl = p; }
    public int getMochaCount(){ return mochaCount; }
    public void setMochaCount(int m){ mochaCount = m; }
}
