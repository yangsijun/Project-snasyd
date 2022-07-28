import java.util.ArrayList;
import java.util.Objects;

public class NextDraw {
    final static private int EMPTY = 0;
    final static private int BLACK = 1;
    final static private int WHITE = 2;
    final static private int RED = 3;
    public final static int HORIZONTAL = 0;
    public final static int VERTICAL = 1;
    public final static int DIA_LTRB = 2;
    public final static int DIA_RTLB = 3;
    private int[][] board;
    private long[][] boardBits;

    Stone[] lastDrawStones;
    int myColor;
    int enemyColor;

    public class Stone {
        public int x;
        public int y;

        Stone(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    NextDraw(int[][] board, int myColor, int enemyColor, String lastDraw) {
        this.board = board;
        this.myColor = myColor;
        this.enemyColor = enemyColor;

        lastDrawStones = new Stone[2];

        String[] stones = lastDraw.split(":");
        int cnt = 0;
        for (String stone : stones) {
            String lowerCaseStone = stone.toLowerCase();
            int letter = lowerCaseStone.charAt(0);
            int tenth = lowerCaseStone.charAt(1);
            int units = lowerCaseStone.charAt(2);

            int i = letter - 'a';
            int j = (tenth - '0') * 10 + (units - '0') - 1;

            if (i != 8) {
                if (i > 7) {
                    i -= 1;
                }
                if (i >= 0 && i <= 18 && j >= 0 && j <= 18) {
                    lastDrawStones[cnt++] = new Stone(i, j);
                }
            }
        }
    }

    public String getNextDraw() {
        BoardBitsConverter boardBitsConverter = new BoardBitsConverter(board);
        boardBits = boardBitsConverter.getBoardBits();
        ArrayList<Stone> nextDrawStones;
        String nextDrawString;

        nextDrawStones = findMyWinningShot();

        if (nextDrawStones != null) {
            // 7 --> H (7), 8 --> J (9)
            char stoneX = (nextDrawStones.get(0).x >= 8) ? (char) (nextDrawStones.get(0).x + 'A' + 1) : (char) (nextDrawStones.get(0).x + 'A');
            nextDrawString = String.format("%c%02d", stoneX, nextDrawStones.get(0).y + 1);
            if (nextDrawStones.get(1) != null) {
                stoneX = (nextDrawStones.get(1).x >= 8) ? (char) (nextDrawStones.get(1).x + 'A' + 1) : (char) (nextDrawStones.get(1).x + 'A');
                nextDrawString += String.format(":%c%02d", stoneX, nextDrawStones.get(1).y + 1);
            }
            System.out.println("This is Winning Shot");
            return nextDrawString;
        }

        nextDrawStones = findMyBestDefenseDefense(board);

        System.out.println("nextDrawStones: " + nextDrawStones.get(0).x + " " + nextDrawStones.get(0).y);
        System.out.println("nextDrawStones: " + nextDrawStones.get(1).x + " " + nextDrawStones.get(1).y);

        // 7 --> H (7), 8 --> J (9)
        char stoneX = (nextDrawStones.get(0).x >= 8) ? (char) (nextDrawStones.get(0).x + 'A' + 1) : (char) (nextDrawStones.get(0).x + 'A');
        nextDrawString = String.format("%c%02d", stoneX, nextDrawStones.get(0).y + 1);
        if (nextDrawStones.get(1) != null) {
            stoneX = (nextDrawStones.get(1).x >= 8) ? (char) (nextDrawStones.get(1).x + 'A' + 1) : (char) (nextDrawStones.get(1).x + 'A');
            nextDrawString += String.format(":%c%02d", stoneX, nextDrawStones.get(1).y + 1);
        }
        return nextDrawString;

//        return "";
    }

    private ArrayList<Stone> findMyWinningShot() {
        // Windows Algorithm
        ThreatDetector threatDetector = new ThreatDetector(boardBits, myColor, enemyColor);
        ArrayList<ThreatDetector.Threat> threats = threatDetector.findCriticalThreat(boardBits, myColor);
        ArrayList<Stone> winningShot = new ArrayList<>(2);
        int[] checkOrder = {3, 4, 2, 5, 1, 6, 0};
        // found my critical threat
        if (threats != null) {
//            ThreatDetector.Threat threat = threats.get(0);
//            ArrayList<Stone> winningShot = new ArrayList<>(2);
//            int[][] dirMap = {{1, 0}, {0, 1}, {1, 1}, {1, -1}, {-1, 0}, {0, -1}, {-1, -1}, {-1, 1}};
//            int startX = -1, endX = 19, startY = -1, endY = 19;
//
//            switch (threat.dir) {
//                case HORIZONTAL -> {
//                    startX = threat.stoneNum;
//                    endX = threat.stoneNum + 6;
//                    startY = threat.lineNum;
//                    endY = threat.lineNum;
//
//
//                }
//                case VERTICAL -> {
//                    startX = threat.lineNum;
//                    endX = threat.lineNum;
//                    startY = threat.stoneNum;
//                    endY = threat.stoneNum + 6;
//                }
//                case DIA_LTRB -> {
//                    if (threat.lineNum < 19) {
//                        startX = threat.stoneNum;
//                        endX = threat.stoneNum + 6;
//                        startY = 18 - threat.lineNum + threat.stoneNum;
//                        endY = 18 - threat.lineNum + threat.stoneNum + 6;
//                    } else {
//                        startX = threat.lineNum - 18 + threat.stoneNum;
//                        endX = threat.lineNum - 18 + threat.stoneNum + 6;
//                        startY = threat.stoneNum;
//                        endY = threat.stoneNum + 6;
//                    }
//                }
//                case DIA_RTLB -> {
//                    if (threat.lineNum < 19) {
//                        startX = threat.lineNum - threat.stoneNum;
//                        endX = threat.lineNum - (threat.stoneNum + 6);
//                        startY = threat.stoneNum;
//                        endY = threat.stoneNum + 6;
//                    } else {
//                        startX = 18 - threat.stoneNum;
//                        endX = 18 - (threat.stoneNum + 6);
//                        startY = threat.lineNum - 18 + threat.stoneNum;
//                        endY = threat.lineNum - 18 + threat.stoneNum + 6;
//                    }
//                }
//            }
            ThreatDetector.Threat threat = threats.get(0);
            switch (threat.dir) {
                case HORIZONTAL -> {
                    int startX = threat.stoneNum;
                    int endX = threat.stoneNum + 6;
                    int Y = threat.lineNum;

                    for (int i = 0; i < 7; i++) {
                        if (board[Y][startX + checkOrder[i]] == EMPTY) {
                            winningShot.add(new Stone(startX + checkOrder[i], Y));
                        }
                    }
                }
                case VERTICAL -> {
                    int X = threat.lineNum;
                    int startY = threat.stoneNum;
                    int endY = threat.stoneNum + 6;

                    for (int i = 0; i < 7; i++) {
                        if (board[startY + checkOrder[i]][X] == EMPTY) {
                            winningShot.add(new Stone(X, startY + checkOrder[i]));
                        }
                    }
                }
                case DIA_LTRB -> {
                    int startX, endX, startY, endY;
                    if (threat.lineNum < 19) {
                        startX = threat.stoneNum;
                        endX = threat.stoneNum + 6;
                        startY = 18 - threat.lineNum + threat.stoneNum;
                        endY = 18 - threat.lineNum + threat.stoneNum + 6;
                    } else {
                        startX = threat.lineNum - 18 + threat.stoneNum;
                        endX = threat.lineNum - 18 + threat.stoneNum + 6;
                        startY = threat.stoneNum;
                        endY = threat.stoneNum + 6;
                    }

                    for (int i = 0; i < 7; i++) {
                        if (board[startY + checkOrder[i]][startX + checkOrder[i]] == EMPTY) {
                            winningShot.add(new Stone(startX + checkOrder[i], startY + checkOrder[i]));
                        }
                    }
                }
                case DIA_RTLB -> {
                    int startX, endX, startY, endY;
                    if (threat.lineNum < 19) {
                        startX = threat.lineNum - threat.stoneNum;
                        endX = threat.lineNum - (threat.stoneNum + 6);
                        startY = threat.stoneNum;
                        endY = threat.stoneNum + 6;
                    } else {
                        startX = 18 - threat.stoneNum;
                        endX = 18 - (threat.stoneNum + 6);
                        startY = threat.lineNum - 18 + threat.stoneNum;
                        endY = threat.lineNum - 18 + threat.stoneNum + 6;
                    }

                    for (int i = 0; i < 7; i++) {
                        if (board[startY + checkOrder[i]][startX + checkOrder[i]] == EMPTY) {
                            winningShot.add(new Stone(startX + checkOrder[i], startY + checkOrder[i]));
                        }
                    }
                }
            }
            if (winningShot.size() == 2) {
                return winningShot;
            } else {
                board[winningShot.get(0).y][winningShot.get(0).x] = myColor;
                for (int i = 0; i < 19; i++) {
                    for (int j = 0; j < 19; j++) {
                        if (board[i][j] == EMPTY) {
                            winningShot.add(new Stone(i, j));
                            return winningShot;
                        }
                    }
                }
                return winningShot;
            }
        }
        // there isn't critical threat
        return null;
    }

//    private ArrayList<Stone> findEnemyWinningShot() {
//        ThreatDetector threatDetector = new ThreatDetector(boardBits, enemyColor, myColor);
//        ArrayList<ThreatDetector.Threat> threats = threatDetector.findCriticalThreat(boardBits, enemyColor);
//
//        // found enemy's critical threat
//        if (threats != null) {
//            for (ThreatDetector.Threat threat : threats) {
//                switch (threat.dir) {
//                    case HORIZONTAL:
//                        long boardBitsOneLine = boardBits[threat.dir][threat.y];
//
//                        int threatStartX = Math.max(threat.x - 2, 0);
//                        int threatEndX = Math.min(threat.x + 6 + 2, 18);
//
//                        for (int i = threatStartX; i <= threatEndX; i++) {
//                            int stone
//                        }
//                }
//            }
//        }
//    }

    private ArrayList<Stone> findMyBestDefenseDefense(int[][] board) {

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

        ArrayList<Stone> possibleDefense = new ArrayList<>();
        int minX = 0, maxX = 18, minY = 0, maxY = 18;
//        for (Stone stone : lastDrawStones) {
//            if (stone.x < minX)
//                minX = stone.x;
//            if (stone.x > maxX)
//                maxX = stone.x;
//            if (stone.y < minY)
//                minY = stone.y;
//            if (stone.y > maxY)
//                maxY = stone.y;
//        }
//
//        minX = Math.max(0, minX - 19);
//        minY = Math.max(0, minY - 19);
//        maxX = Math.min(18, maxX + 19);
//        maxY = Math.min(18, maxY + 19);

        for (int i = minY; i <= maxY; i++) {
            for (int j = minX; j <= maxX; j++) {
                if (board[i][j] == EMPTY) {
                    possibleDefense.add(new Stone(j, i));
                }
            }
        }

        ArrayList<Stone> bestDefense = new ArrayList<>();
        double maxScore = 0;
        Stone maxScoreStone = null;

        for (Stone stone : possibleDefense) {
            double eval = getEvaluation(board, stone, enemyColor);
            if (eval > maxScore) {
                maxScore = eval;
                maxScoreStone = stone;
            }
        }

        bestDefense.add(maxScoreStone);
        possibleDefense.remove(maxScoreStone);
        board[Objects.requireNonNull(maxScoreStone).y][maxScoreStone.x] = myColor;

        maxScore = 0;
        maxScoreStone = null;
        for (Stone stone : possibleDefense) {
            double eval = getEvaluation(board, stone, enemyColor);
            if (eval > maxScore) {
                maxScore = eval;
                maxScoreStone = stone;
            }
        }
        bestDefense.add(maxScoreStone);

        return bestDefense;
    }

//    private ArrayList<Stone> findMyBestOffense() {
//
//    }

    private double getEvaluation(int[][] board, Stone stone, int myColor) {
        int enemyColor = (myColor == BLACK) ? WHITE : BLACK;
        int emptyWight = 2;
        double eval = 0;
        double[] weight = {Math.pow(2, 12), Math.pow(2, 11), Math.pow(2, 10), Math.pow(2, 9), Math.pow(2, 8)};
        double[] dirFreeWeight = {1.0, 1.0, 1.0000181862, 1.0000363725, 1.0000726562};
        int[][][] dirMap = {
                {{1, 0}, {-1, 0}}, // HORIZONTAL
                {{0, 1}, {0, -1}}, // VERTICAL
                {{1, 1}, {-1, -1}}, // DIA_LTRB
                {{1, -1}, {-1, 1}} // DIA_LBRT
        };

        int freeDir = 4;
        for (int i = 0; i < 4; i++) { // dir
            boolean isFreeDir = true;
            double evalDir = 1;
            for (int j = 0; j < 2; j++) { // + or -
                for (int k = 0; k < 5; k++) { // distance
                    int checkingX = stone.x + (k + 1) * dirMap[i][j][0];
                    int checkingY = stone.y + (k + 1) * dirMap[i][j][1];

                    if (checkingX < 0 || checkingX > 18 || checkingY < 0 || checkingY > 18) {
                        isFreeDir = false;
                        break;
                    } else {
                        int checkingStone = board[checkingY][checkingX];
                        if (checkingStone == enemyColor || checkingStone == RED) {
                            isFreeDir = false;
                            break;
                        } else if (checkingStone == EMPTY) {
                            evalDir *= emptyWight;
                        } else if (checkingStone == myColor) {
                            evalDir *= weight[k];
                        }
                    }
                }
            }
            eval += evalDir;
            if (isFreeDir == false) {
                freeDir--;
            }
        }
        eval *= dirFreeWeight[freeDir];
        return eval;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
}