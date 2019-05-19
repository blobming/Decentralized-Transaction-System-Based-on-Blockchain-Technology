package obj;

public class TransGson {

    private double amount;
    private String payerPk;
    private String payerPRK;
    private String payeePkHash;

    public TransGson(String payer1, String payer2,String paye, double amo){
        payerPk = payer1;
        payerPRK = payer2;
        payeePkHash = paye;
        amount = amo;

    }

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPayerPk() {
		return payerPk;
	}

	public void setPayerPk(String payerPk) {
		this.payerPk = payerPk;
	}

	public String getPayerPRK() {
		return payerPRK;
	}

	public void setPayerPRK(String payerPRK) {
		this.payerPRK = payerPRK;
	}

	public String getPayeePkHash() {
		return payeePkHash;
	}

	public void setPayeePkHash(String payeePkHash) {
		this.payeePkHash = payeePkHash;
	}

}
