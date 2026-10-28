package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;
import java.util.List;


public class Checker {

    // first item in the list is the innermost scope
    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();

        // global scope
        openScope();
        checkBody(ast.root.body);
        closeScope();
    }

    // --- scope stuff (CH06) ---

    private void openScope() {
        variableTypes.addFirst(new HashMap<>());
    }

    private void closeScope() {
        variableTypes.removeFirst();
    }

    private void declareVariable(String name, ExpressionType type) {
        variableTypes.getFirst().put(name, type);
    }

    // looks from inner to outer scope, null when it doesn't exist anywhere
    private ExpressionType lookupVariable(String name) {
        for (int i = 0; i < variableTypes.getSize(); i++) {
            ExpressionType type = variableTypes.get(i).get(name);
            if (type != null) {
                return type;
            }
        }
        return null;
    }

    // --- walking the tree ---

    private void checkBody(List<ASTNode> body) {
        for (ASTNode node : body) {
            if (node instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) node);
            } else if (node instanceof Stylerule) {
                checkStylerule((Stylerule) node);
            } else if (node instanceof Declaration) {
                checkDeclaration((Declaration) node);
            } else if (node instanceof IfClause) {
                checkIfClause((IfClause) node);
            }
        }
    }

    private void checkStylerule(Stylerule stylerule) {
        openScope();
        checkBody(stylerule.body);
        closeScope();
    }

    private void checkVariableAssignment(VariableAssignment assignment) {
        ExpressionType type = getType(assignment.expression);
        declareVariable(assignment.name.name, type);
    }

    private void checkDeclaration(Declaration declaration) {
        ExpressionType type = getType(declaration.expression);
        String property = declaration.property.name;

        // CH04: value type has to fit the property
        if (type == ExpressionType.UNDEFINED) {
            return; // already reported something for the expression
        }
        if (property.equals("width") || property.equals("height")) {
            if (type != ExpressionType.PIXEL && type != ExpressionType.PERCENTAGE) {
                declaration.setError("Property '" + property + "' only accepts pixel or percentage values, not " + type);
            }
        } else if (property.equals("color") || property.equals("background-color")) {
            if (type != ExpressionType.COLOR) {
                declaration.setError("Property '" + property + "' only accepts color values, not " + type);
            }
        }
    }

    private void checkIfClause(IfClause ifClause) {
        // CH05: condition has to be a bool
        ExpressionType conditionType = getType(ifClause.conditionalExpression);
        if (conditionType != ExpressionType.BOOL && conditionType != ExpressionType.UNDEFINED) {
            ifClause.setError("If condition has to be a boolean, not " + conditionType);
        }

        openScope();
        checkBody(ifClause.body);
        closeScope();

        if (ifClause.elseClause != null) {
            openScope();
            checkBody(ifClause.elseClause.body);
            closeScope();
        }
    }

    // --- expression types ---

    private ExpressionType getType(Expression expression) {
        if (expression instanceof PixelLiteral) {
            return ExpressionType.PIXEL;
        } else if (expression instanceof PercentageLiteral) {
            return ExpressionType.PERCENTAGE;
        } else if (expression instanceof ColorLiteral) {
            return ExpressionType.COLOR;
        } else if (expression instanceof ScalarLiteral) {
            return ExpressionType.SCALAR;
        } else if (expression instanceof BoolLiteral) {
            return ExpressionType.BOOL;
        } else if (expression instanceof VariableReference) {
            return getVariableType((VariableReference) expression);
        } else if (expression instanceof Operation) {
            return getOperationType((Operation) expression);
        }
        return ExpressionType.UNDEFINED;
    }

    // CH01
    private ExpressionType getVariableType(VariableReference reference) {
        ExpressionType type = lookupVariable(reference.name);
        if (type == null) {
            reference.setError("Variable '" + reference.name + "' is not defined");
            return ExpressionType.UNDEFINED;
        }
        return type;
    }

    private ExpressionType getOperationType(Operation operation) {
        ExpressionType left = getType(operation.lhs);
        ExpressionType right = getType(operation.rhs);

        // something below this already went wrong, don't pile on more errors
        if (left == ExpressionType.UNDEFINED || right == ExpressionType.UNDEFINED) {
            return ExpressionType.UNDEFINED;
        }

        // CH03: no colors (and no bools, makes no sense either) in operations
        if (left == ExpressionType.COLOR || right == ExpressionType.COLOR
                || left == ExpressionType.BOOL || right == ExpressionType.BOOL) {
            operation.setError("Colors and booleans cannot be used in operations");
            return ExpressionType.UNDEFINED;
        }

        // CH02
        if (operation instanceof MultiplyOperation) {
            if (left != ExpressionType.SCALAR && right != ExpressionType.SCALAR) {
                operation.setError("Multiplying needs at least one scalar value");
                return ExpressionType.UNDEFINED;
            }
            return left == ExpressionType.SCALAR ? right : left;
        }

        // add and subtract
        if (left != right) {
            operation.setError("Cannot " + (operation instanceof AddOperation ? "add" : "subtract")
                    + " " + left + " and " + right);
            return ExpressionType.UNDEFINED;
        }
        return left;
    }
}
