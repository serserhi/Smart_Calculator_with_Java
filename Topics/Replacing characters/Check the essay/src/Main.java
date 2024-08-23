import java.util.Scanner;


class CheckTheEssay {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();

        // write your code here
        String textAux1 = text.replaceAll("Franse", "France");
        String textAux2 = textAux1.replaceAll("Eifel tower", "Eiffel Tower");
        String textAux3 = textAux2.replaceAll("19th", "XIXth");
        String textAux4 = textAux3.replaceAll("20th", "XXth");

        String textFixed = textAux4.replaceAll("21st", "XXIst");
        System.out.println(textFixed);
    }
}