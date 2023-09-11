package ru.dolya.dossier.dto;

public enum Theme {
    APPLICATION_DENIED ("Application denied"),
    CREATE_DOCUMENT ("Create documents"),
    CREDIT_ISSUED ("Credit issued"),
    FINISH_REGISTRATION ("Finish registration"),
    SEND_DOCUMENTS ("Your loan documents"),
    SEND_SES ("SES code");
    private final String message;
    Theme(String message) {
        this.message = message;    }

    public String getMessage() {
        return message;
    }
}
