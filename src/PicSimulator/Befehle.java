package PicSimulator;

public class Befehle {

	// stelle => 0-7 !
	public static boolean isBitSetAt(int myByte, int stelle) {
		switch (stelle) {
		case 0:
			return ((myByte & 1) != 0);
		case 1:
			return ((myByte & 0b10) != 0);
		case 2:
			return ((myByte & 0b100) != 0);
		case 3:
			return ((myByte & 0b1000) != 0);
		case 4:
			return ((myByte & 0b10000) != 0);
		case 5:
			return ((myByte & 0b100000) != 0);
		case 6:
			return ((myByte & 0b1000000) != 0);
		case 7:
			return ((myByte & 0b10000000) != 0);
		case 8:
			return ((myByte & 0b10000000) != 0);
		default:
			return false;
		}
	}

	public static void setCarryFlag() {
		Register.setBitAtAddress(3, 0);
	}

	public static void setDigitCarryFlag() {
		Register.setBitAtAddress(3, 1);
	}

	public static void setZeroFlag() {
		Register.setBitAtAddress(3, 2);
	}

	public static void setPDFlag() {
		Register.setBitAtAddress(3, 3);
	}

	public static void setTOFlag() {
		Register.setBitAtAddress(3, 4);
	}

	public static void setIRPFlag() {
		Register.setBitAtAddress(3, 7);
	}

	public static void clearCarryFlag() {
		Register.clearBitAtAddress(3, 0);
	}

	public static void clearDigitCarryFlag() {
		Register.clearBitAtAddress(3, 1);
	}

	public static void clearZeroFlag() {
		Register.clearBitAtAddress(3, 2);
	}

	public static void clearPDFlag() {
		Register.clearBitAtAddress(3, 3);
	}

	public static void clearTOFlag() {
		Register.clearBitAtAddress(3, 4);
	}

	public static void clearIRPFlag() {
		Register.clearBitAtAddress(3, 7);
	}

	public static void fuehreBefehlAus(String befehl, int byte1, int byte2) {
		int result = 0;
		int addressf = 0;
		int valuef = 0;

		switch (befehl) {
		// --------------- Bytebefehle -------------- //

		case "ADDWF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);

			result = Register.W_REGISTER + valuef;
			// Auf C, DC, Z prüfen
			if (result == 0)
				setZeroFlag();
			else
				clearZeroFlag();

			if (isBitSetAt(result, 8))
				setCarryFlag();
			else
				clearCarryFlag();

			// DC
			if ((valuef & 0b00001111) + (Register.W_REGISTER & 0b00001111) > 15)
				setDigitCarryFlag();
			else
				clearDigitCarryFlag();

			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);

			Processor.zyklen++;
			break;

		case "ANDWF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);

			result = Register.W_REGISTER & valuef;
			// Auf Z prüfen
			if (result == 0)
				setZeroFlag();
			else
				clearZeroFlag();

			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);

			Processor.zyklen++;
			break;

		case "CLRF":
			addressf = (byte2 & 0b01111111);
			Register.setValueAtAddress(addressf, 0);

			setZeroFlag();
			Processor.zyklen++;
			break;

		case "CLRW":
			Register.W_REGISTER = 0;

			setZeroFlag();
			Processor.zyklen++;
			break;

		case "COMF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);

			result = (~valuef) & 0xFF;
			// Auf Z prüfen
			if (result == 0)
				setZeroFlag();
			else
				clearZeroFlag();

			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);

			Processor.zyklen++;
			break;

		case "DECF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);

			if (valuef == 0)
				result = 0xFF;
			else
				result = (valuef - 1);

			// Z prüfen
			if (result == 0)
				setZeroFlag();
			else
				clearZeroFlag();

			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);

			Processor.zyklen++;
			break;

		case "DECFSZ":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);

			if (valuef == 0)
				result = 0xFF;
			else
				result = (valuef - 1);

			if (result == 0) {
				Register.PCL++;
				Processor.zyklen++;
			}

			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);

			Processor.zyklen++;
			break;

		case "INCF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			if (valuef == 0xFF)
				result = 0;
			else
				result = (valuef + 1);
			// Z prüfen
			if (result == 0)
				setZeroFlag();
			else
				clearZeroFlag();

			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);

			Processor.zyklen++;
			break;

		case "INCFSZ":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			if (valuef == 0xFF)
				result = 0;
			else
				result = (valuef + 1);

			if (result == 0) {
				Register.PCL++;
				Processor.zyklen++;
			}

			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);

			Processor.zyklen++;
			break;

		case "IORWF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);

			result = Register.W_REGISTER | valuef;

			// Z quer
			if (result == 0)
				clearZeroFlag();
			else
				setZeroFlag();

			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);

			Processor.zyklen++;
			break;

		case "MOVF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);

			if (valuef == 0)
				setZeroFlag();
			else
				clearZeroFlag();

			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, valuef);
			else
				Register.W_REGISTER = valuef;

			Processor.zyklen++;
			break;

		case "MOVWF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);

			Register.setValueAtAddress(addressf, Register.W_REGISTER);

			Processor.zyklen++;
			break;

		case "NOP":
			Processor.zyklen++;
			break;

		case "CLRWDT":
			Processor.wdt.reset();
			Processor.wdt.resetPrescaler();
			Befehle.setPDFlag();
			Befehle.setTOFlag();

			Processor.zyklen++;
			break;

		case "RETFIE":
			Register.PCL = Register.stack.pop();

			// Gerneral Interruption Enable
			Register.setBitAtAddress(0x0B, 7);

			Processor.zyklen++;
			Processor.zyklen++;
			break;

		case "RETURN":
			Register.PCL = Register.stack.pop();

			Processor.zyklen++;
			Processor.zyklen++;
			break;

		case "SLEEP":
			Processor.wdt.reset();
			Processor.wdt.resetPrescaler();
			Befehle.setTOFlag();
			Befehle.clearPDFlag();

			Processor.zyklen++;
			break;

		case "RLF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);

			result = valuef << 1;

			if (isBitSetAt(Register.STATUS, 0))
				result++;

			// Check Carry Flag
			if (isBitSetAt(result, 8))
				setCarryFlag();
			else
				clearCarryFlag();

			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, result & 0b011111111);
			else
				Register.W_REGISTER = result & 0b011111111;

			Processor.zyklen++;
			break;

		case "RRF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);

			boolean setFlag = false;
			if (isBitSetAt(result, 0))
				setFlag = true;

			result = valuef >> 1;

			if (isBitSetAt(Register.STATUS, 0))
				result = result | 0x80;
			if (setFlag)
				setCarryFlag();
			else
				clearCarryFlag();

			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, result & 0b011111111);
			else
				Register.W_REGISTER = result & 0b011111111;

			Processor.zyklen++;
			break;

		case "SUBWF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);

			result = valuef + (((~Register.W_REGISTER) + 1) & 0xFF);

			// Auf C, DC, Z prüfen
			if ((result & 0b011111111) == 0)
				setZeroFlag();
			else
				clearZeroFlag();
			if (isBitSetAt(result, 8))
				setCarryFlag();
			else
				clearCarryFlag();

			// DC
			if ((valuef & 0b00001111)
					+ (~(Register.W_REGISTER & 0b00001111) + 1) > 15)
				setDigitCarryFlag();
			else
				clearDigitCarryFlag();

			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, result & 0b011111111);
			else
				Register.W_REGISTER = result & 0b011111111;

			Processor.zyklen++;
			break;

		case "SWAPF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);

			result = ((valuef & 0b1111) << 4) + ((valuef & 0b11110000) >> 4);

			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, result & 0b011111111);
			else
				Register.W_REGISTER = result & 0b011111111;

			Processor.zyklen++;
			break;

		case "XORWF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);

			result = Register.W_REGISTER ^ valuef;
			// Auf Z prüfen
			if (result == 0)
				setZeroFlag();
			else
				clearZeroFlag();

			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if (isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);

			Processor.zyklen++;
			break;

		// --------------- Bitbefehle -------------- //

		case "BCF":
			addressf = (byte2 & 0b01111111);
			// ersten 2 bit des ersten byte und das letzte bit des zweiten byte
			// ergibt das zu löschende bit
			Register.clearBitAtAddress(addressf, ((byte1 & 0b11) << 1)
					+ ((byte2 & 0b10000000) >> 7));

			Processor.zyklen++;
			break;

		case "BSF":
			addressf = (byte2 & 0b01111111);
			// ersten 2 bit des ersten byte und das letzte bit des zweiten byte
			// ergibt das zu setzende bit
			Register.setBitAtAddress(addressf, ((byte1 & 0b11) << 1)
					+ ((byte2 & 0b10000000) >> 7));

			Processor.zyklen++;
			break;

		case "BTFSC":
			addressf = (byte2 & 0b01111111);

			if (isBitSetAt(Register.getValueAtAddress(addressf),
					((byte1 & 0b11) << 1) + ((byte2 & 0b10000000) >> 7))) {
				return;
			} else {
				Register.PCL++;
				Processor.zyklen++;
			}

			Processor.zyklen++;
			break;

		case "BTFSS":
			addressf = (byte2 & 0b01111111);

			if (isBitSetAt(Register.getValueAtAddress(addressf),
					((byte1 & 0b11) << 1) + ((byte2 & 0b10000000) >> 7))) {
				Register.PCL++;
				Processor.zyklen++;
			} else {
				return;
			}

			Processor.zyklen++;
			break;

		// --------------- Literal/Sprungbefehle --------------- //

		case "ADDLW":
			result = Register.W_REGISTER + byte2;
			// Auf C, DC, Z prüfen
			if (result == 0)
				setZeroFlag();
			else
				clearZeroFlag();
			if (isBitSetAt(result, 8))
				setCarryFlag();
			else
				clearCarryFlag();

			// DC
			if ((byte2 & 0b00001111) + (Register.W_REGISTER & 0b00001111) > 15)
				setDigitCarryFlag();
			else
				clearDigitCarryFlag();

			Register.W_REGISTER = (result & 0b011111111);

			Processor.zyklen++;
			break;

		case "ANDLW":
			result = Register.W_REGISTER & byte2;
			// Auf Z prüfen
			if (result == 0)
				setZeroFlag();
			else
				clearZeroFlag();

			Register.W_REGISTER = (result & 0b011111111);

			Processor.zyklen++;
			break;

		case "CALL":
			Register.stack.push(Register.PCL + 1);
			// Nach jedem Befehl wird der PCL erhöht (bei CALL nicht notwendig)
			Register.PCL = byte2 - 1;
			Register.PCLATH = (byte1 & 0b111);

			Processor.zyklen++;
			Processor.zyklen++;
			break;
		case "GOTO":
			// Nach jedem Befehl wird der PCL erhöht (bei GOTO nicht notwendig)
			Register.PCL = byte2 - 1;
			Register.PCLATH = (byte1 & 0b111);

			Processor.zyklen++;
			Processor.zyklen++;
			break;
		case "IORLW":
			result = Register.W_REGISTER & byte2;

			if (result == 0)
				setZeroFlag();
			else
				clearZeroFlag();

			Register.W_REGISTER = (result & 0b011111111);

			Processor.zyklen++;
			break;

		case "MOVLW":
			Register.W_REGISTER = byte2;

			Processor.zyklen++;
			break;

		case "RETLW":
			Register.PCL = Register.stack.pop();
			Register.W_REGISTER = byte2;

			Processor.zyklen++;
			Processor.zyklen++;
			break;

		case "SUBLW":
			result = byte2 + (((~Register.W_REGISTER) + 1) & 0xFF);

			// Auf C, DC, Z prüfen
			if ((result & 0b011111111) == 0)
				setZeroFlag();
			else
				clearZeroFlag();
			if (isBitSetAt(result, 8))
				setCarryFlag();
			else
				clearCarryFlag();

			// DC
			if ((valuef & 0b00001111)
					+ (~(Register.W_REGISTER & 0b00001111) + 1) > 15)
				setDigitCarryFlag();
			else
				clearDigitCarryFlag();

			Register.W_REGISTER = result & 0b011111111;

			Processor.zyklen++;
			break;

		case "XORLW":
			result = Register.W_REGISTER ^ byte2;
			// Auf Z prüfen
			if (result == 0)
				setZeroFlag();
			else
				clearZeroFlag();

			Register.W_REGISTER = (result & 0b011111111);

			Processor.zyklen++;
			break;

		}
		Register.PCL++;

	}

}
