/* 
 *  Klasa ConsoleUserDialog 
 *  - implementacja interfejsu UserDialog. 
 *  
 *  Prosta biblioteka metod do realizacji
 *  dialogu z użytkownikiem w prostych aplikacjach
 *  bez graficznego interfejsu użytkownika.
 *  
 *  Autor: Pawel Rogalinski
 *   Data: 1 pazdziernika 2016 r.
 */

import java.util.Scanner;

public class ConsoleUserDialog implements UserDialog {
	
	   private Scanner sc = new Scanner(System.in);

	   
	   @Override
		public void printMessage(String message) {
			System.out.println(message);
		}
		
		
		@Override
		public void printInfoMessage(String message) {
			System.out.println(message);
			enterString("Nacisnij ENTER");
		}
		
		
		@Override
		public void printErrorMessage(String message) {
			System.err.println(message);
			System.err.println("Nacisnij ENTER");
			enterString("");
		}
		
		
		@Override
		public void clearConsole(){
			System.out.println("\n\n");
		}

		
		@Override
		public String enterString(String prompt) {
			System.out.print(prompt);
			return sc.nextLine();
		}
		
		
		@Override
		public char enterChar(String prompt) {
			boolean isError;
			char c = ' ';
			do {
				isError = false;
				try {
					c = enterString(prompt).charAt(0);
				} catch (IndexOutOfBoundsException e) {
					System.err.println(ERROR_MESSAGE);
					isError = true;
				}
			} while (isError);
			return c;
		}

		
		@Override
		public int enterInt(String prompt) {
	        boolean isError;
	        int i = 0;
	        do{
	            isError = false;
	            try{ 
	                i = Integer.parseInt(enterString(prompt));
	            } catch(NumberFormatException e){
	            	System.err.println(ERROR_MESSAGE);
	            	isError = true;
	            }
	        }while(isError);
	        return i;
	    }
		
		
		@Override
		public float enterFloat(String prompt) {
	        boolean isError;
	        float d = 0;
	        do{
	            isError = false;
	            try{
	                d = Float.parseFloat(enterString(prompt));
	            } catch(NumberFormatException e){
	            	System.err.println(ERROR_MESSAGE);
	                isError = true;
	            }
	        } while(isError);
	        return d;
	    }   
		
		
		@Override
		public double enterDouble(String prompt) {
	        boolean isError;
	        double d = 0;
	        do{
	            isError = false;
	            try{
	                d = Double.parseDouble(enterString(prompt));
	            } catch(NumberFormatException e){
	            	System.err.println(ERROR_MESSAGE);
	                isError = true;
	            }
	        }while(isError);
	        return d;
	    }   

		
}



