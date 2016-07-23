package com.jiuzhang.guojing.dribbbo.model;

import java.util.Date;
import java.util.Map;

public class Shot {

    public static final String IMAGE_NORMAL = "normal";
    public static final String IMAGE_HIDPI = "hidpi";

    public String id;
    public String title;
    public String description;

    public int width;
    public int height;
    public Map<String, String> images;

    public int views_count;
    public int likes_count;
    public int buckets_count;

    public Date created_at;

    public User user;

    public boolean liked;
}
