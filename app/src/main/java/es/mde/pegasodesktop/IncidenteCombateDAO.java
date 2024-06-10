package es.mde.pegasodesktop;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;

import es.lanyu.commons.reflect.ReflectUtils;

public class IncidenteCombateDAO extends PegasoAbstractDAO<IncidenteCombate> {
  
//  Field fieldId = ReflectUtils.getCampo(IncidenteCombate.class, "id", true);
//  Field fieldDescripcion = ReflectUtils.getCampo(IncidenteCombate.class, "descripcion", true);
//  Field fieldNombre = ReflectUtils.getCampo(IncidenteCombate.class, "nombre", true);
  
  public IncidenteCombateDAO() {
    super(IncidenteCombate.class);
  }

  public List<IncidenteCombate> getIncidentesCombate() {
    Map<String, String> parametros = new HashMap<>();
    parametros.put("size", "300");
    return getEntidades(IncidenteCombate.class, "incidentescombate", parametros);
}
//  @Override
//  public void completarMapeo(IncidenteCombate entidad, JsonNode nodo, Long id) {
//    super.completarMapeo(entidad, nodo, id);
//    try {
//      fieldDescripcion.set(entidad, nodo.findValue("descripcion").asText());
//      //System.err.println(nodo.findValue("descripcion").asText());
//      fieldNombre.set(entidad, nodo.findValue("nombre").asText());
//      //System.err.println(nodo.findValue("nombre").asText());
//      fieldId.set(entidad, id);
//      //System.err.println("Id: " + id);
//    } catch (IllegalArgumentException | IllegalAccessException e) {
//      e.printStackTrace();
//    }
//  }
  
  public void patchIncidenteCombate(IncidenteCombate entidad) {
    super.patchEntidad(entidad, "incidentescombate/" + entidad.getId());
  }
  
  public void postIncidenteCombate(IncidenteCombate entidad) {
    super.postEntidad(entidad, "incidentescombate");
  }
  
  public List<IncidenteCombate> getIncidentesCombateCometidoId(Long id) {
    Map<String, String> parametros = new HashMap<>();
    parametros.put("size", "300");
    return getEntidades(IncidenteCombate.class, "cometidos/" + id + "/incidentescombate", parametros);
  }
  
}
