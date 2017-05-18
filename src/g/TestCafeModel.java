package g;

import java.util.HashMap;
import java.util.Map;

import process.Actor;
import process.Dispatcher;
import process.MultiActor;
import process.QueueForTransactions;
import process.Store;
import stat.DiscretHisto;
import stat.Histo;
import stat.IHisto;
import widgets.experiments.IExperimentable;
import widgets.stat.IStatisticsable;
import widgets.trans.ITransProcesable;

public class TestCafeModel implements IStatisticsable, IExperimentable, ITransProcesable {

	private Dispatcher dispatcher;
	private MainGUI gui;

	private Generator generator;

	private Visitor visitor;

	private Chief chief;
	private MultiActor multiChief;

	private Waiter waiter;
	private MultiActor multiWaiter;

	private Store goneVisitorCount;
	private Store orderToGoCount;
	private Store eatingVisitor;

	private QueueForTransactions<Visitor> readyOrderAmount;
	private QueueForTransactions<Waiter> queueFreeWaiter;
	private QueueForTransactions<Visitor> queueNewVisitor;
	private QueueForTransactions<Visitor> queueToChief;
	private QueueForTransactions<Visitor> visitorInCafe;
	private QueueForTransactions<Visitor> visitorWaitingForWaiter;
	private QueueForTransactions<Visitor> waitingForOrder;

	private DiscretHisto histoForQueueFreeWaiter;
	private DiscretHisto histoForQueueNewVisitor;
	private DiscretHisto histoForQueueToChief;
	private DiscretHisto histoForReadyOrderAmount;
	private DiscretHisto histoForVisitorInCafe;
	private DiscretHisto histoVisitorWaitingForWaiter;

	// private Histo histoFullTime = new Histo();
	private Histo histoWaitingForVisitor;
	private Histo histoWaitingForWaiter;
	private Histo histoCookingTime;
	private Histo histoWaitingForOrder;
	private Histo histoEatingTime;
	private Histo histoWaitingForOrderForRegres;

	public Histo getHistoWaitingForOrderForRegres() {
		if (histoWaitingForOrderForRegres == null)
			histoWaitingForOrderForRegres = new Histo();
		return histoWaitingForOrderForRegres;
	}

	public TestCafeModel(Dispatcher d, MainGUI mainGUI) {
		if (d == null || mainGUI == null) {
			System.out.println("�� ��������� ���������� ��� GUI ��� Model");
			System.out.println("�������� ������ ���������");
			System.exit(0);
		}
		dispatcher = d;
		gui = mainGUI;
		// �������� ������ �� ���������� ������ ����������
		componentsToStartList();
	}

	public void componentsToStartList() {
		// �������� ������ ����������
		dispatcher.addStartingActor(getGenerator());
		dispatcher.addStartingActor(getWaiter());
		dispatcher.addStartingActor(getVisitor());
		dispatcher.addStartingActor(getMultiWaiter());
		dispatcher.addStartingActor(getChief());
		dispatcher.addStartingActor(getMultiChief());

	}

	private MultiActor getMultiWaiter() {
		if (multiWaiter == null) {
			multiWaiter = new MultiActor();
			multiWaiter.setNameForProtocol("MultiActor ��� ���������");
			multiWaiter.setOriginal(getWaiter());
			multiWaiter.setNumberOfClones(gui.getChooseDataWaiter().getInt());
		}
		return multiWaiter;
	}

	private Waiter getWaiter() {
		if (waiter == null) {
			waiter = new Waiter("Waiter", gui, this);
		}
		return waiter;
	}

	private Generator getGenerator() {
		if (generator == null) {
			generator = new Generator("Generator", gui, this);
		}
		return generator;
	}

	private Chief getChief() {
		if (chief == null) {
			chief = new Chief("Chief", gui, this);
		}
		return chief;
	}

	// �����������
	private MultiActor getMultiChief() {
		if (multiChief == null) {
			multiChief = new MultiActor();
			multiChief.setNameForProtocol("MultiActor ��� ���-������");
			multiChief.setOriginal(getWaiter());
			multiChief.setNumberOfClones(gui.getChooseDataChief().getInt());
		}
		return multiChief;
	}

	private Visitor getVisitor() {
		if (visitor == null) {
			visitor = new Visitor("Visitor", gui, this);
		}
		return visitor;
	}
	
	public void setVisitor(Visitor visitor) {
		this.visitor = visitor;
	}

	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}

	public QueueForTransactions<Visitor> getReadyOrderAmount() {
		if (readyOrderAmount == null) {
			readyOrderAmount = new QueueForTransactions<Visitor>();
			readyOrderAmount.setNameForProtocol("����� ����� �� ������");
			readyOrderAmount.setDispatcher(dispatcher);
			readyOrderAmount.setDiscretHisto(getHistoForReadyOrderAmount());
		}
		return readyOrderAmount;
	}

	public QueueForTransactions<Visitor> getVisitorWaitingForWaiter() {
		if (visitorWaitingForWaiter == null) {
			visitorWaitingForWaiter = new QueueForTransactions<Visitor>();
			visitorWaitingForWaiter.setNameForProtocol("����� ���������� �� ������� �� ���������");
			visitorWaitingForWaiter.setDispatcher(dispatcher);
			visitorWaitingForWaiter.setDiscretHisto(getHistoVisitorWaitingForWaiter());
		}
		return visitorWaitingForWaiter;
	}

	public QueueForTransactions<Visitor> getQueueNewVisitor() {
		if (queueNewVisitor == null) {
			queueNewVisitor = new QueueForTransactions<Visitor>();
			queueNewVisitor.setNameForProtocol("����� ����� ���������� ");
			queueNewVisitor.setDispatcher(dispatcher);
			queueNewVisitor.setDiscretHisto(getHistoForQueueNewVisitor());
		}
		return queueNewVisitor;
	}

	public QueueForTransactions<Waiter> getQueueFreeWaiter() {
		if (queueFreeWaiter == null) {
			queueFreeWaiter = new QueueForTransactions<Waiter>();
			queueFreeWaiter.setNameForProtocol("����� ������ ��������� ");
			queueFreeWaiter.setDispatcher(dispatcher);
			queueFreeWaiter.setDiscretHisto(getHistoForQueueFreeWaiter());
		}
		return queueFreeWaiter;
	}

	public QueueForTransactions<Visitor> getQueueToChief() {
		if (queueToChief == null) {
			queueToChief = new QueueForTransactions<Visitor>();
			queueToChief.setNameForProtocol("����� ��������� �� ������������");
			queueToChief.setDispatcher(dispatcher);
			queueToChief.setDiscretHisto(getHistoForQueueToChief());
		}
		return queueToChief;
	}

	public QueueForTransactions<Visitor> getVisitorInCafe() {
		if (visitorInCafe == null) {
			visitorInCafe = new QueueForTransactions<Visitor>();
			visitorInCafe.setNameForProtocol("����� ���������� �� �������������� ");
			visitorInCafe.setDispatcher(dispatcher);
			visitorInCafe.setDiscretHisto(getHistoForVisitorInCafe());
		}
		return visitorInCafe;
	}

	private DiscretHisto getHistoForVisitorInCafe() {
		if (histoForVisitorInCafe == null)
			histoForVisitorInCafe = new DiscretHisto();
		return histoForVisitorInCafe;
	}

	private DiscretHisto getHistoVisitorWaitingForWaiter() {
		if (histoVisitorWaitingForWaiter == null)
			histoVisitorWaitingForWaiter = new DiscretHisto();
		return histoVisitorWaitingForWaiter;
	}

	private DiscretHisto getHistoForQueueNewVisitor() {
		if (histoForQueueNewVisitor == null)
			histoForQueueNewVisitor = new DiscretHisto();
		return histoForQueueNewVisitor;
	}

	private DiscretHisto getHistoForReadyOrderAmount() {
		if (histoForReadyOrderAmount == null)
			histoForReadyOrderAmount = new DiscretHisto();
		return histoForReadyOrderAmount;
	}

	private DiscretHisto getHistoForQueueFreeWaiter() {
		if (histoForQueueFreeWaiter == null)
			histoForQueueFreeWaiter = new DiscretHisto();
		return histoForQueueFreeWaiter;
	}

	private DiscretHisto getHistoForQueueToChief() {
		if (histoForQueueToChief == null)
			histoForQueueToChief = new DiscretHisto();
		return histoForQueueToChief;
	}

	public Histo getHistoWaitingForVisitor() {
		if (histoWaitingForVisitor == null)
			histoWaitingForVisitor = new Histo();
		return histoWaitingForVisitor;
	}

	public Histo getHistoWaitingForWaiter() {
		if (histoWaitingForWaiter == null)
			histoWaitingForWaiter = new Histo();
		return histoWaitingForWaiter;
	}

	public Histo getHistoCookingTime() {
		if (histoCookingTime == null)
			histoCookingTime = new Histo();
		return histoCookingTime;
	}

	public Histo getHistoEatingTime() {
		if (histoEatingTime == null)
			histoEatingTime = new Histo();
		return histoEatingTime;
	}

	public Histo getHistoWaitingForOrder() {
		if (histoWaitingForOrder == null)
			histoWaitingForOrder = new Histo();
		return histoWaitingForOrder;
	}

	public Store getEatingVisitor() {
		if (eatingVisitor == null) {
			eatingVisitor = new Store();
			eatingVisitor.setNameForProtocol("ʳ������ ����������, �� ����� ");
			eatingVisitor.setDispatcher(dispatcher);
		}
		return eatingVisitor;
	}

	public QueueForTransactions<Visitor> getWaitingForOrder() {
		if (waitingForOrder == null) {
			waitingForOrder = new QueueForTransactions<Visitor>();
			waitingForOrder.setNameForProtocol("ʳ������ ����������, �� ������� �� ���������� ");
			waitingForOrder.setDispatcher(dispatcher);
		}
		return waitingForOrder;
	}

	public Store getOrderToGoCount() {
		if (orderToGoCount == null) {
			orderToGoCount = new Store();
			orderToGoCount.setNameForProtocol("ʳ������ ������� ��������� ");
			orderToGoCount.setDispatcher(dispatcher);
		}
		return orderToGoCount;
	}

	public Store getGoneVisitorCount() {
		if (goneVisitorCount == null) {
			goneVisitorCount = new Store();
			goneVisitorCount.setNameForProtocol("ʳ������ ����������, �� �� ���������� ���������� ");
			goneVisitorCount.setDispatcher(dispatcher);
		}
		return goneVisitorCount;
	}

	public void initForTest() {
		getQueueNewVisitor().setPainter(gui.getDiagramQueueVisitor().getPainter());
		getQueueFreeWaiter().setPainter(gui.getDiagramQueueWaiter().getPainter());
		getQueueToChief().setPainter(gui.getDiagramChief().getPainter());
		getGoneVisitorCount().setPainter(gui.getDiagramGoneVisitor().getPainter());
		getEatingVisitor().setPainter(gui.getDiagramEatingVisitor().getPainter());
		getWaitingForOrder().setPainter(gui.getDiagramWaitingForOrder().getPainter());
		if (gui.getCheckBox().isSelected())
			dispatcher.setProtocolFileName("Console");
		else
			dispatcher.setProtocolFileName("");
	}

	@Override
	public void initForStatistics() {
	}

	@Override
	public Map<String, IHisto> getStatistics() {
		Map<String, IHisto> map = new HashMap<>();
		map.put("ó�������� ��� ����� ������ ���������", getHistoForQueueFreeWaiter());
		map.put("ó�������� ��� ����� ����������, �� ������� �� ������� ���������", getHistoForQueueNewVisitor());
		map.put("ó�������� ��� ����� ��������� �� ������������", getHistoForQueueToChief());
		// map.put("ó�������� ��� �������� �� ���������� ���������
		// ����������", getHistoWaitingForWaiter());
		map.put("ó�������� ��� �������� �� ���������� ��������� ����������", getHistoForQueueNewVisitor());
		// map.put("ó�������� ��� �������� �� ������������ ������ ����������",
		// getHistoCookingTime());
		// map.put("ó�������� ��� �������� �� ��������� �������",
		// getHistoEatingTime());
		// map.put("ó�������� ��� �������� �� ���������� ����������
		// ����������", getHistoWaitingForOrder());
		return map;
	}

	@Override
	public void initForExperiment(double factor) {
		multiWaiter.setNumberOfClones((int) factor);
	}

	@Override
	public Map<String, Double> getResultOfExperiment() {
		Map<String, Double> map = new HashMap<>();
		map.put("��������� ���� ����������� � ���� �� �-�� ��������� � ������", getHistoWaitingForOrderForRegres().getAverage());
		//map.put("����� ����� ���������� �� �-�� ���������", getHistoForQueueNewVisitor().getAverage());
		// map.put("����� ��������� �� ������������",
		// getHistoForQueueToChief().getAverage());
		//map.put("��� ���������� ���������� �������������� �� �-�� ���������",
//				getHistoWaitingForWaiter().getAverage());
//		map.put("��� ���������� ���������� ��������� �� �-�� ���������", getHistoWaitingForVisitor().getAverage());
		// map.put("�������� �� ������������ ������ ����������",
		// getHistoCookingTime().getAverage());
		// map.put("�������� �� ��������� �������",
		// getHistoEatingTime().getAverage());
		// map.put("�������� �� ���������� ���������� ����������",
		// getHistoWaitingForOrder().getAverage());
		return map;
	}
	
	@Override
	public void initForTrans(double finishTime) {
		getVisitor().setFinishTime(finishTime);
		getWaiter().setFinishTime(finishTime);
		gui.getChooseDataTime().setDouble(finishTime);
	}

	@Override
	public void resetTransAccum() {
		getQueueFreeWaiter().resetAccum();
		getQueueNewVisitor().resetAccum();
	}

	@Override
	public Map<String, Double> getTransResult() {
		Map<String, Double> map = new HashMap<>();
		map.put("Queue for packing", getQueueFreeWaiter().getAccumAverage());
		map.put("Queue for setup", getQueueNewVisitor().getAccumAverage());
		return map;
	}
	
}