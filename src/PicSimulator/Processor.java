package PicSimulator;

public class Processor implements Runnable
{
	private static Processor instance;
	
	public static final int PWR_ON = 0;
	public static final int SLEEP = 1;
	public static final int WDT = 2;

	static Watchdog wdt;
	static double frequency = 4.0; // in MHz
	static int zyklen = 0;

	boolean isSleeping = false;
	boolean isRunning = false;
	boolean useWatchdog = true;

	public Processor(String dateiName)
	{
		if (dateiName != null)
			Parser.ladeDatei(dateiName);
		wdt = new Watchdog(this);
		instance = this;
	}

	public Processor()
	{
		this(null);
	}

	public void run()
	{
		if (!Parser.isLoaded())
			return;

		isRunning = true;

		while (isRunning && Parser.befehlszeilen[Register.PCL] != null)
		{
			if (Parser.breakpoint.contains(Register.PCL))
			{
				isRunning = false;
				return;
			}
			nextCommand();
		}
		isRunning = false;
		System.out.println("FIN");
	}

	public void stop()
	{
		isRunning = false;
	}

	public void nextCommand()
	{
		int byte1 = Integer.parseInt(Parser.befehlszeilen[Register.PCL].substring(5, 7), 16);
		int byte2 = Integer.parseInt(Parser.befehlszeilen[Register.PCL].substring(7, 9), 16);

		String aktuellerBefehl = Decoder.erkenneBefehl(byte1, byte2);
		System.out.printf(aktuellerBefehl + "		Arg1:	0x%x	Arg2:	0x%x\n", byte1, byte2);

		Befehle.fuehreBefehlAus(aktuellerBefehl, byte1, byte2);

		if (useWatchdog)
			wdt.tick();
		GUI.doRepaint();
	}

	public boolean isSleeping()
	{
		return isSleeping;
	}
	public static void setSleeping(boolean state)
	{
		instance.isSleeping = state;
	}
	public boolean isRunning()
	{
		return isRunning;
	}

	public void useWatchdog(boolean use)
	{
		useWatchdog = use;
	}

	public void reset(int resetCause)
	{
		switch (resetCause)
		{
		case PWR_ON:
			Register.powerOnReset();
			Parser.reset();
			Processor.setSleeping(false);
			break;

		case WDT:
			Register.allOtherReset();
			Register.STATUS &= 0x7;
			Register.setBitAtAddress(3, 3);
			Register.clearBitAtAddress(3, 4);
			Register.PCLATH = 0;
			Register.PCL = 0;
			Processor.setSleeping(false);
			break;
		}
	}
	

	public static void main(String[] args)
	{
		GUI gui = new GUI(new Processor());
		gui.setVisible(true);
	}

}
