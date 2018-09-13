package tfg.main.control;

import java.util.concurrent.TimeUnit;

import tfg.main.logica.Arbol;
import tfg.main.logica.EnumPatron;
import tfg.main.logica.FuncionPatron;

public class ControlTiempos {
	
	public ControlTiempos(int rep, Arbol arbol, String patron, float tol, float div, int mismatches){
		repeticiones=rep;
		this.arbol=arbol;
		this.patron=patron;
		this.tol=tol;
		this.div=div;
		this.mismatches=mismatches;
	}
	
	//Escribe por pantalla el tiempo para cada algoritmo
	public void tiempos(){
		
		//Algoritmo de Fuerza Bruta
		String codigo1="brute force code";
		tiempoCodigoFB(new FuncionPatron(arbol,patron,div,tol,EnumPatron.FuerBrut,mismatches,arbol.getIni(),arbol.getFin()),codigo1);
		System.out.println();
		
		//Algoritmo Boyer Moore
		String codigo2="boyer moore code";
		tiempoCodigoBM(new FuncionPatron(arbol,patron,div,tol,EnumPatron.BoyMor,mismatches,arbol.getIni(),arbol.getFin()),codigo2);
		System.out.println();
		
		String[] aux=patron.split(" ");
		
		//Limites del algoritmo Shift Add
		if(mismatches<2 && aux.length<16 || mismatches==2 && aux.length<10 ||mismatches==3 && aux.length<8){
				String codigo3="shift-add code";
			tiempoCodigoShA(new FuncionPatron(arbol,patron,div,tol,EnumPatron.ShAdd,mismatches,arbol.getIni(),arbol.getFin()),codigo3);
			System.out.println();
		}
		else
			System.out.println("Algorithm Shift Add overflows with the data given by the user");
	}
	
	private void tiempoCodigoFB(FuncionPatron func, String codigo){
		//Version a lo bruto
		tiempoAux=0;
		tiempoInicio=System.nanoTime();
				
		for(int i=0; i<repeticiones; i++){
			func.fuerzaBruta();
			tiempoAux+=func.getTiempo();
		}
			 
		// Resta el tiempo de inicio al actual dando como resultado el tiempo usado
		totalTiempo=System.nanoTime()-tiempoInicio;
		totalTiempo=totalTiempo/repeticiones;
		tiempoAux=tiempoAux/repeticiones;
				
		TimeUnit milisegundos=TimeUnit.NANOSECONDS;
			 
		System.out.println("Delayed time " + codigo +": " + milisegundos.toMicros(totalTiempo) + " microseconds.");
		func.limpiarDatos();
		
		//Version optimizada
		tiempoAux=0;
		tiempoInicio=System.nanoTime();
		
		for(int i=0; i<repeticiones; i++){
			func.fuerzaBrutaOpt();
			tiempoAux+=func.getTiempo();
		}
	 
		// Resta el tiempo de inicio al actual dando como resultado el tiempo usado
		totalTiempo=System.nanoTime()-tiempoInicio;
		totalTiempo=totalTiempo/repeticiones;
		tiempoAux=tiempoAux/repeticiones;
	 
		System.out.println("Delayed time " + codigo + " opt" +": " + milisegundos.toMicros(totalTiempo) + " microseconds.");
		func.limpiarDatos();
	}
	
	//Calcula el tiempo del algoritmo Booyer Moore tanto con preprocesado como sin el
	private void tiempoCodigoBM(FuncionPatron func, String codigo){
		//Version con tabla hash
		tiempoAux=0;
		tiempoInicio = System.nanoTime();
			
		for(int i=0; i<repeticiones; i++){
			func.BoyerMoore();
			tiempoAux+=func.getTiempo();
		}
			 
		// Resta el tiempo de inicio al actual dando como resultado el tiempo usado
		totalTiempo = System.nanoTime() - tiempoInicio;
		totalTiempo=totalTiempo/repeticiones;
		tiempoAux=tiempoAux/repeticiones;
				
		TimeUnit milisegundos=TimeUnit.NANOSECONDS;
		 
		System.out.println("Delayed time " + codigo +": " + milisegundos.toMicros(totalTiempo) + " microseconds.");
		System.out.println("Delayed time " + codigo +" without preprocessing: " + milisegundos.toMicros(tiempoAux) + " microseconds.");
		func.limpiarDatos();
		
		//Version con matriz
		tiempoAux=0;
		tiempoInicio = System.nanoTime();
			 
		tiempoInicio = System.nanoTime();
		for(int i=0 ; i<repeticiones ; i++){
			func.BoyerMooreMatriz();
			tiempoAux+=func.getTiempo();
		}
					
		// Resta el tiempo de inicio al actual resultando el tiempo usado
		totalTiempo = System.nanoTime() - tiempoInicio;
		totalTiempo=totalTiempo/repeticiones;
		tiempoAux=tiempoAux/repeticiones;
				
		System.out.println("Delayed time with matrix " + codigo +": " + milisegundos.toMicros(totalTiempo) + " microseconds.");
		System.out.println("Delayed time with matrix " + codigo +" without preprocessing: " + milisegundos.toMicros(tiempoAux) + " microseconds.");
		func.limpiarDatos();
		
		if(mismatches==0 && patron.split(" ").length>1){
			//Version exacta
			tiempoAux=0;
			tiempoInicio = System.nanoTime();
				 
			tiempoInicio = System.nanoTime();
			for(int i=0 ; i<repeticiones ; i++){
				func.BoyerMooreGoodSuffixRule();
				tiempoAux+=func.getTiempo();
			}
						
			// Resta el tiempo de inicio al actual dando como resultado el tiempo usado
			totalTiempo = System.nanoTime() - tiempoInicio;
			totalTiempo=totalTiempo/repeticiones;
			tiempoAux=tiempoAux/repeticiones;
					
			System.out.println("Delayed time with good suffix rule " + codigo +": " + milisegundos.toMicros(totalTiempo) + " microseconds.");
			System.out.println("Delayed time with good suffix rule " + codigo +" without preprocessing: " + milisegundos.toMicros(tiempoAux) + " microseconds.");
			func.limpiarDatos();
		}
	}

	//Calcula el tiempo del algoritmo Shift Add tanto con preprocesado como sin el
	private void tiempoCodigoShA(FuncionPatron func, String codigo){
		//Version con tabla hash
		tiempoAux=0;
		tiempoInicio = System.nanoTime();
		
		for(int i=0 ; i<repeticiones ; i++){
			func.ShiftAdd();
			tiempoAux+=func.getTiempo();
		}
	 
		// Resta el tiempo de inicio al actual dando como resultado el tiempo usado
		totalTiempo = System.nanoTime() - tiempoInicio;
		totalTiempo=totalTiempo/repeticiones;
		tiempoAux=tiempoAux/repeticiones;
		
		TimeUnit milisegundos=TimeUnit.NANOSECONDS;
	 
		System.out.println("Delayed time " + codigo +": " + milisegundos.toMicros(totalTiempo) + " microseconds.");
		System.out.println("Delayed time " + codigo +" without preprocessing: " + milisegundos.toMicros(tiempoAux) + " microseconds.");
		func.limpiarDatos();
		
		//Version con matriz
		tiempoAux=0;
		tiempoInicio = System.nanoTime();
		 
		tiempoInicio = System.nanoTime();
		for(int i=0 ; i<repeticiones ; i++){
			func.ShiftAddMatriz();
			tiempoAux+=func.getTiempo();
		}
			
		// Resta el tiempo de inicio al actual dando como resultado el tiempo usado
		totalTiempo = System.nanoTime() - tiempoInicio;
		totalTiempo=totalTiempo/repeticiones;
		tiempoAux=tiempoAux/repeticiones;
		
		System.out.println("Delayed time with matrix " + codigo +": " + milisegundos.toMicros(totalTiempo) + " microseconds.");
		System.out.println("Delayed time with matrix " + codigo +" without preprocessing: " + milisegundos.toMicros(tiempoAux) + " microseconds.");
		func.limpiarDatos();
		
	}
	 
	//Atributos de la clase
	private long totalTiempo;
	private long tiempoInicio;
	private long tiempoAux;
	private int repeticiones;

	private Arbol arbol;
	private String patron;
	private float tol;
	private float div;
	private int mismatches;
}
