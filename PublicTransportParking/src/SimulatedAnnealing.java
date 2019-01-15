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
        boolean min1Done = false;
        boolean min5Done = false;
        long start = System.currentTimeMillis();
//        System.out.println(this.fitnessBest);
        Schedule scheduleCurrent = this.scheduleBest;
        Schedule neighbour = null;
        //Debugging
        int numberOfAnnealings = 0;
        Random rand = new Random();
        int numberOfIterations = 4000000;
        int before = 0;
        int after = 0;
        for (int i = 0; i < 1000; i++) {
            while ((neighbour = mergeTwoTracksInOne(scheduleCurrent)).isInvalid()) ;
            double newFitness = neighbour.fitness();
            if (newFitness >= this.scheduleBest.fitness()) {
                scheduleBest = neighbour;
                scheduleCurrent = neighbour;
                //          System.out.println(scheduleCurrent.fitness());
            }
        }
        scheduleCurrent = scheduleBest;
        for (int i = 1; i < numberOfIterations; i++) {
            if (new Random().nextDouble() < 0.3) {
                while ((neighbour = swapTracks(scheduleCurrent)).isInvalid()) ;
            } else {
                while ((neighbour = reorderTracksByVehiclesScheduleType(scheduleCurrent)).isInvalid()) ;
            }

            double newFitness = neighbour.fitness();
            if (newFitness >= this.scheduleBest.fitness()) {
                //           fitnessBest = newFitness;
                scheduleBest = neighbour;
                scheduleCurrent = neighbour;
                //          System.out.println(scheduleCurrent.fitness());
            } else if (newFitness >= scheduleCurrent.fitness()) {
                scheduleCurrent = neighbour;
            } else {

                double diff = scheduleCurrent.fitness() - neighbour.fitness();
                if (diff > 0) {
                    diff *= -1;
                    diff *= numberOfIterations;
                    double T = i;
                    double expression = diff / T;
                    double exp = Math.pow(E, expression);

                    double random = rand.nextDouble();
                    //          System.out.println("Random : "+ random +  " Exp: "+ exp + " Diff: "+ diff +" i:" + i + "Expression: " + expression);
                    if (Math.abs(random) < exp) {
                        if (i < numberOfIterations / 2) {
                            //                System.out.println("i :" + i+"  Exp:" + exp+ " Random: "+ random);
                            before++;
                        } else {
                            after++;
                        }
                        numberOfAnnealings++;
                        scheduleCurrent = neighbour;
                    }
                }

            }
            if (scheduleCurrent.fitness() > this.scheduleBest.fitness()) {
                fitnessBest = scheduleCurrent.fitness();
                scheduleBest = scheduleCurrent;
            }
            // Schedule neighbour = swapTracks(scheduleCurrent);
            long temp = System.currentTimeMillis();
            if((temp - start) / 1000 > 300 && !min5Done) {
                scheduleBest.printScheduleToFile("best5min" + i + "-" + scheduleBest.fitness());
//                System.out.println(i);
                min5Done = true;
            }else if((temp - start) / 1000 > 60 && !min1Done) {
                scheduleBest.printScheduleToFile("best1min" + i + "-" + scheduleBest.fitness());
//                System.out.println(i);
                min1Done = true;
            }else if (i == numberOfIterations - 1){
                scheduleBest.printScheduleToFile("bestTotal" + i + "-" + scheduleBest.fitness());
            }
        }
        System.out.println("numberOfAnnealings: " + numberOfAnnealings);
        System.out.println("before: " + before);
        System.out.println("after: " + after);
        long end = System.currentTimeMillis();

        System.out.println("Time elapsed: " + (end - start) / 1000);
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

        for (Vehicle v1 : tmp1) {
            second.add(v1);
        }

        for (Vehicle v2 : tmp2) {
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
