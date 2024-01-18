package LR0;

import ParsingTable.ParsingTable;
import ParsingTable.ParsingTableRow;
import ParsingTree.OutputTree;
import State.*;
import Utils.Pair;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Data
public class LR0 {
    private final Grammar grammar;
    private final Grammar workingGrammar;
    private List<Pair<String, List<String>>> productions;

    public LR0(Grammar grammar) throws Exception {
        this.grammar = grammar;

        if (this.grammar.getIsEnriched()) {
            this.workingGrammar = this.grammar;
        } else {
            this.workingGrammar = this.grammar.getEnrichedGrammar();
        }

        productions = this.grammar.getOrderedProductions();
    }

    /**
     * This method retrieves the non-terminal which appears before the dot in an item
     *
     * @param item - the given item
     * @return - the non-terminal before the dot, null otherwise
     */
    public String getNonTerminalBeforeDot(Item item) {
        try {
            String term = item.getRightHandSide().get(item.getPositionForDot());
            if (!grammar.getNonTerminals().contains(term)) {
                return null;
            }

            return term;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * This method computes the closure for an item (an item being of the form [A->alpha.beta])
     *
     * @param item - the given element
     * @return - the closure for the given item
     */
    public State closure(Item item) {

        Set<Item> oldClosure;
        Set<Item> currentClosure = Set.of(item);

        do {
            oldClosure = currentClosure;
            Set<Item> newClosure = new LinkedHashSet<>(currentClosure);
            for (Item i : currentClosure) {
                String nonTerminal = getNonTerminalBeforeDot(i);
                if (nonTerminal != null) {
                    for (List<String> prod : grammar.getProductionsForNonTerminal(nonTerminal)) {
                        Item currentItem = new Item(nonTerminal, prod, 0);
                        newClosure.add(currentItem);
                    }
                }
            }
            currentClosure = newClosure;
        } while (!oldClosure.equals(currentClosure));

        return new State(currentClosure);
    }

    /**
     * In state S, we search for the LR0 item that has dot in front of symbol X.
     * Move the dot after symbol X and call closure for this new item.
     *
     * @param state - the state S from which we want to move
     * @param elem  - the symbol after we look
     * @return - returns a State containing  a list of states composed of the states for each computer closure
     */
    public State goTo(State state, String elem) {
        Set<Item> result = new LinkedHashSet<>();

        // loop through each LR(0) item in the current state
        for (Item i : state.getItems()) {
            try {
                // Get the symbol after the dot in the current LR0 item
                String nonTerminal = i.getRightHandSide().get(i.getPositionForDot());

                // Check if the symbol after the dot matches the symbol we are looking for
                // Move the dot after the symbol and call closure for the new item
                if (Objects.equals(nonTerminal, elem)) {
                    Item nextItem = new Item(i.getLeftHandSide(), i.getRightHandSide(), i.getPositionForDot() + 1);
                    State newState = closure(nextItem);

                    // Add the items of the new state to the result
                    result.addAll(newState.getItems());
                }
            } catch (Exception ignored) {
            }
        }

        return new State(result);
    }

    /**
     * This method retrieves the canonical collection for the grammar
     *
     * @return - the canonical collection
     */
    public CanonicalCollection getCanonicalCollectionForGrammar() {
        CanonicalCollection canonicalCollection = new CanonicalCollection();

        // add the closure of the initial item to the collection
        canonicalCollection.addState(
                closure(
                        new Item(
                                workingGrammar.getStart(),
                                workingGrammar.getProductionsForNonTerminal(workingGrammar.getStart()).get(0),
                                0
                        )
                )
        );

        // go through each state from the collection and compute the closure for each symbol
        int index = 0;
        while (index < canonicalCollection.getStates().size()) {
            for (String symbol : canonicalCollection.getStates().get(index).getSymbolsSucceedingTheDot()) {
                State newState = goTo(canonicalCollection.getStates().get(index), symbol);
                if (!newState.getItems().isEmpty()) {
                    int indexState = canonicalCollection.getStates().indexOf(newState);
                    if (indexState == -1) {
                        canonicalCollection.addState(newState);
                        indexState = canonicalCollection.getStates().size() - 1;
                    }
                    canonicalCollection.connectStates(index, indexState, symbol);
                }
            }
            ++index;
        }
        return canonicalCollection;

    }

    /**
     * This method creates the parsing table and detects conflicts if there are any
     *
     * @return - the parsing table corresponding to the parsed grammar if we don't have conflicts,
     * otherwise, return a table with no rows in it
     */
    public ParsingTable getParsingTable(CanonicalCollection canonicalCollection) throws Exception {
        ParsingTable parsingTable = new ParsingTable();

        for (int i = 0; i < this.getCanonicalCollectionForGrammar().getStates().size(); i++) {

            State state = this.getCanonicalCollectionForGrammar().getStates().get(i);

            // We create a new row entry for our parsing table
            ParsingTableRow row = new ParsingTableRow();

            // We set the number of the state (the index)
            row.stateIndex = i;


            // We set the action of the state (SHIFT/REDUCE/ACCEPT)
            row.action = state.getStateActionType();

            // We initialize the shifts list, in case there will be the case to add them.
            row.shifts = new ArrayList<>();

            // If we have any of the two conflicts, we display the state and the symbol and stop the algorithm
            // If we have conflicts we cant go further
            if (state.getStateActionType() == ActionTypeEnum.SHIFT_REDUCE_CONFLICT || state.getStateActionType() == ActionTypeEnum.REDUCE_REDUCE_CONFLICT) {
                for (Map.Entry<Pair<Integer, String>, Integer> e2 : canonicalCollection.getAdjacencyList().entrySet()) {
                    Pair<Integer, String> k2 = e2.getKey();
                    Integer v2 = e2.getValue();

                    if (v2.equals(row.stateIndex)) {
                        System.out.println("STATE INDEX -> " + row.stateIndex);
                        writeToFile("src/IO/Out2.txt", "STATE INDEX -> " + row.stateIndex);
                        System.out.println("SYMBOL -> " + k2.getSecond());
                        writeToFile("src/IO/Out2.txt", "SYMBOL -> " + k2.getSecond());
                        System.out.println("INITIAL STATE -> " + k2.getFirst());
                        writeToFile("src/IO/Out2.txt", "INITIAL STATE -> " + k2.getFirst());
                        System.out.println("( " + k2.getFirst() + ", " + k2.getSecond() + " )" + " -> " + row.stateIndex);
                        writeToFile("src/IO/Out2.txt", "( " + k2.getFirst() + ", " + k2.getSecond() + " )" + " -> " + row.stateIndex);
                        System.out.println("STATE -> " + state);
                        writeToFile("src/IO/Out2.txt", "STATE -> " + state);

                        break;
                    }
                }
                parsingTable.entries = new ArrayList<>();
                return parsingTable;

                // If the action is REDUCE, it means the state has only one item, which has the dot at the end
                // So we set the reduceNonTerminal, which is the lhs and also set the reduce content which
                // is the rhs
            } else if (state.getStateActionType() == ActionTypeEnum.REDUCE) {
                Item item = state.getItems().stream().filter(Item::dotIsLast).findAny().orElse(null);
                if (item != null) {
                    row.shifts = null;
                    row.reduceNonTerminal = item.getLeftHandSide();
                    row.reduceContent = item.getRightHandSide();
                } else {
                    throw new Exception("");
                }
                // If the action is ACCEPT, we just initialize all the other left fields with null, because the action was
                // set in the beginning
            } else if (state.getStateActionType() == ActionTypeEnum.ACCEPT) {
                row.reduceContent = null;
                row.reduceNonTerminal = null;
                row.shifts = null;
                // If the action is SHIFT, we need to look for all the new states that are created from the initial
                // state and add them to the shifts list
            } else if (state.getStateActionType() == ActionTypeEnum.SHIFT) {

                List<Pair<String, Integer>> goTos = new ArrayList<>();

                for (Map.Entry<Pair<Integer, String>, Integer> entry : canonicalCollection.getAdjacencyList().entrySet()) {

                    Pair<Integer, String> key = entry.getKey();
                    if (key.getFirst() == row.stateIndex) {
                        goTos.add(new Pair<>(key.getSecond(), entry.getValue()));
                    }
                }

                row.shifts = goTos;
                row.reduceContent = null;
                row.reduceNonTerminal = null;
            }

            parsingTable.entries.add(row);
        }

        return parsingTable;
    }


    /**
     * This method parses the input sequence and finds if it is accepted by the grammar or not
     *
     * @param sequence     - the sequence
     * @param parsingTable - the parsing table which we will use in order to parse
     * @param filePath     - the file path where we will display the parse result
     * @throws IOException - in case of input output exception for writing/reading the file
     */
    public void parse(Stack<String> sequence, ParsingTable parsingTable, String filePath) throws IOException {
        Stack<Pair<String, Integer>> workingStack = new Stack<>();
        Stack<String> outputStack = new Stack<>();
        Stack<Integer> outputNumberStack = new Stack<>();

        String lastSymbol = "";
        int stateIndex = 0;

        boolean sem = true;

        workingStack.push(new Pair<>(lastSymbol, stateIndex));
        ParsingTableRow lastRow = null;
        String onErrorSymbol = null;

        try {
            do {
                if (!sequence.isEmpty()) {
                    // store the current symbol in case an error will occur
                    onErrorSymbol = sequence.peek();
                }
                // update the last row from the table we worked with
                lastRow = parsingTable.entries.get(stateIndex);

                // copy the entry from the table at the current index and work on it
                ParsingTableRow entry = parsingTable.entries.get(stateIndex);

                if (entry.action.equals(ActionTypeEnum.SHIFT)) {
                    // - if the action is shift, we pop from the input stack
                    // - we look at the last added state from the working stack
                    // - look into the parsing table at that state, and find out from it through what state,
                    // we can obtain the symbol popped from the input stack
                    String symbol = sequence.pop();
                    Pair<String, Integer> state = entry.shifts.stream().filter(it -> it.getFirst().equals(symbol)).findAny().orElse(null);

                    if (state != null) {
                        stateIndex = state.getSecond();
                        lastSymbol = symbol;
                        workingStack.push(new Pair<>(lastSymbol, stateIndex));
                    } else {
                        throw new NullPointerException("Action is SHIFT but there are no matching states");
                    }
                } else if (entry.action.equals(ActionTypeEnum.REDUCE)) {

                    List<String> reduceContent = new ArrayList<>(entry.reduceContent);

                    while (reduceContent.contains(workingStack.peek().getFirst()) && !workingStack.isEmpty()) {
                        reduceContent.remove(workingStack.peek().getFirst());
                        workingStack.pop();
                    }

                    // We look into the row of the last state from the working stack
                    // We look through the shift values and look for the one that corresponds to the reduceNonTerminal
                    // Basically, we look through which state, from the current one, we can obtain that non-terminal
                    Pair<String, Integer> state = parsingTable.entries.get(workingStack.peek().getSecond()).shifts.stream()
                            .filter(it -> it.getFirst().equals(entry.reduceNonTerminal)).findAny().orElse(null);

                    stateIndex = state.getSecond();
                    lastSymbol = entry.reduceNonTerminal;
                    workingStack.push(new Pair<>(lastSymbol, stateIndex));

                    outputStack.push(entry.reduceProductionString());

                    // We "form" the production used for reduction and look for its production number
                    var index = new Pair<>(entry.reduceNonTerminal, entry.reduceContent);
                    int productionNumber = this.productions.indexOf(index);

                    outputNumberStack.push(productionNumber);
                } else {
                    if (entry.action.equals(ActionTypeEnum.ACCEPT)) {
                        List<String> output = new ArrayList<>(outputStack);
                        Collections.reverse(output);
                        List<Integer> numberOutput = new ArrayList<>(outputNumberStack);
                        Collections.reverse(numberOutput);

                        System.out.println("\nThe sequence is ACCEPTED");
                        writeToFile(filePath, "\nThe sequence is ACCEPTED");

                        System.out.println("\nProduction strings:");
                        writeToFile(filePath, "\nProduction strings:");
                        for (String production : output) {
                            System.out.println("\t" + production);
                            writeToFile(filePath, "\t" + production);
                        }

                        System.out.println("\nProductions numbers: " + numberOutput);
                        writeToFile(filePath, "\nProductions numbers: " + numberOutput);

                        OutputTree outputTree = new OutputTree(grammar);
                        outputTree.generateTreeFromSequence(numberOutput);
                        System.out.println("\nThe output tree: ");
                        writeToFile(filePath, "\nThe output tree: ");
                        outputTree.printTree(outputTree.getRoot(), filePath);


                        sem = false;
                    }

                }
            } while (sem);
        } catch (NullPointerException ex) {
            System.out.println("ERROR at state " + stateIndex + " - before symbol " + onErrorSymbol);
            System.out.println(lastRow);

            writeToFile(filePath, "ERROR at state " + stateIndex + " - before symbol " + onErrorSymbol);
            writeToFile(filePath, lastRow.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToFile(String file, String line) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line);
        bw.newLine();
        bw.close();
    }
}

