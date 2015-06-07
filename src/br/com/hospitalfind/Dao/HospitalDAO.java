package br.com.hospitalfind.Dao;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import br.com.hospitalfind.Model.PlaceDetail;


public class HospitalDAO extends DAO{
	
	public boolean adiciona(PlaceDetail pd) {
		SoapObject	adiciona = new SoapObject(HOSPITALNS,ADICIONAR);
		SoapObject place = new SoapObject(HOSPITALNS, "PlaceDetail");
		place.addProperty("id_hospital", pd.getPlaceId());
		place.addProperty("nm_hospital", pd.getName());
		place.addProperty("rt_hospital", pd.getRatings());
		place.addProperty("end_hospital", pd.getEnd());
		place.addProperty("lat_hospital", pd.getLat());
		place.addProperty("lng_hospital", pd.getLon());
		adiciona.addSoapObject(place);
		
		SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		env.setOutputSoapObject(adiciona);
		env.implicitTypes = true;
		HttpTransportSE http = new HttpTransportSE(IP+HOSPITALURL);
		
		try {
			http.call("urn:"+ADICIONAR, env);
			SoapPrimitive resposta = (SoapPrimitive) env.getResponse();
			return Boolean.parseBoolean(resposta.toString());
			
		} catch (HttpResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
				}
}
