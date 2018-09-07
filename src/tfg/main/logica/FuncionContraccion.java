package tfg.main.logica;

import java.util.Vector;

public class FuncionContraccion extends Funcion{
	
	public FuncionContraccion(Arbol arbol, float cantidad, Fecha ini, Fecha fin){
		arbol1=arbol;
		this.ini=ini;
		this.fin=fin;
		this.cantidad=cantidad;
	}

	//Calcula las contracciones del arbol para cada día
	public void calculo() {
		Vector<Float> datos=arbol1.getDatos();
		
		//Se suman un 1 por la fecha que no se cuenta
		int dia1=Utils.medidasHastaFinDia(ini)+1;
		int dia2=dia1+Utils.medidasEnUnDia();
		
		int hasta=Utils.medidasEntreFechas(ini,fin);
		
		ctr=new Vector<Float>(10);
		
		//Se empieza en la primera medida del primer día completo
		for(int i=dia2; i<hasta; i=i+Utils.medidasEnUnDia()){
			ctr.add(contraccion(dia1,i,datos));		
			dia1=i;
		}
		
	}
	
	//Método que calcula la contracción a partir de la fecha de un día dado y un vector
	private float contraccion(int ini, int fin, Vector<Float> datos){
		int mitad=(ini+fin)/2;
		float maxMorning=Float.MIN_VALUE;
		float minDia=Float.MAX_VALUE;
		
		/*Hacemos el cálculo en for's:
		 *Uno que va desde el inicio del día al mediodía
		 *Otro que va desde el mediodía hasta el final del día
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
	
	//Getters y setters
	public Vector<Resultado> getIndices(){return null;}
	
	public void setIndices(Vector<Integer> indices){return;}
	
	public void setTam(int tam){return;}
	
	//Metodo to String
	public String toString() {
		StringBuilder texto=new StringBuilder();
		
		if(cantidad==Float.MAX_VALUE){
			int ind=1; 
			float valor=0;
			
			for(int i=0; i<ctr.size(); i++){
				if(ctr.get(i)>valor){
					valor=ctr.get(i);
					ind=i+1;
				}
			}
			
			texto.append("The biggest contraction of the tree "+arbol1.getNombre() + " occurs on the day " + ind+'\n');
			texto.append("Its value is " + valor+'\n');
		}
		else{
			texto.append("The days when the contraction of the tree "+arbol1.getNombre() + " is greater than " + cantidad + " are :\n");
			for(int i=0; i<ctr.size(); i++){
				if(ctr.get(i)>cantidad)	texto.append("Day " + (i+1) + " with value " + ctr.get(i)+'\n');
			}
		}
		
		return texto.toString();
	}
	
	//Atributos
	private Vector<Float> ctr;
	private float cantidad;
}
