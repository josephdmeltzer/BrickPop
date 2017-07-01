import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph Meltzer on 30/04/2017.
 * Two dimensional coordinate system. In-built method for finding adjacent coordinates.
 */
public class Coord {
    public int x;
    public int y;

    Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public List<Coord> neighbours() {
        List<Coord> list = new ArrayList<>();
        if (x>0) list.add(new Coord(x-1, y));
        if (x<9) list.add(new Coord(x+1, y));
        if (y>0) list.add(new Coord(x, y-1));
        if (y<9) list.add(new Coord(x, y+1));
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Coord) return ((Coord) o).x==x && ((Coord) o).y==y;
        return false;
    }

    @Override
    public String toString() {
        return "("+x+", "+y+")";
    }
}
