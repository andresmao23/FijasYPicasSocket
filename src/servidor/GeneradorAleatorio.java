package servidor;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class GeneradorAleatorio {
	
	int numero;
	final int rango = 9;
	Stack <Integer> pila;
	int [] generado;
	
	public GeneradorAleatorio(){
		pila = new Stack <Integer>();
		generado = new int[4];
	}
	
	public int[] generarNumero(){	    
	    
	    for (int i = 0; i < 4 ; i++) {
	      numero = (int) Math.floor(Math.random() * rango +1);//Numeros aleatorios entre 1 y rango(9)
	      while (pila.contains(numero)) {
	        numero = (int) Math.floor(Math.random() * rango +1);//Numeros aleatorios entre 1 y rango(9) sin repetir
	      }
	      pila.push(numero);
	    }
	    //System.out.println("Núm. aleatorios sin repetición (Stack):");
	    //System.out.println(pila.toString());
		
		for (int i = 0; i < pila.size(); i++) {
			generado[i] = pila.get(i);
		}
		
		return generado;
	}
	
	/*public void imprimirArray(){
		for (int i = 0; i < generado.length; i++) {
			System.out.print(generado[i]+" ");
		}
	}*/

}
