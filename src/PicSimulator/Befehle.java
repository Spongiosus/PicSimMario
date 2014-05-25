package PicSimulator;

public class Befehle {
	
	// stelle => 0-7 !
	public static boolean isBitSetAt (int myByte, int stelle)
	{
		switch(stelle)
		{
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

	
	
	public static void setCarryFlag()
	{
		Register.setBitAtAddress(3, 0);
	}
	public static void setDigitCarryFlag()
	{
		Register.setBitAtAddress(3, 1);
	}
	public static void setZeroFlag()
	{
		Register.setBitAtAddress(3, 2);
	}
	public static void setPDFlag()
	{
		Register.setBitAtAddress(3, 3);
	}
	public static void setTOFlag()
	{
		Register.setBitAtAddress(3, 4);
	}
	public static void setRP0Flag()
	{
		Register.setBitAtAddress(3, 5);
	}
	public static void setRP1Flag()
	{
		Register.setBitAtAddress(3, 6);
	}
	public static void setIRPFlag()
	{
		Register.setBitAtAddress(3, 7);
	}
	
	
	public static void clearCarryFlag()
	{
		Register.clearBitAtAddress(3, 0);
	}
	public static void clearDigitCarryFlag()
	{
		Register.clearBitAtAddress(3, 1);
	}
	public static void clearZeroFlag()
	{
		Register.clearBitAtAddress(3, 2);
	}
	public static void clearPDFlag()
	{
		Register.clearBitAtAddress(3, 3);
	}
	public static void clearTOFlag()
	{
		Register.clearBitAtAddress(3, 4);
	}
	public static void clearRP0Flag()
	{
		Register.clearBitAtAddress(3, 5);
	}
	public static void clearRP1Flag()
	{
		Register.clearBitAtAddress(3, 6);
	}
	public static void clearIRPFlag()
	{
		Register.clearBitAtAddress(3, 7);
	}

	
	
	
	public static void fuehreBefehlAus (String befehl, int byte1, int byte2)
	{
		int result = 0;
		int addressf = 0;
		int valuef = 0;
		
		switch(befehl)
		{
		// --------------- Bytebefehle -------------- //
		
		
		case "ADDWF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			
			result = Register.W_REGISTER + valuef;
			// Auf C, DC, Z prüfen
			if(result == 0)
				setZeroFlag();
			if(isBitSetAt(result, 8))
				setCarryFlag();
			
			// DC
			if((valuef & 0b00001111) + (Register.W_REGISTER & 0b00001111) > 15)
				setDigitCarryFlag();
			
			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if(isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);	
			break;
			
			
		case "ANDWF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			
			result = Register.W_REGISTER & valuef;
			// Auf Z prüfen
			if(result == 0)
				setZeroFlag();
			
			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if(isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);	
			break;
			
			
		case "CLRF":
			addressf = (byte2 & 0b01111111);
			Register.setValueAtAddress(addressf, 0);
			
			setZeroFlag();
			break;
			
			
		case "CLRW":
			Register.W_REGISTER = 0;
			
			setZeroFlag();
			break;
			
			
		case "COMF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			
			result = (~valuef) & 0xFF;
			// Auf Z prüfen
			if(result == 0)
				setZeroFlag();
			
			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if(isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);	
			break;
			
			
		case "DECF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			
			if(valuef == 0)
				result = 0xFF;
			else
				result = valuef--;
			
			// Z prüfen
			if(result == 0)
				setZeroFlag();
			
			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if(isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);	
			break;
			
			
		case "DECFSZ":
			break;
			
			
		case "INCF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			if(valuef == 0xFF)
				result = 0;
			else
				result = valuef++;
			// Z prüfen
			if(result == 0)
				setZeroFlag();
			
			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if(isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);	
			break;
			
			
		case "INCFSZ":
			break;
			
			
		case "IORWF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			
			result = Register.W_REGISTER | valuef;

			// Z quer
			if(result == 0)
				clearZeroFlag();
			else
				setZeroFlag();
			
			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if(isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);	
			break;
			
			
		case "MOVF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			
			if(valuef == 0)
				setZeroFlag();
						
			if(isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, valuef);
			else
				Register.W_REGISTER = valuef;
			break;			
			
			
		case "MOVWF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			
			Register.setValueAtAddress(addressf, Register.W_REGISTER);
			break;
			
			
		case "NOP":
			break;
			
			
		case "CLRWDT":
			break;
			
			
		case "RETFIE":
			break;
			
			
		case "RETURN":
			break;
			
			
		case "SLEEP":
			break;
			
			
		case "RLF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			
			result = valuef << 1;
			
			if(isBitSetAt(Register.STATUS, 0))
				result++;
			if(isBitSetAt(result, 8))
				setCarryFlag();
						
			if(isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, result & 0b011111111);
			else
				Register.W_REGISTER = result & 0b011111111;
			break;	
			
			
		case "RRF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			
			boolean setFlag = false;
			if(isBitSetAt(result, 0))
				setFlag = true;
			
			result = valuef >> 1;
			
			if(isBitSetAt(Register.STATUS, 0))
				result = result | 0x80;			
			if(setFlag)
				setCarryFlag();
						
			if(isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, result & 0b011111111);
			else
				Register.W_REGISTER = result & 0b011111111;
			break;	
			
			
		case "SUBWF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			
			result = valuef + (((~Register.W_REGISTER) + 1) & 0xFF);
			
			// Auf C, DC, Z prüfen
			if((result & 0b011111111) == 0)
				setZeroFlag();
			if(isBitSetAt(result, 8))
				setCarryFlag();
						
			// DC
			if((valuef & 0b00001111) + ( ~ (Register.W_REGISTER & 0b00001111) + 1) > 15)
				setDigitCarryFlag();
			
			if(isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, result & 0b011111111);
			else
				Register.W_REGISTER = result & 0b011111111;
			break;			
			
			
		case "SWAPF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			
			result = ((valuef & 0b1111) << 4) + (valuef & 0b11110000) >> 4;
			
			if(isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, result & 0b011111111);
			else
				Register.W_REGISTER = result & 0b011111111;
			break;
			
			
		case "XORWF":
			addressf = (byte2 & 0b01111111);
			valuef = Register.getValueAtAddress(addressf);
			
			result = Register.W_REGISTER ^ valuef;
			// Auf Z prüfen
			if(result == 0)
				setZeroFlag();
			
			// Wenn bit 7 in byte 2 gesetzt ist, stored in f, else in W
			if(isBitSetAt(byte2, 7))
				Register.setValueAtAddress(addressf, (result & 0b011111111));
			else
				Register.W_REGISTER = (result & 0b011111111);		
			break;
			
			
		
		// --------------- Bitbefehle -------------- //
			
		case "BCF":
			addressf = (byte2 & 0b01111111);
			// ersten 2 bit des ersten byte und das letzte bit des zweiten byte ergibt das zu löschende bit
			Register.clearBitAtAddress(addressf, ((byte1 & 0b11) << 1) + ((byte2 & 0b10000000) >> 7));
			break;
			
			
		case "BSF":
			addressf = (byte2 & 0b01111111);
			// ersten 2 bit des ersten byte und das letzte bit des zweiten byte ergibt das zu setzende bit
			Register.setBitAtAddress(addressf, ((byte1 & 0b11) << 1) + ((byte2 & 0b10000000) >> 7));
			break;
			
			
		case "BTFSC":
			addressf = (byte2 & 0b01111111);
			
			if(isBitSetAt(Register.getValueAtAddress(addressf) ,((byte1 & 0b11) << 1) + ((byte2 & 0b10000000) >> 7)))
			{
				
			}
			break;
			
			
		case "BTFSS":
			break;
			
		// --------------- Literal/Sprungbefehle --------------- //
			
		case "ADDLW":
			result = Register.W_REGISTER + byte2;
			// Auf C, DC, Z prüfen
			if(result == 0)
				setZeroFlag();
			if(isBitSetAt(result, 8))
				setCarryFlag();
			
			//DC
			if((byte2 & 0b00001111) + (Register.W_REGISTER & 0b00001111) > 15)
				setDigitCarryFlag();

				Register.W_REGISTER = (result & 0b011111111);			
			break;

			
		case "ANDLW":
			result = Register.W_REGISTER & byte2;
			// Auf Z prüfen
			if(result == 0)
				setZeroFlag(); //TODO

			Register.W_REGISTER = (result & 0b011111111);			
			break;
			
			
		case "CALL":
			Register.stack.push(Register.PCL + 1);
			Register.PCL = byte2;
			Register.PCLATH = (byte1 & 0b111);
			break;
		case "GOTO":
			Register.PCL = byte2;
			Register.PCLATH = (byte1 & 0b111);
			break;
		case "IORLW":
			result = Register.W_REGISTER & byte2;

			setZeroFlag(); // TODO

			Register.W_REGISTER = (result & 0b011111111);			
			break;			
			
			
		case "MOVLW":
			Register.W_REGISTER = byte2;
			break;
			
			
		case "RETLW":
			break;
		case "SUBLW":		
			result = byte2 + (((~Register.W_REGISTER) + 1) & 0xFF);
			
			// Auf C, DC, Z prüfen
			if((result & 0b011111111) == 0)
				setZeroFlag();
			if(isBitSetAt(result, 8))
				setCarryFlag();
						
			// DC
			if((valuef & 0b00001111) + ( ~ (Register.W_REGISTER & 0b00001111) + 1) > 15)
				setDigitCarryFlag();
			
			Register.W_REGISTER = result & 0b011111111;
			break;
			
			
		case "XORLW":
			result = Register.W_REGISTER ^ byte2;
			// Auf Z prüfen
			if(result == 0)
				setZeroFlag();

			Register.W_REGISTER = (result & 0b011111111);			
			break;
			

		}
		Register.PCL++;
		
	}

}
