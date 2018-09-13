package tfg.main.control;

import java.util.Scanner;

import tfg.main.logica.*;;


public class Controlador {

	public Controlador(Programa p, java.util.Scanner in){
		cin=in;
		programa=p;
	}
	
	//Función que controla el desarrollo de la partida.
	public void run(){
		EnumFuncion valor;
		Funcion func;
		int opcion;
		salir=false;
		
		while(!salir){
			System.out.print("Enter option: ");
			opcion=cin.nextInt();
			
			//Distincion de casos con las funciones del programa			
			if(opcion==0) requestSalir();
			else if(opcion==1) programa.listaArboles();
			else if(opcion==2){
				valor=EnumFuncion.FC1;
				func=pedirDatos(valor);
				if(func!=null) programa.ejecutarFuncion(func,1);
			}
			else if(opcion==3){
				valor=EnumFuncion.FC2;
				func=pedirDatos(valor);
				if(func!=null) programa.ejecutarFuncion(func,1);
			}
			else if(opcion==4){
				valor=EnumFuncion.FC3;
				func=pedirDatos(valor);
				if(func!=null) programa.ejecutarFuncion(func,1);
			}
			else if(opcion==5){
				valor=EnumFuncion.FC4;
				func=pedirDatos(valor);
				if(func!=null) programa.ejecutarFuncion(func,1);
			}
			else if(opcion==6){
				valor=EnumFuncion.FC5;
				func=pedirDatos(valor);
				if(func!=null) programa.ejecutarFuncion(func,1);
			}
			else if(opcion==7){
				valor=EnumFuncion.FC6;
				func=pedirDatos(valor);
				if(func!=null) programa.ejecutarFuncion(func,1);
			}
			else if(opcion==8){
				valor=EnumFuncion.FC7;
				func=pedirDatos(valor);
				if(func!=null) programa.ejecutarFuncion(func,1);
			}
			else if(opcion==9){
				valor=EnumFuncion.FC8;
				func=pedirDatos(valor);
				if(func!=null) programa.ejecutarFuncion(func,1);
			}
			else if(opcion==10){
				valor=EnumFuncion.FC9;
				func=pedirDatos(valor);
				if(func!=null) programa.ejecutarFuncion(func,1);
			}
			else{
				System.out.println("Incorrect option\n");
				programa.reset();
			}
		}
	}
	
	//Setters y getters	
	public Programa getPrograma(){
		return programa;
	}
	
	public Scanner getScanner(){
		return cin;
	}
	
	public void setPrograma(Programa programa){
		this.programa=programa;
	}
	
	public void requestSalir(){
		this.salir=true;
	}
	
	//Método que distingue para cada funcion, los datos que se piden al usuario
	public Funcion pedirDatos(EnumFuncion valor){
		Arbol arbolA,arbolB;
		String arbol1, arbol2;
		String ini, fin, inf, patron;
		Fecha fechaIni, fechaFin;
		int ventana, mismatches, error;
		float eps,cantidad,tol;
		boolean saltos;
		
		switch (valor){
		//Funcion para mostrar los datos de un arbol
		case FC1: 
			System.out.print("Enter the tree to show: ");
			arbol1=cin.next();
			System.out.print("Enter start date: ");
			cin.nextLine();
			ini=cin.nextLine();
			System.out.print("Enter end date: ");
			fin=cin.nextLine();

			if(Utils.fechaValida(ini) && Utils.fechaValida(fin)){
				fechaIni=new Fecha(ini);
				fechaFin=new Fecha(fin);
			}
			else{
				programa.error("");
				return null;
			}			
			
			arbolA=programa.existeArbol(arbol1, fechaIni, fechaFin);
			if(arbolA==null){
				programa.error("");
				return null;
			}
			
			return new FuncionDatos(arbolA, fechaIni, fechaFin);
		//Funcion para buscar intervalos homogeneos en un arbol
		case FC2: 
			System.out.print("Enter the tree on which you want to calculate the interval: ");
			arbol1=cin.next();
			System.out.print("Enter the length of the interval: ");
			inf=cin.next();
			
			if (inf.equals("INF"))	ventana=Integer.MAX_VALUE;
			else  ventana=Integer.valueOf(inf);

			System.out.print("Enter the maximum difference that can exist from one data to the next: ");
			eps=cin.nextFloat();
			System.out.print("Enter the number of mismatches that are allowed: ");
			error=cin.nextInt();
			System.out.print("Enter start date: ");
			cin.nextLine();
			ini=cin.nextLine();
			System.out.print("Enter end date: ");
			fin=cin.nextLine();
			
			if(Utils.fechaValida(ini) && Utils.fechaValida(fin)){
				fechaIni=new Fecha(ini);
				fechaFin=new Fecha(fin);
			}
			else {
				programa.error("");
				return null;
			}
			
			arbolA=programa.existeArbol(arbol1, fechaIni, fechaFin);
			if(arbolA==null || ventana<=0 || eps<=0 || error<0 || error>ventana/2){
				programa.error("");
				return null;
			}
			
			return new FuncionIntervalo(arbolA,ventana,eps,error,fechaIni,fechaFin);
		//Funcion para comparar los intervalos homogeneos en dos arboles	
		case FC3: 
			System.out.print("Enter the tree 1: ");
			arbol1=cin.next();
			System.out.print("Enter the tree 2: ");
			arbol2=cin.next();
			System.out.print("Enter the length of the interval: ");
			inf=cin.next();
			
			if (inf.equals("INF"))	ventana=Integer.MAX_VALUE;
			else  ventana=Integer.valueOf(inf);
			
			System.out.print("Enter the maximum difference that will serve as control: ");
			eps=cin.nextFloat();
			System.out.print("Are large jumps allowed (1/0)? ");
			if(cin.nextInt()==1) saltos=true;
			else saltos=false;
			System.out.print("Enter the number of mismatches that are allowed: ");
			error=cin.nextInt();
			System.out.print("Enter start date: ");
			cin.nextLine();
			ini=cin.nextLine();
			System.out.print("Enter end date: ");
			fin=cin.nextLine();
			
			if(Utils.fechaValida(ini) && Utils.fechaValida(fin)){
				fechaIni=new Fecha(ini);
				fechaFin=new Fecha(fin);
			}
			else {
				programa.error("");
				return null;
			}
			
			arbolA=programa.existeArbol(arbol1, fechaIni, fechaFin);
			arbolB=programa.existeArbol(arbol2, fechaIni, fechaFin);
			if(arbol1.equals(arbol2) || arbolA==null || arbolB==null || ventana<=0 || eps<=0 || error<0 || error>ventana/2){
				programa.error("");
				return null;
			}
			
			return new FuncionComparar(arbolA,arbolB,ventana,eps,saltos,error,fechaIni,fechaFin);
		//Funcion para buscar los intervalos discrepantes entre dos arboles	
		case FC4: 
			System.out.print("Enter the tree 1: ");
			arbol1=cin.next();
			System.out.print("Enter the tree 2: ");
			arbol2=cin.next();
			System.out.print("Enter the length of the interval: ");
			inf=cin.next();
			if (inf.equals("INF")) ventana=Integer.MAX_VALUE;
			else ventana=Integer.valueOf(inf);
			System.out.print("Enter the number of mismatches that are allowed: ");
			error=cin.nextInt();
			System.out.print("Enter start date: ");
			cin.nextLine();
			ini=cin.nextLine();
			System.out.print("Enter end date: ");
			fin=cin.nextLine();

			if(Utils.fechaValida(ini) && Utils.fechaValida(fin)){
				fechaIni=new Fecha(ini);
				fechaFin=new Fecha(fin);
			}
			else {
				programa.error("");
				return null;
			}
			
			arbolA=programa.existeArbol(arbol1, fechaIni, fechaFin);
			arbolB=programa.existeArbol(arbol2, fechaIni, fechaFin);
			if(arbol1.equals(arbol2) || arbolA==null || arbolB==null || ventana<=0 || error<0 || error>ventana/2){
				programa.error("");
				return null;
			}
			
			return new FuncionDiscrepancia(arbolA,arbolB,ventana,error,fechaIni,fechaFin);
		//Funcion para estudiar el crecimiento de un arbol	
		case FC5:
			System.out.print("Enter the tree on which you want to calculate the trunk: ");
			arbol1=cin.next();
			System.out.print("Enter the minimum growth size of the trunk in one day: ");
			inf=cin.next();
			if (inf.equals("INF")){
				cantidad=Float.MAX_VALUE;
			}
			else cantidad=Float.valueOf(inf);
			System.out.print("Enter start date: ");
			cin.nextLine();
			ini=cin.nextLine();
			System.out.print("Enter end date: ");
			fin=cin.nextLine();

			if(Utils.fechaValida(ini) && Utils.fechaValida(fin)){
				fechaIni=new Fecha(ini);
				fechaFin=new Fecha(fin);
			}
			else {
				programa.error("");
				return null;
			}
			
			arbolA=programa.existeArbol(arbol1, fechaIni, fechaFin);
			if(arbolA==null || cantidad<0){
				programa.error("");
				return null;
			}
			
			return new FuncionTronco(arbolA, cantidad, fechaIni, fechaFin);		
		//Funcion para comparar el crecimiento en dos arboles	
		case FC6:
			System.out.print("Enter the tree 1: ");
			arbol1=cin.next();
			System.out.print("Enter the tree 2: ");
			arbol2=cin.next();
			System.out.print("Enter start date: ");
			cin.nextLine();
			ini=cin.nextLine();
			System.out.print("Enter end date: ");
			fin=cin.nextLine();

			if(Utils.fechaValida(ini) && Utils.fechaValida(fin)){
				fechaIni=new Fecha(ini);
				fechaFin=new Fecha(fin);
			}
			else {
				programa.error("");
				return null;
			}
			
			arbolA=programa.existeArbol(arbol1, fechaIni, fechaFin);
			arbolB=programa.existeArbol(arbol2, fechaIni, fechaFin);
			if(arbolA==null || arbolB==null){
				programa.error("");
				return null;
			}
			
			return new FuncionCompararTronco(arbolA, arbolB, fechaIni, fechaFin);	
		//Funcion para estudiar la contraccion de un arbol	
		case FC7:
			System.out.print("Enter the tree on which you want to calculate the contraction: ");
			arbol1=cin.next();
			System.out.print("Enter the minimum size of the contraction: ");
			inf=cin.next();
			if (inf.equals("INF")){
				cantidad=Float.MAX_VALUE;
			}
			else cantidad=Float.valueOf(inf);
			System.out.print("Enter start date: ");
			cin.nextLine();
			ini=cin.nextLine();
			System.out.print("Enter end date: ");
			fin=cin.nextLine();

			if(Utils.fechaValida(ini) && Utils.fechaValida(fin)){
				fechaIni=new Fecha(ini);
				fechaFin=new Fecha(fin);
			}
			else {
				programa.error("");
				return null;
			}
			
			arbolA=programa.existeArbol(arbol1, fechaIni, fechaFin);
			if(arbolA==null || cantidad<0 || fechaFin.mayor(fechaIni)!=1){
				programa.error("");
				return null;
			}
			
			return new FuncionContraccion(arbolA, cantidad, fechaIni, fechaFin);		
		//Funcion para estudiar las contracciones en dos arboles
		case FC8:
			System.out.print("Enter the tree 1: ");
			arbol1=cin.next();
			System.out.print("Enter the tree 2: ");
			arbol2=cin.next();
			System.out.print("Enter start date: ");
			cin.nextLine();
			ini=cin.nextLine();
			System.out.print("Enter end date: ");
			fin=cin.nextLine();

			if(Utils.fechaValida(ini) && Utils.fechaValida(fin)){
				fechaIni=new Fecha(ini);
				fechaFin=new Fecha(fin);
			}
			else {
				programa.error("");
				return null;
			}
			
			arbolA=programa.existeArbol(arbol1, fechaIni, fechaFin);
			arbolB=programa.existeArbol(arbol2, fechaIni, fechaFin);
			if(arbolA==null || arbolB==null){
				programa.error("");
				return null;
			}
			
			return new FuncionCompararContraccion(arbolA, arbolB, fechaIni, fechaFin);	
		//Funcion para buscar patrones en un arbol
		default:
			System.out.print("Enter the tree on which you want to calculate the pattern: ");
			arbol1=cin.next();
			System.out.print("Enter the pattern (whole numbers separated by spaces): ");
			cin.nextLine();
			patron=cin.nextLine();
			System.out.print("Enter the value by which the data will be divided: ");
			eps=cin.nextFloat();
			System.out.print("Enter the tolerance (from 0 to 0.5): ");
			tol=cin.nextFloat();
			System.out.print("Enter the number of mismatches allowed: ");
			mismatches=cin.nextInt();
			System.out.print("Enter start date: ");
			cin.nextLine();
			ini=cin.nextLine();
			System.out.print("Enter end date: ");
			fin=cin.nextLine();
			
			if(Utils.fechaValida(ini) && Utils.fechaValida(fin)){
				fechaIni=new Fecha(ini);
				fechaFin=new Fecha(fin);
			}
			else {
				programa.error("");
				return null;
			}
			
			arbolA=programa.existeArbol(arbol1, fechaIni, fechaFin);
			if(arbolA==null || tol<0 || tol>0.5 || eps==0 || !Utils.patronCorrecto(patron) 
					|| mismatches<0 || mismatches>(patron.length()/2)){
				programa.error("");
				return null;
			}
			return new FuncionPatron(arbolA, patron, eps, tol, EnumPatron.BoyMor, mismatches, fechaIni, fechaFin);
		}
	}
	
	//Atributos
	private boolean salir;
	private Scanner cin;
	private Programa programa;
}
