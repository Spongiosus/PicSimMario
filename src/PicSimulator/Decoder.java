package PicSimulator;

public class Decoder {

	// befehl = erstes byte
	public static String erkenneBefehl(int byte1, int byte2) {

		switch (byte1) {
		case 0b00000111: // ADDWF
			return "ADDWF";
		case 0b00000101: // ANDWF
			return "ANDWF";
		case 0b00000001: // CLRF oder CLRW 8.Bit anschauen
			if ((byte2 & 0b10000000) != 0)
				return "CLRF";
			return "CLRW";
		case 0b00001001: // COMF
			return "COMF";
		case 0b00000011: // DECF
			return "DECF";
		case 0b00001011: // DECFSZ
			return "DECFSZ";
		case 0b00001010: // INCF
			return "INCF";
		case 0b00001111: // INCFSZ
			return "INCFSZ";
		case 0b00000100: // IORWF
			return "IORWF";
		case 0b00001000: // MOVF
			return "MOVF";
		case 0b00000000: // MOVWF oder NOP oder CLRWDT oder RETFIE oder RETURN
							// oder SLEEP
			if ((byte2 & 0b10000000) != 0)
				return "MOVWF";
			if ((byte2 & 0b01100100) == 255)
				return "CLRWDT";
			if ((byte2 & 0b00001001) == 255)
				return "RETFIE";
			if ((byte2 & 0b00001000) == 255)
				return "RETURN";
			if ((byte2 & 0b01100011) == 255)
				return "SLEEP";
			return "NOP";
		case 0b00001101: // RLF
			return "RLF";
		case 0b00001100: // RRF
			return "RRF";
		case 0b00000010: // SUBWF
			return "SUBWF";
		case 0b00001110: // SWAPF
			return "SWAPF";
		case 0b00000110: // XORWF
			return "XORWF";
		case 0b00010000:
		case 0b00010001:
		case 0b00010010:
		case 0b00010011: // BCF
			return "BCF";
		case 0b00010100:
		case 0b00010101:
		case 0b00010110:
		case 0b00010111: // BSF
			return "BSF";
		case 0b00011000:
		case 0b00011001:
		case 0b00011010:
		case 0b00011011: // BTFSC
			return "BTFSC";
		case 0b00011100:
		case 0b00011101:
		case 0b00011110:
		case 0b00011111: // BTFSS
			return "BTFSS";
		case 0b00111110:
		case 0b00111111: // ADDLW
			return "ADDLW";
		case 0b00111001: // ANDLW
			return "ANDLW";
		case 0b00100000:
		case 0b00100001:
		case 0b00100010:
		case 0b00100011:
		case 0b00100100:
		case 0b00100101:
		case 0b00100110:
		case 0b00100111: // CALL
			return "CALL";
		case 0b00101000:
		case 0b00101001:
		case 0b00101010:
		case 0b00101011:
		case 0b00101100:
		case 0b00101101:
		case 0b00101110:
		case 0b00101111: // GOTO
			return "GOTO";
		case 0b00111000: // IORLW
			return "IORLW";
		case 0b00110000:
		case 0b00110001:
		case 0b00110010:
		case 0b00110011: // MOVLW
			return "MOVLW";
		case 0b00110100:
		case 0b00110101:
		case 0b00110110:
		case 0b00110111: // RETLW
			return "RETLW";
		case 0b00111100:
		case 0b00111101: // SUBLW
			return "SUBLW";
		case 0b00111010: // XORLW
			return "XORLW";

		default:
			return "ERROR";
		}
	}

}
