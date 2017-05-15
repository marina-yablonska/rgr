package g;

import process.Actor;
import g.MainGUI;
import g.TestCafeModel;
import g.Visitor;
import rnd.Randomable;

public class Generator extends Actor {
	private double finishTime;
	private Randomable rnd;
	private TestCafeModel model;
	private MainGUI gui;

	public Generator(String name, MainGUI gui, TestCafeModel model) {
		super();
		this.model = model;
		this.gui = gui;
		setNameForProtocol(name);
		finishTime = gui.getChooseDataTime().getDouble();
		rnd = gui.getChooseRandom_Generator();
	}

	@Override
	protected void rule() {
		int n = 0;
		while (getDispatcher().getCurrentTime() <= finishTime) {
			String name = "Відвідувач " + ++n;
			Visitor visitor = new Visitor(name, gui, model);
			visitor.setStartTime(getDispatcher().getCurrentTime());
			getDispatcher().addStartingActor(visitor);
			holdForTime(rnd.next());
		}
	}

	public void setFinishTime(double finishTime) {
		this.finishTime = finishTime;
	}
}// вроде все готово!!!!!!
