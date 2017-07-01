import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Joseph Meltzer on 30/04/2017.
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

     void empty(Coord c) {
         //System.out.println("AAA"+grid[1][0]);

         if (grid[c.x][c.y] == 0) {
            //System.out.println("clicked empty, "+c);
            //System.out.println(regions);
            return;
        }
        for (ColourSet r : regions) {
            if (r.contains(c) && r.size()>1) {
                //System.out.println("AAA"+r);
                for (Coord d : r) {
                    grid[d.x][d.y] = 0;
                }
                score += (r.size())*(r.size()-1);
                //System.out.println("AAA"+grid[1][0]);
                return;
            }
        }
    }

     void fall() {
//        int[][] grid2 = new int[10][10];
//        for (int i=0; i<10; i++) {
//            for (int j=0; j<10; j++) {
//                grid2[i][j] = grid[i][j];
//            }
//        }
//
//        for (int i=0; i<10; i++) {
//            List<Integer> column = new ArrayList<>();
//            for (int j = 0; j < 10; j++) {
//                if (grid2[i][j] != 0) column.add(grid2[i][j]);
//            }
//            while (column.size() < 10) {
//                column.add(0);
//            }
//            for (int j = 0; j < 10; j++) {
//                grid2[i][j] = column.get(j);
//            }
//        }
//
//        List<int[]> columns = new ArrayList<>();
//        for (int i=0; i<10; i++) {
//            if (!arrayZero(grid2[i])) {
//                columns.add(grid2[i]);
//            }
//        }
//        while (columns.size() < 10) {
//            columns.add(new int[10]);
//        }
//        for (int i=0; i < 10; i++) {
//            grid2[i] = columns.get(i);
//        }
//
//         for (int i=0; i<10; i++) {
//             for (int j=0; j<10; j++) {
//                 grid[i][j] = grid2[i][j];
//             }
//         }

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


        //System.out.println("BBB"+grid[0][0]);
    }

    private boolean arrayZero(int[] column) {
        for (int i=0; i<10; i++) {
            if (column[i] != 0) return false;
        }
        return true;
    }

    private void click(Coord c) {
        empty(c);
//        System.out.println("");
//        for (int i=10; i>=1; i--) {
//            for (int j=1; j<=10; j++) {
//                System.out.print(grid[i-1][j-1]+" ");
//            }
//            System.out.println("");
//        }
        fall();
    }

    public BrickPop successor(Coord c) {
        BrickPop bp = new BrickPop();
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                bp.grid[i][j] = grid[i][j];
            }
        }
        bp.score = score;
        //System.out.println("score is "+bp.score);
        //bp.setUpRegions();
        bp.regions = new HashSet<>();
//        for (ColourSet s : regions) {
//            ColourSet set = new ColourSet(s.colour);
//            for (Coord d : s) {
//                set.add(new Coord(d.x, d.y));
//            }
//            bp.regions.add(set);
//        }
        bp.setUpRegions();
        bp.click(c);

        //System.out.println("score is now "+bp.score);

        return bp;
    }

    public boolean finished() {
        return (grid[0][0]==0);
        //System.out.println(grid[0][0]);
        //System.out.println(grid[0][1]);
        //System.out.println(grid[1][0]);
        //System.out.println(grid[1][1]);
        //setUpRegions();
        //System.out.println("After"+grid[0][0]);
        //System.out.println(grid[0][1]);
        //System.out.println(grid[1][0]);
        //System.out.println(grid[1][1]);
        //System.out.println(""+regions.size()+regions);
        //return regions.size()==1;
    }

    public boolean failed() {
//        for (ColourSet r : regions) {
//            Coord c = r.any();
//            ////System.out.println(r.size());
//            if (grid[c.x][c.y] != 0 && r.size()>1) return false;
//        }
//        return true;
        for (int n=1; n<=4; n++) {
            if (failedColour(n)) return true;
        }
        return false;
    }

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


    public static void main(String[] args) {
//        BrickPop bp = new BrickPop();
//        bp.grid[0][1] = 1;
//        bp.grid[0][5] = 2;
        //System.out.println(bp.grid[0][0]);
        //System.out.println(bp.grid[0][1]);
        //System.out.println(bp.grid[0][2]);
        //System.out.println(bp.grid[0][5]);
//        bp.fall();
        //System.out.println(bp.grid[0][0]);
        //System.out.println(bp.grid[0][1]);
        //System.out.println(bp.grid[0][2]);
        //System.out.println(bp.grid[0][5]);

//        Set<Set<Coord>> sets = new HashSet<>();
//        Set<Coord> set1 = new HashSet<>();
//        set1.add(new Coord(1,1));
//        set1.add(new Coord(3,5));
//        Set<Coord> set2 = new HashSet<>();
//        set2.add(new Coord(2,4));
//        sets.add(set1);
//        sets.add(set2);
//
//
//        Set<Set<Coord>> clone = new HashSet<>();
//        for (Set<Coord> s : sets) {
//            Set<Coord> set = new HashSet<>();
//            for (Coord c : s) {
//                set.add(new Coord(c.x, c.y));
//            }
//            clone.add(set);
//        }
//
//        System.out.println(clone.contains(set1));

        BrickPop bp = new BrickPop();

        //String input = "1 1 2 4 3 1 1 2 5 5\n1 4 2 3 5 3 1 3 4 5\n5 5 1 3 5 1 3 3 4 5\n2 5 1 3 3 1 2 1 4 5\n2 5 4 5 3 2 3 1 4 3\n1 1 4 2 3 1 4 1 1 3\n5 3 4 2 3 5 4 4 1 5\n2 2 4 2 5 3 1 4 1 4\n2 2 5 2 3 3 5 4 1 1\n5 3 3 5 3 3 3 5 1 4";
        //String input = "0 0 0 0 0 0 0 0 0 0\n0 0 2 0 0 0 0 0 0 0\n1 1 2 0 0 0 0 0 0 0\n1 4 1 0 0 0 0 0 0 0\n5 5 1 0 0 0 1 0 0 0\n2 5 4 0 0 0 1 0 0 0\n2 5 4 0 0 0 3 0 0 0\n1 1 4 0 0 1 2 2 0 0\n5 3 4 0 0 2 3 3 0 0\n5 3 3 4 3 1 1 3 0 0";
//        String input = "6 2 6 2 2 4 1 6 2 1\n3 4 6 2 6 4 6 6 5 1\n3 4 1 6 6 2 6 2 5 5\n3 5 2 1 2 2 5 5 5 5\n6 3 2 1 5 1 4 6 2 5\n2 3 2 1 1 1 4 3 2 5\n3 5 6 5 5 6 4 4 6 5\n2 1 5 5 5 6 3 3 6 1\n4 5 4 4 2 3 3 6 6 1\n4 4 4 4 4 5 3 3 3 1";
        //String input = "4 1 5 6 6 1 5 5 6 1\n1 1 6 6 4 1 6 6 5 1\n1 1 1 6 4 1 6 6 3 1\n4 3 3 6 4 1 6 3 2 1\n3 3 5 3 4 1 3 3 4 1\n5 5 1 6 4 2 5 2 4 1\n4 5 6 6 6 2 2 4 5 1\n5 5 2 3 3 2 2 3 1 1\n5 1 2 6 3 2 3 5 1 4\n1 2 2 2 2 3 5 5 5 4";
        //String input = "1 2 4 1 3 3 3 3 4 6\n1 2 4 6 1 2 3 3 4 6\n1 4 2 6 6 3 2 3 4 2\n1 4 2 1 6 1 4 4 5 2\n5 4 1 1 3 1 2 2 4 2\n5 5 5 3 2 5 3 4 4 2\n5 5 3 5 5 2 3 5 5 2\n1 2 2 2 2 2 2 2 6 2\n4 3 3 2 6 3 1 1 2 2\n4 3 3 6 1 3 2 3 2 2";
        String input = "4 1 5 2 2 2 4 6 5 5\n5 4 5 2 2 2 6 6 5 5\n3 5 5 1 2 2 6 6 3 4\n3 3 2 1 2 6 2 2 4 4\n1 6 6 1 5 2 4 4 3 4\n1 5 5 1 5 4 1 4 3 3\n4 3 2 5 5 4 3 1 3 3\n4 4 3 2 6 1 3 3 1 1\n4 4 4 2 6 1 3 3 6 6\n4 4 3 2 6 1 1 1 1 1";
        input = "5 6 1 4 1 1 1 4 5 5\n6 6 6 6 1 1 5 4 4 5\n6 2 1 1 2 5 4 3 3 5\n6 2 4 1 2 2 4 2 3 2\n2 5 4 2 2 2 3 2 1 5\n2 3 4 2 1 4 3 2 2 1\n2 3 4 2 5 4 3 1 1 1\n2 3 2 5 4 4 6 3 1 1\n3 1 1 5 6 6 6 3 3 1\n3 1 1 1 1 5 6 6 6 5";

        String[] rows = input.split("\n");
        String[][] sgrid = new String[10][10];
        for (int i=0; i<10; i++) {
            sgrid[i] = rows[i].split(" ");
        }

        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                //System.out.println(sgrid[j][9-i]);
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
//        for (int i : bp.grid[0]) System.out.print(i+" ");
    }

}
