package tfg.main.logica;

import java.util.Collections;
import java.util.Vector;

public class FuncionCompararContraccion extends Funcion{
	
	public FuncionCompararContraccion(Arbol arbol1, Arbol arbol2, Fecha ini, Fecha fin){
		this.arbol1=arbol1;
		this.arbol2=arbol2;
		this.ini=ini;
		this.fin=fin;
	}

	//Calcula las contracciones de cada uno de los arboles
	public void calculo() {
		Vector<Float> datos1=arbol1.getDatos();
		Vector<Float> datos2=arbol2.getDatos();
		int cont=1;
		
		//Se suman un 1 por la fecha que no se cuenta
		int dia1=Utils.medidasHastaFinDia(ini)+1;
		int dia2=dia1+Utils.medidasEnUnDia();
		
		int hasta=Utils.medidasEntreFechas(ini,fin);
		
		ctr1=new Vector<Intervalo>(10);
		ctr2=new Vector<Intervalo>(10);
		
		//Se empieza en la primera medida del primer día completo
		for(int i=dia2; i<hasta; i=i+Utils.medidasEnUnDia()){
			ctr1.add(new Intervalo(cont,contraccion(dia1,i,datos1)));	
			ctr2.add(new Intervalo(cont,contraccion(dia1,i,datos2)));
			dia1=i;
			cont++;
		}
		
	}
	
	//Método que calcula la contracción a partir de la fecha de un día dado y un vector
	private float contraccion(int ini, int fin, Vector<Float> datos){
		int mitad=(ini+fin)/2;
		float maxMorning=Float.MIN_VALUE;
		float minDia=Float.MAX_VALUE;
		
		/*Hacemos el cálculo en for's:
		 * Uno que va desde el inicio del día al mediodía
		 * Otro que va desde el mediodía hasta el final del día
		 */
		for(int i=ini; i<mitad; i++){
			if(datos.get(i)>maxMorning)
				maxMorning=datos.get(i);
			if(datos.get(i)<minDia)
				minDia=datos.get(i);
		}
		
		for(int i=mitad; i<fin; i++){
			if(datos.get(i)<minDia)
				minDia=datos.get(i);
		}
						
		return maxMorning-minDia;
	}
	
	//Medida para indicar como de parecidos son las contracciones de dos arboles
	private float parecidos(){
		int tam=ctr1.size();
		float result=0;
		
		if(tam==0) return 1;
		
		//Ordenamos los dos vectores de tamagnos de troncos
		Collections.sort(ctr1);
		Collections.sort(ctr2);
		
		ind1=new Vector<Integer>(tam);
		ind2=new Vector<Integer>(tam);
		
		for(int i=0; i<tam; i++){
			ind1.add(0);
			ind2.add(0);
		}
		
		// Para cada día se pone a cuántos días gana en ese árbol
		for(int i=0; i<tam;i++){
			ind1.setElementAt(tam-i,ctr1.get(i).getNum()-1);
			ind2.setElementAt(tam-i,ctr2.get(i).getNum()-1);
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
		
		texto.append("The similarity of the contraction between the trees " + arbol1.getNombre());
		texto.append(" and " + arbol2.getNombre() + " is " + sim*100 + "%\n");
		
		return texto.toString();
	}
	
	//Atributos
	private Vector<Intervalo> ctr1;
	private Vector<Intervalo> ctr2;
	private Vector<Integer> ind1;
	private Vector<Integer> ind2;
}
