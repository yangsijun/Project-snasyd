import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    public final static int DRAW = 3;
    public final static int NOT_OVER = -1;
    public final static int MAX_DEPTH = 4;
    public final static int MAX_BREATH = 2;
    public final static int MAX_DEFENSE = 4;
    public final static int MAX_OFFENSE = 6;

    private int[][] board;
    BoardBitsConverter boardBitsConverter;
    private long[][] boardBits;

    Stone[] lastDrawStones;

    private Move nextMove;

    private List<Move> availableMoves;
    int myColor;
    int enemyColor;

    public static class Stone {
        public int x;
        public int y;

        Stone(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Move {
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
        boardBitsConverter = new BoardBitsConverter(board);
        boardBits = boardBitsConverter.getBoardBits();

//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 19; j++) {
//                System.out.println(Long.toBinaryString(boardBits[i][j]));
//            }
//            System.out.println();
//        }
//        for (int i = 2; i < 4; i++) {
//            for (int j = 0; j < 37; j++) {
//                System.out.println(Long.toBinaryString(boardBits[i][j]));
//            }
//            System.out.println();
//        }
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
//        BoardBitsConverter boardBitsConverter = new BoardBitsConverter(board);
//        boardBits = boardBitsConverter.getBoardBits();
//        ArrayList<Stone> nextDrawStones;
        String nextDrawString;

//        nextDrawStones = findMyWinningShot();

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

//        int numOfCoresInMyCPU = Runtime.getRuntime().availableProcessors();
//        ExecutorService executor = Executors.newFixedThreadPool(numOfCoresInMyCPU);

        BigDecimal a = minimax(board, 0, myColor, BigDecimal.valueOf(-Double.MAX_VALUE), BigDecimal.valueOf(Double.MAX_VALUE));
        System.out.println("a: " + a);

        System.out.println("nextDrawStones: " + nextMove.stone1.x + " " + nextMove.stone1.y);
        if (nextMove.stone2 != null) {
            System.out.println("nextDrawStones: " + nextMove.stone2.x + " " + nextMove.stone2.y);
        }

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
        BigDecimal maxScore = new BigDecimal("0");
        Stone maxScoreStone = null;

        for (Stone stone : availableDefense) {
            BigDecimal eval = getEvaluation(board, stone, enemyColor);
            if (eval.compareTo(maxScore) > 0) {
                maxScore = eval;
                maxScoreStone = stone;
            }
        }

        bestDefense.add(maxScoreStone);
        availableDefense.remove(maxScoreStone);
        board[Objects.requireNonNull(maxScoreStone).y][maxScoreStone.x] = myColor;

        maxScore = new BigDecimal("0");
        maxScoreStone = null;
        for (Stone stone : availableDefense) {
            BigDecimal eval = getEvaluation(board, stone, enemyColor);
            if (eval.compareTo(maxScore) > 0) {
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

    public List<Move> getAvailableMoves(int[][] board, int color, int depth) {
        int myColor = color;
        int enemyColor = (color == BLACK) ? WHITE : BLACK;
        class StoneAndScore {
            Stone stone;
            BigDecimal score;

            public StoneAndScore(StoneAndScore stoneAndScore) {
                this.stone = stoneAndScore.stone;
                this.score = stoneAndScore.score;
            }
            public StoneAndScore(Stone stone, BigDecimal score) {
                this.stone = stone;
                this.score = score;
            }
        }

        List<Move> availableMoves = new ArrayList<>();

        ArrayList<StoneAndScore> availableStonesDefense = new ArrayList<>();
        ArrayList<StoneAndScore> availableStonesOffense = new ArrayList<>();
        for (int i = 0; i < 19; i ++) {
            for (int j = 0; j < 19; j ++) {
                if (board[i][j] == EMPTY) {
                    BigDecimal evalDefense = getEvaluation(board, new Stone(j, i), enemyColor);
                    BigDecimal evalOffense = getEvaluation(board, new Stone(j, i), myColor);
                    if (availableStonesDefense.size() == 0) {
                        availableStonesDefense.add(new StoneAndScore(new Stone(j, i), evalDefense));
                    } else {
                        if (availableStonesDefense.size() < MAX_DEFENSE) {
                            for (int k = 0; k <= availableStonesDefense.size(); k++) {
                                if (k == availableStonesDefense.size()) {
                                    availableStonesDefense.add(new StoneAndScore(new Stone(j, i), evalDefense));
                                    break;
                                }
                                if (evalDefense.compareTo(availableStonesDefense.get(k).score) > 0) {
                                    availableStonesDefense.add(k, new StoneAndScore(new Stone(j, i), evalDefense));
                                    break;
                                }
                            }
                        } else {
                            for (int k = 0; k <= availableStonesDefense.size(); k++) {
                                if (k == availableStonesDefense.size()) {
                                    break;
                                }
                                if (evalDefense.compareTo(availableStonesDefense.get(k).score) > 0) {
                                    availableStonesDefense.add(k, new StoneAndScore(new Stone(j, i), evalDefense));
                                    availableStonesDefense.remove(availableStonesDefense.size() - 1);
                                    break;
                                }
                            }
                         }
                    }

                    if (availableStonesOffense.size() == 0) {
                        availableStonesOffense.add(new StoneAndScore(new Stone(j, i), evalOffense));
                    } else {
                        if (availableStonesOffense.size() < MAX_OFFENSE) {
                            for (int k = 0; k <= availableStonesOffense.size(); k++) {
                                if (k == availableStonesOffense.size()) {
                                    availableStonesOffense.add(new StoneAndScore(new Stone(j, i), evalOffense));
                                    break;
                                }
                                if (evalOffense.compareTo(availableStonesOffense.get(k).score) > 0) {
                                    availableStonesOffense.add(k, new StoneAndScore(new Stone(j, i), evalOffense));
                                    break;
                                }
                            }
                        } else {
                            for (int k = 0; k <= availableStonesOffense.size(); k++) {
                                if (k == availableStonesOffense.size()) {
                                    break;
                                }
                                if (evalOffense.compareTo(availableStonesOffense.get(k).score) > 0) {
                                    availableStonesOffense.add(k, new StoneAndScore(new Stone(j, i), evalOffense));
                                    availableStonesOffense.remove(availableStonesOffense.size() - 1);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
//        ArrayList<StoneAndScore> availableStones = new ArrayList<>();
//        availableStones.addAll(availableStonesDefense);
//        availableStones.addAll(availableStonesOffense);

//        ArrayList<StoneAndScore> availableStones2 = new ArrayList<>();
//        ArrayList<StoneAndScore> availableStones2_1 = new ArrayList<>();
//        for (StoneAndScore stone1 : availableStones) {
//            board[stone1.stone.y][stone1.stone.x] = myColor;
//            for (int i = 0; i < 19; i++) {
//                for (int j = 0; j < 19; j++) {
//                    if (board[i][j] == EMPTY) {
//                        double eval = getEvaluation(board, new Stone(j, i), myColor);
//                        if (availableStones2.size() == 0) {
//                            availableStones2.add(new StoneAndScore(new Stone(j, i), eval));
//                        } else {
//                            for (int k = 0; k < availableStones2.size() + 1; k++) {
//                                if (k == availableStones2.size()) {
//                                    if (k < MAX_BREATH) {
//                                        availableStones2.add(new StoneAndScore(new Stone(j, i), eval));
//                                    }
//                                    break;
//                                }
//                                if (eval > availableStones2.get(k).score) {
//                                    availableStones2.add(k, new StoneAndScore(new Stone(j, i), eval));
//                                    if (availableStones2.size() > MAX_BREATH) {
//                                        availableStones2.remove(MAX_BREATH);
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//                        double eval_1 = getEvaluation(board, new Stone(j, i), enemyColor);
//                        if (availableStones2_1.size() == 0) {
//                            availableStones2_1.add(new StoneAndScore(new Stone(j, i), eval_1));
//                        } else {
//                            for (int k = 0; k < availableStones2_1.size() + 1; k++) {
//                                if (k == availableStones2_1.size()) {
//                                    if (k < MAX_BREATH) {
//                                        availableStones2_1.add(new StoneAndScore(new Stone(j, i), eval_1));
//                                    }
//                                    break;
//                                }
//                                if (eval_1 > availableStones2_1.get(k).score) {
//                                    availableStones2_1.add(k, new StoneAndScore(new Stone(j, i), eval_1));
//                                    if (availableStones2_1.size() > MAX_BREATH) {
//                                        availableStones2_1.remove(MAX_BREATH);
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            for (StoneAndScore stone2 : availableStones2) {
//                availableMoves.add(new Move(stone1.stone, stone2.stone));
//            }
//            for (StoneAndScore stone2 : availableStones2_1) {
//                availableMoves.add(new Move(stone1.stone, stone2.stone));
//            }
//            board[stone1.stone.y][stone1.stone.x] = EMPTY;
//        }

//        for (int i = 0; i < availableStones.size(); i++) {
//            for (int j = i + 1; j < availableStones.size(); j++) {
//                availableMoves.add(new Move(availableStones.get(i).stone, availableStones.get(j).stone));
//            }
//        }

        // Defense-Defense
        for (int i = 0; i < availableStonesDefense.size(); i++) {
            boardBits = boardBitsConverter.stoneToBoardBits(boardBits, availableStonesDefense.get(i).stone, myColor);
            for (int j = i + 1; j < availableStonesDefense.size(); j++) {
                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, availableStonesDefense.get(j).stone, myColor);
                // Not 5 Stones Pattern
//                if (!checkFiveStonesPattern(boardBits, availableStonesDefense.get(i).stone, myColor, depth) && !checkFiveStonesPattern(boardBits, availableStonesDefense.get(j).stone, myColor, depth)) {
//                    availableMoves.add(new Move(availableStonesDefense.get(i).stone, availableStonesDefense.get(j).stone));
//                }
                availableMoves.add(new Move(availableStonesDefense.get(i).stone, availableStonesDefense.get(j).stone));
                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, availableStonesDefense.get(j).stone, EMPTY);
            }
            boardBits = boardBitsConverter.stoneToBoardBits(boardBits, availableStonesDefense.get(i).stone, EMPTY);
        }
        // Defense-Offense
        for (int i = 0; i < availableStonesDefense.size(); i++) {
            boardBits = boardBitsConverter.stoneToBoardBits(boardBits, availableStonesDefense.get(i).stone, myColor);
            for (int j = 0; j < availableStonesOffense.size(); j++) {
                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, availableStonesOffense.get(j).stone, myColor);
                // Not 5 Stones Pattern
                if (!checkFiveStonesPattern(boardBits, availableStonesDefense.get(i).stone, myColor, depth) && !checkFiveStonesPattern(boardBits, availableStonesOffense.get(j).stone, myColor, depth)) {
                    availableMoves.add(new Move(availableStonesDefense.get(i).stone, availableStonesOffense.get(j).stone));
                }
                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, availableStonesOffense.get(j).stone, EMPTY);
            }
            boardBits = boardBitsConverter.stoneToBoardBits(boardBits, availableStonesDefense.get(i).stone, EMPTY);
        }
        // Offense-Offense
        for (int i = 0; i < availableStonesOffense.size(); i++) {
            boardBits = boardBitsConverter.stoneToBoardBits(boardBits, availableStonesOffense.get(i).stone, myColor);
            for (int j = i + 1; j < availableStonesOffense.size(); j++) {
                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, availableStonesOffense.get(j).stone, myColor);
                // Not 5 Stones Pattern
                if (!checkFiveStonesPattern(boardBits, availableStonesOffense.get(i).stone, myColor, depth) && !checkFiveStonesPattern(boardBits, availableStonesOffense.get(j).stone, myColor, depth)) {
                    availableMoves.add(new Move(availableStonesOffense.get(i).stone, availableStonesOffense.get(j).stone));
                }
                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, availableStonesOffense.get(j).stone, EMPTY);
            }
            boardBits = boardBitsConverter.stoneToBoardBits(boardBits, availableStonesOffense.get(i).stone, EMPTY);
        }

        if (depth == 0) {
            System.out.println("Available Moves");
            for (Move move : availableMoves) {
                System.out.println(move.stone1.x + " " + move.stone1.y + " " + move.stone2.x + " " + move.stone2.y);
            }
        }

        return availableMoves;
    }

    private int isGameOver(int[][] board) {
//        System.out.println("A");
        long[][] boardBitsCopy = new long[4][37];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 19; j++) {
                boardBitsCopy[i][j] = this.boardBits[i][j];
//                System.out.println(Long.toBinaryString(boardBitsCopy[i][j]));
            }
//            System.out.println();
        }
        for (int i = 2; i < 4; i++) {
            for (int j = 0; j < 37; j++) {
                boardBitsCopy[i][j] = this.boardBits[i][j];
//                System.out.println(Long.toBinaryString(boardBitsCopy[i][j]));
            }
//            System.out.println();
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 19; j++) {
//                System.out.println(Long.toBinaryString(boardBitsCopy[i][j]));
                while (boardBitsCopy[i][j] != RED) {
                    if ((boardBitsCopy[i][j] & 0B111111111111L) == 0B010101010101L) {
                        return BLACK;
                    }
                    if ((boardBitsCopy[i][j] & 0B111111111111L) == 0B101010101010L) {
                        return WHITE;
                    }
                    boardBitsCopy[i][j] >>= 2;
                }
            }
        }
        for (int i = 2; i < 4; i++) {
            for (int j = 0; j < 37; j++) {
//                System.out.println(Long.toBinaryString(boardBitsCopy[i][j]));
                while (boardBitsCopy[i][j] != RED) {
                    if ((boardBitsCopy[i][j] & 0B111111111111L) == 0B010101010101L) {
                        return BLACK;
                    }
                    if ((boardBitsCopy[i][j] & 0B111111111111L) == 0B101010101010L) {
                        return WHITE;
                    }
                    boardBitsCopy[i][j] >>= 2;
                }
            }
        }

        //
//        System.out.println("B");
//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 19; j++) {
//                System.out.println(Long.toBinaryString(boardBits[i][j]));
//            }
//            System.out.println();
//        }
//        for (int i = 2; i < 4; i++) {
//            for (int j = 0; j < 37; j++) {
//                System.out.println(Long.toBinaryString(boardBits[i][j]));
//            }
//            System.out.println();
//        }

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (board[i][j] == EMPTY) {
                    return NOT_OVER;
                }
            }
        }
        return DRAW;
    }

    private BigDecimal boardEvaluation(int[][] board) {
        int gameResult = isGameOver(board);

        if (gameResult == myColor) {
            return BigDecimal.valueOf(Double.MAX_VALUE);
        } else if (gameResult == enemyColor) {
            return BigDecimal.valueOf(-Double.MAX_VALUE);
        } else if (gameResult == DRAW) {
            return BigDecimal.ZERO;
        }

        BigDecimal score = new BigDecimal("0");
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (board[i][j] == myColor) {
                    score = score.add(getEvaluation(board, new Stone(j, i), myColor));
                } else if (board[i][j] == enemyColor) {
                    score = score.subtract(getEvaluation(board, new Stone(j, i), enemyColor));
                }
            }
        }
        return score;
    }

    // the minimax algorithm
    public BigDecimal minimax(int[][] board, int depth, int turn, BigDecimal alpha, BigDecimal beta) {
        int gameResult = isGameOver(board);

        if (gameResult == myColor) {
            return BigDecimal.valueOf(Double.MAX_VALUE);
        } else if (gameResult == enemyColor) {
            return BigDecimal.valueOf(-Double.MAX_VALUE);
        } else if (gameResult == DRAW) {
            return new BigDecimal("0");
        }

        if (depth == MAX_DEPTH) {
            return boardEvaluation(board);
        }

        // Find Winning Shot
        ThreatDetector threatDetector;
        ArrayList<ThreatDetector.Threat> threats;
        ArrayList<Stone> winningShot = new ArrayList<>();
        int[] checkOrder = {3, 4, 2, 5, 1, 6, 0};
        if (turn == myColor) {
            threatDetector = new ThreatDetector(boardBits, myColor, enemyColor);
            threats = threatDetector.findCriticalThreat(boardBits, myColor);
        } else {
            threatDetector = new ThreatDetector(boardBits, enemyColor, myColor);
            threats = threatDetector.findCriticalThreat(boardBits, enemyColor);
        }

        // found my critical threat
        if (threats != null) {
            ThreatDetector.Threat threat = threats.get(0);
            switch (threat.dir) {
                case HORIZONTAL -> {
                    int startX = threat.stoneNum;
                    int Y = threat.lineNum;

                    for (int i = 0; i < 7; i++) {
                        if (startX + checkOrder[i] >= 0 && startX + checkOrder[i] < 19) {
                            if (board[Y][startX + checkOrder[i]] == EMPTY) {
                                winningShot.add(new Stone(startX + checkOrder[i], Y));
                            }
                        }
                    }
                }
                case VERTICAL -> {
                    int X = threat.lineNum;
                    int startY = threat.stoneNum;

                    for (int i = 0; i < 7; i++) {
                        if (startY + checkOrder[i] >= 0 && startY + checkOrder[i] < 19) {
                            if (board[startY + checkOrder[i]][X] == EMPTY) {
                                winningShot.add(new Stone(X, startY + checkOrder[i]));
                            }
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
                        if (startY + checkOrder[i] >= 0 && startY + checkOrder[i] < 19 && startX + checkOrder[i] >= 0 && startX + checkOrder[i] < 19) {
                            if (board[startY + checkOrder[i]][startX + checkOrder[i]] == EMPTY) {
                                winningShot.add(new Stone(startX + checkOrder[i], startY + checkOrder[i]));
                            }
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
                        if (startY + checkOrder[i] >= 0 && startY + checkOrder[i] < 19 && startX + checkOrder[i] >= 0 && startX + checkOrder[i] < 19) {
                            if (board[startY + checkOrder[i]][startX - checkOrder[i]] == EMPTY) {
                                winningShot.add(new Stone(startX - checkOrder[i], startY + checkOrder[i]));
                            }
                        }
                    }
                }
            }
//            if (winningShot.size() != 2) {
//                board[winningShot.get(0).y][winningShot.get(0).x] = RED;
//                for (int i = 0; i < 19; i++) {
//                    for (int j = 0; j < 19; j++) {
//                        if (board[i][j] == EMPTY) {
//                            winningShot.add(new Stone(i, j));
//                            break;
//                        }
//                    }
//                    if (winningShot.size() == 2) {
//                        break;
//                    }
//                }
//                board[winningShot.get(0).y][winningShot.get(0).x] = EMPTY;
//            }
        }

        // there isn't critical threat
//        if (threats == null || winningShot.size() != 2) {
        if (threats == null) {
            winningShot = null;
        }

        if (winningShot != null) {
            if (depth == 0) {
                nextMove = new Move();
                this.nextMove.stone1 = winningShot.get(0);
                if (winningShot.size() > 2) {
                    this.nextMove.stone2 = winningShot.get(1);
                } else {
                    board[winningShot.get(0).y][winningShot.get(0).x] = RED;
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            if (board[i][j] == EMPTY) {
                                winningShot.add(new Stone(i, j));
                                break;
                            }
                        }
                        if (winningShot.size() > 2) {
                            this.nextMove.stone2 = winningShot.get(1);
                            break;
                        }
                    }
                    board[winningShot.get(0).y][winningShot.get(0).x] = EMPTY;
                }
                return BigDecimal.valueOf(Double.MAX_VALUE);
            }
            if (turn == myColor) {
                System.out.println(depth + " Find my Winning Shot");
                return BigDecimal.valueOf(Double.MAX_VALUE);
            } else {
                System.out.println(depth + " Find Enemy's Winning Shot");
                return BigDecimal.valueOf(-Double.MAX_VALUE);
            }
        }
//
//        // Find Enemy's Winning Shot
//        int patternNum = 0;
//        if (turn == myColor) {
//            threatDetector = new ThreatDetector(boardBits, enemyColor, myColor);
//            patternNum = threatDetector.findCriticalThreatAndReturnPatternNum(boardBits, enemyColor);
//        } else {
//            threatDetector = new ThreatDetector(boardBits, myColor, enemyColor);
//            patternNum = threatDetector.findCriticalThreatAndReturnPatternNum(boardBits, myColor);
//        }
//        if (patternNum != -1) {
//            if (patternNum < 6) {
//                double maxEval = -Double.MAX_VALUE;
//                for (int i = 0; i < 19; i++) {
//                    for (int j = 0; j < 19; j++) {
//                        if (board[i][j] == EMPTY) {
//                            double eval = getEvaluation(board, new Stone(j, i), (turn == BLACK) ? WHITE : BLACK);
//                            maxEval = Math.max(maxEval, eval);
//
//                        }
//                    }
//                }
//            }
//        }
//
//        // Found Enemy's Critical Threat
//        if (threats != null) {
//
//        }

        List<Move> gameMovesAvailable = getAvailableMoves(board, turn, depth);
        if (gameMovesAvailable.isEmpty()) {
            return new BigDecimal("0");
        }

//        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;

//        int numOfCoresInMyCPU = Runtime.getRuntime().availableProcessors();
//        ExecutorService executor = Executors.newFixedThreadPool(numOfCoresInMyCPU);

//        for (Move move : gameMovesAvailable) {
//            if (depth == 0) {
//                System.out.println("x1: " + move.stone1.x + " y1: " + move.stone1.y + " x2: " + move.stone2.x + " y2: " + move.stone2.y);
//            }
//
//            if (turn == myColor) {
//                board[move.stone1.y][move.stone1.x] = myColor;
//                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone1, myColor);
//                if (move.stone2 != null) {
//                    board[move.stone2.y][move.stone2.x] = myColor;
//                    boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone2, myColor);
//                }
//                double currentScore = minimax(board, depth + 1, enemyColor);
//
//                board[move.stone1.y][move.stone1.x] = EMPTY;
//                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone1, EMPTY);
//                if (move.stone2 != null) {
//                    board[move.stone2.y][move.stone2.x] = EMPTY;
//                    boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone2, EMPTY);
//                }
//                if (depth == 0) {
//                    System.out.println("currentScore: " + currentScore);
//                    if (currentScore >= max) {
//                        max = currentScore;
//                        if (nextMove == null) {
//                            nextMove = new Move();
//                        }
//                        this.nextMove.stone1 = move.stone1;
//                        this.nextMove.stone2 = move.stone2;
//                    }
//                }
//                max = Math.max(currentScore, max);
//            } else if (turn == enemyColor) {
//                board[move.stone1.y][move.stone1.x] = enemyColor;
//                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone1, enemyColor);
//                if (move.stone2 != null) {
//                    board[move.stone2.y][move.stone2.x] = enemyColor;
//                    boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone2, enemyColor);
//                }
//                double currentScore = minimax(board, depth + 1, myColor);
//
//                board[move.stone1.y][move.stone1.x] = EMPTY;
//                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone1, EMPTY);
//                if (move.stone2 != null) {
//                    board[move.stone2.y][move.stone2.x] = EMPTY;
//                    boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone2, EMPTY);
//                }
//
//                min = Math.min(currentScore, min);
//            }
//        }
        if (turn == myColor) {
            BigDecimal bestScore = BigDecimal.valueOf(-Double.MAX_VALUE);
            for (Move move : gameMovesAvailable) {
                // Execute the move
                board[move.stone1.y][move.stone1.x] = myColor;
                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone1, myColor);
                if (move.stone2 != null) {
                    board[move.stone2.y][move.stone2.x] = myColor;
                    boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone2, myColor);
                }
                // Calculate the score
                BigDecimal currentScore = minimax(board, depth + 1, enemyColor, alpha, beta);
                bestScore = bestScore.max(currentScore);
                // Undo the move
                board[move.stone1.y][move.stone1.x] = EMPTY;
                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone1, EMPTY);
                if (move.stone2 != null) {
                    board[move.stone2.y][move.stone2.x] = EMPTY;
                    boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone2, EMPTY);
                }
                //
                if (depth == 0) {
                    System.out.println("currentScore: " + currentScore);

                    if (nextMove == null) {
                        nextMove = new Move();
                        alpha = currentScore;
                        this.nextMove.stone1 = move.stone1;
                        this.nextMove.stone2 = move.stone2;
                    }
                    if (currentScore.compareTo(alpha) > 0) {
                        alpha = currentScore;
                        this.nextMove.stone1 = move.stone1;
                        this.nextMove.stone2 = move.stone2;
                    }
                }
                // Beta cut-off
                alpha = alpha.max(bestScore);
                if (beta.compareTo(alpha) <= 0) {
                    break;
                }
            }
            return bestScore;
        } else { // Enemy's turn
            BigDecimal bestScore = BigDecimal.valueOf(Double.MAX_VALUE);
            for (Move move : gameMovesAvailable) {
                // Execute the move
                board[move.stone1.y][move.stone1.x] = enemyColor;
                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone1, enemyColor);
                if (move.stone2 != null) {
                    board[move.stone2.y][move.stone2.x] = enemyColor;
                    boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone2, enemyColor);
                }
                // Calculate the score
                BigDecimal currentScore = minimax(board, depth + 1, myColor, alpha, beta);
                bestScore = bestScore.min(currentScore);
                // Undo the move
                board[move.stone1.y][move.stone1.x] = EMPTY;
                boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone1, EMPTY);
                if (move.stone2 != null) {
                    board[move.stone2.y][move.stone2.x] = EMPTY;
                    boardBits = boardBitsConverter.stoneToBoardBits(boardBits, move.stone2, EMPTY);
                }
                // Alpha cut-off
                beta = beta.min(bestScore);
                if (beta.compareTo(alpha) <= 0) {
                    break;
                }
            }
            return bestScore;
        }
    }

    private BigDecimal getEvaluation(int[][] board, Stone stone, int myColor) {
        int enemyColor = (myColor == BLACK) ? WHITE : BLACK;
        BigDecimal emptyWeight = new BigDecimal("2");

        BigDecimal eval = new BigDecimal("0");
        BigDecimal[] weight = {
                new BigDecimal("2").pow(12),
                new BigDecimal("2").pow(11),
                new BigDecimal("2").pow(10),
                new BigDecimal("2").pow(9),
                new BigDecimal("2").pow(8),
        };
        BigDecimal[] dirFreeWeight = {
                new BigDecimal("1.0000000000"),
                new BigDecimal("1.0000000000"),
                new BigDecimal("1.0000181862"),
                new BigDecimal("1.0000363725"),
                new BigDecimal("1.0000726562"),
        };
        int[][][] dirMap = {
                {{1, 0}, {-1, 0}}, // HORIZONTAL
                {{0, 1}, {0, -1}}, // VERTICAL
                {{1, 1}, {-1, -1}}, // DIA_LTRB
                {{1, -1}, {-1, 1}} // DIA_LBRT
        };

        int freeDir = 4;
        for (int i = 0; i < 4; i++) { // dir
            boolean isFreeDir = true;
            BigDecimal evalDir = new BigDecimal("1");
            int myStonesCount = 0;
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
                            evalDir = evalDir.multiply(emptyWeight);
                        } else if (checkingStone == myColor) {
                            myStonesCount++;
                            if (myStonesCount < 5) {
                                evalDir = evalDir.multiply(weight[k]);
                            } else if (myStonesCount == 6){
                                evalDir = evalDir.multiply(weight[k]);
                                evalDir = evalDir.multiply(weight[k]);
                            }
                        }
                    }
                }
            }
            eval = eval.add(evalDir);
            if (isFreeDir == false) {
                freeDir--;
            }
        }
        eval = eval.multiply(dirFreeWeight[freeDir]);
        return eval;
    }

    boolean checkFiveStonesPattern(long[][] boardBits, Stone stone, int myColor, int depth) {
        long[][] fiveStonesPattern = {
                {

                },
                {
                    0B000101010101L, // 011111
                    0B010001010101L, // 101111
                    0B010100010101L, // 110111
                    0B010101000101L, // 111011
                    0B010101010001L, // 111101
                    0B010101010100L, // 111110
                    0B100101010101L, // 211111
                    0B010101010110L, // 111112
                    0B110101010101L, // 311111
                    0B010101010111L, // 111113
                },
                {
                    0B001010101010L, // 022222
                    0B100010101010L, // 202222
                    0B101000101010L, // 220222
                    0B101010001010L, // 222022
                    0B101010100010L, // 222202
                    0B101010101000L, // 222220
                    0B011010101010L, // 122222
                    0B101010101001L, // 222221
                    0B111010101010L, // 322222
                    0B101010101011L, // 222223
                },
        };
        long[] sixStonesPattern = {
                0,
                0B010101010101L, // 111111
                0B101010101010L, // 222222
        };
        int[][] dirMap = {
                {1, 0}, // HORIZONTAL
                {0, 1}, // VERTICAL
                {1, 1}, // DIA_LTRB
                {1, -1}, // DIA_LBRT
        };
        boolean ret = false;

        BoardBitsConverter tempBitsConverter = new BoardBitsConverter(boardBits);

        // Direction
        for (int i = 0; i < 4; i++) {
            for (int j = -5; j <= 0; j++) {
                if (stone.x + j * dirMap[i][0] < 0 || stone.x + j * dirMap[i][0] > 18 || stone.y + j * dirMap[i][1] < 0 || stone.y + j * dirMap[i][1] > 18) {
                    continue;
                }
                long sixStonesBits = tempBitsConverter.getSixStonesBits(boardBits, new Stone(stone.x + j * dirMap[i][0], stone.y + j * dirMap[i][1]), i);
                for (long pattern : fiveStonesPattern[myColor]) {
                    if ((sixStonesBits & 0B111111111111L) == (pattern & 0B111111111111L)) {
                        if (depth == 0) {
                            System.out.println("stone: " + stone.x + " " + stone.y + " p: " + Long.toBinaryString(pattern));
                        }
                        ret = true;
                    } else if ((sixStonesBits & 0B111111111111L) == (sixStonesPattern[myColor] & 0B111111111111L)) {
                        ret = false;
                        return ret;
                    }
                }
            }
            // Get the Six-in-row stones
//            long sixStonesBits = tempBitsConverter.getSixStonesBits(boardBits, stone, i);
//            for (long pattern : fiveStonesPattern[myColor]) {
//                if ((sixStonesBits & 0B111111111111L) == (pattern & 0B111111111111L)) {
//                    if (depth == 0) {
//                        System.out.println("stone: " + stone.x + " " + stone.y + " p: " + Long.toBinaryString(pattern));
//                    }
//                    return true;
//                }
////                System.out.println("p: " + Long.toBinaryString(pattern));
//            }
        }

        return ret;
    }
}