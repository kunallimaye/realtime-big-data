package com.redhat.example.data;

import java.io.Serializable;

public class WordCount implements Serializable {

	private static final long serialVersionUID = 1L;

	private String word;
	private int count;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String toString() {
		return word + "=" + count;
	}
}
