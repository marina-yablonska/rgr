package g;

import java.util.function.BooleanSupplier;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import process.Store;
import rnd.Randomable;

public class Visitor extends Actor {

	int id_Visitor;

	// private double waitingForWaiter;
	private double waiterMaxTime;
	// private double waitingForOrder;
	private double orderMaxTime;
	private Randomable rnd;
	private double birthTime;
	private String name;
	private Store goneVisitorCount;
	private BooleanSupplier c;
	private BooleanSupplier c2;
	private QueueForTransactions<Visitor> waitingForOrder;
	private Store eatingVisitor;
	private boolean food = false;

	private QueueForTransactions<Visitor> queueNewVisitor;
	private QueueForTransactions<Visitor> visitorInCafe;
	private QueueForTransactions<Visitor> visitorWaitingForWaiter;

	private int maxSits;

	public double getBirthTime() {
		return birthTime;
	}

	public Visitor(double currentTime) {
		birthTime = currentTime;
		name = "Відвідувач " + birthTime;
	}

	public Visitor(String name, MainGUI gui, TestCafeModel model) {
		super();
		setNameForProtocol(name);
		queueNewVisitor = model.getQueueNewVisitor();
		goneVisitorCount = model.getGoneVisitorCount();
		rnd = gui.getChooseRandomCookingTime().getRandom();
		waiterMaxTime = gui.getChooseDataMaxWaiting().getDouble();
		orderMaxTime = gui.getChooseDataMaxCooking().getDouble();
		eatingVisitor = model.getEatingVisitor();
		visitorInCafe = model.getVisitorInCafe();
		visitorWaitingForWaiter = model.getVisitorWaitingForWaiter();
		waitingForOrder = model.getWaitingForOrder();
		// setWaitingForVisitorHisto(model.getWaitingForVisitorHisto());
		maxSits = gui.getChooseDataSits().getInt();

	}

	private void initConditions() {
		c = () -> visitorInCafe.size() < maxSits;
		c2 = () -> food;
	}

	@Override
	protected void rule() {
		// НА ВУЛИЦІ

		initConditions();
		// візітор підійшов до кафе
		queueNewVisitor.addLast(this);
		waitForConditionOrHoldForTime(c, "Мають бути місця в кафе", waiterMaxTime);

		// У КАФЕ
		// візітор зайшов у кафе
		queueNewVisitor.remove(this);

		if (c.getAsBoolean()) {

			// новий візітор у кафе
			visitorInCafe.add(this);
			// додає себе у чергу візіторів на обслуговування
			visitorWaitingForWaiter.add(this);
		} else {
			// +1 у Store відвідувачів, що пішли
			goneVisitorCount.add(1);
			getDispatcher()
					.printToProtocol(getNameForProtocol() + "Відвідувач не дочекався обслуговування і залишив кафе");
			return;
		}

		waitForConditionOrHoldForTime(c2, "Має бути їжа", orderMaxTime);
		// чекає на їжу
		waitingForOrder.remove(this);

		if (c2.getAsBoolean()) {
			// додає себе у Store відвідувачів, що їдять
			eatingVisitor.add(1);
			holdForTime(rnd.next());
			// поїв і віднімає зі Store відвідувачів, що їдять
			eatingVisitor.remove(1);
			getDispatcher().printToProtocol(getNameForProtocol() + "Відвідувач розрахувався і залишив кафе");
		} else {
			// візітор не дочекався замовлення і додав себе у Store
			// відвідувачів, що пішли
			goneVisitorCount.add(1);
			getDispatcher().printToProtocol(getNameForProtocol() + "Відвідувач не дочекався замовлення і залишив кафе");
		}
		// відвідувач покинув кафе
		visitorInCafe.remove(this);
	}

	public void setStartTime(double currentTime) {
		// TODO Auto-generated method stub

	}
	public boolean isFood() {
		return food;
	}

	public void setFood(boolean food) {
		this.food = food;
	}

}
