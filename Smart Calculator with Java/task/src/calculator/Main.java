package calculator;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // put your code here
        String linea = scanner.nextLine();
        while (!Objects.equals(linea, "/exit") ) {
            switch (linea) {
                case "/help":
                    System.out.println("The program calculates the sum of numbers");
                    break;
                case "":
                    break;
                default:
                    String[] numeros = linea.split(" ");
                    int sum = 0;
                    for (String numero : numeros) {
                        sum += Integer.parseInt(numero);
                    }
                    System.out.println(sum);
            }
            linea = scanner.nextLine();
        }
        System.out.println("Bye!");
    }
}
