import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class RecurringTask.
 * A Recurring Task is a task that will occur at a regular interval repeatedly until the end date. It is intended to represent something that occurs regularly, such as a class or
 * Sleeping
 */
public class RecurringTask extends Task
{
	private String[] typeList = {"Class", "Study", "Sleep", "Exercise", "Work", "Meal"};
	private int startDate;
	private int endDate;
	private int frequency;

	public RecurringTask() {
		super();
		this.startDate = 0;
		this.endDate = 0;
		this.frequency = 0;
	}

	/**
	 * Instantiates a new recurring task.
	 *
	 * @param name the name
	 * @param type the type
	 * @param startTime the start time
	 * @param duration the duration
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param frequency the frequency
	 * @throws InvalidTypeException the invalid type exception
	 * @throws InvalidFrequencyException the invalid frequency exception
	 */
	public RecurringTask(String name, String type, double startTime, double endTime, double duration,
						 int startDate, int endDate, int frequency)
	{
		super(name, type, startTime, endTime, duration);
		this.startDate = startDate;
		this.endDate = endDate;
		this.frequency = frequency;
	}
	
	/**
	 * This method checks if the type is part of valid types of tasks that exist.
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
	
	// Frequency input validation
	public boolean checkFrequency(int frequency) {
		if(frequency == 1 || frequency == 7 || frequency == 30)
			return true;
		return false;
	}

	/**
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public int getStartDate()
	{
		return startDate;
	}



	/**
	 * Sets the start date.
	 *
	 * @param startDate the new start date
	 */
	public void setStartDate(int startDate)
	{
		this.startDate = startDate;
	}



	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	public int getEndDate()
	{
		return endDate;
	}



	/**
	 * Sets the end date.
	 *
	 * @param endDate the new end date
	 */
	public void setEndDate(int endDate)
	{
		this.endDate = endDate;
	}



	/**
	 * Gets the frequency.
	 *
	 * @return the frequency
	 */
	public int getFrequency()
	{
		return frequency;
	}



	/**
	 * Sets the frequency.
	 *
	 * @param frequency the new frequency
	 */
	public void setFrequency(int frequency)
	{
		this.frequency = frequency;
	}
	
	public int[] getListDates()
	{
		String st = Integer.toString(startDate); //start time
		String et = Integer.toString(endDate); //end time
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
		ArrayList list = new ArrayList();
		
		int temp;
		
		try
		{
			c.setTime(sdf.parse(st));
			end.setTime(sdf.parse(et));
		} catch (ParseException e)
		{
			System.out.println("Something messed up in getListDates()");
			return null;
		}
		
		if(frequency == 1)
		{
			while (!end.before(c)) // while the end date is not before start date
			{
				temp = concat(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
				temp = concat(temp, c.get(Calendar.DAY_OF_MONTH));
				list.add(temp);
				c.add(Calendar.DATE, 1);		
			}
			return convertIntegers(list);
		}
		else if(frequency == 7)
		{
			while (!end.before(c)) // while the end date is not before start date
			{
				temp = concat(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
				temp = concat(temp, c.get(Calendar.DAY_OF_MONTH));
				list.add(temp);
				c.add(Calendar.DATE, 7);		
			}
			return convertIntegers(list);
		}
		else if(frequency == 30)
		{
			while (!end.before(c)) // while the end date is not before start date
			{
				temp = concat(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
				temp = concat(temp, c.get(Calendar.DAY_OF_MONTH));
				list.add(temp);
				c.add(Calendar.MONTH, 1);		
			}
			return convertIntegers(list);
		}
		else
		{
			System.out.println("Frequency is not 1 or 7 or 30");
			return null;
		}
	}
	
	private int concat(int a, int b) 
    { 
  
        // Convert both the integers to string 
        String s1 = Integer.toString(a); 
        String s2 = Integer.toString(b); 
        
        if(a < 10)
        {
        	s1 = "0" + s1;
        }
        if(b < 10)
        {
        	s2 = "0" + s2;
        }
  
        // Concatenate both strings 
        String s = s1 + s2; 
  
        // Convert the concatenated string 
        // to integer 
        int c = Integer.parseInt(s); 
  
        // return the formed integer 
        return c; 
    } 
	
	private static int[] convertIntegers(List<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = integers.get(i).intValue();
	    }
	    return ret;
	}

	@Override
	public String toString() {
		return "\nRecurring Task\nTask Name: " + name + "\nTask Type: " + type + "\nTask Start Time: " + startTime + "\nTask Duration: " + duration + "\nTask Start Date: " + startDate + "\nTask End Date: " + endDate + "\nTask Frequency: " + frequency + "\n";
	}

}
