package PicSimulator;

public class Processor implements Runnable
{
	private static Processor instance;
	
	public static final int PWR_ON = 0;
	public static final int SLEEP = 1;
	public static final int WDT = 2;
	public static final int ALL_RESET = 3;

	static Watchdog wdt;
	static double frequency = 4.0; // in MHz
	static int zyklen = 0;

	int prescaledJumpedTicks = 0;
	int prevRa4 = 0;
	
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
		//System.out.printf(aktuellerBefehl + "		Arg1:	0x%x	Arg2:	0x%x\n", byte1, byte2);

		Befehle.fuehreBefehlAus(aktuellerBefehl, byte1, byte2);

		onTick();
		GUI.doRepaint();
	}
	
	private void onTick()
	{
		if (useWatchdog)
			wdt.tick();
		
		int options = Register.OPTION_REG;
		if ((options & 0b100000) == 0 || hasRa4Transitioned())
		{
			// tmr0 increments on tick or a RA4 pin transition occurred
			if ((options & 0b1000) == 0)
			{
				// prescaler assigned to tmr0
				int prescaler = (int) Math.pow(2, options & 0b111);
				if (prescaledJumpedTicks == prescaler + 1)
				{
					prescaledJumpedTicks = 0;
					incrementTmr0();
				}
				else
				{
					prescaledJumpedTicks++;
				}
			}
			else
			{
				incrementTmr0();
			}
		}
	}

	private boolean hasRa4Transitioned()
	{
		int ra4 = Register.PORTA & 0b10000;
		
		if (ra4 == prevRa4)
			// no change
			return false;
		
		prevRa4 = ra4;
		
		if ((Register.OPTION_REG & 0b10000) != 0)
		{
			// high-to-low
			return ra4 == 0;
		}
		else
		{
			// low-to-high
			return ra4 != 0;
		}
	}
	
	private void incrementTmr0()
	{
		if (Register.TMR0 == 0xFF)
		{
			//TODO: interrupt
			Register.TMR0 = 0;
		}
		else
		{
			Register.TMR0++;
		}
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
			Processor.setSleeping(false);
			break;

		case WDT:
			Register.allOtherReset();
			Processor.zyklen = 0;
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
