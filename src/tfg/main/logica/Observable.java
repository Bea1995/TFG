package tfg.main.logica;

import java.util.List;

public class Observable <T>{
	
	 /*A�ade un nuevo observador a la lista; si el observador ya
	  *exist�a, la operaci�n no tiene efecto. */
	public void addObserver(T o){
		if(!obs.contains(o)) obs.add(o);
	}
	
	/*Elimina de la lista el observador indicado; si el observador
	 *no estaba registrado, la operaci�n no tiene efecto. */
	public void removeObserver(T o){
		if(obs.contains(o)) obs.remove(o);
	}
	
	//Atributos.
	protected List<T> obs;

}
