package PicSimulator;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


@SuppressWarnings("serial")
public class GUI extends JFrame 
{
    private static final int    gpTableColCount = 4;
    private static GUI instance;
    
    
    Thread                      processorThread;
    private Processor           myProcessor;

    private JPanel              mainPanel;
    
    private JPanel				gpPanel;
    private JTable              gpTable;
    private JTable              sfrTable;

    private JButton             btnReset;
    private JButton             btnOpenProgram;
    private JPanel              buttonPanel;

    private JTable              programmTable;
    private JScrollPane         scrollPane;

    private JButton             btnStop;
    private JButton             btnStep;
    private JButton             btnStart;
    private JButton             btnHelp;

    private JPanel              contentPanel;

    private JLabel              stackLabel;
    private JPanel              stackPanel;
    private JTable              stackTable;

    private JPanel              runtimePanel;
    private JLabel              runtimeLabel;
    private JLabel              cycleLabel;
    private JFormattedTextField quarzTextField;
    private JButton             btnApply;

    private JPanel				watchdogPanel;
    private JLabel				watchdogText;
    private JCheckBox			cbEnableWatchdog;
    
    private JPanel				rightPanel;
    
    private IOPanel             ioPanel;

    public GUI(Processor proc)
    {
    	instance = this;
        myProcessor = proc;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 600);

        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(mainPanel);
        mainPanel.setLayout(new BorderLayout(0, 0));
        JFrame.setDefaultLookAndFeelDecorated(true);

        contentPanel = new JPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        configureContentPanel();

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 0, 10));

        btnReset = new JButton("Reset");
        btnHelp = new JButton("Hilfe");
        btnOpenProgram = new JButton("Öffnen");
        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");
        btnStep = new JButton("Schritt");
        
        buttonPanel.add(btnOpenProgram);
        buttonPanel.add(btnStart);
        buttonPanel.add(btnStop);
        buttonPanel.add(btnStep);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnHelp);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        initButtonEvents();
        initGPTable();
        initSFRTable();
        initProgram();
        initStack();
        initRuntimeCounter();

        resetProcessor();
    }

    private void configureContentPanel()
    {
        contentPanel.setLayout(new BorderLayout());

        JPanel upperPanel = new JPanel();

;
        
        gpPanel = new JPanel();

        gpTable = new JTable(new DefaultTableModel(0x43 / gpTableColCount + 1, gpTableColCount));
        gpPanel.add(gpTable);
        upperPanel.add(gpPanel);


        sfrTable = new JTable(new DefaultTableModel(8, 2));
        upperPanel.add(sfrTable);

        stackPanel = new JPanel();
        stackLabel = new JLabel("Stack");
        stackTable = new JTable(new DefaultTableModel(8, 1));
        stackPanel.add(stackLabel);
        stackPanel.add(stackTable);
        stackPanel.setPreferredSize(new Dimension(100, 200));
        stackPanel.setMaximumSize(new Dimension(100, 200));
        upperPanel.add(stackPanel);


        rightPanel= new JPanel(new GridLayout(2, 1, 0,5));
        
        runtimePanel = new JPanel();
        runtimePanel.setLayout(new GridLayout(2, 3, 5, 5));
        runtimePanel.setPreferredSize(new Dimension(200,100));

        quarzTextField = new JFormattedTextField(new DecimalFormat("####.########"));
        quarzTextField.setHorizontalAlignment(JTextField.RIGHT);
        btnApply = new JButton("OK");

        runtimeLabel = new JLabel();
        cycleLabel = new JLabel();

        runtimePanel.add(runtimeLabel);
        runtimePanel.add(cycleLabel);
        runtimePanel.add(new JLabel());
        runtimePanel.add(quarzTextField);
        runtimePanel.add(new JLabel("MHz"));
        runtimePanel.add(btnApply);
        rightPanel.add(runtimePanel);


        watchdogPanel = new JPanel(new GridLayout(4, 1));
        watchdogText = new JLabel("Watchdog: 0ms");
        
        cbEnableWatchdog = new JCheckBox("Watchdog");
        cbEnableWatchdog.setSelected(true);
        cbEnableWatchdog.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				watchdogText.setEnabled(cbEnableWatchdog.isSelected());
				myProcessor.useWatchdog(cbEnableWatchdog.isSelected());
			}
		});
        
        watchdogPanel.add(watchdogText);
        watchdogPanel.add(cbEnableWatchdog);
        rightPanel.add(watchdogPanel);
        
        
        upperPanel.add(rightPanel);
        
        contentPanel.add(upperPanel, BorderLayout.CENTER);


        ioPanel = new IOPanel();
        ioPanel.setPreferredSize(new Dimension(150, 175));
        ioPanel.setMaximumSize(new Dimension(150, 175));
        upperPanel.add(ioPanel);
        

        programmTable = new JTable(new DefaultTableModel(31, 2));
        scrollPane = new JScrollPane(programmTable);

        contentPanel.add(scrollPane, BorderLayout.EAST);
    }

    private void resetProcessor()
    {
    	myProcessor.reset(Processor.PWR_ON);
    	processorThread = null;
        repaintGUI();
    }

    private void initButtonEvents()
    {
        btnReset.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                resetProcessor();
            }
        });
        btnStop.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                myProcessor.stop();
                processorThread = null;
            }
        });
        btnStart.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                processorThread = new Thread(myProcessor);
                processorThread.start();
            }
        });
        btnStep.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                myProcessor.nextCommand();
            }
        });
        final GUI frame = this;
        btnOpenProgram.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                FileDialog filePicker = new FileDialog(frame);
                filePicker.setFile("*.lst");
                filePicker.setVisible(true);

                String filename = filePicker.getDirectory() + filePicker.getFile();

                if (filePicker.getFile() != null)
                {
                    resetProcessor();
					Parser.ladeDatei(filename);
					initProgram();
                }
            }
        });
        btnApply.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                repaintRuntimeCounter();
            }
        });
        btnHelp.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
            	if (Desktop.isDesktopSupported()) {
            	    try {
            	        File myFile = new File("Dokumentation.pdf");
            	        Desktop.getDesktop().open(myFile);
            	    } catch (Exception ex) {
            	        // no application registered for PDFs
            	    }
            	}
            }
        });
    }

    public static void doRepaint()
    {
    	instance.repaintGUI();
    }
    public void repaintGUI()
    {
        repaintGpTable();
        repaintSFRTable();
        repaintProgram();
        repaintIO();
        repaintStack();
        repaintRuntimeCounter();
        repaintWDPanel();
        super.repaint();
    }

    private void initRuntimeCounter()
    {
        quarzTextField.setValue(4);
        repaintRuntimeCounter();
    }

    private void repaintRuntimeCounter()
    {
        double procFreq = Processor.frequency;
        try
        {
            double newFreq = new Double(quarzTextField.getText());
            if (procFreq != newFreq)
            {
                Processor.frequency = newFreq;
                procFreq = newFreq;
            }
        }
        catch (Exception e)
        {
        }
        cycleLabel.setText(" " + Processor.zyklen + " Zyklen");
        runtimeLabel.setText(" " + (procFreq / 4.0d) * Processor.zyklen + " \u00B5s");
    }

    private void initGPTable()
    {
        gpTable.setPreferredSize(new Dimension(150, 300));
        gpTable.setMaximumSize(new Dimension(150, 300));
        gpPanel.setPreferredSize(new Dimension(150, 300));
        gpPanel.setMaximumSize(new Dimension(150, 300));
        gpTable.setEnabled(false);
    }

    private void initSFRTable()
    {
        DefaultTableModel model = (DefaultTableModel) (sfrTable.getModel());
        model.setRowCount(18);
        sfrTable.setEnabled(false);
    }

    private void initProgram()
    {
        DefaultTableModel model = (DefaultTableModel) (programmTable.getModel());
        model.setColumnCount(2);
        model.setColumnIdentifiers(new String[] { "Zeile", "Befehl" });
        programmTable.getColumnModel().getColumn(0).setWidth(50);

        programmTable.setEnabled(false);

        if (!Parser.isLoaded())
            return;

        model.setRowCount(Parser.realSize);

        for (int i = 0; i < Parser.realSize; i++)
        {
            programmTable.setValueAt(new Integer(i).toString(), i, 0);

    		int byte1 = Integer.parseInt(Parser.befehlszeilen[i].substring(5, 7), 16);
    		int byte2 = Integer.parseInt(Parser.befehlszeilen[i].substring(7, 9), 16);
    		
            programmTable.setValueAt(Decoder.erkenneBefehl(byte1, byte2) + " \t" + toHex(byte1) + " \t" + toHex(byte2), i, 1);
        }

        programmTable.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    JTable target = (JTable) e.getSource();
                    int row = target.rowAtPoint(e.getPoint());

                    String oldVal = (String) target.getValueAt(row, 0);

                    if (oldVal.endsWith("B"))
                    {
                        target.setValueAt(oldVal.substring(0, oldVal.length() - 1), row, 0);
                        Parser.breakpoint.remove(new Integer(row));
                        
                    }
                    else
                    {
                        target.setValueAt(oldVal + "B", row, 0);
                        Parser.breakpoint.add(row);
                    }
                }
            }
        });

    }

    private void initStack()
    {
        for (int i = 0; i < stackTable.getRowCount(); i++)
        {
            stackTable.setValueAt("", i, 0);
        }
    }

    private void repaintGpTable()
    {
        for (int i = 0; i < 0x43; i++)
        {
            int byteValue = Register.getValueAtAddress(0x0C + i);
            gpTable.setValueAt(toHex(byteValue), i / gpTableColCount, i % gpTableColCount);
        }
    }

    private void repaintSFRTable()
    {
    	sfrTable.setValueAt("W_REGISTER", 0, 0);
        sfrTable.setValueAt(toBinary(Register.W_REGISTER), 0, 1);
    	sfrTable.setValueAt("TMR0", 1, 0);
        sfrTable.setValueAt(toBinary(Register.TMR0), 1, 1);
    	sfrTable.setValueAt("PCL", 2, 0);
        sfrTable.setValueAt(toBinary(Register.PCL), 2, 1);
    	sfrTable.setValueAt("STATUS", 3, 0);
        sfrTable.setValueAt(toBinary(Register.STATUS), 3, 1);
    	sfrTable.setValueAt("FSR", 4, 0);
        sfrTable.setValueAt(toBinary(Register.FSR), 4, 1);
    	sfrTable.setValueAt("PORTA", 5, 0);
        sfrTable.setValueAt(toBinary(Register.PORTA), 5, 1);
    	sfrTable.setValueAt("PORTB", 6, 0);
        sfrTable.setValueAt(toBinary(Register.PORTB), 6, 1);
    	sfrTable.setValueAt("EEDATA", 7, 0);
        sfrTable.setValueAt(toBinary(Register.EEDATA), 7, 1);
    	sfrTable.setValueAt("EEADR", 8, 0);
        sfrTable.setValueAt(toBinary(Register.EEADR), 8, 1);
    	sfrTable.setValueAt("PCLATH", 9, 0);
        sfrTable.setValueAt(toBinary(Register.PCLATH), 9, 1);
    	sfrTable.setValueAt("INTCON", 10, 0);
        sfrTable.setValueAt(toBinary(Register.INTCON), 10, 1);
    	sfrTable.setValueAt("INDF", 11, 0);
        sfrTable.setValueAt(toBinary(Register.INDF), 11, 1);
    	sfrTable.setValueAt("OPTION_REG", 12, 0);
        sfrTable.setValueAt(toBinary(Register.OPTION_REG), 12, 1);
    	sfrTable.setValueAt("TRISA", 13, 0);
        sfrTable.setValueAt(toBinary(Register.TRISA), 13, 1);
    	sfrTable.setValueAt("TRISB", 14, 0);
        sfrTable.setValueAt(toBinary(Register.TRISB), 14, 1);
    	sfrTable.setValueAt("EECON1", 15, 0);
        sfrTable.setValueAt(toBinary(Register.EECON1), 15, 1);
    	sfrTable.setValueAt("EECON2", 16, 0);
        sfrTable.setValueAt(toBinary(Register.EECON2), 16, 1);
    }

    private void repaintProgram()
    {
        int pcl = Register.PCL;
        programmTable.clearSelection();
        programmTable.addRowSelectionInterval(pcl, pcl);

        Rectangle rect = programmTable.getCellRect(pcl, 0, true);
        programmTable.scrollRectToVisible(rect);
    }

    private void repaintIO()
    {
        ioPanel.repaint(myProcessor);
    }

    private void repaintStack()
    {
    	Stack<Integer> s = Register.stack;
        for (int i = 0;  i < 8; i++)
        {
        	if(i < s.size())
        		stackTable.setValueAt(new Integer( s.get(i)).toString(), i, 0);
        	else
        		stackTable.setValueAt("", i, 0);
        }

    }
    private void repaintWDPanel()
    {
    	if(myProcessor.useWatchdog)
    	{
    		watchdogText.setText("Watchdog: " + Processor.wdt.getMillisLeft() + "ms");
    	}
    }

    private String toHex(int byteValue)
    {
        String hexString = Integer.toHexString(byteValue & 0xFF) + "H";
        while (hexString.length() < 3)
        {
            hexString = "0" + hexString;
        }
        return hexString;
    }

    private String toBinary(int byteValue)
    {
        String byteString = Integer.toBinaryString(byteValue & 0xFF) + "b";
        while (byteString.length() < 9)
        {
            byteString = "0" + byteString;
        }
        return byteString;
    }

    
}
