import java.util.Random;
import java.util.*;
import java.util.stream.Collectors;

public class SimulatedAnnealing {

    private Schedule scheduleBest;
    private double fitnessBest;
    private double E = Math.E;

    public SimulatedAnnealing(Schedule scheduleInitial) {
        this.scheduleBest = scheduleInitial;
        this.fitnessBest = scheduleInitial.fitness();
    }

    public Schedule run() {
//        System.out.println(this.fitnessBest);
        Schedule scheduleCurrent = this.scheduleBest;
        Schedule neighbour = null;
        //Debugging
        int numberOfAnnealings = 0;
        Random rand = new Random();
        int numberOfIterations = 10000;
        for (int i = 1; i < numberOfIterations; i++) {
            while ((neighbour = reorderTracksByVehiclesScheduleType(scheduleCurrent)).isInvalid());
            double newFitness = neighbour.fitness();
            if (newFitness > fitnessBest) {
                fitnessBest = newFitness;
                scheduleBest = neighbour;
                scheduleCurrent = neighbour;
                //          System.out.println(scheduleCurrent.fitness());
            }
            else {
                double diff = scheduleCurrent.fitness() - neighbour.fitness();
                double exp = Math.pow(E, (-diff) / (i * 1000));
                if(rand.nextDouble() < exp) {
                    numberOfAnnealings++;
                    scheduleCurrent = neighbour;
                }
            }
            // Schedule neighbour = swapTracks(scheduleCurrent);

        }
        System.out.println("numberOfAnnealings: " + numberOfAnnealings);
//        System.out.println(scheduleCurrent.fitness());
//        System.out.println(scheduleCurrent.isInvalid());
        return this.scheduleBest;
    }

    public Schedule swapTracks(Schedule schedule) {
        Random rand = new Random();
        Schedule candidate = schedule.createCopy();
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

        for(Vehicle v1 : tmp1) {
            second.add(v1);
        }

        for(Vehicle v2 : tmp2) {
            first.add(v2);
        }

       // firstTrack.vozilaUOvojTraci = tmp2;
       // secondTrack.vozilaUOvojTraci = tmp1;
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
