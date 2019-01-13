import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Track {

    public final int idTrake;
    public final int duljinaTrake;
    public final Set<Integer> trakeKojeBlokirajuOvuTraku;
    public final Set<Integer> trakeKojeOvaTrakaBlokira;

    public List<Vehicle> vozilaUOvojTraci;

    public Track(int idTrake,
                 int duljinaTrake,
                 Set<Integer> trakeKojeBlokirajuOvuTraku,
                 Set<Integer> trakeKojeOvaTrakaBlokira) {
        this.idTrake = idTrake;
        this.duljinaTrake = duljinaTrake;
        this.trakeKojeBlokirajuOvuTraku = trakeKojeBlokirajuOvuTraku;
        this.trakeKojeOvaTrakaBlokira = trakeKojeOvaTrakaBlokira;

        vozilaUOvojTraci = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return idTrake == track.idTrake &&
                duljinaTrake == track.duljinaTrake &&
                Objects.equals(trakeKojeBlokirajuOvuTraku, track.trakeKojeBlokirajuOvuTraku) &&
                Objects.equals(trakeKojeOvaTrakaBlokira, track.trakeKojeOvaTrakaBlokira);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTrake, duljinaTrake, trakeKojeBlokirajuOvuTraku, trakeKojeOvaTrakaBlokira);
    }

    @Override
    public String toString() {
        return "Track{" +
                "idTrake=" + idTrake +
                ", duljinaTrake=" + duljinaTrake +
                ", trakeKojeBlokirajuOvuTraku=" + trakeKojeBlokirajuOvuTraku +
                ", trakeKojeOvaTrakaBlokira=" + trakeKojeOvaTrakaBlokira +
                '}';
    }

    public boolean addVehicle(Vehicle newVehicle) {

        if (checkIfTrackAlreadyFull(newVehicle.duljinaVozila)) {
            return false;
        } else if (!newVehicle.dozvoljeneTrakeZaParkiranje.contains(idTrake)) {
            return false;
        } else if (!vozilaUOvojTraci.isEmpty() && vozilaUOvojTraci.get(0).serijaVozila != newVehicle.serijaVozila) {
            return false;
        }

        return vozilaUOvojTraci.add(newVehicle);
    }

    public boolean checkIfTrackAlreadyFull(int duljinaNovogVozila) {
        return (vozilaUOvojTraci.size() - 1) * 0.5 + zbrojDuljinaDodanihVozila() + duljinaNovogVozila > duljinaTrake;
    }

    public List<Vehicle> getVozilaUOvojTraci() {
        return vozilaUOvojTraci;
    }

    public int zbrojDuljinaDodanihVozila() {
        return vozilaUOvojTraci.stream().mapToInt(value -> value.duljinaVozila).sum();
    }

    public int getVehicleTypeContaining() {
        if (vozilaUOvojTraci.isEmpty()) {
            throw new IllegalStateException("Traka nema ni jedno vozilo u sebi!");
        }
        return vozilaUOvojTraci.get(0).serijaVozila;
    }

    public double unusedCapacity() {
        return duljinaTrake - ((vozilaUOvojTraci.size() - 1) * 0.5 + zbrojDuljinaDodanihVozila());
    }

    public int brojSusjednihParovaUtraciSIstimTipomRasporeda() {
        int sum = 0;

        for (int i = 0; i < vozilaUOvojTraci.size() - 1; i++) {
            if (vozilaUOvojTraci.get(i).tipRasporeda == vozilaUOvojTraci.get(i + 1).tipRasporeda) {
                sum += 1;
            }
        }

        return sum;
    }

    public int getRasporedZadnjegVozilaUTraci(){
        if (vozilaUOvojTraci.isEmpty()){
            throw new IllegalStateException("Traka je prazna");
        }

        return vozilaUOvojTraci.get(vozilaUOvojTraci.size()-1).tipRasporeda;
    }

    public int getRasporedPrvogVozilaUTraci(){
        if (vozilaUOvojTraci.isEmpty()){
            throw new IllegalStateException("Traka je prazna");
        }

        return vozilaUOvojTraci.get(0).tipRasporeda;
    }
}
