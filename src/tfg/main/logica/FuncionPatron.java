package tfg.main.logica;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Vector;

public class FuncionPatron extends Funcion{

	public FuncionPatron(Arbol arbol, String patron, float div, float tol, EnumPatron metodo, int mismatches, Fecha ini, Fecha fin){
		arbol1=arbol;
		this.ini=ini;
		this.fin=fin;
		this.dist=div;
		this.tol=tol;
		this.mismatches=mismatches;
		this.metodo=metodo;
		this.patron=new Vector<Integer>(1);
		String[] patrones=patron.split(" ");
		for(int i=0; i<patrones.length; i++){
			this.patron.addElement(Integer.valueOf(patrones[i]));
		}
		arbolPatron=new Vector<Float>(arbol1.getCont());
		
		//Calculo del texto y de los limites para las versiones con matrices
		computoArbolPatron();
		maxiVect();
		
		indices=new Vector<Integer>(100);
		mismatList=new Vector<Integer>(100);
	}
	
	public void calculo() {
		if(mismatches==0){
			if(patron.size()>1) BoyerMooreGoodSuffixRule();
			else fuerzaBrutaOpt();
		}
		else BoyerMooreMatriz();
	}
	
	//Algoritmo de fuerza bruta
	public void fuerzaBruta(){		
		for(int i=0; i<=arbolPatron.size()-patron.size();i++){
			int neq=0;
			for(int j=0; j<patron.size();j++){
				float aux=arbolPatron.get(i+j);
				int dato=0;
				if(aux!=Float.MAX_VALUE) dato=(int) aux;
				//Dato no valido
				if(arbolPatron.get(i+j)==Float.MAX_VALUE)	neq=neq+1;
				//Dato valido
				else if(tol!=0.5 && dato!=patron.get(j)) neq=neq+1;
				//Si la tolerancia es la mitad puede haber dos posibles valores
				else if(tol==0.5){
					if(aux==dato && dato!=patron.get(j)) neq=neq+1;
					if(aux!=dato && (dato!=patron.get(j) || (dato+1)!=patron.get(j))) neq=neq+1;
				}
			}
			if(neq<=mismatches){
				if(i!=0) indices.addElement(i);
				mismatList.addElement(neq);
			}
		}
	}

	//Algoritmo de fuerza bruta optimizado
	public void fuerzaBrutaOpt(){		
		for(int i=0; i<=arbolPatron.size()-patron.size();i++){
			int neq=0;
			for(int j=0; j<patron.size();j++){
				float aux=arbolPatron.get(i+j);
				int dato=0;
				if(aux!=Float.MAX_VALUE) dato=(int) aux;
				//Dato no valido
				if(arbolPatron.get(i+j)==Float.MAX_VALUE)	neq=neq+1;
				//Dato valido
				else if(tol!=0.5 && dato!=patron.get(j)) neq=neq+1;
				//Si la tolerancia es la mitad puede haber dos posibles valores
				else if(tol==0.5){
					if(aux==dato && dato!=patron.get(j)) neq=neq+1;
					if(aux!=dato && (dato!=patron.get(j) || (dato+1)!=patron.get(j))) neq=neq+1;
				}
				
				if(neq>mismatches) j=patron.size(); //Break del for
			}
			if(neq<=mismatches){
				if(i!=0) indices.addElement(i);
				mismatList.addElement(neq);
			}
		}
	}
	
	//Algoritmo Boyer Moore con tabla hash
	public void BoyerMoore(){		
		int clave;
		int m=patron.size();
		Vector<Integer> valor;
		tabla=new Hashtable<Integer,Vector<Integer>>();
		ready=new Hashtable<Integer, Integer>();
		
		//Preprocesado
		
		//Se rellenan los hash ready y tabla
		for(int j=0; j<m; j++){
			clave=patron.get(j);
			
			//Si no existía esa clave se completa con el valor por defecto
			//ready(clave)=|patron|+1 y tabla(clave)=Vector(|patron|)
			if(!ready.containsKey(clave)) ready.put(clave, m+1);
			
			if(!tabla.containsKey(clave)){
				valor=new Vector<Integer>(m);
				for(int k=0; k<m; k++) valor.addElement(m);
				tabla.put(clave, valor);
			}
		}
		
		//Se calculan los valores finales del valor de la tabla hash para esa clave
		for(int i=m-1; i>=1; i--){
			int max1=i+1;
			if(i+1<m-mismatches) max1=m-mismatches;
			for(int j=ready.get(patron.get(i-1))-1;j>=max1; j--){
				valor=tabla.get(patron.get(i-1));
				valor.setElementAt(j-i, j-1);
				tabla.replace(patron.get(i-1), valor);
			}
			ready.replace(patron.get(i-1), max1);		
		}
		
		//Para el resto de datos que no aparecen en el patron (dato por defecto)
		//ready(clave)=|patron|+1
		clave=Integer.MAX_VALUE;
		ready.put(clave,m+1);
		valor=new Vector<Integer>(m);
		for(int k=0; k<m; k++){
			valor.addElement(m);
		}
		tabla.put(clave, valor);
		
		//Algoritmo
		
		tiempo= System.nanoTime();
		
		int j=m;
		while(j<=arbolPatron.size()){
			int h=j-1;
			int i=m;
			int d=m-mismatches;
			int neq=0;
			
			while(i>0 && neq<=mismatches){
				float aux=arbolPatron.get(h);
				
				//Calculamos la clave para arbolPatron[h]
				if(aux!=Float.MAX_VALUE) clave=(int) aux;
				else clave=Integer.MAX_VALUE;
				
				//Obtenemos el valor de la tabla hash para la clave
				if(!tabla.containsKey(clave)) clave=Integer.MAX_VALUE;
				valor=tabla.get(clave);
				
				//Calculamos la distancia que se va a desplazar
				if(i>=m-mismatches){
					if(d>valor.elementAt(i-1)) d=valor.elementAt(i-1);				
				}
				
				//Vemos si se produce una discrepancia
				if(aux==Float.MAX_VALUE)	neq=neq+1;
				else if(clave!=patron.get(i-1)) neq=neq+1;
				
				i=i-1;
				h=h-1;
			}
			//Si el numero de discrepancias es menor, se produce una ocurrencia
			if(neq<=mismatches){
				if(j-m!=0) indices.addElement(j-m);
				mismatList.addElement(neq);
			}
			j=j+d;
		}
		
		tiempo= System.nanoTime()- tiempo;
	}
	
	//Algoritmo Boyer Moore con matriz
	public void BoyerMooreMatriz(){		
		int dato;
		int m=patron.size();
		
		int [][] tablaPos = new int[maxPos+1][m];
		int [][] tablaNeg = new int[maxNeg+1][m];
		int [] readyPos = new int[maxPos+1];
		int [] readyNeg= new int[maxNeg+1];
		
		//Preprocesado
		
		//Se rellenan los vectores con valores por defecto
		//ready=|patron|+1, tabla[i][]=|patron|
		
		//Caso numeros positivos
		for(int i=0; i<maxPos+1; i++){
			for(int j=0; j<m; j++) tablaPos[i][j]=m;
			readyPos[i]=m+1;
		}
		
		//Caso numeros negativos
		for(int i=0; i<maxNeg+1; i++){
			for(int j=0; j<m; j++) tablaNeg[i][j]=m;
			readyNeg[i]=m+1;
		}
		
		//Se calculan los valores finales de las matrices para cada numero
		for(int i=m-1; i>=1; i--){
			dato=patron.get(i-1);
			int max1=i+1;
			if(i+1<m-mismatches) max1=m-mismatches;
			//Caso positivo
			if(dato>=0){
				for(int j=readyPos[dato]-1; j>=max1; j--) tablaPos[dato][j-1]=j-i;
				readyPos[dato]=max1;
			}
			//Caso negativo
			if(dato<0){
				for(int j=readyNeg[-dato]-1; j>=max1; j--) tablaNeg[-dato][j-1]=j-i;
				readyNeg[-dato]=max1;
			}				
		}
		
		//Algoritmo
		tiempo= System.nanoTime();
		
		int j=m;
		dato=0;
		
		while(j<=arbolPatron.size()){
			int h=j-1;
			int i=m;
			int d=m-mismatches;
			int neq=0;
			
			while(i>0 && neq<=mismatches){
				float valor=arbolPatron.get(h);		
				if(valor!=Float.MAX_VALUE) dato=(int) valor;
				
				//Calculamos la distancia que se va a desplazar
				if(i>=m-mismatches){
					int shift=m;
					if(valor!=Float.MAX_VALUE && dato>=0) shift=tablaPos[dato][i-1];
					if(valor!=Float.MAX_VALUE && dato<0) shift=tablaNeg[-dato][i-1];
					if(d>shift) d=shift;	
				}
				
				//Vemos si se produce una discrepancia
				if(valor==Float.MAX_VALUE) neq=neq+1;
				else if(dato!=patron.get(i-1)) neq=neq+1;
				
				i=i-1;
				h=h-1;
			}
			//Si el numero de discrepancias es menor, se produce una ocurrencia
			if(neq<=mismatches){
				if(j-patron.size()!=0) indices.addElement(j-patron.size());
				mismatList.addElement(neq);
			}
			j=j+d;
		}
		
		tiempo= System.nanoTime()- tiempo;
	}	
	
	//Algoritmo Shift Add con tabla hash
	public void ShiftAdd(){		
		int clave;
		int valorAlg;
		int m=patron.size();
		Vector<Integer> valor;
		tabla=new Hashtable<Integer,Vector<Integer>>();
		tablaValores=new Hashtable<Integer,Integer>();
		
		//Preprocesado
		
		//Calculamos las variables b, OF_MASK y NEG_OF_MASK
		int b=(int)(Math.log(mismatches+1)/Math.log(2)+1+1);
		int OF_MASK=0;		
		for(int i=0; i<patron.size(); i++){
			int auxMask=1<<(b-1);
			OF_MASK=OF_MASK<<b | auxMask;
		}
		int NEG_OF_MASK=~OF_MASK;
		
		//Se rellena la tabla con los datos del patrón
		for(int j=0; j<m;j++){
			clave=patron.get(j);
			
			//Si no existía esa clave se completa con el valor por defecto
			//tabla(clave)=Vector(1);
			if(!tabla.containsKey(clave)){
				valor=new Vector<Integer>(m);
				for(int k=0; k<m; k++) valor.addElement(1);
				tabla.put(clave, valor);
			}
			
			//Se pone un 0 en la posicion en la que coincide el dato con el patron
			valor=tabla.get(clave);
			valor.setElementAt(0, j);
			tabla.replace(clave, valor);
		}
		
		//Para el resto de datos que no se encuentran en el patron (dato por defecto)
		//tabla(clave)=Vector(1);
		clave=Integer.MAX_VALUE;
		valor=new Vector<Integer>(m);
		for(int k=0; k<m; k++)	valor.addElement(1);
		tabla.put(clave, valor);
		
		//Para todos los valores posibles generamos su datoAlg (desplazando b posiciones)
		for(Entry<Integer, Vector<Integer>> entry : tabla.entrySet()){
			int key=entry.getKey();
			Vector<Integer> value=entry.getValue();
			
			valorAlg=0;
			for(int j=0; j<m; j++) valorAlg=valorAlg<<b | value.get(j);
			
			tablaValores.put(key, valorAlg);
		}
		
		//Calculamos las variables s y o
		int s=0 << (b*(patron.size()+1)-1);
		int o=0 << (b*(patron.size()+1)-1);
		
		//Algoritmo
		
		tiempo= System.nanoTime();
		
		for(int i=0; i<arbolPatron.size(); i++){
			float aux=arbolPatron.get(i);
			int datoAlg=0;
			if(aux!=Float.MAX_VALUE) datoAlg=(int) aux;
			
			//Calculamos la clave correspondiente al dato arbolPatron[i]
			if(aux==Float.MAX_VALUE) clave=Integer.MAX_VALUE;
			else if(tol!=0.5){
				if(tabla.containsKey(datoAlg)) clave=datoAlg;
				else clave=Integer.MAX_VALUE;
			}
			else if(tol==0.5){
				if(aux==datoAlg){ //Hay un valor entero
					if(tabla.containsKey(datoAlg)) clave=datoAlg;
					else clave=Integer.MAX_VALUE;
				}
				//Si hay dos valores posibles nos quedamos con el mas prometedor
				else{
					if(tabla.containsKey(datoAlg) && !tabla.containsKey(datoAlg+1))
						clave=datoAlg;
					else if(!tabla.containsKey(datoAlg) && tabla.containsKey(datoAlg+1))
							clave=datoAlg+1;
					else if(tabla.containsKey(datoAlg) && tabla.containsKey(datoAlg)){
						clave=datoAlg;
					}
					else clave=Integer.MAX_VALUE;
				}
			}
			
			//Obtenemos el valor de la tabla hash
			valorAlg=tablaValores.get(clave);
			
			//Actualizamos las variables s y o
			s=(s>>b) + valorAlg;
			o=(o>>b) | (s & OF_MASK);
			s=s & NEG_OF_MASK;
			
			//Nos quedamos con el último bloque de elementos para decidir si hay una ocurrencia
			int mult=1;
			int auxiliar=0;
			for(int j=0; j<b; j++){
				auxiliar+=mult;
				mult*=2;
			}
			int aux1=s & auxiliar;
			int aux2=o & auxiliar;
			
			//Si hay una ocurrencia la devolvemos
			if((aux1+aux2)<=mismatches && i>=m){
				int ind=i-patron.size()+1;
				if(ind!=0) indices.addElement(ind);
				mismatList.addElement(aux1+aux2);
			}
		}
		
		tiempo= System.nanoTime()- tiempo;
	}
	
	//Algoritmo Shift Add con matriz
	public void ShiftAddMatriz(){		
		int dato;
		int valorAlg;
		int valorDefecto;
		int m=patron.size();
		
		int [][] tablaPos = new int[maxPos+1][m];
		int [][] tablaNeg = new int[maxNeg+1][m];
		int [] valoresPos = new int[maxPos+1];
		int [] valoresNeg= new int[maxNeg+1];
		
		//Preprocesado
		
		//Calculamos las variables b, OF_MASK y NEG_OF_MASK
		int b=(int)(Math.log(mismatches+1)/Math.log(2)+1+1);
		int OF_MASK=0;		
		for(int i=0; i<patron.size(); i++){
			int auxMask=1<<(b-1);
			OF_MASK=OF_MASK<<b | auxMask;
		}
		int NEG_OF_MASK=~OF_MASK;
		
		//Rellenamos la tabla con el dato por defecto para cada numero
		//tabla[i][]=1
		
		//Caso positivo
		for(int i=0; i<maxPos+1; i++){
			for(int j=0; j<m; j++)
				tablaPos[i][j]=1;
		}
		//Caso negativo
		for(int i=0; i<maxNeg+1; i++){
			for(int j=0; j<m; j++)
				tablaNeg[i][j]=1;
		}
		
		//Se rellena la tabla con los datos del patrón
		for(int j=0; j<patron.size();j++){
			dato=patron.get(j);
			
			if(dato>=0) tablaPos[dato][j]=0;
			else tablaNeg[-dato][j]=0;
	
		}
		
		//Para todos los valores posibles generamos su datoAlg (desplazando b posiciones)
		
		//Caso positivo
		for(int i=0; i<maxPos+1; i++){
			valorAlg=0;
			for(int j=0; j<m; j++)	valorAlg=valorAlg<<b | tablaPos[i][j];
			valoresPos[i]=valorAlg;
		}
		//Caso negativo
		for(int i=0; i<maxNeg+1; i++){
			valorAlg=0;
			for(int j=0; j<m; j++)	valorAlg=valorAlg<<b | tablaNeg[i][j];
			valoresNeg[i]=valorAlg;
		}
		
		//Creamos una variable valor por defecto para los casos de fallo
		valorDefecto=0;
		for(int j=0; j<m; j++)	valorDefecto=valorDefecto<<b | 1;
				
		//Calculamos las variables s y o
		int s=0 << (b*(patron.size()+1)-1);
		int o=0 << (b*(patron.size()+1)-1);
		
		//Algoritmo
		
		tiempo= System.nanoTime();
		
		for(int i=0; i<arbolPatron.size(); i++){
			float aux=arbolPatron.get(i);
			int datoAlg=0;
			if(aux!=Float.MAX_VALUE) datoAlg=(int) aux;
			
			//Calculamos el valorAlg para arbolPatron[i]
			if(aux==Float.MAX_VALUE) valorAlg=valorDefecto;
			else{
				if(datoAlg>=0) valorAlg=valoresPos[datoAlg];
				else valorAlg=valoresNeg[-datoAlg];
				
				//Si la tolerancia es 0.5 y aux!=datoAlg significa que valen ambos valores
				if(tol==0.5 && aux!=datoAlg && valorAlg==valorDefecto){
					if(datoAlg>=0) valorAlg=valoresPos[datoAlg+1];
					else valorAlg=valoresNeg[-datoAlg+1];
				}	
			}
			
			//Actualizamos las variables s y o
			s=(s>>b) + valorAlg;
			o=(o>>b) | (s & OF_MASK);
			s=s & NEG_OF_MASK;
			
			//Nos quedamos con el último bloque de elementos para decidir si hay una ocurrencia
			int mult=1;
			int auxiliar=0;
			for(int j=0; j<b; j++){
				auxiliar+=mult;
				mult*=2;
			}
			int aux1=s & auxiliar;
			int aux2=o & auxiliar;
				
			//Si hay una ocurrencia la devolvemos
			if((aux1+aux2)<=mismatches && i>=patron.size()){
				int ind=i-patron.size()+1;
				if(ind!=0) indices.addElement(ind);
				mismatList.addElement(aux1+aux2);
			}
		}
		
		tiempo= System.nanoTime()- tiempo;
	}
	
	//Metodo que compara los valores del patrón empezando en las posiciones i y k
	//Devuelve el número de elementos iguales
	private int comparar(int i, int k, Vector<Integer> cad){
		int i1=i, k1=k;
		while(i1<cad.size()-1 && k1<cad.size() && cad.get(i1)==cad.get(k1)){
			i1++;
			k1++;
		}
		return i1-i;
	}
	
	// Algoritmo obtenido del libro: Algorithms on strings, trees, and sequences. Dan Gusfield
	//Pagina 9
	private void calculoZ(Vector<Integer> Z, Vector<Integer> cad){
		
		//Calculo de Z[2]
		Z.add(0);
		Z.add(comparar(0,1,cad));
		
		int r,l;
		if(Z.get(1)>0){
			r=Z.get(1);
			l=1;
		}
		else{
			r=-1;
			l=-1;
		}
		
		//Paso inductivo
		int k=2;
		while(k<cad.size()){
			if(k>r){
				Z.add(comparar(0,k,cad));
				if(Z.get(k)>0){
					r=k+Z.get(k)-1;
					l=k;
				}	
			}
			else{ //patron[k] está contenido en una secuencia
				int k1=k-l; //Caracter que debe ser igual al patron[k]
				int beta=r-k+1; //Longitud del segmento desde k hasta r
				
				//El segmento igual es menor que r
				if(Z.get(k1)<beta){ 
					Z.add(Z.get(k1));
				}
				//El segmento igual que comienza conteiene a k1 es mayor que r
				//Hay que buscar el final del segmento
				else{ 
					int numelems=comparar(beta+1,r+1,cad);
					Z.add(r+numelems-k+1);
					r+=numelems;
					l=k;
				}	
			}
			k++;		
		}
	}
	
	//Metodo para el calculo del vector L
	private void calculoL(Vector<Integer> L, Vector<Integer> Z){
		int m=patron.size();
		
		for(int i=0; i<m; i++) L.add(0);
		
		for(int j=1; j<=m-1; j++){
			int Nj=Z.get(m-j);
			int i=m-Nj+1;
			if(i<=m) L.setElementAt(j,i-1);
		}

	}
	
	//Metodo para el calculo del vector l
	private void calculol(Vector<Integer> l, Vector<Integer> Z){
		int valor=0;
		int m=patron.size();
		
		for(int i=0; i<m; i++)l.add(0);
		
		for(int j=0; j<m; j++){
			if(Z.get(m-j-1)==j+1) valor=j+1;
			l.setElementAt(valor,m-j-1);
		}
	}
	
	//Algoritmo Boyer Moore exacto
	public void BoyerMooreGoodSuffixRule(){
		int m=patron.size();
		float valor;
		int dato;
		int shift;
		
		//Preprocesado
		
		//Cálculo de R(x)		
		int [][] tablaPos = new int[maxPos+1][m];
		int [][] tablaNeg = new int[maxNeg+1][m];
		int [] readyPos = new int[maxPos+1];
		int [] readyNeg= new int[maxNeg+1];
		
		//Calculo de los vectores Z, L y l
		Vector<Integer> Z=new Vector<Integer>(patron.size());
		Vector<Integer> L=new Vector<Integer>(patron.size());
		Vector<Integer> l=new Vector<Integer>(patron.size());
		
		//Se rellenan los vectores con valores por defecto
		//Caso positivo
		for(int i=0; i<maxPos+1; i++){
			for(int j=0; j<m; j++) tablaPos[i][j]=m;
			readyPos[i]=m+1;
		}
		//Caso negativo
		for(int i=0; i<maxNeg+1; i++){
			for(int j=0; j<m; j++) tablaNeg[i][j]=m;
			readyNeg[i]=m+1;
		}
		
		//Se calculan los valores finales de las matrices para cada numero
		for(int i=m-1; i>=1; i--){
			dato=patron.get(i-1);
			int max1=i+1;
			if(i+1<1) max1=1;
			//Caso positivo
			if(dato>=0){
				for(int j=readyPos[dato]-1; j>=max1; j--) tablaPos[dato][j-1]=j-i;
				readyPos[dato]=max1;
			}
			//Caso negativo
			if(dato<0){
				for(int j=readyNeg[-dato]-1; j>=max1; j--) tablaNeg[-dato][j-1]=j-i;
				readyNeg[-dato]=max1;
			}			
		}
		
		//Cálculo de L'
		Vector<Integer> inv=new Vector<Integer>(m);
		for(int i=0; i<m; i++) inv.add(patron.get(m-i-1));
		
		//Calculo de Z y L
		calculoZ(Z,inv); 
		calculoL(L,Z);
		
		//Cálculo de l'
		calculol(l,Z);
		
		//Algoritmo
		tiempo= System.nanoTime();
		
		int j=m;
		
		while(j<=arbolPatron.size()){
			int h=j;
			int i=m;
			
			valor=arbolPatron.get(h-1);
			if(valor!=Float.MAX_VALUE) dato=(int) valor;
			else dato=Integer.MAX_VALUE;
			
			//Se avanza hasta que el patron y el texto difieran
			while(i>0 && dato!=Integer.MAX_VALUE && dato==patron.get(i-1)){
				i=i-1;
				h=h-1;
				
				if(h>0){
					valor=arbolPatron.get(h-1);
					if(valor!=Float.MAX_VALUE) dato=(int) valor;
					else dato=Integer.MAX_VALUE;
				}
			}
			
			//Si se llega al principio, se produce una ocurrencia
			if(i==0){
				if(j-patron.size()!=0) indices.addElement(j-patron.size());
				mismatList.addElement(0);
				j+=m-l.get(1);
			}
			//En otro caso, se calcula el desplazamiento idoneo
			else{
				//Aumento regla del caracter malo
				int shift1=m;
				if(valor!=Float.MAX_VALUE && dato>=0) shift1=tablaPos[dato][i-1];
				if(valor!=Float.MAX_VALUE && dato<0) shift1=tablaNeg[-dato][i-1]; 
				
				if(shift1==m) shift1=i;
				
				//Aumento regla del sufijo bueno
				int shift2=1; //Si falla el primer elemento, se desplaza uno a la derecha
				if(i<m && L.get(i)>0) shift2=m-L.get(i);
				else if(i<m) shift2=m-l.get(i);
				
				//El desplazamiento es el maximo entre las dos reglas
				if(shift1>=shift2) shift=shift1;
				else shift=shift2;
				
				j+=shift;
			}
		}
		
		tiempo= System.nanoTime()- tiempo;
	}	
	
	//Metodo toString
	public String toString() {
		int aux=Utils.medidasEntreFechas(arbol1.getIniProg(), ini);
		StringBuilder texto=new StringBuilder();
		
		texto.append("The intervals of the form ");
		for(int i=0; i<patron.size();i++){
			texto.append(patron.get(i) + " ");
		}
		texto.append("found in the tree " + arbol1.getNombre() + "\nwith a distance " + dist);
		texto.append(", a tolerance " + tol + " and " + mismatches + " possible mismatch(es)\nfor the method " + metodo + " are:\n");
		
		for(int i=0; i<indices.size(); i++){
			texto.append("Interval "+ (i+1) + ": ");
			//El +1 es porque es en lenguaje natural
			texto.append((aux+indices.get(i))+"-"+(aux+indices.get(i)+patron.size()));
			texto.append(" with " + mismatList.get(i) + " mismatch(es)\n");
		}
		
		texto.append("In total there are " + indices.size() + " intervals\n");
		
		return texto.toString();
	}
	
	//Metodo que modifica el vector del arbol segun los atributos dist y tol
	//para obtener el texto sobre el que se va a buscar el patron
	private void computoArbolPatron(){
		Vector<Float> datos=arbol1.getDif();
		
		for(int i=0; i<arbol1.getCont(); i++){
			float aux=datos.get(i);
			float div=aux/dist;
			float parteEnt = (int) div;
			float resto=div-parteEnt;
			int suma=1;
			
			//Si el dato es negativo resto y suma cambian
			if(aux<0){
				resto=-div+parteEnt;
				suma=-1;
			}
		
			if(tol!=0.5){
				//Si tol*dist es mayor que el resto, se añade al texto la parte entera de aux
				if(resto<=tol) arbolPatron.addElement(parteEnt);
				//Si tol*dist es mayor que dist-resto, se añade al texto la siguiente parte entera
				else if((1-resto)<=tol) arbolPatron.addElement(parteEnt+suma);
				//Caso no valido
				else {
					arbolPatron.addElement(Float.MAX_VALUE);		
				}
			}
			else{
				if(resto<tol) arbolPatron.addElement(parteEnt);
				else if((1-resto)<tol) arbolPatron.addElement(parteEnt+suma);
				else { //Caso medio (resto=(1-resto)=0.5)
					arbolPatron.addElement(div);
				}
			}
		}
	}
	
	//Metodo que calcula el maximo valor y el minimo en el vector del arbol modificado
	//Tambien se calcula en el patron. Las variables maxPos y maxNeg seran los valores
	//limites de nuestro alfabeto
	public void maxiVect(){
		float pos=0;
		float neg=0;
		
		for(int i=0; i<arbolPatron.size(); i++){
			if(arbolPatron.get(i)!=Float.MAX_VALUE){
				if(arbolPatron.get(i)>0){
					if(arbolPatron.get(i)>pos) pos=arbolPatron.get(i);
				}
				else if(arbolPatron.get(i)<0){
					if(arbolPatron.get(i)<neg) neg=arbolPatron.get(i);
				}
			}
		}
		
		for(int i=0; i<patron.size();i++){
			if(patron.get(i)>0){
				if(patron.get(i)>pos) pos=patron.get(i);
			}
			else if(patron.get(i)<0){
				if(patron.get(i)<neg) neg=patron.get(i);
			}
		}
			
		maxPos=(int) pos;
		maxNeg=(int) neg;
		maxNeg=-maxNeg;
		
	}
	
	//Getters y setters
	public EnumPatron getMetodo(){
		return metodo;
	}
	
	public long getTiempo(){
		if(metodo.equals(EnumPatron.FuerBrut)) return 0;
		else return tiempo;
	}
	
	public Vector<Resultado> getIndices(){
		Vector<Resultado> result=new Vector<Resultado>(5);
		
		for(int i=0; i<indices.size(); i++)	result.addElement(new Resultado(indices.get(i),patron.size()));
		
		return result;
	}

	public void setIndices(Vector<Integer> indices){return;}
	
	public void setTam(int tam){return;}
	
	public void limpiarDatos(){
		mismatList.clear();
		indices.clear();
	}
	
	//Atributos
	private float dist;
	private float tol;
	private int mismatches;
	private Vector<Integer> patron;
	private Vector<Float> arbolPatron;
	private EnumPatron metodo;
	//No puede darse el indice 0 porque no pertenece a ninguna medida
	private Vector<Integer> indices;
	private Vector<Integer> mismatList;
	private Hashtable<Integer,Vector<Integer>> tabla;
	private Hashtable<Integer,Integer> tablaValores;
	private Hashtable<Integer,Integer> ready;
	private long tiempo;
	private int maxPos;
	private int maxNeg;
}
