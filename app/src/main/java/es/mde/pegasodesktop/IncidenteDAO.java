package es.mde.pegasodesktop;

import java.lang.reflect.Field;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import es.lanyu.commons.reflect.ReflectUtils;
import es.lanyu.comun.evento.Partido;

public abstract class IncidenteDAO<T> extends PegasoAbstractDAO<T> {
  
	  public IncidenteDAO(Class<T> tipo) {
	  super(tipo);
	  }

}
