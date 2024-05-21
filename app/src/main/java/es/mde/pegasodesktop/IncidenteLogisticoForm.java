package es.mde.pegasodesktop;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.esotericsoftware.tablelayout.swing.Table;
import es.lanyu.participante.Participante;

public class IncidenteLogisticoForm extends JPanel {
  
  private IncidenteLogistico incidenteLogistico;
  private JTextField txtNombre;
  private JTextField txtDescripcion;
  private JTextField txtMaterial;
  private JTextField txtCantidad;

  public IncidenteLogistico getIncidenteLogistico() {
    return incidenteLogistico;
  }

  public void setIncidenteLogistico(IncidenteLogistico incidenteLogistico) {
    this.incidenteLogistico = incidenteLogistico;
  }

  public JTextField getTxtNombre() {
    return txtNombre;
  }

  public void setTxtNombre(JTextField txtNombre) {
    this.txtNombre = txtNombre;
  }

  public JTextField getTxtDescripcion() {
    return txtDescripcion;
  }

  public void setTxtDescripcion(JTextField txtDescripcion) {
    this.txtDescripcion = txtDescripcion;
  }

  public JTextField getTxtMaterial() {
    return txtMaterial;
  }

  public void setTxtMaterial(JTextField txtMaterial) {
    this.txtMaterial = txtMaterial;
  }

  public JTextField getTxtCantidad() {
    return txtCantidad;
  }

  public void setTxtCantidad(JTextField txtCantidad) {
    this.txtCantidad = txtCantidad;
  }

  public IncidenteLogisticoForm(IncidenteLogistico incidentelogistico) {
      this(incidentelogistico, false);
  }

  public IncidenteLogisticoForm(IncidenteLogistico incidentelogistico, boolean editable) {
      Table tabla = new Table();
      //tabla.debug();
      txtNombre = new JTextField();
      txtDescripcion = new JTextField();
      txtMaterial = new JTextField();
      txtCantidad = new JTextField();
      getTxtMaterial().setEnabled(editable);
      getTxtNombre().setEnabled(editable);
      getTxtDescripcion().setEnabled(editable);
      getTxtCantidad().setEnabled(editable);

      cargarIncidente(incidentelogistico);
      add(tabla);
      tabla.addCell(new JLabel("ID: " + incidentelogistico.getId()));
      tabla.row();
      tabla.addCell(new JLabel("Nombre: " ));
      tabla.addCell(new JLabel("Descripcion: "));
      tabla.row();
      tabla.addCell(getTxtNombre()).width(300);
      tabla.addCell(getTxtDescripcion()).width(700);
      tabla.row();
      tabla.addCell(new JLabel("Material: "));
      tabla.addCell(new JLabel("Cantidad: "));
      tabla.row();
      tabla.addCell(getTxtMaterial()).width(300);
      tabla.addCell(getTxtCantidad()).width(50);
      
      getTxtNombre().addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            cambiaIncidenteLogistico();
          }
      });
      
      getTxtDescripcion().addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          cambiaIncidenteLogistico();
        }
      });
      
      getTxtCantidad().addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          cambiaIncidenteLogistico();
        }
      });
      
      getTxtMaterial().addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          cambiaIncidenteLogistico();
        }
      });
      
      getTxtNombre().addCaretListener(e -> cambiaIncidenteLogistico());
      getTxtDescripcion().addCaretListener(e -> cambiaIncidenteLogistico());
      getTxtCantidad().addCaretListener(e -> cambiaIncidenteLogistico());
      getTxtMaterial().addCaretListener(e -> cambiaIncidenteLogistico());

      Font fuente = new Font(Font.SERIF, Font.PLAIN, 20);
      Arrays.asList(tabla.getComponents()).forEach(c -> c.setFont(fuente));
  }

  public void cargarIncidente(IncidenteLogistico incidentelogistico) {
      this.setIncidenteLogistico(incidentelogistico);
      if (!Objects.isNull(incidentelogistico)) {
          getTxtNombre().setText(incidentelogistico.getNombre());
          getTxtDescripcion().setText(incidentelogistico.getDescripcion());
          getTxtCantidad().setText(String.valueOf(incidentelogistico.getCantidad()));
          getTxtMaterial().setText(incidentelogistico.getMaterial());
      }
  }

  public void cambiaIncidenteLogistico() {
      String nombrePropiedad = "incidenteCombate";
      this.getIncidenteLogistico().setNombre(getTxtNombre().getText());
      this.getIncidenteLogistico().setDescripcion(getTxtDescripcion().getText());
      this.getIncidenteLogistico().setMaterial(getTxtMaterial().getText());
      String cantidad = this.getTxtCantidad().getText().isEmpty() ? "0" : getTxtCantidad().getText();
      this.getIncidenteLogistico().setCantidad(Integer.parseInt(cantidad));
      PropertyChangeEvent evento = new PropertyChangeEvent(this, nombrePropiedad, null, this.getIncidenteLogistico());
      Arrays.asList(getPropertyChangeListeners(nombrePropiedad)).forEach(l -> l.propertyChange(evento));
  }
  
}