import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

/**
 * This is the generator class that generate new patients for 12 hours
 * @author Shyue Shi Leong, Ze Jia Lim and Peter Aloisi
 *
 */
public class Generator extends SimProcess{
	//state variable
	protected double openTime;
	
	/**
	 * constructor method
	 * @param owner
	 * @param name
	 * @param showInTrace
	 */
	public Generator(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		openTime=0;
	}
	
	/**
	 * This method generate arrivals based on the time
	 */
	@Override
	public void lifeCycle() throws SuspendExecution {
		ProjectModel model = (ProjectModel) getModel();
			openTime=model.presentTime().getTimeAsDouble();
			while(model.presentTime().getTimeAsDouble()-openTime<720){
				double currentTime = model.presentTime().getTimeAsDouble();
				if(currentTime-openTime>=480){
					TimeSpan interarrivalTime = new TimeSpan(model.interarrivalTimes3.sample());
					this.hold(interarrivalTime);
					Patient patient = new Patient(model,"patient",true);
					patient.activate();
				}
				else if(currentTime-openTime>=120&&currentTime-openTime<480){
					TimeSpan interarrivalTime = new TimeSpan(model.interarrivalTimes2.sample());
					this.hold(interarrivalTime);
					Patient patient = new Patient(model,"patient",true);
					patient.activate();
				}
				else{
					TimeSpan interarrivalTime = new TimeSpan(model.interarrivalTimes1.sample());
					this.hold(interarrivalTime);
					Patient patient = new Patient(model,"patient",true);
					patient.activate();
				}
			}
	}

}
