package obj;

public class Status {
	private String statusCode;
	private Object content;
	
	public Status(String statusCode,Object jsonString) {
		this.setStatusCode(statusCode);
		this.setContent(jsonString);
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
}
