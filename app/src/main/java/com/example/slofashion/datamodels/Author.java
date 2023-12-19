package com.example.slofashion.datamodels;

import com.stfalcon.chatkit.commons.models.IUser;

public class Author implements IUser {
    final private String id;
    final private String name;

    public Author(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return null;
    }
}