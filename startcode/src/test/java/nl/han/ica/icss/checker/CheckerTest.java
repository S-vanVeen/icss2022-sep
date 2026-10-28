package nl.han.ica.icss.checker;

import nl.han.ica.icss.Pipeline;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {

    // parses + checks, returns number of semantic errors
    private int errorsFor(String icss) {
        Pipeline pipeline = new Pipeline();
        pipeline.parseString(icss);
        assertTrue(pipeline.isParsed(), "should parse: " + pipeline.getErrors());
        pipeline.check();
        return pipeline.getAST().getErrors().size();
    }

    @Test
    void validSheetHasNoErrors() {
        assertEquals(0, errorsFor(
                "W := 10px;\n p { width: W + 5px; color: #ffffff; if[TRUE] { height: 20%; } else { height: 5px; } }"));
    }

    @Test
    void ch01_undefinedVariable() {
        assertEquals(1, errorsFor("p { width: Nope; }"));
    }

    @Test
    void ch02_mixedTypesInAdd() {
        assertEquals(1, errorsFor("p { width: 10px + 5%; }"));
    }

    @Test
    void ch02_multiplyNeedsScalar() {
        assertEquals(1, errorsFor("p { width: 10px * 5px; }"));
        assertEquals(0, errorsFor("p { width: 2 * 5px; }"));
    }

    @Test
    void ch03_noColorsInOperations() {
        assertEquals(1, errorsFor("p { color: #ffffff + #000000; }"));
    }

    @Test
    void ch04_propertyTypes() {
        assertEquals(1, errorsFor("p { width: #ffffff; }"));
        assertEquals(1, errorsFor("p { color: 10px; }"));
        assertEquals(1, errorsFor("p { background-color: 5%; }"));
    }

    @Test
    void ch05_ifConditionMustBeBool() {
        assertEquals(1, errorsFor("p { if[10px] { width: 5px; } }"));
    }

    @Test
    void ch06_variableNotVisibleOutsideItsScope() {
        assertEquals(1, errorsFor("p { if[TRUE] { X := 5px; } width: X; }"));
    }

    @Test
    void ch06_variableVisibleInNestedScope() {
        assertEquals(0, errorsFor("X := 5px; p { if[TRUE] { width: X; } }"));
    }
}
