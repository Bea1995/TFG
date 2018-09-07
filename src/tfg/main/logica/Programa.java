package tfg.main.logica;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;


public class Programa extends Observable <Programa.Observer>{

	public Programa(int arboles, Fecha ini, Fecha fin, String fich,String texto){
		obs = new ArrayList<Programa.Observer>();
		cont=arboles;
		bosque=new Vector<Arbol>(5);
		this.ini=ini;
		this.fin=fin;
		fichero=fich;
		textoIni=texto;
	}
	
	//Carga los datos de un arbol en memoria
	//Entre las fechas ini y fin
	private Arbol cargarArbol(String arbol, Fecha ini, Fecha fin){
		Arbol arb=null;
		
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(arbol+".txt"));
			Vector<Float> datos=new Vector<Float>(Utils.medidasEntreFechas(ini, fin));
			arb=new Arbol(arbol);
			bosque.addElement(arb);
			
			String linea=buffer.readLine();
			
			while(linea!=null){
				String[] partes=linea.split("\t");
				Fecha aux=new Fecha(partes[0]);
				
				if(ini.mayor(aux)==1) linea=buffer.readLine();
				else{
					if(aux.mayor(fin)==1) linea=null;
					else{
						datos.add(Float.valueOf(partes[1]));
						linea=buffer.readLine();
					}
				}	
			}
			
			arb.setDatos(datos);
			arb.setCont(datos.size());
			arb.setIni(ini);
			arb.setFin(fin);
			arb.setIniProg(this.ini);
			
			calculo0(arb);
			diferencias(arb);
			
			buffer.close();
			
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return arb;
	}
	
	//Calcular los datos según el momento inicial que tenemos en un árbol dado
	public void calculo0(Arbol arbol){
		int cont=arbol.getCont();
		Vector<Float> datos=arbol.getDatos();
			
		float valor0=datos.elementAt(0);
		Vector<Float> aux=new Vector<Float>(cont);
			
		for(int i=0; i<cont; i++) aux.add(datos.elementAt(i)-valor0);				
			
		arbol.setCalculoBase(aux);
	}
	
	//Calcula la lista de diferencias de un árbol dado
	public void diferencias(Arbol arbol){
		int cont=arbol.getCont();
		Vector<Float> datos=arbol.getCalculoBase();
			
		float valorBase=datos.elementAt(0);
		Vector<Float> aux=new Vector<Float>(cont);
			
		float nulo=0;
		aux.add(nulo); //El primer valor no es válido
			
		for(int i=1; i<cont; i++){
			aux.add(datos.elementAt(i)-valorBase);
			valorBase=datos.elementAt(i);
		}
			
		arbol.setDif(aux);		
	}
	
	public int getCont(){
		return cont;
	}
	
	public Fecha getIni(){
		return ini;
	}
	
	//Metodo que devuelve un arbol si este existe segun los parametros dados
	//Si no existe se devuelve null
	public Arbol existeArbol(String arbol, Fecha ini, Fecha fin){
		if(ini.mayor(this.ini)==-1 || fin.mayor(this.fin)==1 || fin.mayor(ini)!=1)
			return null;
		
		return cargarArbol(arbol,ini,fin);
	}
	
	//Metodo para llamar a la lista de observadores
	public void listaArboles(){
		for(Programa.Observer o: obs) o.onListar(fichero);		
	}
	
	public void ejecutarFuncion(Funcion func, int borrar){
		func.calculo();
		for(Programa.Observer o:obs) o.onResultado(func, ini,borrar);
	}
	
	public void reset(){
		System.out.println("Reset of the program\n");
		
		for(Programa.Observer o: obs) o.onInic(textoIni);
	}
	
	public void error(String texto){
		for(Programa.Observer o: obs) o.onError(texto);
	}
	
	public interface Observer{
		
		//Método para iniciar el programa
		void onInic(String texto);
		
		//Método para mostrar los árboles de la aplicación
		void onListar(String fichero);
		
		//Metodo para mostrar el mensaje de error
		void onError(String texto);
		
		//Metodo para mostrar el resultado correspondiente
		void onResultado(Funcion func, Fecha ini, int borrar);
		
	}
	
	//Atributos
	private Vector<Arbol> bosque;
	private int cont; //Del número de árboles que hay
	private Fecha ini;
	private Fecha fin;
	private String fichero;
	private String textoIni;
}
