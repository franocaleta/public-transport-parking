import java.util.Random;
import java.util.*;
import java.util.stream.Collectors;

public class SimulatedAnnealing {

    private Schedule scheduleBest;
    private double fitnessBest;

    public SimulatedAnnealing(Schedule scheduleInitial) {
        this.scheduleBest = scheduleInitial;
        this.fitnessBest = scheduleInitial.fitness();
    }

    public Schedule run() {
//        System.out.println(this.fitnessBest);
        Schedule scheduleCurrent = this.scheduleBest;
        Schedule neighbour = null;
        int index = 0;
        for (int i = 0; i < 10000; i++) {
            while ((neighbour = reorderTracksByVehiclesScheduleType(scheduleCurrent)).isInvalid());
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
//        System.out.println(scheduleCurrent.fitness());
//        System.out.println(scheduleCurrent.isInvalid());
        return scheduleCurrent;
    }

    public Schedule swapTracks(Schedule schedule) {
        Random rand = new Random();
        int firstTrackIndex = rand.nextInt(schedule.getTrake().size());
        int secondTrackIndex = rand.nextInt(schedule.getTrake().size());
        Schedule candidate = schedule.createCopy();
        List<Vehicle> first = schedule.getTrake().get(firstTrackIndex).getVozilaUOvojTraci();
        List<Vehicle> second = schedule.getTrake().get(secondTrackIndex).getVozilaUOvojTraci();

        candidate.getTrake().get(firstTrackIndex).vozilaUOvojTraci = second;

        candidate.getTrake().get(secondTrackIndex).vozilaUOvojTraci = first;
        return candidate;
    }

    public Schedule reorderTracksByVehiclesScheduleType(Schedule schedule) {
        Schedule candidate = schedule.createCopy();

        Random rand = new Random();
        int firstTrackIndex = rand.nextInt(candidate.getTrake().size());
        int secondTrackIndex = rand.nextInt(candidate.getTrake().size());

        Track firstTrack = candidate.getTrake().get(firstTrackIndex);
        Track secondTrack = candidate.getTrake().get(secondTrackIndex);

        List<Vehicle> obeTrake = firstTrack.vozilaUOvojTraci;
        obeTrake.addAll(secondTrack.vozilaUOvojTraci);

        List<Vehicle> poTipuRasporedaPoVremenu = obeTrake.stream()
                .sorted(Comparator.comparingInt(v -> v.tipRasporeda))
                .sorted(Comparator.comparingInt(v -> v.vrijemePolaska))
                .collect(Collectors.toList());

        firstTrack.vozilaUOvojTraci.clear();
        secondTrack.vozilaUOvojTraci.clear();

        for (Vehicle vehicle : poTipuRasporedaPoVremenu) {
            if (!firstTrack.addVehicle(vehicle)) {
                secondTrack.addVehicle(vehicle);
            }
        }

        return candidate;
    }

    public Schedule mergeTwoTracksInOne(Schedule schedule) {
        Schedule candidate = schedule.createCopy();

        Random rand = new Random();
        int firstTrackIndex = rand.nextInt(candidate.getTrake().size());
        int secondTrackIndex = rand.nextInt(candidate.getTrake().size());

        Track firstTrack = candidate.getTrake().get(firstTrackIndex);
        Track secondTrack = candidate.getTrake().get(secondTrackIndex);

        List<Vehicle> obeTrake = firstTrack.vozilaUOvojTraci;
        obeTrake.addAll(secondTrack.vozilaUOvojTraci);

        List<Vehicle> poTipuRasporedaPoVremenu = obeTrake.stream()
//                .sorted(Comparator.comparingInt(v -> v.tipRasporeda)) mozda staviti, mozda ne?
                .sorted(Comparator.comparingInt(v -> v.vrijemePolaska))
                .collect(Collectors.toList());

        firstTrack.vozilaUOvojTraci.clear();
        secondTrack.vozilaUOvojTraci.clear();

        for (Vehicle vehicle : poTipuRasporedaPoVremenu) {
            firstTrack.addVehicle(vehicle);
        }

        return candidate;
    }
}
