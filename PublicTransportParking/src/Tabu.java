import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Tabu {

    private Schedule scheduleBest;
    private double fitnessBest;

    public Tabu(Schedule scheduleInitial) {
        this.scheduleBest = scheduleInitial;
        this.fitnessBest = scheduleInitial.fitness();
    }

    public Schedule run(int iterations, int printStep, boolean print) {
        Schedule candidate = scheduleBest.createCopy();

        for (int i = 0; i < iterations; i++) {
            Schedule sch1 = swapTracks(candidate);
            Schedule sch2 = reorderTracksByVehiclesScheduleType(candidate);
            Schedule sch3 = mergeTwoTracksInOne(candidate);

            if (sch1.fitness() > candidate.fitness()) {
                candidate = sch1;
            }

            if (sch2.fitness() > candidate.fitness()) {
                candidate = sch2;
            }

            if (sch3.fitness() > candidate.fitness()) {
                candidate = sch3;
            }

            if (scheduleBest.fitness() == candidate.fitness()) {
                System.out.println("Nema napretka, zaustavljam se. -> Iteracija: " + i + " Fitness: " + candidate.fitness());
                break;
            }else {
                scheduleBest = candidate;
            }

            if (print && (i % printStep == 0)) {
                System.out.println("Iteration " + i + " fitness: " + candidate.fitness());
            }
        }

        return candidate;
    }

    public Schedule swapTracks(Schedule schedule) {
        double bestFitness = schedule.fitness();
        Schedule bestSchedule = schedule;

        for (Track trackOne : schedule.getTrake()) {
            for (Track trackTwo : schedule.getTrake()) {
                if (trackOne.idTrake == trackTwo.idTrake) continue;

                Schedule candidate = schedule.createCopy();

                Track firstTrack = candidate.getTrake().get(trackOne.idTrake);
                Track secondTrack = candidate.getTrake().get(trackTwo.idTrake);

                List<Vehicle> first = firstTrack.vozilaUOvojTraci;
                List<Vehicle> second = secondTrack.vozilaUOvojTraci;

                List<Vehicle> tmp1 = new ArrayList<>(first);
                List<Vehicle> tmp2 = new ArrayList<>(second);

                firstTrack.vozilaUOvojTraci.clear();
                secondTrack.vozilaUOvojTraci.clear();

                second.addAll(tmp1);
                first.addAll(tmp2);

                if (candidate.fitness() > bestFitness && !candidate.isInvalid()) {
                    bestFitness = candidate.fitness();
                    bestSchedule = candidate;
                }
            }
        }

        return bestSchedule;
    }

    public Schedule reorderTracksByVehiclesScheduleType(Schedule schedule) {
        double bestFitness = schedule.fitness();
        Schedule bestSchedule = schedule;

        for (Track trackOne : schedule.getTrake()) {
            for (Track trackTwo : schedule.getTrake()) {
                if (trackOne.idTrake == trackTwo.idTrake) continue;

                Schedule candidate = schedule.createCopy();

                Track firstTrack = candidate.getTrake().get(trackOne.idTrake);
                Track secondTrack = candidate.getTrake().get(trackTwo.idTrake);

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

                if (candidate.fitness() > bestFitness && !candidate.isInvalid()) {
                    bestFitness = candidate.fitness();
                    bestSchedule = candidate;
                }
            }
        }

        return bestSchedule;
    }

    public Schedule mergeTwoTracksInOne(Schedule schedule) {
        double bestFitness = schedule.fitness();
        Schedule bestSchedule = schedule;

        for (Track trackOne : schedule.getTrake()) {
            for (Track trackTwo : schedule.getTrake()) {
                if (trackOne.idTrake == trackTwo.idTrake) continue;

                Schedule candidate = schedule.createCopy();

                Track firstTrack = candidate.getTrake().get(trackOne.idTrake);
                Track secondTrack = candidate.getTrake().get(trackTwo.idTrake);

                List<Vehicle> obeTrake = firstTrack.vozilaUOvojTraci;
                obeTrake.addAll(secondTrack.vozilaUOvojTraci);

                List<Vehicle> poTipuRasporedaPoVremenu = obeTrake.stream()
//                        .sorted(Comparator.comparingInt(v -> v.tipRasporeda)) mozda staviti, mozda ne ?
                        .sorted(Comparator.comparingInt(v -> v.vrijemePolaska))
                        .collect(Collectors.toList());

                firstTrack.vozilaUOvojTraci.clear();
                secondTrack.vozilaUOvojTraci.clear();

                for (Vehicle vehicle : poTipuRasporedaPoVremenu) {
                    firstTrack.addVehicle(vehicle);
                }

                if (candidate.fitness() > bestFitness && !candidate.isInvalid()) {
                    bestFitness = candidate.fitness();
                    bestSchedule = candidate;
                }
            }
        }

        return bestSchedule;
    }
}
