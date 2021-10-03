package com.example.s13;

public class chatObj {

    private String chatID;



    private String receiverName;
    public chatObj(String chatID, String receiverName) {
        this.chatID = chatID;
        this.receiverName = receiverName;
    }


    public String getReceiverName() {
        return receiverName;
    }

    public String getChatID() {
        return chatID;
    }




}
