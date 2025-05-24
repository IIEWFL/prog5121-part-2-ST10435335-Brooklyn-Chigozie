/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrationloginapp;

/**
 *
 * @author brook
 */

import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;
import java.util.Random;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public final class Message {
    private String messageID;
    private final int messageNumber;
    private final String recipientCell;
    private final String messageContent;
    private final String messageHash;

    private static int messageCount = 0;
    private static final List<Message> sentMessages = new ArrayList<>();

    public Message(String recipientCell, String messageContent) {
        this.messageID = generateMessageID();
        Message.messageCount++;
        this.messageNumber = Message.messageCount;
        this.recipientCell = recipientCell;
        this.messageContent = messageContent;
        this.messageHash = createMessageHash();
    }

    // Method to generate a random ten-digit Message ID
    private String generateMessageID() {
        Random random = new Random();
        long id = random.nextLong(9_000_000_000L) + 1_000_000_000L; // Ensure 10 digits
        return String.valueOf(id);
    }

    // Getter for messageID
    public String getMessageID() {
        return this.messageID;
    }

    // Setter for messageID
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    // Getter for messageContent
    public String getMessageContent() {
        return this.messageContent;
    }

    // Method to check if the message ID is not more than ten characters long
    public boolean checkMessageID() {
        return this.messageID.length() <= 10;
    }

    // Method to check if the recipient cell number is not more than ten characters long and starts with +27
    public int checkRecipientCell() {
        if (recipientCell.length() > 10 || !recipientCell.startsWith("+27")) {
            return 1; // Error code for invalid format
        }
        return 0; // 0 for valid
    }

    // Method to create and return the Message Hash
    public String createMessageHash() {
        String firstTwoID = this.messageID.substring(0, Math.min(2, this.messageID.length()));
        String firstWord = "";
        String lastWord = "";
        String[] words = this.messageContent.trim().split("\\s+");
        if (words.length > 0) {
            firstWord = words[0];
            if (words.length > 1) {
                lastWord = words[words.length - 1];
            } else {
                lastWord = firstWord;
            }
        }
        return (firstTwoID + ":" + this.messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // Method to handle sending, disregarding, or storing the message
    public String SentMessage(int choice) {
        switch (choice) {
            case 1 -> {
                // Send Message
                Message.sentMessages.add(this);
                JOptionPane.showMessageDialog(null, """
                                                    Message sent successfully!
                                                    Message ID: """ + this.messageID + "\n" +
                                "Message Hash: " + this.messageHash + "\n" +
                                        "Recipient: " + this.recipientCell + "\n" +
                                                "Message: " + this.messageContent);
                return "Message successfully sent.";
            }
            case 2 -> {
                // Disregard Message
                return "Press 0 to delete message.";
            }
            case 3 -> {
                // Store Message to send later
                this.storeMessage(); // Call the storeMessage() method
                return "Message successfully stored.";
            }
            default -> {
                return "Invalid choice.";
            }
        }
    }

    // Method to return the total number of messages sent
    public static int returnTotalMessages() {
        return Message.sentMessages.size();
    }

    // Method to return a list of all messages sent (formatted as a String)
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder sb = new StringBuilder("--- Sent Messages ---\n");
        for (Message msg : sentMessages) {
            sb.append("Message ID: ").append(msg.messageID).append("\n");
            sb.append("Message Hash: ").append(msg.messageHash).append("\n");
            sb.append("Recipient: ").append(msg.recipientCell).append("\n");
            sb.append("Message: ").append(msg.messageContent).append("\n");
            sb.append("-----------------------\n");
        }
        return sb.toString();
    }

    // Method to store the message in a JSON file
    public void storeMessage() {
        JSONObject messageJson = new JSONObject();
        messageJson.put("messageID", this.messageID);
        messageJson.put("messageNumber", this.messageNumber);
        messageJson.put("recipientCell", this.recipientCell);
        messageJson.put("messageContent", this.messageContent);
        messageJson.put("messageHash", this.messageHash);

        try (FileWriter file = new FileWriter("stored_messages.json", true)) { // Append to the file
            file.write(messageJson.toString(4) + "\n"); // Use toString(4) for pretty printing
            file.flush();
            JOptionPane.showMessageDialog(null, "Message stored successfully for later sending.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error storing message.");
        }
    }
}
