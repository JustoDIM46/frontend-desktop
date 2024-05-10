package pegaso.desktop;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import com.esotericsoftware.tablelayout.swing.Table;
import es.lanyu.commons.config.Propiedades;
import es.lanyu.commons.reflect.ReflectUtils;
import es.lanyu.comun.evento.Partido;
import es.lanyu.ui.iconos.Iconos;
import es.lanyu.ui.swing.ButtonColumn;
import es.lanyu.ui.swing.SimpleJTable;
import es.lanyu.ui.swing.render.CondicionalCustomRenderer;

public class App {
  
  public final static Propiedades PROPIEDADES;

  static {
    PROPIEDADES = new Propiedades("app.properties");
  }

  private static void guardarConfiguracion(JFrame frame) {
    PROPIEDADES.setProperty("ancho", frame.getWidth() + "");
    PROPIEDADES.setProperty("alto", frame.getHeight() + "");
    PROPIEDADES.guardarPropiedades();
  }

    public static void main(String[] args) {

      int ancho = PROPIEDADES.leerPropiedadInt("ancho");
      int alto = PROPIEDADES.leerPropiedadInt("alto");
      JFrame frame = new JFrame("Listado de Incidentes");
      frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          guardarConfiguracion(frame);
          super.windowClosing(e);
        }
      });
     
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Table tabla = new Table();
      frame.getContentPane().add(tabla);
    
      List<IncidenteCombate> incidentesCombate = 
          new IncidenteCombateDAO().getIncidentesCombate();
      List<IncidenteLogistico> incidentesLogistico = 
          new IncidenteLogisticoDAO().getIncidenteLogisticos();
    
      List<Incidente> incidentesTodos = new ArrayList<>();
      //incidentesTodos.sort((u,v) -> (int) Long.compare(u.getId(),v.getId())); 
      incidentesTodos.addAll(incidentesLogistico);
      incidentesTodos.addAll(incidentesCombate);
      //incidentesTodos.sort(Comparator.comparing(Incidente::getId));
      incidentesTodos.sort((u,v) -> (int) Long.compare(u.getId(),v.getId()));
      
      incidentesTodos.forEach(System.out::println);
      
      JPanel panelFormulario = new JPanel();
      panelFormulario.add(new JLabel("Seleccione un incidente haciendo doble click sobre él"));
      tabla.addCell(panelFormulario).expandX();
      tabla.row();

      
      SimpleJTable<Incidente> tablaIncidentes =
          new SimpleJTable<Incidente>(incidentesTodos,
              new String[] { "Id", "Tipo", "Nombre", "Descripcion", "Bajas", "Borrar" },
              p -> p.getId().toString(),
              p -> (p.getClass() == IncidenteCombate.class) ? "Combate" : "Logistica",
              p -> p.getNombre(),
              p -> p.getDescripcion(),
              p -> ( p instanceof IncidenteCombate) ? 
                   ((IncidenteCombate) p).getBajas() : "No aplica",
              p -> "Borrar");
      
      // Pongo un boton de accion en tabla
      Action accion = new AbstractAction() {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            SimpleJTable<Incidente> tabla = (SimpleJTable<Incidente>) e.getSource();
            int opcionIncidenteCombate;
            int opcionIncidenteLogistico;
//            System.out.println("Borrar " + incidente);
            
            if ( tabla.getSeleccionado().getClass() == IncidenteCombate.class ) {
              IncidenteCombate incidente = (IncidenteCombate) tabla.getSeleccionado();
              opcionIncidenteCombate = JOptionPane.
                  showConfirmDialog(frame, new IncidenteCombateForm(incidente), "Borrar partido",
//                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, Iconos.getIcono(Iconos.BORRAR, 30)); 
              if (opcionIncidenteCombate == JOptionPane.OK_OPTION )
              {
                  System.out.println("Borrar " + incidente);
                  incidentesTodos.remove(tablaIncidentes.getSeleccionado());
                  tablaIncidentes.repaint();
              }
            } else {
//              IncidenteLogistico incidente = (IncidenteLogistico) tabla.getSeleccionado();
//              opcionIncidenteLogistico = JOptionPane.showConfirmDialog(frame, new IncidenteLogisticoForm(incidente), "Borrar partido",
////                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
//                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, Iconos.getIcono(Iconos.BORRAR, 30));
//              if (opcionIncidenteCombate == JOptionPane.OK_OPTION || 
//                  opcionIncidenteLogistico == JOptionPane.OK_OPTION)
//              {
//                  System.out.println("Borrar " + incidente);
//                  incidentesTodos.remove(tablaIncidentes.getSeleccionado());
//                  tablaIncidentes.repaint();
//              }
            }

//            if (opcionIncidenteCombate == JOptionPane.OK_OPTION || 
//                opcionIncidenteLogistico == JOptionPane.OK_OPTION)
//            {
//                System.out.println("Borrar " + incidente);
//                incidentesTodos.remove(tablaIncidentes.getSeleccionado());
//                tablaIncidentes.repaint();
//            }
        }
//        @Override
//        public void actionPerformed(ActionEvent e) {
//          SimpleJTable<Incidente> tabla = (SimpleJTable<Incidente>)e.getSource();
//          Incidente incidente = tabla.getSeleccionado();
//          System.out.println("Borrar " + incidente);
//        }
      }; 
      
      new ButtonColumn(tablaIncidentes, accion, 5);
      tablaIncidentes.setColumnasEditables(5);
      
      DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
      DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
      leftRenderer.setHorizontalAlignment(JLabel.LEFT);
      centerRenderer.setHorizontalAlignment(JLabel.RIGHT);
      
      TableColumn columnaId = tablaIncidentes.getColumn("Id");
      columnaId.setMaxWidth(30);
      columnaId.setCellRenderer(centerRenderer);
      
      TableColumn columnaTipo = tablaIncidentes.getColumn("Tipo");
      columnaTipo.setMaxWidth(100);
      columnaTipo.setCellRenderer(leftRenderer);
      
      TableColumn columnaNombre = tablaIncidentes.getColumn("Nombre");
      columnaNombre.setMaxWidth(250);
      columnaNombre.setCellRenderer(leftRenderer);
      
      TableColumn columnaDescripcion = tablaIncidentes.getColumn("Descripcion");
      columnaDescripcion.setMaxWidth(800);
      columnaDescripcion.setCellRenderer(leftRenderer);
      
      TableColumn columnaBajas = tablaIncidentes.getColumn("Bajas");
      columnaBajas.setMaxWidth(200);
      columnaBajas.setCellRenderer(leftRenderer);
      
      TableColumn columnaBorrar = tablaIncidentes.getColumn("Borrar");
      columnaBorrar.setMaxWidth(50);
      columnaBorrar.setCellRenderer(leftRenderer);
      
      tablaIncidentes.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 1) {
                panelFormulario.removeAll();
                panelFormulario.setLayout((LayoutManager) new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
                if ( tablaIncidentes.getSeleccionado().getClass() == IncidenteCombate.class ) {
                IncidenteCombate incidente = (IncidenteCombate) tablaIncidentes.getSeleccionado();
                System.out.println("Este es el incidente a edidtar: " + incidente);
                panelFormulario.add(new IncidenteCombateForm(incidente, true));               
                JButton btnRefrescar = new JButton("Refrescar");
                btnRefrescar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("El nombre actual del incidente es " + incidente);
                    tablaIncidentes.repaint();
                    //participanteVista.cargarParticipante(participante);
                  }
                  });
                panelFormulario.add(btnRefrescar);
                panelFormulario.revalidate();
                             
                }
            }
         }
      });
      
//      DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
//      dtcr.setHorizontalAlignment(JLabel.LEFT);
//      columnaNombre.setCellRenderer(dtcr);
      
        tablaIncidentes.setAnchosPreferidos(200, 800);

//      int anchoNombre = 200;
//      tablaIncidentes.setAnchosPreferidos(anchoNombre, 600);
//      TableColumn columnaTipo = tablaIncidentes.getColumn("Tipo");
//      columnaTipo.setMaxWidth(30);
//      TableColumn columnaNombre = tablaIncidentes.getColumn("Nombre");
//      columnaNombre.setMaxWidth(anchoNombre);
//      TableColumn columnaDescripcion = tablaIncidentes.getColumn("Descripcion");
//      columnaDescripcion.setMaxWidth(300);
//      DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
//      dtcr.setHorizontalAlignment(JLabel.LEFT);
//      columnaNombre.setCellRenderer(dtcr);

//      tablaIncidentes.getColumnModel().
//      getColumn(3).
//      setCellRenderer(
//          new CondicionalCustomRenderer<Incidente>
//      (p -> p.toString(), false, Color.decode("#b9ea96"), 
//          null, null, null));

      JScrollPane scrollPane = new JScrollPane(tablaIncidentes);

//    JScrollPane scrollPane = new JScrollPane(tablaParticipantes);
      tabla.addCell(scrollPane).fillX();

      //tabla.debug();

      frame.setSize(ancho, alto);
      frame.setVisible(true);
      
      
    }
    
}
