import java.util.Scanner;
import java.util.Random;

/**
 * Simple "Who Wants to Be a Millionaire" style quiz game in Java.
 * Player answers multiple-choice questions and can use lifelines.
 */
public class quizgame {

    public static void main(String[] args) {

        // Scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Random generator for lifelines
        Random random = new Random();

        // ================================
        // QUESTIONS + ANSWERS
        // Format:
        // [0] -> question text
        // [1-4] -> A, B, C, D answers
        // [5] -> correct letter
        // ================================
        String[][] questions = {
                {"Which data type stores whole numbers in Java?", "float", "char", "int", "boolean", "C"},
                {"Which operator represents logical AND?", "||", "&&", "!=", "==", "B"},
                {"Which is NOT a primitive type?", "int", "double", "boolean", "String", "D"},
                {"Which loop always runs at least once?", "for", "while", "do-while", "foreach", "C"},
                {"Which access modifier is the most strict?", "public", "protected", "default", "private", "D"},
                {"Which keyword is used to handle exceptions?", "catch", "error", "exception", "handle", "A"},
                {"Which class is the ancestor of every Java class?", "Base", "Main", "Object", "Class", "C"},
                {"What does the final keyword do?", "Deletes class", "Allows modification", "Prevents modification/inheritance", "Frees memory", "C"},
                {"What happens if there is no try block before catch?", "Program runs", "Compile-time error", "Runtime error", "Warning", "B"},
                {"What is one of Java's biggest advantages?", "Windows only", "Slow but safe", "Platform independent", "Web only", "C"},
                {"Which file extension contains Java source code?", ".exe", ".class", ".java", ".jar", "C"},
                {"Which keyword allows calling a method at class level?", "final", "static", "void", "this", "B"},
                {"What will System.out.println(5 + 3 + \"Java\"); print?", "8Java", "53Java", "Java8", "Compile error", "A"},
                {"Which statement is TRUE about Java?", "Multiple inheritance with classes", "No garbage collection", "Interfaces can't have methods", "Automatic memory management", "D"},
                {"Which keyword prints text to the console?", "print()", "console.log()", "System.out.println()", "echo", "C"}
        };

        // The Correct answers for each question
        String[] correctAnswers = {
                "C","B","D","C","D",
                "A","C","C","B","C",
                "C","B","A","D","C"
        };

        // Prize money for each round
        int[] prizeLevels = {
                10000, 20000, 50000, 100000, 250000,
                500000, 750000, 1000000, 1500000, 2000000,
                5000000, 10000000, 15000000, 25000000, 50000000
        };

        // Lifelines:
        // [0] -> fifty-fifty
        // [1] -> audience
        // [2] -> phone
        boolean[] lifelinesAvailable = {true, true, true};

        // Current prize amount
        int currentPrize = 0;

        // Guaranteed prize at checkpoints
        int guaranteedPrize = 0;


        // ================================
        // MAIN GAME LOOP
        // ================================
        for (int i = 0; i < questions.length; i++) {

            // Print question number and text
            System.out.println("\n" + (i + 1) + ". Question: " + questions[i][0]);

            // Print A-D answers
            for (int j = 1; j <= 4; j++) {
                System.out.println((char) ('A' + j - 1) + ") " + questions[i][j]);
            }

            // Display available lifelines
            System.out.print("\nAvailable lifelines (one-time use): ");
            if (lifelinesAvailable[0]) System.out.print("50/50 ");
            if (lifelinesAvailable[1]) System.out.print("Audience ");
            if (lifelinesAvailable[2]) System.out.print("Phone ");
            System.out.println();

            // Player's input
            String input;

            // Keep asking until valid
            while (true) {

                System.out.print("Answer or use lifeline (A-D / F / K / T): ");
                input = scanner.nextLine().toUpperCase();

                // Player selected an answer
                if (input.matches("[ABCD]")) {
                    break;

                    // Fifty-fifty lifeline
                } else if (input.equals("F") && lifelinesAvailable[0]) {
                    lifelinesAvailable[0] = false;
                    fiftyFifty(questions[i], correctAnswers[i], random);

                    // Audience lifeline
                } else if (input.equals("K") && lifelinesAvailable[1]) {
                    lifelinesAvailable[1] = false;
                    audienceHelp(correctAnswers[i]);

                    // Phone lifeline
                } else if (input.equals("T") && lifelinesAvailable[2]) {
                    lifelinesAvailable[2] = false;
                    phoneFriend(correctAnswers[i]);

                    // Already used messages
                } else if (input.equals("F")) {
                    System.out.println("50/50 already used.");
                } else if (input.equals("K")) {
                    System.out.println("Audience already used.");
                } else if (input.equals("T")) {
                    System.out.println("Phone already used.");
                } else {
                    System.out.println("Invalid input.");
                }

                System.out.println("-----");
            }

            System.out.println("---------------------------");

            // ================================
            // CHECK ANSWER
            // ================================
            if (input.equals(correctAnswers[i])) {

                System.out.println("Correct answer!");

                // Update current prize
                currentPrize = prizeLevels[i];

                // Checkpoints at question 5, 10, 15
                if (i == 4 || i == 9 || i == 14) {

                    guaranteedPrize = currentPrize;

                    System.out.println(
                            "Checkpoint reached! Guaranteed prize: " +
                                    guaranteedPrize + " Ft");

                    // Offer to quit unless it's final question
                    if (i != 14) {
                        System.out.print("Continue playing? (Y/N): ");
                        String decision = scanner.nextLine().toUpperCase();

                        if (decision.equals("N")) {
                            System.out.println(
                                    "You walk away with: " +
                                            guaranteedPrize + " Ft");
                            break;
                        }
                    }

                } else {
                    System.out.println(
                            "Current prize: " +
                                    currentPrize + " Ft");
                }


                // WRONG ANSWER
            } else {

                System.out.println("Wrong answer. Game over.");
                System.out.println("Correct answer was: " + correctAnswers[i]);
                System.out.println(
                        "You won: " +
                                guaranteedPrize + " Ft");
                break;
            }
        }
    }

    /**
     * Fifty-fifty lifeline:
     * Shows the correct answer and one random incorrect answer.
     */
    public static void fiftyFifty(String[] question, String correct, Random random) {

        int wrongIndex;

        // Pick a random wrong answer index (1-4)
        do {
            wrongIndex = random.nextInt(4) + 1;
        } while (wrongIndex == letterToIndex(correct));

        System.out.println(
                "50/50 used. Choose between:");

        System.out.println(
                question[letterToIndex(correct)] +
                        " or " +
                        question[wrongIndex]);
    }

    /**
     * Audience lifeline:
     * Generates a random suggestion.
     */
    public static void audienceHelp(String correct) {

        Random random = new Random();

        char suggested =
                (char) ('A' + random.nextInt(4));

        System.out.println(
                "Audience suggests: " +
                        suggested);
    }

    /**
     * Phone a friend lifeline:
     * Generates a random suggestion.
     */
    public static void phoneFriend(String correct) {

        Random random = new Random();

        char suggested =
                (char) ('A' + random.nextInt(4));

        System.out.println(
                "Your friend thinks the answer is: " +
                        suggested);
    }

    /**
     * Converts answer letter (A-D)
     * into array index (1-4).
     */
    public static int letterToIndex(String letter) {
        return letter.charAt(0) - 'A' + 1;
    }
}