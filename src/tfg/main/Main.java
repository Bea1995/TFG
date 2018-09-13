package tfg.main;

import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

import org.apache.commons.cli.*;
import tfg.main.control.*;
import tfg.main.logica.*;
import tfg.main.vistas.*;

public class Main {
	
	@SuppressWarnings("static-access")
	public static void main(String []args) throws IOException{
		Options opciones=new Options();
		CommandLineParser parser = null;
		CommandLine cmdLine = null;
		
		boolean consola=false;
		boolean tiempos=false;
		
		//Hay que añadir un comando en la consola para cambiar el nombre del fichero
		String fichero="MasDatosArboles.txt";
		
		//Se añaden los posibles comandos, su descripción, y se indica si tienen argumentos.
		opciones.addOption("h", "help", false, "Show this help.");
		opciones.addOption(OptionBuilder.withLongOpt("ui")
				.withDescription("Type of interface (console, window, times). By default, console.")
				.hasArg().withArgName("tipo").create("u"));
		opciones.addOption(OptionBuilder.withDescription("File of the program. By default, MasDatosArboles.txt")
				.hasArg().withArgName("fichero").create("f"));
		
		//Se realiza el parseo de los comandos dentro de un try.
		try{
			parser=new BasicParser();
			cmdLine=parser.parse(opciones, args);
			
			//Si el comando es "-h" o "--help", se muestra la ayuda.
			if (cmdLine.hasOption("h")){
				new HelpFormatter().printHelp(Main.class.getCanonicalName(), opciones, true);
				return;
			}
			//Si el comando es "-u" o "--ui" se comprueban los argumentos para ver qué tipo de interfaz mostrar.
			else if(cmdLine.hasOption("u")){
				//Caso de ventana
				if(cmdLine.getOptionValue('u').equalsIgnoreCase("window")){
					if(cmdLine.getArgs().length!=0){
						throw new org.apache.commons.cli.ParseException("Arguments not understood: " + argumentosDeError(cmdLine.getArgs()));
					}
					consola=false;
					tiempos=false;
				}
				//Caso de consola
				else if(cmdLine.getOptionValue('u').equalsIgnoreCase("console")){
					if(cmdLine.getArgs().length!=0){
						throw new org.apache.commons.cli.ParseException("Arguments not understood: " + argumentosDeError(cmdLine.getArgs()));
					}
					consola=true;
					tiempos=false;
				}
				//Caso de tiempos
				else if(cmdLine.getOptionValue('u').equalsIgnoreCase("times")){
					if(cmdLine.getArgs().length!=0){
						throw new org.apache.commons.cli.ParseException("Arguments not understood: " + argumentosDeError(cmdLine.getArgs()));
					}
					tiempos=true;
				}
				//Error
				else{
					throw new org.apache.commons.cli.ParseException("Type '" + cmdLine.getOptionValue('u') + "' incorrect.");
				}
				
			}
			
			//Si el comando es "-f" se iguala la variable fichero al argumento de la opción
			if(cmdLine.hasOption("f")){
				fichero=cmdLine.getOptionValue("f");
				if(cmdLine.getArgs().length!=0){
					throw new org.apache.commons.cli.ParseException("Arguments not understood: " + argumentosDeError(cmdLine.getArgs()));
				}
			}
			else{
				if(cmdLine.getArgs().length!=0){
					throw new org.apache.commons.cli.ParseException("Unrecognized option: " + argumentosDeError(cmdLine.getArgs()));
				}
			}
					
			//Se arregla el fichero
			FicheroMaestro master=new FicheroMaestro(fichero);
			int arboles=master.arreglarFichero();
			
			Vector<ListaArboles> errores=master.getErrores();
			Collections.sort(errores);
			StringBuilder textoIni=new StringBuilder();
			
			textoIni.append("The file is " + fichero + "\n");
			textoIni.append("The number of trees is "+arboles+"\n\n");
			
			for(int i=0; i<errores.size(); i++){
				textoIni.append("The tree " + errores.get(i).getNombre() + " has " + errores.get(i).getNum() + " errors\n");
				textoIni.append("It is " + errores.get(i).getError() + "% of errors with respect to the total data\n\n");
			}
			
			//Se inicia el programa y se llama al controlador pertinente.
	        Programa programa=new Programa(arboles,master.getIni(),master.getFin(),fichero,textoIni.toString());
	        			
	        if(consola && !tiempos){
	        	//Se inicia el modo de consola
	        	Scanner cin= new Scanner(System.in);
	        	Controlador controlador= new Controlador(programa, cin);
	        	new VistaDeConsola(programa);
	        	programa.reset();
			
	        	controlador.run();
	        	cin.close();
	        	System.exit(0);
	        }
	        else if(!tiempos){
	        	//Se inicia el modo de ventana
	        	ControladorVentana ctrl= new ControladorVentana(programa);
	        	new MainWindow(ctrl, programa, errores);
	        	programa.reset();
	        }
	        else{
	        	//Se inicia el modo de tiempos
	        	Fecha ini=master.getIni();
	        	Fecha fin=master.getFin();
	        	Arbol arbol=programa.existeArbol(errores.get(0).getNombre(), ini, fin);
	        	float tol=(float) 0.5;
	        	float div=(float) 1;
	        	String patron="2 -2 2";
	        	int mismatches=0;
	        	
	        	System.out.print("Times for the tree " + arbol.getNombre());
	        	System.out.println(" with the pattern " + patron + " and " + mismatches + " mismatch(es)\n");
	        	
	        	ControlTiempos ctrl=new ControlTiempos(50, arbol, patron, tol, div, mismatches);
	        	
	        	ctrl.tiempos();
	        
	        }
		}
		
		catch (org.apache.commons.cli.ParseException ex){  
			System.err.print("Incorrect use: ");
            System.err.println(ex.getMessage()); 
            System.err.println("Use -h|--help for more details.");  
            System.exit(1);
        }
	}
	
	private static String argumentosDeError(String[] lista){
		StringBuilder cadena= new StringBuilder();
		
		for(int i=0; i<lista.length; i++){
			cadena.append(lista[i]);
			if(i!=lista.length-1) cadena.append(" ");
		}
		
		return cadena.toString();
	}
}
