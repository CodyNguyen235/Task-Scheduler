import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.lang.Number;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Cody
 * This Class is intended to deal with creating and deleting and the interaction between tasks. 
 */

public class Schedule 
{
	// Types are part of a certain task set, e.g. "Class" is a recursive task,
	// "Visit" is a transient task
	final static Set<String> rTypes = new HashSet<String>(
			Arrays.asList("Class", "Study", "Sleep", "Exercise", "Work", "Meal"));
	final static Set<String> tTypes = new HashSet<String>(Arrays.asList("Visit", "Shopping", "Appointment"));
	final int INITIAL_SIZE = 100;
	static List<Task> mySchedule;
	static Scanner kb = new Scanner(System.in);
	
	private static TransientTask tt;
	private static RecurringTask rt;;
	private static AntiTask at;
	
	private static String name = "";
	private static String type = "";
	private static double startTime = 0.0;
	private static double endTime = 0.0;
	private static int startDate = 0;
	private static int endDate = 0;
	private static double duration = 0;
	private static int frequency = 1;
	private int[] tempArray;

	public Schedule() 
	{
		mySchedule = new ArrayList<Task>(INITIAL_SIZE);
	}

	/**
	 * Creates a task
	 */
	public static void createTask() 
	{
		int input;
		{
			System.out.print(
					"\n\nWhich number of task do you want to create?\n1. Transient Task\n2. Recurring Task\n3. AntiTask\n\n");
			input = kb.nextInt();
			kb.nextLine();
		}
		while (input < 1 || input > 3);

		// Create Task
		switch (input) {
			// Create Transient Task
			case (1):
				createTransientTask(tt);
				break;
			// Create Recurring Task
			case (2):
				createRecurringTask(rt);
				break;
			// Create Anti Task
			case (3):
				createAntiTask(at);
				break;
		}
	}

	/*
	 * Find a task by the name
	 */
	public void findTask() 
	{
		System.out.println("Total tasks: " + (mySchedule.size()));
		System.out.println("\nEnter the name of the task");
		
		name = kb.nextLine();
		int index = -1;
		
		if (mySchedule.isEmpty())
			System.out.println("\nSchedule is empty\n");
		
		for (int i = 0; i < mySchedule.size(); i++) 
		{
			if (mySchedule.get(i).getName().equals(name))
				index = i;
		}
		
		if (index == -1) 
		{
			System.out.println("\nNo matched task has found");
		} else {
			System.out.println(mySchedule.get(index).toString());
			if (isType(index).equals("Recurring")) 
			{
				System.out.println("List of dates the task occurs:");
				
				tempArray = ((RecurringTask) mySchedule.get(index)).getListDates();
				for (int i = 0; i < tempArray.length; i++)
					System.out.println(tempArray[i]);
			}
		}
	}

	/*
	 * Edit a task
	 */
	public void editTask() 
	{
		
		TransientTask tempTT = new TransientTask();
		RecurringTask tempRT = new RecurringTask();
		
		AntiTask tempAT = new AntiTask();
		int index = -1;
		System.out.println("\nEnter the name of the task");
		name = kb.nextLine();
		
		if (mySchedule.isEmpty())
			System.out.println("\nSchedule is empty\n");
		else 
		{
			for (int i = 0; i < mySchedule.size(); i++) 
			{
				if (mySchedule.get(i).getName().equals(name)) 
				{
					index = i;
				} else
					System.out.println("\nNo matched task has found\n");
			}
		}
		if (isType(index).equals("Anti")) 
		{
			tempAT = (AntiTask) mySchedule.get(index);
			
			System.out.println(tempAT.toString());
			System.out.println("\nPlease enter new information\n\n");
			
			createAntiTask((AntiTask) mySchedule.get(index));
			
		} else if (isType(index).equals("Transient")) 
		{
			tempTT = (TransientTask) mySchedule.get(index);
			
			System.out.println(tempTT.toString());
			System.out.println("\nPlease enter new information\n\n");
			
			createTransientTask((TransientTask) mySchedule.get(index));
		} else if (isType(index).equals("Recurring")) 
		{
			tempRT = (RecurringTask) mySchedule.get(index);
			
			System.out.println(tempRT.toString());
			System.out.println("\nPlease enter new information\n");
			
			createRecurringTask((RecurringTask) mySchedule.get(index));
		}

	}

	/*
	 * Creates a Transient Task
	 */
	public static void createTransientTask(TransientTask temp) 
	{
		temp = new TransientTask();
		
		System.out.println("\nEnter the name of the transient task");
		name = kb.nextLine();
		
		if (findTaskName(name).equals(name)) 
		{
			while (findTaskName(name).equals(name)) 
			{
				System.out.println("Task name already exists, try another.");
				name = kb.nextLine();
			}
		}
		
		System.out.println("Enter the type of the task (Visit, Shopping, Appointment)");
		type = kb.nextLine();
		
		System.out.println("Enter the start time (0.0 ~ 23.75)");
		startTime = kb.nextDouble();
		
		System.out.println("Enter the duration of the task? (0.25 ~ 23.75)");
		duration = kb.nextDouble();
		
		System.out.println("Enter the date of task? (YYYYMMDD)");
		startDate = kb.nextInt();
		
		double endTime = startTime + duration;
		kb.nextLine();
		
		if (temp.checkType(type) && temp.checkStartTime(startTime) && temp.checkDuration(duration)
				&& temp.checkDate(startDate)) 
		{
			temp.setName(name);
			temp.setType(type);
			temp.setStartTime(startTime);
			temp.setDuration(duration);
			endTime = startTime + duration;
			
			if (endTime >= 24) 
			{
				endTime -= 24;
			}
			
			temp.setEndTime(endTime);
			temp.setDate(startDate);
			mySchedule.add(temp);
			
			System.out.println("\"" + name + "\" has been added to your schedule\n\n");
			
			if (checkOverlap(temp)) 
			{
				System.out.println("Error: Task overlaps another and has been removed. Please try again.");
				name = "";
				mySchedule.remove(temp);
			}
		} else 
		{
			if (!temp.checkType(type))
				System.out.println("Invalid type input");
			if (!temp.checkStartTime(startTime))
				System.out.println("Invalid start time input");
			if (!temp.checkDuration(duration))
				System.out.println("Invalid duration input");
			if (!temp.checkDate(startDate))
				System.out.println("Invalid start date input");
			System.out.println("Please try again.");
		}
	}

	/*
	 * Creates a Recurring Task
	 */
	public static void createRecurringTask(RecurringTask temp) 
	{
		temp = new RecurringTask();
		System.out.println("\nEnter the name of the recurring task");
		name = kb.nextLine();
		
		if (findTaskName(name).equals(name)) 
		{
			while (findTaskName(name).equals(name)) 
			{
				System.out.println("Task name already exists, try another.");
				name = kb.nextLine();
			}
		}
		
		System.out.println("Enter the type of the task (Class, Study, Sleep, Exercise, Work, Meal)");
		type = kb.nextLine();
		
		System.out.println("Enter the start time of the task(0.0 ~ 23.75)");
		startTime = kb.nextDouble();
		
		System.out.println("Enter the duration of the task? (0.25 ~ 23.75)");
		duration = kb.nextDouble();
		
		System.out.println("Enter the start date of the task? (YYYYMMDD)");
		startDate = kb.nextInt();
		
		System.out.println("Enter the end date of the task? (YYYYMMDD)");
		endDate = kb.nextInt();
		
		System.out.println("Enter the frequency of the task (1, 7, 30)");
		frequency = kb.nextInt();
		
		kb.nextLine();
		
		double endTime = startTime + duration;
		
		if (temp.checkType(type) && temp.checkStartTime(startTime) && temp.checkDuration(duration)
				&& temp.checkDate(startDate, endDate) && temp.checkFrequency(frequency)) 
		{
			temp.setName(name);
			temp.setType(type);
			temp.setStartTime(startTime);
			temp.setDuration(duration);
			endTime = startTime + duration;
			
			if (endTime >= 24) 
			{
				endTime -= 24;
			}
			temp.setEndTime(endTime);
			temp.setStartDate(startDate);
			temp.setEndDate(endDate);
			temp.setFrequency(frequency);
			mySchedule.add(temp);
			System.out.println("\"" + name + "\" has been added to your schedule\n\n");
			if (checkOverlap(temp)) 
			{
				System.out.println("Error: Task overlaps another and has been removed. Please try again.");
				name = "";
				mySchedule.remove(temp);
			}
		} else 
		{
			if (!temp.checkType(type))
				System.out.println("Invalid type input");
			if (!temp.checkStartTime(startTime))
				System.out.println("Invalid start time input");
			if (!temp.checkDuration(duration))
				System.out.println("Invalid duration input");
			if (!temp.checkDate(startDate, endDate))
				System.out.println("Invalid start date input");
			if (!temp.checkFrequency(frequency))
				System.out.println("Invalid frequency input.");
			System.out.println("Please try again.");
		}
	}

	/*
	 * Creates a Anti Task
	 */

	public static void createAntiTask(AntiTask temp) 
	{
		temp = new AntiTask();
		System.out.println("\nEnter the name of the anti task");
		name = kb.nextLine();
		if (findTaskName(name).equals(name)) 
		{
			while (findTaskName(name).equals(name)) 
			{
				System.out.println("Task name already exists, try another.");
				name = kb.nextLine();
			}
		}
		
		System.out.println("Enter the type of the task (Cancellation)");
		type = kb.nextLine();
		
		System.out.println("Enter the start time (0.0 ~ 23.75)");
		startTime = kb.nextDouble();
		
		System.out.println("Enter the duration of the task? (0.25 ~ 23.75)");
		duration = kb.nextDouble();
		
		System.out.println("Enter the date of task? (YYYYMMDD)");
		startDate = kb.nextInt();
		
		double endTime = startTime + duration;
		
		kb.nextLine();
		
		if (temp.checkType(type) && temp.checkStartTime(startTime) && temp.checkDuration(duration)
				&& temp.checkDate(startDate)) 
		{
			for (int index = 0; index < mySchedule.size(); index++) 
			{
				Task temp2 = mySchedule.get(index);
				if ((isType(index) == "Recurring") && (temp2.getStartTime() == temp.getStartTime())
						&& (temp2.getDuration() == temp.getDuration())) 
				{
					break;
				} else
					System.out.println("There is no recurring task that correlates to the anti-task. Please try again.");
			}
			temp.setName(name);
			temp.setType(type);
			temp.setStartTime(startTime);
			temp.setDuration(duration);
			
			endTime = startTime + duration;
			
			if (endTime >= 24) 
			{
				endTime -= 24;
			}
			
			temp.setEndTime(endTime);
			temp.setDate(startDate);
			mySchedule.add(temp);
			
			System.out.println("\"" + name + "\" has been added to your schedule\n\n");
			
			if (checkOverlap(temp)) 
			{
				System.out.println("Error: Task overlaps another and has been removed. Please try again.");
				name = "";
				mySchedule.remove(temp);
			}
			
		} else if (!temp.checkType(type))
			System.out.println("Invalid type input");
		if (!temp.checkStartTime(startTime))
			System.out.println("Invalid start time input");
		if (!temp.checkDuration(duration))
			System.out.println("Invalid duration input");
		if (!temp.checkDate(startDate))
			System.out.println("Invalid start date input");
		System.out.println("Please try again.");
	}

	public static String isType(int index) 
	{
		String temp = "";
		if (mySchedule.get(index).getType().equals("Visit") || mySchedule.get(index).getType().equals("Shopping")
				|| mySchedule.get(index).getType().equals("Appointment"))
		{
			temp = "Transient";
		}
		else if (mySchedule.get(index).getType().equals("Cancellation"))
			temp = "Anti";
		else
			temp = "Recurring";
		return temp;
	}

	 /**
	 * @param taskAdded the task being checked for overlap
	 * @return if tasks overlap, return true
	 */

	private static boolean checkOverlap(Task taskAdded) 
	{
		for (int index = 0; index < mySchedule.size(); index++) 
		{
			if (mySchedule.get(index).isOverlap(taskAdded, mySchedule.get(index))) 
			{
				if (mySchedule.get(index).getName().equals(taskAdded.getName()))
					return false;
				else if (mySchedule.get(index).getType().equals("Anti") && taskAdded.getType().equals("Anti"))
					return false;
				else
					return true;
			}
		}
		return true;
	}

	/**
	 * finds duplicate task name if in the Schedule
	 * @param name
	 * @return
	 */
	private static String findTaskName(String name) 
	{
		String taskResult = "";
		for (int i = 0; i < mySchedule.size(); i++) 
		{
			if (mySchedule.get(i).getName().equals(name)) 
			{
				taskResult = mySchedule.get(i).getName();
			}
			else 
			{
				taskResult = "noTask";
			}
		}
		return taskResult;
	}

	public void deleteTask() 
	{
		String input;
		int index = -1;
		
		System.out.println("\nEnter the name of the task");
		name = kb.nextLine();
		
		if (mySchedule.isEmpty())
			System.out.println("\nSchedule is empty\n");
		
		else { // here, look for recurring and anti
			for (int i = 0; i < mySchedule.size(); i++) 
			{
				if (mySchedule.get(i).getName().equals(name))
					index = i;
			}
			if (index == -1)
				System.out.println("\nNo matched task has found");
		}
		if (isType(index).equals("Recurring")) { // if this task is a recurring task, check for anti-tasks
			for (int antiIndex = 0; antiIndex < mySchedule.size(); antiIndex++) 
			{
				if ((isType(antiIndex) == "Anti")
						&& (mySchedule.get(antiIndex).getStartTime() == mySchedule.get(index).getStartTime())
						&& (mySchedule.get(antiIndex).getDuration() == mySchedule.get(index).getDuration())) 
				{
					do 
					{
						System.out.println("Are you sure you want to delete the task \""
								+ mySchedule.get(index).getName() + "\"" + " and related anti-tasks? (Y/N)");
						input = kb.nextLine();
					} while (!(input.equals("Y") || input.equals("N")));
					if (input.equals("Y")) 
					{
						System.out.println(
								"\n\"" + mySchedule.get(index).getName() + "\" has been removed from your schedule");
						mySchedule.remove(antiIndex);
						mySchedule.remove(index);
					} else 
					{
						System.out.println("You have selected No");
					}
				}
			}
		} else if (isType(index).equals("Anti")) {
		}
		do 
		{
			System.out.println(
					"Are you sure you want to delete the task \"" + mySchedule.get(index).getName() + "\"? (Y/N)");
			input = kb.nextLine();
		} while (!(input.equals("Y") || input.equals("N")));
		if (input.equals("Y")) 
		{
			System.out.println("\n\"" + mySchedule.get(index).getName() + "\" has been removed from your schedule");
			mySchedule.remove(index);
		} else 
		{
			System.out.println("You have selected No");
		}
	}

	public void viewOneDay()
	{
		System.out.println("What date do you want to view? (YYYYMMDD)");
		int date = kb.nextInt();

		System.out.println();

		List<Task> listTasks = new ArrayList<Task>();
		for (int i = 0; i < mySchedule.size(); i++)
		{
			//System.out.println(mySchedule.get(i).toString());
			if(mySchedule.get(i) instanceof RecurringTask)
			{
				int[] temp = ((RecurringTask)mySchedule.get(i)).getListDates();
				//System.out.println(Arrays.toString(temp));
				for (int dateTemp : temp)
				{
					if(dateTemp == date)
					{
						listTasks.add(mySchedule.get(i));
					}
				}
			}
			else if (mySchedule.get(i) instanceof AntiTask)
			{
				for (int a = 0; a < listTasks.size(); a++)
				{
					if(listTasks.get(a) instanceof RecurringTask)
					{
						if(listTasks.get(a).getStartTime() == mySchedule.get(i).getStartTime())
						{
							listTasks.remove(a);
						}
					}
				}
			}
			else
			{
				if(((TransientTask)mySchedule.get(i)).getDate() == date) {
					listTasks.add(mySchedule.get(i));
				}
			}
		}

		if(listTasks.isEmpty()) {
			System.out.println("No Tasks for " + date);
		}
		else {
			System.out.println("Tasks for " + date);

			for(int i = 0; i < listTasks.size(); i++)
			{
				System.out.println(listTasks.get(i).toString());
			}
		}
	}

	public void viewOneWeek()
	{
		System.out.println("What start date do you want to view? (YYYYMMDD)");
		int date = kb.nextInt();

		System.out.println();
		int[] dates = new int[7];
		dates[0] = date;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();

		int tempInt;
		try 
		{
			c.setTime(sdf.parse(Integer.toString(date)));
		} catch (ParseException e) 
		{
			e.printStackTrace();
		}

		for (int i = 1; i < dates.length; i++) 
		{
			c.add(Calendar.DATE, 1);

			tempInt = concat(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
			dates[i] = concat(tempInt, c.get(Calendar.DAY_OF_MONTH));
		}

		//System.out.println(Arrays.toString(dates));


		List<Task> listTasks = new ArrayList<Task>();
		for (int i = 0; i < mySchedule.size(); i++) 
		{
			//System.out.println(mySchedule.get(i).toString());
			if(mySchedule.get(i) instanceof RecurringTask) 
			{
				int[] temp = ((RecurringTask)mySchedule.get(i)).getListDates();
				//System.out.println(Arrays.toString(temp));
				for (int dateTemp : temp) 
				{
					for(int dateTemp2: dates) 
					{
						if(dateTemp2 == dateTemp)
							listTasks.add(mySchedule.get(i));
					}
				}
			}
			else if (mySchedule.get(i) instanceof AntiTask) 
			{
				for (int a = 0; a < listTasks.size(); a++) 
				{
					if(listTasks.get(a) instanceof RecurringTask) 
					{
						if(listTasks.get(a).getStartTime() == mySchedule.get(i).getStartTime())
							listTasks.remove(a);
					}
				}
			}
			else 
			{
				for (int d = 0; d < dates.length; d++) 
				{
					if(((TransientTask)mySchedule.get(i)).getDate() == dates[d])
						listTasks.add(mySchedule.get(i));
				}
			}
		}

		if(listTasks.isEmpty()) 
		{
			System.out.println("No Tasks for " + date);
		}
		else 
		{
			System.out.println("Tasks for " + date);

			for(int i = 0; i < listTasks.size(); i++) 
			{
				System.out.println(listTasks.get(i).toString());
			}
		}

	}

	public void viewOneMonth()
	{
		System.out.println("What start date do you want to view? (YYYYMMDD)");
		int date = kb.nextInt();

		System.out.println();
		int[] dates = new int[30];
		dates[0] = date;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();

		int tempInt;
		try
		{
			c.setTime(sdf.parse(Integer.toString(date)));
		} catch (ParseException e)
		{
			e.printStackTrace();
		}

		for (int i = 1; i < dates.length; i++)
		{
			c.add(Calendar.DATE, 1);

			tempInt = concat(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
			dates[i] = concat(tempInt, c.get(Calendar.DAY_OF_MONTH));
		}

		//System.out.println(Arrays.toString(dates));


		List<Task> listTasks = new ArrayList<Task>();
		for (int i = 0; i < mySchedule.size(); i++)
		{
			//System.out.println(mySchedule.get(i).toString());
			if(mySchedule.get(i) instanceof RecurringTask)
			{
				int[] temp = ((RecurringTask)mySchedule.get(i)).getListDates();
				//System.out.println(Arrays.toString(temp));
				for (int dateTemp : temp)
				{
					for(int dateTemp2: dates)
					{
						if(dateTemp2 == dateTemp)
						{
							listTasks.add(mySchedule.get(i));
						}
					}
				}
			}
			else if (mySchedule.get(i) instanceof AntiTask)
			{
				for (int a = 0; a < listTasks.size(); a++)
				{
					if(listTasks.get(a) instanceof RecurringTask)
					{
						if(listTasks.get(a).getStartTime() == mySchedule.get(i).getStartTime())
						{
							listTasks.remove(a);
						}
					}
				}
			}
			else
			{
				for(int d = 0; d < dates.length; d++)
				{
					if(((TransientTask)mySchedule.get(i)).getDate() == dates[d])
					{
						listTasks.add(mySchedule.get(i));
					}
				}
			}
		}

		if(listTasks.isEmpty())
		{
			System.out.println("No Tasks for " + date);
		}
		else
		{
			System.out.println("Tasks for " + date);

			for(int i = 0; i < listTasks.size(); i++)
			{
				System.out.println(listTasks.get(i).toString());
			}
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
}

	/*
	 * Import .json file to the schedule
	 */
	public static void readFile() 
	{
		String fileName;
		System.out.println("Enter the .json file name");
		fileName = kb.nextLine();
		JSONParser parser = new JSONParser();
		try (FileReader reader = new FileReader("src/" + fileName)) 
		{
			Object obj = parser.parse(reader);
			JSONArray taskList = (JSONArray) obj;
			for (int i = 0; i < taskList.size(); i++) 
			{
				JSONObject temp = (JSONObject) taskList.get(i);
				if (temp.get("Type").equals("Visit") || temp.get("Type").equals("Shopping")
						|| temp.get("Type").equals("Appointment")) 
				{
					TransientTask tt = new TransientTask();
					tt.setName((String) temp.get("Name"));
					tt.setType((String) temp.get("Type"));
					tt.setStartTime(((Number) temp.get("StartTime")).doubleValue());
					tt.setDuration(((Number) temp.get("Duration")).doubleValue());
					double endTime = tt.getStartTime() + tt.getDuration();
					
					if (endTime >= 24)
						endTime -= 24;
					
					tt.setEndTime(endTime);
					tt.setDate(((Number) temp.get("Date")).intValue());
					
					if (tt.checkType(tt.getType()) && tt.checkStartTime(tt.getStartTime())
							&& tt.checkDuration(tt.getDuration()) && ((Task) tt).checkDate(tt.getDate())) 
					{
						System.out.println("\"" + tt.getName() + "\" has been added to your schedule\n\n");
						mySchedule.add(tt);
						
						  if (checkOverlap(tt)) { System.out.
						  println("Error: Task overlaps another and has been removed. Please try again."
						  ); name = ""; mySchedule.remove(tt); }
						 
					} else 
					{
						if (!tt.checkType(tt.getType()))
							System.out.println("Invalid type input");
						if (!tt.checkStartTime(tt.getStartTime()))
							System.out.println("Invalid start time input");
						if (!tt.checkDuration(tt.getDuration()))
							System.out.println("Invalid duration input");
						if (!tt.checkDate(tt.getDate()))
							System.out.println("Invalid start date input");
						System.out.println("Please try again.");
					}
				} else if (temp.get("Type").equals("Cancellation")) {
					AntiTask anti = new AntiTask();
					anti.setName((String) temp.get("Name"));
					anti.setType((String) temp.get("Type"));
					anti.setStartTime(((Number) temp.get("StartTime")).doubleValue());
					anti.setDuration(((Number) temp.get("Duration")).doubleValue());
					double endTime = anti.getStartTime() + anti.getDuration();
					if (endTime >= 24)
						endTime -= 24;
					anti.setEndTime(endTime);
					anti.setDate(((Number) temp.get("Date")).intValue());
					if (anti.checkType(anti.getType()) && anti.checkStartTime(anti.getStartTime())
							&& anti.checkDuration(anti.getDuration()) && ((Task) anti).checkDate(anti.getDate())) {
						System.out.println("\"" + anti.getName() + "\" has been added to your schedule\n\n");
						mySchedule.add(anti);
						
						  if (checkOverlap(anti)) { System.out.
						  println("Error: Task overlaps another and has been removed. Please try again."
						  ); name = ""; mySchedule.remove(anti); }
						 
					} else {
						if (!anti.checkType(anti.getType()))
							System.out.println("Invalid type input");
						if (!anti.checkStartTime(anti.getStartTime()))
							System.out.println("Invalid start time input");
						if (!anti.checkDuration(anti.getDuration()))
							System.out.println("Invalid duration input");
						if (!anti.checkDate(anti.getDate()))
							System.out.println("Invalid start date input");
						System.out.println("Please try again.");
					}
				} else {
					RecurringTask recurring = new RecurringTask();
					recurring.setName((String) temp.get("Name"));
					recurring.setType((String) temp.get("Type"));
					recurring.setStartTime(((Number) temp.get("Duration")).doubleValue());
					recurring.setDuration(((Number) temp.get("Duration")).doubleValue());
					double endTime = recurring.getStartTime() + recurring.getDuration();
					if (endTime >= 24) {
						endTime -= 24;
					}
					recurring.setEndTime(endTime);
					recurring.setStartDate(((Number) temp.get("StartDate")).intValue());
					recurring.setEndDate(((Number) temp.get("EndDate")).intValue());
					recurring.setFrequency(((Number) temp.get("Frequency")).intValue());
					if (recurring.checkType(recurring.getType()) && recurring.checkStartTime(recurring.getStartTime())
							&& recurring.checkDuration(recurring.getDuration())
							&& recurring.checkDate(recurring.getStartDate(), recurring.getEndDate())
							&& recurring.checkFrequency(recurring.getFrequency())) {
						mySchedule.add(recurring);
						System.out.println("\"" + recurring.getName() + "\" has been added to your schedule\n\n");
						
						  if (checkOverlap(recurring)) { System.out.
						  println("Error: Task overlaps another and has been removed. Please try again."
						  ); name = ""; mySchedule.remove(recurring) ; }
						 
					} else {
						if (!recurring.checkType(recurring.getType()))
							System.out.println("Invalid type input");
						if (!recurring.checkStartTime(recurring.getStartTime()))
							System.out.println("Invalid start time input");
						if (!recurring.checkDuration(recurring.getDuration()))
							System.out.println("Invalid duration input");
						if (!recurring.checkDate(recurring.getStartDate(), recurring.getEndDate()))
							System.out.println("Invalid start date input");
						if (!recurring.checkFrequency(recurring.getFrequency()))
							System.out.println("Invalid frequency input.");
						System.out.println("Please try again.");
					}
				}
			}
			System.out.println(fileName + " is imported to your schedule");
		} catch (

		FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Exports .json file of the schedule
	 */
	@SuppressWarnings("unchecked")
	public static void writeFile() 
	{
		String fileName;
		System.out.println("Please enter file name");
		fileName = kb.nextLine();
		JSONArray list = new JSONArray();
		for (int i = 0; i < mySchedule.size(); i++) {
			JSONObject temp = new JSONObject();
			if (mySchedule.get(i).getType().equals("Visit") || mySchedule.get(i).getType().equals("Shopping")
					|| mySchedule.get(i).getType().equals("Appointment")) {
				temp.put("Name", mySchedule.get(i).getName());
				temp.put("Type", mySchedule.get(i).getType());
				temp.put("Date", ((TransientTask) mySchedule.get(i)).getDate());
				temp.put("StartTime", mySchedule.get(i).getStartTime());
				temp.put("Duration", mySchedule.get(i).getDuration());
			} else if (mySchedule.get(i).getType().equals("Cancellation")) {
				temp.put("Name", mySchedule.get(i).getName());
				temp.put("Type", mySchedule.get(i).getType());
				temp.put("Date", ((AntiTask) mySchedule.get(i)).getDate());
				temp.put("StartTime", mySchedule.get(i).getStartTime());
				temp.put("Duration", mySchedule.get(i).getDuration());
			} else {
				temp.put("Name", mySchedule.get(i).getName());
				temp.put("Type", mySchedule.get(i).getType());
				temp.put("StartDate", ((RecurringTask) mySchedule.get(i)).getStartDate());
				temp.put("StartTime", mySchedule.get(i).getStartTime());
				temp.put("Duration", mySchedule.get(i).getDuration());
				temp.put("EndDate", ((RecurringTask) mySchedule.get(i)).getEndDate());
				temp.put("Frequency", ((RecurringTask) mySchedule.get(i)).getFrequency());
			}
			list.add(temp);
		}
		try (FileWriter file = new FileWriter(fileName + ".json")) {
			file.write(list.toString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
