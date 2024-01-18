package LR0;

import Utils.Pair;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Data
public class Grammar {
    private Set<String> nonTerminals;  // Set of non-terminal symbols in the grammar
    private Set<String> terminals;  // Set of terminal symbols in the grammar
    private Map<List<String>, List<List<String>>> productions;  // Map representing the grammar productions
    private String start;  // The start symbol of the grammar
    private boolean isCFG;  // Flag indicating if the grammar is a context free grammar(CFG)
    private boolean isEnriched;  // Flag indicating if the grammar is enriched
    public static String firstState = "S0";  // The state from which an enriched grammar begins

    private static final Logger LOGGER = Logger.getLogger(Grammar.class.getName());

    public Grammar(Set<String> nonTerminals, Set<String> terminals, String start, Map<List<String>, List<List<String>>> productions) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.start = start;
        this.productions = productions;
        this.isEnriched = true;
    }

    public Grammar(String filePath) {
        this.productions = new LinkedHashMap<>();
        this.readGrammarFromFile(filePath);
    }

    public boolean getIsEnriched() {
        return this.isEnriched;
    }

    public boolean isCFG() {
        return this.isCFG;
    }

    /**
     * This method:
     * - splits the production by the left right hand side separator (" ::= ")
     * - splits the lhs symbols by space
     * - splits the rhs by "|".
     * - goes through each production from the rhs and format each of the in order to be added to the map
     *
     * @param production -> the production to be processed
     */
    private void processProduction(String production) {
        // split the production in lhs and rhs
        String[] leftAndRightHandSide = production.split(" ::= ");

        // split the lhs
        List<String> splitLHS = List.of(leftAndRightHandSide[0].split(" "));

        // split the rhs
        String[] splitRHS = leftAndRightHandSide[1].split("\\|");

        this.productions.putIfAbsent(splitLHS, new ArrayList<>());
        for (String splitRH : splitRHS) {
            this.productions.get(splitLHS).add(Arrays.stream(splitRH.split(" ")).collect(Collectors.toList()));
        }
    }

    /**
     * This method reads every line from the file and classifies everything as a terminal/non-terminal/production
     *
     * @param filePath - the path of the file to be read from
     */
    private void readGrammarFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            // read the non-terminals from the file
            this.nonTerminals = new LinkedHashSet<>(List.of(scanner.nextLine().split(" ")));

            // read the terminals from the file
            this.terminals = new LinkedHashSet<>(List.of(scanner.nextLine().split(" ")));

            // read the starting symbol of the grammar
            this.start = scanner.nextLine();

            // read the lines containing the productions
            this.productions = new LinkedHashMap<>();
            while (scanner.hasNextLine()) {
                this.processProduction(scanner.nextLine());
            }

            this.isCFG = this.checkIfCFG();
            this.isEnriched = false;
        } catch (FileNotFoundException e) {
            LOGGER.severe("Error while reading the file!");
            LOGGER.severe(e.getMessage());
        }
    }

    /**
     * This method checks if a grammar is CFG.
     *
     * @return - true if the grammar is CFG, false otherwise
     */
    private boolean checkIfCFG() {
        // check if the starting symbol is within the non-terminals
        if (!this.nonTerminals.contains(this.start)) {
            return false;
        }

        // loop through the productions
        for (List<String> leftHandSide : this.productions.keySet()) {
            // check if the lhs of each production contains only 1 non-terminal
            if (leftHandSide.size() != 1 || !this.nonTerminals.contains(leftHandSide.get(0))) {
                return false;
            }

            // loop through the lhs symbols of each production and check if any of them can be found through the
            // non-terminals/terminals or is empty
            for (List<String> possibleNextMoves : this.productions.get(leftHandSide)) {
                for (String possibleNextMove : possibleNextMoves) {
                    if (!this.nonTerminals.contains(possibleNextMove) && !this.terminals.contains(possibleNextMove) && !possibleNextMove.equals("EPSILON")) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * This method prepares the grammar for the LR0 algorithm by adding another starting state S0
     * which has the production S0 -> currentStartingSymbol
     *
     * @return - the enriched grammar
     * @throws Exception - if the grammar is already enriched, an exception is thrown
     */
    public Grammar getEnrichedGrammar() throws Exception {
        if (isEnriched) {
            throw new Exception("The grammar is already enriched!");
        }

        Grammar enrichedGrammar = new Grammar(nonTerminals, terminals, firstState, productions);

        enrichedGrammar.nonTerminals.add(firstState);
        enrichedGrammar.productions.putIfAbsent(List.of(firstState), new ArrayList<>());
        enrichedGrammar.productions.get(List.of(firstState)).add(List.of(start));

        return enrichedGrammar;
    }

    /**
     * This method goes through all the productions: for a non-terminal from the lhs, write all the productions
     * separately as pairs
     *
     * @return - a list of pairs representing each production individually
     */
    public List<Pair<String, List<String>>> getOrderedProductions() {
        List<Pair<String, List<String>>> result = new ArrayList<>();

        this.productions.forEach((lhs, rhs) -> rhs.forEach((prod) -> result.add(new Pair<>(lhs.get(0), prod))));

        return result;

    }

    /**
     * This method retrieves the productions corresponding to a non-terminal
     *
     * @param nonTerminal -  the given non-terminal
     * @return - the productions corresponding to the given non-terminal
     */
    public List<List<String>> getProductionsForNonTerminal(String nonTerminal) {
        return getProductions().get(List.of(nonTerminal));
    }
}
