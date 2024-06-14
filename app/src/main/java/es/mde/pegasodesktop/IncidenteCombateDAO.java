package es.mde.pegasodesktop;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;

import es.lanyu.commons.reflect.ReflectUtils;

public class IncidenteCombateDAO extends PegasoAbstractDAO<IncidenteCombate> {
  
  public IncidenteCombateDAO() {
    super(IncidenteCombate.class);
  }

  public List<IncidenteCombate> getIncidentesCombate() {
    Map<String, String> parametros = new HashMap<>();
    parametros.put("size", "300");
    return getEntidades(IncidenteCombate.class, "incidentescombate", parametros);
}
  
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
