package com.sung.family.models;

public enum StatusType
{
    OK(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    CONFLICT(409),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    StatusType(int type)
    {
        this.code = type;
    }

    @Override
    public String toString()
    {
        return String.valueOf(this.code);
    }

    public int getType()
    {
        return this.code;
    }
}

