package es.mde.pegasodesktop;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import es.lanyu.commons.reflect.ReflectUtils;

public class PegasoAbstractDAO<T> {

	private ObjectMapper mapper;
	private Class<T> tipo;
    Field fieldId;
    Field fieldDescripcion;
    Field fieldNombre;

	private String getApiUrl() {
		return App.PROPIEDADES.getProperty("url-api");
	}

	private ObjectMapper getMapper() {
		return mapper;
	}

	public PegasoAbstractDAO(Class<T> tipo) {
		super();
		this.tipo = tipo;
		fieldId = ReflectUtils.getCampo(this.tipo, "id", true);
		fieldDescripcion = ReflectUtils.getCampo(this.tipo, "descripcion", true);
		fieldNombre = ReflectUtils.getCampo(this.tipo, "nombre", true);
		System.err.println(tipo);
		mapper = new ObjectMapper();
		getMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
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
			String [] campoRoot = path.split("/");
			String campo = campoRoot[campoRoot.length-1];
	        JsonNode rootNode = mapper.readTree(buffer);
	        JsonNode nodoElementos = rootNode.findValue(campo);
			
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

	public void patchEntidad(T entidad, String path) {
	    HttpURLConnection con = null;
	    try {
	        // Serializar el objeto a JSON utilizando Jackson
	        ObjectMapper objectMapper = new ObjectMapper()
	            .enable(SerializationFeature.INDENT_OUTPUT)
	            .enable(Feature.ALLOW_UNQUOTED_FIELD_NAMES)
	            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
	            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	        
	        String json = objectMapper.writeValueAsString(entidad);
	        // Crear conexión HTTP
	        URL url = new URL(getApiUrl() + path);
	        System.err.println(getApiUrl() + path);
	        System.err.println(json);
	        con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("PUT");

	        con.setDoOutput(true);
	        con.setRequestProperty("Content-Type", "application/json; charset=utf-8");

	        // Enviar JSON como cuerpo de la solicitud PATCH
	        try (OutputStream os = con.getOutputStream()) {
	            byte[] input = json.getBytes(StandardCharsets.UTF_8);
	            os.write(input, 0, input.length);
	        }

	        // Leer respuesta
	        int status = con.getResponseCode();  // Obtener el código de respuesta
	        if (status == HttpURLConnection.HTTP_OK || status == HttpURLConnection.HTTP_NO_CONTENT) {
	            try (BufferedReader br = new BufferedReader(
	                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
	                StringBuilder response = new StringBuilder();
	                String responseLine;
	                while ((responseLine = br.readLine()) != null) {
	                    response.append(responseLine.trim());
	                }
	                   String rawJson = response.toString();
	                    Object json2 = objectMapper.readValue(rawJson, Object.class);
	                    
	                    // Volver a escribir el JSON de manera formateada
	                    String formattedJson = objectMapper.writeValueAsString(json2);
	                System.out.println("Respuesta del API: " + formattedJson);
	            }
	        } else {
	            System.out.println("PATCH request fallo con status: " + status);
	            try (BufferedReader br = new BufferedReader(
	                    new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8))) {
	                StringBuilder errorResponse = new StringBuilder();
	                String responseLine;
	                while ((responseLine = br.readLine()) != null) {
	                    errorResponse.append(responseLine.trim());
	                }
	                String rawJson = errorResponse.toString();
	                Object json2 = objectMapper.readValue(rawJson, Object.class);
	                
	                // Volver a escribir el JSON de manera formateada
	                String formattedJson = objectMapper.writeValueAsString(json2);
	                
	                // Imprimir el JSON formateado
	                //System.out.println(formattedJson);
	                System.out.println("Error response del servidor: " + formattedJson);
	            }
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (con != null) {
	            con.disconnect();
	        }
	    }
	}

	   public void postEntidad(T entidad, String path) {
	        HttpURLConnection con = null;
	        try {
	            // Serializar el objeto a JSON utilizando Jackson
	            ObjectMapper objectMapper = new ObjectMapper()
	                .enable(SerializationFeature.INDENT_OUTPUT)
	                .enable(Feature.ALLOW_UNQUOTED_FIELD_NAMES)
	                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
	                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	            
	            String json = objectMapper.writeValueAsString(entidad);

	            // Crear conexión HTTP
	            URL url = new URL(getApiUrl() + path);
	            System.err.println(getApiUrl() + path);
	            System.err.println(json);
	            con = (HttpURLConnection) url.openConnection();
	            con.setRequestMethod("POST");
	            //con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
	            con.setDoOutput(true);
	            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");

	            // Enviar JSON como cuerpo de la solicitud PATCH
	            try (OutputStream os = con.getOutputStream()) {
	                byte[] input = json.getBytes(StandardCharsets.UTF_8);
	                os.write(input, 0, input.length);
	            }

	            // Leer respuesta
	            int status = con.getResponseCode();
	            if (status == HttpURLConnection.HTTP_OK || status == HttpURLConnection.HTTP_NO_CONTENT
	                || status == HttpURLConnection.HTTP_CREATED) {
	                try (BufferedReader br = new BufferedReader(
	                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
	                    StringBuilder response = new StringBuilder();
	                    String responseLine;
	                    while ((responseLine = br.readLine()) != null) {
	                        response.append(responseLine.trim());
	                    }
	                       String rawJson = response.toString();
	                        Object json2 = objectMapper.readValue(rawJson, Object.class);
	                        
	                        // Volver a escribir el JSON de manera formateada
	                        String formattedJson = objectMapper.writeValueAsString(json2);
	                    System.out.println("Respuesta del API: " + formattedJson);
	                }
	            } else {
	                System.out.println("POST request fallo con status: " + status);
	                try (BufferedReader br = new BufferedReader(
	                        new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8))) {
	                    StringBuilder errorResponse = new StringBuilder();
	                    String responseLine;
	                    while ((responseLine = br.readLine()) != null) {
	                        errorResponse.append(responseLine.trim());
	                    }
	                    String rawJson = errorResponse.toString();
	                    Object json2 = objectMapper.readValue(rawJson, Object.class);
	                    
	                    // Volver a escribir el JSON de manera formateada
	                    String formattedJson = objectMapper.writeValueAsString(json2);
	                    
	                    // Imprimir el JSON formateado
	                    System.out.println("Error response del servidor: " + formattedJson);
	                }
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (con != null) {
	                con.disconnect();
	            }
	        }
	    }
	  public void completarMapeo(T entidad, JsonNode nodo, Long id) {
	    try {
	      fieldDescripcion.set(entidad, nodo.findValue("descripcion").asText());
	      fieldNombre.set(entidad, nodo.findValue("nombre").asText());
	      fieldId.set(entidad, id);
	    } catch (IllegalArgumentException | IllegalAccessException e) {
	      e.printStackTrace();
	    }
	  }

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
