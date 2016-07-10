package com.jiuzhang.guojing.dribbbo.model;

import java.util.Date;
import java.util.List;

public class Shot {
    public int likeCount;
    public int viewCount;
    public int bucketCount;
    public String imageUrl;

    public Date createdAt;
    public String title;
    public String description;
    public User author;

    public List<Comment> comments;
}
