package tfg.main.logica;

public class Fecha {
	
	public Fecha(String fecha){
		convertirFecha(fecha);
	}
	
	//Metodo que convierte un String con formato fecha en una Fecha valida
	public void convertirFecha(String fecha){		
		String [] datos=fecha.split(" ");
		String [] datos1=datos[0].split("/");
		String [] datos2=datos[1].split(":");
		
		dia=Integer.valueOf(datos1[0]);
		mes=Integer.valueOf(datos1[1]);
		agno=Integer.valueOf(datos1[2]);
		
		hora=Integer.valueOf(datos2[0]);
		min=Integer.valueOf(datos2[1]);
		seg=Integer.valueOf(datos2[2]);
	}
	
	//Getters y setters
	public int getAgno(){
		return agno;
	}
	
	public int getMes(){
		return mes;
	}
	
	public int getDia(){
		return dia;
	}
	
	public int getHora(){
		return hora;
	}
	
	public int getMin(){
		return min;
	}
	
	public int getSeg(){
		return seg;
	}
	
	public void setAgno(int agno){
		this.agno=agno;
	}
	
	public void setMes(int mes){
		this.mes=mes;
	}
	
	public void setDia(int dia){
		this.dia=dia;
	}
	
	public void setHora(int hora){
		this.hora=hora;
	}
	
	public void setMin(int min){
		this.min=min;
	}
	
	public void setSeg(int seg){
		this.seg=seg;
	}
	
	//Metodo equals
	public boolean equals(Fecha fecha2){
		return agno==fecha2.getAgno() && mes==fecha2.getMes() && dia==fecha2.getDia() 
				&& hora==fecha2.getHora() && min==fecha2.getMin() && seg==fecha2.getSeg();
	}
	
	//Metodo toString
	public String toString(){
		StringBuilder cadena=new StringBuilder();
		
		if(dia<10) cadena.append("0"+dia+"/");
		else cadena.append(dia+"/");
		
		if(mes<10) cadena.append("0"+mes+"/");
		else cadena.append(mes+"/");
		
		cadena.append(agno+" "+hora+":");
		
		if(min<10) cadena.append("0"+min+":");
		else cadena.append(min+":");
		
		if(seg<10) cadena.append("0"+seg);
		else cadena.append(seg);
		
		return cadena.toString();
	}
	
	//Metodo que compara la instancia con la Fecha pasada por parametros
	public int mayor(Fecha fecha2){		
		
		if(agno>fecha2.getAgno()) return 1;
		else if(agno<fecha2.getAgno()) return -1;
		
		if(mes>fecha2.getMes()) return 1;
		else if(mes<fecha2.getMes()) return -1;
		
		if(dia>fecha2.getDia()) return 1;
		else if(dia<fecha2.getDia()) return -1;
		
		if(hora>fecha2.getHora()) return 1;
		else if(hora<fecha2.getHora()) return -1;
		
		if(min>fecha2.getMin()) return 1;
		else if(min<fecha2.getMin()) return -1;
		
		if(seg>fecha2.getSeg()) return 1;
		else if(seg<fecha2.getSeg()) return -1;
		
		return 0;
	}
	
	//Atributos
	int agno;
	int mes;
	int dia;
	int hora;
	int min;
	int seg;
}
