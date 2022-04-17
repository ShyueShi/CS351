import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

/**
 * This class describes the nurse's overall behavior
 * @author Shyue Shi Leong, Ze Jia Lim, Peter Aloisi
 *
 */
public class Nurse extends SimProcess{
	
	/**
	 * constructor method
	 * @param owner
	 * @param name
	 * @param showInTrace
	 */
	public Nurse(Model owner, String name, boolean showInTrace) {
		super(owner,name,showInTrace);
	}
	
	/**
	 * This method describes the overall behavior of the nurse
	 */
	@Override
	public void lifeCycle() throws SuspendExecution {
		ProjectModel model = (ProjectModel) getModel();
		model.dailyCost.update(1200);
		
		while(true){
			if(model.nurseQueue.isEmpty()==true){
				//no patient to treated at the moment 
				model.idleNurseQueue.insert(this);
				model.nurseUtilization.update(0);
				this.passivate();
			}
			else{
				//remove the first patient in line and treat them
				Patient patient = model.nurseQueue.removeFirst();
				TimeSpan treatmentTime  = new TimeSpan(model.nurseTimes.sample());
				this.hold(treatmentTime);
				patient.activate();
			}
		}
	}

}
