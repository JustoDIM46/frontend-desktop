package pegaso.desktop;

/**
 * Representa un Incidente Logistico
 * 
 *
 */

public class IncidenteLogistico extends Incidente {

  private String material;
  private int cantidad;
  
  public String getMaterial() {
    return material;
  }
  public void setMaterial(String material) {
    this.material = material;
  }
  public int getCantidad() {
    return cantidad;
  }
  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }
  public IncidenteLogistico() {
    super();
  }
  public IncidenteLogistico(String nombre, String descripcion, String material, int cantidad) {
    super(nombre,descripcion);
    this.material = material;
    this.cantidad = cantidad;
  }
  @Override
  public String toString() {
    return "IncidenteLogistico  -> Id: " + this.getId()  + ", "
        + "Nombre: " + getNombre() + ", "
        + "Material: " + this.getMaterial() + ", "
        + "Cantidad: " + this.getCantidad();
  }
  
  
  
  
}
