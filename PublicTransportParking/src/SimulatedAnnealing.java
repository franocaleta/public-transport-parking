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
        int index = 0;
        for (int i = 0; i < 1; i++) {
            while ((neighbour = swapTracks(scheduleCurrent)).isInvalid());
            double newFitness = neighbour.fitness();
            if (newFitness > fitnessBest) {
                fitnessBest = newFitness;
                scheduleCurrent = neighbour;
                index = i;
                //          System.out.println(scheduleCurrent.fitness());
            }

            // Schedule neighbour = swapTracks(scheduleCurrent);

        }
      //  System.out.println("Index: " + index);
        System.out.println(scheduleCurrent.fitness());
        System.out.println(scheduleCurrent.isInvalid());
        return scheduleCurrent;
    }

    public Schedule swapTracks(Schedule schedule) {
        Random rand = new Random();
        int firstTrackIndex = rand.nextInt(schedule.getTrake().size());
        int secondTrackIndex = rand.nextInt(schedule.getTrake().size());
        System.out.println("First "+ firstTrackIndex+" Second" + secondTrackIndex);
        Schedule candidate = schedule.createCopy();
       // candidate.getTrake()
        List<Vehicle> first = schedule.getTrake().get(firstTrackIndex).getVozilaUOvojTraci();
        List<Vehicle> second = schedule.getTrake().get(secondTrackIndex).getVozilaUOvojTraci();

        candidate.getTrake().get(firstTrackIndex).vozilaUOvojTraci = second;
        candidate.getTrake().get(firstTrackIndex).zbrojDuljinaDodanihVozila = second.stream().mapToInt(vehicle -> vehicle.duljinaVozila).sum();

        candidate.getTrake().get(secondTrackIndex).vozilaUOvojTraci = first;
        candidate.getTrake().get(secondTrackIndex).zbrojDuljinaDodanihVozila = first.stream().mapToInt(vehicle -> vehicle.duljinaVozila).sum();
        return candidate;
    }

    public Schedule swapVehicles(Schedule schedule) {
        Random rand = new Random();
        int firstTrackIndex = 0;
        int secondTrackIndex = 0;
        while(true) {
            firstTrackIndex = rand.nextInt(schedule.getTrake().size());
            if (!schedule.getTrake().get(firstTrackIndex).vozilaUOvojTraci.isEmpty())
                break;

        }
        while(true) {
            secondTrackIndex = rand.nextInt(schedule.getTrake().size());
            if (!schedule.getTrake().get(secondTrackIndex).vozilaUOvojTraci.isEmpty())
                break;

        }
        int firstTrackVehicleIndex = rand.nextInt(schedule.getTrake().get(firstTrackIndex).vozilaUOvojTraci.size());
        int secondTrackVehicleIndex = rand.nextInt(schedule.getTrake().get(secondTrackIndex).vozilaUOvojTraci.size());

        if(firstTrackVehicleIndex == 1 && schedule.getTrake().get(firstTrackIndex).vozilaUOvojTraci.size() == 1) {
            firstTrackVehicleIndex --;
        }

        if(secondTrackVehicleIndex == 1 && schedule.getTrake().get(secondTrackIndex).vozilaUOvojTraci.size() == 1) {
            secondTrackVehicleIndex --;
        }


        Schedule candidate = schedule.createCopy();
        Vehicle firstVehicle = candidate.getTrake().get(firstTrackIndex).vozilaUOvojTraci.get(firstTrackVehicleIndex);
        Vehicle secondVehicle = candidate.getTrake().get(secondTrackIndex).vozilaUOvojTraci.get(secondTrackVehicleIndex);
        // candidate.getTrake()

        candidate.getTrake().get(firstTrackIndex).vozilaUOvojTraci.remove(firstTrackVehicleIndex);
        candidate.getTrake().get(secondTrackIndex).vozilaUOvojTraci.remove(secondTrackVehicleIndex);

        candidate.getTrake().get(firstTrackIndex).vozilaUOvojTraci.add(firstTrackVehicleIndex, secondVehicle);
        candidate.getTrake().get(secondTrackIndex).vozilaUOvojTraci.add(secondTrackVehicleIndex, firstVehicle);

        List<Vehicle> first = schedule.getTrake().get(firstTrackIndex).getVozilaUOvojTraci();
        List<Vehicle> second = schedule.getTrake().get(secondTrackIndex).getVozilaUOvojTraci();

        candidate.getTrake().get(firstTrackIndex).zbrojDuljinaDodanihVozila = second.stream().mapToInt(vehicle -> vehicle.duljinaVozila).sum();

        candidate.getTrake().get(secondTrackIndex).zbrojDuljinaDodanihVozila = first.stream().mapToInt(vehicle -> vehicle.duljinaVozila).sum();
        return candidate;
    }
}
