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
	
	private static int MAXIMO_TENTATIVAS_CONSULTA_API = 3;
		
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
		if(response == null) {
			logger.info("API retornou resposta vazia");
			return new ArrayList<>();
		} else if(response.getStatus() != Response.Status.OK.getStatusCode()) {
			String corpoResposta = response.readEntity(String.class);
			logger.info("Corpo da Resposta: {}", corpoResposta);
			return new ArrayList<>();			
		}
		
		GenericType<SiconfiResponse<T>> genericType = new GenericType<>(new ParameterizedType() {
			
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
		return siconfiResponse != null ? siconfiResponse.getItems() : new ArrayList<>();
	}

	private Response obterResponseAPI(APIQueryParamUtil apiQueryParamUtil) {
		
		long ini = System.currentTimeMillis();			
		int tentativa = 1;
		
		this.webTarget = this.client.target(url).path(path);
		apiQueryParamUtil.getMapQueryParam().forEach(this::inserirAPIQueryParam);
		Invocation.Builder invocationBuilder =  webTarget.request(responseType); 	

		Response response = null;
		while (tentativa <= MAXIMO_TENTATIVAS_CONSULTA_API) {
			
			logger.info("Fazendo get na API, tentativa {}: {}",  tentativa, webTarget.getUri());			
			response = invocationBuilder.get();
			mensagemTempoConsultaAPI(ini);
			logger.info("C�digo HTTP de retorno: {}", response.getStatus());
			aguardarUmSegundo();			
			
			if(response.getStatus() == Response.Status.OK.getStatusCode()) {
				return response;				
			}
			tentativa++;
		}

		return response;
	}

	private void inserirAPIQueryParam(String chave, Object valor) {
		this.webTarget = this.webTarget.queryParam(chave, valor);
	}
	
	/**
	 * Segundo documenta��o da API, existe o limite de 1 requisi��o por segundo
	 */
	private void aguardarUmSegundo() {
		try {
			Thread.sleep(1100); //Colocando 1,1s por margem de seguran�a
		} catch (InterruptedException e) {
			logger.error(e);
			Thread.currentThread().interrupt();
		}
	}

	private void mensagemTempoConsultaAPI(long ini) {
		long fim = System.currentTimeMillis();			
		logger.debug("Tempo para consultar na API: {}", (fim -ini));
	} 
}
