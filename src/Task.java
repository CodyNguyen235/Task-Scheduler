import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Task.
 */
public class Task
{
	protected String name;
	protected String type;
	protected double startTime;
	protected double endTime;
	protected double duration;
	protected int date;
	
	public Task() {
		this.name = "";
		this.type = "";
		this.startTime = 0;
		this.duration = 0;
		this.date = 0;
	}
	/**
	 * Instantiates a new task.
	 *
	 * @param name the name
	 * @param type the type
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param duration the duration
	 */
	
	public Task(String name, String type, double startTime, double endTime, double duration)
	{
		this.name = name;
		this.type = type;
		this.startTime = startTime;
		this.duration = duration;
		this.endTime = endTime;
	}
	

	/**
	 * Convert startTime to nearest 15 minutes (0.25)
	 *
	 */
	
	public double convertTime(double time) {
		BigDecimal bd = new BigDecimal(time);
		int intValue = bd.intValue();
		double dv = time - intValue;
		if(dv > 0.00 && dv < 0.125)
			dv = 0.00;
		else if(dv > 0.125 && dv < 0.375)
			dv = 0.25;
		else if(dv > 0.375 && dv < 0.625)
			dv = 0.50;
		else if(dv > 0.625 && dv < 0.875)
			dv = 0.75;
		return intValue + dv;
	}

	/**
	 * Check startTime if it is between 0(midnight) and 23.75(11:45 PM)
	 * @param startTime
	 * @return true if it is between 0 and 23.75
	 */
	public boolean checkStartTime(double startTime) {
		double convertedStartTime = convertTime(startTime);
		if(convertedStartTime < 0.0 || convertedStartTime > 23.75) 
			return false;
		return true;
	}

	/*
	 * Check duration if it is between 0.25 and 23.75
	 */
	public boolean checkDuration(double duration) {
		double convertedDuration = convertTime(duration);
		if(convertedDuration < 0.25 || convertedDuration > 23.75)
			return false;
		return true;
	}

	public int getCurrentDate() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String todayString = formatter.format(date);
		int today = Integer.parseInt(todayString);
		return today;
	}

	/**
	 * Checks if entered date is valid, i.e. not before today's date
	 * @param startDate The start date for the task
	 * @return true if the date is valid
	 */
	public boolean checkDate(int startDate) {
		String temp = Integer.toString(startDate);
		String sYear = Integer.toString(startDate).substring(0,3);
		int year = Integer.parseInt(sYear);
		char monthFirst = temp.charAt(4);
		char monthSecond = temp.charAt(5);
		if(startDate < getCurrentDate())
			return false;
		else if(monthFirst > '1' || temp.charAt(6) > '3')
			return false;
			// Months that have 31 days
		else if((monthFirst == '0' && (monthSecond == '1' || monthSecond == '3' || monthSecond == '5' || monthSecond == '7'
				|| monthSecond == '8')) ||
				(monthFirst == '1' && (monthSecond == '0' || monthSecond == '2'))) {
			if (temp.charAt(6) >= '3' && temp.charAt(7) > '1')
				return false;
		}
			// Months that have 30 days
		else if ((monthFirst == '0' && (monthSecond == '4' || monthSecond == '6' || monthSecond == '9') ||
				(monthFirst == '1' && monthSecond == '1'))) {
			if (temp.charAt(6) >= '3' && temp.charAt(7) > '0')
				return false;
		}
		else if (monthFirst == '0' && monthSecond == '2') {
			if (temp.charAt(6) > '2')
				return false;
			else if (year % 4 == 0) {
				if (temp.charAt(6) == '2' && temp.charAt(7) > '8')
					return false;
			}
		}
		return true;
	}

	/**
	 * Checks if entered date is valid, i.e. not before today's date
	 * @param startDate The start date for the task
	 * @param endDate The end date for the task
	 * @return true if the date is valid
	 */
	public boolean checkDate(int startDate, int endDate) {
		String temp = Integer.toString(startDate);
		String temp2 = Integer.toString(endDate);
		if(startDate < getCurrentDate() || endDate < getCurrentDate())
			return false;
		if(temp.charAt(4) > '1')
			return false;
		return true;
	}

	/**
	 * Checks if tasks overlap
	 *
	 * @param taskAdded The task that is going to be added to Schedule
	 * @param taskChecked The task that is going to be checked i.e. already in the system
	 * @return if tasks overlap, return true
	 */
	public boolean isOverlap(Task taskAdded, Task taskChecked) {
		boolean doesOverlap = false;
		double taskAddedStartTime = taskAdded.getStartTime(), taskCheckedStartTime = taskChecked.getStartTime();
		double taskAddedEndTime = taskAdded.getEndTime(), taskCheckedEndTime = taskChecked.getEndTime();
		if ((taskAddedStartTime >= taskCheckedStartTime && taskAddedStartTime < taskCheckedEndTime) ||
			(taskAddedStartTime <= taskCheckedEndTime && taskAddedEndTime > taskCheckedStartTime) ||
			(taskAddedEndTime > taskCheckedStartTime && taskAddedEndTime < taskCheckedEndTime) ||
			(taskAddedStartTime <= taskCheckedStartTime && taskAddedEndTime > taskCheckedEndTime )) {
			if (taskAdded.getDate() == taskChecked.getDate())
				doesOverlap = true;
		}
		else if (pastMidnight(taskAdded)) {
			if ((taskAddedStartTime <= taskCheckedEndTime && taskAddedEndTime < taskCheckedStartTime) ||
				(taskAddedStartTime >= taskCheckedEndTime && ((taskCheckedStartTime > taskAddedStartTime) ||
						(taskAddedEndTime < taskCheckedEndTime)))) {
				if (taskAdded.getDate() == taskChecked.getDate())
				doesOverlap = true;
			}
		}
		return doesOverlap;
	}

	/**
	 * Checks if tasks go past midnight
	 *
	 * @param task The task being checked
	 * @return if task goes past midnight, return true
	 */

	public boolean pastMidnight(Task task) {
		double taskValue = task.getStartTime() + task.getDuration();
		if (taskValue > task.getEndTime()) {
			return true;
		}
		else
			return false;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public int getDate()
	{
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(int date)
	{
		this.date = date;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public double getStartTime()
	{
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime the new start time
	 */
	public void setStartTime(double startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public double getEndTime()
	{
		return endTime;
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime the new end time
	 */
	public void setEndTime(double endTime)
	{
		this.endTime = endTime;
	}

	/**
	 * Gets the duration.
	 *
	 * @return the duration
	 */
	public double getDuration()
	{
		return duration;
	}

	/**
	 * Sets the duration.
	 *
	 * @param duration the new duration
	 */
	public void setDuration(double duration)
	{
		this.duration = duration;
	}

}
