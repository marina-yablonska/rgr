package g;

import java.util.function.BooleanSupplier;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import process.Store;
import rnd.Randomable;

public class Visitor extends Actor {

	private double waiterMaxTime;
	private double orderMaxTime;
	private Randomable rnd;
	private double birthTime;
	private String name;
	private Store goneVisitorCount;
	private Store eatingVisitor;
	private boolean food = false;
	private BooleanSupplier c;
	private BooleanSupplier c2;
	private int maxSits;

	private QueueForTransactions<Visitor> waitingForOrder;
	private QueueForTransactions<Visitor> queueNewVisitor;
	private QueueForTransactions<Visitor> visitorInCafe;
	private QueueForTransactions<Visitor> visitorWaitingForWaiter;
	
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
		queueNewVisitor = model.getQueueNewVisitor(); // черга нових візіторів
		goneVisitorCount = model.getGoneVisitorCount(); // черга візіторів, що пішли не дочекавшись
		rnd = gui.getChooseRandomCookingTime().getRandom(); // затримка на споживання страви
		waiterMaxTime = gui.getChooseDataMaxWaiting().getDouble(); // критичний час заходу в кафе 
		orderMaxTime = gui.getChooseDataMaxCooking().getDouble(); // критичний час очікування страви
		eatingVisitor = model.getEatingVisitor(); // Store візіторів, що їдять
		visitorInCafe = model.getVisitorInCafe(); // візітори у кафе (повний час)
		visitorWaitingForWaiter = model.getVisitorWaitingForWaiter(); //візітори чекають на офіціанта
		waitingForOrder = model.getWaitingForOrder(); //візітори чекають на замовлення
		maxSits = gui.getChooseDataSits().getInt(); // кількість місць у кафе
	}

	private void initConditions() {
		// перевірка, чи є вільні місця у кафе
		c = () -> visitorInCafe.size() < maxSits;
		// перевірка, чи винесли відвідувачу страву
		c2 = () -> food;
	}

	@Override
	protected void rule() {
		// НА ВУЛИЦІ

		initConditions();
		// візітор підійшов до кафе, додає себе у чергу на вхід у кафе
		queueNewVisitor.addLast(this);
		// перевіряє, чи є місця у кафе:
		// waiterMaxTime - критичний час на очікування вільного місця у кафе
		waitForConditionOrHoldForTime(c, "Мають бути місця в кафе", waiterMaxTime);

		// У КАФЕ
		
		// візітор зайшов у кафе, видаляє себе з черги на вхід у кафе
		queueNewVisitor.remove(this);
		
		// якщо місця були і відвідувач потрапив к кафе до перебільшення критичного часу
		if (c.getAsBoolean()) {
			// додає себе у чергу візіторів всередині кафе
			visitorInCafe.add(this);
			// додає себе у чергу візіторів на обслуговування
			visitorWaitingForWaiter.add(this);
			
		// якщо місця не було і критичний час перебільшено 	
		} else {
			// +1 у Store відвідувачів, що пішли
			goneVisitorCount.add(1);
			getDispatcher()
					.printToProtocol(getNameForProtocol() + "Відвідувач не дочекався обслуговування і залишив кафе");
			return;
		}
		// перевірка умови, що офіціант має принести страву
		// orderMaxTime - критичний час на очікування страви
		waitForConditionOrHoldForTime(c2, "Має бути їжа", orderMaxTime);
		// чекає на їжу
		waitingForOrder.remove(this);

		// якщо страву принесли до перебільшення критичного часу
		if (c2.getAsBoolean()) {
			// додає себе у Store відвідувачів, що їдять
			eatingVisitor.add(1);
			//затримка на споживання страви
			holdForTime(rnd.next());
			// поїв і віднімає себе зі Store відвідувачів, що їдять
			eatingVisitor.remove(1);
			getDispatcher().printToProtocol(getNameForProtocol() + "Відвідувач розрахувався і залишив кафе");
			
		// критичний час на очікування страви перебільшено
		} else {
			// додав себе у Store відвідувачів, що пішли
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
