package com.example.s13;

public class messageObj {
    private String msg;
    private String msgID;
    private String senderID;

    public messageObj(String msg, String msgID, String senderID) {
        this.msg = msg;
        this.msgID = msgID;
        this.senderID = senderID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
}
