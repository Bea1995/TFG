package tfg.main.logica;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class FuncionComparar extends Funcion{
	
	/*Clase que calcula en el vector de índices los comienzos en los que hay una subida o bajada 
	  de tamaño ventana a la vez en los dos árboles escogidos y menor que un valor dado*/
	
	public FuncionComparar(Arbol arbol1, Arbol arbol2, int ventana, float valorMax, boolean saltos, int err, Fecha ini, Fecha fin){
		this.arbol1=arbol1;
		this.arbol2=arbol2;
		if(ventana==Integer.MAX_VALUE) this.ventana=0;
		else this.ventana=ventana;
		this.valorMax=valorMax;
		this.saltos=saltos;
		this.err=err;
		this.ini=ini;
		this.fin=fin;
	}
	
	public void calculo(){
		if(ventana!=0){ //ventana!=INF
			if(err==0) calculoIntervaloSinError();
			else calculoIntervaloConError();
		}
		else{ //ventana=INF
			if(err==0) calculoSegmentoMaximoSinError();
			else calculoSegmentoMaximoConError();
		}
	}
	
	//Calcula el segmento de longitud maxima tal que los elementos no difieren en mas de una cantidad
	public void calculoSegmentoMaximoSinError(){
		// Numero de elementos que no difieren mas de los dado en el segmento que se esta explorando
		int cont=0;
		// Numero de elementos en el intervalo maximo encontrado hasta el momento	
		int contMax = 0;
				
		// Comienzo de los intervalos
		indices=new Vector<Integer>(100);
		
		Vector<Float> datos1=arbol1.getDif();
		Vector<Float> datos2=arbol2.getDif();
		
		for(int i=1; i<arbol1.getCont(); i++){
			
			//Dato "bueno" aumenta la longitud del ultimo segmento
			if((!saltos && Math.abs(datos1.get(i))<valorMax && Math.abs(datos2.get(i))<valorMax) 
					|| (saltos && Math.abs(datos1.get(i)-datos2.get(i))<valorMax))
				cont++;
			//Dato "malo" se empieza un nuevo segmento
			else cont=0;		
			
			//Si el contador es mayor que el maximo, se actualiza el maximo y
			//se borran todos los indices que ya habia
			if(cont>contMax){
				contMax=cont;
				indices.clear();
				indices.add(i-cont+1);
			}
			//Si el contador es igual al maximo se añade el nuevo segmento maximo
			else if(cont==contMax) indices.add(i-cont+1);
		}
		tam=contMax;
	}
	
	// Calcula el segmento de longitud maxima tal que los elementos no difieren en mas de una cantidad
	// Se permiten err errores
	public void calculoSegmentoMaximoConError(){
		// Numero de elementos correctos del ultimo segmento
		int cont=0;
		// Numero de elementos correctos del segmento maximo
		int contMax=0;
		// Numero de errores en el ultimo segmento
		int erroresCometidos=0;
		boolean erroresSeguidos=false;
		
		Vector<Float> datos1=arbol1.getDif();
		Vector<Float> datos2=arbol2.getDif();
		
		// Comienzo de los intervalos
		indices=new Vector<Integer>(100);
		// Guarda las posiciones en que hay un error
		errores=new LinkedList<Integer>();
		for(int i=1; i<arbol1.getCont(); i++){
			
			//Valor correcto
			if((!saltos && Math.abs(datos1.get(i))<valorMax && Math.abs(datos2.get(i))<valorMax) 
					|| (saltos && Math.abs(datos1.get(i)-datos2.get(i))<valorMax)){
				cont++;
				erroresSeguidos=false; //siguiente elemento no puede ser errores seguidos
			}
			// Error pero el segmento sigue siendo bueno
			else if(err>0 && erroresCometidos<err && !erroresSeguidos){
				erroresSeguidos=true; //El siguiente puede dar lugar a errores seguidos
				erroresCometidos++;
				errores.add(i);
			}
			// El segmento no es aceptable. Comienza nuevo desde este punto
			else if(erroresSeguidos){
				cont=0;
				erroresSeguidos=false;
				erroresCometidos=0;
				errores.clear();
			}
			// No hay errores seguidos al final pero he superado los errores permitidos
			// Recuperamos el segmento desde el primer error del ultimo segmento (guardado en la primera posicion de errores)
			else{
				erroresSeguidos=true;
				// Habria que restar un error y sumar uno. Por lo tanto no se modifica
				errores.add(i);
				cont=i-errores.remove()-1;
			}
			
			//Si el contador es mayor, se actualiza el maximo y
			// se borran todos los indices que ya habia
			if(cont+erroresCometidos>contMax){ //Si el contador es mayor, se borran todos los indices
				contMax = cont+erroresCometidos;
				indices.clear();
				indices.add(i-(cont+erroresCometidos-1));
			}
			else if (cont+erroresCometidos==contMax){
				indices.add(i-(cont+erroresCometidos-1));
			}	
		}	
		tam=contMax;
	}

	//Calcula los intervalos tales que los elementos no difieren mas de una cantidad
	public void calculoIntervaloSinError(){
		int cont=0;
			
		Vector<Float> datos1=arbol1.getDif();
		Vector<Float> datos2=arbol2.getDif();
		indices=new Vector<Integer>(100);
			
		// Cuenta el numero de datos correctos de la ventana inicial
		for(int i=1; i<arbol1.getCont() && i<=ventana; i++){
			if((!saltos && Math.abs(datos1.get(i))<valorMax && Math.abs(datos2.get(i))<valorMax) 
					|| (saltos && Math.abs(datos1.get(i)-datos2.get(i))<valorMax))
				cont++;
		}
			
		// Si los primeros valores forman un intervalo valido se añade a indices la primera posicion
		if(cont==ventana) indices.add(1);
			
		//Se trata el vector desde la primera ventana
		for(int i=ventana+1; i<arbol1.getCont(); i++){
				
			//Añade el dato siguiente
			float aux1=datos1.get(i);
			float aux2=datos2.get(i);
			
			if((!saltos && Math.abs(aux1)<valorMax && Math.abs(aux2)<valorMax) 
					|| (saltos && Math.abs(aux1-aux2)<valorMax))
				cont++;
			
			// Elimina el primer elemento de la ventana
			aux1=datos1.get(i-ventana);
			aux2=datos2.get(i-ventana);
			
			if((!saltos && Math.abs(aux1)<valorMax && Math.abs(aux2)<valorMax) 
				|| (saltos && Math.abs(aux1-aux2)<valorMax))
			cont--;
				
			//Si el intervalo es correcto, lo añade a indices
			if (cont==ventana) indices.add(i-ventana+1);
		}

	}
		
	//Calcula los intervalos tales que los elementos no difieren mas de una cantidad
	//Se permiten err errores
	public void calculoIntervaloConError(){
		int erroresCometidos=0;
		// Cuenta el numero de errores seguidos que hay en el intervalo
		int erroresSeguidos=0;
		// Indica si el elemento anterior tuvo un error
		boolean error=false;
		
		Vector<Float> datos1=arbol1.getDif();	
		Vector<Float> datos2=arbol2.getDif();
		indices=new Vector<Integer>(100);
		
		//Suma de la ventana inicial
		for (int i=1; i<arbol1.getCont() && i<=ventana; i++){
			if((!saltos && Math.abs(datos1.get(i))>=valorMax || Math.abs(datos2.get(i))>=valorMax) 
				|| (saltos && Math.abs(datos1.get(i)-datos2.get(i))>=valorMax)){ // Error
				erroresCometidos++;
				if (error) erroresSeguidos++;
				error = true;
			}
			else error = false; // Correcto
		}
		
		//Intervalo correcto
		if (erroresCometidos<=err && erroresSeguidos==0) indices.add(1);
			
		// Suma el resto de los valores. Añade el siguiente y quita el anterior
		for(int i=ventana+1; i<arbol1.getCont(); i++){
			// Añade el nuevo elemento
			float aux1=datos1.get(i);
			float aux2=datos2.get(i);
				
			if((!saltos && Math.abs(aux1)<valorMax && Math.abs(aux2)<valorMax) 
					|| (saltos && Math.abs(aux1-aux2)<valorMax))
				error=false;
			else{
				if (error) erroresSeguidos++;
				erroresCometidos++;
				error = true;
			}
			
			//Elimina el primer elemento de la ventana
			aux1=datos1.get(i-ventana);
			aux2=datos2.get(i-ventana);
			
			if((!saltos && (Math.abs(aux1)>=valorMax || Math.abs(aux2)>=valorMax)) 
					|| (saltos && Math.abs(aux1-aux2)>=valorMax)){ // El primer elemento era un dato erroneo
				if((!saltos && (Math.abs(datos1.get(i-ventana-1))>=valorMax || Math.abs(datos2.get(i-ventana-1))>=valorMax)) 
						|| (saltos && Math.abs(datos1.get(i-ventana-1)-datos2.get(i-ventana-1))>=valorMax)){
					erroresSeguidos--;
				}
				erroresCometidos--;
			}
			// La ventana sigue siendo correcta
			if (erroresCometidos<=err && erroresSeguidos==0) indices.add(i-ventana+1);
		}
	}

	//Getters y setters
	public Vector<Resultado> getIndices(){
		Vector<Resultado> result=new Vector<Resultado>(5);
		
		for(int i=0; i<indices.size(); i++){
			if(ventana==0) result.addElement(new Resultado(indices.get(i),tam));
			else result.addElement(new Resultado(indices.get(i),ventana));
		}
		
		return result;
	}
	
	public void setIndices(Vector<Integer> indices){
		this.indices=indices;
	}
	
	public void setTam(int tam){
		ventana=0;
		this.tam=tam;
	}
	
	//Metodo toString
	public String toString() {
		int aux=Utils.medidasEntreFechas(arbol1.getIniProg(), ini);
		StringBuilder texto=new StringBuilder();
		
		texto.append("Comparison of trees "+arbol1.getNombre()+" and " + arbol2.getNombre()+".");
		if(ventana!=0)	texto.append(" Interval length " + ventana + ".\n");
		else texto.append(" Largest intervals:\n");
		
		int numIntervalos = 0;
		
		for(int i=0; i<indices.size(); i++){
			texto.append("Interval "+(i+1));
			texto.append(": ");
			texto.append((aux+indices.get(i))+"-");
			
			//El +1 es porque es en lenguaje natural
			if(ventana!=0){
				// Acumula todos los intervalos que son iguales
				int cuenta=0;
				while (i < indices.size()-1 && indices.get(i)+1 == indices.get(i+1)){
					cuenta++; 
					i++;
				}
				cuenta += ventana;
				texto.append((aux+indices.get(i)+ventana)+" length " + cuenta + "\n");
				numIntervalos++;
			}
			else{
				texto.append((aux+indices.get(i)+tam) + " length " + tam +"\n");
				numIntervalos++;
			}
		}
		texto.append("Number of intervals " + numIntervalos+ "\n");
		
		return texto.toString();
	}
	
	//Atributos
	private Vector<Integer> indices;
	private boolean saltos;
	private Queue<Integer> errores;
	private int ventana;
	private float valorMax;
	private int err;
	private int tam;
}
