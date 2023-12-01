package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import static puzzles.common.solver.Solver.Solve;

public class Hoppers {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }
        try {
            HoppersConfig ht = new HoppersConfig(args[0]);
            System.out.println("File: " + args[0]);
            System.out.println(ht.toString());
            LinkedList<Configuration> a = (LinkedList<Configuration>) Solve(ht);
            System.out.println("Total configs: " + Solver.getTotConfigCount());
            System.out.println("Unique configs: " + Solver.getUniConfigCount());
            for(int i = 0; i < a.size();i++){
                if(!a.get(a.size() - 1).isSolution()){
                    System.out.println("No Solution");
                    break;
                }
                System.out.println("\nStep " + i + ":");
                System.out.println(a.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
