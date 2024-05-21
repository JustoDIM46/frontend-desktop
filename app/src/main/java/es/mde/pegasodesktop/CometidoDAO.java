package es.mde.pegasodesktop;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.JsonNode;

import es.lanyu.commons.reflect.ReflectUtils;

public class CometidoDAO extends PegasoAbstractDAO<Cometido> {
  
//  Field fieldId = ReflectUtils.getCampo(IncidenteCombate.class, "id", true);
//  Field fieldDescripcion = ReflectUtils.getCampo(IncidenteCombate.class, "descripcion", true);
//  Field fieldNombre = ReflectUtils.getCampo(IncidenteCombate.class, "nombre", true);
  
  private IncidenteCombateDAO incidenteCombateDAO;
  private IncidenteLogisticoDAO incidenteLogisticoDAO;
  
  public CometidoDAO() {
    super(Cometido.class);
    incidenteCombateDAO = new IncidenteCombateDAO();
    incidenteLogisticoDAO = new IncidenteLogisticoDAO();
  }

  public List<Cometido> getCometidos() {
    Map<String, String> parametros = new HashMap<>();
    parametros.put("size", "300");
    return getEntidades(Cometido.class, "cometidos", parametros);
}
 
  public List<Cometido> getCometidosConIncidentes() {
    List<Cometido> cometidosConIncidentes = new ArrayList<>();
    this.getCometidos().forEach( c -> {
      List<Incidente> incidentes = new ArrayList<>();
      incidentes.addAll(incidenteCombateDAO.getIncidentesCombateCometidoId(c.getId()));
      incidentes.addAll(incidenteLogisticoDAO.getIncidentesLogisticosCometidoId(c.getId()));
      c.setIncidentes(incidentes);
      cometidosConIncidentes.add(c);
      });
    return cometidosConIncidentes;
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
  
//  public void patchIncidenteCombate(IncidenteCombate entidad) {
//    super.patchEntidad(entidad, "incidentescombate/" + entidad.getId());
//  }
  
}
