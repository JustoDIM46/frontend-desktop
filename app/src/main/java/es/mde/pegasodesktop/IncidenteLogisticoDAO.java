package es.mde.pegasodesktop;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;

import es.lanyu.commons.reflect.ReflectUtils;

public class IncidenteLogisticoDAO extends PegasoAbstractDAO<IncidenteLogistico> {
  
  public IncidenteLogisticoDAO() {
    super(IncidenteLogistico.class);
  }
  
  public List<IncidenteLogistico> getIncidenteLogisticos() {
    Map<String, String> parametros = new HashMap<>();
    parametros.put("size", "300");
    return getEntidades(IncidenteLogistico.class, "incidenteslogistico", parametros);
}
  
  public List<IncidenteLogistico> getIncidentesLogisticos() {
    Map<String, String> parametros = new HashMap<>();
    return getEntidades(IncidenteLogistico.class, "incidenteslogistico", parametros);
}
  
  public List<IncidenteLogistico> getIncidentesLogisticosCometidoId(Long id) {
    Map<String, String> parametros = new HashMap<>();
    parametros.put("size", "300");
    return getEntidades(IncidenteLogistico.class,"cometidos/" + id + "/incidenteslogistico", parametros);
  }
  public void patchIncidenteLogistico(IncidenteLogistico entidad) {
    super.patchEntidad(entidad, "incidenteslogistico/" + entidad.getId());
}
  public void postIncidenteLogistico(IncidenteLogistico entidad) {
    super.postEntidad(entidad, "incidenteslogistico");
  }

}
