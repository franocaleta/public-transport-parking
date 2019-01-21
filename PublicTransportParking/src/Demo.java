import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Demo {

    public static Data data;
    public static String ins = "instanca1";

    public static int brojIteracija = 0;

    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);

        Schedule currentBest = null;
        boolean min1Done = false;
        boolean min5Done = false;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            data = new Data(ins + ".txt");

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

            if (currentBest == null){
                currentBest = sce;
            }

            SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(sce);
            Schedule best = simulatedAnnealing.run();

            if (best.fitness() > currentBest.fitness()){
                currentBest = best;
                String fileName = "rjesenja/" + df.format(currentBest.fitness()) + "-" + brojIteracija + "-" + ins;
                currentBest.printScheduleToFile(fileName);
            }


            //----------------------- ispisi za 1,5,n ------------------------

            long currentTime = System.currentTimeMillis();
            if ((currentTime - startTime) / 1000 > 60 && !min1Done){
                min1Done = true;
                String fileName = "min1/" + df.format(currentBest.fitness()) + "-" + brojIteracija + "-" + ins;
                currentBest.printScheduleToFile(fileName);
            }else if ((currentTime - startTime) / 1000 > 300 && !min5Done){
                min5Done = true;
                String fileName = "min5/" + df.format(currentBest.fitness()) + "-" + brojIteracija + "-" + ins;
                currentBest.printScheduleToFile(fileName);
            }

            System.out.println(i);
        }
    }
}
