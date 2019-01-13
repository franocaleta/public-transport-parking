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
        Schedule neighbour1 = null;
        Schedule neighbour2 = null;
        Schedule neighbour3 = null;
        for (int i = 0; i < 10000; i++) {
            System.out.println(i + " " + scheduleCurrent.fitness());

            while ((neighbour1 = swapTracks(scheduleCurrent)).isInvalid()) ;
            while ((neighbour2 = reorderTracksByVehiclesScheduleType(scheduleCurrent)).isInvalid()) ;
            while ((neighbour3 = mergeTwoTracksInOne(scheduleCurrent)).isInvalid()) ;

            Map<Double, Schedule> neighbours = new HashMap<>();
            neighbours.put(neighbour1.fitness(), neighbour1);
            neighbours.put(neighbour2.fitness(), neighbour2);
            neighbours.put(neighbour3.fitness(), neighbour3);

            double newFitness = neighbours.keySet().stream().max(Comparator.comparingDouble(v -> v)).get();
            if (newFitness > fitnessBest) {
                fitnessBest = newFitness;
                scheduleCurrent = neighbours.get(fitnessBest);
            }

        }

        return scheduleCurrent;
    }

    public Schedule swapTracks(Schedule schedule) {
        Schedule candidate = schedule.createCopy();

        Random rand = new Random();
        int firstTrackIndex = rand.nextInt(candidate.getTrake().size());
        int secondTrackIndex = rand.nextInt(candidate.getTrake().size());

        Track firstTrack = candidate.getTrake().get(firstTrackIndex);
        Track secondTrack = candidate.getTrake().get(secondTrackIndex);

        List<Vehicle> first = firstTrack.vozilaUOvojTraci;
        List<Vehicle> second = secondTrack.vozilaUOvojTraci;

        List<Vehicle> tmp1 = new ArrayList<>(first);
        List<Vehicle> tmp2 = new ArrayList<>(second);

        firstTrack.vozilaUOvojTraci.clear();
        secondTrack.vozilaUOvojTraci.clear();

        firstTrack.vozilaUOvojTraci = tmp2;
        secondTrack.vozilaUOvojTraci = tmp1;

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
