/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectFiles;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.Semaphore;
import javax.swing.JOptionPane;

/**
 *
 * @author Nicolás Briceño y Christian Behrens
 */
public class Interfaz extends javax.swing.JFrame {
    
    private boolean start;
    private double tornillosDaily=30;
    private double tablasDaily=1;//ERRORRRRRRRRRR
    private double patasDaily=2;
    
    
    private int dayDuration;
    public static volatile int daysPast;
    public static volatile int daysToDeliver;
    
    //Contadores de piezas en el almacen
    public static volatile int patasDisp = 0;
    public static volatile int tablasDisp = 0;
    public static volatile int tornillosDisp = 0;
    
    //Contador de escritorios
    public static volatile int pahlsDisp = 0;
    
    //Datos iniciales ProdTornillos de Patas
    private int maxStoragePatas;
    public static volatile int numProdPatas;
    private int maxProdPatas;
    
    //Semaforos Patas
    private Semaphore mutexPatas;
    private Semaphore semPatas;
    private Semaphore semEnsPatas;
    
    //Datos iniciales ProdTornillos de Tornillos
    private int maxStorageTorn;
    public static volatile int numProdTorn;
    private int maxProdTorn;
    
    //Semaforos Tornillos
    private Semaphore mutexTornillos;
    private Semaphore semTornillos;
    private Semaphore semEnsTornillos;
    
    //Datos iniciales ProdTornillos de Tablas
    private int maxStorageTablas;
    public static volatile int numProdTablas;
    private int maxProdTablas;
    
    //Semaforos Tablas
    private Semaphore mutexTablas;
    private Semaphore semTablas;
    private Semaphore semEnsTablas;
    
    //Datos iniciales Ensambladores
    public static volatile int numAssemblers;
    private int maxAssemblers;
    
    //Semaforos enambladores
    private Semaphore mutexEns;
    
    //Arrays de productores
    private ProdPatas arrayPatas[];
    private ProdTablas arrayTablas[];
    private ProdTornillos arrayTornillos[];
    
    
    //Array de ensambladores
    private Ensamblador arrayEns[];
    
    
    //Jefe y gerente
    private Jefe jefe;
    private Gerente gerente;
    
    //Semaforo jefe y gerente
    private Semaphore mutexCounter;
    
    /**
     * Creates new form Interfaz
     */
    public Interfaz() {
        initComponents();
        readJson();
    }
    
    //Función encargada de leer los datos del archivo inicial
    public void readJson() {
        
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();

        try {
            BufferedReader br = new BufferedReader(new FileReader("src/projectFiles/data.json"));
            Object json = gson.fromJson(br, Object.class).toString();
            String strJson = json.toString();
            JsonObject jsonObj = parser.parse(strJson).getAsJsonObject();
            
            //Variables del Json - dias
            JsonElement days = jsonObj.get("days").getAsJsonArray().get(0);
            JsonObject daysObj = parser.parse(days.toString()).getAsJsonObject();
            double numRound1 = Double.parseDouble(daysObj.get("timeSeg").toString());
            dayDuration = (int)numRound1; //luego de la conversión, el 1 estaba como 1.0, se convierte a 1
            double numRound2 = Double.parseDouble(daysObj.get("counter").toString());
            daysToDeliver = (int)numRound2;

            //Variables del Json - productores
            for (int i = 0; i < 3; i++) {
                JsonElement producers = jsonObj.get("producers").getAsJsonArray().get(i);
                JsonObject producersObj = parser.parse(producers.toString()).getAsJsonObject();
                switch (i) {
                    case 0:
                        double a = Double.parseDouble(producersObj.get("maxStorage").toString());
                        maxStoragePatas = (int)a;
                        double b = Double.parseDouble(producersObj.get("numProducers").toString());
                        numProdPatas = (int)b;
                        double c = Double.parseDouble(producersObj.get("maxProducers").toString());
                        maxProdPatas = (int)c;
                        break;
                    case 1:
                        double d = Double.parseDouble(producersObj.get("maxStorage").toString());
                        maxStorageTorn = (int)d;
                        double e = Double.parseDouble(producersObj.get("numProducers").toString());
                        numProdTorn = (int)e;
                        double f = Double.parseDouble(producersObj.get("maxProducers").toString());
                        maxProdTorn = (int)f;
                        break;
                    case 2:
                        double g = Double.parseDouble(producersObj.get("maxStorage").toString());
                        maxStorageTablas = (int)g;
                        double h = Double.parseDouble(producersObj.get("numProducers").toString());
                        numProdTablas = (int)h;
                        double j = Double.parseDouble(producersObj.get("maxProducers").toString());
                        maxProdTablas = (int)j;
                        break;
                }
            }
          
            //Variables del Json - ensambladores
            JsonElement assemblers = jsonObj.get("assemblers").getAsJsonArray().get(0);
            JsonObject assemObj = parser.parse(assemblers.toString()).getAsJsonObject();
            double numRound3 = Double.parseDouble(assemObj.get("numAssemblers").toString());
            numAssemblers = (int)numRound3; //continúa el problema de que los números del json se formatean como doubles (ejem 1.0 en vez de 1)
            double numRound4 = Double.parseDouble(assemObj.get("maxAssemblers").toString());
            maxAssemblers = (int)numRound4;
            
        } catch (FileNotFoundException ex) {
           JOptionPane.showMessageDialog(this, "No se encontró el archivo de texto (json)","ALERTA", JOptionPane.WARNING_MESSAGE);
        }
        
        //Inicializamos valores maximos de productores
        this.prPatas.setText(Integer.toString(maxProdPatas));
        this.prTablas.setText(Integer.toString(maxProdTablas));
        this.prTornillos.setText(Integer.toString(maxProdTorn));
        
        //Inicializamos valores maximos de almacenamiento
        this.storeMaxPatas.setText(Integer.toString(maxStoragePatas));
        this.storeMaxTablas.setText(Integer.toString(maxStorageTablas));
        this.storeMaxTornillos.setText(Integer.toString(maxStorageTorn));
        
        //Inicializamos valor máximo de ensambladores
        this.maxEns.setText(Integer.toString(maxAssemblers));
        
        //Inicializamos cantidad en almacen de cada producto
        this.avPatas.setText(Integer.toString(patasDisp));
        this.avTablas.setText(Integer.toString(tablasDisp));
        this.avTornillos.setText(Integer.toString(tornillosDisp));
        
        //Inicializamos cantidad de productores activos
        this.activePatas.setText(Integer.toString(numProdPatas));
        this.activeTablas.setText(Integer.toString(numProdTablas));
        this.activeTornillos.setText(Integer.toString(numProdTorn));
        
        //Inicializamos cantidad de ensambladores activos
        this.activeEns.setText(Integer.toString(numAssemblers));
        
        //Cantidad de escritorios disponibles
        this.avPahls.setText(Integer.toString(pahlsDisp));
        
        //Estado inicial del jefe y el gerente
        this.bossTxt.setText("ZZZ");
        this.managerTxt.setText("ZZZ");
        
        this.delivery.setText(Integer.toString(daysToDeliver));
        
        //Semaforos de productores
        
        //Tablas
        this.mutexTablas = new Semaphore(1);
        this.semTablas = new Semaphore(Integer.parseInt(this.storeMaxTablas.getText()));
        this.semEnsTablas = new Semaphore(0);
        
        //Tornillos
        this.mutexTornillos = new Semaphore(1);
        this.semTornillos = new Semaphore(Integer.parseInt(this.storeMaxTornillos.getText()));
        this.semEnsTornillos = new Semaphore(0);
        
        //Patas
        this.mutexPatas = new Semaphore(1);
        this.semPatas = new Semaphore(Integer.parseInt(this.storeMaxPatas.getText()));
        this.semEnsPatas = new Semaphore(0);
        
        //Semaforo ensambladores
        
        this.mutexEns = new Semaphore(1);
        
        //Inicializamos los arrays con sus tamaños
        arrayPatas= (new ProdPatas[maxProdPatas]);
        arrayTablas=(new ProdTablas[maxProdTablas]);
        arrayTornillos=(new ProdTornillos[maxProdTorn]);
        arrayEns=(new Ensamblador[maxAssemblers]);
        
        //Inicializar semaforo de jefe y mngr
        
        this.mutexCounter= new Semaphore(1); 
        //Inicializar variables jefe y mngr
        this.jefe = new Jefe(dayDuration, mutexCounter);
        this.gerente = new Gerente(dayDuration, mutexCounter, daysToDeliver);
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        avPahls = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        bossTxt = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        activeTablas = new javax.swing.JTextPane();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        activePatas = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        activeTornillos = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        prTornillos = new javax.swing.JTextPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        prTablas = new javax.swing.JTextPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        prPatas = new javax.swing.JTextPane();
        minusPatas = new javax.swing.JButton();
        plusPatas = new javax.swing.JButton();
        minusTorn = new javax.swing.JButton();
        plusTorn = new javax.swing.JButton();
        minusTab = new javax.swing.JButton();
        plusTab = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        avTablas = new javax.swing.JTextPane();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        avPatas = new javax.swing.JTextPane();
        jScrollPane11 = new javax.swing.JScrollPane();
        avTornillos = new javax.swing.JTextPane();
        jScrollPane12 = new javax.swing.JScrollPane();
        storeMaxTornillos = new javax.swing.JTextPane();
        jScrollPane13 = new javax.swing.JScrollPane();
        storeMaxTablas = new javax.swing.JTextPane();
        jScrollPane14 = new javax.swing.JScrollPane();
        storeMaxPatas = new javax.swing.JTextPane();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        managerTxt = new javax.swing.JTextPane();
        jLabel16 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        activeEns = new javax.swing.JTextPane();
        minusEns = new javax.swing.JButton();
        plusEns = new javax.swing.JButton();
        jScrollPane17 = new javax.swing.JScrollPane();
        maxEns = new javax.swing.JTextPane();
        startButton = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane18 = new javax.swing.JScrollPane();
        delivery = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));

        closeButton.setBackground(new java.awt.Color(255, 51, 51));
        closeButton.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        closeButton.setText("SALIR");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        avPahls.setEditable(false);
        jScrollPane1.setViewportView(avPahls);

        bossTxt.setEditable(false);
        jScrollPane2.setViewportView(bossTxt);

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("GERENTE:");

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("JEFE:");

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ALMACEN");

        jPanel2.setBackground(new java.awt.Color(0, 0, 102));

        jLabel5.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 0));
        jLabel5.setText("Trabajando");

        jLabel7.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 0));
        jLabel7.setText("Cant. máxima");

        activeTablas.setEditable(false);
        jScrollPane3.setViewportView(activeTablas);

        jLabel8.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 0));
        jLabel8.setText("Tornillos");

        jLabel9.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 0));
        jLabel9.setText("Patas");

        jLabel10.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 0));
        jLabel10.setText("Tablas");

        activePatas.setEditable(false);
        jScrollPane4.setViewportView(activePatas);

        activeTornillos.setEditable(false);
        jScrollPane5.setViewportView(activeTornillos);

        prTornillos.setEditable(false);
        jScrollPane6.setViewportView(prTornillos);

        prTablas.setEditable(false);
        jScrollPane7.setViewportView(prTablas);

        prPatas.setEditable(false);
        jScrollPane8.setViewportView(prPatas);

        minusPatas.setBackground(new java.awt.Color(255, 0, 51));
        minusPatas.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        minusPatas.setText("-");
        minusPatas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minusPatasActionPerformed(evt);
            }
        });

        plusPatas.setBackground(new java.awt.Color(0, 204, 51));
        plusPatas.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        plusPatas.setForeground(new java.awt.Color(255, 255, 255));
        plusPatas.setText("+");
        plusPatas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plusPatasActionPerformed(evt);
            }
        });

        minusTorn.setBackground(new java.awt.Color(255, 0, 51));
        minusTorn.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        minusTorn.setText("-");
        minusTorn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minusTornActionPerformed(evt);
            }
        });

        plusTorn.setBackground(new java.awt.Color(0, 204, 51));
        plusTorn.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        plusTorn.setForeground(new java.awt.Color(255, 255, 255));
        plusTorn.setText("+");
        plusTorn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plusTornActionPerformed(evt);
            }
        });

        minusTab.setBackground(new java.awt.Color(255, 0, 51));
        minusTab.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        minusTab.setText("-");
        minusTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minusTabActionPerformed(evt);
            }
        });

        plusTab.setBackground(new java.awt.Color(0, 204, 51));
        plusTab.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        plusTab.setForeground(new java.awt.Color(255, 255, 255));
        plusTab.setText("+");
        plusTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plusTabActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(106, 106, 106)
                        .addComponent(jLabel7))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel10)
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(minusTab)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(plusTab))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(minusTorn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(plusTorn))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel8))
                                .addGap(121, 121, 121)
                                .addComponent(minusPatas)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(plusPatas)))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING))))
                .addGap(23, 23, 23))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(104, 104, 104)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(219, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(minusPatas)
                        .addComponent(plusPatas)
                        .addComponent(jLabel9)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane5)
                                .addGap(44, 44, 44)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(minusTorn)
                                .addComponent(plusTorn))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(minusTab)
                                .addComponent(plusTab)))))
                .addGap(48, 48, 48))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(65, 65, 65)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(203, Short.MAX_VALUE)))
        );

        jLabel4.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("ENSAMBLADORES");

        jPanel4.setBackground(new java.awt.Color(0, 0, 102));

        jLabel6.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 0));
        jLabel6.setText("Disponible");

        jLabel11.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 0));
        jLabel11.setText("Cant. máxima");

        avTablas.setEditable(false);
        jScrollPane9.setViewportView(avTablas);

        jLabel12.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 0));
        jLabel12.setText("Tornillos");

        jLabel13.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 0));
        jLabel13.setText("Patas");

        jLabel14.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 0));
        jLabel14.setText("Tablas");

        avPatas.setEditable(false);
        jScrollPane10.setViewportView(avPatas);

        avTornillos.setEditable(false);
        jScrollPane11.setViewportView(avTornillos);

        storeMaxTornillos.setEditable(false);
        jScrollPane12.setViewportView(storeMaxTornillos);

        storeMaxTablas.setEditable(false);
        jScrollPane13.setViewportView(storeMaxTablas);

        storeMaxPatas.setEditable(false);
        jScrollPane14.setViewportView(storeMaxPatas);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(106, 106, 106)
                .addComponent(jLabel11)
                .addGap(23, 23, 23))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel14)
                        .addGap(30, 30, 30)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel13)
                        .addComponent(jLabel12)))
                .addGap(98, 98, 98)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane14)
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                            .addComponent(jScrollPane12))
                        .addGap(26, 26, 26))))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(104, 104, 104)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(215, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jScrollPane11)
                                .addGap(44, 44, 44)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addGap(48, 48, 48))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(65, 65, 65)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(203, Short.MAX_VALUE)))
        );

        jLabel15.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("ESCRITORIOS PAHL DISPONIBLES: ");

        managerTxt.setEditable(false);
        managerTxt.setBackground(new java.awt.Color(0, 0, 51));
        jScrollPane15.setViewportView(managerTxt);

        jLabel16.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("PRODUCTORES");

        jPanel5.setBackground(new java.awt.Color(0, 0, 102));

        jLabel18.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 0));
        jLabel18.setText("Cant. máxima");

        jLabel17.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 0));
        jLabel17.setText("Trabajando");

        activeEns.setEditable(false);
        jScrollPane16.setViewportView(activeEns);

        minusEns.setBackground(new java.awt.Color(255, 0, 51));
        minusEns.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        minusEns.setText("-");
        minusEns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minusEnsActionPerformed(evt);
            }
        });

        plusEns.setBackground(new java.awt.Color(0, 204, 51));
        plusEns.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        plusEns.setForeground(new java.awt.Color(255, 255, 255));
        plusEns.setText("+");
        plusEns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plusEnsActionPerformed(evt);
            }
        });

        maxEns.setEditable(false);
        jScrollPane17.setViewportView(maxEns);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(minusEns)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(plusEns)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(41, 41, 41))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel18)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(minusEns)
                        .addComponent(plusEns)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        startButton.setBackground(new java.awt.Color(0, 153, 0));
        startButton.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        startButton.setText("INICIAR SIMULACION");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Días para la entrega:");

        delivery.setEditable(false);
        jScrollPane18.setViewportView(delivery);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(162, 162, 162)
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(120, 120, 120)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(closeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(2, 2, 2))
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(67, 67, 67))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addGap(228, 228, 228))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel1))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 956, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void minusPatasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minusPatasActionPerformed
        if(this.arrayPatas != null){
            if(Integer.parseInt(this.activePatas.getText())>0){
                this.activePatas.setText(Integer.toString(Integer.parseInt(this.activePatas.getText()) - 1));
                if (this.start) {
                    this.arrayPatas[Integer.parseInt(activePatas.getText())].setStop(true);
                }
            }
        
        }
    }//GEN-LAST:event_minusPatasActionPerformed

    private void plusPatasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plusPatasActionPerformed
        if(this.arrayPatas != null){
            if(Integer.parseInt(this.activePatas.getText()) < Integer.parseInt(this.prPatas.getText())){
                this.arrayPatas[Integer.parseInt(this.activePatas.getText())] = new ProdPatas(this.dayDuration,  mutexPatas,  semPatas,  semEnsPatas);

                this.activePatas.setText(Integer.toString(Integer.parseInt(this.activePatas.getText()) + 1));
                if (this.start) {
                    this.arrayPatas[Integer.parseInt(activePatas.getText()) - 1].start();
                }
            }
        }
    }//GEN-LAST:event_plusPatasActionPerformed

    private void minusTornActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minusTornActionPerformed
        if(this.arrayTornillos != null){
            if(Integer.parseInt(this.activeTornillos.getText())>0){
                this.activeTornillos.setText(Integer.toString(Integer.parseInt(this.activeTornillos.getText()) - 1));
                if (this.start) {
                    this.arrayTornillos[Integer.parseInt(activeTornillos.getText())].setStop(true);
                }
            }
        
        }
    }//GEN-LAST:event_minusTornActionPerformed

    private void plusTornActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plusTornActionPerformed
        if(this.arrayTornillos != null){
            if(Integer.parseInt(this.activeTornillos.getText()) < Integer.parseInt(this.prTornillos.getText())){
                this.arrayTornillos[Integer.parseInt(this.activeTornillos.getText())] = new ProdTornillos(this.dayDuration,  mutexPatas,  semPatas,  semEnsPatas);

                this.activeTornillos.setText(Integer.toString(Integer.parseInt(this.activeTornillos.getText()) + 1));
                if (this.start) {
                    this.arrayTornillos[Integer.parseInt(activeTornillos.getText()) - 1].start();
                }
            }
        }
    }//GEN-LAST:event_plusTornActionPerformed

    private void minusTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minusTabActionPerformed
        if(this.arrayTablas != null){
            if(Integer.parseInt(this.activeTablas.getText())>0){
                this.activeTablas.setText(Integer.toString(Integer.parseInt(this.activeTablas.getText()) - 1));
                if (this.start) {
                    this.arrayTablas[Integer.parseInt(activeTablas.getText())].setStop(true);
                }
            }
        
        }
    }//GEN-LAST:event_minusTabActionPerformed

    private void plusTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plusTabActionPerformed
        if(this.arrayTablas != null){
            if(Integer.parseInt(this.activeTablas.getText()) < Integer.parseInt(this.prTablas.getText())){
                this.arrayTablas[Integer.parseInt(this.activeTablas.getText())] = new ProdTablas(this.dayDuration,  mutexPatas,  semPatas,  semEnsPatas);

                this.activeTablas.setText(Integer.toString(Integer.parseInt(this.activeTablas.getText()) + 1));
                if (this.start) {
                    this.arrayTablas[Integer.parseInt(activeTablas.getText()) - 1].start();
                }
            }
        }
    }//GEN-LAST:event_plusTabActionPerformed

    private void minusEnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minusEnsActionPerformed
        if(this.arrayEns != null){
            if(Integer.parseInt(this.activeEns.getText())>0){
                this.activeEns.setText(Integer.toString(Integer.parseInt(this.activeEns.getText()) - 1));
                if (this.start) {
                    this.arrayEns[Integer.parseInt(activeEns.getText())].setStop(true);
                }
            }
        
        }
    }//GEN-LAST:event_minusEnsActionPerformed

    private void plusEnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plusEnsActionPerformed
        if(this.arrayEns != null){
            if(Integer.parseInt(this.activeEns.getText()) < Integer.parseInt(this.maxEns.getText())){
                this.arrayEns[Integer.parseInt(this.activeEns.getText())] = new Ensamblador( dayDuration,  mutexEns,  mutexTablas,  semTablas,  semEnsTablas,  mutexPatas,  semPatas,  semEnsPatas,  mutexTornillos,  semTornillos,  semEnsTornillos);

                this.activeEns.setText(Integer.toString(Integer.parseInt(this.activeEns.getText()) + 1));
                if (this.start) {
                    this.arrayEns[Integer.parseInt(activeEns.getText()) - 1].start();
                }
            }
        }
    }//GEN-LAST:event_plusEnsActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        if(!this.start){
            start = true;
            //Llenar arrays de productores
            //Patas
            for (int i = 0; i < Integer.parseInt(this.activePatas.getText()); i++) {
                this.arrayPatas[i] = new ProdPatas(this.dayDuration,  mutexPatas,  semPatas,  semEnsPatas);
                this.arrayPatas[i].start();
            }
            //Tablas
            for (int i = 0; i < Integer.parseInt(this.activeTablas.getText()); i++) {
                this.arrayTablas[i] = new ProdTablas(this.dayDuration, mutexTablas, semTablas, semEnsTablas);
                this.arrayTablas[i].start();
            }
            
            //Tornillos
            for (int i = 0; i < Integer.parseInt(this.activeTornillos.getText()); i++) {
                this.arrayTornillos[i] = new ProdTornillos(this.dayDuration, mutexTornillos, semTornillos, semEnsTornillos);
                this.arrayTornillos[i].start();
            }
            
            //Llenar array de ensambladores
            
            for (int i = 0, j = 0; i < Integer.parseInt(this.activeEns.getText()); i++) {
                this.arrayEns[i] = new Ensamblador( dayDuration,  mutexEns,  mutexTablas,  semTablas,  semEnsTablas,  mutexPatas,  semPatas,  semEnsPatas,  mutexTornillos,  semTornillos,  semEnsTornillos);
                this.arrayEns[i].start();
            }
            
            //Jefe y gerente
            this.jefe = new Jefe(dayDuration, mutexCounter);
            this.jefe.start();
            
            this.gerente = new Gerente(dayDuration, mutexCounter, daysToDeliver);
            this.gerente.start();
        }
        
        
        
    }//GEN-LAST:event_startButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane activeEns;
    private javax.swing.JTextPane activePatas;
    private javax.swing.JTextPane activeTablas;
    private javax.swing.JTextPane activeTornillos;
    public static volatile javax.swing.JTextPane avPahls;
    public static volatile javax.swing.JTextPane avPatas;
    public static volatile javax.swing.JTextPane avTablas;
    public static volatile javax.swing.JTextPane avTornillos;
    public static volatile javax.swing.JTextPane bossTxt;
    private javax.swing.JButton closeButton;
    public static volatile javax.swing.JTextPane delivery;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    public static volatile javax.swing.JTextPane managerTxt;
    private javax.swing.JTextPane maxEns;
    private javax.swing.JButton minusEns;
    private javax.swing.JButton minusPatas;
    private javax.swing.JButton minusTab;
    private javax.swing.JButton minusTorn;
    private javax.swing.JButton plusEns;
    private javax.swing.JButton plusPatas;
    private javax.swing.JButton plusTab;
    private javax.swing.JButton plusTorn;
    private javax.swing.JTextPane prPatas;
    private javax.swing.JTextPane prTablas;
    private javax.swing.JTextPane prTornillos;
    private javax.swing.JButton startButton;
    private javax.swing.JTextPane storeMaxPatas;
    private javax.swing.JTextPane storeMaxTablas;
    private javax.swing.JTextPane storeMaxTornillos;
    // End of variables declaration//GEN-END:variables
}
