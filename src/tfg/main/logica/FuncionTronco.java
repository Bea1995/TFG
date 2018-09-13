package tfg.main.logica;

import java.util.Vector;

public class FuncionTronco extends Funcion{
	
	public FuncionTronco(Arbol arbol, float cantidad, Fecha ini, Fecha fin){
		arbol1=arbol;
		this.ini=ini;
		this.fin=fin;
		this.cantidad=cantidad;
	}

	//Calcula el crecimiento del arbol para cada dia
	public void calculo() {
		Vector<Float> datos=arbol1.getDatos();
		
		//Se suman un 1 por la fecha que no se cuenta
		int dia1=Utils.medidasHastaFinDia(ini)+1;
		int dia2=dia1+Utils.medidasEnUnDia();
		
		int hasta=Utils.medidasEntreFechas(ini,fin);
		float ant=datos.get(dia1); //Medida del tronco del arbol en el primer dia
		
		crecs=new Vector<Float>(10);
		
		for(int i=dia2; i<hasta; i=i+Utils.medidasEnUnDia()){
			crecs.add(datos.get(i)-ant);
			ant=datos.get(i);			
		}
		
	}

	//Getters y setters
	public Vector<Resultado> getIndices(){return null;}
	
	public void setIndices(Vector<Integer> indices){return;}
	
	public void setTam(int tam){return;}
	
	//Metodo toString
	public String toString() {
		StringBuilder texto=new StringBuilder();
		
		if(crecs.size()==0) texto.append("There is not a full day between both dates\n");
		else if(cantidad==Float.MAX_VALUE){
			int ind=1; 
			float valor=0;
			
			for(int i=0; i<crecs.size(); i++){
				if(crecs.get(i)>valor){
					valor=crecs.get(i);
					ind=i+1;
				}
			}
			
			texto.append("The trunk of the tree "+arbol1.getNombre() + " has grown more in the day " + ind+'\n');
			texto.append("Its value is " + valor+'\n');
		}
		else{
			texto.append("The days on which the trunk of the tree "+arbol1.getNombre() + " grows more than " + cantidad + " are :\n");
			for(int i=0; i<crecs.size(); i++){
				if(crecs.get(i)>cantidad) texto.append("Day " + (i+1) + " with value " + crecs.get(i)+'\n');
			}
		}
		
		return texto.toString();
	}
	
	//Atributos
	private Vector<Float> crecs;
	private float cantidad;
}
