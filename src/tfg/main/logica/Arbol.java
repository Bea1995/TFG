package tfg.main.logica;

import java.util.Vector;

public class Arbol {

	public Arbol(String nombre){
		this.nombre=nombre;		
	}
	
	//Getters y setter
	public String getNombre(){
		return nombre;
	}
	
	public Vector<Float> getDatos(){
		return datos;
	}
		
	public void setDatos(Vector<Float> dat){
		datos=dat; 
	}
	
	public Vector<Float> getCalculoBase(){
		return calculoBase;
	}
	
	public void setCalculoBase(Vector<Float> dat){
		calculoBase=dat; 
	}
	
	public Vector<Float> getDif(){
		return diferencias;
	}
	
	public void setDif(Vector<Float> dat){
		diferencias=dat; 
	}
	
	public int getCont(){
		return cont;
	}
	
	public void setCont(int cont){
		this.cont=cont;
	}
	
	public Fecha getIni(){
		return ini;
	}
	
	public void setIni(Fecha ini){
		this.ini=ini;
	}
	
	public Fecha getFin(){
		return fin;
	}
	
	public void setFin(Fecha fin){
		this.fin=fin;
	}
	
	public Fecha getIniProg(){
		return iniProg;
	}
	
	public void setIniProg(Fecha ini){
		iniProg=ini;
	}
	
	//Atributos
	private String nombre;
	private Vector<Float> datos;
	private Vector<Float> calculoBase;
	private Vector<Float> diferencias;
	private int cont; //De los elementos del vector (comparten el mismo contador los 3 vectores)
	private Fecha ini;
	private Fecha fin;
	private Fecha iniProg;
}
