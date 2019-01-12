import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Demo {

    public static void main(String[] args) {
        Data data = new Data("instanca1.txt");

//        Schedule schedule = new Schedule(
//                data.getAllTracks(),
//                data.getAllVehicles(),
//                data.getTracksThatBlockOtherTracks());
//
//        schedule.printScheduleToFile("res-1m-i1");

        List<Schedule> populacija = fillPopulation(data, 1000);
    }

    private static List<Schedule> fillPopulation(Data data, int numberOfIndividuals) {
        List<Schedule> populacija = new ArrayList<>();

        while (populacija.size() < numberOfIndividuals) {
            Schedule schedule = null;
            try {
                schedule = new Schedule(
                        data.getAllTracks(),
                        data.getAllVehicles(),
                        data.getTracksThatBlockOtherTracks());
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
