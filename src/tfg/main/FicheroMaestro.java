package tfg.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import tfg.main.logica.Fecha;
import tfg.main.logica.Utils;

public class FicheroMaestro {
	public FicheroMaestro(String nombre){
		fichero=nombre;
		errores=new Vector<ListaArboles>(10);
	}
	
	public Fecha getIni(){
		return ini;
	}
	
	public Fecha getFin(){
		return fin;
	}
	
	public Vector<ListaArboles> getErrores(){
		return errores;
	}
	
	//Metodo que arrega el fichero que nos dan
	public int arreglarFichero(){
		int arboles=0;
		
		//Se cambian las comas por puntos
		cambiarComasPorPuntos();
		
		try{
			BufferedReader buffer = new BufferedReader(new FileReader(ficheroBien));
			
			String nombres=buffer.readLine();
			String [] nombre=nombres.split("\t");
			arboles=nombre.length-1;
			
			Vector<PrintWriter> escritor=new Vector<PrintWriter>(arboles);
			
			//Se crea un fichero para cada arbol
			for(int i=1; i<=arboles; i++){
				String name=nombre[i]+"Mal.txt";
				escritor.add(new PrintWriter(new FileWriter(name)));
			}
			
			String linea;
			
			//Para cada fichero de arbol, se añade la fecha y el valor del arbol
			while((linea=buffer.readLine())!=null){
				String [] partes=linea.split("\t");
				for(int i=1; i<=arboles; i++){
					escritor.get(i-1).write(partes[0]+"\t");
					escritor.get(i-1).write(partes[i]+"\n");
				}				
			}
			buffer.close();
			
			//Se arregla cada fichero de arbol
			for(int i=0; i<arboles; i++){
				escritor.get(i).close();
				arreglarArbol(nombre[i+1]);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return arboles;
	}
	
	//Metodo que cambia las comas del fichero por puntos (por el formato de los numeros float)
	private void cambiarComasPorPuntos(){
		String [] aux=fichero.split("\\.");
		ficheroBien=aux[0]+"Bien.txt";
	
		try{
			int c;
			BufferedReader buffer = new BufferedReader(new FileReader(fichero));
			PrintWriter output=new PrintWriter(new FileWriter(ficheroBien));
			
			while((c=buffer.read())!=-1){
				if(c==',')
					output.write('.');
				else
					output.write(c);
			}
			
			buffer.close();
			output.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	//Metodo para arreglar cada fichero de arbol
	//Se arreglan los datos de fechas que falten
	//Se arreglan datos erroneos (fallo)
	//Se arreglan valores muy alejados de otros (mas de 100 valores)
	private void arreglarArbol(String arbol){
		int err=0;
		
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(arbol+"Mal.txt"));
			PrintWriter output=new PrintWriter(new FileWriter(arbol+".txt"));
			
			float ant=0;
			String linea1, linea2, fechAnt;
			String error="#¡VALOR!";
			float epsilon=100, acumulado=0;
			
			int cont; //Indica si estamos en 00 o 30 segundos en las medidas
			
			//La primeras dos línea las leo fuera del bucle porque indican la fecha en el fichero
			linea1=buffer.readLine();
			linea2=buffer.readLine();
			String[] partes1=linea1.split("\t");
			String[] partes2=linea2.split("\t");
			Float float1, float2;
			
			//Se mira que no son errores las medidas
			//Si lo son se ponen al valor anterior (en este caso el por defecto: 0)
			if(partes1[1].equals(error)){
				float1=ant;
				err++;
			}
			else{
				float1=Float.valueOf(partes1[1]);
				ant=float1;
			}
			
			if(partes2[1].equals(error)){
				float2=ant;
				err++;
			}
			else{
				float2=Float.valueOf(partes2[1]);
				ant=float2;
			}
			
			//Si son la misma fecha
			if(partes1[0].equals(partes2[0])){
				output.write(partes1[0]+":00" +"\t");
				output.write(float1+"\n");
				
				//Se comprueba que no superan el epsilon permitido
				if(Math.abs(float1-float2)>epsilon){
					acumulado+=float1-float2;
					err++;
				}
					
				output.write(partes2[0]+":30" + "\t");
				output.write((float2+acumulado)+"\n");
				cont=0;
				fechAnt=partes2[0]+":30";
				this.ini=new Fecha(partes1[0]+":00");
			}
			//Si son fechas distintas
			else{
				output.write(partes1[0]+":30" +"\t");
				output.write(float1+"\n");
				
				if(Math.abs(float1-float2)>epsilon){
					acumulado+=float1-float2;
					err++;
				}
					
				output.write(partes2[0]+":00" + "\t");
				output.write((float2+acumulado)+"\n");
				cont=1;
				fechAnt=partes2[0]+":00";
				this.ini=new Fecha(partes1[0]+":30");
			}
			
			String linea;		
			linea=buffer.readLine();
			
			while(linea!=null){
				String[] partes=linea.split("\t");
				Float num;
				
				//Se mira si la medida no es un error
				if(partes[1].equals(error)){
					num=ant;
					err++;
				}
				else
					num=Float.valueOf(partes[1]);
				
				if(cont==0){
					String fecha=partes[0]+":00";
					
					//Se comprueba que las fechas son consecutivas
					if(Utils.siguienteMedida(fechAnt).equals(fecha)){
						output.write(fecha +"\t");
					
						//Se comprueba que no se supera el epsilon permitido entre valores
						if(Math.abs(ant-num)>epsilon){
							acumulado+=ant-num;
							err++;
						}
						
						output.write((num+acumulado)+"\n");
						
						//Se actualizan las variables
						cont++;			
						ant=num;
						fechAnt=fecha;
						linea=buffer.readLine();
					}
					else{//Falta una fecha
							err++;
							fechAnt=Utils.siguienteMedida(fechAnt);
							
							output.write(fechAnt +"\t");		
							output.write((ant+acumulado)+"\n");
						
							cont++;		
						}
				}
				else{ //cont==1
					String fecha=partes[0]+":30";
					
					//Se comprueba que las fechas son consecutivas
					if(Utils.siguienteMedida(fechAnt).equals(fecha)){
						output.write(fecha +"\t");
						
						//Se comprueba que no se supera el epsilon permitido entre valores
						if(Math.abs(ant-num)>epsilon){
							acumulado+=ant-num;
							err++;
						}
							
						output.write((num+acumulado)+"\n");
						
						//Se actualizan las variables
						cont--;
						ant=num;
						fechAnt=fecha;
						linea=buffer.readLine();
					}
					else{ //Falta una fecha
						err++;
						fechAnt=Utils.siguienteMedida(fechAnt);
						
						output.write(fechAnt +"\t");			
						output.write((ant+acumulado)+"\n");
						
						cont--;						
					}
				}
			}
			this.fin=new Fecha(fechAnt);
			
			buffer.close();
			output.close();
			
			float total=Utils.medidasEntreFechas(this.ini, this.fin);
			
			errores.addElement(new ListaArboles(arbol,err,(err/total)*100 ));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//Atributos
	private String fichero;
	private String ficheroBien;
	private Fecha ini;
	private Fecha fin;
	private Vector<ListaArboles> errores;
}