package obj;

public class UserTransmissionGson {
	private String operation;
	private String name;
    private String pass;
    

    public UserTransmissionGson(String operation, String name, String password){
        this.operation= operation;
        this.name = name;
        this.pass = password;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
}
