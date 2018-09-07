package tfg.main.logica;

import java.util.Vector;

public abstract class Funcion {

	public Funcion(){};	
	
	//Funciones abstractas de la clase	
	public abstract void calculo();	
	public abstract String toString();	
	//Devuelve el vector de indices resultado
	public abstract Vector<Resultado> getIndices();
	//Pone un vector de indices como resultado
	public abstract void setIndices(Vector<Integer> indices);
	public abstract void setTam(int tam);
	
	//Getters y setters
	public Arbol getArbol1(){
		return arbol1;
	}
	
	public Arbol getArbol2(){
		return arbol2;
	}
	
	public Fecha getIni(){
		return ini;
	}
	
	public Fecha getFin(){
		return fin;
	}
	
	//Devuelve el maximo valor de los arboles que forman la funcion
	public int maximo(){
		float max1=Float.MIN_VALUE;
		float max2=Float.MIN_VALUE;
		
		for(int i=0; i<arbol1.getCont(); i++){
			if(max1<arbol1.getCalculoBase().elementAt(i))
				max1=arbol1.getCalculoBase().elementAt(i);
			
			if(arbol2!=null && max2<arbol2.getCalculoBase().elementAt(i))
				max2=arbol2.getCalculoBase().elementAt(i);
		}
		
		float max;
		
		if(max1>=max2) max=max1;
		else max=max2;
		
		return (int) max+5;		
	}
	
	//Devuelve el minimo valor de los arboles que forman la funcion
	public int minimo(){
		float min1=Float.MAX_VALUE;
		float min2=Float.MAX_VALUE;
		
		for(int i=0; i<arbol1.getCont(); i++){
			if(min1>arbol1.getCalculoBase().elementAt(i))
				min1=arbol1.getCalculoBase().elementAt(i);
			
			if(arbol2!=null && min2<arbol2.getCalculoBase().elementAt(i))
				min2=arbol2.getCalculoBase().elementAt(i);
		}
		
		float min;
		
		if(min1<=min2) min=min1;
		else min=min2;
		
		return (int) min-5;	
	}
	
	//Atributos
	protected Arbol arbol1;
	protected Arbol arbol2;
	protected Fecha ini;
	protected Fecha fin;
}
