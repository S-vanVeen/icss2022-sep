package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.ArrayList;
import java.util.HashMap;

public class Evaluator implements Transform {

    // first item is the innermost scope, same idea as in the Checker
    private IHANLinkedList<HashMap<String, Literal>> variableValues;

    public Evaluator() {
        variableValues = new HANLinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        variableValues = new HANLinkedList<>();

        openScope();
        for (ASTNode node : ast.root.body) {
            if (node instanceof VariableAssignment) {
                evaluateAssignment((VariableAssignment) node);
            } else if (node instanceof Stylerule) {
                Stylerule stylerule = (Stylerule) node;
                openScope();
                stylerule.body = transformBody(stylerule.body);
                closeScope();
            }
        }
        closeScope();
    }

    // --- scopes ---

    private void openScope() {
        variableValues.addFirst(new HashMap<>());
    }

    private void closeScope() {
        variableValues.removeFirst();
    }

    private Literal lookupVariable(String name) {
        for (int i = 0; i < variableValues.getSize(); i++) {
            Literal value = variableValues.get(i).get(name);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    // --- transforming ---

    // returns a new body where everything is evaluated and if/else clauses are replaced
    // by the body of the branch that is taken
    private ArrayList<ASTNode> transformBody(ArrayList<ASTNode> body) {
        ArrayList<ASTNode> result = new ArrayList<>();

        for (ASTNode node : body) {
            if (node instanceof Declaration) {
                Declaration declaration = (Declaration) node;
                declaration.expression = evaluate(declaration.expression);
                result.add(declaration);
            } else if (node instanceof VariableAssignment) {
                evaluateAssignment((VariableAssignment) node);
                // not needed in the output, so it doesn't go in the result
            } else if (node instanceof IfClause) {
                result.addAll(transformIfClause((IfClause) node));
            }
        }
        return result;
    }

    private ArrayList<ASTNode> transformIfClause(IfClause ifClause) {
        BoolLiteral condition = (BoolLiteral) evaluate(ifClause.conditionalExpression);

        ArrayList<ASTNode> chosenBody = new ArrayList<>();
        if (condition.value) {
            chosenBody = ifClause.body;
        } else if (ifClause.elseClause != null) {
            chosenBody = ifClause.elseClause.body;
        }

        openScope();
        ArrayList<ASTNode> result = transformBody(chosenBody);
        closeScope();
        return result;
    }

    private void evaluateAssignment(VariableAssignment assignment) {
        Literal value = evaluate(assignment.expression);
        assignment.expression = value;
        variableValues.getFirst().put(assignment.name.name, value);
    }

    // --- expressions ---

    private Literal evaluate(Expression expression) {
        if (expression instanceof Literal) {
            return (Literal) expression;
        } else if (expression instanceof VariableReference) {
            return lookupVariable(((VariableReference) expression).name);
        } else {
            return evaluateOperation((Operation) expression);
        }
    }

    private Literal evaluateOperation(Operation operation) {
        Literal left = evaluate(operation.lhs);
        Literal right = evaluate(operation.rhs);

        if (operation instanceof MultiplyOperation) {
            int result = getValue(left) * getValue(right);
            // the checker made sure one side is a scalar, the other side decides the type
            if (left instanceof ScalarLiteral) {
                return makeLiteral(right, result);
            }
            return makeLiteral(left, result);
        } else if (operation instanceof AddOperation) {
            return makeLiteral(left, getValue(left) + getValue(right));
        } else if (operation instanceof SubtractOperation) {
            return makeLiteral(left, getValue(left) - getValue(right));
        }
        return left;
    }

    // only pixel, percentage and scalar can be used in operations (checker ensures this)
    private int getValue(Literal literal) {
        if (literal instanceof PixelLiteral) {
            return ((PixelLiteral) literal).value;
        } else if (literal instanceof PercentageLiteral) {
            return ((PercentageLiteral) literal).value;
        }
        return ((ScalarLiteral) literal).value;
    }

    // new literal of the same type as the template
    private Literal makeLiteral(Literal template, int value) {
        if (template instanceof PixelLiteral) {
            return new PixelLiteral(value);
        } else if (template instanceof PercentageLiteral) {
            return new PercentageLiteral(value);
        }
        return new ScalarLiteral(value);
    }
}
