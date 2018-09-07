package tfg.main.logica;

public class Intervalo implements Comparable<Intervalo>{
	public Intervalo(int num, float valor){
		this.num=num;
		this.valor=valor;
	}
	
	public int getNum(){
		return num;
	}
	
	public float getValor(){
		return valor;
	}
	
	//Para ordenar los intervalos si usamos una función sort
	public int compareTo(Intervalo t) {
		if(valor<t.getValor())
			return -1;
		else if(valor>t.getValor())
			return 1;
		else
			return 0;
	}
	
	//Atributos
	private int num;
	private float valor;
}
