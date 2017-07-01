import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Joseph Meltzer on 30/04/2017.
 * State of, and behaviour for, the game.
 */
public class BrickPop {
    int[][] grid;
    Set<ColourSet> regions;
    int score;

    BrickPop() {
        grid = new int[10][10];
        regions = new HashSet<>();
    }

    private boolean coordInAnyRegion(Coord c) {
        for (ColourSet r : regions) {
            if (r.contains(c)) return true;
        }
        return false;
    }

    /**
     * Given any game state, calculate the different regions present
     * and record them in the 'regions' field.
     */
    public void setUpRegions() {
        regions.clear();
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                if (!coordInAnyRegion(new Coord(i,j))) {
                    regions.add(new ColourSet(new Coord(i,j), grid));
                }
            }
        }
    }

    /**
     * Removes a region from the grid, as if it was clicked on.
     * @param c     Coordinates of any square in the region to be removed.
     */
     void empty(Coord c) {
        if (grid[c.x][c.y] == 0) {
            return;
        }
        for (ColourSet r : regions) {
            if (r.contains(c) && r.size()>1) {
                for (Coord d : r) {
                    grid[d.x][d.y] = 0;
                }
                score += (r.size())*(r.size()-1);
                return;
            }
        }
     }

    /**
     * Re-align the tiles in the grid, by having them fall downwards, and then
     * have columns 'fall' to the left.
     */
    void fall() {
        for (int j=0; j<10; j++) {
            List<Integer> column = new ArrayList<>();
            for (int i=0; i<10; i++) {
                if (grid[i][j]>0) column.add(grid[i][j]);
            }
            while (column.size()<10) column.add(0);
            for (int i=0; i<10; i++) {
                grid[i][j] = column.get(i);
            }
        }

        List<List<Integer>> columns = new ArrayList<>();
        for (int j=0; j<10; j++) {
            List<Integer> column = new ArrayList<>();
            boolean columnEmpty = true;
            for (int i=0; i<10; i++) {
                if (grid[i][j]>0) columnEmpty = false;
                column.add(grid[i][j]);
            }
            if (!columnEmpty) columns.add(column);
        }
        if (columns.size()<10) {
            List<Integer> zero = new ArrayList<>();
            for (int i = 0; i < 10; i++) zero.add(0);
            while (columns.size()<10) {
                columns.add(zero);
            }
        }

        for (int j=0; j<10; j++) {
            for (int i=0; i<10; i++) {
                grid[i][j] = columns.get(j).get(i);
            }
        }
    }

    /**
     * Clicks a square. Empties the region it's in, and then re-aligns the grid.
     */
    private void click(Coord c) {
        empty(c);
        fall();
    }

    /**
     * Returns a new game state representing the one that occurs after a tile is clicked on the current one.
     * @param c     Coordinates of the square to be clicked.
     * @return      The prospective new game state.
     */
    public BrickPop successor(Coord c) {
        BrickPop bp = new BrickPop();
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                bp.grid[i][j] = grid[i][j];
            }
        }
        bp.score = score;
        bp.regions = new HashSet<>();
        bp.setUpRegions();
        bp.click(c);
        return bp;
    }

    /**
     * Checks if the round is over and successful (can continue to the next round).
     */
    public boolean finished() {
        return (grid[0][0]==0);
    }

    /**
     * Checks if the game is over (round finished but cannot continue to next round).
     * Happens when there are unclickable tiles remaining at the end of the round.
     */
    public boolean failed() {
        for (int n=1; n<=4; n++) {
            if (failedColour(n)) return true;
        }
        return false;
    }

    /**
     * Tests if a given colour has 'failed': if it exists on the grid but only as isolated single tiles.
     * @param c     The colour to test.
     */
    private boolean failedColour(int c) {
        int count = 0;
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                if (grid[i][j]==c && count>=1) return false;
                if (grid[i][j]==c && count==0) count = 1;
            }
        }
        return count==1;
    }

    /**
     * Testing of this class.
     */
    public static void main(String[] args) {
        BrickPop bp = new BrickPop();
        String input = "4 1 5 2 2 2 4 6 5 5\n5 4 5 2 2 2 6 6 5 5\n3 5 5 1 2 2 6 6 3 4\n3 3 2 1 2 6 2 2 4 4\n1 6 6 1 5 2 4 4 3 4\n1 5 5 1 5 4 1 4 3 3\n4 3 2 5 5 4 3 1 3 3\n4 4 3 2 6 1 3 3 1 1\n4 4 4 2 6 1 3 3 6 6\n4 4 3 2 6 1 1 1 1 1";
        input = "5 6 1 4 1 1 1 4 5 5\n6 6 6 6 1 1 5 4 4 5\n6 2 1 1 2 5 4 3 3 5\n6 2 4 1 2 2 4 2 3 2\n2 5 4 2 2 2 3 2 1 5\n2 3 4 2 1 4 3 2 2 1\n2 3 4 2 5 4 3 1 1 1\n2 3 2 5 4 4 6 3 1 1\n3 1 1 5 6 6 6 3 3 1\n3 1 1 1 1 5 6 6 6 5";

        String[] rows = input.split("\n");
        String[][] sgrid = new String[10][10];
        for (int i=0; i<10; i++) {
            sgrid[i] = rows[i].split(" ");
        }

        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                bp.grid[i][j] = Integer.parseInt(sgrid[9-i][j]);
            }
        }
        bp.setUpRegions();
        for (int i=10; i>=1; i--) {
            for (int j=1; j<=10; j++) {
                System.out.print(bp.grid[i-1][j-1]+" ");
            }
            System.out.println("");
        }
        System.out.println("");
        Coord [] ca = new Coord[29];
        ca[0] = new Coord	(10, 4)	;
        ca[1] = new Coord	(10, 7)	;
        ca[2] = new Coord	(2, 1)	;
        ca[3] = new Coord	(8, 10)	;
        ca[4] = new Coord	(3, 3)	;
        ca[5] = new Coord	(9, 6)	;
        ca[6] = new Coord	(2, 10)	;
        ca[7] = new Coord	(4, 10)	;
        ca[8] = new Coord	(9, 2)	;
        ca[9] = new Coord	(5, 1)	;
        ca[10] =new Coord	(8, 9)	;
        ca[11] =new Coord	(2, 7)	;
        ca[12] =new Coord	(2, 8)	;
        ca[13] =new Coord	(5, 6)	;
        ca[14] =new Coord	(3, 6)	;
        ca[15] =new Coord	(2, 3)	;
        ca[16] =new Coord	(5, 2)	;
        ca[17] =new Coord	(2, 2)	;
        ca[18] =new Coord	(3, 2)	;
        ca[19] =new Coord	(1, 1)	;
        ca[20] =new Coord	(3, 5)	;
        ca[21] =new Coord	(2, 9)	;
        ca[22] =new Coord	(2, 3)	;
        ca[23] =new Coord	(1, 3)	;
        ca[24] =new Coord	(1, 3)	;
        ca[25] =new Coord	(2, 2)	;
        ca[26] =new Coord	(2, 2)	;
        ca[27] =new Coord	(2, 1)	;
        ca[28] =new Coord	(1, 1)	;

        for (Coord c : ca) {
            bp.setUpRegions();
            bp.click(new Coord(c.x-1 , c.y-1));
            System.out.println(bp.failed());
            for (int i=10; i>=1; i--) {
                for (int j=1; j<=10; j++) {
                    System.out.print(bp.grid[i-1][j-1]+" ");
                }
                System.out.println("");
            }
            System.out.println("");
        }
    }

}
