import java.util.ArrayList;
import java.util.List;

public class Minimax implements Runnable {
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
    private int depth;
    private int turn;

    private int myColor;
    private int enemyColor;
    private long[][] boardBits;
    private NextDraw.Move nextMove;

    private double score;

    public Minimax(int[][] board, long[][] boardBits, int depth, int turn) {
        this.board = board;
        this.boardBits = boardBits;
        this.depth = depth;
        this.turn = turn;
        myColor = turn;
        enemyColor = (turn == BLACK) ? WHITE : BLACK;
    }

    private int isGameOver() {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 19; j++) {
                while (boardBits[i][j] != RED) {
                    if ((boardBits[i][j] & 0B111111111111L) == 0B010101010101L) {
                        return BLACK;
                    }
                    if ((boardBits[i][j] & 0B111111111111L) == 0B101010101010L) {
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
                    score += getEvaluation(board, new NextDraw.Stone(j, i), myColor);
                } else if (board[i][j] == enemyColor) {
                    score -= getEvaluation(board, new NextDraw.Stone(j, i), enemyColor);
                }
            }
        }
        return score;
    }

    private double getEvaluation(int[][] board, NextDraw.Stone stone, int myColor) {
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

    public List<NextDraw.Move> getAvailableMoves(int[][] board, int color) {
        int myColor = color;
        int enemyColor = color == BLACK ? WHITE : BLACK;
        class StoneAndScore {
            NextDraw.Stone stone;
            double score;

            public StoneAndScore(StoneAndScore stoneAndScore) {
                this.stone = stoneAndScore.stone;
                this.score = stoneAndScore.score;
            }
            public StoneAndScore(NextDraw.Stone stone, double score) {
                this.stone = stone;
                this.score = score;
            }
        }

        int[][] checkedAvailable = new int[19][19];
        List<NextDraw.Move> availableMoves = new ArrayList<>();

        // find available moves
        // Defensive
        ArrayList<StoneAndScore> availableDefense = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (board[i][j] == EMPTY) {
                    double eval = getEvaluation(board, new NextDraw.Stone(j, i), enemyColor);
                    if (availableDefense.size() == 0) {
                        availableDefense.add(new StoneAndScore(new NextDraw.Stone(j, i), eval));
                    } else {
                        for (int k = 0; k < availableDefense.size(); k++) {
                            if (eval > availableDefense.get(k).score) {
                                availableDefense.add(k, new StoneAndScore(new NextDraw.Stone(j, i), eval));
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
                        double evalDefense = getEvaluation(boardCopy, new NextDraw.Stone(j, i), enemyColor);
                        if (availableDefenseDefense.size() == 0) {
                            availableDefenseDefense.add(new StoneAndScore(new NextDraw.Stone(j, i), evalDefense));
                        } else {
                            for (int k = 0; k < availableDefenseDefense.size(); k++) {
                                if (evalDefense > availableDefenseDefense.get(k).score) {
                                    availableDefenseDefense.add(k, new StoneAndScore(new NextDraw.Stone(j, i), evalDefense));
                                    if (availableDefenseDefense.size() > 2) {
                                        availableDefenseDefense.remove(2);
                                    }
                                    break;
                                }
                            }
                        }
                        // Defense Offense
                        double evalOffense = getEvaluation(boardCopy, new NextDraw.Stone(j, i), myColor);
                        if (availableDefenseOffense.size() == 0) {
                            availableDefenseOffense.add(new StoneAndScore(new NextDraw.Stone(j, i), evalOffense));
                        } else {
                            for (int k = 0; k < availableDefenseOffense.size(); k++) {
                                if (evalDefense > availableDefenseOffense.get(k).score) {
                                    availableDefenseOffense.add(k, new StoneAndScore(new NextDraw.Stone(j, i), evalOffense));
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
                availableMoves.add(new NextDraw.Move(stone1.stone, stone2.stone));
            }
            for (StoneAndScore stone2 : availableDefenseOffense) {
                availableMoves.add(new NextDraw.Move(stone1.stone, stone2.stone));
            }
        }

        // Offensive
        ArrayList<StoneAndScore> availableOffense = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (board[i][j] == EMPTY) {
                    double eval = getEvaluation(board, new NextDraw.Stone(j, i), myColor);
                    if (availableOffense.size() == 0) {
                        availableOffense.add(new StoneAndScore(new NextDraw.Stone(j, i), eval));
                    } else {
                        for (int k = 0; k < availableOffense.size(); k++) {
                            if (eval > availableOffense.get(k).score) {
                                availableOffense.add(k, new StoneAndScore(new NextDraw.Stone(j, i), eval));
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
                        double evalDefense = getEvaluation(boardCopy, new NextDraw.Stone(j, i), enemyColor);
                        if (availableOffenseDefense.size() == 0) {
                            availableOffenseDefense.add(new StoneAndScore(new NextDraw.Stone(j, i), evalDefense));
                        } else {
                            for (int k = 0; k < availableOffenseDefense.size(); k++) {
                                if (evalDefense > availableOffenseDefense.get(k).score) {
                                    availableOffenseDefense.add(k, new StoneAndScore(new NextDraw.Stone(j, i), evalDefense));
                                    break;
                                }
                            }
                            if (availableOffenseDefense.size() > 2) {
                                availableOffenseDefense.remove(2);
                            }
                        }
                        // Offense Offense
                        double evalOffense = getEvaluation(boardCopy, new NextDraw.Stone(j, i), myColor);
                        if (availableOffenseOffense.size() == 0) {
                            availableOffenseOffense.add(new StoneAndScore(new NextDraw.Stone(j, i), evalOffense));
                        } else {
                            for (int k = 0; k < availableOffenseOffense.size(); k++) {
                                if (evalDefense > availableOffenseOffense.get(k).score) {
                                    availableOffenseOffense.add(k, new StoneAndScore(new NextDraw.Stone(j, i), evalOffense));
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
                availableMoves.add(new NextDraw.Move(stone1.stone, stone2.stone));
            }
            for (StoneAndScore stone2 : availableOffenseOffense) {
                availableMoves.add(new NextDraw.Move(stone1.stone, stone2.stone));
            }
        }

        if (availableMoves.size() == 0) {
            availableMoves.add(new NextDraw.Move(availableDefense.get(0).stone));
        }

        return availableMoves;
    }

    public double getScore() {
        return score;
    }

    @Override
    public void run() {
//        int gameResult = isGameOver();
//        if (gameResult == myColor) {
//            score = Double.MAX_VALUE;
//            return;
//        } else if (gameResult == enemyColor) {
////            for (int i = 0; i < 19; i++) {
////                for (int j = 0; j < 19; j++) {
////                    System.out.print(board[i][j] + " ");
////                }
////                System.out.println();
////            }
////            System.out.println();
//            score = -Double.MAX_VALUE;
//            return;
//        } else if (gameResult == DRAW) {
//            score = 0;
//            return;
//        }
//
//        if (depth == 5) {
//            score = boardEvaluation(board);
//            return;
//        }
//
//        List<NextDraw.Move> gameMovesAvailable = getAvailableMoves(board, turn);
//        if (gameMovesAvailable.isEmpty()) {
//            score = 0;
//            return;
//        }
//
//        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;
//
////        int numOfCoresInMyCPU = Runtime.getRuntime().availableProcessors();
////        ExecutorService executor = Executors.newFixedThreadPool(numOfCoresInMyCPU);
//
//        for (NextDraw.Move move : gameMovesAvailable) {
////            int[][] boardCopy = new int[19][19];
////            for (int i = 0; i < 19; i++)
////                System.arraycopy(board[i], 0, boardCopy[i], 0, 19);
//
//            if (turn == myColor) {
////                if (depth == 0) {
////                    for (int i = 0; i < 19; i++) {
////                        for (int j = 0; j < 19; j++) {
////                            System.out.print(boardCopy[i][j] + " ");
////                        }
////                        System.out.println();
////                    }
////                    System.out.println();
////                }
//                board[move.stone1.y][move.stone1.x] = myColor;
//                if (move.stone2 != null) {
//                    board[move.stone2.y][move.stone2.x] = myColor;
//                }
////                if (depth == 0) {
////                    for (int i = 0; i < 19; i++) {
////                        for (int j = 0; j < 19; j++) {
////                            System.out.print(boardCopy[i][j] + " ");
////                        }
////                        System.out.println();
////                    }
////                    System.out.println();
////                    System.out.println();
////                }
//                double currentScore = minimax(board, depth + 1, enemyColor);
//                if (depth == 0) {
////                    System.out.println("max: " + max);
////                    System.out.println("gameResult: " + gameResult);
////                    System.out.println("currentScore: " + currentScore);
//                    if (currentScore >= max) {
//                        max = currentScore;
//                        if (nextMove == null) {
//                            nextMove = new NextDraw.Move();
//                        }
//                        this.nextMove.stone1 = move.stone1;
//                        this.nextMove.stone2 = move.stone2;
////                        System.out.println("max: " + max);
////                        System.out.println("nextMove.stone1: " + nextMove.stone1.x + " " + nextMove.stone1.y);
////                        System.out.println("nextMove.stone2: " + nextMove.stone2.x + " " + nextMove.stone2.y);
//                    }
//                }
//                max = Math.max(currentScore, max);
//            } else if (turn == enemyColor) {
//                board[move.stone1.y][move.stone1.x] = enemyColor;
//                if (move.stone2 != null) {
//                    board[move.stone2.y][move.stone2.x] = enemyColor;
//                }
//                double currentScore = minimax(board, depth + 1, myColor);
//
//                min = Math.min(currentScore, min);
//            }
//            board[move.stone1.y][move.stone2.x] = EMPTY;
//            if (move.stone2 != null) {
//                board[move.stone2.y][move.stone2.x] = EMPTY;
//            }
//        }
//        score = (turn == myColor) ? max : min;
//        return;
    }
}