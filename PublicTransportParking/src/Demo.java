import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Demo {

    public static Data data;

    public static void main(String[] args) {
        //     Data data = new Data("instanca1.txt");
//        data.printParsedData();

        //       List<Vehicle> vehicles = data.getAllVehicles();
        //      List<Track> tracks = data.getAllTracks();


//        tracks.forEach(System.out::println);
//        vehicles.forEach(System.out::println);
        Demo.data = new Data("instanca1.txt");
        IntStream.range(0, 20).forEach(i ->
        {
            Demo.data = new Data("instanca1.txt");
            Schedule schedule = new Schedule(
                    new ArrayList<>(data.getAllTracks()),
                    new ArrayList<>(data.getAllVehicles()),
                    new ArrayList<>(data.getTracksThatBlockOtherTracks()),
                    new ArrayList<>(data.getTracksBlockedByOtherTracks()));

            System.out.println(
                    "G1:" + schedule.firstGlobalGoal()
                            + " G2: " + schedule.secondGlobalGoal()
                            + " Fitness:" + schedule.fitness()
            );
            int size = 0;
            List<Track> trake = schedule.getTrake();
            for (int j = 0; j < trake.size(); j++) {
                Track tr = trake.get(j);
                size += tr.getVozilaUOvojTraci().size();
            }
            System.out.println("Size" + size);
        });
    }
}
