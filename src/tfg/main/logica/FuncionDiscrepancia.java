package tfg.main.logica;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class FuncionDiscrepancia extends Funcion{

	/*Clase que calcula en el vector de índices los valores en donde se produce una
	 *discrepancia entre ambos árboles, es decir, uno de ellos vale 0 y el otro cambia de valor*/
	public FuncionDiscrepancia(Arbol arbol1, Arbol arbol2, int ventana, int err, Fecha ini, Fecha fin){
		this.arbol1=arbol1;
		this.arbol2=arbol2;
		if(ventana==Integer.MAX_VALUE) this.ventana=0;
		else this.ventana=ventana;
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
	
	//Calcula el segmento de longitud maxima tal que los elementos son discrepantes entre si
	public void calculoSegmentoMaximoSinError(){
		// Numero de elementos que no difieren mas de los dado en el segmento que se esta explorando
		int cont=0;
		// Numero de elementos en el intervalo maximo encontrado hasta el momento	
		int contMax = 0;
				
		// Comienzo de los intervalos
		indices=new Vector<Intervalo>(100);
		
		//Lleva el valor de la discrepancia
		float valPunto=0;
		
		Vector<Float> datos1=arbol1.getDif();
		Vector<Float> datos2=arbol2.getDif();
			
		for(int i=1; i<arbol1.getCont(); i++){
				
			//Dato "bueno" aumenta la longitud del ultimo segmento
			if(discrepante(datos1.get(i), datos2.get(i))){
				cont++;
				valPunto+=Math.abs(datos1.get(i))+Math.abs(datos2.get(i));
			}
			//Dato "malo" se empieza un nuevo segmento
			else{
				cont=0;		
				valPunto=0;
			}
				
			//Si el contador es mayor que el maximo, se actualiza el maximo y
			//se borran todos los indices que ya habia
			if(cont>contMax){
				contMax=cont;
				indices.clear();
				indices.add(new Intervalo(i-cont+1,valPunto));
			}
			//Si el contador es igual al maximo se añade el nuevo segmento maximo
			else if(cont==contMax) indices.add(new Intervalo(i-cont+1,valPunto));
		}
		tam=contMax;
	}
		
	// Calcula el segmento de longitud maxima tal que los elementos son discrepantes entre sí
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
		indices=new Vector<Intervalo>(100);
		
		//Lleva el valor de la discrepancia
		float valPunto=0;
		
		// Guarda las posiciones en que hay un error
		errores=new LinkedList<Integer>();
		for(int i=1; i<arbol1.getCont(); i++){
			
			//Valor correcto
			if(discrepante(datos1.get(i),datos2.get(i))){
				cont++;
				valPunto+=Math.abs(datos1.get(i))+Math.abs(datos2.get(i));
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
				valPunto=0;
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
				indices.add(new Intervalo(i-(cont+erroresCometidos-1),valPunto));
			}
			else if (cont+erroresCometidos==contMax) 
				indices.add(new Intervalo(i-(cont+erroresCometidos-1),valPunto));		
		}	
		tam=contMax;
	}

	//Calcula los intervalos tales que los elementos son discrepantes entre sí
	public void calculoIntervaloSinError(){
		int cont=0;
			
		Vector<Float> datos1=arbol1.getDif();
		Vector<Float> datos2=arbol2.getDif();
		
		// Comienzo de los intervalos
		indices=new Vector<Intervalo>(100);
		
		//Lleva el valor de la discrepancia
		float valPunto=0;
			
		// Cuenta el numero de datos correctos de la ventana inicial
		for(int i=1; i<arbol1.getCont() && i<=ventana; i++){
			if(discrepante(datos1.get(i),datos2.get(i))){
				cont++;
				valPunto+=Math.abs(datos1.get(i))+Math.abs(datos2.get(i));
			}
		}
				
		// Si los primeros valores forman un intervalo valido se añade a indices la primera posicion
		if(cont==ventana) indices.add(new Intervalo(1,valPunto));
			
		//Se trata el vector desde la primera ventana
		for(int i=ventana+1; i<arbol1.getCont(); i++){
				
			//Añade el dato siguiente
			float aux1=datos1.get(i);
			float aux2=datos2.get(i);
			
			if(discrepante(datos1.get(i),datos2.get(i))){
				cont++;
				valPunto+=Math.abs(datos1.get(i))+Math.abs(datos2.get(i));
			}
			
			// Elimina el primer elemento de la ventana
			aux1=datos1.get(i-ventana);
			aux2=datos2.get(i-ventana);
			
			if(discrepante(aux1,aux2)){
				cont--;
				valPunto-=Math.abs(aux1)+Math.abs(aux2);
			}
				
			//Si el intervalo es correcto, lo añade a indices
			if (cont==ventana) indices.add(new Intervalo(i-ventana+1,valPunto));
		}
	}
			
	//Calcula los intervalos tales que los elementos son discrepantes entre si
	//Se permiten err errores
	public void calculoIntervaloConError(){
		int erroresCometidos=0;
		// Cuenta el numero de errores seguidos que hay en el intervalo
		int erroresSeguidos=0;
		// Indica si el elemento anterior tuvo un error
		boolean error=false;
			
		Vector<Float> datos1=arbol1.getDif();	
		Vector<Float> datos2=arbol2.getDif();
			
		// Comienzo de los intervalos
		indices=new Vector<Intervalo>(100);
	
		//Lleva el valor de la discrepancia
		float valPunto=0;
				
				
		//Suma de la ventana inicial
		for (int i=1; i<arbol1.getCont() && i<=ventana; i++){
			if(!discrepante(datos1.get(i),datos2.get(i))){ //Error
				erroresCometidos++;
				if (error) erroresSeguidos++;
				error = true;
			}
			else{ //Correcto
				error = false;
				valPunto+=Math.abs(datos1.get(i))+Math.abs(datos2.get(i));
			}
		}
			
		//Intervalo correcto
		if (erroresCometidos<=err && erroresSeguidos==0) indices.add(new Intervalo(1,valPunto));
				
		// Suma el resto de los valores. Añade el siguiente y quita el anterior
		for(int i=ventana+1; i<arbol1.getCont(); i++){
			// Añade el nuevo elemento
			float aux1=datos1.get(i);
			float aux2=datos2.get(i);
		
			if(discrepante(aux1,aux2)){
				error=false;
				valPunto+=Math.abs(aux1)+Math.abs(aux2);
			}
			else{
				if (error) erroresSeguidos++;
				erroresCometidos++;
				error = true;
			}
				
			//Elimina el primer elemento de la ventana
			aux1=datos1.get(i-ventana);
			aux2=datos2.get(i-ventana);
				
			if(!discrepante(aux1,aux2)){ // El primer elemento era un dato erroneo
				if(!discrepante(datos1.get(i-ventana-1),datos2.get(i-ventana-1))){
					erroresSeguidos--;
				}
				
				erroresCometidos--;
			}
			else valPunto-=Math.abs(aux1)+Math.abs(aux2);
			
			// La ventana sigue siendo correcta
			if (erroresCometidos<=err && erroresSeguidos==0) indices.add(new Intervalo(i-ventana+1,valPunto));
		}
	}
	
	public boolean discrepante(float dato1, float dato2){
		
		if(dato1>0 && dato2<=0) return true;
		if(dato1<0 && dato2>=0) return true;
		
		if(dato2>0 && dato1<=0) return true;
		if(dato2<0 && dato1>=0) return true;
		
		return false;
	}
	
	//Getters y setters
	public Vector<Resultado> getIndices(){
		Vector<Resultado> result=new Vector<Resultado>(5);
		
		for(int i=0; i<indices.size(); i++){
			if(ventana==0) result.addElement(new Resultado(indices.get(i).getNum(),tam));
			else result.addElement(new Resultado(indices.get(i).getNum(),ventana));
		}
		
		return result;
	}
	
	public void setIndices(Vector<Integer> indices){return;}
	
	public void setTam(int tam){return;}

	//Metodo toString
	public String toString() {
		int aux=Utils.medidasEntreFechas(arbol1.getIniProg(), ini);
		StringBuilder texto=new StringBuilder();
		
		texto.append("Results for the tree "+arbol1.getNombre() + ".");
		if(ventana!=0)	texto.append(" Interval length " + ventana + ".\n");
		else texto.append(" Largest intervals:\n");
		
		int numIntervalos = 0;
		float valor=0;
		
		for(int i=0; i<indices.size(); i++){
			texto.append("Interval "+(i+1));
			texto.append(": ");
			texto.append((aux+indices.get(i).getNum()+1)+"-");
			
			//El +1 es porque es en lenguaje natural
			if(ventana!=0){
				// Acumula todos los intervalos que son iguales
				int cuenta=0;
				valor=0;
				while (i < indices.size()-1 && indices.get(i).getNum()+1 == indices.get(i+1).getNum()){
					cuenta++; 
					valor+=Math.abs(arbol1.getDif().elementAt(indices.get(i).getNum()))+Math.abs(arbol2.getDif().elementAt(indices.get(i).getNum()));
					i++;
				}
				cuenta += ventana;
				texto.append((aux+indices.get(i).getNum()+ventana)+" length " + cuenta);
				numIntervalos++;
			}
			else{
				texto.append((aux+indices.get(i).getNum()+tam) + " length " + tam);
				numIntervalos++;
			}
			
			texto.append(" with discrepancy " + (valor+indices.get(i).getValor()) +"\n");	
		}
		texto.append("Number of intervals " + numIntervalos+ "\n");
		
		return texto.toString();
	}

	//Atributos
	private Vector<Intervalo> indices;
	private int tam;
	private Queue<Integer> errores;
	private int ventana;
	private int err;
}
