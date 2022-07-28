public class BoardBitsConverter {

    private int[][] board;
    public final static int HORIZONTAL = 0;
    public final static int VERTICAL = 1;
    public final static int DIA_LTRB = 2;
    public final static int DIA_RTLB = 3;

    BoardBitsConverter(int[][] board) {
        this.board = board;
    }

    public long[][] getBoardBits() {
        long[][] boardBits = new long[4][37];

        // head of line
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 19; j++) {
                boardBits[i][j] = 0B11;
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
                boardBits[DIA_LTRB][j - 1 + 18] <<= 2;
                boardBits[DIA_LTRB][j - i + 18] |= board[i][j];
                // Diagonal right top to left bottom
                boardBits[DIA_RTLB][i + j] <<= 2;
                boardBits[DIA_RTLB][i + j] |= board[i][j];
            }
        }

        // end of line
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 19; j++) {
                boardBits[i][j] <<= 2;
                boardBits[i][j] |= 0B11;
            }
        }

        return boardBits;
    }
}
