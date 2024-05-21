package es.mde.pegasodesktop;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Representa un Cometido
 * 
 *
 */

public class Cometido  {

  private Long id;
  private String nombre;
  private String descripcion;
  private float tiempoEstimado;

  private Collection<Incidente> incidentes = new ArrayList<>();
  
  public Cometido() {
  }
  
  public Cometido(String nombre, String descripcion, float tiempoEstimado) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.tiempoEstimado = tiempoEstimado;
  }
  
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getNombre() {
    return nombre;
  }
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  public String getDescripcion() {
    return descripcion;
  }
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }
  public float getTiempoEstimado() {
    return tiempoEstimado;
  }
  public void setTiempoEstimado(float tiempoEstimado) {
    this.tiempoEstimado = tiempoEstimado;
  }
  public Collection<Incidente> getIncidentes() {
    return incidentes;
  }
  public void setIncidentes(Collection<Incidente> incidentes) {
    this.incidentes = incidentes;
  }

}
