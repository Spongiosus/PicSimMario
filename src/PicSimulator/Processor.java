package PicSimulator;

public class Processor {
		
	
	public static void main(String[] args) { 
		
		
        String dateiName = "C://test.lst";
        Parser.ladeDatei(dateiName);
        
        int befehlsZeilenNummer = 0;
        
        int byte1 = 0;
        int byte2 = 0;  
        
        
        Register.W_REGISTER = 0x02;
        Register.GPR[0] = 240;
        Befehle.fuehreBefehlAus("COMF", 0, 0x0C);

        
        //System.out.println(((byte1 & 0b11) << 1) + ((byte2 & 0b10000000) >> 7));
        /*
        while(Parser.befehlszeilen[befehlsZeilenNummer] != null)
        {
        	
        	// Befehlszeile holen TODO
        	
        	byte1 = Integer.parseInt(Parser.befehlszeilen[befehlsZeilenNummer].substring(5, 7), 16);
        	byte2 = Integer.parseInt(Parser.befehlszeilen[befehlsZeilenNummer].substring(7, 9), 16);   	
        	
        	String aktuellerBefehl = Decoder.erkenneBefehl(byte1, byte2);
        	System.out.printf(aktuellerBefehl + "		Arg1:	0x%x	Arg2:	0x%x\n",byte1,byte2);
        	
        	Befehle.fuehreBefehlAus(aktuellerBefehl, byte1, byte2);
        	
        	befehlsZeilenNummer = ...;
        }
        */
        System.out.println("FIN");
        return;
   }

}


