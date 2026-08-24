import java.util.Scanner;

public class Main {
    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // prompt user
        System.out.println("Informe ax: ");
        int ax = scanner.nextInt();

        System.out.println("informe b: ");
        int b = scanner.nextInt();

        System.out.println("Informe c: ");
        int c = scanner.nextInt();
        System.out.println();

        // b to the power of two
        double powerB = Math.pow(b, 2);

        // divide delta in two parts
        double partIIDelta = ((4) * (ax) * (c));

        // calculates delta and square it
        double delta = powerB - partIIDelta;
        double squareDelta = Math.sqrt(delta);

        // convert b, multiply to minus one
        double minusB = b *= -1;

        // divide bhaskara in two parts
        double twoA = 2 * ax;

        // core math
        if (squareDelta > 0) {
            double bhaskaraXI = (minusB + squareDelta) / (twoA);
            double bhaskaraXII = (minusB - squareDelta) / (twoA);
            System.out.println("xI = " + bhaskaraXI + ", xII = " + bhaskaraXII);
        } if (squareDelta == 0) {
                double xIandxII = minusB / twoA;
                System.out.println(xIandxII);
        } else {
            System.out.println("Como Δ < 0, a equação não possui raízes reais.");
        }
    }
}
