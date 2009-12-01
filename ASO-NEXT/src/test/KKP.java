package test;

import java.io.Console;

public class KKP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean a = true;
		if(true && (a=getFalse())){
			System.out.println("Jan heeft gelijk");
		}else
		{
			System.out.println("Patrick heeft gelijk");
		}

	}
	
	
	
	static boolean getFalse(){
		
		return false;
	}

}
