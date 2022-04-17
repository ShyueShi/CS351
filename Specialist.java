import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

/**
 * This class describes the specialist's overall behavior
 * @author Shyue Shi Leong, Ze Jia Lim, Pater Aloisi
 *
 */
public class Specialist extends SimProcess{
	
	/**
	 * constructor method
	 * @param owner
	 * @param name
	 * @param showInTrace
	 */
	public Specialist(Model owner, String name, boolean showInTrace) {
		super(owner,name,showInTrace);
	}
	
	/**
	 * This method describes the overall behavior of the specialist
	 */
	@Override
	public void lifeCycle() throws SuspendExecution {
		ProjectModel model = (ProjectModel) getModel();
		model.dailyCost.update(1500);
		
		while(true){
			if(model.specialistQueue.isEmpty()==true){
				//no patient to treated at the moment 
				model.idleSpecialistQueue.insert(this);
				model.specialistUtilization.update(0);
				this.passivate();
			}
			else{
				//remove the first patient in line and treat them
				Patient patient = model.specialistQueue.removeFirst();
				TimeSpan specialistTime = new TimeSpan(model.specialistTimes.sample());
				this.hold(specialistTime);
				patient.activate();
			}
		}
	}

}
