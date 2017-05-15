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

public class TestCafeModel implements IStatisticsable, IExperimentable {

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

	public TestCafeModel(Dispatcher d, MainGUI mainGUI) {
		if (d == null || mainGUI == null) {
			System.out.println("Не визначено диспетчера або GUI для Model");
			System.out.println("Подальша робота неможлива");
			System.exit(0);
		}
		dispatcher = d;
		gui = mainGUI;
		// Передаємо акторів до стартового списку диспетчера
		componentsToStartList();
	}

	public void componentsToStartList() {
		// Передаємо акторів диспетчеру
		dispatcher.addStartingActor(getGenerator());
		dispatcher.addStartingActor(getWaiter());
		dispatcher.addStartingActor(getVisitor());
		dispatcher.addStartingActor(getMultiWaiter());
		dispatcher.addStartingActor(getChief());
		dispatcher.addStartingActor(getMultiChief());

	}

	private Actor getMultiWaiter() {
		if (multiWaiter == null) {
			multiWaiter = new MultiActor();
			multiWaiter.setNameForProtocol("MultiActor для офіціантів");
			multiWaiter.setOriginal(getWaiter());
			multiWaiter.setNumberOfClones(gui.getChooseDataWaiter().getInt());
		}
		return multiWaiter;
	}

	private Actor getWaiter() {
		if (waiter == null) {
			waiter = new Waiter("Waiter", gui, this);
		}
		return waiter;
	}

	private Actor getGenerator() {
		if (generator == null) {
			generator = new Generator("Generator", gui, this);
		}
		return generator;
	}

	private Actor getChief() {
		if (chief == null) {
			chief = new Chief("Chief", gui, this);
		}
		return chief;
	}

	// Мультикухар
	private Actor getMultiChief() {
		if (multiChief == null) {
			multiChief = new MultiActor();
			multiChief.setNameForProtocol("MultiActor для шеф-кухарів");
			multiChief.setOriginal(getWaiter());
			multiChief.setNumberOfClones(gui.getChooseDataChief().getInt());
		}
		return multiChief;
	}

	private Actor getVisitor() {
		if (visitor == null) {
			visitor = new Visitor("Visitor", gui, this);
		}
		return visitor;
	}

	public QueueForTransactions<Visitor> getReadyOrderAmount() {
		if (readyOrderAmount == null) {
			readyOrderAmount = new QueueForTransactions<Visitor>();
			readyOrderAmount.setNameForProtocol("Черга страв до видачі");
			readyOrderAmount.setDispatcher(dispatcher);
			readyOrderAmount.setDiscretHisto(getHistoForReadyOrderAmount());
		}
		return readyOrderAmount;
	}

	public QueueForTransactions<Visitor> getVisitorWaitingForWaiter() {
		if (visitorWaitingForWaiter == null) {
			visitorWaitingForWaiter = new QueueForTransactions<Visitor>();
			visitorWaitingForWaiter.setNameForProtocol("Черга відвідувачів які чекають на офіціанта");
			visitorWaitingForWaiter.setDispatcher(dispatcher);
			visitorWaitingForWaiter.setDiscretHisto(getHistoVisitorWaitingForWaiter());
		}
		return visitorWaitingForWaiter;
	}

	public QueueForTransactions<Visitor> getQueueNewVisitor() {
		if (queueNewVisitor == null) {
			queueNewVisitor = new QueueForTransactions<Visitor>();
			queueNewVisitor.setNameForProtocol("Черга нових відвідувачів ");
			queueNewVisitor.setDispatcher(dispatcher);
			queueNewVisitor.setDiscretHisto(getHistoForQueueNewVisitor());
		}
		return queueNewVisitor;
	}

	public QueueForTransactions<Waiter> getQueueFreeWaiter() {
		if (queueFreeWaiter == null) {
			queueFreeWaiter = new QueueForTransactions<Waiter>();
			queueFreeWaiter.setNameForProtocol("Черга вільних офіціантів ");
			queueFreeWaiter.setDispatcher(dispatcher);
			queueFreeWaiter.setDiscretHisto(getHistoForQueueFreeWaiter());
		}
		return queueFreeWaiter;
	}

	public QueueForTransactions<Visitor> getQueueToChief() {
		if (queueToChief == null) {
			queueToChief = new QueueForTransactions<Visitor>();
			queueToChief.setNameForProtocol("Черга замовлень на приготування");
			queueToChief.setDispatcher(dispatcher);
			queueToChief.setDiscretHisto(getHistoForQueueToChief());
		}
		return queueToChief;
	}

	public QueueForTransactions<Visitor> getVisitorInCafe() {
		if (visitorInCafe == null) {
			visitorInCafe = new QueueForTransactions<Visitor>();
			visitorInCafe.setNameForProtocol("Черга відвідувачів на обслуговування ");
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
			eatingVisitor.setNameForProtocol("Кількість відвідувачів, що їдять ");
			eatingVisitor.setDispatcher(dispatcher);
		}
		return eatingVisitor;
	}

	public QueueForTransactions<Visitor> getWaitingForOrder() {
		if (waitingForOrder == null) {
			waitingForOrder = new QueueForTransactions<Visitor>();
			waitingForOrder.setNameForProtocol("Кількість відвідувачів, що чекають на замовлення ");
			waitingForOrder.setDispatcher(dispatcher);
		}
		return waitingForOrder;
	}

	public Store getOrderToGoCount() {
		if (orderToGoCount == null) {
			orderToGoCount = new Store();
			orderToGoCount.setNameForProtocol("Кількість виданих замовлень ");
			orderToGoCount.setDispatcher(dispatcher);
		}
		return orderToGoCount;
	}

	public Store getGoneVisitorCount() {
		if (goneVisitorCount == null) {
			goneVisitorCount = new Store();
			goneVisitorCount.setNameForProtocol("Кількість відвідувачів, що не дочекались замовлення ");
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
		map.put("Гістограма для черги вільних офіціантів", getHistoForQueueFreeWaiter());
		map.put("Гістограма для черги відвідувачів, що чекають на вільного офіціанта", getHistoForQueueNewVisitor());
		map.put("Гістограма для черги замовлень на приготування", getHistoForQueueToChief());
		// map.put("Гістограма для затримки на очікування офіціанта
		// відвідувачем", getHistoWaitingForWaiter());
		map.put("Гістограма для затримки на очікування відвідувача офіціантом", getHistoForQueueNewVisitor());
		// map.put("Гістограма для затримки на приготування одного замовлення",
		// getHistoCookingTime());
		// map.put("Гістограма для затримки на тривалість трапези",
		// getHistoEatingTime());
		// map.put("Гістограма для затримки на очікування замовлення
		// відвідувачем", getHistoWaitingForOrder());
		return map;
	}

	@Override
	public void initForExperiment(double factor) {
		multiWaiter.setNumberOfClones((int) factor);
	}

	@Override
	public Map<String, Double> getResultOfExperiment() {
		Map<String, Double> map = new HashMap<>();
		map.put("Черга вільних офіціантів від їх к-сті", getHistoForQueueFreeWaiter().getAverage());
		map.put("Черга нових відвідувачів від к-сті офіціантів", getHistoForQueueNewVisitor().getAverage());
		// map.put("Черга замовлень на приготування",
		// getHistoForQueueToChief().getAverage());
		map.put("Час очікування відвідувачем обслуговування від к-сті офіціантів",
				getHistoWaitingForWaiter().getAverage());
		map.put("Час очікування офіціантом відвідувача від к-сті офіціантів", getHistoWaitingForVisitor().getAverage());
		// map.put("Затримка на приготування одного замовлення",
		// getHistoCookingTime().getAverage());
		// map.put("Затримка на тривалість трапези",
		// getHistoEatingTime().getAverage());
		// map.put("Затримка на очікування замовлення відвідувачем",
		// getHistoWaitingForOrder().getAverage());
		return map;
	}
}