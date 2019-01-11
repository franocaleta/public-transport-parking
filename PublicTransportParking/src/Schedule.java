import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Schedule {

    private List<Track> trake;
    private List<Vehicle> vehicles;

    public Schedule(List<Track> trake,
                    List<Vehicle> vehicles,
                    List<Integer> tracksThatBlockOtherTracks,
                    List<Integer> tracksBlockedByOtherTracks) {
        this.vehicles = getSortedehiclesByTime(vehicles);
        this.trake = getRandomConstrainedTracks(trake, tracksThatBlockOtherTracks, tracksBlockedByOtherTracks);

        fillTracksWithVehicles();
    }

    private List<Vehicle> getSortedehiclesByTime(List<Vehicle> vehicles) {
        return vehicles.stream()
                .sorted(Comparator.comparingInt(v -> v.vrijemePolaska))
                .collect(Collectors.toList());
    }

    private List<Track> getRandomConstrainedTracks(List<Track> trake,
                                                   List<Integer> tracksThatBlockOtherTracks,
                                                   List<Integer> tracksBlockedByOtherTracks) {

        List<Integer> blokirajuce = new ArrayList<>(tracksThatBlockOtherTracks);
        List<Integer> blokiraneTrake = new ArrayList<>(tracksBlockedByOtherTracks);

        Collections.shuffle(blokirajuce);
        Collections.shuffle(blokiraneTrake);

        blokirajuce.addAll(blokiraneTrake);

        List<Track> poredaneTrake = new ArrayList<>();
        for (Integer ind : blokirajuce) {
            poredaneTrake.add(trake.get(ind));
        }

        return poredaneTrake;
    }

    private void fillTracksWithVehicles() {
        for (Vehicle vehicle : vehicles) {
            boolean filled =  false;
            for (Track track : trake) {
                if (track.addVehicle(vehicle)){
             //       System.out.println("spremljen");

              //      filled = true;
                    break;
                }
            }
            if(!filled) {
             //   System.out.println("nije spremljen");
                filled = false;
            }
        }
    }

    public double g1min1() {
        double different_neighbours = 0.0;
        for (int i = 0; i < this.trake.size() - 1; i++) {
            Track first = null;
            Track second = null;
            while ((first = this.trake.get(i)).getVozilaUOvojTraci().isEmpty()) {
                i += 1;
            }
            int j = i + 1;
            while ((second = this.trake.get(j)).getVozilaUOvojTraci().isEmpty()) {
                j += 1;
            }
            if (first.getVehicleTypeContaining() != second.getVehicleTypeContaining())
                different_neighbours++;
        }
        return different_neighbours / (this.trake.size() - getNumberOfEmptyTracks());
    }

    public long getNumberOfEmptyTracks() {
        return this.trake.stream().filter(track -> track.getVozilaUOvojTraci().isEmpty()).count();
    }

    public double g2min2() {
        return (this.trake.size() - this.getNumberOfEmptyTracks()) * 1.0 / this.trake.size();
    }

    public double g3min3() {
        double sumNeiskoristenogProstoraTraka = trake.stream().mapToDouble(Track::unusedCapacity).sum();
        double sumSvihDuljinatraka = trake.stream().mapToDouble(t -> t.duljinaTrake).sum();
        double sumVozila = vehicles.stream().mapToDouble(v -> v.duljinaVozila).sum();

        return sumNeiskoristenogProstoraTraka / (sumSvihDuljinatraka - sumVozila);
    }

    public double firstGlobalGoal() {
        return g1min1() + g2min2() + g3min3();
    }

    public double g2min1() {
        //todo dovrsit ovo i sve ostale ciljeve
        //todo isprobat jednu generiranu jedinku i ispisat da vidimo jel rjesene zadovoljavajuce
        //todo napravit genetski i smislit krizanje

        double brojIstihParovaPoSeriji = trake.stream().mapToDouble(Track::brojIstihSusjednihParovaUTraci).sum();

        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int j = 0;
        for (int i = 0 ; i < this.trake.size(); i++) {
            Track tr = this.trake.get(i);
            for(Vehicle v : tr.getVozilaUOvojTraci()) {
                j++;
                sb.append(v.vrijemePolaska);
                sb.append("\n");
            }
            sb.append(".---------------------------------------------------- \n");
        }
        sb.append("vozila : " + j);
        return sb.toString();
    }
}
