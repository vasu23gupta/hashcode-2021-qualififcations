import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static class Intersection{
        ArrayList<Street> incoming= new ArrayList<>();
        ArrayList<String> outgoing= new ArrayList<>();
        int id;
        void sortIncoming(){
            Collections.sort(incoming, new Street());
        }
    }

    public static class Street implements Comparator<Street> {
        @Override
        public int compare(Street o1, Street o2) {
            return -Integer.valueOf(o1.freq).compareTo(Integer.valueOf(o2.freq));
        }
        String name;
        int time;
        int freq;
        //Intersection from;
        //Intersection to;
        int from;
        int to;
        int greenTime;
    }

    public static class Car{
        int nStreets;
    }

    static int D;
    static int I;
    static int S;
    static int V;
    static int F;
    //static ArrayList<Intersection> intersections = new ArrayList<>();
    //static ArrayList<Street> streets = new ArrayList<>();
    static HashMap<String,Street> streets = new HashMap<>();
    static HashMap<Integer, Intersection> intersections = new HashMap<>();

    public static void main(String[] args) throws IOException {
        File a = new File("a.txt");
        File b = new File("b.txt");
        File c = new File("c.txt");
        File d = new File("d.txt");
        File e = new File("e.txt");
        File f = new File("f.txt");
        File file=c;

            Scanner sc = new Scanner(file);
            D = sc.nextInt();
            I = sc.nextInt();
            S = sc.nextInt();
            V = sc.nextInt();
            F = sc.nextInt();
            sc.nextLine();
            for (int i = 0; i < S; i++) {
                Street street = new Street();
                int fromInt = sc.nextInt();
                int toInt = sc.nextInt();
                street.name = sc.next();
                street.time = sc.nextInt();
                if(street.time!=D){
                    if (intersections.containsKey(fromInt) && intersections.containsKey(toInt)) {
                        intersections.get(fromInt).outgoing.add(street.name);
                        intersections.get(toInt).incoming.add(street);

                    } else if (!intersections.containsKey(fromInt) && intersections.containsKey(toInt)) {
                        Intersection intersection = new Intersection();
                        intersection.id = fromInt;
                        intersection.outgoing.add(street.name);
                        intersections.put(fromInt, intersection);
                        intersections.get(toInt).incoming.add(street);
                    } else if (intersections.containsKey(fromInt) && !intersections.containsKey(toInt)) {
                        Intersection intersection = new Intersection();
                        intersection.id = toInt;
                        intersection.incoming.add(street);
                        intersections.put(toInt, intersection);
                        intersections.get(fromInt).outgoing.add(street.name);
                    } else {
                        Intersection from = new Intersection();
                        from.id = fromInt;
                        from.outgoing.add(street.name);
                        intersections.put(fromInt, from);

                        Intersection to = new Intersection();
                        to.id = toInt;
                        to.incoming.add(street);
                        intersections.put(toInt, to);
                    }

                    street.to = toInt;
                    street.from = fromInt;
                    streets.put(street.name, street);

                    sc.nextLine();
                }
            }
            for (int i = 0; i < V; i++) {
                Car car = new Car();
                car.nStreets = sc.nextInt();
                for (int j = 0; j < car.nStreets; j++) {
                    String curStreet = sc.next();
                    streets.get(curStreet).freq++;
                }
            }
            ArrayList<Street> streetsToRemove = new ArrayList<>();
        for(Map.Entry<String, Street> entry : streets.entrySet()) {
            String streetName = entry.getKey();
            Street street = entry.getValue();
            if(street.freq==0){
                streetsToRemove.add(street);
            }
        }
        for (Street s:
             streetsToRemove) {
            streets.remove(s.name);
            for (Intersection i:
                 intersections.values()) {
                i.incoming.remove(s.name);
            }
        }
        ArrayList<Intersection> intersectionsToRemove = new ArrayList<>();
        for (Integer i:
                intersections.keySet()) {
            if(intersections.get(i).incoming.size()==0) intersectionsToRemove.add(intersections.get(i));
        }
        for (Intersection i:
             intersectionsToRemove) {
            intersections.remove(i.id);
        }
            FileWriter fileWriter = new FileWriter("ans"+file.getName());
            fileWriter.write(intersections.size()+"\n");
            for (Intersection i :
                    intersections.values()) {
                if(i.incoming.size()!=0){
                    i.sortIncoming();
                    fileWriter.write(i.id + "\n");
                    fileWriter.write(i.incoming.size() + "\n");
                    for (int j = 0; j < i.incoming.size(); j++) {
                        Street curr = i.incoming.get(j);
                        fileWriter.write(curr.name + " " + Integer.max((int) (curr.time / curr.freq), 1) + "\n");
                    }
                }
            }
            fileWriter.close();

    }
    private static Map<String, Street> sortByComparator(Map<String, Street> unsortMap, final boolean order)
    {

        List<Map.Entry<String, Street>> list = new LinkedList<Map.Entry<String, Street>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Street>>()
        {
            public int compare(Map.Entry<String, Street> o1,
                               Map.Entry<String, Street> o2)
            {
                if (order)
                {
                    return Integer.valueOf(o1.getValue().freq).compareTo(Integer.valueOf(o2.getValue().freq));
                }
                else
                {
                    return Integer.valueOf(o2.getValue().freq).compareTo(Integer.valueOf(o1.getValue().freq));

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Street> sortedMap = new LinkedHashMap<String, Street>();
        for (Map.Entry<String, Street> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
