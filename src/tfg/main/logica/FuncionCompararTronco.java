package tfg.main.logica;

import java.util.Collections;
import java.util.Vector;

public class FuncionCompararTronco extends Funcion{
	
	public FuncionCompararTronco(Arbol arbol1, Arbol arbol2, Fecha ini, Fecha fin){
		this.arbol1=arbol1;
		this.arbol2=arbol2;
		this.ini=ini;
		this.fin=fin;
	}

	//Calcula los crecimientos del tronco de los dos arboles
	public void calculo() {
		Vector<Float> datos1=arbol1.getDatos();
		Vector<Float> datos2=arbol2.getDatos();
		int cont=1;
		
		//Se suman un 1 por la fecha que no se cuenta
		int dia1=Utils.medidasHastaFinDia(ini)+1;
		int dia2=dia1+Utils.medidasEnUnDia();
		
		int hasta=Utils.medidasEntreFechas(ini,fin);
		
		float ant1=datos1.get(dia1); //Medida del tronco del arbol1 al inicio
		float ant2=datos2.get(dia1); //Medida del tronco del arbol2 al inicio
		
		crecs1=new Vector<Intervalo>(10);
		crecs2=new Vector<Intervalo>(10);
		
		for(int i=dia2; i<hasta; i=i+Utils.medidasEnUnDia()){
			crecs1.add(new Intervalo(cont,datos1.get(i)-ant1));
			ant1=datos1.get(i);		
			crecs2.add(new Intervalo(cont,datos2.get(i)-ant2));
			ant2=datos2.get(i);	
			cont++;
		}
		
	}
	
	//Medida para indicar como de parecidos son los crecimientos del tronco de dos arboles
	private float parecidos(){
		int tam=crecs1.size();
		float result=0;
		
		if(tam==0) return 1;
		
		//Ordenamos los dos vectores de tamagnos de troncos
		Collections.sort(crecs1);
		Collections.sort(crecs2);
		
		ind1=new Vector<Integer>(tam);
		ind2=new Vector<Integer>(tam);
		
		for(int i=0; i<tam; i++){
			ind1.add(0);
			ind2.add(0);
		}
		
		// Para cada día se pone a cuántos días gana en ese árbol
		for(int i=0; i<tam;i++){
			ind1.setElementAt(tam-i,crecs1.get(i).getNum()-1);
			ind2.setElementAt(tam-i,crecs2.get(i).getNum()-1);
		}
		
		//Para cada día se suma el mínimo de entre los dos vectores calculados
		for(int i=0; i<tam;i++){
			if(ind1.elementAt(i)>=ind2.elementAt(i)) result+=ind2.elementAt(i);
			else result+=ind1.elementAt(i);
		}
		
		//El sumatorio de 1 a crecs.size() es el total de valores en los que puede coincidir los vectores
		float total=tam*(tam+1)/2;
					
		return result/total;
	}
	
	//Getters y setters
	public Vector<Resultado> getIndices(){return null;}
	
	public void setIndices(Vector<Integer> indices){return;}
	
	public void setTam(int tam){return;}

	//Metodo toString
	public String toString() {
		StringBuilder texto=new StringBuilder();
		float sim=parecidos();
		
		texto.append("The similarity of trunk growth between trees " + arbol1.getNombre());
		texto.append(" and " + arbol2.getNombre() + " is " + sim*100 + "%\n");
		
		return texto.toString();
	}
	
	//Atributos
	private Vector<Intervalo> crecs1;
	private Vector<Intervalo> crecs2;
	private Vector<Integer> ind1;
	private Vector<Integer> ind2;
}
