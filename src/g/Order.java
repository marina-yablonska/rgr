package g;

public class Order {
	String name;
	int countTest;
	boolean ok;
	double birthTime;
	int id;
	private Visitor visitor;

	public double getBirthTime() {
		return birthTime;
	}

	public Order(double currentTime, Visitor visitor, Waiter waiter) {
		birthTime = currentTime;
		name = "Замовлення " + birthTime;
		this.setVisitor(visitor);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCountTest() {
		return countTest;
	}

	public void setCountTest() {
		countTest++;
	}

	public Visitor getVisitor() {
		return visitor;
	}

	public void setVisitor(Visitor visitor) {
		this.visitor = visitor;
	}

}