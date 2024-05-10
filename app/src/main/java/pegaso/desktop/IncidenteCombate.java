package pegaso.desktop;

/**
 * Representa un Incidente de Combate
 * 
 *
 */

public class IncidenteCombate extends Incidente {

  private int bajas;

  public int getBajas() {
    return bajas;
  }

  public void setBajas(int bajas) {
    this.bajas = bajas;
  }

  public IncidenteCombate() {
    super();
  }

  public IncidenteCombate(String nombre, String descripcion, int bajas) {
    super(nombre,descripcion);
    this.bajas = bajas;
  }

  @Override
  public String toString() {
    return "IncidenteCombate -> Id: " + getId() + ", Nombre: "
        + getNombre() + ", Descripcion: " + getDescripcion() + ", " + 
        "bajas: " + this.getBajas();
  }
 
}
