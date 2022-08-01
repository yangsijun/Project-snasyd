import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NextDraw {
    final static private int EMPTY = 0;
    final static private int BLACK = 1;
    final static private int WHITE = 2;
    final static private int RED = 3;
    public final static int HORIZONTAL = 0;
    public final static int VERTICAL = 1;
    public final static int DIA_LTRB = 2;
    public final static int DIA_RTLB = 3;
    public final static int DRAW = 3;
    public final static int NOT_OVER = -1;

    private int[][] board;
    private long[][] boardBits;

    Stone[] lastDrawStones;

    private Move nextMove;

    private List<Move> availableMoves;
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

    public class Move {
        Stone stone1;
        Stone stone2;

        Move(Stone stone1, Stone stone2) {
            this.stone1 = stone1;
            this.stone2 = stone2;
        }

        Move(Stone stone) {
            this.stone1 = stone;
            this.stone2 = null;
        }
        Move() {
            this.stone1 = null;
            this.stone2 = null;
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

//    public String getNextDraw() {
//        BoardBitsConverter boardBitsConverter = new BoardBitsConverter(board);
//        boardBits = boardBitsConverter.getBoardBits();
//        ArrayList<Stone> nextDrawStones;
//        String nextDrawString;
//
//        nextDrawStones = findMyWinningShot();
//
//        if (nextDrawStones != null) {
//            // 7 --> H (7), 8 --> J (9)
//            char stoneX = (nextDrawStones.get(0).x >= 8) ? (char) (nextDrawStones.get(0).x + 'A' + 1) : (char) (nextDrawStones.get(0).x + 'A');
//            nextDrawString = String.format("%c%02d", stoneX, nextDrawStones.get(0).y + 1);
//            if (nextDrawStones.get(1) != null) {
//                stoneX = (nextDrawStones.get(1).x >= 8) ? (char) (nextDrawStones.get(1).x + 'A' + 1) : (char) (nextDrawStones.get(1).x + 'A');
//                nextDrawString += String.format(":%c%02d", stoneX, nextDrawStones.get(1).y + 1);
//            }
//            System.out.println("This is Winning Shot");
//            return nextDrawString;
//        }
//
//        nextDrawStones = findMyBestDefenseDefense(board);
//
//        System.out.println("nextDrawStones: " + nextDrawStones.get(0).x + " " + nextDrawStones.get(0).y);
//        System.out.println("nextDrawStones: " + nextDrawStones.get(1).x + " " + nextDrawStones.get(1).y);
//
//        // 7 --> H (7), 8 --> J (9)
//        char stoneX = (nextDrawStones.get(0).x >= 8) ? (char) (nextDrawStones.get(0).x + 'A' + 1) : (char) (nextDrawStones.get(0).x + 'A');
//        nextDrawString = String.format("%c%02d", stoneX, nextDrawStones.get(0).y + 1);
//        if (nextDrawStones.get(1) != null) {
//            stoneX = (nextDrawStones.get(1).x >= 8) ? (char) (nextDrawStones.get(1).x + 'A' + 1) : (char) (nextDrawStones.get(1).x + 'A');
//            nextDrawString += String.format(":%c%02d", stoneX, nextDrawStones.get(1).y + 1);
//        }
//        return nextDrawString;
//    }

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

        minimax(board, 0, myColor);
        System.out.println("nextDrawStones: " + nextMove.stone1.x + " " + nextMove.stone1.y);
        System.out.println("nextDrawStones: " + nextMove.stone2.x + " " + nextMove.stone2.y);

        // 7 --> H (7), 8 --> J (9)
        char stoneX = (nextMove.stone1.x >= 8) ? (char) (nextMove.stone1.x + 'A' + 1) : (char) (nextMove.stone1.x + 'A');
        nextDrawString = String.format("%c%02d", stoneX, nextMove.stone1.y + 1);
        if (nextMove.stone2 != null) {
            stoneX = (nextMove.stone2.x >= 8) ? (char) (nextMove.stone2.x + 'A' + 1) : (char) (nextMove.stone2.x + 'A');
            nextDrawString += String.format(":%c%02d", stoneX, nextMove.stone2.y + 1);
        }
        return nextDrawString;
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

                    for (int i = 0; i < 7; i++) {
                        if (board[startY + checkOrder[i]][X] == EMPTY) {
                            winningShot.add(new Stone(X, startY + checkOrder[i]));
                        }
                    }
                }
                case DIA_LTRB -> {
                    int startX, startY;
                    if (threat.lineNum < 19) {
                        startX = threat.stoneNum;
                        startY = 18 - threat.lineNum + threat.stoneNum;
                    } else {
                        startX = threat.lineNum - 18 + threat.stoneNum;
                        startY = threat.stoneNum;
                    }

                    for (int i = 0; i < 7; i++) {
                        if (board[startY + checkOrder[i]][startX + checkOrder[i]] == EMPTY) {
                            winningShot.add(new Stone(startX + checkOrder[i], startY + checkOrder[i]));
                        }
                    }
                }
                case DIA_RTLB -> {
                    int startX, startY;
                    if (threat.lineNum < 19) {
                        startX = threat.lineNum - threat.stoneNum;
                        startY = threat.stoneNum;
                    } else {
                        startX = 18 - threat.stoneNum;
                        startY = threat.lineNum - 18 + threat.stoneNum;
                    }

                    for (int i = 0; i < 7; i++) {
                        if (board[startY + checkOrder[i]][startX - checkOrder[i]] == EMPTY) {
                            winningShot.add(new Stone(startX - checkOrder[i], startY + checkOrder[i]));
                        }
                    }
                }
            }
            if (winningShot.size() != 2) {
                board[winningShot.get(0).y][winningShot.get(0).x] = myColor;
                for (int i = 0; i < 19; i++) {
                    for (int j = 0; j < 19; j++) {
                        if (board[i][j] == EMPTY) {
                            winningShot.add(new Stone(i, j));
                            return winningShot;
                        }
                    }
                }
            }
            return winningShot;
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

        ArrayList<Stone> availableDefense = new ArrayList<>();
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
                    availableDefense.add(new Stone(j, i));
                }
            }
        }

        ArrayList<Stone> bestDefense = new ArrayList<>();
        double maxScore = 0;
        Stone maxScoreStone = null;

        for (Stone stone : availableDefense) {
            double eval = getEvaluation(board, stone, enemyColor);
            if (eval > maxScore) {
                maxScore = eval;
                maxScoreStone = stone;
            }
        }

        bestDefense.add(maxScoreStone);
        availableDefense.remove(maxScoreStone);
        board[Objects.requireNonNull(maxScoreStone).y][maxScoreStone.x] = myColor;

        maxScore = 0;
        maxScoreStone = null;
        for (Stone stone : availableDefense) {
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

    public List<Move> getAvailableMoves(int[][] board, int color) {
        int myColor = color;
        int enemyColor = color == BLACK ? WHITE : BLACK;
        class StoneAndScore {
            Stone stone;
            double score;

            public StoneAndScore(StoneAndScore stoneAndScore) {
                this.stone = stoneAndScore.stone;
                this.score = stoneAndScore.score;
            }
            public StoneAndScore(Stone stone, double score) {
                this.stone = stone;
                this.score = score;
            }
        }

        int[][] checkedAvailable = new int[19][19];
        List<Move> availableMoves = new ArrayList<>();

        // find available moves
        // Defensive
        ArrayList<StoneAndScore> availableDefense = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (board[i][j] == EMPTY) {
                    double eval = getEvaluation(board, new Stone(j, i), enemyColor);
                    if (availableDefense.size() == 0) {
                        availableDefense.add(new StoneAndScore(new Stone(j, i), eval));
                    } else {
                        for (int k = 0; k < availableDefense.size(); k++) {
                            if (eval > availableDefense.get(k).score) {
                                availableDefense.add(k, new StoneAndScore(new Stone(j, i), eval));
                                break;
                            }
                        }
                        if (availableDefense.size() > 2) {
                            availableDefense.remove(2);
                        }
                    }
                }
            }
        }

        for (StoneAndScore stone1 : availableDefense) {
            ArrayList<StoneAndScore> availableDefenseDefense = new ArrayList<>();
            ArrayList<StoneAndScore> availableDefenseOffense = new ArrayList<>();
            int[][] boardCopy = new int[19][19];
            for (int i = 0; i < 19; i++) {
                System.arraycopy(board[i], 0, boardCopy[i], 0, 19);
            }
            boardCopy[stone1.stone.y][stone1.stone.x] = myColor;
            for (int i = 0; i < 19; i++) {
                for (int j = 0; j < 19; j++) {
                    if (boardCopy[i][j] == EMPTY) {
                        // Defense Defense
                        double evalDefense = getEvaluation(boardCopy, new Stone(j, i), enemyColor);
                        if (availableDefenseDefense.size() == 0) {
                            availableDefenseDefense.add(new StoneAndScore(new Stone(j, i), evalDefense));
                        } else {
                            for (int k = 0; k < availableDefenseDefense.size(); k++) {
                                if (evalDefense > availableDefenseDefense.get(k).score) {
                                    availableDefenseDefense.add(k, new StoneAndScore(new Stone(j, i), evalDefense));
                                    if (availableDefenseDefense.size() > 2) {
                                        availableDefenseDefense.remove(2);
                                    }
                                    break;
                                }
                            }
                        }
                        // Defense Offense
                        double evalOffense = getEvaluation(boardCopy, new Stone(j, i), myColor);
                        if (availableDefenseOffense.size() == 0) {
                            availableDefenseOffense.add(new StoneAndScore(new Stone(j, i), evalOffense));
                        } else {
                            for (int k = 0; k < availableDefenseOffense.size(); k++) {
                                if (evalDefense > availableDefenseOffense.get(k).score) {
                                    availableDefenseOffense.add(k, new StoneAndScore(new Stone(j, i), evalOffense));
                                    break;
                                }
                            }
                            if (availableDefenseOffense.size() > 2) {
                                availableDefenseOffense.remove(2);
                            }
                        }
                    }
                }
            }
            for (StoneAndScore stone2 : availableDefenseDefense) {
                availableMoves.add(new Move(stone1.stone, stone2.stone));
            }
            for (StoneAndScore stone2 : availableDefenseOffense) {
                availableMoves.add(new Move(stone1.stone, stone2.stone));
            }
        }

        // Offensive
        ArrayList<StoneAndScore> availableOffense = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (board[i][j] == EMPTY) {
                    double eval = getEvaluation(board, new Stone(j, i), myColor);
                    if (availableOffense.size() == 0) {
                        availableOffense.add(new StoneAndScore(new Stone(j, i), eval));
                    } else {
                        for (int k = 0; k < availableOffense.size(); k++) {
                            if (eval > availableOffense.get(k).score) {
                                availableOffense.add(k, new StoneAndScore(new Stone(j, i), eval));
                                if (availableOffense.size() > 2) {
                                    availableOffense.remove(2);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        for (StoneAndScore stone1 : availableOffense) {
            ArrayList<StoneAndScore> availableOffenseDefense = new ArrayList<>();
            ArrayList<StoneAndScore> availableOffenseOffense = new ArrayList<>();
            int[][] boardCopy = new int[19][19];
            for (int i = 0; i < 19; i++) {
                System.arraycopy(board[i], 0, boardCopy[i], 0, 19);
            }
            boardCopy[stone1.stone.y][stone1.stone.x] = myColor;
            for (int i = 0; i < 19; i++) {
                for (int j = 0; j < 19; j++) {
                    if (boardCopy[i][j] == EMPTY) {
                        // Offense Defense
                        double evalDefense = getEvaluation(boardCopy, new Stone(j, i), enemyColor);
                        if (availableOffenseDefense.size() == 0) {
                            availableOffenseDefense.add(new StoneAndScore(new Stone(j, i), evalDefense));
                        } else {
                            for (int k = 0; k < availableOffenseDefense.size(); k++) {
                                if (evalDefense > availableOffenseDefense.get(k).score) {
                                    availableOffenseDefense.add(k, new StoneAndScore(new Stone(j, i), evalDefense));
                                    break;
                                }
                            }
                            if (availableOffenseDefense.size() > 2) {
                                availableOffenseDefense.remove(2);
                            }
                        }
                        // Offense Offense
                        double evalOffense = getEvaluation(boardCopy, new Stone(j, i), myColor);
                        if (availableOffenseOffense.size() == 0) {
                            availableOffenseOffense.add(new StoneAndScore(new Stone(j, i), evalOffense));
                        } else {
                            for (int k = 0; k < availableOffenseOffense.size(); k++) {
                                if (evalDefense > availableOffenseOffense.get(k).score) {
                                    availableOffenseOffense.add(k, new StoneAndScore(new Stone(j, i), evalOffense));
                                    break;
                                }
                            }
                            if (availableOffenseOffense.size() > 2) {
                                availableOffenseOffense.remove(2);
                            }
                        }
                    }
                }
            }
            for (StoneAndScore stone2 : availableOffenseDefense) {
                availableMoves.add(new Move(stone1.stone, stone2.stone));
            }
            for (StoneAndScore stone2 : availableOffenseOffense) {
                availableMoves.add(new Move(stone1.stone, stone2.stone));
            }
        }

        if (availableMoves.size() == 0) {
            availableMoves.add(new Move(availableDefense.get(0).stone));
        }

        return availableMoves;
    }

    private int isGameOver(int[][] board) {
        BoardBitsConverter boardBitsConverter = new BoardBitsConverter(board);
        boardBits = boardBitsConverter.getBoardBits();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 19; j++) {
                while (boardBits[i][j] != RED) {
                    if ((boardBits[i][j] & 0B111111111111) == 0B010101010101) {
                        return BLACK;
                    }
                    if ((boardBits[i][j] & 0B111111111111) == 0B101010101010) {
                        return WHITE;
                    }
                    boardBits[i][j] >>= 2;
                }
            }
        }
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (board[i][j] == EMPTY) {
                    return NOT_OVER;
                }
            }
        }
        return DRAW;
    }

    private double boardEvaluation(int[][] board) {
        double score = 0;
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (board[i][j] == myColor) {
                    score += getEvaluation(board, new Stone(j, i), myColor);
                } else if (board[i][j] == enemyColor) {
                    score -= getEvaluation(board, new Stone(j, i), enemyColor);
                }
            }
        }
        return score;
    }

    // the minimax algorithm
    public double minimax(int[][] board, int depth, int turn) {

        int gameResult = isGameOver(board);
        if (gameResult == myColor) {
            return Double.MAX_VALUE;
        } else if (gameResult == enemyColor) {
//            for (int i = 0; i < 19; i++) {
//                for (int j = 0; j < 19; j++) {
//                    System.out.print(board[i][j] + " ");
//                }
//                System.out.println();
//            }
//            System.out.println();
            return -Double.MAX_VALUE;
        } else if (gameResult == DRAW) {
            return 0;
        }

        if (depth == 5) {
            return boardEvaluation(board);
        }

        List<Move> gameMovesAvailable = getAvailableMoves(board, turn);
        if (gameMovesAvailable.isEmpty())
            return 0;

        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;

//        int numOfCoresInMyCPU = Runtime.getRuntime().availableProcessors();
//        ExecutorService executor = Executors.newFixedThreadPool(numOfCoresInMyCPU);

        for (Move move : gameMovesAvailable) {
//            int[][] boardCopy = new int[19][19];
//            for (int i = 0; i < 19; i++)
//                System.arraycopy(board[i], 0, boardCopy[i], 0, 19);

            if (turn == myColor) {
//                if (depth == 0) {
//                    for (int i = 0; i < 19; i++) {
//                        for (int j = 0; j < 19; j++) {
//                            System.out.print(boardCopy[i][j] + " ");
//                        }
//                        System.out.println();
//                    }
//                    System.out.println();
//                }
                board[move.stone1.y][move.stone1.x] = myColor;
                if (move.stone2 != null) {
                    board[move.stone2.y][move.stone2.x] = myColor;
                }
//                if (depth == 0) {
//                    for (int i = 0; i < 19; i++) {
//                        for (int j = 0; j < 19; j++) {
//                            System.out.print(boardCopy[i][j] + " ");
//                        }
//                        System.out.println();
//                    }
//                    System.out.println();
//                    System.out.println();
//                }
                double currentScore = minimax(board, depth + 1, enemyColor);
                if (depth == 0) {
//                    System.out.println("max: " + max);
//                    System.out.println("gameResult: " + gameResult);
//                    System.out.println("currentScore: " + currentScore);
                    if (currentScore >= max) {
                        max = currentScore;
                        if (nextMove == null) {
                            nextMove = new Move();
                        }
                        this.nextMove.stone1 = move.stone1;
                        this.nextMove.stone2 = move.stone2;
//                        System.out.println("max: " + max);
//                        System.out.println("nextMove.stone1: " + nextMove.stone1.x + " " + nextMove.stone1.y);
//                        System.out.println("nextMove.stone2: " + nextMove.stone2.x + " " + nextMove.stone2.y);
                    }
                }
                max = Math.max(currentScore, max);
            } else if (turn == enemyColor) {
                board[move.stone1.y][move.stone1.x] = enemyColor;
                if (move.stone2 != null) {
                    board[move.stone2.y][move.stone2.x] = enemyColor;
                }
                double currentScore = minimax(board, depth + 1, myColor);

                min = Math.min(currentScore, min);
            }
            board[move.stone1.y][move.stone2.x] = EMPTY;
            if (move.stone2 != null) {
                board[move.stone2.y][move.stone2.x] = EMPTY;
            }
        }
        return (turn == myColor) ? max : min;
    }

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