import javax.swing.*;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author juanr
 *
 */
public class Cliente {

    private JFrame frame;
    private JTextField textField;
    private Timer timer;
    private int entradasDisponibles;
    private String tipoEntrada;
    private int entradasCompradas;
    private String mensajeString;
    private Socket socket;
    private BufferedReader bufferedReader;
    private OutputStream outputStream;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private static String nombreEntrada;
    private static double precioEntrada;
    private static double precioTotal;
    private static int cantidad;
    
    /**
	 * Launch the application.
	 */
    /**
     * Metodo que inicia la vista.
     * @param args para los argumentos de la línea de comandos.
     */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cliente window = new Cliente();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Este metodo llama la funcion que crea todo.
	 * @throws IOException esto si ocurre un error de entrada/salida.
     * @throws UnknownHostException esto si no se puede determinar el host del servidor.
	 */
	public Cliente() throws UnknownHostException, IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException
     * @throws UnknownHostException 
	 */
	/**
	 * Este metodo es el centro de todo el codigo de cliente, crea el socket, se
	 * conecta con el servidor, recibe y envia informacion, toma accion mediante que
	 * botones, o que se seleccione en el programa (entradas).
	 * 
	 * @throws IOException esto si ocurre un error de entrada/salida.
	 */
	
    private void initialize() throws IOException{
    	
    	try {
            socket = new Socket("localhost", 12345);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            entradasDisponibles = dataInputStream.readInt();
            socket.setSoTimeout(5000);

        } catch (IOException ex) {
            ex.printStackTrace();
        }


        frame = new JFrame();
        frame.setBounds(100, 100, 599, 413);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton btnEntradasDispo = new JButton("Ver entradas disponibles");
        btnEntradasDispo.setBounds(10, 11, 158, 23);
        frame.getContentPane().add(btnEntradasDispo);

        textField = new JTextField();
        textField.setBounds(229, 115, 96, 20);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JButton btnGeneral = new JButton("General");
        btnGeneral.setBounds(79, 186, 89, 23);
        frame.getContentPane().add(btnGeneral);
        
        JButton btnVip = new JButton("VIP");
		btnVip.setBounds(229, 186, 89, 23);
		frame.getContentPane().add(btnVip);
		
		JButton btnUltraVip = new JButton("Ultra VIP");
		btnUltraVip.setBounds(373, 186, 89, 23);
		frame.getContentPane().add(btnUltraVip);

        JButton btnConfirmacion = new JButton("Confirmar compra");
        btnConfirmacion.setEnabled(false);
        btnConfirmacion.setBounds(207, 288, 134, 23);
        frame.getContentPane().add(btnConfirmacion);
        
        JLabel lblNewLabel = new JLabel("Ingrese la cantida de entradas (1-3):");
		lblNewLabel.setBounds(190, 90, 195, 14);
		frame.getContentPane().add(lblNewLabel);

        btnGeneral.addActionListener(e -> {
        	 try {
                 int numeroIngresado = Integer.parseInt(textField.getText());
                 if (numeroIngresado >= 1 && numeroIngresado <= 3) {
                     if (numeroIngresado <= entradasDisponibles) {
                    	 tipoEntrada = "1";
                    	 nombreEntrada = "Entrada General";
                    	 precioEntrada = 5.0;
                    	 entradasCompradas = numeroIngresado;
                    	 cantidad = numeroIngresado;
                         entradasDisponibles -= numeroIngresado;
                         btnConfirmacion.setEnabled(true);
                         iniciarTemporizador();
                     } else {
                         JOptionPane.showMessageDialog(frame, "No hay suficientes entradas disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
                     }
                 } else {
                     JOptionPane.showMessageDialog(frame, "Ingresa un número válido (1-3) en el campo.", "Error", JOptionPane.ERROR_MESSAGE);
                 }
             } catch (NumberFormatException ex) {
                 JOptionPane.showMessageDialog(frame, "Ingresa un número válido en el campo.", "Error", JOptionPane.ERROR_MESSAGE);
             }
        });
        
        btnVip.addActionListener(e -> {
        	try {
                int numeroIngresado = Integer.parseInt(textField.getText());
                if (numeroIngresado >= 1 && numeroIngresado <= 3) {
                    if (numeroIngresado <= entradasDisponibles) {
                    	tipoEntrada = "2";
                   	 	nombreEntrada = "Entrada VIP";
                   	 	precioEntrada = 10.0;
                    	entradasCompradas = numeroIngresado;
                    	cantidad = numeroIngresado;
                        entradasDisponibles -= numeroIngresado;
                        btnConfirmacion.setEnabled(true);
                        iniciarTemporizador();
                    } else {
                        JOptionPane.showMessageDialog(frame, "No hay suficientes entradas disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Ingresa un número válido (1-3) en el campo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Ingresa un número válido en el campo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
		});
        
        btnUltraVip.addActionListener(e -> {
        	try {
                int numeroIngresado = Integer.parseInt(textField.getText());
                if (numeroIngresado >= 1 && numeroIngresado <= 3) {
                    if (numeroIngresado <= entradasDisponibles) {
                    	tipoEntrada = "3";
                   	 	nombreEntrada = "Entrada Ultra VIP";
                   	 	precioEntrada = 15.0;
                    	entradasCompradas = numeroIngresado;
                    	cantidad = numeroIngresado;
                        entradasDisponibles -= numeroIngresado;
                        btnConfirmacion.setEnabled(true);
                        iniciarTemporizador();
                    } else {
                        JOptionPane.showMessageDialog(frame, "No hay suficientes entradas disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Ingresa un número válido (1-3) en el campo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Ingresa un número válido en el campo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
		});

        btnConfirmacion.addActionListener(e -> {
        	try {
        		String mensaje = tipoEntrada + "," + entradasCompradas;
        		System.out.println(mensaje);
        		dataOutputStream.writeUTF(mensaje);
                dataOutputStream.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	detenerTemporizador();
            JOptionPane.showMessageDialog(frame, "Compra confirmada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            textField.setText("");
            btnConfirmacion.setEnabled(false);
            Map<String, Object> parametro = new HashMap<>();
            precioTotal = (precioEntrada*cantidad);
			parametro.put("TipoEntrada", nombreEntrada);
			parametro.put("Precio", precioTotal);
			parametro.put("Cantidad", cantidad);
			
			System.out.println("TipoEntrada " + nombreEntrada);
			System.out.println("Precio" + precioTotal);
			System.out.println("Cantidad"+ cantidad);
			
			try {
				JasperReport jasperReport = JasperCompileManager.compileReport("jasper\\Blank_A4.jrxml");
				JasperPrint informePrint = JasperFillManager.fillReport(jasperReport, parametro, new JREmptyDataSource());
				JasperViewer.viewReport(informePrint);
			} catch (JRException e1) {	
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });

        timer = new Timer(120000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Tiempo agotado. La vista se cerrará.", "Tiempo Expirado", JOptionPane.WARNING_MESSAGE);
                frame.dispose();
            }
        });
        
        btnEntradasDispo.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Quedan " + entradasDisponibles + " entradas disponibles.", "Entradas Disponibles", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    /**
     * Este metodo inica el temporizador del programa.
     */
    private void iniciarTemporizador() {
        timer.start();
    }

    /**
     * Este metodo detiene el temporizador del programa.
     */
    private void detenerTemporizador() {
        timer.stop();
    }
}