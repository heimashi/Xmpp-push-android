package com.xmpppush.client;

import org.jivesoftware.smack.packet.IQ;

public class NotificationReceivedIQ extends IQ {

	private String uuid;
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<").append("notification_received").append(" xmlns=\"").append(
                "pushserver:iq:notification_received").append("\">");
        if (uuid != null) {
            buf.append("<uuid>").append(uuid).append("</uuid>");
        }
        buf.append("</").append("notification_received").append("> ");
        return buf.toString();
	}

}
