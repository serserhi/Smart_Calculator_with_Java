import java.util.Scanner;
import java.util.Stack;

class RemoveExtraSpacesProblem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();

        // write your code here

        //String stringWithoutSpaces = text.replaceAll("\\s+", " ");
       // System.out.println(stringWithoutSpaces);

        String lineaCorregida = text.replaceAll("\\s+", " ");
        lineaCorregida = lineaCorregida.replaceAll("\\*+", "*");
        lineaCorregida = lineaCorregida.replaceAll("/+", "/");
        lineaCorregida = lineaCorregida.replaceAll("--", "+");
        lineaCorregida = lineaCorregida.replaceAll("\\+-", "-");
        lineaCorregida = lineaCorregida.replaceAll("-\\+", "-");
        String infixExpression = lineaCorregida.replaceAll("\\++", "+");
        String postfixExpression = infixToPostfix(infixExpression);
        System.out.println("Postfix Expression: " + postfixExpression);
        if (!postfixExpression.equals("Invalid expression")) {
            int result = evaluatePostfix(postfixExpression);
            System.out.println("Result: " + result);
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
    public static int evaluatePostfix(String postfix) {
        Stack<Integer> stack = new Stack<>();
        String[] tokens = postfix.split(" ");

        for (String token : tokens) {
            if (token.isEmpty()) continue; // Skip empty tokens

            // If the token is a number, push it to the stack
            if (Character.isDigit(token.charAt(0)) || (token.length() > 1 && token.charAt(0) == '-')) {
                stack.push(Integer.parseInt(token));
            }
            // If the token is an operator, pop two elements and apply the operator
            else {
                int b = stack.pop();
                int a = stack.pop();
                switch (token.charAt(0)) {
                    case '+':
                        stack.push(a + b);
                        break;
                    case '-':
                        stack.push(a - b);
                        break;
                    case '*':
                        stack.push(a * b);
                        break;
                    case '/':
                        stack.push(a / b);
                        break;
                }
            }
        }
        return stack.pop(); // The final result is on the top of the stack
    }




}