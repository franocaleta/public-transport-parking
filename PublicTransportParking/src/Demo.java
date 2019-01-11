import java.util.List;
import java.util.stream.Collectors;

public class Demo {

    public static void main(String[] args) {
        Data data = new Data("instanca1.txt");
//        data.printParsedData();

        List<Vehicle> vehicles = data.getAllVehicles();
        List<Track> tracks = data.getAllTracks();
        List<Track> bTracks = tracks.stream().filter(track -> !track.trakeKojeOvaTrakaBlokira.isEmpty()).collect(Collectors.toList());
        List<Integer> blockingTracks = tracks.stream().filter(track -> !track.trakeKojeOvaTrakaBlokira.isEmpty()).map(t -> t.idTrake).collect(Collectors.toList());
        List<Integer> blockedTracks = tracks.stream().filter(
                track -> bTracks.stream().flatMap(tr ->
                        tr.trakeKojeOvaTrakaBlokira.stream()).collect(Collectors.toList()).contains(track.idTrake)).map(track -> track.idTrake).collect(Collectors.toList());

        blockingTracks.forEach(System.out::println);
        System.out.println("");
        blockedTracks.forEach(System.out::println);

        tracks.forEach(System.out::println);
//        vehicles.forEach(System.out::println);
        for (int i = 0; i< 20; i++) {
       //     Schedule sc = new Schedule(tracks, vehicles, blockingTracks, blockedTracks);
      //      System.out.println(sc.firstGlobalGoal());

        }
      //  Schedule sc = new Schedule(tracks, vehicles, blockingTracks, blockedTracks);
      //  System.out.println(sc);

    }
}
