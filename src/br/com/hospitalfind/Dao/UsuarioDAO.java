package br.com.hospitalfind.Dao;

import java.io.IOException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import br.com.hospitalfind.Model.Usuario;

public class UsuarioDAO extends DAO {
	
						
	public boolean adiciona(Usuario user) {
	SoapObject	adiciona = new SoapObject(NAMESPACE,ADICIONAR);
	SoapObject usr = new SoapObject(NAMESPACE, "user");
	usr.addProperty("email", user.getEmail());
	usr.addProperty("id",user.getId());
	usr.addProperty("nome", user.getNome());
	usr.addProperty("senha", user.getSenha());
	usr.addProperty("raio_busca", user.getRaio_busca());
	adiciona.addSoapObject(usr);
	
	SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	env.setOutputSoapObject(adiciona);
	env.implicitTypes = true;
	HttpTransportSE http = new HttpTransportSE(IP+USERURL);
	
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
	public Usuario getUsuarioPorEmail(String Email) {
		Usuario userbd = null;
		SoapObject	busca = new SoapObject(NAMESPACE,USUARIO);
		busca.addProperty("Email", Email);
		SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		env.setOutputSoapObject(busca);
		env.implicitTypes = true;
		HttpTransportSE http = new HttpTransportSE(IP+USERURL);
		
		try {
			http.call("urn:"+USUARIO, env);
			SoapObject resposta = (SoapObject) env.getResponse();
			userbd = new Usuario();
			userbd.setId(Integer.parseInt(resposta.getProperty("id").toString()));
			userbd.setEmail(resposta.getProperty("email").toString());
			userbd.setNome(resposta.getProperty("nome").toString());
			userbd.setRaio_busca(Integer.parseInt(resposta.getProperty("raio_busca").toString()));
			userbd.setSenha(resposta.getProperty("senha").toString());
					 						
			} catch (HttpResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
				
	
		return userbd;
	}
	public boolean altera(Usuario user) {
		SoapObject	adiciona = new SoapObject(NAMESPACE,ALTERAR);
		SoapObject usr = new SoapObject(NAMESPACE, "user");
		usr.addProperty("email", user.getEmail());
		usr.addProperty("id",user.getId());
		usr.addProperty("nome", user.getNome());
		usr.addProperty("senha", user.getSenha());
		usr.addProperty("raio_busca", user.getRaio_busca());
		adiciona.addSoapObject(usr);
		
		SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		env.setOutputSoapObject(adiciona);
		env.implicitTypes = true;
		HttpTransportSE http = new HttpTransportSE(IP+USERURL);
		
		try {
			http.call("urn:"+ALTERAR, env);
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
	public Boolean remove(Usuario user) {
		SoapObject	adiciona = new SoapObject(NAMESPACE,REMOVER);
		SoapObject usr = new SoapObject(NAMESPACE, "user");
		usr.addProperty("email", user.getEmail());
		usr.addProperty("id",user.getId());
		usr.addProperty("nome", user.getNome());
		usr.addProperty("senha", user.getSenha());
		adiciona.addSoapObject(usr);
		
		SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		env.setOutputSoapObject(adiciona);
		env.implicitTypes = true;
		HttpTransportSE http = new HttpTransportSE(IP+USERURL);
		
		try {
			http.call("urn:"+REMOVER, env);
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

	




