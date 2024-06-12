package es.mde.pegasodesktop;

import java.util.ArrayList;


import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
/**
 * Representa un Incidente
 * 
 *
 */

public class Incidente implements Comparable<Incidente> {
 
  @JsonIgnore
  private Long id;
  private String nombre;
  private String descripcion;

  private Collection<Cometido> cometidos = new ArrayList<>();
  
  @JsonValue
  public String urlIncidente() {
    return "https://pegaso-7053cf70389a.herokuapp.com/api/incidentes/" + this.getId();
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
  
//public Collection<Cometido> getCometidos() {
//    return cometidos;
//  }
//
//  public void setCometidos(Collection<Cometido> cometidos) {
//    this.cometidos = cometidos;
//  }
 
  public Incidente() {
    super();
  }

  public Incidente(String nombre, String descripcion) {
    this.nombre = nombre;
    this.descripcion = descripcion;
  }

  @Override
  public int compareTo(Incidente o) {
    return (int) Long.compare(o.getId(),this.getId());
  }
}
