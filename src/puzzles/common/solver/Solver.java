package puzzles.common.solver;

import java.util.*;

public class Solver  {
    // TODO
    private static HashMap<Configuration, Configuration> map = new HashMap<>();
    private static List<Configuration> queue = new LinkedList<>();
    private static Configuration start = null;
    private static Configuration current;

    private static int totConfigCount;
    private static int uniConfigCount;
    public Solver(){
        this.totConfigCount = 0;
        this.uniConfigCount = 0;
    }
    public static Collection<Configuration> Solve(Configuration arg){
        queue.add(arg);
        map.put(arg, start);
        while(!queue.isEmpty()) {
            current = queue.remove(0);
            if (!current.isSolution()) {
                Collection<Configuration> b = current.getNeighbors();
                uniConfigCount++;
                for (Configuration child : b) {
                    if (!map.containsKey(child)) {
                        map.put(child,current);
                        queue.add(child);
                        totConfigCount++;
                    }
                }
            }
            if (current.isSolution()) {
                break;
            }
        }
        return constructPath(arg,current);
    }

    public static List<Configuration> constructPath(Configuration start, Configuration end){
        List<Configuration> path = new LinkedList<>();
        if(map.containsKey(end)){
            Configuration currNode = end;
            while(currNode != start){
                path.add(0,currNode);
                currNode = map.get(currNode);
            }
            path.add(0,start);
        }
        map = new HashMap<>();
        return path;
    }

    public static  int getTotConfigCount(){
        return totConfigCount;
    }
    public static int getUniConfigCount(){
        return uniConfigCount;
    }

}
