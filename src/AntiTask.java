
// TODO: Auto-generated Javadoc
/**
 * The Class AntiTask.
 */
public class AntiTask extends Task
{
	
	private String[] typeList = {"Cancellation"};

	/**
	 * Empty Constructor
	 */
	public AntiTask() {
		super();
	}
	
	/**
	 * Instantiates a new anti task.
	 *
	 * @param name the name
	 * @param type the type
	 * @param startTime the start time
	 * @param duration the duration
	 * @param date the date
	 * @throws InvalidTypeException the invalid type exception
	 */
	public AntiTask(String name, String type, double startTime, double endTime, double duration, int date)
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
	public boolean checkType(String type)
	{
		for(String typeCheck: typeList)
		{
			if(typeCheck.equals(type))
			{
				return true;
			}
		}
		return false;
		
	}

	@Override
	public String toString() {
		return "\nAnti-Task\nTask Name: " + name + "\nTask Type: " + type + "\nTask Start Time: " + startTime + "\nTask Duration: " + duration + "\nTask Date: " + date + "\n";
	}

}
