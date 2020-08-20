import java.util.Scanner;

public class Main {

	private static Schedule mySchedule = new Schedule();

	public static void main(String[] args) throws InvalidTypeException {

		Scanner kb = new Scanner(System.in);
		int input;
		do {
			do {
				mainMenu();
				input = kb.nextInt();
			} while (input < 0 || input > 9);

			// Tasks
			switch (input) {
			case 1:
				mySchedule.createTask();
				break;
			case 2:
				mySchedule.findTask();
				break;
			case 3:
				mySchedule.deleteTask();
				break;
			case 4:
				mySchedule.editTask();
				break;
			case 5:
				mySchedule.writeFile();
				break;
			case 6:
				mySchedule.readFile();
				//FileAccess read = new FileAccess();
				//read.readFromJson();
				break;
			case 7:
				mySchedule.viewOneDay();
				break;
			case 8:
				mySchedule.viewOneWeek();
				break;
			case 9:
				mySchedule.viewOneMonth();
				mySchedule.writeFile();
				break;
			case 0:
				System.exit(0);
			default:
				mainMenu();
			}
		} while (input != 0);
	}

	public static void mainMenu() {
		System.out.print("\nMy Schedule \n\n1. Create a task \n2. Find a task \n3. "
				+ "Delete a task \n4. Edit a task \n5. Write the schedule to a file \n6. "
				+ "Read the schedule to a file \n7. View or write the schedule for one day \n8. "
				+ "View or write the schedule for one week \n9. View or write the schedule for one month \n0. Exit \nPlease enter number of task\n");
	}

}
