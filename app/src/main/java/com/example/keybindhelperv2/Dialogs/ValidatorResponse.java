package com.example.keybindhelperv2.Dialogs;

public class ValidatorResponse {
    public String invalidMessage;
    public Boolean isValid;

    public ValidatorResponse(Boolean isValid, String invalidMessage) {
        this.isValid = isValid;
        this.invalidMessage = invalidMessage;
    }
}