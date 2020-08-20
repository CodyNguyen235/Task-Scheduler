import com.google.gson.Gson;
/**
 * Gson external library is used to parse json file into a java object.
 * This requires adding the necessary jar file to your project libraries.
 * The file can be found in lib/gson-2.8.6.jar
 */

import java.io.FileReader;
import java.util.List;

import java.io.FileNotFoundException;


/**
 * The FileAccess Class.
 *
 * This is used for reading or writing to json files.
 */
public class FileAccess {

    /**
     * The JsonTask private SubClass.
     *
     * This class is used to temporarily hold the data for each task that is retrieved from the json file.
     */
    private class JsonTask {
        private String Name;
        private String Type;
        private int Date;
        private int StartDate;
        private double StartTime;
        private double Duration;
        private double EndDate;
        private int Frequency;
    }

    /**
     * This method reads from json file, creates a list of JsonTasks and calls sendToSchedule method.
     */
    public void readFromJson() { // TODO: Change argument to "String: filename" and pass into filereader
        try {
            Gson gson = new Gson();
            JsonTask taskList[] = gson.fromJson(new FileReader("src/test/Set1.json"), JsonTask[].class);
            sendToSchedule(taskList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This private method is called by readFromJson.
     * This creates the appropriate type of task for each JsonTask, based off it's String-Type value.
     * Then each task is added to the schedule.
     */
    // TODO: Add exception handling
    private void sendToSchedule(JsonTask[] taskList) {
        for (JsonTask task : taskList) {
            switch (task.Type) {
                case "Visit":
                case "Shopping":
                case "Appointment":
                    System.out.println("Adding Transient Task");
                    TransientTask newTransientTask = new TransientTask(task.Name, task.Type, task.StartTime, task.EndDate, task.Duration, task.Date);
                    // TODO: add transient-task to schedule
                    break;

                case "Class":
                case "Study":
                case "Sleep":
                case "Exercise":
                case "Work":
                case "Meal":
                    System.out.println("Adding Recurring Task");
                    RecurringTask newRecurringTask = new RecurringTask(task.Name, task.Type, task.StartTime, task.EndDate, task.Duration, task.StartDate, task.Date, task.Frequency);
                    // TODO: add recurring-task to schedule
                    break;

                case "Cancellation":
                    System.out.println("Adding Anti Task");
                    AntiTask newAntiTask = new AntiTask(task.Name, task.Type, task.StartTime, task.EndDate, task.Duration, task.Date);
                    // TODO: add anti-task to schedule
                    break;

                default:
                    System.out.println(task.Name + " could not be added due to an invalid Type.");

            }

        }
    }

    /**
     * This method writes the given ArrayLists of tasks to json file
     *
     * @param fileName      The filename to create and write to
     * @param transientTask The ArrayList of transient tasks to be outputted to file
     * @param recurringTask The ArrayList of recurring tasks to be outputted to file
     * @param antiTask      The ArrayList of anti tasks to be outputted to file
     */
    public void writeToJson(String fileName, List<TransientTask> transientTask, List<RecurringTask> recurringTask, List<AntiTask> antiTask) {

    }

}
