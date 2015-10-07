package cliente;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.SwingUtilities;

public class Cliente {
	
	private final int PUERTO = 9000;
	private final String HOST_REMOTO = "127.0.0.1";
	
	Socket cliente;
	ObjectInputStream entrada;
	ObjectOutputStream salida;
	String mensaje = "";
	Scanner teclado;
	
	public void Cliente(){
		teclado = new Scanner(System.in);
	}
	
	public void ejecutarCliente(){
		try {
			conectarAlServidor();
			obtenerFlujos();
			procesarConexion();
		} catch (EOFException excepcionEOF) {
			mostrarMensaje("\nCliente termino la conexion...");
		} catch (IOException excepcionES) {
			excepcionES.printStackTrace();
		} finally{
			cerrarConexion();
		}
	}
	
	private void conectarAlServidor() throws IOException{
		mostrarMensaje("Intentando conectar con el servidor...\n");
		cliente = new Socket(HOST_REMOTO, PUERTO);
		mostrarMensaje("Conectado a: "+ cliente.getInetAddress().getHostName());
	}
	
	private void obtenerFlujos() throws IOException{
		salida = new ObjectOutputStream(cliente.getOutputStream());
		salida.flush();
		entrada = new ObjectInputStream(cliente.getInputStream());
		mostrarMensaje( "\nSe obtuvieron los flujos de E/S\n" );
	}
	
	private void procesarConexion() throws IOException{
		
		do {
			try {
				mensaje = (String) entrada.readObject();
				mostrarMensaje("\n"+mensaje);
				if (mensaje.equals("true")) {
					mostrarMensaje("\nGANASTE EL JUEGO!!!\n\n");
				}else{
					String fijas = mensaje.substring(0,1);
					String picas = mensaje.substring(1);
					System.out.println("\nIntente nuevamente...");
					System.out.println("\nFijas: "+fijas);
					System.out.println("\nPicas: "+picas);
					ingresarNumero();
				}
			} catch (ClassNotFoundException excepcionClaseNoEncontrada) {
				mostrarMensaje("\nSe recibio un tipo de objeto desconocido...");
			}			
		} while (!mensaje.equals("SERVIDOR>>> TERMINAR"));
		
	}
	
	private void cerrarConexion(){
		mostrarMensaje("\nTerminando la conexion...\n");
		
		try {
			salida.close();
			entrada.close();
			cliente.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void ingresarNumero(){    	
    	
    	Scanner entrada = new Scanner(System.in);
    	
    	System.out.print("\nIngrese un número de 4 dígitos: ");
		int numero = entrada.nextInt();
		
		while (!(numero>=1000 && numero<=9999)) {
			System.out.print("\nIngrese un número de 4 dígitos: ");
			numero = entrada.nextInt();
		}
		enviarNumero(numero);
    }
	
	private void enviarNumero(int numero){
		try {
			salida.writeObject(String.valueOf(numero));
			salida.flush();
			mostrarMensaje("\nNumero enviado");
		} catch (IOException e) {
			System.out.println("\nError al escribir el objeto...");
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

}
