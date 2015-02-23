package com.redhat.example.data;

import java.io.Serializable;

public class InformationEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	private InformationHeader header;
	private String title;
	private String content;

	public InformationHeader getHeader() {
		return header;
	}

	public void setHeader(InformationHeader header) {
		this.header = header;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
