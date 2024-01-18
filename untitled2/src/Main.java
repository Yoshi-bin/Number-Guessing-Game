

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        GuessingNumberGame game = new GuessingNumberGame();
        game.startGame();
        scanner.close();
    }
}
