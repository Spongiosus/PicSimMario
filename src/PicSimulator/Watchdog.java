package PicSimulator;

public class Watchdog
{
    public static final int    timeoutMillis  = 18;   // without prescaling!

    private final Processor proc;
    private int                ticks;
    private double             millisLeft;
    private boolean            stopOnWatchdog = false;

    public Watchdog(Processor processor)
    {
        this.proc = processor;
    }

    public void tick()
    {
        ticks++;

        double millisPassed = 0.004d / Processor.frequency * ticks;
        int millisToPass = timeoutMillis;

        int options = Register.OPTION_REG;
        if ((options & 0b100) != 0)
        {
            // the Prescaler is assigned to the WDT
            int timeFactor = (int) Math.pow(2, (options & 0b111));
            millisToPass *= timeFactor;
        }

        if (millisPassed >= millisToPass)
        {
            millisLeft = 0.0d;
            proc.reset(Processor.WDT);
            ticks = 0;
            if (stopOnWatchdog)
            {
                proc.stop();
            }
            return;
        }

        millisLeft = millisToPass - millisPassed;
    }

    public double getMillisLeft()
    {
        return millisLeft;
    }

    public void reset()
    {
        ticks = 0;
    }

    public void enableStopOnWatchdog(boolean doStop)
    {
        stopOnWatchdog = doStop;
    }

    public void resetPrescaler()
    {
    	int options = Register.OPTION_REG;
        if ((options & 0b100) != 0)
        {
            // the Prescaler is assigned to the WDT, reset it
           Register.OPTION_REG = (short) (options & 0xFC);
        }
    }
}
