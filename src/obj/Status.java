package obj;

public class Status {
	private String statusCode;
	private String content;
	
	public Status(String statusCode,String jsonString) {
		this.setStatusCode(statusCode);
		this.setContent(jsonString);
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
