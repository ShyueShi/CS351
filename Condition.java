import desmoj.core.simulator.Model;
import desmoj.core.simulator.ModelCondition;

/**
 * This is a condition class to terminate the simulation
 * @author Shyue Shi Leong, Ze Jia Lim, Pater Aloisi
 *
 */
public class Condition extends ModelCondition{
	
	/**
	 * constructor method
	 * @param owner
	 * @param name
	 * @param showInReport
	 * @param arg3
	 */
	public Condition(Model owner, String name, boolean showInReport, Object[] arg3) {
		super(owner,name,showInReport);
	}

	@Override
	public boolean check() {
		ProjectModel model = (ProjectModel) getModel();
		if(model.gen.isTerminated()==true){
			if(model.nurseQueue.isEmpty()==true && model.specialistQueue.isEmpty()==true){
				if(model.idleNurseQueue.length()==model.numNurses && model.idleSpecialistQueue.length()==model.numSpecialists){
					if(model.numPatients.getValue()==0){
						return true;
					}
				}
			}
		}
		return false;
	}

}
