import java.util.Random;

public class SimulatedAnnealing {

    private Schedule scheduleBest;

    public SimulatedAnnealing(Schedule scheduleInitial) {
        this.scheduleBest = scheduleInitial;
    }

    public void run() {
        Schedule scheduleCurrent = this.scheduleBest;

        while(true) {
            Schedule neighbour = swapTracks(scheduleCurrent);
        }
    }

    public Schedule swapTracks(Schedule schedule) {
        Random rand = new Random();
        int firstTrack = rand.nextInt(schedule.getTrake().size());
        int secondTrack = rand.nextInt(schedule.getTrake().size());
        //Schedule neighbour = new Schedule(schedule.getTrake(), schedule.)
        return null;
    }
}
