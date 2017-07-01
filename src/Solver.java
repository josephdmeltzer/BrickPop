import java.util.*;

/**
 * Created by Joseph Meltzer on 30/04/2017.
 */
public class Solver {

    public class RegionScore {
        int score;
//        ColourSet region;
        ArrayList<Coord> steps;
//        RegionScore previous;

        RegionScore(int s, ArrayList<Coord> steps) {
            score = s;
            this.steps = steps;
        }

//        RegionScore(int s, ColourSet r) {
//            score = s;
//            region = r;
//        }
//
//        RegionScore(int s, ColourSet r, RegionScore prev) {
//            score = s;
//            region = r;
//            previous = prev;
//        }

//        public boolean equals(Object o) {
//            if (o instanceof RegionScore) {
//                return score == ((RegionScore) o).score &&
//                        region.equals(((RegionScore) o).region) && previous.equals(((RegionScore) o).previous);
//            }
//            return false;
//        }
    }

    public class Node {
        BrickPop bp;
        ColourSet region;
        Node previous;
        ArrayList<Coord> steps;

//        Node(BrickPop bp, ColourSet r, Node p) {
//            this.bp = bp;
//            region = r;
//            previous = p;
//        }

        Node(BrickPop bp, ColourSet r) {
            this.bp = bp;
            region = r;
            this.steps = new ArrayList<>();
        }

        Node(BrickPop bp, ColourSet r, Coord next, ArrayList<Coord> steps) {
            this.bp = bp;
            region = r;
            this.steps = new ArrayList<>(steps);
            this.steps.add(next);
        }

        public int depth() {
            return steps.size();
//            if (previous==null) return 1;
//            else return 1 + previous.depth();
        }
    }

    public ArrayList<Coord> solveBRFS(BrickPop bp) {
        Queue<Node> frontier = new ArrayDeque<>();
        frontier.add(new Node(bp, null));

        while (frontier.size()>0) {
            if (!frontier.peek().bp.finished()) {
                Node current = frontier.poll();
                for (ColourSet r : current.bp.regions) {
                    if (r.size()>1 && r.colour>0) {
                        Node node = new Node(bp.successor(r.any()), r, r.any(), current.steps);
                        System.out.println("Adding node: depth=" + node.depth());
                        // + ",\t" + "score=" + node.bp.score + ",\tregion=" + r
                        frontier.add(node);
                    }
                }
            }
            else break;
        }
        return frontier.peek().steps;
//        ArrayList<Coord> movesList = new ArrayList<>();
//        Node currentnode = frontier.peek();
//        while (currentnode.previous!=null) {
//            movesList.add(currentnode.region.any());
//            currentnode = currentnode.previous;
//        }
//        Collections.reverse(movesList);
//        return movesList;
    }

    public Random rand = new Random();

    public ArrayList<Coord> solve(BrickPop bp, int lb, int ub) {
        //RegionScore initialnode = new RegionScore(-5, null);
        RegionScore bestnode = null;
        for (int i=lb; i <=ub; i++) {
            RegionScore result = dfs(i, bp, new ArrayList<>());
            //System.out.println("final region"+result.region);
            //System.out.println("final score"+result.score);
            if (bestnode==null || result.score>bestnode.score) {
                bestnode = result;
            }
//            if (result.score>0) {
//                System.out.println(result.score);
//                return result.steps;
//                //finalnode = result;
//                //break;
//            }
        }
        System.out.println(bestnode.score+", over "+bestnode.steps.size()+" steps. ");
//        for (int i=10; i>=1; i--) {
//            for (int j=1; j<=10; j++) {
//                System.out.print(bestnode.bp.grid[i-1][j-1]+" ");
//            }
//            System.out.println("");
//        }
        return bestnode.steps;

        //System.out.println("No solution found");
        //return null;

//        if (finalnode==null) {
//
//        }
//        ArrayList<Coord> moveList = new ArrayList<>();
//        //System.out.println(finalnode.region);
//        RegionScore currentnode = finalnode;
//        //System.out.println(finalnode.previous.score);
//        while (currentnode.previous!=null) {
//            moveList.add(currentnode.region.any());
//            currentnode = currentnode.previous;
//        }
//        Collections.reverse(moveList);
//        return moveList;
    }

    public RegionScore dfs(int depth, BrickPop bp, ArrayList<Coord> steps) {
        //System.out.println("Examining node: depth="+(5-depth)+", score="+bp.score+", steps="+steps);
        if (bp.finished()) {
            //System.out.println("finished");
            //System.out.println(bp.score );
//            return new RegionScore(bp.score, prevnode.region, prevnode.previous);
            System.out.println("Solution found! Depth = "+steps.size()+", Score = "+(bp.score));
            return new RegionScore(bp.score+9000, steps);
        }
        if (depth==0) {
            //System.out.println("X");
            //if (depth==0) //System.out.println("depth 0");
            //return new RegionScore(0, prevnode.region, prevnode.previous);
            return new RegionScore(bp.score, steps);
        }
        if (bp.failed()) {
            //System.out.println("X");
            //if (depth==0) //System.out.println("depth 0");
            //return new RegionScore(0, prevnode.region, prevnode.previous);
            return new RegionScore(bp.score-9000, steps);
        }

        //System.out.println("hi");
        int bestScore = -9001;
        //ColourSet bestRegion = null;
        RegionScore bestRS = null;
        for (ColourSet r : bp.regions) {
            if (r.colour>0 && r.size()>1 && (rand.nextFloat()>0.98 || bestRS==null) ) {
                //System.out.println("selected"+r);
                //System.out.println(bp.grid[1][0]);
//            RegionScore next = dfs(depth-1, bp.successor(r.any()), new RegionScore(bp.score, r, prevnode));
                ArrayList<Coord> steps2 = new ArrayList<>(steps);
                steps2.add(r.any());
                RegionScore next = dfs(depth - 1, bp.successor(r.any()), steps2);
                ////System.out.println(next.region);
                if (next.score >= bestScore) {
                    //System.out.println("new best, "+next.score);
                    bestScore = next.score;
                    //bestRegion = r;
                    bestRS = next;
                }
            }
        }
        if (bestRS==null) {
//            System.out.println(bp.regions);
//            for (int i=10; i>=1; i--) {
//                for (int j = 1; j <= 10; j++) {
//                    System.out.print(bp.grid[i - 1][j - 1] + " ");
//                }
//                System.out.println("");
//            }
            return new RegionScore(bp.score-9000, steps);
        };
        //if (bestScore<0) System.out.println("Failed");
        return bestRS;
    }

    public static void main(String[] args) {
        BrickPop bp = new BrickPop();
//        bp.grid[0][1] = 1;
//        bp.grid[0][0] = 1;
//        bp.grid[1][0] = 2;
//        bp.grid[1][1] = 2;
//        bp.setUpRegions();
//
//        Solver s = new Solver();
//        ArrayList<Coord> moves = s.solve(bp);
//        //System.out.println("##########");
//        for (Coord c : moves) {
//            //System.out.println(c);
//        }

//        String line1 = "1 1 2 3 3 2 1 2 3 2";
//        String[] l1s = line1.split(" ");
//        for (String st : l1s) //System.out.println(st);

//        String input = "1 1 2 3 3 2 1 2 3 2\n1 3 3 3 3 1 2 2 2 2\n3 3 2 3 3 1 2 3 2 3\n1 3 2 2 3 3 2 1 2 3\n1 3 1 2 1 1 2 3 2 2\n1 1 1 2 1 1 1 3 2 2\n2 1 1 1 2 2 1 3 2 1\n2 2 2 2 1 2 3 3 1 1\n3 3 3 2 2 2 3 3 1 1\n3 3 3 2 2 2 3 2 2 2";
//        String input = "2 2 1 3 3 2 2 2 1 1\n2 2 1 3 3 1 3 2 1 1\n2 2 1 3 2 1 3 2 2 1\n1 2 2 3 1 1 3 2 3 1\n3 3 2 1 3 2 3 2 3 3\n3 1 3 3 1 3 3 3 3 3\n3 1 1 2 1 3 2 2 2 2\n1 1 3 2 1 3 3 2 3 3\n1 1 3 1 2 3 3 2 3 3\n2 2 3 2 2 2 3 2 1 1";
        //String input = "3 3 1 1 1 3 2 2 2 2\n3 3 1 1 3 1 2 3 1 1\n3 1 3 3 3 1 3 2 3 3\n3 3 3 3 1 1 2 2 1 3\n1 2 3 2 1 2 3 2 2 2\n2 3 3 1 1 2 3 2 1 2\n3 3 3 3 2 3 2 2 1 1\n1 1 1 3 2 2 1 1 1 1\n1 2 2 3 2 2 1 1 1 1\n1 1 1 3 2 2 2 1 1 1";
        //String input = "3 3 2 2 2 2 3 2 1 1\n3 1 1 2 2 1 2 1 3 1\n3 1 2 2 1 1 2 1 1 3\n3 1 3 2 3 1 3 1 1 1\n1 3 2 1 2 1 3 1 1 1\n2 1 2 3 2 2 3 3 3 3\n2 2 2 3 3 2 2 3 1 1\n2 2 2 3 3 3 2 2 1 1\n3 1 2 1 1 1 3 3 2 2\n2 2 2 1 3 3 2 2 2 2";
        //String input = "2 4 4 4 4 3 3 1 3 3\n4 4 3 4 2 3 2 4 3 3\n2 2 3 4 2 2 2 4 3 2\n4 4 2 3 3 4 2 4 2 2\n4 1 2 4 3 4 3 4 3 2\n1 1 1 2 3 4 3 4 3 3\n1 2 2 1 4 4 3 4 1 3\n1 1 1 1 1 4 4 1 4 3\n2 1 1 1 2 2 3 1 4 4\n2 2 1 4 2 2 3 4 4 4";
//        String input = "2 3 3 2 2 1 1 1 3 1\n2 1 3 2 2 3 3 1 1 1\n2 1 2 2 2 3 1 1 1 1\n1 1 2 2 2 3 1 3 1 1\n1 1 2 1 2 3 3 1 3 1\n1 3 2 1 3 3 3 2 3 3\n3 3 2 1 1 2 3 3 2 3\n3 1 2 2 1 3 2 3 2 2\n3 1 2 2 2 3 3 3 2 2\n3 1 3 3 3 3 3 1 3 3";
        //String input = "2 1 4 1 3 3 4 4 3 4\n4 4 1 1 3 3 2 4 3 3\n4 2 1 4 3 3 4 4 3 3\n4 2 1 4 4 4 1 4 4 1\n1 4 1 2 4 1 1 2 4 1\n1 1 3 2 2 1 1 1 3 1\n1 4 2 2 2 1 3 3 3 3\n1 2 3 3 2 2 3 1 3 4\n4 1 3 3 2 2 3 3 1 4\n1 3 3 3 3 3 1 1 2 2";
//        String input = "3 1 2 3 3 3 2 2 3 1\n3 1 2 3 3 3 2 2 1 1\n3 2 1 3 3 2 3 3 1 1\n3 2 1 3 2 2 3 3 1 2\n1 2 1 3 2 1 3 3 1 1\n1 1 1 3 2 1 1 1 1 1\n1 1 1 2 1 1 1 3 1 1\n3 1 3 2 1 3 3 3 1 1\n2 3 3 1 1 1 3 3 2 1\n3 3 3 2 2 2 3 3 3 2";
        //String input = "3 3 4 4 2 2 2 3 2 2\n3 4 4 2 3 3 3 3 2 4\n2 3 4 3 3 1 1 3 1 4\n3 3 4 4 3 1 1 1 3 4\n3 3 4 4 4 2 2 4 4 4\n1 2 2 4 4 4 2 3 4 2\n1 1 2 1 1 1 1 3 4 2\n2 4 1 1 4 4 4 1 3 2\n4 2 2 2 3 3 3 1 2 2\n4 1 2 4 4 4 2 2 2 2"
      //String input = "1 1 2 4 3 1 1 2 5 5\n1 4 2 3 5 3 1 3 4 5\n5 5 1 3 5 1 3 3 4 5\n2 5 1 3 3 1 2 1 4 5\n2 5 4 5 3 2 3 1 4 3\n1 1 4 2 3 1 4 1 1 3\n5 3 4 2 3 5 4 4 1 5\n2 2 4 2 5 3 1 4 1 4\n2 2 4 2 3 3 5 4 1 1\n5 3 3 5 3 3 3 5 1 4";
        /*String input = "0 0 0 0 0 0 0 0 0 0\n0 3 0 0 0 0 0 0 0 0\n3 3 0 0 0 0 0 0 0 0\n3 3 0 0 0 0 0 0 0 0\n2 3 0 0 0 0 2 0 0 0\n3 2 0 0 0 0 2 0 0 0\n3 1 2 0 0 2 2 0 2 0\n1 4 2 2 2 2 1 0 2 0\n1 2 2 2 3 3 4 1 1 0\n2 1 2 4 4 4 3 1 3 2";*/
        //String input = "0 0 0 0 0 0 0 0 0 0\n0 0 2 0 0 0 0 0 0 0\n1 1 2 0 0 0 0 0 0 0\n1 4 1 0 0 0 0 0 0 0\n5 5 1 0 0 0 1 0 0 0\n2 5 4 0 0 0 1 0 0 0\n2 5 4 0 0 0 3 0 0 0\n1 1 4 0 0 1 2 2 0 0\n5 3 4 0 0 2 3 3 0 0\n5 3 3 4 3 1 1 3 0 0";
        //String input = "0 0 0 0 0 0 0 0 0 0\n1 0 2 0 0 0 0 0 0 0\n1 0 2 0 0 0 0 0 0 0\n2 1 1 0 0 0 0 0 0 0\n2 4 1 0 0 1 1 0 0 0\n1 1 4 0 0 3 1 0 0 0\n5 3 4 0 0 1 3 0 0 0\n2 2 4 0 0 1 2 2 0 0\n2 2 4 0 0 2 3 3 0 0\n5 3 3 4 3 1 1 3 0 0";
//        String input = "1 1 0 0 0 0 0 0 0 0\n1 4 2 0 0 0 0 0 0 0\n5 5 2 0 0 0 0 0 0 0\n2 5 1 0 0 0 0 0 0 0\n2 5 1 0 0 0 0 0 0 0\n1 1 4 0 0 0 0 0 0 0\n5 3 4 0 0 0 0 0 0 0\n2 2 4 0 0 1 0 0 0 0\n2 2 4 0 0 2 0 0 0 0\n5 3 3 4 3 1 1 0 0 0";
//        String input = "6 2 6 2 2 4 1 6 2 1\n3 4 6 2 6 4 6 6 5 1\n3 4 1 6 6 2 6 2 5 5\n3 5 2 1 2 2 5 5 5 5\n6 3 2 1 5 1 4 6 2 5\n2 3 2 1 1 1 4 3 2 5\n3 5 6 5 5 6 4 4 6 5\n2 1 5 5 5 6 3 3 6 1\n4 5 4 4 2 3 3 6 6 1\n4 4 4 4 4 5 3 3 3 1";
        //String input = "4 1 5 6 6 1 5 5 6 1\n1 1 6 6 4 1 6 6 5 1\n1 1 1 6 4 1 6 6 3 1\n4 3 3 6 4 1 6 3 2 1\n3 3 5 3 4 1 3 3 4 1\n5 5 1 6 4 2 5 2 4 1\n4 5 6 6 6 2 2 4 5 1\n5 5 2 3 3 2 2 3 1 1\n5 1 2 6 3 2 3 5 1 4\n1 2 2 2 2 3 5 5 5 4";
        //String input = "1 2 4 1 3 3 3 3 4 6\n1 2 4 6 1 2 3 3 4 6\n1 4 2 6 6 3 2 3 4 2\n1 4 2 1 6 1 4 4 5 2\n5 4 1 1 3 1 2 2 4 2\n5 5 5 3 2 5 3 4 4 2\n5 5 3 5 5 2 3 5 5 2\n1 2 2 2 2 2 2 2 6 2\n4 3 3 2 6 3 1 1 2 2\n4 3 3 6 1 3 2 3 2 2";
        String input = "4 1 5 2 2 2 4 6 5 5\n5 4 5 2 2 2 6 6 5 5\n3 5 5 1 2 2 6 6 3 4\n3 3 2 1 2 6 2 2 4 4\n1 6 6 1 5 2 4 4 3 4\n1 5 5 1 5 4 1 4 3 3\n4 3 2 5 5 4 3 1 3 3\n4 4 3 2 6 1 3 3 1 1\n4 4 4 2 6 1 3 3 6 6\n4 4 3 2 6 1 1 1 1 1";
        input = "4 0 0 0 0 2 4 6 0 0\n5 1 5 2 0 2 6 6 0 0\n3 4 5 2 0 2 6 6 3 4\n3 5 5 1 2 6 2 2 4 4\n3 3 2 1 2 2 4 4 3 4\n1 6 6 1 2 4 1 4 3 3\n4 3 2 1 2 4 3 1 3 3\n4 4 3 2 6 1 3 3 1 1\n4 4 4 2 6 1 3 3 6 6\n4 4 3 2 6 1 1 1 1 1";
        input = "4 6 6 1 1 4 4 6 1 1\n4 2 6 1 1 6 6 1 1 2\n6 2 4 3 5 3 3 1 1 2\n6 4 1 3 5 3 6 3 2 5\n5 6 6 1 4 5 1 3 4 4\n5 5 6 1 6 5 4 3 2 6\n1 1 5 1 4 3 3 3 2 6\n2 1 1 1 5 4 1 2 5 6\n2 2 1 5 5 1 4 4 5 4\n2 2 3 3 1 1 4 3 5 5";
        input = "4 4 5 4 1 1 6 4 4 5\n4 5 5 1 3 1 2 4 4 2\n5 3 5 1 5 1 3 6 3 2\n3 3 4 5 5 2 3 2 3 2\n2 3 5 1 3 2 1 5 3 2\n5 5 6 4 2 4 1 4 1 1\n3 5 3 4 4 4 1 1 3 1\n5 5 5 5 4 3 5 5 2 6\n2 3 3 6 2 6 4 2 2 6\n6 6 6 3 6 2 5 2 5 2";
        input = "3 1 4 4 3 2 1 1 5 4\n4 4 3 4 3 1 1 1 1 4\n3 1 3 4 2 1 2 1 5 5\n3 5 3 5 1 3 4 2 6 4\n1 5 6 1 4 3 1 5 6 1\n1 6 6 3 2 3 4 2 4 6\n5 4 6 1 5 4 4 2 4 6\n5 6 4 2 2 2 2 5 5 6\n5 1 5 1 3 2 4 5 5 6\n3 3 3 2 3 4 5 1 1 6";
        input = "4 2 1 1 3 6 6 3 6 2\n2 2 1 5 1 4 3 6 6 2\n4 2 1 5 1 4 1 6 6 2\n6 4 5 3 1 1 6 1 5 3\n6 4 3 3 4 4 2 5 1 1\n6 6 5 4 2 2 2 4 2 1\n1 1 5 4 4 2 1 5 5 1\n1 1 4 2 3 1 5 5 4 3\n1 4 2 2 6 4 4 4 2 3\n5 5 5 3 3 3 6 1 3 3";
        input = "4 4 3 4 6 6 5 3 5 4\n4 4 3 4 2 2 2 3 2 4\n1 3 3 4 4 2 2 5 2 4\n1 5 3 4 2 2 3 5 4 4\n1 5 5 2 2 2 3 5 5 4\n1 4 5 6 6 6 5 4 5 5\n6 6 6 6 1 5 5 4 4 6\n2 1 6 6 6 5 1 4 6 6\n2 1 6 6 6 1 4 6 1 6\n2 2 2 1 1 1 4 1 1 1";
        input = "3 3 2 2 3 3 3 1 1 3\n3 3 2 3 3 3 3 3 1 1\n1 3 3 2 3 1 1 1 1 1\n3 3 1 1 3 3 1 1 1 1\n1 1 2 2 2 3 3 2 2 1\n1 1 2 2 3 3 2 3 2 1\n1 3 2 2 1 3 3 3 2 1\n1 3 1 1 3 3 3 3 3 3\n2 2 3 1 1 3 2 2 2 3\n2 2 3 3 3 2 2 2 2 3";
        input = "2 2 1 1 3 3 1 1 2 1\n4 2 2 1 3 4 1 1 3 1\n1 1 1 1 1 1 1 2 3 4\n1 2 1 3 3 1 1 3 3 4\n1 1 4 3 3 1 1 3 3 4\n4 4 2 3 3 3 1 3 2 4\n4 4 2 3 3 3 3 2 2 4\n1 2 3 3 3 4 2 2 1 4\n1 2 4 2 2 2 1 1 2 2\n4 4 4 3 3 2 2 2 4 2";
        input = "1 1 3 5 5 4 4 1 2 3\n2 1 3 5 5 1 1 1 2 3\n2 3 5 5 1 3 1 2 3 3\n2 1 3 5 3 4 3 3 2 2\n4 5 3 5 3 2 1 3 2 3\n4 5 3 3 3 1 1 1 2 3\n3 3 2 2 4 2 2 2 5 5\n3 3 1 1 4 2 2 5 1 1\n1 4 1 4 5 5 5 2 5 5\n4 2 1 1 2 2 2 2 2 5";
        input = "1 1 2 5 5 4 5 5 6 4\n1 1 2 2 5 3 5 6 6 4\n1 1 5 6 5 3 3 6 2 4\n4 6 5 4 5 1 3 3 1 4\n3 6 6 3 4 1 3 5 1 2\n4 6 2 5 1 1 1 1 1 1\n4 2 6 5 1 1 3 5 2 2\n1 1 4 5 3 5 5 5 6 2\n1 4 4 3 2 4 5 3 6 6\n3 4 3 3 3 4 4 5 2 2";
        input = "3 6 3 6 6 2 4 1 3 2\n6 4 5 6 1 2 4 4 4 5\n2 4 3 1 1 1 2 3 3 5\n5 4 4 4 4 1 1 3 3 5\n2 6 6 3 3 3 1 1 1 2\n3 2 6 4 3 6 1 6 1 2\n1 5 2 6 3 6 1 6 6 2\n2 3 2 2 1 6 1 6 6 4\n2 1 6 6 6 1 5 6 3 4\n2 1 5 6 6 6 5 3 3 2";
        input = "3 3 3 1 2 6 3 5 3 3\n5 3 1 1 2 6 5 5 3 2\n1 5 3 4 5 6 5 5 3 1\n1 4 4 1 3 5 4 3 1 1\n1 1 4 6 6 5 1 4 2 1\n3 1 4 2 3 5 1 4 3 3\n3 3 4 2 3 6 2 4 5 5\n1 3 4 5 2 3 6 1 5 4\n3 6 2 5 2 3 6 4 4 4\n6 2 2 2 2 2 1 1 4 4";
        input = "3 4 3 6 3 4 4 4 1 3\n3 4 2 6 3 3 4 4 4 2\n5 1 2 6 6 4 2 6 4 5\n1 3 2 6 6 1 2 4 4 3\n1 3 1 1 4 6 4 1 1 6\n5 1 6 4 4 6 6 6 3 6\n6 6 6 4 2 6 6 2 2 6\n5 1 6 4 1 1 1 2 2 6\n2 5 5 5 1 4 1 1 5 6\n2 5 4 5 2 2 2 3 5 6";
        input = "5 6 1 4 1 1 1 4 5 5\n6 6 6 6 1 1 5 4 4 5\n6 2 1 1 2 5 4 3 3 5\n6 2 4 1 2 2 4 2 3 2\n2 5 4 2 2 2 3 2 1 5\n2 3 4 2 1 4 3 2 2 1\n2 3 4 2 5 4 3 1 1 1\n2 3 2 5 4 4 6 3 1 1\n3 1 1 5 6 6 6 3 3 1\n3 1 1 1 1 5 6 6 6 5";
        input = "2 6 1 3 3 4 1 1 1 1\n4 6 5 5 3 5 2 1 3 1\n4 6 5 3 3 5 2 2 4 5\n4 5 3 3 3 5 4 2 4 5\n6 5 1 1 3 5 4 6 4 3\n2 2 1 1 3 6 6 1 4 3\n2 2 4 4 3 6 6 1 4 4\n5 6 1 1 3 4 1 1 4 1\n5 1 1 1 4 1 4 4 1 1\n5 5 4 4 6 6 6 6 2 2";
        input = "3 2 3 4 1 1 2 1 3 6\n1 2 2 4 1 1 2 1 6 6\n2 5 1 2 6 1 2 3 6 2\n2 5 1 6 6 1 2 4 2 1\n1 5 2 6 6 5 6 4 1 1\n5 1 4 6 5 5 6 4 2 1\n5 3 4 2 5 5 3 4 5 2\n1 6 4 5 2 3 3 5 5 5\n3 6 4 2 3 6 6 4 4 5\n6 2 2 3 2 2 6 6 6 6";
        input = "5 6 4 2 2 4 3 5 4 4\n3 1 6 2 6 3 4 5 6 4\n1 1 2 1 6 5 5 3 6 4\n1 3 1 2 6 5 5 3 6 6\n3 6 2 2 1 4 4 4 6 4\n6 4 1 1 4 1 1 4 4 3\n6 4 2 2 6 1 1 4 5 4\n5 6 2 4 6 6 6 2 3 4\n2 2 4 6 1 5 2 2 3 4\n5 5 5 4 6 5 5 5 4 4";

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

//        for (int i=10; i>=1; i--) {
//            for (int j=1; j<=10; j++) {
//                System.out.print(bp.grid[i-1][j-1]+" ");
//            }
//            System.out.println("");
//        }

        Solver s = new Solver();
        ArrayList<Coord> moves = s.solve(bp, 22, 35);
        //System.out.println("##########");
        for (Coord c : moves) {
            System.out.println("("+(c.x+1)+", "+(c.y+1)+")");
        }
    }
}