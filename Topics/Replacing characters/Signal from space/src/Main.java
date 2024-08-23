import java.util.Scanner;

public class Main {
    public static String[] decipherCosmicSignal(String[] spaceSignalArray) {
        // write your code here
        String[] aux = new String[spaceSignalArray.length];
        for (int i = 0; i < spaceSignalArray.length; i++) {
            aux[i] = spaceSignalArray[i].replaceAll("~+", "");
        }
        return aux;
    }    

    // Don't change the code below
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] encryptedSpaceSignalArray = scanner.nextLine().split("\\s");
        String[] spaceSignalArray = decipherCosmicSignal(encryptedSpaceSignalArray);
        StringBuilder spaceMessage = new StringBuilder();
        for (String signal : spaceSignalArray) {
            spaceMessage.append(signal).append("\s");
        }
        System.out.println(spaceMessage.toString().trim());
    }
}