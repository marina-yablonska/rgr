package g;

import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import widgets.ChooseData;
import widgets.ChooseRandom;
import widgets.Diagram;

import java.awt.GridBagLayout;
import java.awt.Color;
import process.Dispatcher;
import process.IModelFactory;
import rnd.Negexp;
import rnd.Norm;
import g.TestCafeModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import widgets.stat.StatisticsManager;
import widgets.experiments.ExperimentManager;
import widgets.trans.TransProcessManager;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class MainGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1522250418960520793L;

	private JPanel contentPane;

	/**
	 * @wbp.nonvisual location=73,19
	 */
	// private final Histo histo = new Histo();
	private ChooseRandom chooseRandom_Generator;
	private JButton btnStartTest;
	private JCheckBox checkBox;
	private Diagram diagramQueueVisitor;
	private Diagram diagramQueueWaiter;
	private Diagram diagramChief;
	private ChooseData chooseDataWaiter;
	private ChooseData chooseDataChief;
	private ChooseData chooseDataSits;
	private ChooseRandom chooseRandomCookingTime;
	private ChooseData chooseDataMaxWaiting;
	private ChooseData chooseDataMaxCooking;
	private ChooseData chooseDataTime;
	private Diagram diagramGoneVisitor;
	private Diagram diagramWaitingForOrder;
	private Diagram diagramEatingVisitor;
	private StatisticsManager statisticsManager;
	private ExperimentManager experimentManager;
	private JPanel panelTrans;
	private TransProcessManager transProcessManager;
	private JPanel panelTest;
	private ChooseRandom chooseRandomWaiterSpeed;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI frame = new MainGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public MainGUI() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 953, 617);
		contentPane = new JPanel();
		String str = "/g/tz.htm";
		URL url = getClass().getResource(str);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(230, 11, 697, 556);
		contentPane.add(tabbedPane);

		JPanel panelTZ = new JPanel();
		tabbedPane.addTab("TZ", null, panelTZ, null);
		panelTZ.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 692, 528);
		panelTZ.add(scrollPane);

		JTextPane textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		((JEditorPane) textPane).setPage(url);

		panelTest = new JPanel();
		tabbedPane.addTab("Test", null, panelTest, null);
		panelTest.setLayout(null);

		diagramQueueVisitor = new Diagram();
		diagramQueueVisitor.setBounds(0, 0, 343, 164);
		diagramQueueVisitor.setHorizontalMaxText("50");
		diagramQueueVisitor.setVerticalMaxText("10");
		GridBagLayout gbl_diagramQueueVisitor = (GridBagLayout) diagramQueueVisitor.getLayout();
		gbl_diagramQueueVisitor.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		gbl_diagramQueueVisitor.rowHeights = new int[] { 0, 13, 34, -42 };
		gbl_diagramQueueVisitor.columnWeights = new double[] { 0.0, 0.0, 0.0 };
		gbl_diagramQueueVisitor.columnWidths = new int[] { 9, 0, 289 };
		diagramQueueVisitor.getDiagramPanel().setBackground(new Color(255, 255, 204));
		diagramQueueVisitor.setTitleText(
				"\u0427\u0435\u0440\u0433\u0430 \u0432\u0456\u0434\u0432\u0456\u0434\u0443\u0432\u0430\u0447\u0456\u0432");
		diagramQueueVisitor.setPainterColor(Color.BLUE);
		diagramQueueVisitor.setGridColor(new Color(204, 204, 204));
		panelTest.add(diagramQueueVisitor);

		diagramQueueWaiter = new Diagram();
		diagramQueueWaiter.setVerticalMaxText("2");
		diagramQueueWaiter.setBounds(343, 0, 349, 164);
		diagramQueueWaiter.setHorizontalMaxText("50");
		GridBagLayout gbl_diagramQueueWaiter = (GridBagLayout) diagramQueueWaiter.getLayout();
		gbl_diagramQueueWaiter.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		gbl_diagramQueueWaiter.rowHeights = new int[] { 0, 13, 34, -42 };
		gbl_diagramQueueWaiter.columnWeights = new double[] { 0.0, 0.0, 0.0 };
		gbl_diagramQueueWaiter.columnWidths = new int[] { 9, 0, 289 };
		diagramQueueWaiter.getDiagramPanel().setBackground(new Color(255, 255, 204));
		diagramQueueWaiter.setTitleText(
				"\u0427\u0435\u0440\u0433\u0430 \u0432\u0456\u043B\u044C\u043D\u0438\u0445 \u043E\u0444\u0456\u0446\u0456\u0430\u043D\u0442\u0456\u0432");
		diagramQueueWaiter.setPainterColor(Color.GREEN);
		diagramQueueWaiter.setGridColor(new Color(204, 204, 204));
		panelTest.add(diagramQueueWaiter);

		checkBox = new JCheckBox(
				"\u041F\u0440\u043E\u0442\u043E\u043A\u043E\u043B \u043D\u0430 \u043A\u043E\u043D\u0441\u043E\u043B\u044C");
		checkBox.setBounds(63, 498, 177, 23);
		checkBox.setSelected(true);
		panelTest.add(checkBox);

		btnStartTest = new JButton("\u0421\u0442\u0430\u0440\u0442");
		btnStartTest.setBounds(482, 498, 80, 23);
		btnStartTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startTest();
			}
		});
		panelTest.add(btnStartTest);

		diagramGoneVisitor = new Diagram();
		GridBagLayout gbl_diagramGoneVisitor = (GridBagLayout) diagramGoneVisitor.getLayout();
		gbl_diagramGoneVisitor.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		gbl_diagramGoneVisitor.rowHeights = new int[] { 0, 13, 34, -42 };
		gbl_diagramGoneVisitor.columnWeights = new double[] { 0.0, 0.0, 0.0 };
		gbl_diagramGoneVisitor.columnWidths = new int[] { 9, 0, 289 };
		diagramGoneVisitor.getDiagramPanel().setBackground(new Color(255, 255, 204));
		diagramGoneVisitor.setVerticalMaxText("20");
		diagramGoneVisitor.setTitleText(
				"\u0412\u0456\u0434\u0432\u0456\u0434\u0443\u0432\u0430\u0447\u0456, \u0449\u043E \u0437\u0430\u043B\u0438\u0448\u0438\u043B\u0438 \u043A\u0430\u0444\u0435");
		diagramGoneVisitor.setPainterColor(Color.RED);
		diagramGoneVisitor.setHorizontalMaxText("50");
		diagramGoneVisitor.setGridColor(new Color(204, 204, 204));
		diagramGoneVisitor.setBounds(343, 327, 349, 164);
		panelTest.add(diagramGoneVisitor);

		diagramWaitingForOrder = new Diagram();
		diagramWaitingForOrder.setVerticalMaxText("10");
		GridBagLayout gbl_diagramWaitingForOrder = (GridBagLayout) diagramWaitingForOrder.getLayout();
		gbl_diagramWaitingForOrder.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		gbl_diagramWaitingForOrder.rowHeights = new int[] { 0, 13, 34, -42 };
		gbl_diagramWaitingForOrder.columnWeights = new double[] { 0.0, 0.0, 0.0 };
		gbl_diagramWaitingForOrder.columnWidths = new int[] { 9, 0, 289 };
		diagramWaitingForOrder.getDiagramPanel().setBackground(new Color(255, 255, 204));
		diagramWaitingForOrder.setTitleText(
				"\u0412\u0456\u0434\u0432\u0456\u0434\u0443\u0432\u0430\u0447\u0456, \u044F\u043A\u0456 \u0447\u0435\u043A\u0430\u044E\u0442\u044C \u043D\u0430 \u0441\u0442\u0440\u0430\u0432\u0443");
		diagramWaitingForOrder.setPainterColor(Color.MAGENTA);
		diagramWaitingForOrder.setHorizontalMaxText("50");
		diagramWaitingForOrder.setGridColor(new Color(204, 204, 204));
		diagramWaitingForOrder.setBounds(0, 163, 343, 164);
		panelTest.add(diagramWaitingForOrder);

		diagramEatingVisitor = new Diagram();
		diagramEatingVisitor.setVerticalMaxText("10");
		GridBagLayout gbl_diagramEatingVisitor = (GridBagLayout) diagramEatingVisitor.getLayout();
		gbl_diagramEatingVisitor.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		gbl_diagramEatingVisitor.rowHeights = new int[] { 0, 13, 34, -42 };
		gbl_diagramEatingVisitor.columnWeights = new double[] { 0.0, 0.0, 0.0 };
		gbl_diagramEatingVisitor.columnWidths = new int[] { 9, 0, 289 };
		diagramEatingVisitor.getDiagramPanel().setBackground(new Color(255, 255, 204));
		diagramEatingVisitor.setTitleText(
				"\u0412\u0456\u0434\u0432\u0456\u0434\u0443\u0432\u0430\u0447\u0456, \u044F\u043A\u0456 \u0457\u0434\u044F\u0442\u044C");
		diagramEatingVisitor.setPainterColor(Color.BLACK);
		diagramEatingVisitor.setHorizontalMaxText("50");
		diagramEatingVisitor.setGridColor(new Color(204, 204, 204));
		diagramEatingVisitor.setBounds(0, 327, 343, 164);
		panelTest.add(diagramEatingVisitor);

		diagramChief = new Diagram();
		diagramChief.setVerticalMaxText("2");
		diagramChief.setBounds(343, 163, 343, 164);
		panelTest.add(diagramChief);
		diagramChief.setHorizontalMaxText("50");
		GridBagLayout gbl_diagramChief = (GridBagLayout) diagramChief.getLayout();
		gbl_diagramChief.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		gbl_diagramChief.rowHeights = new int[] { 0, 13, 34, -42 };
		gbl_diagramChief.columnWeights = new double[] { 0.0, 0.0, 0.0 };
		gbl_diagramChief.columnWidths = new int[] { 9, 0, 289 };
		diagramChief.getDiagramPanel().setBackground(new Color(255, 255, 204));
		diagramChief.setTitleText("\u0427\u0435\u0440\u0433\u0430 \u043A\u0443\u0445\u0430\u0440\u0456\u0432");
		diagramChief.setPainterColor(Color.ORANGE);
		diagramChief.setGridColor(new Color(204, 204, 204));

		JPanel panelStat = new JPanel();
		tabbedPane.addTab("Stat", null, panelStat, null);
		panelStat.setLayout(null);

		statisticsManager = new StatisticsManager();
		statisticsManager.setFactory((d) -> new TestCafeModel(d, this));
		statisticsManager.setBounds(0, 0, 692, 528);
		panelStat.add(statisticsManager);

		JPanel panelRegres = new JPanel();
		tabbedPane.addTab("Regres", null, panelRegres, null);
		panelRegres.setLayout(null);

		experimentManager = new ExperimentManager();
		experimentManager.getChooseDataFactors().setText("1  2  3  4  5");
		experimentManager.getJButtonStart().setBounds(595, 497, 91, 23);
		experimentManager.getChooseDataRepeat().setBounds(403, 497, 182, 26);
		experimentManager.getJButtonRedraw().setBounds(204, 497, 118, 23);
		experimentManager.getJCheckBox().setBounds(84, 497, 54, 23);
		experimentManager.getComboBox().setBounds(19, 461, 374, 20);
		experimentManager.getChooseDataFactors().setBounds(403, 440, 283, 46);
		experimentManager.getRegresAnaliser().setBounds(403, 6, 283, 424);
		experimentManager.getDiagram().setBounds(6, 6, 397, 424);
		experimentManager.setFactory((d) -> new TestCafeModel(d, this));
		experimentManager.setBounds(0, 0, 692, 528);
		panelRegres.add(experimentManager);
		experimentManager.setLayout(null);

		panelTrans = new JPanel();
		tabbedPane.addTab("Transient", null, panelTrans, null);
		panelTrans.setLayout(null);

		transProcessManager = new TransProcessManager();
		transProcessManager.setFactory((d) -> new TestCafeModel(d, this));
		transProcessManager.setBounds(0, 0, 692, 528);
		panelTrans.add(transProcessManager);

		chooseDataWaiter = new ChooseData();
		chooseDataWaiter.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				updateTime();
			}
		});
		chooseDataWaiter.setText("2");
		chooseDataWaiter.setTitle(
				"\u041A\u0456\u043B\u044C\u043A\u0456\u0441\u0442\u044C \u043E\u0444\u0456\u0446\u0456\u0430\u043D\u0442\u0456\u0432");
		chooseDataWaiter.setBounds(10, 11, 210, 57);
		contentPane.add(chooseDataWaiter);

		chooseRandomCookingTime = new ChooseRandom();
		chooseRandomCookingTime.setRandom(new Norm(10, 0.2));
		chooseRandomCookingTime.setTitle(
				"\u0427\u0430\u0441 \u043F\u0440\u0438\u0433\u043E\u0442\u0443\u0432\u0430\u043D\u043D\u044F");
		chooseRandomCookingTime.setBounds(10, 197, 210, 45);
		contentPane.add(chooseRandomCookingTime);

		chooseDataTime = new ChooseData();
		chooseDataTime.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				updateTime();
			}
		});
		chooseDataTime.setTitle("Час моделювання");
		chooseDataTime.setText("50");
		chooseDataTime.setBounds(10, 399, 210, 57);
		contentPane.add(chooseDataTime);

		chooseDataChief = new ChooseData();
		chooseDataChief.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				updateTime();
			}
		});
		chooseDataChief.setText("2");
		chooseDataChief.setTitle(
				"\u041A\u0456\u043B\u044C\u043A\u0456\u0441\u0442\u044C \u043A\u0443\u0445\u0430\u0440\u0456\u0432");
		chooseDataChief.setBounds(10, 71, 210, 57);
		contentPane.add(chooseDataChief);

		chooseDataSits = new ChooseData();
		chooseDataSits.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				updateTime();
			}
		});
		chooseDataSits.setTitle(
				"\u041A\u0456\u043B\u044C\u043A\u0456\u0441\u0442\u044C \u043C\u0456\u0441\u0446\u044C \u0443 \u043A\u0430\u0444\u0435");
		chooseDataSits.setText("10");
		chooseDataSits.setBounds(10, 129, 210, 57);
		contentPane.add(chooseDataSits);

		chooseDataMaxWaiting = new ChooseData();
		chooseDataMaxWaiting
				.setTitle("\u043E\u0431\u0441\u043B\u0443\u0433\u043E\u0432\u0443\u0432\u0430\u043D\u043D\u044F");
		chooseDataMaxWaiting.setText("15");
		chooseDataMaxWaiting.setBounds(10, 331, 109, 57);
		contentPane.add(chooseDataMaxWaiting);

		chooseDataMaxCooking = new ChooseData();
		chooseDataMaxCooking.setTitle("\u043F\u0440\u0438\u0433\u043E\u0442\u0443\u0432\u0430\u043D\u043D\u044F");
		chooseDataMaxCooking.setText("30");
		chooseDataMaxCooking.setBounds(112, 331, 102, 57);
		contentPane.add(chooseDataMaxCooking);

		JLabel label = new JLabel("\u041A\u0440\u0438\u0442\u0438\u0447\u043D\u0438\u0439 \u0447\u0430\u0441");
		label.setBounds(70, 309, 102, 14);
		contentPane.add(label);

		chooseRandom_Generator = new ChooseRandom();
		chooseRandom_Generator.setRandom(new Negexp(3));
		GridBagLayout gbl_chooseRandom_Generator = (GridBagLayout) chooseRandom_Generator.getLayout();
		gbl_chooseRandom_Generator.rowWeights = new double[] { 1.0 };
		gbl_chooseRandom_Generator.rowHeights = new int[] { 0 };
		gbl_chooseRandom_Generator.columnWeights = new double[] { 0.0, 1.0 };
		gbl_chooseRandom_Generator.columnWidths = new int[] { 32, 0 };
		chooseRandom_Generator.setTitle(
				"\u041F\u043E\u044F\u0432\u0430 \u0432\u0456\u0434\u0432\u0456\u0434\u0443\u0432\u0430\u0447\u0456\u0432");
		chooseRandom_Generator.setBounds(10, 253, 210, 45);
		contentPane.add(chooseRandom_Generator);

		chooseRandomWaiterSpeed = new ChooseRandom();
		chooseRandomWaiterSpeed.setRandom(new Norm(1, 0.2));
		GridBagLayout gbl_chooseRandomWaiterSpeed = (GridBagLayout) chooseRandomWaiterSpeed.getLayout();
		gbl_chooseRandomWaiterSpeed.rowWeights = new double[] { 1.0 };
		gbl_chooseRandomWaiterSpeed.rowHeights = new int[] { 0 };
		gbl_chooseRandomWaiterSpeed.columnWeights = new double[] { 0.0, 1.0 };
		gbl_chooseRandomWaiterSpeed.columnWidths = new int[] { 32, 0 };
		chooseRandomWaiterSpeed.setTitle(
				"\u041F\u0440\u043E\u0434\u0443\u043A\u0442\u0438\u0432\u043D\u0456\u0441\u0442\u044C \u043E\u0444\u0456\u0446\u0456\u0430\u043D\u0442\u0430");
		chooseRandomWaiterSpeed.setBounds(10, 473, 210, 45);
		contentPane.add(chooseRandomWaiterSpeed);
	}

	protected void updateTime() {
		try {
			if (panelTest.isShowing()) {
				String txt = chooseDataTime.getText();
				getDiagramChief().setHorizontalMaxText(txt);
				getDiagramEatingVisitor().setHorizontalMaxText(txt);
				getDiagramGoneVisitor().setHorizontalMaxText(txt);
				getDiagramQueueVisitor().setHorizontalMaxText(txt);
				getDiagramQueueWaiter().setHorizontalMaxText(txt);
				getDiagramWaitingForOrder().setHorizontalMaxText(txt);
				String w = chooseDataWaiter.getText();
				getDiagramQueueWaiter().setVerticalMaxText(w);
				String v = chooseDataSits.getText();
				getDiagramEatingVisitor().setVerticalMaxText(v);
				getDiagramQueueVisitor().setVerticalMaxText(v);
				getDiagramWaitingForOrder().setVerticalMaxText(v);
				String c = chooseDataChief.getText();
				getDiagramChief().setVerticalMaxText(c);
			}
		} catch (Exception e) {
		}
	}

	public MainGUI(JPanel contentPane) throws HeadlessException {
		super();
		this.contentPane = contentPane;
	}

	protected void startTest() {
		getBtnStartTest().setEnabled(false);
		getDiagramQueueVisitor().clear();// черга відвідувачів
		getDiagramQueueWaiter().clear();// черга офііціантів
		getDiagramGoneVisitor().clear();// відвідувачі, що пішли
		getDiagramChief().clear();// черга кухарів
		getDiagramWaitingForOrder(); // відвідувачі що чекають на замовлення
		getDiagramEatingVisitor(); // відвідувачі, що їдять
		Dispatcher dispatcher = new Dispatcher();
		dispatcher.addDispatcherFinishListener(() -> getBtnStartTest().setEnabled(true));
		IModelFactory factory = (d) -> new TestCafeModel(d, this);
		TestCafeModel model = (TestCafeModel) factory.createModel(dispatcher);
		model.initForTest();
		dispatcher.start();
	}

	public ChooseRandom getChooseRandom_Generator() {
		return chooseRandom_Generator;
	}

	public JButton getBtnStartTest() {
		return btnStartTest;
	}

	public JCheckBox getCheckBox() {
		return checkBox;
	}

	public Diagram getDiagramQueueVisitor() {
		return diagramQueueVisitor;
	}

	public Diagram getDiagramQueueWaiter() {
		return diagramQueueWaiter;
	}

	public Diagram getDiagramChief() {
		return diagramChief;
	}

	public ChooseData getChooseDataWaiter() {
		return chooseDataWaiter;
	}

	public ChooseData getChooseDataChief() {
		return chooseDataChief;
	}

	public ChooseData getChooseDataSits() {
		return chooseDataSits;
	}

	public ChooseRandom getChooseRandomCookingTime() {
		return chooseRandomCookingTime;
	}

	public ChooseData getChooseDataMaxWaiting() {
		return chooseDataMaxWaiting;
	}

	public ChooseData getChooseDataMaxCooking() {
		return chooseDataMaxCooking;
	}

	public ChooseData getChooseDataTime() {
		return chooseDataTime;
	}

	public Diagram getDiagramGoneVisitor() {
		return diagramGoneVisitor;
	}

	public Diagram getDiagramWaitingForOrder() {
		return diagramWaitingForOrder;
	}

	public Diagram getDiagramEatingVisitor() {
		return diagramEatingVisitor;
	}

	public StatisticsManager getStatisticsManager() {
		return statisticsManager;
	}

	public ExperimentManager getExperimentManager() {
		return experimentManager;
	}

	public TransProcessManager getTransProcessManager() {
		return transProcessManager;
	}

	public JPanel getPanelTest() {
		return panelTest;
	}

	public ChooseRandom getChooseRandomWaiterSpeed() {
		return chooseRandomWaiterSpeed;
	}
}
