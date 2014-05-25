package PicSimulator;

public class Register {
	
	static void powerOnReset ()
	{
		W_REGISTER = 0;
		INDF = 0;
		PCL = 0;
		STATUS = 0b00011111;
		PCLATH = 0;
		INTCON = 0;
		OPTION_REG = 0xFF;
		TRISA = 0b00011111;
		TRISB = 0xFF;
		EECON1 = 0;
		EECON2 = 0;		
	}
	// Other Resets TODO
		
	static void wakeUpReset()
	{
		
	}
	
	static int getValueAtAddress(int address)
	{
		if((address >= 0x0C) && (address <= 0x2F))
		{
		// GPR
		return GPR[address - 0x0C];
		}
		if ((address >= 0x8C) && (address <= 0xAF))
		{
		//GPR
		return GPR[address - 0x8C];
		}
		if(address == 0x00)
			return INDF;
		if(address == 0x01)
			return TMR0;
		if(address == 0x81)
			return OPTION_REG;
		if((address == 0x02) || (address == 0x82))
			return PCL;
		if((address == 0x03) || (address == 0x83))
			return STATUS;
		if((address == 0x04) || (address == 0x84))
			return FSR;
		if(address == 0x05)
			return PORTA;
		if(address == 0x85)
			return TRISA;
		if(address == 0x06)
			return PORTB;
		if(address == 0x86)
			return TRISB;
		// Unbelegte Register
		if((address == 0x07) || (address == 0x87))
			return 0;
		if(address == 0x86)
			return TRISB;
		if(address == 0x08)
			return EEDATA;
		if(address == 0x88)
			return EECON1;
		if(address == 0x09)
			return EEADR;
		if(address == 0x89)
			return EECON2;
		if((address == 0x0A) || (address == 0x8A))
			return PCLATH;
		if((address == 0x0B) || (address == 0x8B))
			return INTCON;	
		
		// Unimplemented
		if((address <= 0x30) || (address >= 0x7F))
			return 0;
		if((address <= 0xB0) || (address >= 0xFF))
			return 0;
	
				
		return 0;
	}
	
	static void setBitAtAddress(int address, int bit)
	{
		if((address == 0x03) || (address == 0x83))
			STATUS = (STATUS | (1 << bit));
		if(address == 0x05)
			PORTA = (PORTA | (1 << bit));
		if(address == 0x85)
			TRISA = (TRISA | (1 << bit));
		if(address == 0x06)
			PORTB = (PORTB | (1 << bit));
		if(address == 0x86)
			TRISB = (TRISB | (1 << bit));
		if(address == 0x0A)
			PCLATH = (PCLATH | (1 << bit));
		if(address == 0x0B)
			INTCON = (INTCON | (1 << bit));
		//TODO Wo muss man alles bits setzen können ?
	}
	
	static void clearBitAtAddress(int address, int bit)
	{
		int maske = 0;
		switch(bit)
		{
		case 0:
			maske = 0b11111110;
			break;
		case 1:
			maske = 0b11111101;
			break;
		case 2:
			maske = 0b11111011;
			break;
		case 3:
			maske = 0b11110111;
			break;
		case 4:
			maske = 0b11101111;
			break;
		case 5:
			maske = 0b11011111;
			break;
		case 6:
			maske = 0b10111111;
			break;
		case 7:
			maske = 0b01111111;
			break;
		}
		if((address == 0x03) || (address == 0x83))
			STATUS = (STATUS & maske);
		if(address == 0x05)
			PORTA = (PORTA & maske);
		if(address == 0x85)
			TRISA = (TRISA & maske);
		if(address == 0x06)
			PORTB = (PORTB & maske);
		if(address == 0x86)
			TRISB = (TRISB & maske);
		if(address == 0x0A)
			PCLATH = (PCLATH & maske);
		if(address == 0x0B)
			INTCON = (INTCON & maske);
		//TODO Wo muss man alles bits clearen können ?
	}
	
	static void setValueAtAddress(int address, int value)
	{
		if((address >= 0x0C) && (address <= 0x2F))
		{
		// GPR
		GPR[address - 0x0C] = value;
		}
		if ((address >= 0x8C) && (address <= 0xAF))
		{
		//GPR
		GPR[address - 0x8C] = value;
		}
		if((address == 0x00) || (address == 0x80))
			INDF = value;
		if(address == 0x01)
			TMR0 = value;
		if((address == 0x02) || (address == 0x82))
			PCL = value;
		if((address == 0x03) || (address == 0x83))
			STATUS = value;
		if((address == 0x04) || (address == 0x84))
			FSR = value;
		if(address == 0x85)
			TRISA = value;
		if(address == 0x86)
			TRISB = value;
				
		// Unimplemented
		if((address == 0x07) || (address == 0x87))
			return;
		if(address == 0x08)
			EEDATA = value;
		if(address == 0x09)
			EEADR = value;
		if(address == 0x89)
			EECON2 = value;	
		
		// Unimplemented
		if((address <= 0x30) || (address >= 0x7F))
			return;
		if((address <= 0xB0) || (address >= 0xFF))
			return;
	}
	
			//-----------------------------------------BANK0--------------------------------//
	public static int TMR0;
		// Real Time Clock
	
	public static int PCL;
		// Low order 8 bits of program counter
	
	public static int STATUS;	
		// 7.Bit: IRP (Register Bank Select Bit)
		// 6.Bit: RP1 (Register Bank Select Bit)
		// 5.Bit: RP0 (Register Bank Select Bit)
		// 4.Bit: TO  (Timeout)
		// 3.Bit: PD  (Power Down Bit)
		// 2.Bit: Z   (Zero Flag)
		// 1.Bit: DC  (Digit Carry)
		// 0.Bit: C   (Carry Bit)
	
	public static int FSR;
		// Indirect Data Memory Address Pointer
	public static int PORTA;
		// 3 msb nicht belegt
	public static int PORTB;
	public static int EEDATA;
		// EEPROM Data Register
	public static int EEADR;
		// EEPROM Address Register
	public static int PCLATH;
	public static int INTCON;
	
			//-----------------------------------------BANK1--------------------------------//
	public static int INDF;
	public static int OPTION_REG;
		// 7.Bit: RBPU   	 (Pull-up enable Bit)
		// 6.Bit: INTEDG 	 (Interrupt Edge Select)
		// 5.Bit: T0CS   	 (TMR0 Clock Source Select Bit)
		// 4.Bit: T0SE   	 (TMR0 Source Edge Select Bit)
		// 3.Bit: PSA    	 (Prescaler Assignement Bit)
		// 2.-0.Bit: PS2:PS0 (Prescaler Rate Select Bits)
		
	public static int TRISA;
		// 3 msb nicht belegt
		// Port Data Direction Register
	
	public static int TRISB;
		// Port Data Direction Register
	
	public static int EECON1;
	public static int EECON2;
	
	
	public static int[] GPR = new int[36];
	
	public static int W_REGISTER;
	
	//WATCHDOG
	//WATCHDOG COUNTER
	//ZYKLEN
	//
	//STACK
	
	
	
}
