package pegaso.desktop;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.lanyu.comun.evento.Partido;

public class PegasoAbstractDAO<T> {

	private ObjectMapper mapper;

	private String getApiUrl() {
//		return "https://pruebas-acing-43227cb1dc53.herokuapp.com/api/";
		return App.PROPIEDADES.getProperty("url-api");
	}

	private ObjectMapper getMapper() {
		return mapper;
	}

	public PegasoAbstractDAO() {
		super();
		mapper = new ObjectMapper();
		getMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		//getMapper().addMixIn(Participante.class, MixIns.Participantes.class);
		//getMapper().addMixIn(Incidente.class, MixIns.Participantes.class);
	}

	public List<T> getEntidades(Class<T> tipo, String path, Map<String, String> parametros) {
		List<T> elementos = new ArrayList<T>();
		HttpURLConnection con = null;
		try {
			String queryString = "";
			if (parametros != null) {
				queryString = getParamsString(parametros);
			}
			URL url = new URL(getApiUrl() + path + "?" + queryString);
			con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			BufferedReader buffer = new BufferedReader(new InputStreamReader(con.getInputStream()));
			   
	        JsonNode rootNode = mapper.readTree(buffer);
	        JsonNode nodoElementos = rootNode.findValue(path);
			//JsonNode nodoElementos = getMapper().readTree(buffer).findValue(path);
//			for (JsonNode nodo : nodoElementos) {
//				T entidad = getMapper().readValue(nodo.traverse(), tipo);
//				completarMapeo(entidad, nodo);
//				elementos.add(entidad);
//			}
			
		    if (nodoElementos != null && nodoElementos.isArray()) {
		        for (JsonNode nodo : nodoElementos) {
		            JsonNode linkNode = nodo.path("_links").path("self").path("href");
		            if (!linkNode.isMissingNode()) {
		                String href = linkNode.asText();
		                String[] partes = href.split("/");
		                Long id = Long.parseLong(partes[partes.length-1]);
		                T entidad = mapper.readValue(nodo.traverse(), tipo);
		                completarMapeo(entidad, nodo, id);
		                elementos.add(entidad);
		            } else {
		                System.out.println("Enlace faltante para el cometido");
		            }
		        }
		    }

			buffer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			con.disconnect();
		}
		return elementos;
	}

	public void completarMapeo(T entidad, JsonNode nodo, Long id) {
	};

	public static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}

		String resultString = result.toString();
		return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
	}

}
