package PicSimulator;
import java.io.BufferedReader; 
import java.io.File; 
import java.io.FileReader; 
import java.io.IOException; 

public class Parser {
	
    static String[] zeilen = new String[1024];
    static String[] befehlszeilen = new String[1024];
	
	static void ladeDatei(String dateiName) { 

        File file = new File(dateiName);
        int zeilennummer = 0;
        int befehlszeilennummer = 0;
        
        Parser.zeilen = new String[1024];
        Parser.befehlszeilen = new String[1024];

        if (!file.canRead() || !file.isFile()) 
        {		
        	System.out.println("Coud not open file");
        	return;	
        }
        	
        BufferedReader in = null; 
        try { 
            in = new BufferedReader(new FileReader(dateiName)); 
            String zeile = null; 
            
            while ((zeile = in.readLine()) != null) { 
            	if (zeile.charAt(0) != 32)
            		{
            			befehlszeilen[befehlszeilennummer++] = zeile.substring(0, 9);
            			//System.out.println(befehlszeilen[befehlszeilennummer-1]); 
            		}
            	zeilen[zeilennummer++] = zeile;
                // System.out.println(zeile);             
            } 
            
        } catch (IOException e) { 
            e.printStackTrace(); 
        } finally { 
            if (in != null) 
                try { 
                    in.close(); 
                } catch (IOException e) { 
                } 
        }      
               
    } 

}
