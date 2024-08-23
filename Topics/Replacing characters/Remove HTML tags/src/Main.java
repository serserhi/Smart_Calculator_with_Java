import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static HashMap<String, Integer> precedence = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //String stringWithHtmlTags = scanner.nextLine();

        // write your code here

        //String stringWithoutHtmlTags = stringWithHtmlTags.replaceAll("<[^>]*>", "");
        //System.out.println(stringWithoutHtmlTags);

        initPrecedence();
        String linea = scanner.nextLine();

        System.out.println(linea + " --> " + infixToPostfix(linea));

        System.out.println(postfixEval(infixToPostfix(linea)));

    }

    public static void initPrecedence() {
        precedence.put("*", 3);
        precedence.put("/", 3);
        precedence.put("+", 2);
        precedence.put("-", 2);
        precedence.put("(", 1);
    }

    public static String infixToPostfix(String infixExpr) {
        final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Pattern patternLetters = Pattern.compile("[a-zA-Z]+");
        final String DIGITS = "0123456789";
        Pattern patternDigits = Pattern.compile("[0-9]+");
        final String OPERATORS = "*/+-()";
        Pattern patternOperators = Pattern.compile("[-+*/()]");

        Stack<String> opStack = new Stack<String>();
        ArrayList<String> postfixList = new ArrayList<String>();

        String[] tokenList = generateTokenList(infixExpr);

        for (String token: tokenList) {
            if (token != null) {
                Matcher matcherLetters = patternLetters.matcher(token);
                Matcher matcherDigits = patternDigits.matcher(token);
                if (matcherLetters.find() || matcherDigits.find()) {
                    postfixList.add(token);
                } else if (token.equals("(")) {
                    opStack.push(token);
                } else if (token.equals(")")) {
                    // pop everything down to the matching open paren
                    String topToken = opStack.pop();
                    while (!topToken.equals("(")) {
                        postfixList.add(topToken);
                        topToken = opStack.pop();
                    }
                } else if (OPERATORS.indexOf(token) >= 0) {
                    // pop higher-precedence operations
                    while (!opStack.isEmpty() &&
                            (precedence.get(opStack.peek())
                                    >= precedence.get(token))) {
                        postfixList.add(opStack.pop());
                    }
                    // then push this operator
                    opStack.push(token);
                }
            }
        }

        // If any operators remain, add them to the postfix expression
        //while (!opStack.isEmpty()) {
        while (!opStack.isEmpty() && !opStack.peek().equals("(") && !opStack.peek().equals(")") ) {   //
                postfixList.add(opStack.pop());
            }                                   //
        //}

        String result = "";
        for (String s: postfixList) {
            result = result + s + " ";
        }
        if (opStack.isEmpty()) { //
            return result; //
        } else {
            return "Invalid expression";
        }

    }

    public static boolean sameCategory(String a, String b) {
        Pattern patternLetters = Pattern.compile("[a-zA-Z]+");
        Pattern patternDigits = Pattern.compile("[0-9]+");
        Pattern patternOperators = Pattern.compile("[-+*/()]");
        Matcher matcherLettersA = patternLetters.matcher(a);
        Matcher matcherDigitsA = patternDigits.matcher(a);
        Matcher matcherOperatorsA = patternOperators.matcher(b);
        Matcher matcherLettersB = patternLetters.matcher(b);
        Matcher matcherDigitsB = patternDigits.matcher(b);
        Matcher matcherOperatorsB = patternOperators.matcher(a);
        if (matcherLettersA.find() && !matcherLettersB.find() ) {
            return false;
        } else if (matcherDigitsA.find() && !matcherDigitsB.find()) {
            return false;
        } else if (matcherOperatorsA.find() && !matcherOperatorsB.find()) {
            return false;
        } else {
            return true;
        }

    }

    public static String[] generateTokenList(String infixExpr) {

        String[] tokenList = new String[1000];
        final String OPERATORS = "*/+-()";

        int indice = 0;
        String infixExprNoSpaces = infixExpr.replaceAll("\\s", "");
        String trozo = infixExprNoSpaces.substring(0, 1);

        for (int i = 1; i < infixExprNoSpaces.length(); i++) {
            if (OPERATORS.indexOf(infixExprNoSpaces.substring(i , i + 1 )) >= 0) {
                if (!trozo.isEmpty()) {
                    tokenList[indice] = trozo;
                    trozo = "";
                    indice++;
                }
                tokenList[indice] = infixExprNoSpaces.substring(i, i + 1);
                indice++;
            } else if (!sameCategory(infixExprNoSpaces.substring(i - 1, i), infixExprNoSpaces.substring(i, i + 1))) {
                tokenList[indice] = trozo;
                trozo = "";
                indice++;
            } else {
                trozo = trozo + infixExprNoSpaces.substring(i, i + 1);
            }
        }
        if (!trozo.isEmpty()) {
            tokenList[indice] = trozo;
        }
        return tokenList;
    }

    public static Integer postfixEval(String postfixExpr) {

        final String DIGITS = "0123456789";
        Pattern patternDigits = Pattern.compile("[0-9]+");

        Stack<Integer> operandStack = new Stack<Integer>();

        String[] tokenList = postfixExpr.split(" ");

        for (String token: tokenList) {
            Matcher matcherDigits = patternDigits.matcher(token);
            if (matcherDigits.find()) {
                operandStack.push(Integer.valueOf(token));
            }
            else {
                Integer operand2 = operandStack.pop();
                Integer operand1 = operandStack.pop();
                Integer result = doMath(token, operand1, operand2);
                operandStack.push(result);
            }
        }
        return operandStack.pop();
    }

    public static Integer doMath(String operator, Integer operand1, Integer operand2) {
        if (operator.equals("*")) {
            return operand1 * operand2;
        }
        else if (operator.equals("/")) {
            return operand1 / operand2;
        }
        else if (operator.equals("+")) {
            return operand1 + operand2;
        }
        else {
            return operand1 - operand2;
        }
    }



}