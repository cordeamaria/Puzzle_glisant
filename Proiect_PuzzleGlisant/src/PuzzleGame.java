import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PuzzleGame {
    private final int size = 3; // Dimensiunea grilei 3x3
    private int[][] board;
    private int emptyRow, emptyCol; // Poziția celulei goale

    public PuzzleGame() {
        initializeBoard();
    }

    public int getEmptyRow() {
        return emptyRow;
    }

    public int getSize() {
        return size;
    }

    public int getEmptyCol() {
        return emptyCol;
    }
    public int[][] getBoard() {
        return board;
    }

    private void initializeBoard() {
        ArrayList<Integer> pieces = new ArrayList<>();
        for (int i = 0; i < size * size; i++) {
            pieces.add(i);
        }

        boolean isSolvable = false;
        while (!isSolvable) {
            pieces.remove(Integer.valueOf(0));
            Collections.shuffle(pieces); // Amestecăm piesele aleatoriu
            board = new int[size][size];
            int index = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i==size-1 && j==size-1) {
                        emptyRow = i;
                        emptyCol = j;
                        board[i][j]=0;

                    }
                    else {
                        board[i][j] = pieces.get(index++);// pune piesa de index si dupa index ++
                }

            }
        }

        isSolvable = isSolvable(); // Verificăm dacă tabla este rezolvabilă
    }
    }

    public boolean movePiece(int row, int col) {
        if (Math.abs(row - emptyRow) + Math.abs(col - emptyCol) == 1) { // Verificăm dacă este adiacenta
            board[emptyRow][emptyCol] = board[row][col];
            board[row][col] = 0;
            emptyRow = row;
            emptyCol = col;
            return true;
        }
        return false;
    }

    public boolean isSolved() {
        int counter = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == size - 1 && j == size - 1) return board[i][j] == 0; // Ultima celulă goală
                if (board[i][j] != counter++) return false;
            }
        }
        return true;
    }



    public boolean isSolvable() {
        List<Integer> flattened = new ArrayList<>();
        int emptyRow = 0;

        // Aplatizăm tabla într-o listă unidimensională
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                    flattened.add(board[i][j]);
            }
        }

        // Calculăm numărul de inversiuni
        int inversions = 0;
        for (int i = 0; i < flattened.size(); i++) {
            for (int j = i + 1; j < flattened.size(); j++) {
                if (flattened.get(i) > flattened.get(j)) {
                    inversions++;
                }
            }
        }

        // Verificăm regulile de rezolvabilitate

            //  numărul de inversiuni trebuie să fie par
            return inversions % 2 == 0;

    }





    // Modificarea metodei shuffle pentru a folosi initializeBoard
    public void shuffle() {
        initializeBoard(); // Folosim initializeBoard pentru a amesteca piesele
    }
}
