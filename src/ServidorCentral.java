import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * 
 * @author juanr
 *
 */
public class ServidorCentral {

	private JFrame frame;
	private JLabel lblTit2;
	private JLabel lblTit3;
	private JLabel lblEntradasDispo;
	private JLabel lblEntradasCompradas;
	private JLabel lblFacturacion;
	private JLabel lblNewLabel;
	private static int entradasDispo = 100;
	private String mensajeCliente;
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private static String entradasCompradas;
	private int facturacion = 0;
	// private int entradasCompradas = 0;

	/**
	 * Metodo que inicia la vista.
	 * 
	 * @param args para los argumentos de la línea de comandos.
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				ServidorCentral window = new ServidorCentral();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws IOException esto si ocurre un error de entrada/salida.
	 * @wbp.parser.entryPoint
	 */
	public ServidorCentral() throws IOException {
		Thread thread = new Thread(() -> {
			try {
				startServer();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		thread.start();
		initialize();
	}

	/**
	 * Este metodo inicia el servidor y conecta con los clientes.
	 * 
	 * @throws IOException esto si ocurre un error de entrada/salida.
	 */
	private void startServer() throws IOException {
		try {
			ServerSocket serverSocket = new ServerSocket(12345);
			while (true) {
				Socket socket = serverSocket.accept();
				Thread clientThread = new Thread(() -> {
					try {
						clienteServer(socket);

					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				clientThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Este metodo recibe el mensaje que le envia el cliente con la informacion
	 * necesaria.
	 * 
	 * @param socket Es el socket de la conexion con el cliente, para recibir y
	 *               enviar mensajes.
	 * @throws IOException esto si ocurre un error de entrada/salida.
	 */
	private void clienteServer(Socket socket) throws IOException {
		try {
			while (true) {
				dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataInputStream = new DataInputStream(socket.getInputStream());

				dataOutputStream.writeInt(entradasDispo);
				dataOutputStream.flush();

				String mensajeCliente = dataInputStream.readUTF();
				System.out.println(mensajeCliente);
				actualizarServidor(mensajeCliente);
			}

		} catch (IOException ex) {
			System.err.println("Error al procesar la conexión del cliente: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			System.out.println("Socket cerrado");
		}
	}

	/**
	 * Este metodo actualiza el servidor cada que se realiza una nueva compra desde
	 * el cliente.
	 * 
	 * @param mensajeString Es el mensaje que recibimos del cliente, dependiendo del
	 *                      mismo seguira la logica del metodo para ver que entrad y
	 *                      cuantas compro.
	 */
	private void actualizarServidor(String mensajeString) {
		String[] partes = mensajeString.split(",");
		String tipoEntrada = partes[0];
		int entradasCompradas2 = Integer.parseInt(partes[1]);

		entradasCompradas = String.valueOf(entradasCompradas2 + Integer.parseInt(lblEntradasCompradas.getText()));
		lblEntradasDispo.setText(String.valueOf(Integer.parseInt(lblEntradasDispo.getText()) - entradasCompradas2));
		lblEntradasCompradas.setText(entradasCompradas);
		switch (tipoEntrada) {
		case "1":
			facturacion = (5 * entradasCompradas2) + facturacion;
			System.out.println(facturacion);
			lblFacturacion.setText(String.valueOf(facturacion));
			break;
		case "2":
			facturacion = (10 * entradasCompradas2) + facturacion;
			System.out.println(facturacion);
			lblFacturacion.setText(String.valueOf(facturacion));
			break;
		case "3":
			facturacion = (15 * entradasCompradas2) + facturacion;
			System.out.println(facturacion);
			lblFacturacion.setText(String.valueOf(facturacion));
			break;
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException esto si ocurre un error de entrada/salida.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() throws IOException {

		frame = new JFrame();
		frame.setBounds(100, 100, 600, 413);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblTit1 = new JLabel("Entradas Disponibles");
		lblTit1.setBounds(215, 24, 178, 14);
		frame.getContentPane().add(lblTit1);

		lblTit2 = new JLabel("Entradas Compradas");
		lblTit2.setBounds(215, 129, 265, 14);
		frame.getContentPane().add(lblTit2);

		lblTit3 = new JLabel("Facturación");
		lblTit3.setBounds(233, 253, 226, 14);
		frame.getContentPane().add(lblTit3);

		lblEntradasDispo = new JLabel(String.valueOf(entradasDispo));
		lblEntradasDispo.setBounds(255, 49, 23, 14);
		frame.getContentPane().add(lblEntradasDispo);

		lblEntradasCompradas = new JLabel("0");
		lblEntradasCompradas.setBounds(263, 154, 15, 14);
		frame.getContentPane().add(lblEntradasCompradas);

		lblFacturacion = new JLabel("0");
		lblFacturacion.setBounds(243, 278, 36, 14);
		frame.getContentPane().add(lblFacturacion);

		lblNewLabel = new JLabel("€");
		lblNewLabel.setBounds(277, 278, 49, 14);
		frame.getContentPane().add(lblNewLabel);
	}

}