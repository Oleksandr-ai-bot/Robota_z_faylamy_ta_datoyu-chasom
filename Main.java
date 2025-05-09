import java.util.Scanner;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final String failKonfiguratsiyi = "konfiguratsiyi.txt";
    private static final String failStatystyky = "statystyka.txt";
    private static String pershyiGravets = "Гравець 1";
    private static String druhyiGravets = "Гравець 2";
    private static char symbolPershoho = 'X';
    private static int size = 3;

    public static void main(String[] args) {
        Scanner egg = new Scanner(System.in);

        Object[] config = vidnovytyKonfiguratsiyu();
        size = (int) config[0];
        symbolPershoho = (char) config[1];


        while (true) {
            mainMenu();
            if (!egg.hasNextInt()) {
                System.out.println("Помилка! Введіть число. (►__◄)");
                egg.next();
                continue;
            }

            int scan = egg.nextInt();

            if (scan == 1) {
                playGame(egg, symbolPershoho, size);
            } else if (scan == 2) {
                int[] settings = setings(egg, symbolPershoho, size);
                size = settings[0];
                symbolPershoho = (char) settings[1];
                zberehtyKonfiguratsiyu(size, symbolPershoho);
            } else if (scan == 3) {
                pokazatyStatystyku();
            } else if (scan == 4) {
                System.out.println("Дякуємо за гру! ＼（〇_ｏ）／");
                break;
            }
        }
    }

    public static void mainMenu() {
        System.out.println("ПРИВІТ!!! ༼ つ ◕_◕ ༽つ");
        System.out.println("Головне меню:\n1. Нова гра\n2. Налаштування\n3. Статистика\n4. Вихід");
    }

    public static int[] setings(Scanner egg, char currentStartPlayerSymbol, int currentSize) {
        char playerSymbolToStart = currentStartPlayerSymbol;
        int newSize = currentSize;

        while (true) {
            System.out.println("Налаштування:");
            System.out.println("1. Хто ходить першим (Зараз символ: " + playerSymbolToStart + " для гравця '" + pershyiGravets + "')");
            System.out.println("2. Розмір поля (Зараз: " + newSize + "x" + newSize + ")");
            System.out.println("3. Змінити ім'я першого гравця (Зараз: " + pershyiGravets + ")");
            System.out.println("4. Змінити ім'я другого гравця (Зараз: " + druhyiGravets + ")");
            System.out.println("5. Назад");

            if (!egg.hasNextInt()) {
                System.out.println("Помилка! Введіть число. (►__◄)");
                egg.next();
                continue;
            }
            int option = egg.nextInt();
            egg.nextLine();

            if (option == 1) {
                if (playerSymbolToStart == 'X') {
                    playerSymbolToStart = 'O';
                } else {
                    playerSymbolToStart = 'X';
                }
                System.out.println(pershyiGravets + " тепер буде починати символом: " + playerSymbolToStart);
            } else if (option == 2) {
                newSize = changeBoardSize(egg);
            } else if (option == 3) {
                System.out.print("Введіть нове ім'я для першого гравця: ");
                pershyiGravets = egg.nextLine();
                System.out.println("Ім'я першого гравця змінено на: " + pershyiGravets);
            } else if (option == 4) {
                System.out.print("Введіть нове ім'я для другого гравця: ");
                druhyiGravets = egg.nextLine();
                System.out.println("Ім'я другого гравця змінено на: " + druhyiGravets);
            } else if (option == 5) {
                break;
            }
        }
        return new int[] {newSize, playerSymbolToStart};
    }

    public static int changeBoardSize(Scanner egg) {
        while (true) {
            System.out.println("Виберіть розмір поля (3, 5, 7, 9): ");
            if (!egg.hasNextInt()) {
                System.out.println("Помилка! Введіть число. (►__◄)");
                egg.next();
                continue;
            }
            int newSizeChoice = egg.nextInt();
            if (newSizeChoice == 3 || newSizeChoice == 5 || newSizeChoice == 7 || newSizeChoice == 9) {
                return newSizeChoice;
            } else {
                System.out.println("Некоректний розмір.（︶^︶）");
            }
        }
    }

    public static char[][] createBoard(int currentSize) {
        int boardSize = currentSize * 2 + 1;
        char[][] board = new char[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        board[i][j] = '+';
                    } else {
                        board[i][j] = '-';
                    }
                } else {
                    if (j % 2 == 0) {
                        board[i][j] = '|';
                    } else {
                        board[i][j] = ' ';
                    }
                }
            }
        }

        board[0][0] = ' ';
        for (int i = 1; i < boardSize; i += 2) {
            board[i][0] = board[0][i] = (char) ('0' + (i / 2) + 1);
        }

        return board;
    }

    public static void displayBoard(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static int[] makeMove(Scanner egg, char[][] board, int currentSize, char currentPlayerSymbol, char initialSymbol) {
        int row = -1, col = -1;
        boolean validMove = false;

        String currentPlayerName;
        if (currentPlayerSymbol == initialSymbol) {
            currentPlayerName = pershyiGravets;
        } else {
            currentPlayerName = druhyiGravets;
        }

        while (!validMove) {
            System.out.println(currentPlayerName + " (" + currentPlayerSymbol + "), введіть номер рядка (1-" + currentSize + "): ");
            if (!egg.hasNextInt()) {
                System.out.println("Помилка! Введіть число. (►__◄)");
                egg.next();
                continue;
            }
            row = egg.nextInt() - 1;

            System.out.println("Введіть номер стовпця (1-" + currentSize + "): ");
            if (!egg.hasNextInt()) {
                System.out.println("Помилка! Введіть число. (►__◄)");
                egg.next();
                continue;
            }
            col = egg.nextInt() - 1;

            if (row >= 0 && row < currentSize && col >= 0 && col < currentSize) {
                row = row * 2 + 1;
                col = col * 2 + 1;

                if (board[row][col] == ' ') {
                    board[row][col] = currentPlayerSymbol;
                    validMove = true;
                } else {
                    System.out.println("Помилка! Клітинка зайнята. Спробуйте ще раз.^_____^");
                }
            } else {
                System.out.println("Помилка! Неправильний хід. Введіть номер рядка та стовпця від 1 до " + currentSize + ". ಠ﹏ಠ");
            }
        }

        return new int[]{row, col};
    }

    public static boolean checkWin(char[][] board, char playerSymbol) {
        int boardSize = board.length;
        for (int i = 1; i < boardSize; i += 2) {
            for (int j = 1; j < boardSize; j += 2) {
                if (j + 4 < boardSize && board[i][j] == playerSymbol && board[i][j+2] == playerSymbol && board[i][j+4] == playerSymbol) {
                    return true;
                }
                if (i + 4 < boardSize && board[i][j] == playerSymbol && board[i+2][j] == playerSymbol && board[i+4][j] == playerSymbol) {
                    return true;
                }
            }
        }
        for (int i = 1; i < boardSize; i += 2) {
            for (int j = 1; j < boardSize; j += 2) {
                if (i + 4 < boardSize && j + 4 < boardSize && board[i][j] == playerSymbol && board[i+2][j+2] == playerSymbol && board[i+4][j+4] == playerSymbol) {
                    return true;
                }
                if (i + 4 < boardSize && j - 4 >= 0 && board[i][j] == playerSymbol && board[i+2][j-2] == playerSymbol && board[i+4][j-4] == playerSymbol) {
                    return true;
                }
            }
        }
        return false;
    }

    public static char switchPlayer(char currentPlayerSymbol) {
        return (currentPlayerSymbol == 'X') ? 'O' : 'X';
    }

    public static void playGame(Scanner egg, char startingPlayerSymbol, int currentSize) {
        int moves = 0;
        boolean gameOn = true;
        char currentPlayerSymbol = startingPlayerSymbol;

        char[][] board = createBoard(currentSize);

        while (gameOn) {
            displayBoard(board);

            makeMove(egg, board, currentSize, currentPlayerSymbol, startingPlayerSymbol);
            moves++;

            boolean win = checkWin(board, currentPlayerSymbol);

            if (win) {
                gameOn = false;
                String winnerName;
                if (currentPlayerSymbol == startingPlayerSymbol) {
                    winnerName = pershyiGravets;
                } else {
                    winnerName = druhyiGravets;
                }
                System.out.println("Переміг " + winnerName + " (" + currentPlayerSymbol + ")! ☆*: .｡. o(≧▽≦)o .｡.:*☆");
                displayBoard(board);

                zberehtyStatystyku(winnerName, currentPlayerSymbol, currentSize, true, startingPlayerSymbol);
            } else if (moves == currentSize * currentSize) {
                System.out.println("Нічия! (づ￣ 3￣)づ");
                gameOn = false;
                displayBoard(board);

                zberehtyStatystyku("Нічия", startingPlayerSymbol, currentSize, false, startingPlayerSymbol);
            } else {
                currentPlayerSymbol = switchPlayer(currentPlayerSymbol);
            }
        }
    }

    public static void zberehtyKonfiguratsiyu(int rozmirPola, char startSymbol) {
        try {
            FileWriter fileWriter = new FileWriter(failKonfiguratsiyi);

            fileWriter.write(rozmirPola + "\n");
            fileWriter.write(startSymbol + "\n"); // Символ для першого гравця (pershyiGravets)
            fileWriter.write(pershyiGravets + "\n");
            fileWriter.write(druhyiGravets + "\n");

            fileWriter.close();
            System.out.println("\n✓ Конфігурацію успішно збережено!");
        } catch (IOException e) {
            System.out.println("\nПомилка збереження конфігурації: " + e.getMessage());
        }
    }

    public static Object[] vidnovytyKonfiguratsiyu() {
        int currentSize = 3;
        char firstPlayer = 'X';

        try {
            File file = new File(failKonfiguratsiyi);

            if (!file.exists()) {
                System.out.println("Файл конфігурації не знайдено. Використовую стандартні налаштування.");
                return new Object[]{currentSize, firstPlayer};
            }

            FileReader fileReader = new FileReader(file);
            Scanner fileSkan = new Scanner(fileReader);

            if (fileSkan.hasNextLine()) {
                currentSize = Integer.parseInt(fileSkan.nextLine());
            }
            if (fileSkan.hasNextLine()) {
                firstPlayer = fileSkan.nextLine().charAt(0);
            }
            if (fileSkan.hasNextLine()) {
                pershyiGravets = fileSkan.nextLine();
            }
            if (fileSkan.hasNextLine()) {
                druhyiGravets = fileSkan.nextLine();
            }

            fileSkan.close();
            System.out.println("✓ Конфігурацію успішно відновлено з файлу!");

        } catch (IOException e) {
            System.out.println("Помилка читання файлу конфігурації: " + e.getMessage() + ". Використовую стандартні.");
        } catch (NumberFormatException e) {
            System.out.println("Помилка формату файлу конфігурації (розмір або символ): " + e.getMessage() + ". Використовую стандартні.");
        } catch (Exception e) {
            System.out.println("Неочікувана помилка при відновленні конфігурації: " + e.getMessage() + ". Використовую стандартні.");
        }

        Main.size = currentSize;
        Main.symbolPershoho = firstPlayer;
        return new Object[]{currentSize, firstPlayer};
    }
    public static void zberehtyStatystyku(String peremozhets, char symbolVykorystanyj, int rozmirPola, boolean vyhra, char initialSymbol) {
        try {
            FileWriter fileWriter = new FileWriter(failStatystyky, true);
            LocalDateTime zaraz = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dataChas = zaraz.format(formatter);

            String resultMessage;
            String playerInfo;

            if (vyhra) {
                resultMessage = peremozhets + " переміг";
                playerInfo = peremozhets + " грав(ла) за " + symbolVykorystanyj;
            } else { 
                resultMessage = "Нічия";
                char symbolDruhoho = (initialSymbol == 'X') ? 'O' : 'X';
                playerInfo = pershyiGravets + " (" + initialSymbol + ") vs " + druhyiGravets + " (" + symbolDruhoho + ")";
            }

            String statystyka = dataChas + "\t" +
                    rozmirPola + "x" + rozmirPola + "\t" +
                    playerInfo + "\t" +
                    resultMessage + "\n";

            fileWriter.write(statystyka);
            fileWriter.close();
            System.out.println("\n✓ Статистику гри збережено!");
        } catch (IOException e) {
            System.out.println("\nПомилка збереження статистики: " + e.getMessage());
        }
    }

    public static void pokazatyStatystyku() {
        try {
            File file = new File(failStatystyky);

            if (!file.exists() || file.length() == 0) {
                System.out.println("\nСтатистика порожня. Зіграйте хоча б одну гру!");
                return;
            }

            FileReader fileReader = new FileReader(file);
            Scanner fileSkan = new Scanner(fileReader);

            System.out.println("\n┌─────────────────────────────────────────────────────────────────────────────────┐");
            System.out.println("│ СТАТИСТИКА ІГОР                                                                 │");
            System.out.println("└─────────────────────────────────────────────────────────────────────────────────┘");
            System.out.println("Дата та час\t\tРозмір\tГравці (символи)\t\t\tРезультат");
            System.out.println("───────────────────────────────────────────────────────────────────────────────────");

            int kilkistIhor = 0;

            while (fileSkan.hasNextLine()) {
                String ryadok = fileSkan.nextLine();
                System.out.println(ryadok);
                kilkistIhor++;
            }

            System.out.println("───────────────────────────────────────────────────────────────────────────────────");
            System.out.println("Загальна кількість ігор: " + kilkistIhor);

            fileSkan.close();
        } catch (IOException e) {
            System.out.println("Помилка читання файлу статистики: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неочікувана помилка при відображенні статистики: " + e.getMessage());
        }
    }
}