package it.edu.polomanettiporciatti.terranovaclient;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
public class SaveDataToDB{
    Map<String, Number> CityId= new HashMap<String, Number>();
    String[] EventsName={
        "",
        "neve",
        "nebbia",
        "pioggia",
        "temporale",
        "grandine"
    };
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static String DRIVER;
    private static String COUNT;
    private static String INSERT = "INSERT INTO weather " + "(id, DataRequested, RainCM, AVGPression, SLMPression, DewPoint, Gust, TMax, TAVG, TMin, Humidity, MaxWind, AVGWind, Visibility, sunny, snow, fog, rain, storm, hail, CityId) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private Connection conn;
    public SaveDataToDB(Properties conf){
        URL= conf.getProperty("URL");
        USER= conf.getProperty("USER");
        PASSWORD= conf.getProperty("PASSWORD");
    }
    public void TryConn(){
        CityId.put("Grosseto", 1);
        CityId.put("Bergamo", 2);
        CityId.put("Napoli", 3);
       try {
         conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Sembra aver funzionato");
    } catch (SQLException e) {
        System.out.println(e);
    }

    }
    public void SendQuery(MeteoDataConverted data){
        try{
            List<String> event =Arrays.asList( data.GetFenomeno().split("\\s+"));
        //Statement stmt = conn.createStatement();
        PreparedStatement pstmt = conn.prepareStatement(INSERT); //preparo  la query sql
        pstmt.setInt(1, data.GetID());  //definisco tutti i parametri della query
        pstmt.setString(2, data.GetData());
        pstmt.setInt(3, data.GetPioggia());
        pstmt.setInt(4, data.GetPressioneAVG());
        pstmt.setInt(5, data.GetPressioneSLM());
        pstmt.setInt(6, data.GetPuntoRugiada());
        pstmt.setInt(7, data.GetRaffica());
        pstmt.setInt(8, data.GetTmax());
        pstmt.setInt(9, data.GetTmedia());
        pstmt.setInt(10, data.GetTmin());
        pstmt.setInt(11, data.Getumidita());
        pstmt.setInt(12, data.GetVentoMax());
        pstmt.setInt(13, data.GetVentoMedia());
        pstmt.setInt(14, data.GetVisibilita());
        pstmt.setInt(21, (int)CityId.get(data.GetLocalita()));
        for (int i=0; i< EventsName.length ; i++) {
            if(event.contains(EventsName[i])){
                pstmt.setBoolean(i+15, true);
            }
            else pstmt.setBoolean(i+15, false);
        }
        pstmt.execute(); //eseguo la query
        }catch(Exception e){ System.out.println(e);}
    }
    public int GetRecordsCount() throws SQLException{
        COUNT="SELECT COUNT(*) as total FROM Weather";
        PreparedStatement pstmt= conn.prepareStatement(COUNT);
        ResultSet res=pstmt.executeQuery();
        while(res.next()){
            return res.getInt("total");
        }
        return 0;
    }
}