import java.util.Random;
import java.util.concurrent.TimeUnit;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.statistic.ConfidenceCalculator;

/**
 * This is a replication model class that does multiple runs for a simulation
 * @author Shyue Shi Leong, Ze Jia Lim, Peter Aloisi
 *
 */
public class ReplicationModel extends Model{
	
	//fixed variables
	public static final int NUM_REPLICATIONS = 1000;
	public static final boolean INCLUDE_OUTPUT_PER_REPLICATION = true;
	public static final boolean INCLUDE_REPORT_PER_REPLICATION = false;
	
	//Statistical trackers
	protected ConfidenceCalculator costCalculator;
	protected ConfidenceCalculator patientArrivedCalculator;
	protected ConfidenceCalculator patientOvercrowdedCalculator;
	protected ConfidenceCalculator patientDivertedCalculator;
	protected ConfidenceCalculator patientTreatedCalculator;
	protected ConfidenceCalculator responseTimeCalculator;
	protected ConfidenceCalculator nurseUtilizationCalculator;
	protected ConfidenceCalculator specialistUtilizationCalculator;
	protected ConfidenceCalculator nurseQueueCalculator;
	
	/**
	 * constructor method
	 * @param owner
	 * @param name
	 * @param showInReport
	 * @param showInTrace
	 */
	public ReplicationModel(Model owner, String name, boolean showInReport, boolean showInTrace) {
		super(owner,name,showInReport,showInTrace);
	}

	@Override
	public String description() {
		return "A model that runs multiple experiments in desmo-j";
	}
	
	/**
	 * This method set up the simulation before it begins simulating
	 */
	@Override
	public void doInitialSchedules() {
		int counter=0;
		while(counter!=NUM_REPLICATIONS){
			Experiment.setReferenceUnit(TimeUnit.MINUTES);
			
			ProjectModel model = new ProjectModel(null,"Single Server Queueing System",true,true,2,3,6);
			Experiment exp = new Experiment("SSQExperiment");
			Random rnd = new Random();
			int seed = rnd.nextInt(999)+1;
			exp.setSeedGenerator(seed);
			model.connectToExperiment(exp);
			
			Condition condition = new Condition(model," test",true,null);
			exp.setShowProgressBar(false);
			
			exp.stop(condition);
			exp.tracePeriod(new TimeInstant(0,TimeUnit.MINUTES),new TimeInstant(960,TimeUnit.MINUTES));
			exp.debugPeriod(new TimeInstant(0,TimeUnit.MINUTES),new TimeInstant(960,TimeUnit.MINUTES));
			
			exp.start();
			
			exp.report();
			if(exp.isStopped()){
				counter++;
			}
			exp.finish();
			costCalculator.update(model.dailyCost.getValue());
			patientArrivedCalculator.update(model.patientArrived.getValue());
			patientOvercrowdedCalculator.update(model.patientOvercrowded.getValue());
			patientDivertedCalculator.update(model.patientDiverted.getValue());
			patientTreatedCalculator.update(model.patientTreated.getValue());
			responseTimeCalculator.update(model.responseTime.getMean());
			nurseUtilizationCalculator.update(model.nurseUtilization.getMean());
			specialistUtilizationCalculator.update(model.specialistUtilization.getMean());
			nurseQueueCalculator.update(model.nurseQueue.averageLength());
		}
	}
	
	/**
	 * This method initializes all the variables and structures used in the simulation
	 */
	@Override
	public void init() {
		costCalculator = new ConfidenceCalculator(this, "Cost Calculator",true,false);
		patientArrivedCalculator = new ConfidenceCalculator(this, "Patient Arrived Calculator",true,false);
		patientOvercrowdedCalculator = new ConfidenceCalculator(this, "Patient Overcrowded Calculator",true,false);
		patientDivertedCalculator = new ConfidenceCalculator(this, "Patient Diverted Calculator",true,false);
		patientTreatedCalculator = new ConfidenceCalculator(this, "Patient Treated Calculator",true,false);
		responseTimeCalculator = new ConfidenceCalculator(this, "Response Time Calculator",true,false);
		nurseUtilizationCalculator = new ConfidenceCalculator(this, "Nurse Utilization Calculator",true,false);
		specialistUtilizationCalculator = new ConfidenceCalculator(this, "Specialist Utilization Calculator",true,false);
		nurseQueueCalculator = new ConfidenceCalculator(this, "Nurse Queue Calculator",true,false);
	}

	/**
     * Runs the model.
     *
     * @param args is an array of command-line arguments (ignored here)
     */
	public static void main(String args[]){
		Experiment.setReferenceUnit(TimeUnit.MINUTES);
		
		ReplicationModel repModel = new ReplicationModel(null,"Replication Model for ProjectModel",true,true);
		Experiment exp = new Experiment("ProjectReplication");
		repModel.connectToExperiment(exp);
		
		// set experiment parameters
        exp.setShowProgressBar(false);
        exp.stop(new TimeInstant(0));
        exp.traceOff(new TimeInstant(0));
        exp.debugOff(new TimeInstant(0));
        exp.setSilent(true);
        
        exp.start();
        
        exp.report();
        
        exp.finish();
	}
}
