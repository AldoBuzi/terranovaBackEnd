package it.edu.polomanettiporciatti.terranovaclient;
import java.util.Timer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.TimerTask;

public class TerranovaMain extends TimerTask{
	
	private TerranovaMain(){
	}
	public void run(){
		String[] CityName= {"Bergamo","Napoli","Grosseto"};
		String result="";
		RequestToTerranova req= new RequestToTerranova(); //creo l'oggetto che ci permettera di inviare la richiesta a terranova e confrontare i dati
		for (String string : CityName) {
			System.out.println("Inviata richiesta per "+string);
			result+=req.SendGetRequest(string);			
		}
		try {
			req.CompareData(result);
			System.out.println("Richiesta effettuata con successo");
		} catch (IOException e) {
			System.out.println("Richiesta fallita, error:  "+e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Prossima richiesta tra 60 secondi");
	}
    public static void main(String[] args) throws IOException{
		Timer DataRefresh= new Timer("RefreshThread", false) ;
		System.out.println("Invio Richiesta");
		TimerTask ProgramMain = new TerranovaMain();
		DataRefresh.schedule(ProgramMain, 10,60000);
    }

}