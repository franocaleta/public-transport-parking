import java.util.Random;
import java.util.*;

public class SimulatedAnnealing {

    private Schedule scheduleBest;
    private double fitnessBest;

    public SimulatedAnnealing(Schedule scheduleInitial) {
        this.scheduleBest = scheduleInitial;
        this.fitnessBest = scheduleInitial.fitness();
    }

    public Schedule run() {
        System.out.println(this.fitnessBest);
        Schedule scheduleCurrent = this.scheduleBest;
        Schedule neighbour = null;
        for (int i = 0; i < 100; i++) {
            while ((neighbour = swapTracks(scheduleCurrent)).isInvalid());
            double newFitness = neighbour.fitness();
            if (newFitness > fitnessBest) {
                fitnessBest = newFitness;
                scheduleCurrent = neighbour;
                //          System.out.println(scheduleCurrent.fitness());
            }

            // Schedule neighbour = swapTracks(scheduleCurrent);

        }
        System.out.println(scheduleCurrent.fitness());
        System.out.println(scheduleCurrent.isInvalid());
        return scheduleCurrent;
    }

    public Schedule swapTracks(Schedule schedule) {
        Random rand = new Random();
        int firstTrack = rand.nextInt(schedule.getTrake().size());
        int secondTrack = rand.nextInt(schedule.getTrake().size());
        Schedule candidate = schedule.createCopy();
        List<Vehicle> first = schedule.getTrake().get(firstTrack).getVozilaUOvojTraci();
        List<Vehicle> second = schedule.getTrake().get(secondTrack).getVozilaUOvojTraci();

        candidate.getTrake().get(firstTrack).vozilaUOvojTraci = second;
        candidate.getTrake().get(firstTrack).zbrojDuljinaDodanihVozila = second.stream().mapToInt(vehicle -> vehicle.duljinaVozila).sum();

        candidate.getTrake().get(secondTrack).vozilaUOvojTraci = first;
        candidate.getTrake().get(firstTrack).zbrojDuljinaDodanihVozila = first.stream().mapToInt(vehicle -> vehicle.duljinaVozila).sum();
        return candidate;
    }
}
