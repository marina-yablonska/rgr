package g;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import process.Store;

import g.Order;

import java.util.function.BooleanSupplier;

import g.MainGUI;
import g.TestCafeModel;
import g.Visitor;

import rnd.Randomable;

public class Waiter extends Actor {

	private double finishTime;
	private Store orderToGoCount;
	private BooleanSupplier isVisitor;
	private BooleanSupplier isOrder;
	private double maxSits;
	private Randomable rnd;
	private boolean food;

	private QueueForTransactions<Visitor> readyOrderAmount;
	private QueueForTransactions<Waiter> queueFreeWaiter;
	private QueueForTransactions<Visitor> queueNewVisitor;
	private QueueForTransactions<Visitor> queueToChief;
	private QueueForTransactions<Visitor> visitorInCafe;
	private QueueForTransactions<Visitor> visitorWaitingForWaiter;
	private QueueForTransactions<Visitor> waitingForOrder;
	private BooleanSupplier isWork;

	public Waiter(String name, MainGUI gui, TestCafeModel model) {
		super();
		setNameForProtocol(name);
		// visitorInCafe = model.getVisitorInCafe();
		orderToGoCount = model.getOrderToGoCount();
		queueFreeWaiter = model.getQueueFreeWaiter();
		queueToChief = model.getQueueToChief();
		queueNewVisitor = model.getQueueNewVisitor();
		finishTime = gui.getChooseDataTime().getDouble();
		maxSits = gui.getChooseDataSits().getDouble();
		rnd = gui.getChooseRandomWaiterSpeed().getRandom();
		visitorInCafe = model.getVisitorInCafe();
		visitorWaitingForWaiter = model.getVisitorWaitingForWaiter();
		waitingForOrder = model.getWaitingForOrder();
		readyOrderAmount = model.getReadyOrderAmount();
		// setWaitingForVisitorHisto(model.getWaitingForVisitorHisto());
	}

	private void initConditions() {
		isWork = () -> (visitorWaitingForWaiter.size() > 0) || (readyOrderAmount.size() > 0);
		isVisitor = () -> visitorWaitingForWaiter.size() > 0;
		isOrder = () -> readyOrderAmount.size() > 0;
	}

	@Override
	protected void rule() {
		//перевірка двох умов:чергу відвідувачів,що чекають на офіціанта і чергу готових замовлень
		initConditions();
		while (getDispatcher().getCurrentTime() <= finishTime) {
			//Офіціант додає себе до черги вільних офіціантів
			queueFreeWaiter.addLast(this);
			try {
				//чекає до появи відвідувача
				waitForCondition(isWork, "Має бути робота");
			} catch (DispatcherFinishException e) {
				return;
			} 	
			//офіціант видаляє себе з черги вільних офіціантів
			//queueFreeWaiter.remove(this);
			
			//перевірка виконання умови на появу відвідувача
			if (isVisitor.getAsBoolean()) {
				// затримка, поки офіціант підійде до візітора
				//holdForTime(rnd.next());	
				getDispatcher().printToProtocol(getNameForProtocol() + "Приймає замовлення");

				// видалення візітора з черги на очікування обслуговування
				Visitor visitor = visitorWaitingForWaiter.removeFirst();
				// додати візітора в чергу на очікування замовлення
				waitingForOrder.add(visitor);
				// додати візітора в чергу до кухаря
				queueToChief.add(visitor);  
			}
			
			//перевірка виконання умови на появу готових замовлень у черзі
			else {				
				//видалення готового замовлення з черги
				Visitor visitor = readyOrderAmount.removeFirst();
				//перевірка черги на наявність відвідувача,що чекає (що він є у кафе)
				if (waitingForOrder.contains(visitor)) {
					// умова, завдяки якій візітор розуміє, що йому принесли страву
					visitor.setFood(true);
					// затримка на винос страви
					holdForTime(rnd.next());
					getDispatcher().printToProtocol(getNameForProtocol() + "Виносить замовлення і розраховується");
				// якщо цього відвідувача немає у кафе
				} else
					getDispatcher().printToProtocol(getNameForProtocol() + "Цього відвідувача немає в кафе");
			}
		}
	}

	public void setFinishTime(double finishTime2) {
		this.finishTime = finishTime2;
	}
}
