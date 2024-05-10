package pegaso.desktop;

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

public class IncidenteLogisticoForm extends JPanel {
  
  private Participante participante;
  private JTextField txtNombre;

  public Participante getParticipante() {
      return participante;
  }

  public void setParticipante(Participante participante) {
      this.participante = participante;
  }

  public JTextField getTxtNombre() {
      return txtNombre;
  }

  public void setTxtNombre(JTextField txtNombre) {
      this.txtNombre = txtNombre;
  }

//  public ParticipanteForm(Participante participante) {
//      this(participante, false);
//  }
//
//  public ParticipanteForm(Participante participante, boolean editable) {
//      Table tabla = new Table();
//      tabla.debug();
//      txtNombre = new JTextField();
//      getTxtNombre().setEnabled(editable);
//
//      cargarParticipante(participante);
//      add(tabla);
//      tabla.addCell(new JLabel("ID: " + getParticipante().getIdentificador()));
//      tabla.addCell(getTxtNombre()).width(250);
//      getTxtNombre().addActionListener(new ActionListener() {
//
//          @Override
//          public void actionPerformed(ActionEvent e) {
//              cambiaParticipante();
//          }
//      });
//      getTxtNombre().addCaretListener(e -> cambiaParticipante());
//
//      Font fuente = new Font(Font.SERIF, Font.PLAIN, 30);
//      Arrays.asList(tabla.getComponents()).forEach(c -> c.setFont(fuente));
//
//  }

  public void cargarParticipante(Participante participante) {
      setParticipante(participante);
      if (participante != null) {
          getTxtNombre().setText(getParticipante().getNombre());
      }
  }

  public void cambiaParticipante() {
      String nombrePropiedad = "participante";
      getParticipante().setNombre(getTxtNombre().getText());
      PropertyChangeEvent evento = new PropertyChangeEvent(this, nombrePropiedad, null, getParticipante());
      Arrays.asList(getPropertyChangeListeners(nombrePropiedad)).forEach(l -> l.propertyChange(evento));
  }
  
}