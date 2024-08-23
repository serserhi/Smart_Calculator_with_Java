package calculator;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // put your code here
        String linea = "";
        Map<String, BigInteger> variables = new HashMap<>();

        while (scanner.hasNextLine()) {
            linea = scanner.nextLine();
            Pattern patternCommand = Pattern.compile("^/\\w*");
            Matcher matcher = patternCommand.matcher(linea);

            if (!linea.isEmpty()) {
                if (matcher.find()) {
                    manageCommand(linea);
                } else {

                    Pattern patternAssign = Pattern.compile(".*=.*");
                    Matcher matcher2 = patternAssign.matcher(linea);

                    if (matcher2.find()) {
                        manageVariable(linea, variables);
                    } else {

                        Pattern patternVariable = Pattern.compile("^[a-zA-Z]+$");
                        Matcher matcher3 = patternVariable.matcher(linea.trim());

                        if (matcher3.find()) {
                            if (variables.containsKey(linea.trim())) {
                                System.out.println(variables.get(linea.trim()));
                            } else {
                                System.out.println("Unknown variable");
                            }
                        } else {
                            Pattern patternExpression = Pattern.compile("-?[a-zA-Z0-9]+\\s*([/*+-]+\\s*-?[a-zA-Z0-9]+\\s*)*[/*+-]+?\\s*-?[a-zA-Z0-9]+\\s*|^([+-]?[a-zA-Z0-9]+)$");
                            Matcher matcher4 = patternExpression.matcher(linea);
                            if (matcher4.find()) {
                                String lineaCorregida = linea.replaceAll("\\s+", " ");
                                Pattern patternMultiplyDivision = Pattern.compile("\\*{2,}|/{2,}");
                                Matcher matcher5 = patternMultiplyDivision.matcher(linea);
                                if (matcher5.find()) {
                                    System.out.println("Invalid expression");
                                } else {
                                    lineaCorregida = lineaCorregida.replaceAll("\\*+", "*");
                                    lineaCorregida = lineaCorregida.replaceAll("/+", "/");
                                    lineaCorregida = lineaCorregida.replaceAll("--", "+");
                                    lineaCorregida = lineaCorregida.replaceAll("\\+-", "-");
                                    lineaCorregida = lineaCorregida.replaceAll("-\\+", "-");
                                    lineaCorregida = lineaCorregida.replaceAll("\\++", "+");
                                    String postfixExpression = infixToPostfix(lineaCorregida);
                                    if (!postfixExpression.equals("Invalid expression")) {
                                        evaluatePostfix(postfixExpression, variables);
                                    } else {
                                        System.out.println("Invalid expression");
                                    }
                                }
                                //manageExpression(lineaCorregida, variables);
                            } else {
                                System.out.println("Invalid expression");
                            }

                        }
                    }
                }
            }
        }

    }

    public static void manageCommand(String linea) {
        switch(linea) {
            case "/help":
                System.out.println("The program calculates the sum and subtractions of numbers");
                break;
            case "/exit":
                System.out.println("Bye!");
                System.exit(0);
            default:
                System.out.println("Unknown command");
                break;
        }
    }

    public static void manageVariable(String linea, Map<String, BigInteger> variables) {
        String[] partes = linea.split("=");
        Pattern patternAssign = Pattern.compile("[a-zA-Z]+\\d+|\\W+|\\d+[a-zA-Z]+");
        Matcher matcher = patternAssign.matcher(partes[0].trim());
        if (matcher.find()) {
            System.out.println("Invalid identifier");
        } else {
            Pattern patternAssign2 = Pattern.compile("[a-zA-Z]+\\d+|[^a-zA-Z_0-9-]+|\\d+[a-zA-Z]+");
            Matcher matcher2 = patternAssign2.matcher(partes[1].trim());
            if (matcher2.find()) {
                System.out.println("Invalid assignment");
            } else {
                Pattern patternAssign3 = Pattern.compile("[0-9]+");
                Matcher matcher3 = patternAssign3.matcher(partes[1].trim());
                if (matcher3.find()) {
                    variables.put(partes[0].trim(), new BigInteger(partes[1].trim()));
                } else {
                    if (variables.containsKey(partes[1].trim())) {
                        variables.put(partes[0].trim(), variables.get(partes[1].trim()));
                    } else {
                        System.out.println("Unknown variable");
                    }

                }
            }
        }

    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // Method to get the precedence of operators
    private static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    // Method to convert infix to postfix
    public static String infixToPostfix(String expression) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // If character is a whitespace, skip it
            if (c == ' ') continue;

            // If character is an operand, add it to output
            if (Character.isLetterOrDigit(c) || (c == '-' && (i == 0 || expression.charAt(i - 1) == '('))) {
                while (i < expression.length() && (Character.isLetterOrDigit(expression.charAt(i)) || expression.charAt(i) == '-')) {
                    postfix.append(expression.charAt(i++));
                }
                postfix.append(' '); // Add space after the number
                i--; // Move back to the last digit
            }
            // If character is '(', push it to stack
            else if (c == '(') {
                stack.push(c);
            }
            // If character is ')', pop and output from the stack until '(' is found
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(' ');
                }
                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop(); // pop '('
                } else {
                    return "Invalid expression";
                }
            }
            // An operator is encountered
            else if (isOperator(c)) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                    postfix.append(stack.pop()).append(' ');
                }
                stack.push(c);
            }
        }

        // Pop all the operators from the stack
        while (!stack.isEmpty() && !stack.peek().equals('(') && !stack.peek().equals(')')) {
            postfix.append(stack.pop()).append(' ');
        }
        if (stack.isEmpty()) {
            return postfix.toString().trim(); // Convert to string and trim whitespace
        } else {
            return "Invalid expression";
        }
    }

    // Method to evaluate postfix expression
    public static void evaluatePostfix(String postfix, Map<String, BigInteger> variables) {
        Stack<BigInteger> stack = new Stack<>();
        String[] tokens = postfix.split(" ");
        boolean error = false;

        for (String token : tokens) {
            if (token.isEmpty()) continue; // Skip empty tokens

            // If the token is a number, push it to the stack
            if (Character.isDigit(token.charAt(0)) || (token.length() > 1 && token.charAt(0) == '-')) {
                stack.push(new BigInteger(token));
            }
            else if (Character.isLetter(token.charAt(0))) {
                if (variables.containsKey(token)) {
                    stack.push(variables.get(token));
                } else {
                    System.out.println("Unknown variable");
                    error = true;
                    break;
                }
            }
            // If the token is an operator, pop two elements and apply the operator
            else {
                BigInteger b = stack.pop();
                BigInteger a = stack.pop();
                switch (token.charAt(0)) {
                    case '+':
                        stack.push(a.add(b));
                        break;
                    case '-':
                        stack.push(a.subtract(b));
                        break;
                    case '*':
                        stack.push(a.multiply(b));
                        break;
                    case '/':
                        stack.push(a.divide(b));
                        break;
                }
            }
        }
        if (error == false) {
            System.out.println(stack.pop()); // The final result is on the top of the stack
        }
    }


}
