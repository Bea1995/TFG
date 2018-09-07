package tfg.main.vistas;
import java.util.Vector;

import tfg.main.grafica.ContinuousFunctionPlotter2;
import tfg.main.logica.Arbol;


public class MuestraArbol extends ContinuousFunctionPlotter2{
	
	//Ini y fin son los puntos desde los que queremos pintar el árbol
	public MuestraArbol(Arbol arb, int ini, int fin, int base, String nombre){
		arbol=arb;
		this.ini=ini;
		this.fin=fin;
		this.nombre=nombre;
		this.base=base;
		datos=arbol.getCalculoBase();
	}
	
	/*Para mostrar los árboles como son puntos podemos usar la ecuación
	 * de la recta que pasa por el punto anterior y el siguiente para 
	 * el argumento de arg0 (que será el x)
	 */

	/* Ecuación de la recta que pasa por dos puntos:
	 * (y-y1)/(x-x1)=(y2-y1)/(x2-x1) donde los xi son los indices del vector
	 * normalizados al inicio del intervalo y los yi el valor del tronco del árbol
	 */
	public double getY(double arg0) {
		double y;
		int parteEnt = (int) arg0;
		if(arg0<ini+1 || arg0>fin+2) y=0;
		else if(nombre.equals("Trozo") && arg0<=ini+2){
			float y1=datos.get(parteEnt-(ini+1)+base);
			y=parteEnt;
			if(y1>0 && arg0>0 && arg0<=y1) y=parteEnt;
			else if(y1<0 && arg0<0 && arg0>y1) y=parteEnt;
			else y=0;				
		}
		else{
			float y1=datos.get(parteEnt-(ini+1)+base);
			float y2;
			
			if(parteEnt-1==fin) y2=0;
			else y2=datos.get(parteEnt+1-(ini+1)+base);
			
			int x1=parteEnt;
			int x2=parteEnt+1;
			
			//Ecuación de la recta despejada
			if(parteEnt-1==fin){
				if(y1>=0 && arg0>y2 && arg0<=y1) y=x1;
				else if(y1<0 && arg0<y2 && arg0>y1) y=x1;
				else y=0;				
			}
			else y=((y2-y1)*(arg0-x1))/(x2-x1)+y1;	
		}
		
		return y;
	}
	
	public String getName() {
		return nombre;
	}

	//Atributos
	private Vector<Float> datos;
	private Arbol arbol;
	private int ini;
	private int fin;
	private int base;
	private String nombre;
}

