package Proyect;

public class Addendum {
    
    public String Addendum(String msj){
        
        String addendum;
        String[] noSpace = msj.replaceAll(" ", "").split("\\s+");
        int p = Integer.parseInt(noSpace[0]);
        
        for (int x = 1; x < noSpace.length; x++){
            String xor = String.valueOf(noSpace[x]);
            int xorInt = Integer.parseInt(xor);
            p = (p^xorInt);
        }
        addendum = String.valueOf(p);       
        return addendum;
    }    
}
