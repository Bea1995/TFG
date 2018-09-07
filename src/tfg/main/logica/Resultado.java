package tfg.main.logica;

public class Resultado {
	public Resultado(int ind, int tam){
		this.ind=ind;
		this.tam=tam;
	}
	
	public int getInd(){
		return ind;
	}
	
	public int getTam(){
		return tam;
	}
	
	//Atributos
	private int ind;
	private int tam;
}
