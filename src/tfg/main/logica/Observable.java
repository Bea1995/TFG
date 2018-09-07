package tfg.main.logica;

import java.util.List;

public class Observable <T>{
	
	 /*Añade un nuevo observador a la lista; si el observador ya
	  *existía, la operación no tiene efecto. */
	public void addObserver(T o){
		if(!obs.contains(o)) obs.add(o);
	}
	
	/*Elimina de la lista el observador indicado; si el observador
	 *no estaba registrado, la operación no tiene efecto. */
	public void removeObserver(T o){
		if(obs.contains(o)) obs.remove(o);
	}
	
	//Atributos.
	protected List<T> obs;

}
