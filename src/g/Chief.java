package g;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import rnd.Randomable;

import java.util.function.BooleanSupplier;

import g.Order;

public class Chief extends Actor {

	private double finishTime;
	private Randomable rnd;
	private QueueForTransactions<Visitor> readyOrderAmount;
	private BooleanSupplier isOrder;

	private QueueForTransactions<Visitor> queueToChief;

	public Chief(String name, MainGUI gui, TestCafeModel model) {
		super();

		setNameForProtocol(name);
		rnd = gui.getChooseRandomCookingTime();
		queueToChief = model.getQueueToChief();
		readyOrderAmount = model.getReadyOrderAmount();
		finishTime = gui.getChooseDataTime().getDouble();

	}

	private void initConditions() {
		isOrder = () -> queueToChief.size() > 0;
	}

	@Override
	protected void rule() {
		// TODO Auto-generated method stub
		initConditions();

		while (getDispatcher().getCurrentTime() <= finishTime) {
			try {
				waitForCondition(isOrder, "Має бути замовлення");
			} catch (DispatcherFinishException e) {
				return;
			}
			Visitor order = queueToChief.removeFirst();
			getDispatcher().printToProtocol(getNameForProtocol() + "Приготування замовлення");
			holdForTime(rnd.next());

			readyOrderAmount.add(order);
			getDispatcher().printToProtocol(getNameForProtocol() + "Замовлення готове");
		}
	}

	public void setFinishTime(double finishTime) {
		this.finishTime = finishTime;
	}

}
