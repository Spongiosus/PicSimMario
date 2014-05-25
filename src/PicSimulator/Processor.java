package PicSimulator;

public class Processor {
		
	
	public static void main(String[] args) { 
		
		
        String dateiName = "C://test.lst";
        Parser.ladeDatei(dateiName);
        
        
        int byte1 = 0;
        int byte2 = 0;  
        
        Register.powerOnReset();

        while(Parser.befehlszeilen[Register.PCL] != null)
        {
        	
        	
        	byte1 = Integer.parseInt(Parser.befehlszeilen[Register.PCL].substring(5, 7), 16);
        	byte2 = Integer.parseInt(Parser.befehlszeilen[Register.PCL].substring(7, 9), 16);   	
        	
        	String aktuellerBefehl = Decoder.erkenneBefehl(byte1, byte2);
        	System.out.printf(aktuellerBefehl + "		Arg1:	0x%x	Arg2:	0x%x\n",byte1,byte2);
        	
        	Befehle.fuehreBefehlAus(aktuellerBefehl, byte1, byte2);
        	      	
        }
        
        System.out.println("FIN");
        return;
   }

}


