package simulator;

import threads.Threads;

public class SimulatorThread implements Runnable {
	
	private boolean isRunning;
	private double start;
	private double stepTime;
	private double dt;
	
	private final double Timestep = 1000/30;
	
	public SimulatorThread() {
		this.isRunning = false;
		System.out.println("SimulatorThread: Hello world!");
		
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		this.isRunning = true;
		System.out.println("SimulatorThread: main loop is now running");
		while(isRunning) {
			try {
				Thread.sleep(1);
				
				doThreadMessageFlush();
				
				dt = System.nanoTime()/1000000; // dt in milliseconds
				while(stepTime < dt) {
					stepTime += Timestep;
					
					// step simulation
					
					
				}
				
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void doThreadMessageFlush() {
		SimulatorThreadMessage msg = null;
		while ((msg = Threads.getSimulatorQueue().poll()) != null) {
			int type = msg.getType();
			int from = msg.getFrom();
			
			switch (type) {
				case SimulatorThreadMessage.Type.None: {
					if (from == Threads.Main) {
						String command = msg.getCommand();
						if (command != null) {
							System.out.println("SimulatorThread: received command from server");
							System.out.println("command: " + command);
						}
					}
					break;
				}
				case SimulatorThreadMessage.Type.Update: {
					System.out.println("SimulatorThread: received an Update event");
					if (from == Threads.Server) {
						System.out.println("... from Server!");
					}
					break;
				}
				case SimulatorThreadMessage.Type.Add: {
					System.out.println("SimulatorThread: received an Add event");
					if (from == Threads.Server) {
						System.out.println("... from Server!");
					}
					break;
				}
				case SimulatorThreadMessage.Type.Remove: {
					System.out.println("SimulatorThread: received an Remove event");
					if (from == Threads.Server) {
						System.out.println("... from Server!");
					}
					break;
				}
				default: {
					break;
				}
			
			}
		}
	}

}
