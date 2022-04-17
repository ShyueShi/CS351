import desmoj.core.dist.BoolDistBernoulli;
import desmoj.core.dist.ContDistExponential;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ProcessQueue;
import desmoj.core.statistic.Accumulate;
import desmoj.core.statistic.Count;
import desmoj.core.statistic.Tally;

/**
 * This is model class for the simulation 
 * @author Shyue Shi Leong, Ze Jia Lim, Peter Aloisi
 *
 */
public class ProjectModel extends Model{
	
	//state variables
	protected int waitingRoomLimit=8;
	protected int examRoomLimit;
	protected int numNurses;
	protected int numSpecialists;
	
	//Structures used
	protected ProcessQueue<Patient> nurseQueue;
	protected ProcessQueue<Nurse> idleNurseQueue;
	protected ProcessQueue<Patient> specialistQueue;
	protected ProcessQueue<Specialist> idleSpecialistQueue;
	
	//Distributions used to generate random numbers
	protected ContDistExponential interarrivalTimes1;
	protected ContDistExponential interarrivalTimes2;
	protected ContDistExponential interarrivalTimes3;
	protected ContDistExponential nurseTimes;
	protected ContDistExponential specialistTimes;
	protected BoolDistBernoulli specialistChances;
	
	//Statistical Trackers
	protected Count numPatients;
	protected Count patientArrived;
	protected Count patientTreatedByNurse;
	protected Count patientTreatedBySpecialist;
	protected Count patientOvercrowded;
	protected Count patientDiverted;
	protected Count patientTreated;
	protected Count dailyCost;
	protected Tally responseTime;
	protected Accumulate nurseUtilization;
	protected Accumulate specialistUtilization;
	protected Generator gen;
	
	/**
	 * constructor method
	 * @param owner
	 * @param name
	 * @param showInReport
	 * @param showInTrace
	 * @param numNurse
	 * @param numSpecialist
	 * @param numRoom
	 */
	public ProjectModel(Model owner, String name, boolean showInReport, boolean showInTrace,
			int numNurse, int numSpecialist, int numRoom) {
		
		super(owner, name, showInReport,showInTrace);
		numNurses = numNurse;
		numSpecialists = numSpecialist;
		examRoomLimit = numRoom-numSpecialists;
	}
	
	/**
	 * This method returns the description of the simulation
	 */
	public String description() {
		return "This model describes a single server queueing system";
	}
	
	/**
	 * This method set up the simulation before it begins simulating
	 */
	public void doInitialSchedules() {
		gen = new Generator(this, "Generator",true);
		gen.activate();
		Nurse[] nurseList = new Nurse[numNurses];
		Specialist[] specialistList = new Specialist[numSpecialists];
		for(int i=0;i<numNurses;i++){
			nurseList[i] = new Nurse(this, "Nurse "+ i,true);;
			nurseList[i].activate();
			
		}
		for(int i=0;i<numSpecialists;i++){
			specialistList[i] = new Specialist(this, "Specialist "+i,true);
			specialistList[i].activate();
		}
		dailyCost.update(300*(numSpecialists+examRoomLimit));
	}
	
	/**
	 * This method initializes all the variables and structures used in the simulation
	 */
	public void init() {
		nurseQueue = new ProcessQueue<Patient>(this,"Nurse Queue",true,false);
		idleNurseQueue = new ProcessQueue<Nurse>(this,"Idle Nurse Queue",true,false);
		specialistQueue = new ProcessQueue<Patient>(this,"Specialist Queue",true,false);
		idleSpecialistQueue = new ProcessQueue<Specialist>(this,"Idle Specialist Queue",true,false);
		interarrivalTimes1 = new ContDistExponential(this,"Interarrival Times 1",15,true,false);
		interarrivalTimes2 = new ContDistExponential(this,"Interarrival Times 2",6,true,false);
		interarrivalTimes3 = new ContDistExponential(this,"Interarrival Times 3",9,true,false);
		nurseTimes = new ContDistExponential(this,"Nurse Times",8,true,false);
		specialistTimes = new ContDistExponential(this,"Specialist Times",25,true,false);
		specialistChances = new BoolDistBernoulli(this, "Specialist Probability", 0.4, true, false);
		numPatients = new Count(this,"Number of Patients",true,false);
		patientArrived = new Count(this,"Patient Arrived",true,false);
		patientTreatedByNurse = new Count(this,"Patients treated by Nurse",true,false);
		patientTreatedBySpecialist = new Count(this,"Patients treated by specialist",true,false);
		patientOvercrowded = new Count(this,"Patient Overcrowded",true,false);
		patientDiverted = new Count(this,"Patient Diverted",true,false);
		patientTreated = new Count(this,"Patient Treated",true,false);
		dailyCost = new Count(this,"Cost",true,false);
		responseTime = new Tally(this,"Response Time",true,false);
		nurseUtilization = new Accumulate(this, "Nurse Utilization", true, false);
		specialistUtilization = new Accumulate(this, "Specialist Utilization", true, false);
		
	}
	
}
