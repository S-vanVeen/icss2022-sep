package nl.han.ica.icss.generator;


import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;

public class Generator {

	private static final String INDENT = "  ";

	public String generate(AST ast) {
		StringBuilder css = new StringBuilder();
		boolean first = true;

		for (ASTNode node : ast.root.body) {
			if (node instanceof Stylerule) {
				// empty line between rules
				if (!first) {
					css.append("\n");
				}
				generateStylerule((Stylerule) node, css);
				first = false;
			}
		}
		return css.toString();
	}

	private void generateStylerule(Stylerule stylerule, StringBuilder css) {
		// multiple selectors would be separated by a comma
		for (int i = 0; i < stylerule.selectors.size(); i++) {
			if (i > 0) {
				css.append(", ");
			}
			css.append(stylerule.selectors.get(i));
		}
		css.append(" {\n");

		for (ASTNode node : stylerule.body) {
			if (node instanceof Declaration) {
				Declaration declaration = (Declaration) node;
				css.append(INDENT)
						.append(declaration.property.name)
						.append(": ")
						.append(literalToString(declaration.expression))
						.append(";\n");
			}
		}
		css.append("}\n");
	}

	private String literalToString(Expression expression) {
		if (expression instanceof PixelLiteral) {
			return ((PixelLiteral) expression).value + "px";
		} else if (expression instanceof PercentageLiteral) {
			return ((PercentageLiteral) expression).value + "%";
		} else if (expression instanceof ScalarLiteral) {
			return String.valueOf(((ScalarLiteral) expression).value);
		} else if (expression instanceof ColorLiteral) {
			return ((ColorLiteral) expression).value;
		} else if (expression instanceof BoolLiteral) {
			return ((BoolLiteral) expression).value ? "TRUE" : "FALSE";
		}
		return "";
	}
}
