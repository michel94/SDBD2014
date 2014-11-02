import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class UdpConnection implements Runnable {
	private int udpPort = 5000;
	private Boolean secondary;
	private boolean sec;
	private int losses=0;
	private final int ACCEPTED_LOSSES = 3;
	private String address;

	public UdpConnection(Boolean s, String address){
		secondary = s;
		sec = s;
		this.address=address;
		Thread t = new Thread(this, "pong");
		
		t.start();

	}

	public void run() {
		DatagramSocket socket = null;
		String s;
		while(true){
			if(!sec){ //primary
				System.out.println("Server is now primary");
				
				try{
					socket = new DatagramSocket(udpPort);

					while(true){
						byte[] buffer = new byte[1000];
						DatagramPacket request = new DatagramPacket(buffer, buffer.length);
						socket.receive(request);
						s = new String(request.getData(), 0, request.getLength());
						System.out.println("Server Received: " + s);

						buffer = (new String("pong")).getBytes();
						DatagramPacket reply = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
						socket.send(reply);
					}
				}catch (SocketException e){
					;
				}catch (IOException e) {
					;
				}finally {
					;
				}
			}else{ // secondary
				try {
					socket = new DatagramSocket();
					socket.setSoTimeout(1500);

					while(true){
						
						if(losses > ACCEPTED_LOSSES){
							
							synchronized(secondary){
								System.out.println("synchronized");
								sec = false;
								secondary.notify();
								break;
							}
						}

						Thread.sleep(1000);
						byte [] m = (new String("ping")).getBytes();
						InetAddress aHost = InetAddress.getByName(address);
						DatagramPacket request = new DatagramPacket(m, m.length, aHost, udpPort);

						System.out.println("Sending ping");
						socket.send(request);

						System.out.println("Waiting pong");
						byte[] buffer = new byte[1000];
						DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
						try{
							socket.receive(reply);
							losses = 0;
						}catch(SocketTimeoutException e){
							losses++;
							System.out.println("Did not received answer " + losses);
						}
						System.out.println("Recebeu: " + new String(reply.getData(), 0, reply.getLength()));
					}
				}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
				}catch (IOException e){System.out.println("IO: " + e.getMessage());
				}catch (InterruptedException e){System.out.println("Thread Cannot Sleep");
				}finally {if(socket != null) socket.close();
				}
			}


		}
	}
}
