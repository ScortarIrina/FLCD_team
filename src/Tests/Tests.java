package Tests;

import LR0.Grammar;
import LR0.LR0;
import State.State;
import State.Item;

import java.util.List;
import java.util.Objects;

public class Tests {

    private Grammar grammar1;
    private LR0 lrAlg;

    public Tests() {
        grammar1 = new Grammar("src/IO/GTest.txt");
        try {
            lrAlg = new LR0(grammar1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void runClosureTest() {
        lrAlg.getCanonicalCollectionForGrammar();
        Object[] result = lrAlg.closure(new Item(
                lrAlg.getWorkingGrammar().getStart(),
                lrAlg.getWorkingGrammar().getProductionsForNonTerminal(lrAlg.getGrammar().getStart()).get(0),
                0)).getItems().toArray();
        assert result.length == 1;
        assert Objects.equals(result[0], new Item("S0", List.of("a", "A"), 0));
        System.out.println("Closure test 1 successful");
    }

    public void runClosureTest2() throws Exception {
        grammar1 = new Grammar("src/IO/GTest2.txt");
        lrAlg = new LR0(grammar1);
        lrAlg.getCanonicalCollectionForGrammar();
        Object[] result = lrAlg.closure(new Item(
                lrAlg.getWorkingGrammar().getStart(),
                lrAlg.getWorkingGrammar().getProductionsForNonTerminal(lrAlg.getGrammar().getStart()).get(0),
                0)).getItems().toArray();
        assert result.length == 1;
        assert Objects.equals(result[0], new Item("S0", List.of("a"), 0));
        System.out.println("Closure test 2 successful");
    }

    public void runClosureTest3() throws Exception {
        grammar1 = new Grammar("src/IO/GTest3.txt");
        lrAlg = new LR0(grammar1);
        lrAlg.getCanonicalCollectionForGrammar();
        Object[] result = lrAlg.closure(new Item(
                lrAlg.getWorkingGrammar().getStart(),
                lrAlg.getWorkingGrammar().getProductionsForNonTerminal(lrAlg.getGrammar().getStart()).get(0),
                0)).getItems().toArray();
        assert result.length == 3;
        assert Objects.equals(result[1], new Item("A", List.of("S"), 0));
        System.out.println("Closure test 3 successful");
    }

    public void runAllClosureTest() throws Exception {
        runClosureTest();
        runClosureTest2();
        runClosureTest3();
    }

    public void runGoToTest1() throws Exception {
        grammar1 = new Grammar("src/IO/GTest.txt");
        lrAlg = new LR0(grammar1);
        lrAlg.getCanonicalCollectionForGrammar();
        State state = lrAlg.closure(new Item(
                lrAlg.getWorkingGrammar().getStart(),
                lrAlg.getWorkingGrammar().getProductionsForNonTerminal(lrAlg.getGrammar().getStart()).get(0),
                0));
        State result = lrAlg.goTo(state, state.getPartAfterDot().get(0));
        assert result.getItems().size() == 2;
        assert Objects.equals(result.getItems().toArray()[1], new Item("A", List.of("a","b"),0));
        System.out.println("Go To Test 1 Successful");
    }

    public void runGoToTest2() throws Exception {
        grammar1 = new Grammar("src/IO/GTest2.txt");
        lrAlg = new LR0(grammar1);
        lrAlg.getCanonicalCollectionForGrammar();
        State state = lrAlg.closure(new Item(
                lrAlg.getWorkingGrammar().getStart(),
                lrAlg.getWorkingGrammar().getProductionsForNonTerminal(lrAlg.getGrammar().getStart()).get(0),
                0));
        State result = lrAlg.goTo(state, state.getPartAfterDot().get(0));
        assert result.getItems().size() == 1;
        assert Objects.equals(result.getItems().toArray()[1], new Item("S0", List.of("a"),1));
        System.out.println("Go To Test 2 Successful");
    }

    public void runAllGoToTests() throws Exception {
        runGoToTest1();
        runGoToTest2();
    }

    public void runAllCanonicalTests() throws Exception {
        runGetCanonicalCollectionTest1();
        runGetCanonicalCollectionTest2();
    }

    public void runGetCanonicalCollectionTest1() throws Exception {
        grammar1 = new Grammar("src/IO/GTest.txt");
        lrAlg = new LR0(grammar1);
        List<State> result = lrAlg.getCanonicalCollectionForGrammar().getStates();
        assert result.size() == 6;
        assert Objects.equals(result.get(result.size() - 1).getItems().toArray()[0], new Item("A", List.of("a", "b"), 2));
        System.out.println("Canonical Test 1 Successful");
    }

    public void runGetCanonicalCollectionTest2() throws Exception {
        grammar1 = new Grammar("src/IO/GTest3.txt");
        lrAlg = new LR0(grammar1);
        List<State> result = lrAlg.getCanonicalCollectionForGrammar().getStates();
        assert result.size() == 1;
        assert Objects.equals(result.get(0).getItems().toArray()[0], new Item("A", List.of("a"), 1));
        System.out.println("Canonical Test 2 Successful");
    }

}
