package com.newtrendz.pass.exceptions;

public class DuplicateEntryException extends RuntimeException{
    public DuplicateEntryException(String message){
        super(message);
    }

    public DuplicateEntryException(String message, Throwable throwable){
        super(message);
    }
}
