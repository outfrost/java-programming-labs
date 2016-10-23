/* 
 *  Interfejs UserDialog
 *  
 *  Prosta biblioteka metod do realizacji
 *  dialogu z użytkownikiem w prostych aplikacjach
 *  bez graficznego interfejsu użytkownika.
 *  
 *  Autor: Pawel Rogalinski
 *   Data: 1 pazdziernika 2016 r.
 */

public interface UserDialog{
	
	 /** Komunikat o błędnym formacie wprowadzonych danych. */
    static final String ERROR_MESSAGE =
          "Nieprawidłowe dane!\nSpróbuj jeszcze raz.";

  
	public void printMessage(String message);
	
	public void printInfoMessage(String message);
	
	public void printErrorMessage(String message);
	
	public void clearConsole();
	
	public String enterString(String prompt);
	
	public char enterChar(String prompt);
	
	public int enterInt(String prompt);
	
	public float enterFloat(String prompt);
	
	public double enterDouble(String prompt);
	
}
