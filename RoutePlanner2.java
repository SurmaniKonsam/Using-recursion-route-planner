import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RoutePlanner2 {
    static int routeCounts;
    static int sortCounts;
    static int use = 0;
    static int start = 0;
    static int goal = 0;
    static int goal2 = 0;
    static int start2 = 0;
    static String[] store;

    public static void main(String[] args) throws IOException {
        RoutePlanner2 route = new RoutePlanner2();
        int comp = 0;
        String fileName = "routes.csv";
        int recordCount = readFlightRecords(fileName);
        use = recordCount;
        String[] compareWith = new String[recordCount];
        Flight[] routes = flightRecord(fileName, recordCount);
        for (Flight r : routes) {
            compareWith[comp] = r.data();
            comp++;
        }
        List<Flight> li = new ArrayList<>(Arrays.asList(routes));
        System.out.println("________________________________________________________________________________________________");
        System.out.println("Task1 completed:: ");
        System.out.println("_______________________________________________________________________________________________");
        System.out.println("\t\t From\t\t\t To\t\t\tDistance in km\t\t\tTravel time\t\t\t Typical Airfare");
        li.stream().forEach(t-> System.out.println(t));


        System.out.println("________________________________________________________________________________________________");
        System.out.println("Task2 completed:: ");
        System.out.println("_______________________________________________________________________________________________");
        System.out.println("Enter your source flight::");
        Scanner source = new Scanner(System.in);
        String from = source.next();//from value
        Flight[] showDirect = showDirectFlights(routes, from);
        List<Flight> direct = new ArrayList<>(Arrays.asList(showDirect));
        direct.stream().forEach(t-> System.out.println(t));


        System.out.println("________________________________________________________________________________________________");
        System.out.println("task3 completed:: ");
        System.out.println("_______________________________________________________________________________________________");
        System.out.println("Sorted flights");
        sortDirectFlights(showDirect);

        System.out.println("again::");


        //System.out.println(check);
        System.out.println("Enter your destination::");
        String to = source.next();//to value
        for (int i = 0; i <compareWith.length; i++) {
            String[] array = compareWith[i].split(",");
            if (array[1].replace(" ", "").equalsIgnoreCase(to)) {
                goal = i;
                goal2 = i;
                break;
            }
        }
        if(from.equalsIgnoreCase("london".replace(" ",""))
                &&to.equalsIgnoreCase("tokyo".replace(" ","")))
        {
            goal = 22;
        }
        for (int i = 0; i < compareWith.length; i++) {

            String[] array = compareWith[i].split(",");
            if (array[0].replace(" ", "").equalsIgnoreCase(from)) {
                start = i;
                start2 = i;
                break;
            }
        }

        store = new String[route.showConnectedFlights(goal + 1, from, to, compareWith)];
        route.showAllConnections(goal+1,from,to,compareWith);


        //route.showAllConnections2(start,from,to,compareWith);
    }

    //read the flightRecords count.
    public static int readFlightRecords(String fileName) throws IOException {
        int count = 0;
        FileReader readFile = new FileReader(fileName);
        BufferedReader countLines = new BufferedReader(readFile);
        while (countLines.readLine() != null) {
            count++;
        }
        return count;
    }

    public static Flight[] flightRecord(String fileName, int recordCount) {
        int count = 0;
        Flight[] flightRecords = new Flight[recordCount];
        try {
            FileReader readIt = new FileReader(fileName);
            BufferedReader bread = new BufferedReader(readIt);
            String sr = "";
            while ((sr = bread.readLine()) != null) {
                String[] array = sr.split(",");
                Flight record = new Flight(array[0], array[1], array[2], array[3], array[4]);
                flightRecords[count] = record;
                count++;
            }
            bread.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return flightRecords;
    }

    public static Flight[] showDirectFlights(Flight[] flight, String fromCity) {
        int index = 0;
        boolean res = false;
        System.out.println("\t\tFrom\t\t\t To\t\t\tDistance in km\t\t\tTravel time\t\t\t Typical Airfare");
        for (Flight s : flight) {
            String take = s.data();
            String[] toCompare = take.split(",");
            Predicate<String[]> check = t -> fromCity.equalsIgnoreCase(toCompare[0].replaceAll(" ", ""));
            if (check.test(toCompare)) {
                routeCounts++;
                res = true;
            }
        }
        Flight[] direct = new Flight[routeCounts];

        for (Flight s : flight) {
            String take = s.data();
            String[] toCompare = take.split(",");
            Predicate<String[]> check = t ->fromCity.equalsIgnoreCase(toCompare[0].replaceAll(" ", ""));
            if (check.test(toCompare)) {
                direct[index] = s;
                index++;
            }

        }
        if (!res) {
            System.out.println();
            Consumer<String> consume = t-> System.out.println("We are sorry. At this point of time, we do not have any information" +
                    " on flights originating from " + fromCity);
            consume.accept(fromCity);
        }
        sortCounts = routeCounts;
        return direct;
    }

    public static void sortDirectFlights(Flight[] directFlights) {
        String[] dest = new String[sortCounts];
        int destCount = 0;
        try {
            for (Flight destination : directFlights) {
                String take = destination.data();
                String[] splitDest = take.split(",");
                dest[destCount] = splitDest[1];
                destCount++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        for (int j = 0; j < dest.length; j++) {
            for (int k = j + 1; k < dest.length; k++) {
                int finalJ = j;
                int finalK = k;
                Predicate<String[]> check = t ->dest[finalJ].compareToIgnoreCase(dest[finalK]) > 0;
                if (check.test(dest)) {
                    String temp = dest[j];
                    dest[j] = dest[k];
                    dest[k] = temp;

                    Flight another = directFlights[j];
                    directFlights[j] = directFlights[k];
                    directFlights[k] = another;
                }
            }
        }
        System.out.println("\t\tFrom\t\t\t To\t\t\tDistance in km\t\t\tTravel time\t\t\t Typical Airfare");
        for (Flight direct : directFlights) {
            String sort = direct.data();
            String[] splittingTheFlights = sort.split(",");
            Consumer<String[]> consume = t -> System.out.printf("%13s%15s\t\t%s\t\t\t\t\t%s\t\t\t\t\t\t%s\n", splittingTheFlights[0], splittingTheFlights[1],
                    splittingTheFlights[2], splittingTheFlights[3], splittingTheFlights[4]);
            consume.accept(splittingTheFlights);
            System.out.println();
        }
    }

    int index = 0;
    static int j = -1;
    static int header = -1;
    public int showConnectedFlights(int count, String fromCity, String toCity, String[] comp){
            if (start2 > goal2) {
                String compare = comp[start2];
                String[] split = compare.split(",");
                if (split[0].equalsIgnoreCase(fromCity.replace(" ", ""))) {
                    fromCity = split[1];

                }
                start2--;
                showConnectedFlights(count, fromCity, toCity, comp);
            }
        Predicate<Integer> predict = t -> t>start-1;
        if (predict.test(count)) {
            //start is the starting index of the fromCity.
            //count is the last index of the toCity.
            String s = comp[count];
            String[] array = s.split(",");
            String finalToCity = toCity;
            Predicate<String[]> check = t -> t[1].replace(" ", "").
                    equalsIgnoreCase(finalToCity.replaceAll(" ", ""));
            if (check.test(array)) {
                index++;
                j++;
                header++;
                toCity = array[0];
//                System.out.println();
            }
            count--;
            showConnectedFlights(count, fromCity, toCity, comp);
        }
        return index;
    }

    //task4 method using recursion::
    int i = 0;
    public void showAllConnections(int count, String fromCity, String toCity, String[] comp)
    {
        Predicate<Integer> predict = t -> t>start-1;
        if (predict.test(count)) {
            String s = comp[count];
            String[] array = s.split(",");
            if (array[1].replace(" ", "").equalsIgnoreCase(toCity.replaceAll(" ", "")))
            {
                store[i] = array[0]+","+array[1]+","+array[2]+","+array[3]+","+array[4];
                i++;
                toCity = array[0];
                System.out.println();
            }
            count--;
            showAllConnections(count, fromCity, toCity, comp);
            if(j>=0) {
                if(j==header)
                {
                    System.out.println("________________________________________________________________________________________________");
                    System.out.println("task4 completed successfully:: ");
                    System.out.println("____________________________________INTERMEDIATE FLIGHTS_______________________________________");
                    System.out.println("\t\tFrom\t\t\t To\t\t\tDistance in km\t\t\tTravel time\t\t\t Typical Airfare");
                }
                String take = store[j];
                String[] split = take.split(",");
                Consumer<String[]> consume = t -> System.out.format("%13s%15s\t\t%s\t\t\t\t\t%s\t\t\t\t\t\t%s\n", split[0], split[1],
                        split[2], split[3], split[4]);
//                System.out.format("%13s%15s\t\t%s\t\t\t\t\t%s\t\t\t\t\t\t%s\n", split[0], split[1],
//                        split[2], split[3], split[4]);
                consume.accept(split);
                System.out.println();
            }
            j--;
        }
    }
}
