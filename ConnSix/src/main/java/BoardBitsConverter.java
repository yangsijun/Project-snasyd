public class BoardBitsConverter {

    private int[][] board;
    long[][] boardBits = new long[4][37];
    public final static int HORIZONTAL = 0;
    public final static int VERTICAL = 1;
    public final static int DIA_LTRB = 2;
    public final static int DIA_RTLB = 3;

    BoardBitsConverter(int[][] board) {
        this.board = board;
    }

    BoardBitsConverter(long[][] boardBits) {
        this.boardBits = boardBits;
    }

    public long[][] getBoardBits() {

        // head of line
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 19; j++) {
                boardBits[i][j] = 0B11L;
            }
        }
        for (int i = 2; i < 4; i++) {
            for (int j = 0; j < 37; j++) {
                boardBits[i][j] = 0B11L;
            }
        }

        for (int i = 18; i >= 0; i--) {
            for (int j = 18; j >= 0; j--) {
                // Horizontal
                boardBits[HORIZONTAL][i] <<= 2;
                boardBits[HORIZONTAL][i] |= board[i][j];
                // Vertical
                boardBits[VERTICAL][j] <<= 2;
                boardBits[VERTICAL][j] |= board[i][j];
                // Diagonal left top to right bottom
                boardBits[DIA_LTRB][j - i + 18] <<= 2;
                boardBits[DIA_LTRB][j - i + 18] |= board[i][j];
                // Diagonal right top to left bottom
                boardBits[DIA_RTLB][i + j] <<= 2;
                boardBits[DIA_RTLB][i + j] |= board[i][j];
            }
        }

        // end of line
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 19; j++) {
                boardBits[i][j] <<= 2;
                boardBits[i][j] |= 0B11L;
            }
        }
        for (int i = 2; i < 4; i++) {
            for (int j = 0; j < 37; j++) {
                boardBits[i][j] <<= 2;
                boardBits[i][j] |= 0B11L;
            }
        }

        return boardBits;
    }

    public long[][] stoneToBoardBits(long[][] boardBits, NextDraw.Stone stone, int color) {
        int x = stone.x;
        int y = stone.y;
        this.boardBits = boardBits;

        // Horizontal
        long bin = (long) color << (2 * (x + 1));
        long binRemover = ~(0B11L << (2 * (x + 1)));
        boardBits[HORIZONTAL][y] = (boardBits[HORIZONTAL][y] & binRemover) | bin;

        // Vertical
        bin = (long) color << (2 * (y + 1));
        binRemover = ~(0B11L << (2 * (y + 1)));
        boardBits[VERTICAL][x] = (boardBits[VERTICAL][x] & binRemover) | bin;

        // Diagonal left top to right bottom
        if (x - y + 18 < 19) { // 0~18 Line
            bin = (long) color << (2 * (x + 1));
            binRemover = ~(0B11L << (2 * (x + 1)));
        } else { // 19~36 Line
            bin = (long) color << (2 * (y + 1));
            binRemover = ~(0B11L << (2 * (y + 1)));
        }
        boardBits[DIA_LTRB][x - y + 18] = (boardBits[DIA_LTRB][x - y + 18] & binRemover) | bin;

        // Diagonal right top to left bottom
        if (x + y < 19) { // 0~18 Line
            bin = (long) color << (2 * (y + 1));
            binRemover = ~(0B11L << (2 * (y + 1)));
        } else { // 19~36 Line
            bin = (long) color << (2 * (18 - x + 1));
            binRemover = ~(0B11L << (2 * (18 - x + 1)));
        }
        boardBits[DIA_RTLB][x + y] = (boardBits[DIA_RTLB][x + y] & binRemover) | bin;

        return boardBits;
    }

    public long getSixStonesBits(long[][] boardBits, NextDraw.Stone stone, int dir) {
        int x = stone.x;
        int y = stone.y;
        long bin = 0B0L;

        switch (dir) {
            case HORIZONTAL:
                bin = boardBits[HORIZONTAL][y] >> 2 * (x + 1) & 0B111111111111L;
                break;
            case VERTICAL:
                bin = boardBits[VERTICAL][x] >> 2 * (y + 1) & 0B111111111111L;
                break;
            case DIA_LTRB:
                bin = boardBits[DIA_LTRB][x - y + 18];
                if (x - y + 18 < 19) { // 0~18 Line
                    bin = bin >> 2 * (x + 1) & 0B111111111111L;
                } else { // 19~36 Line
                    bin = bin >> 2 * (y + 1) & 0B111111111111L;
                }
                break;
            case DIA_RTLB:
                bin = boardBits[DIA_RTLB][x + y];
                if (x + y < 19) { // 0~18 Line
                    bin = bin >> 2 * (y + 1) & 0B111111111111L;
                } else { // 19~36 Line
                    bin = bin >> 2 * (18 - x + 1) & 0B111111111111L;
                }
                break;
        }
        return bin;
    }
}
