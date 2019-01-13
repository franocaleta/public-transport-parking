import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Demo {

    public static Data data;
    public static void main(String[] args) {
        data = new Data("instanca1.txt");

        Schedule schedule = new Schedule(
                data.getAllTracks(),
                data.getAllVehicles(),
                data.getTracksThatBlockOtherTracks(),
                data.getTracksBlockedByOtherTracks(),
                true);

      //  schedule.printScheduleToFile("res-1m-i1");
      //  schedule.debbugFunkcije();

        double fitnessMax = 0.0;
        Schedule sce = null;
        List<Schedule> populacija = fillPopulation(data, 20);
        for(Schedule sc : populacija) {
            System.out.println(sc.isInvalid());
            if(sc.fitness() > fitnessMax) {
                fitnessMax = sc.fitness();
                sce  = sc;
            }
        }
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(sce);
        Schedule best = simulatedAnnealing.run();
        best.printScheduleToFile("best");
     //   best.getTrake().stream().forEach(track -> System.out.println(track.duljinaTrake+" Unused"+ track.unusedCapacity()));
    //    best.debbugFunkcije();

   //     sce.printScheduleToFile("rjesenje");
        System.out.println(fitnessMax);
    }

    private static List<Schedule> fillPopulation(Data data, int numberOfIndividuals) {
        List<Schedule> populacija = new ArrayList<>();

        while (populacija.size() < numberOfIndividuals) {
            Schedule schedule = null;
            try {
                schedule = new Schedule(
                        data.getAllTracks(),
                        data.getAllVehicles(),
                        data.getTracksThatBlockOtherTracks(),
                        data.getTracksBlockedByOtherTracks(),
                        true);
            } catch (IllegalStateException ignorable) {
                //bacit ce exception kada nisu sva vozila raspodijeljena --> vidi track#addVehicle()
                //todo: mozda vidit zasto se to dogada da nekad vozila nisu raspodijeljena
                continue;
            }

            populacija.add(schedule);
        }

        return populacija;
    }
}
