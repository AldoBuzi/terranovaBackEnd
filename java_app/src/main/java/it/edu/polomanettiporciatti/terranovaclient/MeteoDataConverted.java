package it.edu.polomanettiporciatti.terranovaclient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.*;
public class MeteoDataConverted {
@JsonFormat(pattern= "yyyy-MM-dd'T'HH:mm:ss.SSSZ", shape =JsonFormat.Shape.STRING) //definisco che tipo di formato la libreria jackson si aspettera'
private String DATA;
@JsonFormat(shape = JsonFormat.Shape.STRING)
private String FENOMENI, LOCALITA;
@JsonFormat(shape = JsonFormat.Shape.NUMBER)
private int ID, PIOGGIA, PRESSIONEMEDIA, PRESSIONESLM, PUNTORUGIADA, RAFFICA, TMAX, TMEDIA, TMIN, UMIDITA, VENTOMAX, VENTOMEDIA, VISIBILITA;
public MeteoDataConverted(){
}
public int GetID(){
    return ID;
}
public String GetData(){
    return DATA;
}
public String GetFenomeno(){
    return FENOMENI;
}
public String GetLocalita(){
    return LOCALITA;
}
public int GetPioggia(){
    return PIOGGIA;
}
public int GetPressioneAVG(){
    return PRESSIONEMEDIA;
}
public int GetPressioneSLM(){
    return PRESSIONESLM;
}
public int GetPuntoRugiada(){
    return PUNTORUGIADA;
}
public int GetRaffica(){
    return RAFFICA;
}
public int GetTmax(){
    return TMAX;
}
public int GetTmedia(){
    return TMEDIA;
}
public int GetTmin(){
    return TMIN;
}
public int Getumidita(){
    return UMIDITA;
}
public int GetVentoMax(){
    return VENTOMAX;
}
public int GetVentoMedia(){
    return VENTOMEDIA;
}
public int GetVisibilita(){
    return VISIBILITA;
}
}