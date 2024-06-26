package es.mde.pegasodesktop;

import java.awt.Color;


import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import com.esotericsoftware.tablelayout.swing.Table;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import es.lanyu.commons.config.Propiedades;
import es.lanyu.commons.reflect.ReflectUtils;
import es.lanyu.ui.iconos.Iconos;
import es.lanyu.ui.swing.ButtonColumn;
import es.lanyu.ui.swing.SimpleJTable;
import es.lanyu.ui.swing.render.CondicionalCustomRenderer;

public class App {
  
  public final static Propiedades PROPIEDADES;
  public final static JFrame frame;
  public static List<Incidente> incidentesTodos;
  public static List<Cometido> cometidosTodos;
  private static ObjectMapper mapper;
  private static int ancho;
  private static int alto;

  static {
    PROPIEDADES = new Propiedades("app.properties");
    frame = new JFrame("Listado de Incidentes y Cometidos");
    mapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .enable(Feature.ALLOW_UNQUOTED_FIELD_NAMES)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    incidentesTodos = new ArrayList<>();
  }

  private static void guardarConfiguracion(JFrame frame) {
    PROPIEDADES.setProperty("ancho", frame.getWidth() + "");
    PROPIEDADES.setProperty("alto", frame.getHeight() + "");
    PROPIEDADES.guardarPropiedades();
  }
  
    public static void main(String[] args) throws IOException {

      int ancho = PROPIEDADES.leerPropiedadInt("ancho");
      int alto = PROPIEDADES.leerPropiedadInt("alto");
       
      frame.setLocation(0, 0);
      frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          guardarConfiguracion(frame);
          super.windowClosing(e);
        }
      });
     
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      //Menu
      JMenuBar menuBar = new JMenuBar();
      JMenu menu = new JMenu("Menu");
      menuBar.add(menu);
      JMenuItem table1Item = new JMenuItem("Tabla de Incidentes");
      JMenuItem table3Item = new JMenuItem("Salir");
      JMenuItem table2Item = new JMenuItem("Tabla de Cometidos");

      table1Item.addActionListener(e -> mostrarTablaIncidentes());
      table2Item.addActionListener(e -> mostrarTablaCometidos());
      table3Item.addActionListener(e -> System.exit(0));

      menu.add(table1Item);
      menu.add(table2Item);
      menu.addSeparator();
      menu.add(table3Item);
      menuBar.add(menu);
      frame.setJMenuBar(menuBar);
      
      //Cargamos incidentes
      cargarIncidencias();
      //Cargamos cometidos
      cargarCometidos();
      frame.setSize(ancho, alto);
      frame.setVisible(true);
    }
    
    public static void mostrarTablaIncidentes() {
      
      frame.getContentPane().removeAll();
      frame.getContentPane().revalidate();
      frame.getContentPane().repaint();
      Table tabla = new Table();
      JPanel panelFormulario = new JPanel();
      JPanel panelBotonesFormulario = new JPanel();
      JPanel panelBotonesAnadir = new JPanel();
      
      tabla.addCell(new JLabel("Pulse para anadir nuevo incidente"));
      tabla.row();
      tabla.row();
      panelBotonesAnadir.setLayout((LayoutManager) new BoxLayout(panelBotonesAnadir, BoxLayout.X_AXIS));
      
      // Cargar las imágenes como iconos
//      ImageIcon iconoCombate = 
//          new ImageIcon(ClassLoader.getSystemResource("/combate.png"));  // Ruta relativa a la carpeta resources
//      ImageIcon iconoLogistico = 
//          new ImageIcon(ClassLoader.getSystemResource("/logistico.png"));  // Ruta relativa a la carpeta resources
      
      // Crear los botones con los iconos
      JButton botonAnadirCombate = new JButton("COMBATE");
      botonAnadirCombate.setToolTipText("Añadir Incidente de Combate");
      botonAnadirCombate.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              // Lógica para añadir un incidente de combate
              // Aquí puedes abrir un formulario o dialogo para añadir el incidente de combate
              System.out.println("Añadir Incidente de Combate");
          }
      });

      JButton botonAnadirLogistico = new JButton("LOGISTICO");
      botonAnadirLogistico.setToolTipText("Añadir Incidente Logístico");
      botonAnadirLogistico.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              // Lógica para añadir un incidente logístico
              // Aquí puedes abrir un formulario o dialogo para añadir el incidente logístico
              System.out.println("Añadir Incidente Logístico");
          }
      });

      panelBotonesAnadir.add(botonAnadirCombate);
      panelBotonesAnadir.add(botonAnadirLogistico);
           
      tabla.addCell(panelBotonesAnadir);
      tabla.row();
      panelFormulario.add(new JLabel("Seleccione un incidente haciendo doble click sobre él para editarlo"));
      tabla.addCell(panelFormulario).expandX();
      tabla.row();
      
      ImageIcon iconoBorrar = Iconos.getIcono(Iconos.BORRAR, 14);
      SimpleJTable<Incidente> tablaIncidentes =
          new SimpleJTable<Incidente>(incidentesTodos,
              new String[] { "Id", "Tipo", "Nombre", "Descripcion", "Bajas", "Material", "Cantidad", "Borrar" },
              p -> p.getId().toString(),
              p -> (p.getClass() == IncidenteCombate.class) ? "Combate" : "Logistica",
              p -> p.getNombre(),
              p -> p.getDescripcion(),
              p -> ( p instanceof IncidenteCombate) ? 
                   ((IncidenteCombate) p).getBajas() : "No aplica",
              p -> ( p instanceof IncidenteLogistico) ? 
                   ((IncidenteLogistico) p).getMaterial() : "No aplica",
              p -> ( p instanceof IncidenteLogistico) ? 
                   ((IncidenteLogistico) p).getCantidad() : "No aplica",
              p -> iconoBorrar);
      
      tablaIncidentes.setAnchosPreferidos(ancho/16, 3*ancho/16, 2*ancho/16,3*ancho/16,ancho/16,3*ancho/16, ancho/16, 2*ancho/16);
      
      // Pongo un boton de accion en tabla
      Action accion = new AbstractAction() {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            SimpleJTable<Incidente> tabla = (SimpleJTable<Incidente>) e.getSource();
            int opcionIncidenteCombate;
            int opcionIncidenteLogistico;
            
            if ( tabla.getSeleccionado().getClass() == IncidenteCombate.class ) {
              IncidenteCombate incidente = (IncidenteCombate) tabla.getSeleccionado();
              opcionIncidenteCombate = JOptionPane.
                  showConfirmDialog(frame, new IncidenteCombateForm(incidente), "Borrar incidente",
                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, Iconos.getIcono(Iconos.BORRAR, 30)); 
              if (opcionIncidenteCombate == JOptionPane.OK_OPTION )
              {
                  System.out.println("Borrar " + incidente);
                  incidentesTodos.remove(tablaIncidentes.getSeleccionado());
                  tablaIncidentes.repaint();
              }
            } else {
              IncidenteLogistico incidente = (IncidenteLogistico) tabla.getSeleccionado();
              opcionIncidenteLogistico = JOptionPane.showConfirmDialog(frame, new IncidenteLogisticoForm(incidente), "Borrar partido",
                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, Iconos.getIcono(Iconos.BORRAR, 30));
              if (opcionIncidenteLogistico == JOptionPane.OK_OPTION)
              {
                  System.out.println("Borrar " + incidente);
                  incidentesTodos.remove(tablaIncidentes.getSeleccionado());
                  tablaIncidentes.repaint();
              }
            }
          }
      }; 
      
      new ButtonColumn(tablaIncidentes, accion, 7);
      tablaIncidentes.setColumnasEditables(7);
      
      DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
      DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
      leftRenderer.setHorizontalAlignment(JLabel.LEFT);
      centerRenderer.setHorizontalAlignment(JLabel.RIGHT);
      
      TableColumn columnaId = tablaIncidentes.getColumn("Id");
      columnaId.setMaxWidth(50);
      columnaId.setCellRenderer(centerRenderer);
      
      TableColumn columnaTipo = tablaIncidentes.getColumn("Tipo");
      columnaTipo.setMaxWidth(100);
      columnaTipo.setCellRenderer(leftRenderer);
      
      TableColumn columnaNombre = tablaIncidentes.getColumn("Nombre");
      columnaNombre.setMaxWidth(350);
      columnaNombre.setCellRenderer(leftRenderer);
      
      TableColumn columnaDescripcion = tablaIncidentes.getColumn("Descripcion");
      columnaDescripcion.setMaxWidth(450);
      columnaDescripcion.setCellRenderer(leftRenderer);
      
      TableColumn columnaBajas = tablaIncidentes.getColumn("Bajas");
      columnaBajas.setMaxWidth(150);
      columnaBajas.setCellRenderer(leftRenderer);
      
      TableColumn columnaMaterial = tablaIncidentes.getColumn("Material");
      columnaMaterial.setMaxWidth(300);
      columnaMaterial.setCellRenderer(leftRenderer);
      
      TableColumn columnaCantidad = tablaIncidentes.getColumn("Cantidad");
      columnaCantidad.setMaxWidth(150);
      columnaCantidad.setCellRenderer(leftRenderer);
      
      TableColumn columnaBorrar = tablaIncidentes.getColumn("Borrar");
      columnaBorrar.setMaxWidth(100);
      //columnaBorrar.setCellRenderer(leftRenderer);
      
      tablaIncidentes.addMouseListener(
          new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 1) {
                Incidente incidente;
                incidente = tablaIncidentes.getSeleccionado();
                panelFormulario.removeAll();
                panelBotonesFormulario.removeAll();
                panelFormulario.setLayout((LayoutManager) new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
                
                if ( tablaIncidentes.getSeleccionado().getClass() == IncidenteCombate.class ) {
                incidente = (IncidenteCombate) tablaIncidentes.getSeleccionado();
                try {
                  System.out.println("Este es el incidente a editar: \n" + mapper.writeValueAsString(incidente) );
                  panelFormulario.add(new IncidenteCombateForm((IncidenteCombate)incidente, true)); 
                
                } catch (JsonProcessingException e1) {
                  e1.printStackTrace();
                }
                }
                else
                {
                incidente = (IncidenteLogistico) tablaIncidentes.getSeleccionado();
                try {      
                  System.out.println("Este es el incidente a editar: \n" + mapper.writeValueAsString(incidente) );
                  panelFormulario.add(new IncidenteLogisticoForm((IncidenteLogistico)incidente, true));
                } catch (JsonProcessingException e1) {
                  e1.printStackTrace();
                }                             
                }
      
                JButton btnRefrescar = new JButton("Refrescar");
                //panelFormulario.add(btnRefrescar);
                panelBotonesFormulario.add(btnRefrescar);
                panelFormulario.revalidate();  
                panelBotonesFormulario.revalidate();
                
                btnRefrescar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) { 
                    try {
                      if ( tablaIncidentes.getSeleccionado().getClass() == IncidenteLogistico.class) {
                        IncidenteLogistico incidente = (IncidenteLogistico) tablaIncidentes.getSeleccionado();
                        new IncidenteLogisticoDAO().patchIncidenteLogistico(incidente);
                        System.out.println("El nombre actual del incidente es: \n" +
                        mapper.writeValueAsString((IncidenteLogistico) incidente));
                      } else {
                        IncidenteCombate incidente = (IncidenteCombate) tablaIncidentes.getSeleccionado();
                        new IncidenteCombateDAO().patchIncidenteCombate((IncidenteCombate)incidente);
                        System.out.println("El nombre actual del incidente es: \n" +
                        mapper.writeValueAsString((IncidenteCombate) incidente));
                      }
                   } catch (JsonProcessingException e1) {
                      e1.printStackTrace();
                    }
                    tablaIncidentes.repaint();
                    panelFormulario.removeAll();
                    panelFormulario.revalidate();
                    panelBotonesFormulario.removeAll();
                    panelBotonesFormulario.revalidate();
                    panelFormulario.
                    add(new JLabel("Seleccione un incidente haciendo doble click sobre él para editarlo"));
                  }
                  });           
                panelBotonesFormulario.setLayout((LayoutManager) new BoxLayout(panelBotonesFormulario, BoxLayout.X_AXIS));
                JButton btnCerrar = new JButton("Cerrar");
                panelBotonesFormulario.add(btnCerrar);
                panelBotonesFormulario.revalidate();
                panelFormulario.revalidate();             
                panelFormulario.add(panelBotonesFormulario);
                btnCerrar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Cerramos pantalla");
                    tablaIncidentes.repaint();
                    panelFormulario.removeAll();
                    panelFormulario.revalidate();
                    panelBotonesFormulario.removeAll();
                    panelBotonesFormulario.revalidate(); 
                    panelFormulario.add(new JLabel("Seleccione un incidente haciendo doble click sobre él para editarlo"));
                  
                   };
                });
         }
        }
        });
      //Cerramos si pulsamos doble click sobre el formulario
      
      panelFormulario.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() > 1) {
              panelFormulario.removeAll();
              panelFormulario.setLayout((LayoutManager) new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
              tablaIncidentes.repaint();
              panelFormulario.revalidate();
              panelFormulario.add(new JLabel("Seleccione un incidente haciendo doble click sobre él"));
          }
       }
    });
      
      botonAnadirCombate.addActionListener(l -> {
        IncidenteCombateForm incidenteCombateForm = new IncidenteCombateForm();
        int opcion = JOptionPane.showConfirmDialog(frame, incidenteCombateForm, "Nuevo incidente de combate",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion == JOptionPane.OK_OPTION) {
          IncidenteCombate incidenteCombate = incidenteCombateForm.getIncidenteCombate();
            System.out.println("Creado " + incidenteCombate.getNombre());
            try {
              new IncidenteCombateDAO().postIncidenteCombate(incidenteCombate);
              System.out.println("El nombre actual del incidente es: \n" +
              mapper.writeValueAsString((IncidenteCombate) incidenteCombate));
              cargarIncidencias();
              tablaIncidentes.revalidate();
              tablaIncidentes.repaint();
            } catch (JsonProcessingException e1) {
              e1.printStackTrace();
            }
        }
    });

      botonAnadirLogistico.addActionListener(l -> {
        IncidenteLogisticoForm incidenteLogisticoForm = new IncidenteLogisticoForm();
        int opcion = JOptionPane.showConfirmDialog(frame, incidenteLogisticoForm, "Nuevo incidente logistico",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion == JOptionPane.OK_OPTION) {
          IncidenteLogistico incidenteLogistico = incidenteLogisticoForm.getIncidenteLogistico();
          System.out.println("Creado " + incidenteLogistico.getNombre());
          try {
            new IncidenteLogisticoDAO().postIncidenteLogistico(incidenteLogistico);
            System.out.println("El nombre actual del incidente es: \n" +
            mapper.writeValueAsString((IncidenteLogistico) incidenteLogistico));
            cargarIncidencias();
            tablaIncidentes.revalidate();
            tablaIncidentes.repaint();
          } catch (JsonProcessingException e1) {
            e1.printStackTrace();
          }
        }
    });
     
        frame.getContentPane().add(new JScrollPane(tabla));
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
        JScrollPane scrollPane = new JScrollPane(tablaIncidentes);
        tabla.addCell(scrollPane).fillX();
    }
   
   public static void cargarCometidos() {
     cometidosTodos = new CometidoDAO().getCometidosConIncidentes();
     cometidosTodos.forEach(c -> {
       c.getIncidentes().forEach(System.out::println);
     });    
     
     cometidosTodos.stream()
     .map(p -> {
             String json = "N/D";
             try {
                 json = mapper.writeValueAsString(p);
             } catch (JsonProcessingException e) {
                 e.printStackTrace();
             }
             return json;
         })
     .forEach(System.err::println);
   }
    
   public static void cargarIncidencias() {
     List<IncidenteCombate> incidentesCombate = 
         new IncidenteCombateDAO().getIncidentesCombate();
     List<IncidenteLogistico> incidentesLogistico = 
         new IncidenteLogisticoDAO().getIncidenteLogisticos();
     incidentesTodos.clear();
     incidentesTodos.addAll(incidentesCombate);
     incidentesTodos.addAll(incidentesLogistico);
     incidentesTodos.sort(null);
     incidentesTodos.stream()
     .map(p -> {
             String json = "N/D";
             try {
                 json = mapper.writeValueAsString(p);
             } catch (JsonProcessingException e) {
                 e.printStackTrace();
             }
             return json;
         })
     .forEach(System.out::println);
   }
    
   public static void mostrarTablaCometidos() {
      
      frame.getContentPane().removeAll();
      frame.getContentPane().revalidate();
      frame.getContentPane().repaint();
      Table tabla = new Table();
      JPanel panelFormulario = new JPanel();
      JPanel panelBotonesFormulario = new JPanel();
      panelFormulario.add(new JLabel("Seleccione un cometido haciendo doble click sobre él para editarlo"));
      tabla.addCell(panelFormulario).expandX();
      tabla.row();
      
      ImageIcon iconoAnadir = Iconos.getIcono(Iconos.NUEVO, 14);
      
      SimpleJTable<Cometido> tablaCometidos =
          new SimpleJTable<Cometido>(cometidosTodos,
              new String[] { "Id", "Nombre", "Descripcion", "Tiempo Estimado", "Añadir" },
              p -> p.getId().toString(),
              p -> p.getNombre(),
              p -> p.getDescripcion(),
              p -> p.getTiempoEstimado(),
              p -> iconoAnadir);
      
      tablaCometidos.setAnchosPreferidos(1*ancho/30, 10*ancho/30, 10*ancho/30, 1*ancho/30, 1*ancho/30);
      
      // Pongo un boton de accion en tabla
      Action accion2 = new AbstractAction() {
        
        @Override
        public void actionPerformed(ActionEvent e) {
          SimpleJTable<Cometido> tabla = (SimpleJTable<Cometido>) e.getSource();
          Cometido cometidoSeleccionado = tabla.getSeleccionado();
          IncidenteForm incidenteForm = new IncidenteForm(incidentesTodos);
          int opcion = JOptionPane.showConfirmDialog(frame, incidenteForm, "Nuevo incidente",
                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
          if (opcion == JOptionPane.OK_OPTION) {
              Incidente incidente = incidenteForm.leerIncidente();
              System.out.println("Creado " + incidente.getDescripcion() + "con id: " + incidente.getId());
              cometidoSeleccionado.getIncidentes().add(incidente);
              cometidoSeleccionado.getIncidentes().forEach(System.out::println);
              new CometidoDAO().patchCometido(cometidoSeleccionado);
              cargarCometidos();
              tablaCometidos.revalidate();
              tablaCometidos.repaint();
          }
      }
      };
          
      new ButtonColumn(tablaCometidos, accion2, 4);
      tablaCometidos.setColumnasEditables(4);
      
      DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
      DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
      leftRenderer.setHorizontalAlignment(JLabel.LEFT);
      centerRenderer.setHorizontalAlignment(JLabel.RIGHT);
      
      TableColumn columnaId = tablaCometidos.getColumn("Id");
      columnaId.setMaxWidth(50);
      columnaId.setCellRenderer(centerRenderer);
      
      TableColumn columnaNombre = tablaCometidos.getColumn("Nombre");
      columnaNombre.setMaxWidth(250);
      columnaNombre.setCellRenderer(leftRenderer);
      
      TableColumn columnaDescripcion = tablaCometidos.getColumn("Descripcion");
      columnaDescripcion.setMaxWidth(800);
      columnaDescripcion.setCellRenderer(leftRenderer);
      
      TableColumn columnaBajas = tablaCometidos.getColumn("Tiempo Estimado");
      columnaBajas.setMaxWidth(150);
      columnaBajas.setCellRenderer(leftRenderer);
      
      TableColumn columnaBorrar = tablaCometidos.getColumn("Añadir");
      columnaBorrar.setMaxWidth(50);
      
      tablaCometidos.addMouseListener(
          new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 1) {
                Cometido cometido;
                cometido = tablaCometidos.getSeleccionado();
                panelFormulario.removeAll();
                panelBotonesFormulario.removeAll();
                panelFormulario.setLayout((LayoutManager) new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
                mostrarDialogoConTabla((List<Incidente>) cometido.getIncidentes());
      
                JButton btnRefrescar = new JButton("Refrescar");
                //panelFormulario.add(btnRefrescar);
                panelBotonesFormulario.add(btnRefrescar);
                panelFormulario.revalidate();  
                panelBotonesFormulario.revalidate();
                
                btnRefrescar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) { 
                    tablaCometidos.repaint();
                    panelFormulario.removeAll();
                    panelFormulario.revalidate();
                    panelBotonesFormulario.removeAll();
                    panelBotonesFormulario.revalidate();
                    panelFormulario.
                    add(new JLabel("Seleccione un incidente haciendo doble click sobre él para editarlo"));
                  }
                  });           
                panelBotonesFormulario.setLayout((LayoutManager) new BoxLayout(panelBotonesFormulario, BoxLayout.X_AXIS));
                JButton btnCerrar = new JButton("Cerrar");
                panelBotonesFormulario.add(btnCerrar);
                panelBotonesFormulario.revalidate();
                panelFormulario.revalidate();             
                panelFormulario.add(panelBotonesFormulario);
                btnCerrar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Cerramos pantalla");
                    tablaCometidos.repaint();
                    panelFormulario.removeAll();
                    panelFormulario.revalidate();
                    panelBotonesFormulario.removeAll();
                    panelBotonesFormulario.revalidate(); 
                    panelFormulario.add(new JLabel("Seleccione un incidente haciendo doble click sobre él para editarlo"));
                  
                   };
                });
         }
        }
        });
      
      //Cerramos si pulsamos doble click sobre el formulario 
      panelFormulario.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() > 1) {
              panelFormulario.removeAll();
              panelFormulario.setLayout((LayoutManager) new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
              tablaCometidos.repaint();
              panelFormulario.revalidate();
              panelFormulario.add(new JLabel("Seleccione un incidente haciendo doble click sobre él"));
          }
       }
    });
           
        frame.getContentPane().add(new JScrollPane(tabla));
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
        JScrollPane scrollPane = new JScrollPane(tablaCometidos);
        tabla.addCell(scrollPane).fillX();
    
}
   public static void mostrarDialogoConTabla(List<Incidente> incidentes) {
     int ancho = PROPIEDADES.leerPropiedadInt("ancho");
     int alto = PROPIEDADES.leerPropiedadInt("alto");
     JDialog dialogo = new JDialog(frame, "Tabla de Incidentes", true);
     
     SimpleJTable<Incidente> tablaIncidentesCometido =
         new SimpleJTable<Incidente>(incidentes,
             new String[] { "Id", "Tipo", "Nombre", "Descripcion", "Bajas", "Material", "Cantidad" },
             p -> p.getId().toString(),
             p -> (p.getClass() == IncidenteCombate.class) ? "Combate" : "Logistica",
             p -> p.getNombre(),
             p -> p.getDescripcion(),
             p -> ( p instanceof IncidenteCombate) ? 
                  ((IncidenteCombate) p).getBajas() : "No aplica",
             p -> ( p instanceof IncidenteLogistico) ? 
                  ((IncidenteLogistico) p).getMaterial() : "No aplica",
             p -> ( p instanceof IncidenteLogistico) ? 
                  ((IncidenteLogistico) p).getCantidad() : "No aplica");
     
     JScrollPane scrollPane = new JScrollPane(tablaIncidentesCometido);

     dialogo.getContentPane().removeAll();
     dialogo.getContentPane().validate();
     dialogo.getContentPane().repaint();
     dialogo.add(scrollPane);
     dialogo.setSize(ancho, alto);
     dialogo.setLocationRelativeTo(frame);
     dialogo.setVisible(true);
     
 }
}
