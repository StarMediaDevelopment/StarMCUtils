package com.starmediadev.plugins.starmcutils.command;

import java.util.List;

public class Argument {
    protected String name;
    protected boolean required;
    protected String errorMessage;
    
    public Argument(String name) {
        this.name = name;
    }
    
    public Argument(String name, boolean required, String errorMessage) {
        this.name = name;
        this.required = required;
        this.errorMessage = errorMessage;
    }
    
    public List<String> getCompletions() {
        return null;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isRequired() {
        return required;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
}
