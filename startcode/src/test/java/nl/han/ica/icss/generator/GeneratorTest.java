package nl.han.ica.icss.generator;

import nl.han.ica.icss.Pipeline;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorTest {

    // runs the whole pipeline and gives back the css
    private String cssFor(String icss) {
        Pipeline pipeline = new Pipeline();
        pipeline.parseString(icss);
        assertTrue(pipeline.isParsed(), "should parse: " + pipeline.getErrors());
        assertTrue(pipeline.check(), "should check: " + pipeline.getErrors());
        pipeline.transform();
        return pipeline.generate();
    }

    @Test
    void simpleRule() {
        assertEquals("p {\n  width: 500px;\n}\n", cssFor("p { width: 500px; }"));
    }

    @Test
    void variablesAreReplaced() {
        assertEquals("a {\n  color: #ff0000;\n}\n", cssFor("C := #ff0000; a { color: C; }"));
    }

    @Test
    void mathIsEvaluated() {
        assertEquals("#menu {\n  width: 520px;\n}\n", cssFor("W := 500px; #menu { width: W + 2 * 10px; }"));
        assertEquals("p {\n  width: 40%;\n}\n", cssFor("p { width: 50% - 10%; }"));
    }

    @Test
    void minusAndPlusAreLeftToRight() {
        assertEquals("p {\n  width: 7px;\n}\n", cssFor("p { width: 10px - 5px + 2px; }"));
    }

    @Test
    void multiplyGoesBeforePlus() {
        assertEquals("p {\n  width: 68px;\n}\n", cssFor("p { width: 50px + 2 * 10px - 2px; }"));
    }

    @Test
    void ifTakesTheRightBranch() {
        String icss = "A := TRUE; B := FALSE;"
                + " p { if[A] { width: 1px; } else { width: 2px; }"
                + " if[B] { height: 3px; } else { height: 4px; } }";
        assertEquals("p {\n  width: 1px;\n  height: 4px;\n}\n", cssFor(icss));
    }

    @Test
    void nestedIfAndFalseWithoutElse() {
        String icss = "p { if[TRUE] { color: #000000; if[FALSE] { width: 1px; } } height: 20px; }";
        assertEquals("p {\n  color: #000000;\n  height: 20px;\n}\n", cssFor(icss));
    }

    @Test
    void multipleRulesAreSeparatedByEmptyLine() {
        assertEquals("p {\n  width: 1px;\n}\n\n.a {\n  width: 2px;\n}\n",
                cssFor("p { width: 1px; } .a { width: 2px; }"));
    }
}
