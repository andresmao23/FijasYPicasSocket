package servidor;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.SwingUtilities;

public class Servidor {
	
	private final int PUERTO = 9000;
	private int f, p;
	
	ServerSocket servidor;
	Socket conexion;
	ObjectInputStream entrada;
	ObjectOutputStream salida;
	
	GeneradorAleatorio g;
    private int [] generado;
	int [] array;
	
	public Servidor(){
		array = new int[4];
		g = new GeneradorAleatorio();
    	generado = g.generarNumero();  
    	
    	System.out.println("Numero generado por el servidor:");
        for (int i = 0; i < generado.length; i++) {
			System.out.printf("%-4d",generado[i]);
		}
    	
	}
	
	public void ejecutarServidor(){
		try {
			servidor = new ServerSocket(PUERTO);
			
			while (true) {
				try {
					esperarConexion();
					obtenerFlujos();
					procesarConexion();
				} catch (EOFException excepcionEOF) {
					mostrarMensaje("\nServidor termino la conexion");
				}finally{
					cerrarConexion();
				}
			}
			
		} catch (IOException e) {
			System.out.println("Error del servidor "+e.getMessage());
		}
	}
	
	private void esperarConexion() throws IOException{
		mostrarMensaje("\nEsperando una conexion...\n");
		conexion = servidor.accept();
		mostrarMensaje("Conexion recibida de: "+conexion.getInetAddress().getHostName());
	}
	
	private void obtenerFlujos() throws IOException{
		salida = new ObjectOutputStream(conexion.getOutputStream());
		salida.flush();
		entrada = new ObjectInputStream(conexion.getInputStream());
		mostrarMensaje( "\nSe obtuvieron los flujos de E/S\n" );
	}
	
	private void procesarConexion() throws IOException{
		String mensaje = "Conexion exitosa!!!";
		salida.writeObject("Saludando desde el servidor!!!");
		
		do {
			try {
				mensaje = (String) entrada.readObject();
				int numero = Integer.parseInt(mensaje);
				separarNumero(numero);
				for (int i = 0; i < array.length; i++) {
					System.out.printf("%-4d", array[i]);
				}
				
				if (comparar(array)) {
					salida.writeObject("true");
				}else{					
					System.out.println("\nFijas: "+f);
					System.out.println("\nPicas: "+p);
					salida.writeObject(f+""+p);
				}
								
			} catch (ClassNotFoundException excepcionClaseNoEncontrada) {
				mostrarMensaje("\nSe recibio un tipo de objeto desconocido...");
			}			
		} while (!mensaje.equals("CLIENTE>>> TERMINAR"));
		
	}
	
	private void cerrarConexion(){
		mostrarMensaje("\nTerminando la conexion...\n");
		
		try {
			salida.close();
			entrada.close();
			conexion.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void mostrarMensaje(String mensaje){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(mensaje);
				
			}
		});
	}
	
	 public int[] separarNumero(int numero){		
			array[3] = numero % 10;
			array[2] = numero % 100 / 10;
			array[1] = numero % 1000 / 100;
			array[0] = numero % 10000 / 1000;
			
	    	return array;
	 }
	 
	 public boolean comparar(int [] array){    	
		 int fijas = 0, picas = 0;	
    	for (int i = 0; i < generado.length; i++) {
			for (int j = 0; j < generado.length; j++) {
				if (i == j) {
					if (generado[i] == array[j]) {
						fijas++;
					}
				}else {
					if (generado[i] == array[j]) {
						picas++;
					}			
				}
			}
		}
    	
    	f = fijas;
    	p = picas;
    	
    	if (fijas == 4) {
			return true;
		}
    	
    	return false;
	}
    
    public int[] getGenerado() {
		 return generado;
	}

}
