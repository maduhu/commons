package sos.util.string2bool;

import java.util.ArrayList;
import java.util.List;

public final class SOSMalformedBooleanException extends Exception {

    private static final long serialVersionUID = 1L;
    private List booleanExpressionErrorIndexes;
    private String booleanExpression;
    private String booleanExpressionErrorMessage;

    SOSMalformedBooleanException(final String errorMessage, final int errorIndex, final String newBooleanExpression) {
        this(errorMessage, toList(errorIndex), newBooleanExpression);
    }

    SOSMalformedBooleanException(final String errorMessage, final List errorIndexes, final String newBooleanExpression) {
        super(format(errorMessage, errorIndexes, newBooleanExpression));
        this.booleanExpression = newBooleanExpression;
        this.booleanExpressionErrorIndexes = errorIndexes;
        this.booleanExpressionErrorMessage = errorMessage;
    }

    public String getBooleanExpression() {
        return this.booleanExpression;
    }

    public List getBooleanExpressionErrorIndexes() {
        return this.booleanExpressionErrorIndexes;
    }

    public String getBooleanExpressionErrorMessage() {
        return this.booleanExpressionErrorMessage;
    }

    private static List toList(final int errorIndex) {
        List errorIndexes = new ArrayList();
        errorIndexes.add(new Integer(errorIndex));
        return errorIndexes;
    }

    private static String format(final String errorMessage, final List errorIndexes, final String newBooleanExpression) {
        if (errorMessage == null || "".equals(errorMessage)) {
            throw new IllegalArgumentException("errorMessage is null or void");
        }
        if (errorIndexes == null || errorIndexes.isEmpty()) {
            throw new IllegalArgumentException("errorIndexes is null or void");
        }
        if (newBooleanExpression == null || "".equals(newBooleanExpression)) {
            throw new IllegalArgumentException("newBooleanExpression is null or void");
        }
        StringBuilder error = new StringBuilder();
        error.append(errorMessage);
        error.append(" in [ ");
        int size = errorIndexes.size();
        int lastIndex = 0;
        for (int i = 0; i < size; i++) {
            int index = ((Integer) errorIndexes.get(i)).intValue();
            error.append(newBooleanExpression.substring(lastIndex, index));
            error.append("_");
            lastIndex = index;
        }
        error.append(newBooleanExpression.substring(lastIndex, newBooleanExpression.length()));
        error.append(" ]");
        if (size == 1) {
            error.append(" - Index [");
        } else if (size > 1) {
            error.append(" - Indexes [");
        }
        for (int i = 0; i < size; i++) {
            error.append(errorIndexes.get(i));
            if (i != (size - 1)) {
                error.append(", ");
            }
        }
        if (size > 0) {
            error.append("]");
        }
        return error.toString();
    }

}