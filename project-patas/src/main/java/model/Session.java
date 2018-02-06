package model;

public class Session {
	private String key;
	private String value;
	private Long duration;
	private Long createdTime;
	
	public Session() {
	}
	
	public Session(String key, String value, Long duration,
			Long createdTime) {
		super();
		this.key = key;
		this.value = value;
		this.duration = duration;
		this.createdTime = createdTime;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public Long getDuration() {
		return duration;
	}
	
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
	public Long getCreatedTime() {
		return createdTime;
	}
	
	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}
}
