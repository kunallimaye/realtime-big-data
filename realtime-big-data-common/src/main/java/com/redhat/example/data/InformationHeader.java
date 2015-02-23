package com.redhat.example.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class InformationHeader implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;
	
	/** 情報源１(自治体、時事通信等) **/
	private String source1 = "UNKNOWN";

	/** 情報源2(自治体名等) **/
	private String source2 = "UNKNOWN";

	/** 地域1(関東甲信越等) **/
	private String region1 = "UNKNOWN";

	/** 地域2(県) **/
	private String region2 = "UNKNOWN";

	/** 地域3(市町村) **/
	private String region3 = "UNKNOWN";
	
	/** データの発生日付 **/
	private String businessDate;
	
	/** データの投入日付 **/
	private String systemDate;
	

	public InformationHeader() {
		Date current = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		businessDate = sdf1.format(current);
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S");
		systemDate = sdf2.format(current);
	}

	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}

	public String getSystemDate() {
		if(systemDate == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S");
			systemDate = sdf.format(new Date());
		}
		return systemDate;
	}

	public void setSystemDate(String systemDate) {
		this.systemDate = systemDate;
	}

	public String getKey() {
		if(key == null) {
			UUID uuid = UUID.randomUUID();
			key = uuid.toString();
		}
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSource1() {
		return source1;
	}

	public void setSource1(String source1) {
		this.source1 = source1;
	}

	public String getSource2() {
		return source2;
	}

	public void setSource2(String source2) {
		this.source2 = source2;
	}

	public String getRegion1() {
		return region1;
	}

	public void setRegion1(String region1) {
		this.region1 = region1;
	}

	public String getRegion2() {
		return region2;
	}

	public void setRegion2(String region2) {
		this.region2 = region2;
	}

	public String getRegion3() {
		return region3;
	}

	public void setRegion3(String region3) {
		this.region3 = region3;
	}
}
