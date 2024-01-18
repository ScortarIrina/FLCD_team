import LR0.CanonicalCollection;
import LR0.Grammar;
import LR0.LR0;
import ParsingTable.ParsingTable;
import Tests.Tests;
import Utils.Pair;
import Scanner.*;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static void printToFile(String filePath, Object object) {
        try (PrintStream printStream = new PrintStream(filePath)) {
            printStream.println(object);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error writing to file: " + filePath, e);
        }
    }

    public static void printMenu() {
        System.out.println("\n\n~~~~~~~~~~~~~~~~ MENU ~~~~~~~~~~~~~~~\n");
        System.out.println("0. Exit");
        System.out.println("1. To print non-terminals, press 1.");
        System.out.println("2. To print terminals, press 2.");
        System.out.println("3. To print the starting symbol, press 3.");
        System.out.println("4. To print all productions, press 4.");
        System.out.println("5. To print all productions for a non-terminal, press 5.");
        System.out.println("6. To check if the grammar is a context-free grammar (CFG), press 6.");
        System.out.println("7. To run LR0 for G1.txt and parse sequence.txt, press 7.");
        System.out.println("8. To run LR0 for G2.txt, press 8.");
        System.out.println("9. To run all tests for the grammar and LR0, press 9.");
    }

    public static void printMenuParser() {
        System.out.println("\n0. Exit");
        System.out.println("1. Parse P1.txt");
        System.out.println("2. Parse P2.txt");
        System.out.println("3. Parse P3.txt\n");
    }

    public static void runGrammar() throws Exception {
        Grammar grammar = new Grammar("src/IO/G1.txt");
        boolean isMenuRunning = true;
        while (isMenuRunning) {
            printMenu();
            Scanner keyboard = new Scanner(System.in);
            System.out.print("\nChoose an option (0-9): ");
            int option = keyboard.nextInt();
            if (option == 0) {
                isMenuRunning = false;
            } else if (option == 1) {
                System.out.println("\nNon-terminals -> " + grammar.getNonTerminals());
            } else if (option == 2) {
                System.out.println("\nTerminals -> " + grammar.getTerminals());
            } else if (option == 3) {
                System.out.println("\nStarting symbol -> " + grammar.getStartingSymbol());
            } else if (option == 4) {
                System.out.print("\nAll productions:\n");
                grammar.getProductions().forEach((lhs, rhs) -> System.out.println("\t" + lhs + " -> " + rhs));
            } else if (option == 5) {
                Scanner sc = new Scanner(System.in); //System.in is a standard input stream.
                System.out.print("Enter a non-terminal: ");
                String nonTerminal = sc.nextLine(); //reads string.
                System.out.println("\nProductions of the non-terminal: ");
                List<String> key = new ArrayList<>();
                key.add(nonTerminal);
                try {
                    grammar.getProductions().get(key).forEach((rhs) -> System.out.println(key + " -> " + rhs));
                } catch (NullPointerException e) {
                    System.out.println("Error! This non-terminal doesn't exist!");
                }
            } else if (option == 6) {
                boolean result = grammar.isCFG();
                if (result) {
                    System.out.println("\nThe grammar is CFG (context-free grammar).");
                } else {
                    System.out.println("\nThe grammar is not CFG.");
                }
            } else if (option == 7) {
                emptyFile("src/IO/Out1.txt");

                Grammar grammar1 = new Grammar("src/IO/G1.txt");
                LR0 lrAlg = new LR0(grammar1);

                CanonicalCollection canonicalCollection = lrAlg.canonicalCollection();

                System.out.println("States");
                writeToFile("src/IO/Out2.txt", "States");

                for (int i = 0; i < canonicalCollection.getStates().size(); i++) {
                    System.out.println("\tstate " + i + " " + canonicalCollection.getStates().get(i));
                }

                System.out.println("\nState transitions");
                writeToFile("src/IO/Out2.txt", "\nState transitions");

                for (Map.Entry<Pair<Integer, String>, Integer> entry : canonicalCollection.getAdjacencyList().entrySet()) {
                    System.out.println("\t" + entry.getKey() + " -> " + entry.getValue());
                    writeToFile("src/IO/Out2.txt", entry.getKey() + " -> " + entry.getValue());
                }

                System.out.println();

                ParsingTable parsingTable = lrAlg.getParsingTable(canonicalCollection);
                if (parsingTable.entries.isEmpty()) {
                    System.out.println("There are conflicts in the parsing table. We can't go further with the algorithm.");
                    writeToFile("src/IO/Out2.txt", "There are conflicts in the parsing table. We can't go further with the algorithm.");
                } else {
                    System.out.println(parsingTable);
                    writeToFile("src/IO/Out2.txt", parsingTable.toString());
                }

                Stack<String> word = readSequence("src/IO/sequence.txt");

                lrAlg.parse(word, parsingTable, "src/IO/Out1.txt");

            } else if (option == 8) {
                Grammar grammar2 = new Grammar("src/IO/G2.txt");
                LR0 lrAlg2 = new LR0(grammar2);

                CanonicalCollection canonicalCollection2 = lrAlg2.canonicalCollection();

                System.out.println("States");
                for (int i = 0; i < canonicalCollection2.getStates().size(); i++) {
                    System.out.println(i + " " + canonicalCollection2.getStates().get(i));
                }

                System.out.println("\nState transitions");
                for (Map.Entry<Pair<Integer, String>, Integer> entry : canonicalCollection2.getAdjacencyList().entrySet()) {
                    System.out.println("\t" + entry.getKey() + " -> " + entry.getValue());
                }
                System.out.println();

                ParsingTable parsingTable2 = lrAlg2.getParsingTable(canonicalCollection2);
                if (parsingTable2.entries.isEmpty()) {
                    System.out.println("There are conflicts in the parsing table. We can't go further with the algorithm.");
                    writeToFile("src/IO/Out2.txt", "There are conflicts in the parsing table. We can't go further with the algorithm.");
                } else {
                    System.out.println(parsingTable2);
                }


                boolean stop = false;
                while (!stop) {
                    printMenuParser();
                    Scanner keyboard2 = new Scanner(System.in);
                    System.out.print("\nChoose an option (0-3): ");
                    int option2 = keyboard2.nextInt();

                    if (option2 == 0) {
                        stop = true;
                    } else if (option2 == 1) {
                        emptyFile("src/IO/Out2.txt");
                        MyScanner scanner2 = new MyScanner("src/IO/p1.txt");
                        scanner2.scan();
                        printToFile("src/IO/p1.txt".replace(".txt", "PIF.txt"), scanner2.getPif());

                        Stack<String> word2 = readFirstElemFromFile("src/IO/p1PIF.txt");

                        lrAlg2.parse(word2, parsingTable2, "src/IO/Out2.txt");
                    } else if (option2 == 2) {
                        emptyFile("src/IO/Out2.txt");
                        MyScanner scanner3 = new MyScanner("src/IO/p2.txt");
                        scanner3.scan();
                        printToFile("src/IO/p2.txt".replace(".txt", "PIF.txt"), scanner3.getPif());

                        Stack<String> word3 = readFirstElemFromFile("src/IO/p2PIF.txt");

                        lrAlg2.parse(word3, parsingTable2, "src/IO/Out2.txt");
                    } else if (option2 == 3) {
                        emptyFile("src/IO/Out2.txt");
                        MyScanner scanner4 = new MyScanner("src/IO/p3.txt");
                        scanner4.scan();
                        printToFile("src/IO/p3.txt".replace(".txt", "PIF.txt"), scanner4.getPif());

                        Stack<String> word4 = readFirstElemFromFile("src/IO/p3PIF.txt");

                        lrAlg2.parse(word4, parsingTable2, "src/IO/Out2.txt");
                    }
                }
            } else if (option == 9) {
                Tests test = new Tests();
                test.runAllClosureTest();
                test.runAllGoToTests();
                test.runAllCanonicalTests();
            }
        }
    }

    public static Stack<String> readSequence(String filename) {
        BufferedReader reader;
        Stack<String> wordStack = new Stack<>();
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            if (line != null) {
                Arrays.stream(new StringBuilder(line).reverse().toString().split("")).forEach(wordStack::push);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordStack;
    }

    public static Stack<String> readFirstElemFromFile(String filename) {
        BufferedReader reader;
        Stack<String> wordStack = new Stack<>();
        ArrayList<String> normal = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.split("\\s+");
                normal.add(split[0]);
                line = reader.readLine();
            }
            for (int i = normal.size() - 1; i >= 0; i--) {
                wordStack.add(normal.get(i));
            }
            return wordStack;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void emptyFile(String file) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();
    }

    public static void writeToFile(String file, String line) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line);
        bw.newLine();
        bw.close();
    }

    public static void main(String[] args) throws Exception {
        runGrammar();
    }
}
