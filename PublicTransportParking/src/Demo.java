import java.util.List;
import java.util.stream.Collectors;

public class Demo {

    public static void main(String[] args) {
        Data data = new Data("instanca1.txt");
//        data.printParsedData();

        List<Vehicle> vehicles = data.getAllVehicles();
        List<Track> tracks = data.getAllTracks();


//        tracks.forEach(System.out::println);
//        vehicles.forEach(System.out::println);

        Schedule schedule = new Schedule(
                tracks,
                vehicles,
                data.getTracksThatBlockOtherTracks(),
                data.getTracksBlockedByOtherTracks());

        System.out.println(schedule.firstGlobalGoal());

    }
}
