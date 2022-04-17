import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;

/**
 * This is the patient class and it describes the patients overall behavior
 * @author Shyue Shi Leong, Ze Jia Lim and Peter Aloisi
 *
 */
public class Patient extends SimProcess{
	
	//state variable
	protected double arrivalTime;
	
	/**
	 * constructor method
	 * @param owner
	 * @param name
	 * @param showInTrace
	 */
	public Patient(Model owner, String name, boolean showInTrace) {
		super(owner,name,showInTrace);
	}
	
	/**
	 * This method describes the overall behavior of patient
	 */
	@Override
	public void lifeCycle() throws SuspendExecution {
		ProjectModel model = (ProjectModel) getModel();
		this.arrivalTime = model.presentTime().getTimeAsDouble();
		model.numPatients.update(1);
		model.patientArrived.update(1);
		model.sendTraceNote(this+" arrives");
		
		if(model.nurseQueue.length()<model.waitingRoomLimit){
			model.nurseQueue.insert(this);
			if(model.idleNurseQueue.isEmpty()==false){
				model.nurseUtilization.update(1);
				model.idleNurseQueue.removeFirst().activate();
			}
			this.passivate();
			model.dailyCost.update(100);
			model.patientTreatedByNurse.update(1);
			
			boolean specialistConsultation = model.specialistChances.sample();
			if(specialistConsultation==true){
				double currentTime = model.presentTime().getTimeAsDouble();
				if(model.specialistQueue.length()>=model.examRoomLimit ||currentTime-this.arrivalTime>30){
					//the patient is redirected to emergency room because no rooms for them or they waited too long
					model.sendTraceNote(this+" leaves because exam room full or waited too long");
					if(currentTime-this.arrivalTime>30){
						model.sendTraceNote("guys left as he waited too long");
					}
					model.patientDiverted.update(1);
					model.dailyCost.update(500);
				}
				else{
					model.specialistQueue.insert(this);
					if(model.idleSpecialistQueue.isEmpty()==false){
						model.specialistUtilization.update(1);
						model.idleSpecialistQueue.removeFirst().activate();
					}
					this.passivate();
					model.patientTreatedBySpecialist.update(1);
					model.dailyCost.update(200);
					model.responseTime.update(model.presentTime().getTimeAsDouble()-this.arrivalTime);
					model.patientTreated.update(1);
				}
			}
			else{
				//patients done because they do not need to see the specialist
				model.responseTime.update(model.presentTime().getTimeAsDouble()-this.arrivalTime);
				model.patientTreated.update(1);
			}
		}
		else{
			//patients go to emergency room due to overcrowded waiting room
			model.sendTraceNote(this+" leaves because of waiting room full");
			model.patientOvercrowded.update(1);
			model.dailyCost.update(500);
		}
		model.numPatients.update(-1);
		
	}

}
