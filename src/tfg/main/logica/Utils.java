package tfg.main.logica;

public class Utils {
	
	//Calcula cuantos dias pasan entre dos fechas dadas
	public static int medidasEntreFechas(Fecha fecha1, Fecha fecha2){
		if(fecha1.mayor(fecha2)!=-1) return 0;
		else{
			int cont=0;
			
			int diasFecha1=diasHastaEseMes(fecha1.getMes())+fecha1.getDia()-1;
			int diasFecha2=diasHastaEseMes(fecha2.getMes())+fecha2.getDia()-1;
			
			//Estamos en el mismo año
			if(fecha1.getAgno()==fecha2.getAgno()){
				cont+=(diasFecha2-diasFecha1-1)*medidasEnUnDia()+medidasHastaFinDia(fecha1)+medidasHastaAhoraDia(fecha2);
				return cont;
			}
			
			//Diferencia de años
			int dato1=(fecha2.getAgno()-fecha1.getAgno()-1)*365*medidasEnUnDia(); //Medidas entre años
			int dato2=(365-diasFecha1-1)*medidasEnUnDia(); //Medidas hasta fin de año
			int dato3=medidasHastaFinDia(fecha1); //Medidas hasta el fín del dia
			int dato4=diasFecha2*medidasEnUnDia(); //Medidas del nuevo año
			int dato5=medidasHastaAhoraDia(fecha2); //Medidas de nuestro día
			
			cont=dato1+dato2+dato3+dato4+dato5;
			
			return cont;
		}
	}
	
	//Devuelve el numero de medidas que se producen en un dia
	public static int medidasEnUnDia(){
		return 24*60*2;	//24 horas x 60 minutos x 2 medidas/minuto
	}
	
	//Devuelve el numero de dias que hay hasta un mes dado
	public static int diasHastaEseMes(int mes){
		int cont=0;
		
		if(mes==1) cont=0;
		else if(mes==2) cont=31;
		else if(mes==3) cont=31+28;
		else if(mes==4) cont=31*2+28;
		else if(mes==5) cont=31*2+30+28;
		else if(mes==6) cont=31*3+30+28;
		else if(mes==7)	cont=31*3+30*2+28;
		else if(mes==8) cont=31*4+30*2+28;
		else if(mes==9) cont=31*5+30*2+28;
		else if(mes==10) cont=31*5+30*3+28;
		else if(mes==11) cont=31*6+30*3+28;
		else if(mes==12) cont=31*6+30*4+28;
		
		return cont;
	}
	
	//Devuelve el numero de medidas que han pasado hasta la Fecha dada en ese dia
	public static int medidasHastaAhoraDia(Fecha fecha){
		int cont=(fecha.getHora())*60*2+(fecha.getMin())*2;
		
		if(fecha.getSeg()==0) cont+=1;
		else cont+=2;
		
		return cont;
	}
	
	//Devuelve el numero de medidas que faltan desde una fecha hasta el final del dia
	public static int medidasHastaFinDia(Fecha fecha){
		return medidasEnUnDia()-medidasHastaAhoraDia(fecha);
	}
	
	//Devuelve la siguiente fecha respecto de una fecha dada
	public static String siguienteMedida(String fech){
		Fecha fecha=new Fecha(fech);
		
		if(fecha.getSeg()==0) fecha.setSeg(30);
		else{
			fecha.setSeg(0);
			fecha.setMin((fecha.getMin()+1)%60);

			if(fecha.getMin()==0){
				fecha.setHora((fecha.getHora()+1)%24);
			
				if(fecha.getHora()==0){
					if(fecha.getMes()==2) fecha.setDia((fecha.getDia()+1)%29);
					else if(fecha.getMes()==4 || fecha.getMes()==6 || fecha.getMes()==9 || fecha.getMes()==11){
						fecha.setDia((fecha.getDia()+1)%31);
					}
					else fecha.setDia((fecha.getDia()+1)%32);
					
					if(fecha.getDia()==0){
						fecha.setDia(1);
						fecha.setMes((fecha.getMes()+1)%13);
						
						if(fecha.getMes()==0){
							fecha.setMes(1);
							fecha.setAgno(fecha.getAgno()+1);
						}
					}
				}
			}	
		}
		
		return fecha.toString();
	}

	//Indica si la fecha dada como String es una fecha valida
	public static boolean fechaValida(String fecha){
		String [] datos=fecha.split(" ");
		
		if(datos.length!=2) return false;
		
		String [] datos1=datos[0].split("/");
		String [] datos2=datos[1].split(":");
		
		if(datos1.length!=3 && datos2.length!=3) return false;
		
		if(!isInteger(datos1[0]) && !isInteger(datos1[1]) && !isInteger(datos1[2])) return false;
		if(!isInteger(datos2[0]) && !isInteger(datos2[1]) && !isInteger(datos2[2])) return false;
		
		int dia=Integer.valueOf(datos1[0]);
		int mes=Integer.valueOf(datos1[1]);
		int agno=Integer.valueOf(datos1[2]);
		int hora=Integer.valueOf(datos2[0]);
		int min=Integer.valueOf(datos2[1]);
		int seg=Integer.valueOf(datos2[2]);
		
		if(seg!=0 && seg!=30) return false;
		if(min<0 || min>59) return false;
		if(hora<0 || hora>23) return false;
		
		if(agno<0) return false;
		if(mes<0 || mes>12) return false;
		
		if(dia<1) return false;
		
		//Febrero (bisiesto y no bisiesto)
		if(mes==2 && (agno%4==0 && dia>29 || agno%4!=0 && dia>28)) return false;
		//Abril, junio, septiembre y noviembre
		if((mes==4 || mes==6 || mes==9 || mes==11) && dia>30) return false;
		//El resto de meses
		if(dia>31) return false;
		
		return true;
	}
	
	//Indica si el string dado es un patron valido
	public static boolean patronCorrecto(String patron){
		String [] datos=patron.split(" ");
		
		for(int i=0; i<datos.length; i++)
			if(!isInteger(datos[i])) return false;
		
		return true;
	}
	
	//Indica si el String dado es un numero entero
	public static boolean isInteger(String s) {
	    try {Integer.parseInt(s);}
	    catch(NumberFormatException e){return false;}
	    return true;
	}
	
	//Indica si el String dado es un numero real
	public static boolean isFloat(String s) {
	    try {Float.parseFloat(s);}
	    catch(NumberFormatException e){return false;}
	    return true;
	}

}