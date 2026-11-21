import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CatalogoCarrerasUASD1 extends JFrame {
    private JComboBox<ComboItem> comboFacultades, comboEscuelas;
    private JComboBox<String> comboOrdenar;
    private JTextField txtBuscar;
    private JCheckBox chkAscendente;
    private JButton btnConsultar, btnTodas, btnLimpiar, btnCerrarSesion;
    private JTable tablaCarreras;
    private DefaultTableModel modeloTabla;
    private JLabel lblLogoFacultad, lblNombreFacultad, lblInfoFacultad, lblEstado;
    private JPanel panelIdentidad;
    private CardLayout cardIdentidad;
    private String usuarioLogueado = "";
    private final String URL_BD = "jdbc:sqlite:uasd_catalogo.db";
    private static final String LOGO_UASD = "/imagenes/logos/escudo_uasd.png";
    private boolean cargandoFacultades = false;

    private static final Color AZUL = new Color(24, 58, 105);
    private static final Color AZUL_CLARO = new Color(235, 242, 250);
    private static final Color BORDE = new Color(210, 218, 230);
    private static final Color TEXTO = new Color(35, 45, 58);
    private static final Color FONDO = new Color(246, 248, 252);
    private static final Color CAMPO = Color.WHITE;
    private static final Color TEXTO_SUAVE = new Color(75, 85, 100);

    public CatalogoCarrerasUASD1() {
        inicializarBaseDatosSQLite();
        if (!mostrarLogin()) System.exit(0);
        configurarVentanaPrincipal();
    }

    private void inicializarBaseDatosSQLite() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("BASE DE DATOS UTILIZADA: "
                    + new java.io.File("uasd_catalogo.db").getAbsolutePath());
            try (Connection conn = DriverManager.getConnection(URL_BD);
                 Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (usuario TEXT PRIMARY KEY, clave TEXT)");
                stmt.execute("CREATE TABLE IF NOT EXISTS facultades (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT UNIQUE, logo TEXT)");
                stmt.execute("CREATE TABLE IF NOT EXISTS escuelas (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT UNIQUE, facultad_id INTEGER, FOREIGN KEY(facultad_id) REFERENCES facultades(id))");
                stmt.execute("CREATE TABLE IF NOT EXISTS carreras (codigo TEXT PRIMARY KEY, nombre TEXT, creditos INTEGER DEFAULT 0, escuela_id INTEGER, FOREIGN KEY(escuela_id) REFERENCES escuelas(id))");
                stmt.executeUpdate("ALTER TABLE carreras ADD COLUMN creditos INTEGER DEFAULT 0");
            }
        } catch (SQLException e) {
            // La columna ya existe en bases anteriores.
            if (!e.getMessage().toLowerCase().contains("duplicate column")) {
                System.out.println("Aviso BD: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró SQLite JDBC: " + e.getMessage());
        }

        try (Connection conn = DriverManager.getConnection(URL_BD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE facultades SET logo = '/imagenes/facultades/facultad_' || id || '.png' WHERE id BETWEEN 1 AND 9");
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO usuarios VALUES ('admin','1234')");
                stmt.execute("INSERT INTO usuarios VALUES ('EMBP','1234')");
                stmt.execute("INSERT INTO usuarios VALUES ('EM','1234')");
                stmt.execute("INSERT INTO usuarios VALUES ('EMOD','1234')");
                stmt.execute("INSERT INTO usuarios VALUES ('JLP','1234')");
            }
        } catch (SQLException e) {
            System.out.println("Error de BD: " + e.getMessage());
        }
    }

    private void configurarVentanaPrincipal() {
        setTitle("UASD | Catálogo Académico - " + usuarioLogueado);
        setSize(1180, 760);
        setMinimumSize(new Dimension(980, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FONDO);
        aplicarEstiloCampos();

        crearMenu();
        add(crearEncabezado(), BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(14, 12));
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(14, 18, 10, 18));
        contenido.add(crearPanelFiltros(), BorderLayout.NORTH);

        cardIdentidad = new CardLayout();
        panelIdentidad = new JPanel(cardIdentidad);
        panelIdentidad.setOpaque(false);
        panelIdentidad.add(crearIdentidadFacultad(), "FACULTAD");
        panelIdentidad.add(crearIdentidadTodas(), "TODAS");
        contenido.add(panelIdentidad, BorderLayout.CENTER);

        JPanel centro = new JPanel(new BorderLayout(0, 10));
        centro.setOpaque(false);
        centro.add(panelIdentidad, BorderLayout.NORTH);
        centro.add(crearTabla(), BorderLayout.CENTER);
        contenido.add(centro, BorderLayout.CENTER);

        add(contenido, BorderLayout.CENTER);
        add(crearBarraEstado(), BorderLayout.SOUTH);

        cargarFacultades();
    }

    private void crearMenu() {
        JMenuBar barra = new JMenuBar();
        barra.setBorder(new MatteBorder(0,0,1,0,BORDE));

        JMenu mCatalogo = new JMenu("Catálogo");
        JMenuItem inicio = new JMenuItem("Inicio");
        JMenuItem todas = new JMenuItem("Todas las carreras");
        JMenuItem facultad = new JMenuItem("Carreras por facultad");
        JMenuItem escuela = new JMenuItem("Carreras por escuela");
        inicio.addActionListener(e -> limpiarFiltros());
        todas.addActionListener(e -> cargarTodasLasCarreras());
        facultad.addActionListener(e -> { comboFacultades.requestFocus(); });
        escuela.addActionListener(e -> { comboEscuelas.requestFocus(); });
        mCatalogo.add(inicio); mCatalogo.addSeparator(); mCatalogo.add(todas);
        mCatalogo.add(facultad); mCatalogo.add(escuela);

        JMenu mFiltros = new JMenu("Filtros y organización");
        JMenuItem aplicar = new JMenuItem("Aplicar filtros");
        JMenuItem limpiar = new JMenuItem("Limpiar filtros");
        JMenuItem ordenarNombre = new JMenuItem("Ordenar por nombre");
        JMenuItem ordenarCreditos = new JMenuItem("Ordenar por créditos");
        aplicar.addActionListener(e -> consultar());
        limpiar.addActionListener(e -> limpiarFiltros());
        ordenarNombre.addActionListener(e -> { comboOrdenar.setSelectedItem("Carrera"); consultar(); });
        ordenarCreditos.addActionListener(e -> { comboOrdenar.setSelectedItem("Créditos"); consultar(); });
        mFiltros.add(aplicar); mFiltros.add(limpiar); mFiltros.addSeparator();
        mFiltros.add(ordenarNombre); mFiltros.add(ordenarCreditos);

        JMenu mVista = new JMenu("Vista");
        JCheckBoxMenuItem compacta = new JCheckBoxMenuItem("Vista compacta");
        compacta.addActionListener(e -> tablaCarreras.setRowHeight(compacta.isSelected() ? 22 : 30));
        mVista.add(compacta);

        JMenu mSesion = new JMenu("Sesión");
        JMenuItem cerrar = new JMenuItem("Cerrar sesión");
        cerrar.addActionListener(e -> cerrarSesion());
        mSesion.add(cerrar);

        JMenu mAyuda = new JMenu("Ayuda");
        JMenuItem acerca = new JMenuItem("Acerca del catálogo");
        acerca.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Catálogo Académico UASD\n\nConsulta organizada de facultades, escuelas y carreras.\n\nCréditos mostrados según los datos registrados en la base de datos.",
                "Acerca del catálogo", JOptionPane.INFORMATION_MESSAGE));
        mAyuda.add(acerca);

        barra.add(mCatalogo); barra.add(mFiltros); barra.add(mVista);
        barra.add(Box.createHorizontalGlue()); barra.add(mAyuda); barra.add(mSesion);
        setJMenuBar(barra);
    }

    private JPanel crearEncabezado() {
        JPanel p = new JPanel(new BorderLayout(14, 0));
        p.setBackground(AZUL);
        p.setBorder(new EmptyBorder(12, 18, 12, 18));

        JLabel logo = new JLabel();
        cargarImagenEnLabel(logo, LOGO_UASD, 52, 52);
        p.add(logo, BorderLayout.WEST);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("CATÁLOGO ACADÉMICO UASD");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 23));
        JLabel subtitulo = new JLabel("Facultades  •  Escuelas  •  Carreras");
        subtitulo.setForeground(new Color(220,230,244));
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        textos.add(titulo); textos.add(Box.createVerticalStrut(3)); textos.add(subtitulo);
        p.add(textos, BorderLayout.CENTER);

        JLabel usuario = new JLabel("Usuario: " + usuarioLogueado);
        usuario.setForeground(Color.WHITE);
        usuario.setBorder(new EmptyBorder(0, 10, 0, 10));
        p.add(usuario, BorderLayout.EAST);
        return p;
    }

    private JPanel crearPanelFiltros() {
        JPanel exterior = panelRedondeado();
        exterior.setLayout(new BorderLayout(10, 10));
        exterior.setBorder(new CompoundBorder(new TitledBorder(
                new LineBorder(BORDE), "Explorar y organizar catálogo",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 13), AZUL),
                new EmptyBorder(10, 12, 12, 12)));

        JPanel fila1 = new JPanel(new GridBagLayout());
        fila1.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(3,5,3,5);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx=0; g.gridy=0; g.weightx=0; fila1.add(etiqueta("Buscar:"), g);
        txtBuscar = new JTextField();
        estilizarCampo(txtBuscar);
        txtBuscar.setToolTipText("Busca por código, carrera, escuela o facultad");
        g.gridx=1; g.weightx=1; fila1.add(txtBuscar, g);

        g.gridx=2; g.weightx=0; fila1.add(etiqueta("Facultad:"), g);
        comboFacultades = new JComboBox<>();
        estilizarCombo(comboFacultades);
        g.gridx=3; g.weightx=.7; fila1.add(comboFacultades, g);

        g.gridx=4; g.weightx=0; fila1.add(etiqueta("Escuela:"), g);
        comboEscuelas = new JComboBox<>();
        estilizarCombo(comboEscuelas);
        g.gridx=5; g.weightx=.7; fila1.add(comboEscuelas, g);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        fila2.setOpaque(false);
        fila2.add(etiqueta("Ordenar por:"));
        comboOrdenar = new JComboBox<>(new String[]{"Facultad", "Escuela", "Carrera", "Código", "Créditos"});
        estilizarCombo(comboOrdenar);
        fila2.add(comboOrdenar);
        chkAscendente = new JCheckBox("Ascendente", true);
        chkAscendente.setOpaque(false);
        fila2.add(chkAscendente);

        btnConsultar = boton("Aplicar filtros", new Color(225,235,247));
        btnTodas = boton("Todas las carreras", new Color(232,238,247));
        btnLimpiar = boton("Limpiar", new Color(237,240,244));
        fila2.add(btnConsultar); fila2.add(btnTodas); fila2.add(btnLimpiar);

        exterior.add(fila1, BorderLayout.CENTER);
        exterior.add(fila2, BorderLayout.SOUTH);

        comboFacultades.addActionListener(e -> {
            if (!cargandoFacultades) cargarEscuelasDeFacultad();
        });
        comboEscuelas.addActionListener(e -> {
            if (!cargandoFacultades && comboEscuelas.getSelectedIndex() > 0) {
                cardIdentidad.show(panelIdentidad, "FACULTAD");
            }
        });
        btnConsultar.addActionListener(e -> consultar());
        btnTodas.addActionListener(e -> cargarTodasLasCarreras());
        btnLimpiar.addActionListener(e -> limpiarFiltros());
        txtBuscar.addActionListener(e -> consultar());
        return exterior;
    }

    private JPanel crearIdentidadFacultad() {
        JPanel p = panelRedondeado();
        p.setLayout(new BorderLayout(15, 5));
        p.setBorder(new EmptyBorder(8, 14, 8, 14));
        lblLogoFacultad = new JLabel();
        lblLogoFacultad.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lblLogoFacultad, BorderLayout.WEST);

        JPanel datos = new JPanel();
        datos.setOpaque(false);
        datos.setLayout(new BoxLayout(datos, BoxLayout.Y_AXIS));
        lblNombreFacultad = new JLabel("Seleccione una facultad");
        lblNombreFacultad.setForeground(AZUL);
        lblNombreFacultad.setFont(new Font("SansSerif", Font.BOLD, 19));
        lblInfoFacultad = new JLabel("Seleccione una facultad para comenzar.");
        lblInfoFacultad.setForeground(TEXTO);
        lblInfoFacultad.setFont(new Font("SansSerif", Font.PLAIN, 13));
        datos.add(lblNombreFacultad); datos.add(Box.createVerticalStrut(5)); datos.add(lblInfoFacultad);
        p.add(datos, BorderLayout.CENTER);
        return p;
    }

    private JPanel crearIdentidadTodas() {
        JPanel p = panelRedondeado();
        p.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(0, 12, 0, 12);
        JLabel izq=new JLabel(), der=new JLabel();
        cargarImagenEnLabel(izq, LOGO_UASD, 50, 50);
        cargarImagenEnLabel(der, LOGO_UASD, 50, 50);
        JLabel texto = new JLabel("TODAS LAS CARRERAS");
        texto.setForeground(AZUL); texto.setFont(new Font("SansSerif", Font.BOLD, 21));
        g.gridx=0; p.add(izq,g); g.gridx=1; p.add(texto,g); g.gridx=2; p.add(der,g);
        return p;
    }

    private JScrollPane crearTabla() {
        modeloTabla = new DefaultTableModel(new Object[]{"Código","Carrera","Créditos","Escuela","Facultad"},0) {
            public boolean isCellEditable(int r,int c){ return false; }
        };
        tablaCarreras = new JTable(modeloTabla);
        tablaCarreras.setRowHeight(30);
        tablaCarreras.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tablaCarreras.setForeground(TEXTO);
        tablaCarreras.setSelectionBackground(new Color(214,228,245));
        tablaCarreras.setSelectionForeground(Color.BLACK);
        tablaCarreras.setGridColor(new Color(232,236,242));
        tablaCarreras.setShowVerticalLines(false);
        tablaCarreras.setIntercellSpacing(new Dimension(0,1));
        tablaCarreras.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tablaCarreras.getTableHeader().setBackground(AZUL);
        tablaCarreras.getTableHeader().setForeground(Color.WHITE);
        tablaCarreras.getTableHeader().setPreferredSize(new Dimension(0,34));

        tablaCarreras.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaCarreras.getColumnModel().getColumn(1).setPreferredWidth(340);
        tablaCarreras.getColumnModel().getColumn(2).setPreferredWidth(85);
        tablaCarreras.getColumnModel().getColumn(3).setPreferredWidth(240);
        tablaCarreras.getColumnModel().getColumn(4).setPreferredWidth(270);

        tablaCarreras.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean focus,int row,int col) {
                Component c=super.getTableCellRendererComponent(t,v,sel,focus,row,col);
                setBorder(new EmptyBorder(0,7,0,7));
                if (!sel) setBackground(row%2==0 ? Color.WHITE : new Color(247,249,252));
                if (col==2) setHorizontalAlignment(SwingConstants.CENTER); else setHorizontalAlignment(SwingConstants.LEFT);
                return c;
            }
        });
        JScrollPane scroll = new JScrollPane(tablaCarreras);
        scroll.getViewport().setBackground(CAMPO);
        scroll.setBorder(new LineBorder(BORDE));
        return scroll;
    }

    private JPanel crearBarraEstado() {
        JPanel p=new JPanel(new BorderLayout());
        p.setBorder(new CompoundBorder(new MatteBorder(1,0,0,0,BORDE), new EmptyBorder(7,18,7,18)));
        p.setBackground(Color.WHITE);
        lblEstado=new JLabel("Listo para consultar el catálogo.");
        lblEstado.setForeground(new Color(90,100,115));
        p.add(lblEstado,BorderLayout.WEST);
        btnCerrarSesion=boton("Cerrar sesión",new Color(250,232,232));
        btnCerrarSesion.addActionListener(e->cerrarSesion());
        p.add(btnCerrarSesion,BorderLayout.EAST);
        return p;
    }

    private void cargarFacultades() {
        cargandoFacultades=true;
        try(Connection conn=DriverManager.getConnection(URL_BD);
            PreparedStatement ps=conn.prepareStatement("SELECT id,nombre,logo FROM facultades ORDER BY nombre");
            ResultSet rs=ps.executeQuery()) {
            comboFacultades.removeAllItems();
            comboFacultades.addItem(new ComboItem(0,"Todas las facultades",null));
            while(rs.next()) comboFacultades.addItem(new ComboItem(rs.getInt(1),rs.getString(2),rs.getString(3)));
            comboFacultades.setSelectedIndex(0);
        }catch(SQLException e){ mostrarError(e); }
        finally { cargandoFacultades=false; cargarEscuelasGenerales(); cargarTodasLasCarreras(); }
    }

    private void cargarEscuelasGenerales() {
        cargandoFacultades=true;
        try(Connection conn=DriverManager.getConnection(URL_BD);
            PreparedStatement ps=conn.prepareStatement("SELECT id,nombre FROM escuelas ORDER BY nombre");
            ResultSet rs=ps.executeQuery()) {
            comboEscuelas.removeAllItems();
            comboEscuelas.addItem(new ComboItem(0,"Todas las escuelas",null));
            while(rs.next()) comboEscuelas.addItem(new ComboItem(rs.getInt(1),rs.getString(2),null));
            comboEscuelas.setSelectedIndex(0);
        }catch(SQLException e){ mostrarError(e); }
        finally { cargandoFacultades=false; }
    }

    private void cargarEscuelasDeFacultad() {
        ComboItem f=(ComboItem)comboFacultades.getSelectedItem();
        if(f==null) return;
        cargandoFacultades=true;
        try(Connection conn=DriverManager.getConnection(URL_BD);
            PreparedStatement ps=conn.prepareStatement("SELECT id,nombre FROM escuelas WHERE facultad_id=? ORDER BY nombre")) {
            ps.setInt(1,f.id);
            try(ResultSet rs=ps.executeQuery()) {
                comboEscuelas.removeAllItems();
                comboEscuelas.addItem(new ComboItem(-1,"Todas las carreras de esta facultad",null));
                while(rs.next()) comboEscuelas.addItem(new ComboItem(rs.getInt(1),rs.getString(2),null));
                comboEscuelas.setSelectedIndex(0);
            }
        }catch(SQLException e){ mostrarError(e); }
        finally { cargandoFacultades=false; }
        if(f.id>0) {
            cardIdentidad.show(panelIdentidad,"FACULTAD");
            cargarImagenEnLabel(lblLogoFacultad,f.logo,115,82);
            lblNombreFacultad.setText(f.nombre);
            lblInfoFacultad.setText("Seleccione “Todas las carreras de esta facultad” o una escuela específica.");
        }
    }

    private void consultar() {
        ComboItem f=(ComboItem)comboFacultades.getSelectedItem();
        ComboItem e=(ComboItem)comboEscuelas.getSelectedItem();
        String buscar=txtBuscar.getText().trim();
        boolean todasFac=(f!=null && f.id>0 && e!=null && e.id==-1);
        boolean todas=(f==null || f.id==0);

        if(todasFac) {
            cardIdentidad.show(panelIdentidad,"FACULTAD");
            cargarImagenEnLabel(lblLogoFacultad,f.logo,115,82);
            lblNombreFacultad.setText(f.nombre);
            lblInfoFacultad.setText("Todas las carreras de esta facultad");
        } else if(todas) {
            cardIdentidad.show(panelIdentidad,"TODAS");
        }

        cargarTabla(buscar, f, e);
    }

    private void cargarTodasLasCarreras() {
        cargandoFacultades=true;
        comboFacultades.setSelectedIndex(0);
        cargarEscuelasGenerales();
        cargandoFacultades=false;
        cardIdentidad.show(panelIdentidad,"TODAS");
        cargarTabla(txtBuscar.getText().trim(), (ComboItem)comboFacultades.getSelectedItem(), (ComboItem)comboEscuelas.getSelectedItem());
    }

    private void cargarTabla(String buscar, ComboItem f, ComboItem e) {
        modeloTabla.setRowCount(0);
        StringBuilder q=new StringBuilder(
            "SELECT c.codigo,c.nombre,c.creditos,e.nombre escuela,f.nombre facultad " +
            "FROM carreras c JOIN escuelas e ON c.escuela_id=e.id JOIN facultades f ON e.facultad_id=f.id WHERE 1=1");
        List<Object> params=new ArrayList<>();

        if(f!=null && f.id>0) { q.append(" AND f.id=?"); params.add(f.id); }
        if(e!=null && e.id>0) { q.append(" AND e.id=?"); params.add(e.id); }
        if(buscar!=null && !buscar.isEmpty()) {
            q.append(" AND (LOWER(c.codigo) LIKE ? OR LOWER(c.nombre) LIKE ? OR LOWER(e.nombre) LIKE ? OR LOWER(f.nombre) LIKE ?)");
            String s="%"+buscar.toLowerCase()+"%";
            params.add(s);params.add(s);params.add(s);params.add(s);
        }

        String[] campos={"f.nombre","e.nombre","c.nombre","c.codigo","c.creditos"};
        int idx=comboOrdenar.getSelectedIndex();
        String orden=campos[Math.max(0,Math.min(idx,4))];
        q.append(" ORDER BY ").append(orden).append(chkAscendente.isSelected()?" ASC":" DESC");
        q.append(", c.codigo ASC");

        try(Connection conn=DriverManager.getConnection(URL_BD);
            PreparedStatement ps=conn.prepareStatement(q.toString())) {
            for(int i=0;i<params.size();i++) ps.setObject(i+1,params.get(i));
            try(ResultSet rs=ps.executeQuery()) {
                int n=0;
                while(rs.next()) {
                    int cr=rs.getInt("creditos");
                    String creditos=rs.wasNull() || cr<=0 ? "No registrado" : String.valueOf(cr);
                    modeloTabla.addRow(new Object[]{rs.getString("codigo"),rs.getString("nombre"),creditos,rs.getString("escuela"),rs.getString("facultad")});
                    n++;
                }
                String contexto=(f!=null&&f.id>0)?f.nombre:"Todas las facultades";
                lblEstado.setText(n+" carrera(s) encontrada(s)  •  "+contexto+"  •  "+(buscar.isEmpty()?"sin búsqueda": "búsqueda: "+buscar));
            }
        }catch(SQLException ex){ mostrarError(ex); }
    }

    private void limpiarFiltros() {
        txtBuscar.setText("");
        comboOrdenar.setSelectedItem("Facultad");
        chkAscendente.setSelected(true);
        cargarTodasLasCarreras();
    }

    private boolean mostrarLogin() {
        final boolean[] ok={false};
        JDialog d=new JDialog((Frame)null,"UASD | Inicio de sesión",true);
        d.setSize(430,280); d.setLocationRelativeTo(null);
        JPanel base=panelRedondeado(); base.setBorder(new EmptyBorder(20,24,20,24));
        base.setLayout(new BorderLayout(15,12));
        JPanel top=new JPanel(new BorderLayout(12,0)); top.setOpaque(false);
        JLabel logo=new JLabel(); cargarImagenEnLabel(logo,LOGO_UASD,60,60); top.add(logo,BorderLayout.WEST);
        JPanel tt=new JPanel();tt.setOpaque(false);tt.setLayout(new BoxLayout(tt,BoxLayout.Y_AXIS));
        JLabel a=new JLabel("Catálogo Académico");a.setFont(new Font("SansSerif",Font.BOLD,19));a.setForeground(AZUL);
        JLabel b=new JLabel("Universidad Autónoma de Santo Domingo");b.setFont(new Font("SansSerif",Font.PLAIN,12));
        tt.add(a);tt.add(Box.createVerticalStrut(4));tt.add(b);top.add(tt);
        base.add(top,BorderLayout.NORTH);

        JPanel form=new JPanel(new GridBagLayout());form.setOpaque(false);
        GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(5,4,5,4);g.fill=GridBagConstraints.HORIZONTAL;
        JTextField u=new JTextField();JPasswordField pass=new JPasswordField();
        g.gridx=0;g.gridy=0;form.add(new JLabel("Usuario:"),g);g.gridx=1;g.weightx=1;form.add(u,g);
        g.gridx=0;g.gridy=1;g.weightx=0;form.add(new JLabel("Contraseña:"),g);g.gridx=1;g.weightx=1;form.add(pass,g);
        base.add(form,BorderLayout.CENTER);
        JPanel botones=new JPanel(new FlowLayout(FlowLayout.RIGHT));botones.setOpaque(false);
        JButton entrar=boton("Ingresar",new Color(225,235,247)), cancelar=boton("Cancelar",new Color(237,240,244));
        botones.add(cancelar);botones.add(entrar);base.add(botones,BorderLayout.SOUTH);
        entrar.addActionListener(e->{if(validarUsuario(u.getText().trim(),new String(pass.getPassword()).trim())){ok[0]=true;usuarioLogueado=u.getText().trim();d.dispose();}else JOptionPane.showMessageDialog(d,"Usuario o contraseña incorrectos.","Acceso denegado",JOptionPane.ERROR_MESSAGE);});
        cancelar.addActionListener(e->d.dispose());
        d.setContentPane(base);d.setVisible(true);return ok[0];
    }

    private boolean validarUsuario(String usuario,String clave) {
        try(Connection c=DriverManager.getConnection(URL_BD);
            PreparedStatement p=c.prepareStatement("SELECT 1 FROM usuarios WHERE usuario=? AND clave=?")) {
            p.setString(1,usuario);p.setString(2,clave);
            try(ResultSet r=p.executeQuery()){return r.next();}
        }catch(SQLException e){mostrarError(e);return false;}
    }

    private void cerrarSesion() {
        if(JOptionPane.showConfirmDialog(this,"¿Desea cerrar la sesión actual?","Cerrar sesión",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            dispose();new CatalogoCarrerasUASD1().setVisible(true);
        }
    }

    private JPanel panelRedondeado(){JPanel p=new JPanel();p.setBackground(Color.WHITE);p.setBorder(new CompoundBorder(new LineBorder(BORDE),new EmptyBorder(8,8,8,8)));return p;}
    private JLabel etiqueta(String s){JLabel l=new JLabel(s);l.setForeground(TEXTO);l.setFont(new Font("SansSerif",Font.BOLD,12));return l;}

    private void aplicarEstiloCampos(){
        UIManager.put("ComboBox.background", CAMPO);
        UIManager.put("ComboBox.foreground", TEXTO);
        UIManager.put("TextField.background", CAMPO);
        UIManager.put("TextField.foreground", TEXTO);
        UIManager.put("TextField.caretForeground", AZUL);
        UIManager.put("CheckBox.foreground", TEXTO);
        UIManager.put("CheckBox.background", FONDO);
        UIManager.put("MenuBar.background", CAMPO);
        UIManager.put("Menu.foreground", TEXTO);
        UIManager.put("MenuItem.foreground", TEXTO);
        UIManager.put("MenuItem.background", CAMPO);
        UIManager.put("Table.background", CAMPO);
        UIManager.put("Table.foreground", TEXTO);
        UIManager.put("Button.foreground", TEXTO);
        UIManager.put("Button.disabledText", TEXTO_SUAVE);
    }

    private void estilizarCampo(JTextField campo){
        campo.setBackground(CAMPO);
        campo.setForeground(TEXTO);
        campo.setCaretColor(AZUL);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campo.setBorder(new CompoundBorder(new LineBorder(BORDE), new EmptyBorder(6,8,6,8)));
    }

    private void estilizarCombo(JComboBox<?> combo){
        combo.setBackground(CAMPO);
        combo.setForeground(TEXTO);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setOpaque(true);
    }
    private JButton boton(String s,Color c){JButton b=new JButton(s);b.setBackground(c);b.setForeground(AZUL);b.setFont(new Font("SansSerif",Font.BOLD,12));b.setFocusPainted(false);b.setBorder(new CompoundBorder(new LineBorder(new Color(190,202,218)),new EmptyBorder(7,13,7,13)));return b;}

    private void cargarImagenEnLabel(JLabel label,String ruta,int ancho,int alto){
        URL url=getClass().getResource(ruta);
        if(url==null){label.setIcon(null);label.setText("UASD");return;}
        ImageIcon orig=new ImageIcon(url);
        int ow=orig.getIconWidth(),oh=orig.getIconHeight();
        if(ow<=0||oh<=0)return;
        double escala=Math.min((double)ancho/ow,(double)alto/oh);
        Image img=orig.getImage().getScaledInstance(Math.max(1,(int)(ow*escala)),Math.max(1,(int)(oh*escala)),Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(img));label.setText("");
        label.setPreferredSize(new Dimension(ancho,alto));
        label.setHorizontalAlignment(SwingConstants.CENTER);label.setVerticalAlignment(SwingConstants.CENTER);
    }

    private void mostrarError(Exception e){JOptionPane.showMessageDialog(this,"Error: "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    private static class ComboItem {
        int id; String nombre,logo;
        ComboItem(int id,String nombre,String logo){this.id=id;this.nombre=nombre;this.logo=logo;}
        public String toString(){return nombre;}
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}catch(Exception ignored){}
            new CatalogoCarrerasUASD1().setVisible(true);
        });
    }
}
