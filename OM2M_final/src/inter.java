import java.awt.EventQueue;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JLabel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Timer;

import java.util.TimerTask;
import java.awt.event.ActionEvent;



public class inter {
	private final String password="admin:admin";
	private String temper="23";
	String data="";
	String sw="off";
	JFrame frame;
	String line="";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					inter window = new inter();
					window.frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
	}

	/**
	 * Create the application.
	 */
	public inter() {
		try {
			initialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	class get_http extends TimerTask{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			URL url;
			line="";
			try {
				url = new URL("http://140.116.247.73:8181/om2m/gscl/applications/air_conditioner/containers/DATA/contentInstances/latest/content");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET"); 
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestProperty("Authorization", "Basic "+Base64.getEncoder().encodeToString(password.getBytes()));
				conn.connect();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
				String temp="";
				while((temp=br.readLine())!=null)
					line+=temp;
				
	            
	            br.close();
	            
	            String[]str=line.split("\"");
	    		temper=str[15];
	    		sw=str[23];
	    		
	    		System.out.println(temper);
	    		System.out.println(sw);
				
	            //System.out.print("res"+line);
				//System.out.print("response"+conn.getResponseCode());
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws IOException {
		
		
		
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(150, 37, 108, 82);
		frame.getContentPane().add(spinner);
		
		
		temper=spinner.getValue().toString();
		
		spinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				temper=spinner.getValue().toString();
				
				//System.out.print(line);
				data="<obj>" + 
						"<str name=\"type\" val=\"actuator\"/>" + 
						"<str name=\"appId\" val=\"air_conditioner\"/>" + 
						"<str name=\"category\" val=\"temperature \"/>" + 
						"<int name=\"value\" val=\""+temper+"\"/>" + 
						"<int name=\"unit\" val=\"celsius\"/>" + 
						"<str name=\"switch\" val=\""+sw+"\"/>" + 
						"</obj>";
				
				URL url;
				try {
					url = new URL("http://140.116.247.73:8181/om2m/gscl/applications/air_conditioner/containers/DATA/contentInstances");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST"); 
					conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setRequestProperty("Authorization", "Basic "+Base64.getEncoder().encodeToString(password.getBytes()));
					
					
					DataOutputStream dos = null;
					dos = new DataOutputStream(conn.getOutputStream());
					dos.write(data.getBytes(Charset.forName("utf-8")));
					dos.flush();
					
					System.out.print("response"+conn.getResponseCode());
					
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
		});
		
		
		//LB¶}Ãö«ö¶sONOFF
		JLabel lb = new JLabel("New label");
		lb.setBounds(150, 146, 108, 82);
		frame.getContentPane().add(lb);
		
		Icon icon=new ImageIcon("C:/Users/happysorry/Desktop/OM2mfinal/Btn_OFF.png");
		lb.setIcon(icon);
		
		
		//lb.addMouseListener((MouseListener) this);
		lb.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(sw.equals("off")) {
					Icon icon=new ImageIcon("C:/Users/happysorry/Desktop/OM2mfinal/Btn_ON.png");
					lb.setIcon(icon);
					sw="on";
					data="<obj>" + 
							"<str name=\"type\" val=\"actuator\"/>" + 
							"<str name=\"appId\" val=\"air_conditioner\"/>" + 
							"<str name=\"category\" val=\"temperature \"/>" + 
							"<int name=\"value\" val=\""+temper+"\"/>" + 
							"<int name=\"unit\" val=\"celsius\"/>" + 
							"<str name=\"switch\" val=\"on\"/>" + 
							"</obj>";
				}
				else if(sw.equals("on")) {
					Icon icon=new ImageIcon("C:/Users/happysorry/Desktop/OM2mfinal/Btn_OFF.png");
					lb.setIcon(icon);
					sw="off";
					data="<obj>" + 
							"<str name=\"type\" val=\"actuator\"/>" + 
							"<str name=\"appId\" val=\"air_conditioner\"/>" + 
							"<str name=\"category\" val=\"temperature \"/>" + 
							"<int name=\"value\" val=\""+temper+"\"/>" + 
							"<int name=\"unit\" val=\"celsius\"/>" + 
							"<str name=\"switch\" val=\"off\"/>" + 
							"</obj>";
				}
				System.out.print("lb");
				try {
					URL url=new URL("http://140.116.247.73:8181/om2m/gscl/applications/air_conditioner/containers/DATA/contentInstances");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST"); 
					conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setRequestProperty("Authorization", "Basic "+Base64.getEncoder().encodeToString(password.getBytes()));
					
					
					
					DataOutputStream dos = null;
					dos = new DataOutputStream(conn.getOutputStream());
					dos.write(data.getBytes(Charset.forName("utf-8")));
					dos.flush();

					
					
					
					System.out.print("res"+conn.getResponseCode());
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		frame.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				System.out.print("clicked");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		Timer timer =new Timer(true);
		timer.schedule(new get_http(),1000, 10000);
		
		
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				spinner.setValue(Integer.valueOf(temper));
				if(sw.equals("off")) {
					Icon icon=new ImageIcon("C:/Users/happysorry/Desktop/OM2mfinal/Btn_OFF.png");
					lb.setIcon(icon);
				}
				else if(sw.equals("on")) {
					Icon icon=new ImageIcon("C:/Users/happysorry/Desktop/OM2mfinal/Btn_ON.png");
					lb.setIcon(icon);
				}
			}
			
		},1000,1000);
		
		
		
		//System.out.print(line);
		
		
		
	}

	
	
	
	
	
}
