import java.util.ArrayList;
import java.util.List;

public class Demo {

    public static Data data;
    public static String ins = "1";

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            ins = String.valueOf(i % 3 + 1);
            data = new Data("instanca" + ins + ".txt");

            List<Schedule> pocetneJedinke = new ArrayList<>();

            while (pocetneJedinke.size() < 40) {
                try {
                    Schedule schedule = new Schedule(
                            data.getAllTracks(),
                            data.getAllVehicles(),
                            data.getTracksThatBlockOtherTracks(),
                            data.getTracksBlockedByOtherTracks(),
                            true);

                    if (schedule.isInvalid()) continue;

                    pocetneJedinke.add(schedule);
                } catch (Exception e) {
                    continue;
                }

            }


            double fitnessMax = 0.0;
            Schedule sce = null;
            for (Schedule sc : pocetneJedinke) {
                if (sc.fitness() > fitnessMax) {
                    fitnessMax = sc.fitness();
                    sce = sc;
                }
            }
//        sce.printInColumns();
            sce.debbugFunkcije();
            System.out.println("===============================");
//        sce.printScheduleToFile("rjesenje");

            SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(sce);
            Schedule best = simulatedAnnealing.run();
//        System.out.println(best.isInvalid());

//        best.printScheduleToFile("best");
//        best.printInColumns();
            best.debbugFunkcije();
        }
    }
}
