// TODO: Auto-generated Javadoc
/**
 * The Class TransientTask.
 * A Transient Task is a task that occurs once at a defined time.
 */
public class TransientTask extends Task
{
	
	private String[] typeList = {"Visit", "Shopping", "Appointment"};
	private int date;

	/**
	 * Empty Constructor
	 */
	public TransientTask() {
		super();
		this.date = 0;
	}
	
	/**
	 * Instantiates a new transient task.
	 *
	 * @param name the name
	 * @param type the type
	 * @param startTime the start time
	 * @param duration the duration
	 * @param date the date
	 * @throws InvalidTypeException the invalid type exception
	 */
	public TransientTask(String name, String type, double startTime, double endTime, double duration, int date)
	{
		super(name, type, startTime, endTime, duration);
		this.date = date;
	}
	
	/**
	 * Check type.
	 *
	 * @param type the type
	 * @return true, if successful
	 */
	public boolean checkType(String type){
		for(String typeCheck: typeList){
			if(typeCheck.equals(type)){
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "\nTransient Task\nTask Name: " + name + "\nTask Type: " + type + "\nTask Start Time: " + startTime + "\nTask Duration: " + duration + "\nTask Date: " + date + "\n";
	}

}
