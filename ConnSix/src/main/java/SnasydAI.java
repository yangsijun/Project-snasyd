import java.util.Scanner;

public class SnasydAI {

    final static private int EMPTY = 0;
    final static private int BLACK = 1;
    final static private int WHITE = 2;
    final static private int RED = 3;
    private static int[][] board = new int[19][19];

    public static void main(String[] args) throws Exception {

        // Read the ip address
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the ip address > ");
        String ip = scanner.nextLine();
        // Read the port number
        System.out.print("Input the port number > ");
        int port = Integer.parseInt(scanner.nextLine());
        // Read the color
        System.out.print("Input the color > ");
        String color = scanner.nextLine();

        // Connect to the server
        ConnectSix conSix = new ConnectSix(ip, port, color);
        // Red Stone positions
        System.out.println("Red Stone positions are " + conSix.redStones);
        if (conSix.redStones != null)
            drawToBoard(conSix.redStones, RED);

        System.out.println("This is SnasydAI");
        int myColor;
        int enemyColor;
        String read;

        if (color.toLowerCase().compareTo("black") == 0) { // if the color is black
            // Draw and read the first stone (Center of the board), (White's first draw)
            myColor = BLACK;
            enemyColor = WHITE;
            read = conSix.drawAndRead("K10");
            drawToBoard("K10", BLACK);
            drawToBoard(read, WHITE);
        } else /*if (color.toLowerCase().compareTo("white") == 0)*/ { // if the color is white
            // Draw and read the first stone (Empty draw), (Black's first draw)
            myColor = WHITE;
            enemyColor = BLACK;
            read = conSix.drawAndRead("");
            drawToBoard(read, BLACK);
        }

        // While the game is not over
        while (true) {

//            // Stone to draw
//            char alpha1 = (char) ((Math.random() * 19) + 'A');
//            int num1 = (int)( Math.random() * 19) + 1;
//            char alpha2 = (char) ((Math.random() * 19) + 'A');
//            int num2 = (int)( Math.random() * 19) + 1;
//
//            // Draw
//            String draw = String.format("%c%02d:%c%02d", alpha1, num1, alpha2, num2);

            for (int i = 0; i < 19; i++) {
                for (int j = 0; j < 19; j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();

            NextDraw nextDraw = new NextDraw(board, myColor, enemyColor, read);

            // draw
            String draw = nextDraw.getNextDraw();

            drawToBoard(draw, myColor);

            // Read
            read = conSix.drawAndRead(draw);

            drawToBoard(read, enemyColor);

            // Check if the game is over
            if(read.compareTo("WIN") == 0 || read.compareTo("LOSE") == 0 || read.compareTo("EVEN") == 0) {
                break;
            }
        }
    }

    private static void drawToBoard(String draw, int color) {
        System.out.println(draw);
        // readStones
        String[] stones = draw.split(":");
        for (String stone : stones) {
            // updateBoard
            String lowerCaseStone = stone.toLowerCase();
            int letter = lowerCaseStone.charAt(0);
            int tenth = lowerCaseStone.charAt(1);
            int units = lowerCaseStone.charAt(2);

            int j = letter - 'a';
            int i = (tenth - '0') * 10 + (units - '0') - 1;

            System.out.println("i = " + i + " j = " + j);

            if (j != 8) {
                if (j > 7) {
                    j -= 1;
                }
                if (i >= 0 && i <= 18 && j >= 0 && j <= 18 && board[i][j] == EMPTY)
                    board[i][j] = color;
            }

            for (int a = 0; a < 19; a++) {
                for (int b = 0; b < 19; b++) {
                    System.out.print(board[a][b] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

}