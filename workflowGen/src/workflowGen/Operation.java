package workflowGen;
interface Item {
    double getWeight();
    String getOperation();
}
public class Operation implements Item{

	public double weight =0;
	public String operation ="";
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	@Override
	public double getWeight() {
		return weight;
	}
	public Operation(double weight, String operation) {
		super();
		this.weight = weight;
		this.operation = operation;
	}

}
