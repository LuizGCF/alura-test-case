package com.alura.coursecase.utils;

public class EmailSender {

    private final static String SUBJECT_PREFIX = "A new feedback with rating lower than 6 has been posted on your course %s";
    private final static String BODY_PREFIX = "Hi, %s.\nHere is the recent feedback on your course:\n\n%s";

    public static void send(String recipientEmail, String recipientName, String courseName, String body) {
        System.out.println("Simulating sending email to [%s]:\n".formatted(recipientEmail));
        System.out.println("\"Subject: %s\n Body: %s\"".formatted(SUBJECT_PREFIX.formatted(courseName), BODY_PREFIX.formatted(recipientName, body)));
    }
}
