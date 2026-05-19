import java.util.HashMap;
import java.util.Map;

/**
 * 三目並べ（Tic-Tac-Toe）完全解析プログラム
 * 後退解析（Backward Induction）による全盤面の勝敗判定
 */
public class TicTacToeSolver {
    private static final int SIZE = 3;
    private static final char EMPTY = ' ';
    private static final char PLAYER_X = 'X'; // 先手
    private static final char PLAYER_O = 'O'; // 後手

    // 盤面の状態と評価値を記録するメモ化用マップ
    private Map<String, Integer> memoTable = new HashMap<>();

    public static void main(String[] args) {
        TicTacToeSolver solver = new TicTacToeSolver();
        char[][] initialBoard = {
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY}
        };

        System.out.println("三目並べの完全解析を開始します（バージョン2）...");
        int result = solver.analyzePosition(initialBoard, true);
        
        System.out.println("--- 解析結果 ---");
        if (result == 1) {
            System.out.println("最善を尽くした場合：先手（X）の必勝です。");
        } else if (result == -1) {
            System.out.println("最善を尽くした場合：後手（O）の必勝です。");
        } else {
            System.out.println("最善を尽くした場合：必ず引き分けになります。");
        }
        System.out.println("探索したユニークな盤面数: " + solver.memoTable.size());
    }

    /**
     * 局面を再帰的に探索し、勝敗を判定する（Minimax法）
     */
    public int analyzePosition(char[][] board, boolean isMax) {
        String boardState = boardToString(board);
        if (memoTable.containsKey(boardState)) {
            return memoTable.get(boardState);
        }

        // 終了状態のチェック
        if (checkWin(board, PLAYER_X)) return 1;  // 先手勝ち
        if (checkWin(board, PLAYER_O)) return -1; // 後手勝ち
        if (isBoardFull(board)) return 0;         // 引き分け

        int bestValue = isMax ? -2 : 2;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    // 試しに着手
                    board[i][j] = isMax ? PLAYER_X : PLAYER_O;
                    
                    int value = analyzePosition(board, !isMax);
                    
                    // 評価値の更新
                    if (isMax) {
                        bestValue = Math.max(bestValue, value);
                    } else {
                        bestValue = Math.min(bestValue, value);
                    }
                    
                    // 盤面を元に戻す
                    board[i][j] = EMPTY;
                }
            }
        }

        memoTable.put(boardState, bestValue);
        return bestValue;
    }

    private boolean checkWin(char[][] board, char player) {
        // 行と列のチェック
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        // 斜めのチェック
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;
        
        return false;
    }

    private boolean isBoardFull(char[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) return false;
            }
        }
        return true;
    }

    private String boardToString(char[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sb.append(board[i][j]);
            }
        }
        return sb.toString();
    }
}