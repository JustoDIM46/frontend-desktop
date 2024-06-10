package es.mde.pegasodesktop;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.esotericsoftware.tablelayout.swing.Table;
import es.lanyu.participante.Participante;

public class IncidenteCombateForm extends JPanel {
  
  private IncidenteCombate incidenteCombate;
  private JTextField txtNombre;
  private JTextField txtDescripcion;
  private JTextField txtBajas;

  public IncidenteCombate getIncidenteCombate() {
    return incidenteCombate;
  }

  public void setIncidenteCombate(IncidenteCombate incidenteCombate) {
    this.incidenteCombate = incidenteCombate;
  }

  public JTextField getTxtDescripcion() {
    return txtDescripcion;
  }

  public void setTxtDescripcion(JTextField txtDescripcion) {
    this.txtDescripcion = txtDescripcion;
  }

  public JTextField getTxtNombre() {
      return txtNombre;
  }

  public void setTxtNombre(JTextField txtNombre) {
      this.txtNombre = txtNombre;
  }

  public JTextField getTxtBajas() {
    return txtBajas;
  }

  public void setTxtBajas(JTextField txtBajas) {
    this.txtBajas = txtBajas;
  }

  public IncidenteCombateForm() {
    this(new IncidenteCombate(),true);   
  }
  
  public IncidenteCombateForm(IncidenteCombate incidenteCombate) {
      this(incidenteCombate, false);
  }

  public IncidenteCombateForm(IncidenteCombate incidenteCombate, boolean editable) {
      Table tabla = new Table();
      //tabla.debug();
      txtNombre = new JTextField();
      txtDescripcion = new JTextField();
      txtBajas = new JTextField();
      getTxtNombre().setEnabled(editable);
      getTxtDescripcion().setEnabled(editable);
      getTxtBajas().setEnabled(editable);

      cargarIncidente(incidenteCombate);
      add(tabla);
      tabla.addCell(new JLabel("ID    "));
      tabla.addCell(new JLabel("Nombre: " ));
      tabla.addCell(new JLabel("Descripcion: "));
      tabla.addCell(new JLabel("Bajas: "));
      tabla.row();
      tabla.addCell(""+incidenteCombate.getId()).width(20);
      tabla.addCell(getTxtNombre()).width(300);
      tabla.addCell(getTxtDescripcion()).width(700);
      tabla.addCell(getTxtBajas()).width(30);
      
      getTxtNombre().addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            cambiaIncidenteCombate();
          }
      });
      
      getTxtDescripcion().addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          cambiaIncidenteCombate();
        }
      });
      
      getTxtBajas().addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          cambiaIncidenteCombate();
        }
      });
      
      getTxtNombre().addCaretListener(e -> cambiaIncidenteCombate());
      getTxtDescripcion().addCaretListener(e -> cambiaIncidenteCombate());
      getTxtBajas().addCaretListener(e -> cambiaIncidenteCombate());

      Font fuente = new Font(Font.SERIF, Font.PLAIN, 20);
      Arrays.asList(tabla.getComponents()).forEach(c -> c.setFont(fuente));
  }

  public void cargarIncidente(IncidenteCombate incidenteCombate) {
      this.setIncidenteCombate(incidenteCombate);
      if (incidenteCombate != null) {
          getTxtNombre().setText(incidenteCombate.getNombre());
          getTxtDescripcion().setText(incidenteCombate.getDescripcion());
          getTxtBajas().setText(String.valueOf(incidenteCombate.getBajas()));
      }
  }

  public void cambiaIncidenteCombate() {
      String nombrePropiedad = "incidenteCombate";
      this.getIncidenteCombate().setNombre(getTxtNombre().getText());
      this.getIncidenteCombate().setDescripcion(getTxtDescripcion().getText());
      String bajas = this.getTxtBajas().getText().isEmpty() ? "0" : getTxtBajas().getText();
      this.getIncidenteCombate().setBajas(Integer.parseInt(bajas));
      PropertyChangeEvent evento = new PropertyChangeEvent(this, nombrePropiedad, null, this.getIncidenteCombate());
      Arrays.asList(getPropertyChangeListeners(nombrePropiedad)).forEach(l -> l.propertyChange(evento));
  }
  
}