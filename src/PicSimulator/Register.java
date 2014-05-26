package PicSimulator;

import java.util.Stack;

public class Register {

	static Stack<Integer> stack = new Stack<Integer>();

	public static void pushStack() {
		if (Register.stack.size() == 8)
			return; // TODO
		Register.stack.push(Register.PCL);
	}

	public static void popStack() {
		if (Register.stack.size() == 0)
			return; // TODO
		Register.PCL = Register.stack.pop();
	}

	static void powerOnReset() {
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
		stack.clear();
		Processor.zyklen = 0;
		
		for(int i = 0; i<= 35; i++)
		{
			GPR[i] = 0;
		}
		
	}

	static void allOtherReset() {
		PCL = 0;
		STATUS = STATUS & 0b00011111;
		PCLATH = 0;
		INTCON = INTCON & 0b00000001;
		OPTION_REG = 0xFF;
		TRISA = 0xFF;
		TRISB = 0xFF;
		EECON1 = 0;
		PCLATH = 0;
	}

	static int getValueAtAddress(int address) {
		// Bankabfrage
		if (Befehle.isBitSetAt(Register.STATUS, 5))
			address = address + 0x80;

		if ((address >= 0x0C) && (address <= 0x2F)) {
			// GPR
			return GPR[address - 0x0C];
		}
		if ((address >= 0x8C) && (address <= 0xAF)) {
			// GPR
			return GPR[address - 0x8C];
		}
		if (address == 0x00 || address == 0x80)
		{
			if(FSR == 0x00 || FSR == 0x80)
				return 0;
			return Register.getValueAtAddress(FSR);
		}
		if (address == 0x01)
			return TMR0;
		if (address == 0x81)
			return OPTION_REG;
		if ((address == 0x02) || (address == 0x82))
			return PCL;
		if ((address == 0x03) || (address == 0x83))
			return STATUS;
		if ((address == 0x04) || (address == 0x84))
			return FSR;
		if (address == 0x05)
			return PORTA;
		if (address == 0x85)
			return TRISA;
		if (address == 0x06)
			return PORTB;
		if (address == 0x86)
			return TRISB;
		// Unbelegte Register
		if ((address == 0x07) || (address == 0x87))
			return 0;
		if (address == 0x08)
			return EEDATA;
		if (address == 0x88)
			return EECON1;
		if (address == 0x09)
			return EEADR;
		if (address == 0x89)
			return EECON2;
		if ((address == 0x0A) || (address == 0x8A))
			return PCLATH;
		if ((address == 0x0B) || (address == 0x8B))
			return INTCON;

		// Unimplemented
		if ((address <= 0x30) || (address >= 0x7F))
			return 0;
		if ((address <= 0xB0) || (address >= 0xFF))
			return 0;

		return 0;
	}

	static void setBitAtAddress(int address, int bit) {
		// Bankabfrage
		if (Befehle.isBitSetAt(Register.STATUS, 5))
			address = address + 0x80;

		if ((address >= 0x0C) && (address <= 0x2F)) {
			// GPR
			GPR[address - 0x0C] = GPR[address - 0x0C] | (1 << bit);
		}
		if ((address >= 0x8C) && (address <= 0xAF)) {
			// GPR
			GPR[address - 0x8C] = GPR[address - 0x8C] | (1 << bit);
		}
		if ((address == 0x00) || (address == 0x80))
			if(FSR == 0x00 || FSR == 0x80)
				return;
			else
				setBitAtAddress(FSR, bit);
		if (address == 0x01)
			TMR0 = TMR0 | (1 << bit);
		if (address == 0x81)
			OPTION_REG = OPTION_REG | (1 << bit);
		if ((address == 0x02) || (address == 0x82))
			PCL = PCL | (1 << bit);
		if ((address == 0x03) || (address == 0x83))
			STATUS = STATUS | (1 << bit);
		if ((address == 0x04) || (address == 0x84))
			FSR = FSR | (1 << bit);
		if (address == 0x85)
			TRISA = TRISA | (1 << bit);
		if (address == 0x86)
			TRISB = TRISB | (1 << bit);
		if (address == 0x05)
			PORTA = PORTA | (1 << bit);
		if (address == 0x06)
			PORTB = PORTB | (1 << bit);

		// Unimplemented
		if ((address == 0x07) || (address == 0x87))
			return;
		if (address == 0x08)
			EEDATA = EEDATA | (1 << bit);
		if (address == 0x09)
			EEADR = EEADR | (1 << bit);
		if (address == 0x88)
			EECON1 = EECON1 | (1 << bit);
		if (address == 0x89)
			EECON2 = EECON2 | (1 << bit);
		if ((address == 0x0A) || (address == 0x8A))
			PCLATH = PCLATH | (1 << bit);
		if ((address == 0x0B) || (address == 0x8B))
			INTCON = INTCON | (1 << bit);

		// Unimplemented
		if ((address <= 0x30) || (address >= 0x7F))
			return;
		if ((address <= 0xB0) || (address >= 0xFF))
			return;
	}

	static void clearBitAtAddress(int address, int bit) {
		int maske = 0;
		switch (bit) {
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
		// Bankabfrage
		if (Befehle.isBitSetAt(Register.STATUS, 5))
			address = address + 0x80;

		if ((address >= 0x0C) && (address <= 0x2F)) {
			// GPR
			GPR[address - 0x0C] = GPR[address - 0x0C] & maske;
		}
		if ((address >= 0x8C) && (address <= 0xAF)) {
			// GPR
			GPR[address - 0x8C] = GPR[address - 0x8C] & maske;
		}
		if ((address == 0x00) || (address == 0x80))
			setValueAtAddress(FSR, getValueAtAddress(FSR) & maske);

		if (address == 0x01)
			TMR0 = TMR0 & maske;
		if (address == 0x81)
			OPTION_REG = OPTION_REG & maske;
		if ((address == 0x02) || (address == 0x82))
			PCL = PCL & maske;
		if ((address == 0x03) || (address == 0x83))
			STATUS = STATUS & maske;
		if ((address == 0x04) || (address == 0x84))
			FSR = FSR & maske;
		if (address == 0x85)
			TRISA = TRISA & maske;
		if (address == 0x86)
			TRISB = TRISB & maske;
		if (address == 0x05)
			PORTA = PORTA & maske;
		if (address == 0x06)
			PORTB = PORTB & maske;

		// Unimplemented
		if ((address == 0x07) || (address == 0x87))
			return;
		if (address == 0x08)
			EEDATA = EEDATA & maske;
		if (address == 0x09)
			EEADR = EEADR & maske;
		if (address == 0x88)
			EECON1 = EECON1 & maske;
		if (address == 0x89)
			EECON2 = EECON2 & maske;
		if ((address == 0x0A) || (address == 0x8A))
			PCLATH = PCLATH & maske;
		if ((address == 0x0B) || (address == 0x8B))
			INTCON = INTCON & maske;

		// Unimplemented
		if ((address <= 0x30) || (address >= 0x7F))
			return;
		if ((address <= 0xB0) || (address >= 0xFF))
			return;
	}

	static void setValueAtAddress(int address, int value) {
		// Bankabfrage
		if (Befehle.isBitSetAt(Register.STATUS, 5))
			address = address + 0x80;

		if ((address >= 0x0C) && (address <= 0x2F)) {
			// GPR
			GPR[address - 0x0C] = value;
		}
		if ((address >= 0x8C) && (address <= 0xAF)) {
			// GPR
			GPR[address - 0x8C] = value;
		}
		if ((address == 0x00) || (address == 0x80))
		{
			if(FSR == 0x00 || FSR == 0x80)
			{
				// verhindert Endlosschleife
			}
			else
			{
				Register.setValueAtAddress(FSR, value);
			}
		}
		if (address == 0x01)
			TMR0 = value;
		if (address == 0x81)
			OPTION_REG = value;
		if ((address == 0x02) || (address == 0x82))
			PCL = value;
		if ((address == 0x03) || (address == 0x83))
			STATUS = value;
		if ((address == 0x04) || (address == 0x84))
			FSR = value;
		if (address == 0x85)
			TRISA = value;
		if (address == 0x86)
			TRISB = value;
		if (address == 0x05)
			PORTA = value;
		if (address == 0x06)
			PORTB = value;

		// Unimplemented
		if ((address == 0x07) || (address == 0x87))
			return;
		if (address == 0x08)
			EEDATA = value;
		if (address == 0x09)
			EEADR = value;
		if (address == 0x88)
			EECON1 = value;
		if (address == 0x89)
			EECON2 = value;
		if ((address == 0x0A) || (address == 0x8A))
			PCLATH = value;
		if ((address == 0x0B) || (address == 0x8B))
			INTCON = value;

		// Unimplemented
		if ((address <= 0x30) || (address >= 0x7F))
			return;
		if ((address <= 0xB0) || (address >= 0xFF))
			return;
	}

	// -----------------------------------------BANK0--------------------------------//
	public static int TMR0;
	// Real Time Clock

	public static int PCL;
	// Low order 8 bits of program counter

	public static int STATUS;
	// 7.Bit: IRP (Register Bank Select Bit)
	// 6.Bit: RP1 (Register Bank Select Bit)
	// 5.Bit: RP0 (Register Bank Select Bit)
	// 4.Bit: TO (Timeout)
	// 3.Bit: PD (Power Down Bit)
	// 2.Bit: Z (Zero Flag)
	// 1.Bit: DC (Digit Carry)
	// 0.Bit: C (Carry Bit)

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

	// -----------------------------------------BANK1--------------------------------//
	public static int INDF;
	public static int OPTION_REG;
	// 7.Bit: RBPU (Pull-up enable Bit)
	// 6.Bit: INTEDG (Interrupt Edge Select)
	// 5.Bit: T0CS (TMR0 Clock Source Select Bit)
	// 4.Bit: T0SE (TMR0 Source Edge Select Bit)
	// 3.Bit: PSA (Prescaler Assignement Bit)
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

	// WATCHDOG
	// WATCHDOG COUNTER
	// ZYKLEN
	//
	// STACK

}
