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
            boolean filled = false;
            for (Track track : trake) {
                if (track.addVehicle(vehicle)) {
                    //       System.out.println("spremljen");

                    //      filled = true;
                    break;
                }
            }
            if (!filled) {
                //   System.out.println("nije spremljen");
                filled = false;
            }
        }
    }

    public double g1min1() {
        double different_neighbours = 0.0;

        List<Track> trakeSVozilima = getTracksWithVehicles();

        for (int i = 0; i < trakeSVozilima.size() - 1; i++) {
            int serijaOve = trakeSVozilima.get(i).getVozilaUOvojTraci().get(0).serijaVozila;
            int serijaSljedece = trakeSVozilima.get(i + 1).getVozilaUOvojTraci().get(0).serijaVozila;

            if (serijaOve != serijaSljedece) different_neighbours++;
        }

        return different_neighbours / (this.trake.size() - getNumberOfEmptyTracks());
    }

    private long getNumberOfEmptyTracks() {
        return this.trake.stream().filter(track -> track.getVozilaUOvojTraci().isEmpty()).count();
    }

    private List<Track> getTracksWithVehicles() {
        return trake.stream()
                .filter(track -> !track.getVozilaUOvojTraci().isEmpty())
                .collect(Collectors.toList());
    }

    private double g1min2() {
        return (this.trake.size() - this.getNumberOfEmptyTracks()) * 1.0 / this.trake.size();
    }

    private double g1min3() {
        double sumNeiskoristenogProstoraTraka = trake.stream().mapToDouble(Track::unusedCapacity).sum();
        double sumSvihDuljinaTraka = trake.stream().mapToDouble(t -> t.duljinaTrake).sum();
        double sumDuljinaVozila = vehicles.stream().mapToDouble(v -> v.duljinaVozila).sum();

        return sumNeiskoristenogProstoraTraka / (sumSvihDuljinaTraka - sumDuljinaVozila);
    }

    public double g2min1() {
        double brojIstihParovaPoRasporedu = trake.stream()
                .mapToDouble(Track::brojSusjednihParovaUtraciSIstimTipomRasporeda)
                .sum();

        double brojKoristenihTraka = trake.size() - getNumberOfEmptyTracks();

        return brojIstihParovaPoRasporedu / (vehicles.size() - brojIstihParovaPoRasporedu);
    }

    public double g2min2() {
        double count = 0;
        double brojKoristenihTraka = trake.size() - getNumberOfEmptyTracks();

        List<Track> trakeSVozilima = getTracksWithVehicles();

        for (int i = 0; i < trakeSVozilima.size() - 1; i++) {
            int ovaZadnji = trakeSVozilima.get(i).getRasporedZadnjegVozilaUTraci();
            int sljedecaPrvi = trakeSVozilima.get(i + 1).getRasporedPrvogVozilaUTraci();

            if (ovaZadnji == sljedecaPrvi) count++;
        }

        return count / (brojKoristenihTraka - 1);
    }

    public double g2min3() {
        double sumaNagradaIPenala = 0;
        List<Track> trakeSVozilima = getTracksWithVehicles();

        for (Track track : trakeSVozilima) {
            for (int i = 0; i < track.getVozilaUOvojTraci().size() - 1; i++) {
                Vehicle ovo = track.getVozilaUOvojTraci().get(i);
                Vehicle sljedece = track.getVozilaUOvojTraci().get(i + 1);

                sumaNagradaIPenala += nagradaIliPenal(ovo, sljedece);
            }
        }

        double brojEvaluiranihParova = trakeSVozilima.stream()
                .mapToDouble(t -> t.getVozilaUOvojTraci().size() - 1)
                .sum();

        return sumaNagradaIPenala / (15 * brojEvaluiranihParova);
    }

    private double nagradaIliPenal(Vehicle ovo, Vehicle sljedece) {
        int vr = sljedece.vrijemePolaska - ovo.vrijemePolaska;

        if (10 <= vr && vr <= 20) {
            return 15;
        } else if (vr > 20) {
            return 10;
        } else {
            return -4 * (10 - vr);
        }
    }

    public double firstGlobalGoal() {
        return g1min1() + g1min2() + g1min3();
    }

    public double secondGlobalGoal() {
        return g2min1() + g2min2() + g2min3();
    }

    public double fitness() {
        // Predana rješenja će biti rangirana prema omjeru ovih funkcija: drugi_globalni_cilj / prvi_globalni_cilj.
        // Što je omjer veći, to je rješenje bolje.
        return secondGlobalGoal() / firstGlobalGoal();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int j = 0;
        for (int i = 0; i < this.trake.size(); i++) {
            Track tr = this.trake.get(i);
            for (Vehicle v : tr.getVozilaUOvojTraci()) {
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
