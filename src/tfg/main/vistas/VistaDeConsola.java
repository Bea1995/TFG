package tfg.main.vistas;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ac.essex.graphing.plotting.PlotSettings;
import tfg.main.grafica.Graph2;
import tfg.main.grafica.GraphApplication2;
import tfg.main.logica.Arbol;
import tfg.main.logica.Fecha;
import tfg.main.logica.Funcion;
import tfg.main.logica.Programa;
import tfg.main.logica.Observable;
import tfg.main.logica.Utils;

public class VistaDeConsola implements Programa.Observer {

	public VistaDeConsola(Observable<Programa.Observer> programa){
		programa.addObserver(this);
	}
	
	//Métodos de la interfaz Programa.Observer
	
	public void onInic(String texto) {
		System.out.println(texto);
		onMostrar();
	}
	
	//Muestra la lista de arboles
	public void onListar(String fichero) { 
		
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(fichero));
			
			String nombres;
			nombres = buffer.readLine();
			buffer.close();
			
			String[] nombre=nombres.split("\t");
			
			int cont=1;
			for(int i=1; i<nombre.length; i++){
				System.out.println("Tree "+cont+": "+nombre[i]);
				cont++;
			}
			System.out.println();
			onMostrar();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	//Muestra el texto de error
	public void onError(String texto) {
		System.out.println("Some of the data is not correct and no results can be displayed\n");
		onMostrar();	
	}
	
	//Muestra el resultado de una funcion
	public void onResultado(Funcion func, Fecha ini, int borrar){
		System.out.println(func.toString()+"\n");
		onMostrar();	
		onMostrarGrafica(func, ini);
	}
	
	//Muestra el menu de opciones
	private void onMostrar(){
		System.out.println("1. List of application trees");
		System.out.println("2. View of the data of a specific tree");
		System.out.println("3. Search for the interval in a specific tree");
		System.out.println("4. Comparison of two concrete trees");
		System.out.println("5. Search for discrepancies in two concrete trees");
		System.out.println("6: Growth of the trunk in a tree");
		System.out.println("7: Comparison of trunk growth between two trees");
		System.out.println("8: Contraction in a tree");
		System.out.println("9: Comparison of the contractions of two trees");
		System.out.println("10: Search for patterns in a tree");
		System.out.println("0. Exit");
	}
	
	//Muestra la grafica de los arboles
	private void onMostrarGrafica(Funcion func, Fecha ini){
		Arbol arbol1=func.getArbol1();
		Arbol arbol2=func.getArbol2();
		
		Fecha fechaIni=func.getIni();
		Fecha fechaFin=func.getFin();
		
		int medidas1=Utils.medidasEntreFechas(ini, fechaIni);
		int medidas2=Utils.medidasEntreFechas(ini, fechaFin);
		
		int max=func.maximo();
		int min=func.minimo();
		
		//El eje X va con el tiempo (las medidas)
		//El eje Y va con máximos y mínimos
        PlotSettings p = new PlotSettings(medidas1, medidas2, min, max); 
        p.setPlotColor(Color.RED); 
        p.setGridSpacingX(5); 
        p.setGridSpacingY(20); 
        p.setTitle("One tree or two trees painted"); 
		
        Graph2 graph = new Graph2(p); 
		
		if(arbol1!=null) graph.functions.add(new MuestraArbol(arbol1,medidas1,medidas2,0,arbol1.getNombre())); 
		if(arbol2!=null) graph.functions.add(new MuestraArbol(arbol2,medidas1,medidas2,0,arbol2.getNombre())); 
		
        new GraphApplication2(graph); 		
	}

}
