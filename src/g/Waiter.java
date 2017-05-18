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
	private BooleanSupplier isVisitor;
	private BooleanSupplier isOrder;
	private Randomable rnd;
	private boolean food;

	private QueueForTransactions<Visitor> readyOrderAmount;
	private QueueForTransactions<Waiter> queueFreeWaiter;
	private QueueForTransactions<Visitor> queueToChief;
	private QueueForTransactions<Visitor> visitorWaitingForWaiter;
	private QueueForTransactions<Visitor> waitingForOrder;
	private BooleanSupplier isWork;

	public Waiter(String name, MainGUI gui, TestCafeModel model) {
		super();
		setNameForProtocol(name);
		queueFreeWaiter = model.getQueueFreeWaiter();
		queueToChief = model.getQueueToChief();
		finishTime = gui.getChooseDataTime().getDouble();
		rnd = gui.getChooseRandomWaiterSpeed().getRandom();
		visitorWaitingForWaiter = model.getVisitorWaitingForWaiter();
		waitingForOrder = model.getWaitingForOrder();
		readyOrderAmount = model.getReadyOrderAmount();
	}

	private void initConditions() {
		isWork = () -> (visitorWaitingForWaiter.size() > 0) || (readyOrderAmount.size() > 0);
		isVisitor = () -> visitorWaitingForWaiter.size() > 0;
		isOrder = () -> readyOrderAmount.size() > 0;
	}

	@Override
	protected void rule() {
		//�������� ���� ����:����� ����������,�� ������� �� ��������� � ����� ������� ���������
		initConditions();
		while (getDispatcher().getCurrentTime() <= finishTime) {
			//�������� ���� ���� �� ����� ������ ���������
			queueFreeWaiter.addLast(this);
			try {
				//���� �� ����� ���������
				waitForCondition(isWork, "�� ���� ������");
			} catch (DispatcherFinishException e) {
				return;
			} 	
			//�������� ������� ���� � ����� ������ ���������
			queueFreeWaiter.remove(this);
			
			//�������� ��������� ����� �� ����� ���������
			if (isVisitor.getAsBoolean()) {
				// ��������, ���� �������� ����� �� ������
				//holdForTime(rnd.next());	
				getDispatcher().printToProtocol(getNameForProtocol() + "������ ����������");

				// ��������� ������ � ����� �� ���������� ��������������
				Visitor visitor = visitorWaitingForWaiter.removeFirst();
				// ������ ������ � ����� �� ���������� ����������
				waitingForOrder.add(visitor);
				// ������ ������ � ����� �� ������
				queueToChief.add(visitor);  
			}
			
			//�������� ��������� ����� �� ����� ������� ��������� � ����
			else {				
				//��������� �������� ���������� � �����
				Visitor visitor = readyOrderAmount.removeFirst();
				//�������� ����� �� �������� ���������,�� ���� (�� �� � � ����)
				if (waitingForOrder.contains(visitor)) {
					// �����, ������� ��� ����� ����쳺, �� ���� �������� ������
					visitor.setFood(true);
					// �������� �� ����� ������
					holdForTime(rnd.next());
					getDispatcher().printToProtocol(getNameForProtocol() + "�������� ���������� � �������������");
				// ���� ����� ��������� ���� � ����
				} else
					getDispatcher().printToProtocol(getNameForProtocol() + "����� ��������� ���� � ����");
			}
		}
	}
	public void setFinishTime(double finishTime) {
		this.finishTime = finishTime;
	}
	
	public double getFinishTime() {
		return finishTime;
	}
}
