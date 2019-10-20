package simulator;

import threads.Threads;
import tools.Profiler;

public class SimulatorThread implements Runnable {
	
	private boolean isRunning;
	private double start;
	private double dt;
	private double stepTime;
	
	private World world;
	private final double Timestep = 1000/30;
	
	private Profiler profiler = new Profiler();
	
	public SimulatorThread() {
		this.isRunning = false;
		System.out.println("SimulatorThread: Hello world!");
		
		this.start = System.nanoTime()/1000000;
		this.dt = this.start;
		this.stepTime = this.start;
		this.world = new World();
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		this.isRunning = true;
		System.out.println("SimulatorThread: main loop is now running");
		while(isRunning) {
			try {
				Thread.sleep(1);
				
				flushThreadMessages();
				
				stepTime = System.nanoTime()/1000000; // dt in milliseconds
				
				
				profiler.start("main");				
				
				while(dt < stepTime) {
					dt += Timestep;
					//System.out.println("blip");
					// step simulation
					world.step(stepTime);
					
					
				}				
				
				profiler.stop("main");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void flushThreadMessages() {
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
						if (msg.getCharacter() == null) {
							//Threads.getDatabaseQueue().offer(new DatabaseThreadMessage)
						}
						else {
							new NetObject(world, msg.getCharacter());
						}
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
