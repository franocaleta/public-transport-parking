import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Demo {

    public static void main(String[] args) {
        Data data = new Data("instanca1.txt");
//        data.printParsedData();

        List<Vehicle> vehicles = data.getAllVehicles();
        List<Track> tracks = data.getAllTracks();


//        tracks.forEach(System.out::println);
//        vehicles.forEach(System.out::println);
        
        IntStream.range(0, 20).forEach(i ->
        {
            Schedule schedule = new Schedule(
                    tracks,
                    vehicles,
                    data.getTracksThatBlockOtherTracks(),
                    data.getTracksBlockedByOtherTracks());

            System.out.println(
                    "G1:" + schedule.firstGlobalGoal()
                            + " G2: " + schedule.secondGlobalGoal()
                            + " Fitness:" + schedule.fitness()
            );
        });
    }
}
