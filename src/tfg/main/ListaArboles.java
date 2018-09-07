package tfg.main;

public class ListaArboles implements Comparable<ListaArboles>{
	public ListaArboles(String nombre, int num, float error){
		this.nombre=nombre;
		this.num=num;
		this.error=error;
	}
	
	public String getNombre(){
		return nombre;
	}
	
	public int getNum(){
		return num;
	}
	
	public float getError(){
		return error;
	}
	
	public boolean getValido(){
		if(error>0.2) return false;
		else return true;
	}
	
	//Para ordenar los arboles si usamos una función sort
	public int compareTo(ListaArboles t) {
		if(nombre.compareTo(t.getNombre())<0)
			return -1;
		else if(nombre.compareTo(t.getNombre())>0)
			return 1;
		else
			return 0;
	}
	
	//Atributos
	private String nombre;
	private int num;
	private float error;
}
