package br.gov.ce.sefaz.siconfi.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class ConsultaApiUtil<T> {

	private static Logger logger = LogManager.getLogger(ConsultaApiUtil.class);
	
	private Client client;
	 
	private WebTarget webTarget;

	private String url;
	
	private String path;
	
	private String responseType;
		
	public ConsultaApiUtil(String url, String path, String responseType) {
		super();
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(JacksonJsonProvider.class);
		this.client = ClientBuilder.newClient(clientConfig);
		this.url = url;
		this.path = path;
		this.responseType = responseType;
	}

	public List<T> lerEntidades(APIQueryParamUtil apiQueryParamUtil, Class<T> clazz) {
		Response response = obterResponseAPI(apiQueryParamUtil);
		GenericType<SiconfiResponse<T>> genericType = new GenericType<SiconfiResponse<T>>(new ParameterizedType() {
			
			  public Type[] getActualTypeArguments() {
				    return new Type[]{clazz};
				  }

				  public Type getRawType() {
				    return SiconfiResponse.class;
				  }

				  public Type getOwnerType() {
				    return null;
				  }
		});

		SiconfiResponse<T> siconfiResponse = response.readEntity(genericType);

//		SiconfiResponse<T> siconfiResponse = response
//				.readEntity(new GenericType<SiconfiResponse<T>>() {
//				});
		return siconfiResponse != null ? siconfiResponse.getItems() : new ArrayList<T>();
	}

	private Response obterResponseAPI(APIQueryParamUtil apiQueryParamUtil) {
		
		long ini = System.currentTimeMillis();			
		this.webTarget = this.client.target(url).path(path);
		apiQueryParamUtil.getMapQueryParam().forEach((chave, valor) -> inserirAPIQueryParam(chave, valor));
		Invocation.Builder invocationBuilder =  this.webTarget.request(responseType); 	

		logger.info("Fazendo get na API: " + webTarget.getUri().toString());			
		Response response = invocationBuilder.get();
		mensagemTempoConsultaAPI(ini);
		return response;		
	}

	private void inserirAPIQueryParam(String chave, Object valor) {
		this.webTarget = this.webTarget.queryParam(chave, valor);
	}
	
	private void mensagemTempoConsultaAPI(long ini) {
		long fim = System.currentTimeMillis();			
		logger.debug("Tempo para consultar na API:" + (fim -ini));
	} 
}
