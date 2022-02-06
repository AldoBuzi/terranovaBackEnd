package it.edu.polomanettiporciatti.terranovaclient;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.*;
public class RequestToTerranova {
    private static String  DataStart, DataEnd, token, RequestUrl, user, pass, PutUrl;
    ObjectMapper JsonMapper;
    StringBuilder DataResult;
    HttpURLConnection conn;
    MeteoDataConverted[] map;
    List<MeteoDataConverted> MixedMapList= new ArrayList<>();
    static Properties conf; 
    
public RequestToTerranova(){
    try{
        //Utilizzo la classe Properties per ottenere i valori dei parametri, di seguito tutta la struttura per caricare il file e ottenere i parametri
        conf= new Properties();
        FileInputStream path= new FileInputStream("../Config.txt");
        conf.load(path);
        JsonMapper = new ObjectMapper();
        DataStart=conf.getProperty("STARTDATE");
        DataEnd=conf.getProperty("ENDDATE");
        token= conf.getProperty("TOKEN");
        RequestUrl= conf.getProperty("REQUESTURL");
        user= conf.getProperty("USERNAME");
        pass = conf.getProperty("PASS");
        PutUrl= conf.getProperty("PUTURL");
        path.close();
        }
        catch(Exception e){}
}
public  String SendGetRequest(String city)  {
     try{
        DataResult= new StringBuilder();
        URL url = new URL(RequestUrl+"token="+token+"&Localita="+city+"&datainizio="+DataStart+"&datafine="+DataEnd);
        conn = (HttpURLConnection) url.openConnection(); //apro la connessione con l'url superiore
        conn.setRequestMethod("GET"); //definisco il metodo della richiesta
        try (var reader = new BufferedReader( //definisco il BufferedReader e ottengo i dati contenuti nel input stream dell'oggetto conn
                new InputStreamReader(conn.getInputStream()))) {
          for (String line; (line = reader.readLine()) != null; ) {
              DataResult.append(line);
          }
      }
      map = JsonMapper.readValue(DataResult.toString(), MeteoDataConverted[].class); //converto i dati json e li mappo nella classe MeteoDataConverted tramite il metodo readValue
      MixArray(); //unisco tutti gli array delle tre citta
        return  DataResult.toString();
     }catch(Exception e){System.out.println(e); return "Something go wrong \n"+e; }
   }



public void CompareData(String result) throws IOException, SQLException{
     //Da rivedere il confronto tra i dati, nel prossimo meeting affrontero la questione degli id
    String ToCompare = Files.readString(Path.of("JsonData.txt"),StandardCharsets.US_ASCII); //leggo da file l'ultimo id massimo ottenuto
    if(result.indexOf("500 for URL")!=-1) {
        FileOutputStream out = new FileOutputStream("Config.txt");
        System.out.println("Errore Token Scaduto");
        String s=SendPutRequest();
        System.out.println(s);
        conf.setProperty("TOKEN",s );
        conf.store(out, null);
        out.close();
        System.out.println("Nuovo Token ottenuto con successo");
    
    
    return;}
    if(Integer.parseInt(ToCompare)<GetMaxID()){ //confronto l'id salvato con il nuovo id, se e' minore allora sono presenti nuovi dati
        System.out.println("Incogruenza dei dati, provvedimento necessario");
        SaveDataToDB MysqlConn= new SaveDataToDB(conf); //creo l'oggetto che ci permettera di salvare i dati nel db
        MysqlConn.TryConn(); //apro la connessione con il db
        System.out.println(MysqlConn.GetRecordsCount());
        for(int i=MysqlConn.GetRecordsCount();i<MixedMapList.size();i++){
            MysqlConn.SendQuery(MixedMapList.get(i));
        }
        ToCompare=""+GetMaxID();
        String path = "JsonData.txt";
        Files.write( Path.of(path), ToCompare.getBytes());
        System.out.println("Dati aggiornati con successo");
    }
    else{
        System.out.println("Dati già aggiornati, nulla da aggiornare");
    }

}
private String SendPutRequest(){
    DataResult= new StringBuilder();
    try {
        URL url = new URL(PutUrl+"username="+user+"&password="+pass);
        conn = (HttpURLConnection) url.openConnection(); 
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        OutputStreamWriter out = new OutputStreamWriter(
        conn.getOutputStream());
        out.write("Resource content");
        out.close();
        try (var reader = new BufferedReader( //definisco il BufferedReader e ottengo i dati contenuti nel input stream dell'oggetto conn
                new InputStreamReader(conn.getInputStream()))) {
          for (String line; (line = reader.readLine()) != null; ) {
              DataResult.append(line);
          }
        }
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    String s=DataResult.toString().replaceAll("^\"|\"$", "");
    return s;
}
private int GetMaxID(){
    int max=0;
   for(int i=0;i<MixedMapList.size();i++){
            if(MixedMapList.get(i).GetID()>=max){
                max = MixedMapList.get(i).GetID();
            }
   }
   System.out.println("Il massimo e "+max);
   return max;
}
private void MixArray(){
       for(int j=0;j<map.length;j++){
            MixedMapList.add(map[j]);
       } 
}

}