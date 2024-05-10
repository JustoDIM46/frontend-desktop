package pegaso.desktop;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;

import es.lanyu.commons.reflect.ReflectUtils;

public class IncidenteLogisticoDAO extends PegasoAbstractDAO<IncidenteLogistico> {
  
  Field fieldId = ReflectUtils.getCampo(IncidenteLogistico.class, "id", true);
  Field fieldDescripcion = ReflectUtils.getCampo(IncidenteLogistico.class, "descripcion", true);
  Field fieldNombre = ReflectUtils.getCampo(IncidenteLogistico.class, "nombre", true);
  
  public List<IncidenteLogistico> getIncidenteLogisticos() {
    Map<String, String> parametros = new HashMap<>();
    parametros.put("size", "300");
    return getEntidades(IncidenteLogistico.class, "incidenteslogistico", parametros);
}
  
  @Override
  public void completarMapeo(IncidenteLogistico entidad, JsonNode nodo, Long id) {
    super.completarMapeo(entidad, nodo, id);
    try {
      fieldDescripcion.set(entidad, nodo.findValue("descripcion").asText());
      //System.err.println(nodo.findValue("descripcion").asText());
      fieldNombre.set(entidad, nodo.findValue("nombre").asText());
      //System.err.println(nodo.findValue("nombre").asText());
      fieldId.set(entidad, id);
      //System.err.println("id: " + id);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }

  }

}
