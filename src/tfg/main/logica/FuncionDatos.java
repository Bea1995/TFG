package tfg.main.logica;

import java.util.Vector;

public class FuncionDatos extends Funcion{
	
	public FuncionDatos(Arbol arbol, Fecha ini, Fecha fin){
		arbol1=arbol;
		this.ini=ini;
		this.fin=fin;
	}
	
	public void calculo(){return;}
	
	//Getters y setters	
	public Vector<Resultado> getIndices(){return null;}
	
	public void setIndices(Vector<Integer> indices){return;}
	
	public void setTam(int tam){return;}

	//Metodo toString
	public String toString() {
		int aux=Utils.medidasEntreFechas(arbol1.getIniProg(), ini);
		StringBuilder texto=new StringBuilder();
		
		texto.append("The data of the tree "+ arbol1.getNombre() + " are:\n");
		
		for(int i=0; i<arbol1.getCont(); i++){
			texto.append("Interval\tData\tNormalized\tDifferences\n");
			texto.append((aux+i+1)+"\t"+arbol1.getDatos().get(i)+"\t"+arbol1.getCalculoBase().get(i)+"\t"+arbol1.getDif().get(i)+"\n");
		}
		
		return texto.toString();
	}

}
