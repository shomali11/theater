package com.jpmc.theater.models;

public enum MovieCode {
    NORMAL(0),
    SPECIAL(1);

    public final int id;

    MovieCode(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
