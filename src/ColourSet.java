import java.util.HashSet;

/**
 * Created by Joseph Meltzer on 30/04/2017.
 * Definition for the 'ColourSet' class, which represents a region of the board
 * by being a set containing the blocks in that region.
 */
public class ColourSet extends HashSet<Coord> {
    int colour;

    ColourSet(int c) {
        this.colour = c;
    }

    ColourSet(Coord c, int[][] grid) {
        colour = grid[c.x][c.y];
        this.add(c);
        for (Coord d : c.neighbours()) {
            FF(d, grid);
        }
    }

    /**
     * Performs the flood-fill algorithm to find all matching tiles around a given one.
     * @param c     The coordinates of the tile.
     * @param grid  The grid of all tiles.
     */
    private void FF(Coord c, int[][] grid) {
        if (!(grid[c.x][c.y] != colour || this.contains(c))) {
            this.add(c);
            for (Coord d : c.neighbours()) {
                FF(d, grid);
            }
        }
    }

    /**
     * Gets the coordinate of some tile in this region. May as well be random.
     */
    public Coord any() {
        for (Coord c : this) return c;
        return null;
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Coord) {
            for (Coord c : this) {
                if (c.equals(o)) return true;
            }
        }
        return false;
    }

    /**
     * Testing for this class.
     */
    public static void main(String[] args) {
        int[][] grid = new int[10][10];
        for (int i=0; i<10; i++) {
            for (int j=0; j<5; j++) {
                grid[i][j]=1;
            }
        }
        System.out.println(grid[0][0]);
        ColourSet cs = new ColourSet(new Coord(0,0), grid);
        System.out.println(cs.colour);
        for (Coord c : cs) {
            System.out.println("("+c.x+", "+c.y+")");
        }
        for (Coord c : new Coord(0,0).neighbours()) {
            System.out.println("("+c.x+", "+c.y+")");
        }
    }
}
