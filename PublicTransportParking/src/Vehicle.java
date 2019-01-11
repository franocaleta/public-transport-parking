import java.util.Objects;
import java.util.Set;

public class Vehicle {

    public final int idVozila;
    public final int duljinaVozila;
    public final int serijaVozila;
    public final Set<Integer> dozvoljeneTrakeZaParkiranje;
    public final int vrijemePolaska;
    public final int tipRasporeda;

    public Vehicle(int idVozila,
                   int duljinaVozila,
                   int serijaVozila,
                   Set<Integer> dozvoljeneTrakeZaParkiranje,
                   int vrijemePolaska,
                   int tipRasporeda) {
        this.idVozila = idVozila;
        this.duljinaVozila = duljinaVozila;
        this.serijaVozila = serijaVozila;
        this.dozvoljeneTrakeZaParkiranje = dozvoljeneTrakeZaParkiranje;
        this.vrijemePolaska = vrijemePolaska;
        this.tipRasporeda = tipRasporeda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return idVozila == vehicle.idVozila &&
                duljinaVozila == vehicle.duljinaVozila &&
                serijaVozila == vehicle.serijaVozila &&
                vrijemePolaska == vehicle.vrijemePolaska &&
                tipRasporeda == vehicle.tipRasporeda &&
                Objects.equals(dozvoljeneTrakeZaParkiranje, vehicle.dozvoljeneTrakeZaParkiranje);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVozila, duljinaVozila, serijaVozila, dozvoljeneTrakeZaParkiranje, vrijemePolaska, tipRasporeda);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "idVozila=" + idVozila +
                ", duljinaVozila=" + duljinaVozila +
                ", serijaVozila=" + serijaVozila +
                ", dozvoljeneTrakeZaParkiranje=" + dozvoljeneTrakeZaParkiranje +
                ", vrijemePolaska=" + vrijemePolaska +
                ", tipRasporeda=" + tipRasporeda +
                '}';
    }
}
