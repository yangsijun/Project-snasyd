import java.util.ArrayList;
import java.util.List;

public class ThreatDetector {
    private long[][] boardBits;
    int myColor;
    int enemyColor;

    final static private int EMPTY = 0;
    final static private int BLACK = 1;
    final static private int WHITE = 2;
    final static private int RED = 3;

    public class Threat {
        public int dir;
        public int lineNum;
        public int stoneNum;

        Threat() {}
        Threat(int dir, int lineNum, int stoneNum) {
            this.dir = dir;
            this.lineNum = lineNum;
            this.stoneNum = stoneNum;
        }
    }
    ThreatDetector(long[][] boardBits, int myColor, int enemyColor) {
        this.boardBits = boardBits;
        this.myColor = myColor;
        this.enemyColor = enemyColor;
    }

//    public ArrayList<Threat> findCriticalThreat(long[][] boardBits, int color) {
//        int enemyColor = (color == BLACK ? WHITE : BLACK);
//        ArrayList<Threat> threats = new ArrayList<>();
//        int x, y, dir;
//        // direction
//        for (int i = 0; i < 2; i++) {
//            dir = i;
//            // line
//            for (int j = 0; j < 19; j++) {
//                x = j;
//                y = 0;
//                // until end of line
//                for (;;) {
//                    int[] count = new int[4];
//                    // six stones in a row
//                    y++;
//                    for (int k = 0; k < 6; k++) {
//                        int stone = (int) (boardBits[i][j] & 0B11);
//                        count[stone]++;
//                        if (stone != EMPTY && stone != color)
//                            break;
//                        boardBits[i][j] >>= 2;
//                    }
//
//                    // if found critical threat
//                    if (count[color] >= 4 && count[enemyColor] == 0 && count[RED] == 0 && count[EMPTY] <= 2)
//                        threats.add(new Threat(dir, x, y));
//
//                    // end of line
//                    if (boardBits[i][j] == RED)
//                        break;
//                    else
//                        boardBits[i][j] >>= 2;
//                }
//            }
//        }
//        for (int i = 2; i < 4; i++) {
//            dir = i;
//            // line
//            for (int j = 0; j < 38; j++) {
//                x = j;
//                y = 0;
//                // until end of line
//                for (;;) {
//                    int[] count = new int[4];
//                    // six stones in a row
//                    y++;
//                    for (int k = 0; k < 6; k++) {
//                        int stone = (int) (boardBits[i][j] & 0B11);
//                        count[stone]++;
//                        if (stone != EMPTY && stone != color)
//                            break;
//                        boardBits[i][j] >>= 2;
//                    }
//
//                    // if found critical threat
//                    if (count[color] >= 4 && count[enemyColor] == 0 && count[RED] == 0 && count[EMPTY] <= 2)
//                        threats.add(new Threat(dir, x, y));
//
//                    // end of line
//                    if (boardBits[i][j] == RED)
//                        break;
//                    else
//                        boardBits[i][j] >>= 2;
//                }
//            }
//        }
//        if (threats.size() == 0)
//            return null;
//        return threats;
//    }

    public ArrayList<Threat> findCriticalThreat(long[][] boardBits, int color) {
        int enemyColor = (color == BLACK ? WHITE : BLACK);
        ArrayList<Threat> threats = new ArrayList<>();
        int dir, lineNum, stoneNum;

        long[][] boardBitsCopy = new long[4][37];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 19; j++) {
                boardBitsCopy[i][j] = boardBits[i][j];
            }
        }
        for (int i = 2; i < 4; i++) {
            for (int j = 0; j < 37; j++) {
                boardBitsCopy[i][j] = boardBits[i][j];
            }
        }

        // Direction
        for (int i = 0; i < 2; i++) {
            dir = i;
            // Line
            for (int j = 0; j < 19; j++) {
                lineNum = j;
                stoneNum = -1;
                // Stone, Until end of line
                for (;;) {
                    long sevenStones = boardBitsCopy[i][j] & 0B11111111111111L;
                    boolean found = false;
                    for (long l : threatPattern[color]) {
                        if (sevenStones == l) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        threats.add(new Threat(dir, lineNum, stoneNum));
                    }
                    // end of line
                    if (boardBitsCopy[i][j] == RED) {
                        break;
                    } else {
                        boardBitsCopy[i][j] >>= 2;
                        stoneNum++;
                    }
                }
            }
        }
        for (int i = 2; i < 4; i++) {
            dir = i;
            // Line
            for (int j = 0; j < 37; j++) {
                lineNum = j;
                stoneNum = -1;
                // Stone, Until end of line
                for (;;) {
                    long sevenStones = boardBitsCopy[i][j] & 0B11111111111111L;
                    boolean found = false;
                    for (long l : threatPattern[color]) {
                        if (sevenStones == l) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        threats.add(new Threat(dir, lineNum, stoneNum));
                    }
                    // end of line
                    if (boardBitsCopy[i][j] == RED) {
                        break;
                    } else {
                        boardBitsCopy[i][j] >>= 2;
                        stoneNum++;
                    }
                }
            }
        }
        if (threats.size() == 0)
            return null;
        return threats;
    }

    public int findCriticalThreatAndReturnPatternNum(long[][] boardBits, int color) {
        int enemyColor = (color == BLACK ? WHITE : BLACK);
        ArrayList<Threat> threats = new ArrayList<>();
        int dir, lineNum, stoneNum;

        long[][] boardBitsCopy = new long[4][37];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 19; j++) {
                boardBitsCopy[i][j] = boardBits[i][j];
            }
        }
        for (int i = 2; i < 4; i++) {
            for (int j = 0; j < 37; j++) {
                boardBitsCopy[i][j] = boardBits[i][j];
            }
        }

        // Direction
        for (int i = 0; i < 2; i++) {
            dir = i;
            // Line
            for (int j = 0; j < 19; j++) {
                lineNum = j;
                stoneNum = -1;
                // Stone, Until end of line
                for (;;) {
                    long sevenStones = boardBitsCopy[i][j] & 0B11111111111111L;
                    int found = -1;
                    for (int k = 0; k < threatPattern[color].length; k++) {
                        if (sevenStones == threatPattern[color][k]) {
                            found = k;
                            break;
                        }
                    }
                    if (found != -1) {
                        return found;
                    }
                    // end of line
                    if (boardBitsCopy[i][j] == RED) {
                        break;
                    } else {
                        boardBitsCopy[i][j] >>= 2;
                        stoneNum++;
                    }
                }
            }
        }
        for (int i = 2; i < 4; i++) {
            dir = i;
            // Line
            for (int j = 0; j < 37; j++) {
                lineNum = j;
                stoneNum = -1;
                // Stone, Until end of line
                for (;;) {
                    long sevenStones = boardBitsCopy[i][j] & 0B11111111111111L;
                    int found = -1;
                    for (int k = 0; k < threatPattern[color].length; k++) {
                        if (sevenStones == threatPattern[color][k]) {
                            found = k;
                            break;
                        }
                    }
                    if (found != -1) {
                        return found;
                    }
                    // end of line
                    if (boardBitsCopy[i][j] == RED) {
                        break;
                    } else {
                        boardBitsCopy[i][j] >>= 2;
                        stoneNum++;
                    }
                }
            }
        }
        return -1;
    }

    final static private long[][] threatPattern = {
            {

            },
            {
                // 2 Threat
                0B00010101010100L, // 0111110
                0B00010101010001L, // 0111101
                0B00010101000101L, // 0111011
                0B00010101010000L, // 0111100

                0B00010101010100L, // 0111110
                0B01000101010100L, // 1011110
                0B01010001010100L, // 1101110
                0B00000101010100L, // 0011110

                // 1 Threat
                0B00010101000100L, // 0111010
                0B00010101000001L, // 0111001
                0B00010100010100L, // 0110110
                0B00010100010001L, // 0110101
                0B00010100000101L, // 0110011

                0B00010101010100L, // 0101110
                0B01000001010100L, // 1001110
                0B00010100010100L, // 0110110
                0B01000100010100L, // 1010110
                0B01010000010100L, // 1100110

                0B10010101010100L, // 2111110
                0B10010101010001L, // 2111101
                0B10010101000101L, // 2111011
                0B10010101010000L, // 2111100
                0B10010101000100L, // 2111010
                0B10010101000001L, // 2111001
                0B10010100010100L, // 2110110
                0B10010100010001L, // 2110101
                0B10010100000101L, // 2110011

                0B00010101010110L, // 0111112
                0B01000101010110L, // 1011112
                0B01010001010110L, // 1101112
                0B00000101010110L, // 0011112
                0B00010001010110L, // 0101112
                0B01000001010110L, // 1001112
                0B00010100010110L, // 0110112
                0B01000100010110L, // 1010112
                0B01010000010110L, // 1100112

                0B11010101010100L, // 3111110
                0B11010101010001L, // 3111101
                0B11010101000101L, // 3111011
                0B11010101010000L, // 3111100
                0B11010101000100L, // 3111010
                0B11010101000001L, // 3111001
                0B11010100010100L, // 3110110
                0B11010100010001L, // 3110101
                0B11010100000101L, // 3110011

                0B00010101010111L, // 0111113
                0B01000101010111L, // 1011113
                0B01010001010111L, // 1101113
                0B00000101010111L, // 0011113
                0B00010001010111L, // 0101113
                0B01000001010111L, // 1001113
                0B00010100010111L, // 0110113
                0B01000100010111L, // 1010113
                0B01010000010111L, // 1100113
            },
            {
                // 2 Threat
                0B00101010101000L, // 0111110
                0B00101010100010L, // 0111101
                0B00101010001010L, // 0111011
                0B00101010100000L, // 0111100

                0B00101010101000L, // 0111110
                0B10001010101000L, // 1011110
                0B10100010101000L, // 1101110
                0B00001010101000L, // 0011110

                // 1 Threat
                0B00101010001000L, // 0111010
                0B00101010000010L, // 0111001
                0B00101000101000L, // 0110110
                0B00101000100010L, // 0110101
                0B00101000001010L, // 0110011

                0B00101010101000L, // 0101110
                0B10000010101000L, // 1001110
                0B00101000101000L, // 0110110
                0B10001000101000L, // 1010110
                0B10100000101000L, // 1100110

                0B01101010101000L, // 2111110
                0B01101010100010L, // 2111101
                0B01101010001010L, // 2111011
                0B01101010100000L, // 2111100
                0B01101010001000L, // 2111010
                0B01101010000010L, // 2111001
                0B01101000101000L, // 2110110
                0B01101000100010L, // 2110101
                0B01101000001010L, // 2110011

                0B00101010101001L, // 0111112
                0B10001010101001L, // 1011112
                0B10100010101001L, // 1101112
                0B00001010101001L, // 0011112
                0B00100010101001L, // 0101112
                0B10000010101001L, // 1001112
                0B00101000101001L, // 0110112
                0B10001000101001L, // 1010112
                0B10100000101001L, // 1100112

                0B11101010101000L, // 3111110
                0B11101010100010L, // 3111101
                0B11101010001010L, // 3111011
                0B11101010100000L, // 3111100
                0B11101010001000L, // 3111010
                0B11101010000010L, // 3111001
                0B11101000101000L, // 3110110
                0B11101000100010L, // 3110101
                0B11101000001010L, // 3110011

                0B00101010101011L, // 0111113
                0B10001010101011L, // 1011113
                0B10100010101011L, // 1101113
                0B00001010101011L, // 0011113
                0B00100010101011L, // 0101113
                0B10000010101011L, // 1001113
                0B00101000101011L, // 0110113
                0B10001000101011L, // 1010113
                0B10100000101011L, // 1100113
            }
    };

}
