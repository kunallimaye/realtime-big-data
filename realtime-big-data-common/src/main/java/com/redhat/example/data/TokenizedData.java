package com.redhat.example.data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TokenizedData implements Externalizable {

	private static final long serialVersionUID = 1L;

	private static final String NULL_EX = "NULL_EX";
	
	private String source;

	private String region;
	
	private String key;

	private String surfaceForm;
	
	private String allFeatures;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getSurfaceForm() {
		return surfaceForm;
	}

	public void setSurfaceForm(String surfaceForm) {
		this.surfaceForm = surfaceForm;
	}

	public String getAllFeatures() {
		return allFeatures;
	}

	public void setAllFeatures(String allFeatures) {
		this.allFeatures = allFeatures;
	}
	
	@Override
	public String toString() {
		return surfaceForm + ":" + allFeatures;
	}

	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(source == null ? NULL_EX : source);
		out.writeUTF(region == null ? NULL_EX : region);
		out.writeUTF(key == null ? NULL_EX : key);
		out.writeUTF(surfaceForm == null ? NULL_EX : surfaceForm);
		out.writeUTF(allFeatures == null ? NULL_EX : allFeatures);
		
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		source = in.readUTF();
		source = NULL_EX.equals(source) ? null : source;
		
		region = in.readUTF();
		region = NULL_EX.equals(region) ? null : region;

		key = in.readUTF();
		key = NULL_EX.equals(key) ? null : key;
		
		surfaceForm = in.readUTF();
		surfaceForm = NULL_EX.equals(surfaceForm) ? null : surfaceForm;
		
		allFeatures = in.readUTF();
		allFeatures = NULL_EX.equals(allFeatures) ? null : allFeatures;
		
	}
	
}
