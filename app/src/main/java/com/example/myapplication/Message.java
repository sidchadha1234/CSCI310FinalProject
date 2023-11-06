package com.example.myapplication;
public class Message {
    private String senderEmail;
    private String receiverEmail;
    private String message;
    private long timestamp; // Use System.currentTimeMillis() to generate the timestamp

    // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    public Message() {
    }

    // Parameterized constructor
    public Message(String senderEmail, String receiverEmail, String message) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    // Getter and setter for senderEmail
    public String getSenderId() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    // Getter and setter for receiverEmail
    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    // Getter and setter for message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter and setter for timestamp
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
