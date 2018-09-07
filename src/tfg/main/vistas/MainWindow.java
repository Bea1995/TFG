package tfg.main.vistas;

import java.io.IOException;
import java.util.Vector;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

import ac.essex.graphing.plotting.PlotSettings;
import tfg.main.ListaArboles;
import tfg.main.control.ControladorVentana;
import tfg.main.grafica.Graph2;
import tfg.main.grafica.GraphApplication2;
import tfg.main.logica.*;


public class MainWindow extends JFrame implements Programa.Observer{
	
	private static final long serialVersionUID = 1L;
	
	public MainWindow(ControladorVentana ctrl, Observable<Programa.Observer> programa, Vector<ListaArboles> errores) throws IOException{
		setTitle("TFG");
		
		clase=tfg.main.vistas.MainWindow.class;
		this.setIconImage(new ImageIcon(clase.getResource("iconos/app.png")).getImage());
		
		controlador=ctrl;
		this.errores=errores;	
		grafica=null;
		
		programa.addObserver(this);
		
		iniciarBotones();
		initGUI();
		pack();
		setVisible(true);
	}
	
	//Metodo que inicia los valores de la ventana
	private void initGUI() throws IOException{
		setLocation(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(initDatos(), BorderLayout.NORTH);
		add(initResultados(), BorderLayout.CENTER);
	}
	
	//Metodo que inicia los botones de la ventana
	private void iniciarBotones(){
		calcular();
		sacarGrafica();
		salir();
	}
	
	/*Método que inicializa el botón Calcular. Hay una clase interna que implementa el 
	 *listener de movimiento del ratón sobre el botón.*/
	private void calcular(){
		Icon disabledIcon = new ImageIcon(clase.getResource("iconos/calcularGris.png"));
		calcular=new JButton("Compute", new ImageIcon(clase.getResource("iconos/calcular.png")));
		calcular.setDisabledIcon(disabledIcon);
		
		//Su actionListener está en uno de los paneles porque depende de todas las comboBox
		
		calcular.addMouseMotionListener(new MouseMotionListener(){

			public void mouseDragged(MouseEvent e) {}

			public void mouseMoved(MouseEvent e) {
				calcular.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			
		});
	}
	
	/*Método que inicializa el botón Sacar Gráfica. Hay una clase interna que implementa el 
	 *listener de movimiento del ratón sobre el botón.*/
	private void sacarGrafica(){
		Icon disabledIcon = new ImageIcon(clase.getResource("iconos/graficaGris.png"));
		sacarGrafica=new JButton("Show Graph", new ImageIcon(clase.getResource("iconos/grafica.png")));
		sacarGrafica.setDisabledIcon(disabledIcon);
		
		//Su actionListener está en uno de los paneles porque depende de que haya algún resultado
		
		sacarGrafica.addMouseMotionListener(new MouseMotionListener(){

			public void mouseDragged(MouseEvent e) {}

			public void mouseMoved(MouseEvent e) {
				sacarGrafica.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			
		});
	}
	
	/*Método que inicializa el botón Salir. Hay una clase interna que implementa el listener
	 *de dicho botón y otra que implementa el listener de movimiento del ratón.*/
	private void salir(){
		salir = new JButton("Exit", new ImageIcon(clase.getResource("iconos/salir.png")));
		
		salir.addActionListener( new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to leave?", 
						"Exit", JOptionPane.YES_NO_OPTION);
				
				if(n==0) controlador.requestSalir();			
			}	
			
		});
		
		salir.addMouseMotionListener(new MouseMotionListener(){

			public void mouseDragged(MouseEvent e) {}

			public void mouseMoved(MouseEvent e) {
				salir.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			
		});
	}
	
	//Metodo con los elementos del panel de datos
	private JPanel initDatos() throws IOException{
		final JPanel panel = new JPanel();
		final JPanel funcion=new JPanel();
		final JPanel opciones=initOpciones();
		
		funcion.setBorder(BorderFactory.createTitledBorder("Function"));
		funcion.add(funcionComboBox);
		
		panel.setLayout(new BorderLayout());
		panel.add(funcion,BorderLayout.NORTH);
		panel.add(opciones,BorderLayout.CENTER);
		panel.add(calcular,BorderLayout.SOUTH);
		
		return panel;		
	}
	
	//Metodo con los elementos del panel de opciones
	private JPanel initOpciones() throws IOException{
		JPanel panel=new JPanel();
		
		panel.setBorder(BorderFactory.createTitledBorder("Options"));
		panel.setLayout(new FlowLayout());
		
		final JPanel numArbol=new JPanel();
		final JPanel tamagno=new JPanel();
		final JPanel valorMax=new JPanel();
		final JPanel error=new JPanel();
		final JPanel tiempIni=new JPanel();
		final JPanel tiempFin=new JPanel();
		final JPanel saltosG=new JPanel();
		final JPanel troncoP=new JPanel();
		final JPanel contr=new JPanel();
		final JPanel pat=new JPanel();
		final JPanel division=new JPanel();
		final JPanel tolerancia=new JPanel();
		final JPanel mism=new JPanel();
		final JPanel arbol=new JPanel();
		final JPanel arboles=new JPanel();
		
		//Creacion de todos los paneles con los nombres que se muestran al usuario
		numArbol.setBorder(BorderFactory.createTitledBorder("Tree number"));
		tamagno.setBorder(BorderFactory.createTitledBorder("Interval size"));
		valorMax.setBorder(BorderFactory.createTitledBorder("Maximum difference"));
		error.setBorder(BorderFactory.createTitledBorder("Mismatches"));
		tiempIni.setBorder(BorderFactory.createTitledBorder("Initial time"));
		tiempFin.setBorder(BorderFactory.createTitledBorder("Final time"));
		saltosG.setBorder(BorderFactory.createTitledBorder("Jumps (1/0)?"));
		troncoP.setBorder(BorderFactory.createTitledBorder("Min trunk size"));
		contr.setBorder(BorderFactory.createTitledBorder("Min contraction size"));
		pat.setBorder(BorderFactory.createTitledBorder("Pattern"));
		division.setBorder(BorderFactory.createTitledBorder("Division"));
		tolerancia.setBorder(BorderFactory.createTitledBorder("Tolerance"));
		mism.setBorder(BorderFactory.createTitledBorder("Mismatches"));
		arbol.setBorder(BorderFactory.createTitledBorder("Tree list"));
		arboles.setBorder(BorderFactory.createTitledBorder("Tree list"));
		
		funcionComboBox = new JComboBox<EnumFuncion>(EnumFuncion.values());
		
		//Creacion del RadioButton con los nombres de los arboles
		arbolRadioButton=new Vector<JRadioButtonMenuItem>(5); 
		JMenu arbolMenu = new JMenu("Tree");
		ButtonGroup group=new ButtonGroup();
		JMenuBar menuBar=new JMenuBar();;
		
		for(int i=0; i<errores.size(); i++){
			arbolRadioButton.add(new JRadioButtonMenuItem(errores.get(i).getNombre(), false));
			arbolRadioButton.get(i).setActionCommand(errores.get(i).getNombre());
			group.add(arbolRadioButton.get(i));
			arbolMenu.add(arbolRadioButton.get(i));
		}
		
		menuBar.add(arbolMenu);
		menuBar.setPreferredSize(new Dimension(100,20));
		menuBar.setMinimumSize(getPreferredSize());
		arbol.add(menuBar);	
		
		//Creacion de la JCheckBox con los nombres de los arboles
		arbolCheckBox=new Vector<JCheckBoxMenuItem>(5); 
		JMenu arbolMenu2 = new JMenu("Tree");
		JMenuBar menuBar2=new JMenuBar();;
	
		for(int i=0; i<errores.size(); i++){
			arbolCheckBox.add(new JCheckBoxMenuItem(errores.get(i).getNombre(), false));
			arbolCheckBox.get(i).setActionCommand(errores.get(i).getNombre());
			arbolMenu2.add(arbolCheckBox.get(i));
		}
		
		menuBar2.add(arbolMenu2);
		menuBar2.setPreferredSize(new Dimension(100,20));
		menuBar2.setMinimumSize(getPreferredSize());
		arboles.add(menuBar2);	
		
		//Se agnade a cada panel el JTextField que le corresponde (atributos de la ventana)
		arb=new JTextField();
		arb.setPreferredSize(new Dimension(100,20));
		arb.setMinimumSize(getPreferredSize());
		numArbol.add(arb);
		
		tam=new JTextField();
		tam.setPreferredSize(new Dimension(100,20));
		tam.setMinimumSize(getPreferredSize());
		tamagno.add(tam);
		
		max=new JTextField();
		max.setPreferredSize(new Dimension(150,20));
		max.setMinimumSize(getPreferredSize());
		valorMax.add(max);
		
		err=new JTextField();
		err.setPreferredSize(new Dimension(100,20));
		err.setMinimumSize(getPreferredSize());
		error.add(err);
		
		tIni=new HintTextField("dd/mm/aa hh:mm:ss");
		tIni.setPreferredSize(new Dimension(150,20));
		tIni.setMinimumSize(getPreferredSize());
		tiempIni.add(tIni);
		
		tFin=new HintTextField("dd/mm/aa hh:mm:ss");
		tFin.setPreferredSize(new Dimension(150,20));
		tFin.setMinimumSize(getPreferredSize());
		tiempFin.add(tFin);
		
		saltos=new JTextField();
		saltos.setPreferredSize(new Dimension(100,20));
		saltos.setMinimumSize(getPreferredSize());
		saltosG.add(saltos);
		
		tronco=new JTextField();
		tronco.setPreferredSize(new Dimension(100,20));
		tronco.setMinimumSize(getPreferredSize());
		troncoP.add(tronco);
		
		contraccion=new JTextField();
		contraccion.setPreferredSize(new Dimension(120,20));
		contraccion.setMinimumSize(getPreferredSize());
		contr.add(contraccion);
		
		patron=new JTextField();
		patron.setPreferredSize(new Dimension(100,20));
		patron.setMinimumSize(getPreferredSize());
		pat.add(patron);
		
		div=new JTextField();
		div.setPreferredSize(new Dimension(100,20));
		div.setMinimumSize(getPreferredSize());
		division.add(div);
		
		tol=new JTextField();
		tol.setPreferredSize(new Dimension(100,20));
		tol.setMinimumSize(getPreferredSize());
		tolerancia.add(tol);
		
		mismatches=new JTextField();
		mismatches.setPreferredSize(new Dimension(100,20));
		mismatches.setMinimumSize(getPreferredSize());
		mism.add(mismatches);
		
		//Se implementa el action listener para la ComboBox de funciones
		funcionComboBox.addActionListener(new ActionListener(){
			
			//Para cada funcion se agnaden unos paneles determinados
			public void actionPerformed(ActionEvent e) {
				panel.removeAll();
				panel.revalidate();
				panel.repaint();
				
				if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC1)){
					panel.add(arbol);
					panel.add(tiempIni);
					panel.add(tiempFin);
				}
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC2)){
					panel.add(arbol);
					panel.add(tamagno);
					panel.add(valorMax);
					panel.add(error);
					panel.add(tiempIni);
					panel.add(tiempFin);					
				}
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC3)){
					panel.add(numArbol);
					panel.add(arboles);
					panel.add(tamagno);
					panel.add(valorMax);
					panel.add(saltosG);
					panel.add(error);
					panel.add(tiempIni);
					panel.add(tiempFin);
					
				}
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC4)){
					panel.add(arboles);
					panel.add(tamagno);
					panel.add(error);
					panel.add(tiempIni);
					panel.add(tiempFin);
				}
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC5)){
					panel.add(arbol);
					panel.add(troncoP);
					panel.add(tiempIni);
					panel.add(tiempFin);
					
				}
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC6)){
					panel.add(numArbol);
					panel.add(arboles);
					panel.add(tiempIni);
					panel.add(tiempFin);
				}
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC7)){
					panel.add(arbol);
					panel.add(contr);
					panel.add(tiempIni);
					panel.add(tiempFin);
				}
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC8)){
					panel.add(numArbol);
					panel.add(arboles);
					panel.add(tiempIni);
					panel.add(tiempFin);
				}
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC9)){
					panel.add(arbol);
					panel.add(pat);
					panel.add(division);
					panel.add(tolerancia);
					panel.add(mism);
					panel.add(tiempIni);
					panel.add(tiempFin);
				}
				pack();
			}	
			
		});
		
		funcionComboBox.setSelectedItem(EnumFuncion.FC1);
		
		//Se implementa el action listener del boton calcular
		//Depende del valor de la ComboBox de funciones
		calcular.addActionListener( new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {	
				String fecha1=tIni.getText();
				String fecha2=tFin.getText();
			
				String mensajeError="";
				String arbol1=null, pat;
				
				Arbol arbolA=null;
				Arbol arbolB=null;
				String[] trees=new String[25];
				int contadorTrees=0;
				Vector<Arbol> arboles=new Vector<Arbol>(2);
				
				String ventana, valorMax, error, salt, tolerancia, division, mismat;
				String tronc, contr;
				
				Fecha fechaIni=null, fechaFin=null;
				
				Funcion funcion=null;
				Vector<Funcion> funciones=null;
				
				boolean fallo=false;
				
				grafica=null;
				
				if(Utils.fechaValida(fecha1) && Utils.fechaValida(fecha2)){
					fechaIni=new Fecha(fecha1);
					fechaFin=new Fecha(fecha2);
				}
				else {
					fallo=true;
					mensajeError="Some of the dates are not valid\n";
				}
				
				//Funcion para mostrar los datos de un arbol
				if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC1)){
					
					if(group.getSelection()!=null) arbol1=group.getSelection().getActionCommand();
										
					if(arbol1!=null && !fallo) arbolA=controlador.comprobarArbol(arbol1, fechaIni, fechaFin);
					else mensajeError+="There is no tree selected\n";
					
					if(arbolA==null) mensajeError+="There is no tree that is requested in the selected date range\n";
					
					if(arbolA!=null) funcion=new FuncionDatos(arbolA, fechaIni, fechaFin);
					else mensajeError+="Some of the data is not correct and no results can be displayed\n";
					
					controlador.ejecutarFuncion(funcion,mensajeError,1);
				}
				//Funcion para buscar los intervalos homogeneos en un arbol
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC2)){
					int vent=0, erro=0;
					float valorMaximo=0;
					
					if(group.getSelection()!=null) arbol1=group.getSelection().getActionCommand();
					
					if(arbol1!=null && !fallo) arbolA=controlador.comprobarArbol(arbol1, fechaIni, fechaFin);
					else mensajeError+="There is no tree selected\n";
					
					if(arbolA==null) mensajeError+="There is no tree that is requested in the selected date range\n";
					
					ventana=tam.getText();	
					valorMax=max.getText();
				    error=err.getText();
				    
				    if(ventana.equals("INF")) vent=Integer.MAX_VALUE;
				    else if(Utils.isInteger(ventana)) vent=Integer.valueOf(ventana);
				    else{
				    	fallo=true;
				    	mensajeError+="The interval is not an integer greater than 1 or INF\n";
				    }
				    
				    if(!Utils.isFloat(valorMax)){
				    	fallo=true;
				    	mensajeError+="The maximum difference is not a real greater than 0\n";
				    }
				    else valorMaximo=Float.valueOf(valorMax);
				    
				    if(!Utils.isInteger(error)){
				    	fallo=true;
				    	mensajeError+="The dada of Mismatches is not an integer greater than 0 and less than or equal to window/2\n";
				    }
				    else erro=Integer.valueOf(error);	
				    
				    if(!fallo && arbolA!=null && vent>0 && erro>=0 && valorMaximo>0 && erro<=vent/2) 
				    	funcion=new FuncionIntervalo(arbolA,vent,valorMaximo,erro,fechaIni,fechaFin);
				    else mensajeError+="Some of the data is not correct and no results can be displayed\n";
				    
				    controlador.ejecutarFuncion(funcion,mensajeError,1);
				}
				//Funcion para comparar los intervalos homogeneos entre varios arboles
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC3)){
					int cont=0, numArboles=0;
					int vent=0, erro=0;
					float valorMaximo=0;
					boolean jump=false;
					
					for (JCheckBoxMenuItem checkBox : arbolCheckBox) {
						if (checkBox.isSelected()) {
							trees[contadorTrees]=checkBox.getActionCommand();
							contadorTrees++;
						}
					}
						
					arbol1=arb.getText();
						
					if(Utils.isInteger(arbol1)) numArboles=Integer.valueOf(arbol1);
					else{
						fallo=true;
						mensajeError+="The number of trees is not an integer greater than 0\n";
					}
					
					if(!fallo && (numArboles<=0 || numArboles!=contadorTrees)){
						fallo=true;
						mensajeError+="The same number of trees has not been selected";
					}
						
					if(!fallo){
						for(int i=0; i<contadorTrees; i++){
							Arbol arbolAux=controlador.comprobarArbol(trees[i],fechaIni,fechaFin);
							if(arbolAux!=null) {
								arboles.addElement(arbolAux);
								cont++;
							}	
						}
						
						if(!fallo && numArboles!=cont){
							fallo=true;
							mensajeError+="There is no tree in the selected date range\n";
						}
						
						ventana=tam.getText();	
					    error=err.getText();
					    valorMax=max.getText();
					    salt=saltos.getText();
					    
					    if(ventana.equals("INF")){
					    	if(numArboles==2) vent=Integer.MAX_VALUE;
					    	else vent=1;
					    }
					    else if(Utils.isInteger(ventana)) vent=Integer.valueOf(ventana);
					    else{
					    	fallo=true;
					    	mensajeError+="The interval is not an integer greater than 1 or INF\n";
					    }
					    
					    if(!Utils.isFloat(valorMax)){
					    	fallo=true;
					    	mensajeError+="The maximum difference is not a real greater than 0\n";
					    }
					    else valorMaximo=Float.valueOf(valorMax);
					    
					    if(!Utils.isInteger(error)){
					    	fallo=true;
					    	mensajeError+="The dada of Mismatches is not an integer greater than 0 and less than or equal to window/2\n";
					    }
					    else erro=Integer.valueOf(error);	
						
					    if(salt.equals("0")) jump=false;
					    else if(salt.equals("1")) jump=true;
					    else{
					    	fallo=true;
					    	mensajeError+="The jump value is different from 0/1\n";
					    } 
					    
					    if(numArboles>2 && ventana.equals("INF") && erro>0){
					    	fallo=true;
					    	mensajeError+="It is not possible to calculate maximum intervals with errors for more than two trees\n";
					    }
					    
						if(!fallo && vent>0 && erro>=0 && valorMaximo>0 && erro<=vent/2){
							result.setText(null);
							funciones=new Vector<Funcion>(5);
							for(int i=0; i<arboles.size(); i++){
								for(int j=i+1; j<arboles.size(); j++){
									funcion=new FuncionComparar(arboles.get(i), arboles.get(j),vent,valorMaximo,jump,erro,fechaIni,fechaFin);
									funciones.addElement(funcion);
									controlador.ejecutarFuncion(funcion, mensajeError,0);
								}
							}
							if(numArboles>2){
								if(!ventana.equalsIgnoreCase("INF")){
									interseccionFunciones(funciones);
									escribirResultado(funciones,vent,arboles);
									onResultado(funciones.firstElement(), controlador.getIni(),2);
								}
								else{
									interseccionFunciones(funciones);
									int max=maximoFunciones(funciones);
									escribirResultado(funciones,max,arboles);
									onResultado(funciones.firstElement(), controlador.getIni(),2);
								}
							}
						}
						else {
							mensajeError+="Some of the data is not correct and no results can be displayed\n";
							controlador.ejecutarFuncion(funcion,mensajeError,1);
						}
					}
					else controlador.ejecutarFuncion(funcion,mensajeError,1);
					
				}
				//Funcion para buscar los intervalos discrepantes entre dos arboles
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC4)){
					int cont=0;
					int vent=0, erro=0;
					
					for (JCheckBoxMenuItem checkBox : arbolCheckBox) {
						if (checkBox.isSelected()) {
							trees[contadorTrees]=checkBox.getActionCommand();
							contadorTrees++;
						}
					}
					
					if(!fallo && contadorTrees!=2){
						fallo=true;
						mensajeError+="A different number of 2 trees has been selected";
					}
						
					if(!fallo){
						for(int i=0; i<contadorTrees; i++){
							Arbol arbolAux=controlador.comprobarArbol(trees[i],fechaIni,fechaFin);
							if(arbolAux!=null) {
								arboles.addElement(arbolAux);
								cont++;
							}	
						}
						
						if(!fallo && cont!=2){
							fallo=true;
							mensajeError+="There is no tree in the selected date range\n";
						}
						
						ventana=tam.getText();
					    error=err.getText();
					    
					    if(ventana.equals("INF")) vent=Integer.MAX_VALUE;
					    else if(Utils.isInteger(ventana)) vent=Integer.valueOf(ventana);
					    else{
					    	fallo=true;
					    	mensajeError+="The interval is not an integer greater than 1 or INF\n";
					    }
					    
					    if(!Utils.isInteger(error)){
					    	fallo=true;
					    	mensajeError+="The dada of Mismatches is not an integer greater than 0 and less than or equal to window/2\n";
					    }
					    else erro=Integer.valueOf(error);
						
						if(!fallo && vent>0 && erro>=0 && erro<=vent/2){
							result.setText(null);
							funcion=new FuncionDiscrepancia(arboles.get(0), arboles.get(1),vent,erro,fechaIni,fechaFin);
							arbolA=arboles.get(0);
							arbolB=arboles.get(1);
						}
						else mensajeError+="Some of the data is not correct and no results can be displayed\n";
						
						controlador.ejecutarFuncion(funcion,mensajeError,1);
					}
					else controlador.ejecutarFuncion(funcion,mensajeError,1);
				}
				//Funcion para estrudiar el crecimiento del tronco en un arbol
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC5)){
					float tr=0;
					
					if(group.getSelection()!=null) arbol1=group.getSelection().getActionCommand();
					
					if(arbol1!=null && !fallo) arbolA=controlador.comprobarArbol(arbol1, fechaIni, fechaFin);
					else mensajeError+="There is no tree selected\n";
					
					if(arbolA==null) mensajeError+="There is no tree that is requested in the selected date range\n";
					
					tronc=tronco.getText();
					
					if(tronc.equals("INF")) tr=Float.MAX_VALUE;
					else if(Utils.isFloat(tronc)) tr=Float.valueOf(tronc);
					else{
						fallo=true;
						mensajeError+="The trunk is not a real greater than 0 or INF\n";
					}
					 
					if(!fallo && arbolA!=null && tr>=0) funcion=new FuncionTronco(arbolA, tr, fechaIni, fechaFin);
					else mensajeError+="Some of the data is not correct and no results can be displayed\n";
					
					controlador.ejecutarFuncion(funcion,mensajeError,1);
				}
				//Funcion para comparar el crecimiento del tronco entre varios arboles
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC6)){
					int cont=0, numArboles=0;
					
					for (JCheckBoxMenuItem checkBox : arbolCheckBox) {
						if (checkBox.isSelected()) {
							trees[contadorTrees]=checkBox.getActionCommand();
							contadorTrees++;
						}
					}
						
					arbol1=arb.getText();
						
					if(Utils.isInteger(arbol1)) numArboles=Integer.valueOf(arbol1);
					else{
						fallo=true;
						mensajeError+="The number of trees is not an integer greater than 0\n";
					}
					
					if(!fallo && (numArboles<=0 || numArboles!=contadorTrees)){
						fallo=true;
						mensajeError+="The same number of trees has not been selected";
					}
						
					if(!fallo){
						for(int i=0; i<contadorTrees; i++){
							Arbol arbolAux=controlador.comprobarArbol(trees[i],fechaIni,fechaFin);
							if(arbolAux!=null) {
								arboles.addElement(arbolAux);
								cont++;
							}	
						}
						
						if(!fallo && numArboles!=cont){
							fallo=true;
							mensajeError+="There is no tree in the selected date range\n";
						}
						
						if(!fallo){
							result.setText(null);
							for(int i=0; i<arboles.size(); i++){
								for(int j=i+1; j<arboles.size(); j++){
									funcion=new FuncionCompararTronco(arboles.get(i), arboles.get(j), fechaIni, fechaFin);
									controlador.ejecutarFuncion(funcion, mensajeError,0);
								}
							}
						}
						else controlador.ejecutarFuncion(funcion,mensajeError,1);
					}
					else controlador.ejecutarFuncion(funcion,mensajeError,1);
					
				}
				//Funcion para estudiar las contracciones de un arbol
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC7)){
					float cr=0;
					
					if(group.getSelection()!=null) arbol1=group.getSelection().getActionCommand();
					
					if(arbol1!=null && !fallo) arbolA=controlador.comprobarArbol(arbol1, fechaIni, fechaFin);
					else mensajeError+="There is no tree selected\n";
					
					if(arbolA==null) mensajeError+="There is no tree that is requested in the selected date range\n";
					
					contr=contraccion.getText();
					
					if(contr.equals("INF")) cr=Float.MAX_VALUE;
					else if(Utils.isFloat(contr)) cr=Float.valueOf(contr);
					else{
						fallo=true;
						mensajeError+="The contraction is not a real greater than 0 or INF\n";
					}
					 
					if(!fallo && arbolA!=null && cr>=0) funcion=new FuncionContraccion(arbolA, cr, fechaIni, fechaFin);
					else mensajeError+="Some of the data is not correct and no results can be displayed\n";
				
					controlador.ejecutarFuncion(funcion,mensajeError,1);
				}
				//Funcion para comparar las contracciones entre varios arboles
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC8)){
					int cont=0, numArboles=0;
					
					for (JCheckBoxMenuItem checkBox : arbolCheckBox) {
						if (checkBox.isSelected()) {
							trees[contadorTrees]=checkBox.getActionCommand();
							contadorTrees++;
						}
					}
						
					arbol1=arb.getText();
						
					if(Utils.isInteger(arbol1)) numArboles=Integer.valueOf(arbol1);
					else{
						fallo=true;
						mensajeError+="The number of trees is not an integer greater than 0\n";
					}
					
					if(!fallo && (numArboles<=0 || numArboles!=contadorTrees)){
						fallo=true;
						mensajeError+="The same number of trees has not been selected";
					}
						
					if(!fallo){
						for(int i=0; i<contadorTrees; i++){
							Arbol arbolAux=controlador.comprobarArbol(trees[i],fechaIni,fechaFin);
							if(arbolAux!=null) {
								arboles.addElement(arbolAux);
								cont++;
							}	
						}
						
						if(!fallo && numArboles!=cont){
							fallo=true;
							mensajeError+="There is no tree in the selected date range\n";
						}
						
						if(!fallo){
							result.setText(null);
							for(int i=0; i<arboles.size(); i++){
								for(int j=i+1; j<arboles.size(); j++){
									funcion=new FuncionCompararContraccion(arboles.get(i), arboles.get(j), fechaIni, fechaFin);
									controlador.ejecutarFuncion(funcion, mensajeError,0);
								}
							}
						}
						else controlador.ejecutarFuncion(funcion,mensajeError,1);
					}
					else controlador.ejecutarFuncion(funcion,mensajeError,1);
				}
				//Funcion para buscar un patron en un arbol
				else if(funcionComboBox.getSelectedItem().equals(EnumFuncion.FC9)){
					int erro=0;
					float toler=0, divi=0;
					
					if(group.getSelection()!=null) arbol1=group.getSelection().getActionCommand();
									
					if(arbol1!=null && !fallo) arbolA=controlador.comprobarArbol(arbol1, fechaIni, fechaFin);
					else mensajeError+="There is no tree selected\n";
					
					if(arbolA==null) mensajeError+="There is no tree that is requested in the selected date range\n";
					
					pat=patron.getText();
					tolerancia=tol.getText();
					division=div.getText();
					mismat=mismatches.getText();
					
					if(!Utils.isFloat(division)){
				    	fallo=true;
				    	mensajeError+="Tolerance is not real\n";
				    }
				    else divi=Float.valueOf(division);
					
					if(!Utils.isFloat(tolerancia)){
				    	fallo=true;
				    	mensajeError+="Tolerance is not a real greater than 0 and less than 0.5\n";
				    }
				    else toler=Float.valueOf(tolerancia);
				    
					
					if(!Utils.isInteger(mismat)){
					  	fallo=true;
					   	mensajeError+="The dada of Mismatches is not an integer greater than 0 and less than or equal to half the length of the pattern\n";
					}
					else erro=Integer.valueOf(mismat);
					
					if(!fallo && arbolA!=null && Utils.patronCorrecto(pat) && toler>=0 && toler<=0.5 && erro>=0 && erro<=(pat.length()/4))
						funcion=new FuncionPatron(arbolA, pat, divi, toler, EnumPatron.BoyMor, erro, fechaIni, fechaFin);
					else mensajeError+="Some of the data is not correct and no results can be displayed\n";
					
					controlador.ejecutarFuncion(funcion,mensajeError,1);
				}
				
				//Sacar grafica
				if(funcion!=null){
					iniciarGrafica(funcion,controlador.getIni());
					if(arbolA!=null){
						agnadirGrafica(arbolA,funcion,controlador.getIni());
						if(arbolB!=null){
							agnadirGrafica(arbolB,funcion,controlador.getIni());
							agnadirTrozo(arbolB,funcion,controlador.getIni());
						}
						agnadirTrozo(arbolA,funcion,controlador.getIni());
					}				
					else{
						for(int i=0; i<arboles.size();i++)
							agnadirGrafica(arboles.get(i),funcion,controlador.getIni());
						if(funciones!=null){
							for(int i=0; i<funciones.size(); i++){
								agnadirTrozo(funciones.elementAt(i).getArbol1(),funciones.elementAt(i),controlador.getIni());
								agnadirTrozo(funciones.elementAt(i).getArbol2(),funciones.elementAt(i),controlador.getIni());
							}
						}
					}
				}
				
			}
			
		});
		
		return panel;
	}
	
	//Metodo que inicia el panel con los resultados
	private JPanel initResultados() throws IOException{
		final JPanel panel=new JPanel();
		final JPanel derecha=new JPanel();
		final JPanel resultados=calcResultados();
		
		derecha.setLayout(new BorderLayout());
		derecha.add(sacarGrafica,BorderLayout.CENTER);
		derecha.add(salir,BorderLayout.SOUTH);
		
		panel.setLayout(new BorderLayout());
		panel.add(resultados,BorderLayout.CENTER);
		panel.add(derecha,BorderLayout.EAST);
		
		//Se implementa el action listener del boton sacarGrafica
		//Crea una ventana aparte con la grafica de los arboles y sus resultados
		sacarGrafica.addActionListener( new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				if(grafica!=null) new GraphApplication2(grafica);
			}	
			
		});
		
		return panel;
	}
	
	//Metodo que inicia el panel con el calculo de los resultados
	private JPanel calcResultados() throws IOException{
		final JPanel panel=new JPanel();
		
		panel.setBorder(BorderFactory.createTitledBorder("Results"));
		
		result=new JTextArea(200,200);
		
		//Se crea un JScrollPane para mostrar los resultados textuales
		JScrollPane resultScroll = new JScrollPane(result);
		resultScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		resultScroll.setPreferredSize(new Dimension(500, 500));
		resultScroll.setMinimumSize(getPreferredSize());
		
		panel.add(resultScroll);
		
		return panel;		
		
	}
	
	//Metodo para iniciar los valores de la grafica que se muestra al usuario
	private void iniciarGrafica(Funcion func,Fecha ini){
		if(func==null) grafica=null;
		else{
			Fecha fechaIni=func.getIni();
			Fecha fechaFin=func.getFin();
		
			int medidas1=Utils.medidasEntreFechas(ini, fechaIni);
			int medidas2=Utils.medidasEntreFechas(ini, fechaFin);
			
			int max=func.maximo();
			int min=func.minimo();
			
			//El eje X va con el tiempo (las medidas)
			//El eje Y va con máximos y mínimos
			int medidasTotalX=medidas2-medidas1;
			int medidasTotalY=max-min;
			float y=(float) 0.5;
			float x=(float) 0.5;
			
			if(medidasTotalY/23>y) y=medidasTotalY/23;
			if(medidasTotalX/10>x) x=medidasTotalX/10;
		
			PlotSettings p = new PlotSettings(medidas1, medidas2, min, max); 
			p.setPlotColor(Color.BLACK); 
			p.setGridSpacingX(x); 
			p.setGridSpacingY(y); 
			p.setTitle("Graphs of the trees result"); 
		
			grafica=new Graph2(p);
		}
	}
	
	//Metodo para agnadir la grafica de un arbol a la grafica final
	private void agnadirGrafica(Arbol arbol, Funcion func,Fecha ini){
		
		Fecha fechaIni=func.getIni();
		Fecha fechaFin=func.getFin();
		
		int medidas1=Utils.medidasEntreFechas(ini, fechaIni);
		int medidas2=Utils.medidasEntreFechas(ini, fechaFin);
		
		grafica.functions.add(new MuestraArbol(arbol,medidas1,medidas2,0,arbol.getNombre())); 
	
	}
	
	//Metodo para agnadir la grafica de un trozo resultado de un arbol a la grafica final
	private void agnadirTrozo(Arbol arbol, Funcion func, Fecha ini){
		int medidas1, medidas2;
		Vector<Resultado> inds=func.getIndices();
		
		int medidaBase=Utils.medidasEntreFechas(ini, func.getIni());
		
		if(inds!=null){
			for(int i=0; i<inds.size(); i++){
				medidas1=medidaBase+inds.get(i).getInd()-1;
				medidas2=medidaBase+inds.get(i).getInd()+inds.get(i).getTam()-1;
				grafica.functions.add(new MuestraArbol(arbol,medidas1,medidas2,inds.get(i).getInd()-1,"Trozo"));
			}
		}
	}
	
	//Metodo que calcula la interseccion de los indices de varias funciones 
	//Cambia el vector de indices de todas las funciones por el resultado
	private void interseccionFunciones(Vector<Funcion> funciones){
		Vector <Resultado> ini=funciones.elementAt(0).getIndices();
		Vector <Integer> resultado=obtenerInd(ini);
		
		for(int i=1; i<funciones.size(); i++)
			resultado=interseccion(resultado,obtenerInd(funciones.elementAt(i).getIndices()));
		
		for(int i=0; i<funciones.size(); i++)
			funciones.elementAt(i).setIndices(resultado);
		
	}
	
	//Metodo para poner el vector de indices a cada funcion en el caso de intervalo maximo
	private int maximoFunciones(Vector<Funcion> funciones){
		Vector<Resultado> ini=funciones.elementAt(0).getIndices();
		Vector <Resultado> aux=new Vector<Resultado>();
		Vector <Integer> resultado=new Vector<Integer>();
		int maximo=0;
		
		//Buvcle para agnadir a aux todos los intervalos sumados
		for(int i=0; i<ini.size();i++){
			int inicial=ini.get(i).getInd();
			int cuenta=1;
			while (i < ini.size()-1 && ini.get(i).getInd()+1 == ini.get(i+1).getInd()){
				cuenta++; 
				i++;
			}
			aux.addElement(new Resultado(inicial,cuenta));
		}
		
		//Bucle para saber cual es el maximo de los intervalos
		for(int i=0; i<aux.size();i++){
			if(maximo<aux.get(i).getTam())
				maximo=aux.get(i).getTam();
		}		
		
		//Bucle para agnadir a resultado los intervalos maximos
		for(int i=0; i<aux.size();i++){
			if(maximo==aux.get(i).getTam())
				resultado.addElement(aux.get(i).getInd());
		}
		
		//Bucle para agnadir a cada funcion su maximo y su vector de indices
		for(int i=0; i<funciones.size();i++){
			funciones.elementAt(i).setTam(maximo);
			funciones.elementAt(i).setIndices(resultado);
		}
		
		return maximo;
	}
	
	//Obtiene los indices del vector vect y los devuelve en resultado
	private Vector<Integer> obtenerInd(Vector<Resultado> vect){
		Vector <Integer> resultado=new Vector<Integer>(10);
		
		for(int i=0; i<vect.size(); i++) resultado.add(vect.get(i).getInd());
		
		return resultado;
	}
	
	//Devuelve la interseccion de indices entre vect1 y vect2
	private Vector<Integer> interseccion(Vector<Integer> vect1, Vector<Integer> vect2){
		Vector<Integer> resultado=new Vector<Integer>(10);
		
		int i=0, j=0;
		
		while(i<vect1.size() && j<vect2.size()){
			int cmp=vect1.get(i).compareTo(vect2.get(j));
			
			if(cmp==0){
				resultado.add(vect1.get(i));
				i++; j++;
			}
			else if(cmp>0) j++;
			else i++;
		}
		
		return resultado;
	}
	
	//Escribe el resultado para la funcion de comparar mas de dos arboles
	private void escribirResultado(Vector<Funcion> funciones, int ventana, Vector<Arbol> arboles){
		Arbol arbol1=funciones.elementAt(0).getArbol1();
		Vector<Resultado> indices=funciones.elementAt(0).getIndices();
		
		int aux=Utils.medidasEntreFechas(arbol1.getIniProg(), controlador.getIni());
		StringBuilder texto=new StringBuilder();
		
		texto.append("Comparison of trees ");
		
		for(int i=0; i<arboles.size()-1; i++){
			texto.append(arboles.elementAt(i).getNombre());
			if(i<arboles.size()-2) texto.append(", ");
			else texto.append(" and ");
		}
		
		texto.append(arboles.elementAt(arboles.size()-1).getNombre() + ".");
		texto.append(" Interval length " + ventana + ".\n");
		
		int numIntervalos = 0;
		
		for(int i=0; i<indices.size(); i++){
			texto.append("Interval "+(i+1));
			texto.append(": ");
			texto.append((aux+indices.get(i).getInd())+"-");
			
			// Acumula todos los intervalos que son iguales
			int cuenta=0;
			while (i < indices.size()-1 && indices.get(i).getInd()+1 == indices.get(i+1).getInd()){
				cuenta++; 
				i++;
			}
			cuenta += ventana;
			texto.append((aux+indices.get(i).getInd()+ventana)+" length " + (cuenta+1) + "\n");
			numIntervalos++;
						
		}
		texto.append("Number of intervals " + numIntervalos+ "\n");
		
		textAux=texto.toString();
		
	}
	
	//Métodos de la interfaz Programa.Observer
	
	//Muestra el texto inicial en el panel de resultados
	public void onInic(String texto) {
		result.setText(texto);
	}

	public void onListar(String fichero){}	
	
	//Muestra el mensaje de error en el panel de resultados
	public void onError(String texto) {
		if(!texto.equals("")) result.setText(texto);
		else result.setText("Some of the data is not correct and no results can be displayed\n");
	}
	
	//Muestra el texto del resultado de una funcion en el panel de resultados
	//Si borrar esta a 0, se mantiene el texto que estaba en el panel, sino se reescribe
	public void onResultado(Funcion func, Fecha ini, int borrar){
		if(borrar==0) result.setText(result.getText() + func.toString()+"\n");
		else if(borrar==1) result.setText(func.toString()+"\n");
		else result.setText(textAux);
	}
	
	//Atributos
	private JButton calcular;
	private JButton sacarGrafica;
	private JButton salir;
	
	private JComboBox<EnumFuncion> funcionComboBox;
	
	private JTextField tam;
	private JTextField max;
	private JTextField tIni;
	private JTextField tFin;
	private JTextField err;
	private JTextField arb;
	private JTextField saltos;
	private JTextField tronco;
	private JTextField contraccion;
	private JTextField patron;
	private JTextField div;
	private JTextField tol;
	private JTextField mismatches;
	
	private JTextArea result;
	
	private Vector<JRadioButtonMenuItem> arbolRadioButton;
	private Vector<JCheckBoxMenuItem> arbolCheckBox;
	
	private ControladorVentana controlador;
	private Class<MainWindow> clase;
	private Vector<ListaArboles> errores;
	
	private Graph2 grafica;
	
	private String textAux;
	
}
