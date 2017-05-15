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
		name = "³������� " + birthTime;
	}

	public Visitor(String name, MainGUI gui, TestCafeModel model) {
		super();
		setNameForProtocol(name);
		queueNewVisitor = model.getQueueNewVisitor(); // ����� ����� ������
		goneVisitorCount = model.getGoneVisitorCount(); // ����� ������, �� ���� �� �����������
		rnd = gui.getChooseRandomCookingTime().getRandom(); // �������� �� ���������� ������
		waiterMaxTime = gui.getChooseDataMaxWaiting().getDouble(); // ��������� ��� ������ � ���� 
		orderMaxTime = gui.getChooseDataMaxCooking().getDouble(); // ��������� ��� ���������� ������
		eatingVisitor = model.getEatingVisitor(); // Store ������, �� �����
		visitorInCafe = model.getVisitorInCafe(); // ������ � ���� (������ ���)
		visitorWaitingForWaiter = model.getVisitorWaitingForWaiter(); //������ ������� �� ���������
		waitingForOrder = model.getWaitingForOrder(); //������ ������� �� ����������
		maxSits = gui.getChooseDataSits().getInt(); // ������� ���� � ����
	}

	private void initConditions() {
		// ��������, �� � ���� ���� � ����
		c = () -> visitorInCafe.size() < maxSits;
		// ��������, �� ������� ��������� ������
		c2 = () -> food;
	}

	@Override
	protected void rule() {
		// �� ����ֲ

		initConditions();
		// ����� ������ �� ����, ���� ���� � ����� �� ���� � ����
		queueNewVisitor.addLast(this);
		// ��������, �� � ���� � ����:
		// waiterMaxTime - ��������� ��� �� ���������� ������� ���� � ����
		waitForConditionOrHoldForTime(c, "����� ���� ���� � ����", waiterMaxTime);

		// � ����
		
		// ����� ������ � ����, ������� ���� � ����� �� ���� � ����
		queueNewVisitor.remove(this);
		
		// ���� ���� ���� � �������� �������� � ���� �� ������������ ���������� ����
		if (c.getAsBoolean()) {
			// ���� ���� � ����� ������ �������� ����
			visitorInCafe.add(this);
			// ���� ���� � ����� ������ �� ��������������
			visitorWaitingForWaiter.add(this);
			
		// ���� ���� �� ���� � ��������� ��� ����������� 	
		} else {
			// +1 � Store ����������, �� ����
			goneVisitorCount.add(1);
			getDispatcher()
					.printToProtocol(getNameForProtocol() + "³������� �� ��������� �������������� � ������� ����");
			return;
		}
		// �������� �����, �� �������� �� �������� ������
		// orderMaxTime - ��������� ��� �� ���������� ������
		waitForConditionOrHoldForTime(c2, "�� ���� ���", orderMaxTime);
		// ���� �� ���
		waitingForOrder.remove(this);

		// ���� ������ �������� �� ������������ ���������� ����
		if (c2.getAsBoolean()) {
			// ���� ���� � Store ����������, �� �����
			eatingVisitor.add(1);
			//�������� �� ���������� ������
			holdForTime(rnd.next());
			// ��� � ����� ���� � Store ����������, �� �����
			eatingVisitor.remove(1);
			getDispatcher().printToProtocol(getNameForProtocol() + "³������� ������������ � ������� ����");
			
		// ��������� ��� �� ���������� ������ �����������
		} else {
			// ����� ���� � Store ����������, �� ����
			goneVisitorCount.add(1);
			getDispatcher().printToProtocol(getNameForProtocol() + "³������� �� ��������� ���������� � ������� ����");
		}
		// �������� ������� ����
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
