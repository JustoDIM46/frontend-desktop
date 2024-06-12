package es.mde.pegasodesktop;

import java.time.LocalDateTime;
import java.util.Collection;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import com.esotericsoftware.tablelayout.swing.Table;
import com.github.lgooddatepicker.components.DateTimePicker;
import es.lanyu.commons.math.MathUtils;
import es.lanyu.commons.tiempo.DatableLocalDateTime;
import es.lanyu.comun.evento.Partido;
import es.lanyu.participante.Participante;

public class IncidenteForm extends Table {
  
    //private JComboBox<Incidente> incidente;
    //private Partido partido;
    private JComboBox<Incidente> cIncidente;
    //private DateTimePicker dtpFechaPartido;
    
    public IncidenteForm(Collection<Incidente> incidentes) {
      JLabel lblCabecera = new JLabel("Incidente a añadir");
      //lblResultado.setSize(20, 0);
      //dtpFechaPartido = new DateTimePicker();
      //dtpFechaPartido.setDateTimePermissive(LocalDateTime.now());
      cIncidente = cargarIncidente(incidentes, false);
      //cbLocal = cargarParticipante(participantes, false);
      cIncidente.setSelectedIndex((int) MathUtils.generarFloatRandom(0, incidentes.size()));
      //cbVisitante = cargarParticipante(participantes, false);
      //cbVisitante.setSelectedIndex((int) MathUtils.generarFloatRandom(0, incidentes.size()));
      montarComponentes(lblCabecera, cIncidente);
  }

    public JComboBox<Incidente> getcIncidente() {
      return cIncidente;
    }

    public void setcIncidente(JComboBox<Incidente> cIncidente) {
      this.cIncidente = cIncidente;
    }

    private static JComboBox<Incidente> cargarIncidente(Collection<Incidente> incidentes,
        boolean alinearDerecha) {
    JComboBox<Incidente> cbIncidente = new JComboBox<>(incidentes.toArray(new Incidente[0]));
    
    if (alinearDerecha) {
        ((JLabel) cbIncidente.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
    }
    return cbIncidente;
    }

    private void montarComponentes(JComponent lblCabecera, JComponent cIncidente) {
    addCell(lblCabecera).colspan(3);
    row();
    addCell(cIncidente).uniformX().center();
    }
    
    public Incidente leerIncidente() {
      Incidente incidente = cIncidente.getItemAt(cIncidente.getSelectedIndex());
      return incidente;
  }
}
