package com.xmpppush.client;

import org.jivesoftware.smack.packet.IQ;

public class SetAliasIQ extends IQ {

	private String usename;
	
	private String alias;
	
	public String getUsename() {
		return usename;
	}

	public void setUsename(String usename) {
		this.usename = usename;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<").append("setalias").append(" xmlns=\"").append(
                "pushserver:iq:setalias").append("\">");
        if (usename != null) {
            buf.append("<usename>").append(usename).append("</usename>");
        }
        if (alias != null) {
            buf.append("<alias>").append(alias).append("</alias>");
        }
        buf.append("</").append("setalias").append("> ");
        return buf.toString();
	}

}
