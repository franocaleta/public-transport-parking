import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Data {
    private int brojVozila;
    private int brojTraka;
    private List<Integer> duljineVozila;
    private List<Integer> serijaVozila;
    private List<Integer> duljineTraka;
    private List<Integer> vrijemePolaska;
    private List<Integer> tipRasporeda;
    private Map<Integer, Set<Integer>> blokiraneTrake;
    private Map<Integer, Set<Integer>> blokirajuceTrake;
    private List<Set<Integer>> dozvoljenjeTrakeZaVozila;

    public Data(String fileName) {
        readData(fileName);
    }

    private void readData(String fileName) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("resources/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.brojVozila = Integer.parseInt(lines.get(0));
        this.brojTraka = Integer.parseInt(lines.get(1));
        this.duljineVozila = Arrays.stream(lines.get(3).split("\\s"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        this.serijaVozila = Arrays.stream(lines.get(5).split("\\s"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        this.dozvoljenjeTrakeZaVozila = new ArrayList<>();
        lines.stream()
                .skip(7)
                .limit(brojVozila)
                .forEach(line -> {
                    List<Integer> tmp = Arrays.stream(line.split("\\s"))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());

                    Set<Integer> dozvoljeneTrake = new HashSet<>();
                    for (int i = 0; i < tmp.size(); i++) {
                        if (tmp.get(i) == 1) {
                            dozvoljeneTrake.add(i);
                        }
                    }

                    dozvoljenjeTrakeZaVozila.add(dozvoljeneTrake);
                });

        this.duljineTraka = Arrays.stream(lines.get(8 + brojVozila).split("\\s"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        this.vrijemePolaska = Arrays.stream(lines.get(10 + brojVozila).split("\\s"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        this.tipRasporeda = Arrays.stream(lines.get(12 + brojVozila).split("\\s"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        this.blokiraneTrake = new HashMap<>();
        this.blokirajuceTrake = new HashMap<>();
        for (int i = 0; i < brojTraka; i++) {
            blokiraneTrake.put(i, new HashSet<>());
            blokirajuceTrake.put(i, new HashSet<>());
        }

        lines.stream()
                .skip(14 + brojVozila)
                .forEach(line -> {
                    LinkedList<Integer> parts = Arrays.stream(line.split("\\s"))
                            .map(Integer::parseInt)
                            .map(e -> e - 1)
                            .collect(Collectors.toCollection(LinkedList::new));

                    blokiraneTrake.get(parts.pop()).addAll(parts);
                });


        for (Map.Entry<Integer, Set<Integer>> entry : blokiraneTrake.entrySet()) {
            for (Integer value : entry.getValue()) {
                blokirajuceTrake.get(value).add(entry.getKey());
            }
        }
    }

    public void printParsedData() {
        System.out.println("brojVozila=" + brojVozila + "\n" +
                "brojTraka=" + brojTraka + "\n" +
                "duljineVozila=" + duljineVozila + "\n" +
                "serijaVozila=" + serijaVozila + "\n" +
                "dozvoljenjeTrakeZaVozila=" + dozvoljenjeTrakeZaVozila + "\n" +
                "duljineTraka=" + duljineTraka + "\n" +
                "vrijemePolaska=" + vrijemePolaska + "\n" +
                "tipRasporeda=" + tipRasporeda + "\n" +
                "blokiraneTrake=" + blokiraneTrake + "\n");
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vozila = new ArrayList<>();

        for (int i = 0; i < brojVozila; i++) {
            vozila.add(new Vehicle(
                    i,
                    duljineVozila.get(i),
                    serijaVozila.get(i),
                    dozvoljenjeTrakeZaVozila.get(i),
                    vrijemePolaska.get(i),
                    tipRasporeda.get(i)));
        }

        return vozila;
    }

    public List<Track> getAllTracks() {
        List<Track> trake = new ArrayList<>();

        for (int i = 0; i < brojTraka; i++) {
            trake.add(new Track(
                    i,
                    duljineTraka.get(i),
                    blokirajuceTrake.get(i),
                    blokiraneTrake.get(i)));
        }

        return trake;
    }

    public List<Integer> getTracksThatBlockOtherTracks() {
        return new ArrayList<>(blokirajuceTrake.keySet().stream().filter(t -> !blokirajuceTrake.get(t).isEmpty()).collect(Collectors.toList()));
    }

    public List<Integer> getTracksBlockedByOtherTracks(){
        return new ArrayList<>(blokiraneTrake.keySet().stream().filter(t -> !blokiraneTrake.get(t).isEmpty()).collect(Collectors.toList()));
    }
}
