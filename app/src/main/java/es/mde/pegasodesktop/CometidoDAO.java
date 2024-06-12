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
  
  public void patchCometido(Cometido entidad) {
    System.err.println("patch: " + "cometidos/" + entidad.getId());
    super.patchEntidad(entidad, "cometidos/" + entidad.getId());
  }
  
}
