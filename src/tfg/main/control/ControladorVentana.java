package tfg.main.control;

import tfg.main.logica.*;

public class ControladorVentana {

	public ControladorVentana(Programa p){
		programa=p;
	}
	
	//Funciones que ejecutarán las funciones de la clase Programa

	//Metodo que comprueba si existe un arbol por medio del atributo Programa
	public Arbol comprobarArbol(String arbol, Fecha ini, Fecha fin){
		return programa.existeArbol(arbol, ini, fin);
	}
	
	//Metodo que ejecuta una funcion por medio del atributo Programa
	public void ejecutarFuncion(Funcion func, String texto, int borrar){
		if(func!=null) programa.ejecutarFuncion(func, borrar);
		else programa.error(texto);
	}
	
	public void requestSalir(){
		System.exit(0);
	}
	
	public Fecha getIni(){
		return programa.getIni();
	}
	
	//Atributos
	private Programa programa;
}
