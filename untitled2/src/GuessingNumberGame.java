import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GuessingNumberGame {
	private Player player;
	private final Random random;
	private final Scanner scanner;
	private String difficulty;
	private int lowerBound;
	private int upperBound;
	private final List<Player> players;
	private final Player computerPlayer;

	public GuessingNumberGame() {
		this.random = new Random();
		this.scanner = new Scanner(System.in);
		this.players = new ArrayList<>();
		this.computerPlayer = new Player("Computer");
	}

	public void selectDifficulty() {
		System.out.println("Select difficulty level: 1 - Easy, 2 - Normal, 3 - Hard, 4 - Custom");
		int choice = scanner.nextInt();
		switch (choice) {
		case 1:
			lowerBound = 0;
			upperBound = 100;
			difficulty = "Easy";
			break;
		case 2:
			lowerBound = 0;
			upperBound = 10000;
			difficulty = "Normal";
			break;
		case 3:
			lowerBound = 0;
			upperBound = 1000000;
			difficulty = "Hard";
			break;
		case 4:
			System.out.println("Set lower bound: ");
			lowerBound = scanner.nextInt();
			System.out.println("Set upper bound: ");
			upperBound = scanner.nextInt();
			difficulty = "Custom";
			break;
		default:
			System.out.println("Invalid choice. Set to Easy difficulty.");
			lowerBound = 0;
			upperBound = 100;
			difficulty = "Easy";
			break;
		}
	}

	public void startGame() {
		do {
			selectGameMode();
		} while (askToPlayAgain());
	}

	private boolean askToPlayAgain() {
		System.out.println("Do you want to play again? (yes/no)");
		String response = scanner.next();
		return response.equalsIgnoreCase("yes");
	}

	private void selectGameMode() {
		System.out.println("Select game mode: 1 - Guessing, 2 - Reverse, 3 - Mixed");
		int mode = scanner.nextInt();
		selectDifficulty();

		switch (mode) {
		case 1:
			System.out.print("Enter your nickname: ");
			String nickname = scanner.next();
			player = new Player(nickname);
			startStandardGame();
			break;
		case 2:
			System.out.print("Enter your nickname: ");
			String reverseNickname = scanner.next();
			player = new Player(reverseNickname);
			System.out.println("Playing in Reverse mode, where you think of a number, and the program guesses.");
			startReverseGame();
			break;
		case 3:
			System.out.print("Enter your nickname: ");
			String playerNickname = scanner.next();
			player = new Player(playerNickname);
			players.add(player);
			System.out.println("Do you want the computer player to participate in the game? (yes/no)");
			String computerPlayerOption = scanner.next();
			if (computerPlayerOption.equalsIgnoreCase("yes")) {
				players.add(computerPlayer);
				startMixedGameWithComputer();
			} else {
				System.out.println("Enter amount of players: ");

				int playersAmount = scanner.nextInt();

				for (int i = 1; i < playersAmount; i++) {
					System.out.println("Enter next player name: ");
					String nextPlayerNickname = scanner.next();
					players.add(new Player(nextPlayerNickname));
				}

				startMixedGameWithMultipePlayers();
			}
			break;

		default:
			System.out.println("Invalid choice. Please try again.");
			selectGameMode();
			break;
		}
	}

	public void startStandardGame() {
		int numberToGuess = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
		int numberOfTries = 0;
		System.out.println("Guess the number between " + lowerBound + " and " + upperBound + ". ");

		while (true) {
			System.out.print("Enter a number: ");
			int guess = scanner.nextInt();
			numberOfTries++;

			if (guess == numberToGuess) {
				System.out.println("Congratulations! You guessed the number in " + numberOfTries + " tries.");
				player.setBestScoreForModeAndDifficulty("Standard", difficulty, numberOfTries);
				updatePlayerScoreInFile(player, "Standard");
				break;
			} else if (guess < numberToGuess) {
				System.out.println("Too low!");
			} else {
				System.out.println("Too high!");
			}
		}
	}

	public void startReverseGame() {
		int low = lowerBound;
		int high = upperBound;
		System.out.println("Think of a number between " + low + " and " + high + " and I will try to guess it.");

		int numberOfTries = 0;

		label: while (low <= high) {
			int guess = low + (high - low) / 2;
			System.out.println("Is your number " + guess + "? (lower/higher/correct)");
			String response = scanner.next();
			numberOfTries++;

			switch (response) {
			case "lower":
				high = guess - 1;
				break;
			case "higher":
				low = guess + 1;
				break;
			case "correct":
				System.out.println("The program guessed your number: " + guess);
				player.setBestScoreForModeAndDifficulty("Reverse", difficulty, numberOfTries);
				updatePlayerScoreInFile(player, "Reverse");
				break label;
			}
		}
	}

	public void startMixedGameWithComputer() {
		boolean playerTurn = random.nextBoolean();
		System.out.println("Coin toss... " + (playerTurn ? "Player starts." : "Program starts."));
		int numberToGuess = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
		int playerGuess;
		int low = lowerBound;
		int high = upperBound;
		int programGuess;
		boolean isGuessed = false;

		while (!isGuessed) {
			if (playerTurn) {
				System.out.print("Your turn, guess the number: ");
				playerGuess = scanner.nextInt();
				if (playerGuess == numberToGuess) {
					System.out.println("Congratulations! You guessed the number.");
					player.setBestScoreForModeAndDifficulty("Mixed", difficulty, playerGuess);
					updatePlayerScoreInFile(player, "Mixed");
					isGuessed = true;
				} else if (playerGuess < numberToGuess) {
					System.out.println("Too low!");
				} else {
					System.out.println("Too high!");
				}
			} else {
				programGuess = low + (high - low) / 2;
				System.out.println("Program guesses: " + programGuess);
				if (programGuess == numberToGuess) {
					System.out.println("The program guessed your number!");
					player.setBestScoreForModeAndDifficulty("Mixed", difficulty, programGuess);
					updatePlayerScoreInFile(computerPlayer, "Mixed");
					isGuessed = true;
				} else if (programGuess < numberToGuess) {
					low = programGuess + 1;
					System.out.println("Program says: Too low!");
				} else {
					high = programGuess - 1;
					System.out.println("Program says: Too high!");
				}
			}
			playerTurn = !playerTurn; // Switch turn
		}
	}

	public void startMixedGameWithMultipePlayers() {
		int numberToGuess = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
		int playerGuess;
		int score = 0;
		boolean isGameOver = false;
		
		do {
			score++;
			for (Iterator<Player> iterator = players.iterator(); iterator.hasNext();) {
				Player player = (Player) iterator.next();

				System.out.print("Your turn " + player.getNickname() + ", guess the number: ");
				playerGuess = scanner.nextInt();
				if (playerGuess == numberToGuess) {
					System.out.println("Congratulations! " + player.getNickname() 
					+ " guessed the number. Game is over!");
					player.setBestScoreForModeAndDifficulty("Mixed", difficulty, score);
					updatePlayerScoreInFile(player, "Mixed");
					isGameOver = true;
					break;
				} else if (playerGuess < numberToGuess) {
					System.out.println("Too low!");
				} else {
					System.out.println("Too high!");
				}
			}
		} while (!isGameOver);
	}

	private void updatePlayerScoreInFile(Player player, String mode) {
		try {
			String filename = player.getNickname() + ".txt";
			File file = new File(filename);

			if (!file.exists()) {
				savePlayerData(player);
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			StringBuilder fileContent = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				if (line.contains(mode + "_" + difficulty)) {
					line = mode + "_" + difficulty + " Best Score: "
							+ player.getBestScoreForModeAndDifficulty(mode, difficulty);
				}
				fileContent.append(line).append("\n");
			}
			reader.close();

			FileWriter writer = new FileWriter(file);
			writer.write(fileContent.toString());
			writer.close();
		} catch (IOException e) {
			System.out.println("Error updating data: " + e.getMessage());
		}
	}
	

	private void savePlayerData(Player player) {
		try (PrintWriter out = new PrintWriter(new FileOutputStream(player.getNickname() + ".txt"))) {
			String[] modes = { "Standard", "Reverse", "Mixed"};
			String[] difficulties = { "Easy", "Normal", "Hard", "Custom"};

			for (String mode : modes) {
				for (String difficulty : difficulties) {
					int bestScore = player.getBestScoreForModeAndDifficulty(mode, difficulty);
					String key = mode + "_" + difficulty;
					out.println(key + " Best Score: " + bestScore);
				}
			}

			out.println("General Best Score: " + player.getBestScore());
			out.println("Current Score: " + player.getCurrentScore());
			out.println("Nickname: " + player.getNickname());
		} catch (IOException e) {
			System.out.println("Error saving data: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		GuessingNumberGame game = new GuessingNumberGame();
		game.startGame();
	}
}
