import java.util.Scanner;
import java.util.Random;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

class node {
    int xPos;
    int yPos;
    boolean marked = false;
}

public class CatAndMouse {
    public static void main(String[] a) throws IOException {
        int row = MakeOdd(11);
        int column = MakeOdd(11);
        int score = 0;
        boolean keepGoing = true;
        final String mouse = "M";
        final String cheese = "%";
        final String wall = "#";
        String inp = " ";

        BufferedReader inputStream = new BufferedReader(new FileReader("score.txt"));
        String[] line = GetLine(inputStream);
        int highScore = Integer.parseInt(line[0]);
        String name = line[1];
        inputStream.close();

        String[][] map;
        while (keepGoing) {
            inp = " ";

            int[] mousePosition = { 1, 1 };
            map = InitializeBorder(row, column, wall);
            map = InitializeMaze(map, wall);
            map = InitializeCheese(map, cheese);
            map = InitializeMouse(map, mouse, mousePosition);
            PrintControls();

            while (!inp.equals("exit")) {
                DisplayMap(map);
                boolean validMovement = false;

                while (!validMovement) {
                    inp = GetMovementInput();
                    validMovement = CheckIfValidMove(mousePosition, inp, map, wall);
                }

                if (inp.equals("exit")) {
                    keepGoing = false;
                }

                mousePosition = MoveMouse(map, inp, mousePosition);
                map = UpdateMap(map, mousePosition, mouse);

                if (checkIfWon(map, cheese)) {
                    DisplayMap(map);
                    score++;
                    keepGoing = WonMessage(score, highScore, name);
                    inp = "exit";
                }
            }
        }

        if (score > highScore) {
            String newName = InputString("What is your name?");
            PrintWriter writer = new PrintWriter(new FileWriter("score.txt"));
            writer.print(score);
            writer.print(",");
            writer.print(newName);
            writer.close();
        }

        return;
    }

    public static String[] GetLine(BufferedReader r) throws IOException {
        String line = r.readLine();
        String[] sep = line.split(",");
        return sep;
    }

    public static boolean WonMessage(int score, int hs, String name) {
        boolean valid = false;
        boolean keepGoing = false;
        System.out.println("Congrats! You won!");
        System.out.println("Current score: " + score);
        System.out.println("Highest Score: " + hs);
        System.out.println("Name of person who got the Highest Score " + name);
        String inp = InputString("Do you want to keep playing?(y/n)");
        while (!valid) {
            if (inp.equals("y")) {
                valid = true;
                keepGoing = true;
            } else if (inp.equals("n")) {
                valid = true;
                keepGoing = false;
            } else {
                inp = InputString("Invalid input. (y/n)");
            }
        }
        return keepGoing;
    }

    public static int MakeOdd(int i) {
        if (i % 2 == 1) {
            return i;
        } else {
            return i + 1;
        }
    }

    public static String GetMovementInput() {
        boolean valid = false;
        String movement = "";
        while (!valid) {
            valid = true;
            String inp = InputString("What is your next move?");
            if (inp.equals("W") || inp.equals("w")) {
                movement = "up";
            } else if (inp.equals("A") || inp.equals("a")) {
                movement = "left";
            } else if (inp.equals("D") || inp.equals("d")) {
                movement = "right";
            } else if (inp.equals("S") || inp.equals("s")) {
                movement = "down";
            } else if (inp.equals("EXIT") || inp.equals("exit")) {
                movement = "exit";
            } else {
                System.out.println("Invalid input, Please try again");
                PrintControls();
                valid = false;
            }
        }
        return movement;
    }

    public static boolean CheckIfValidMove(int[] mPos, String inp, String[][] map, String wall) {
        int xPos = mPos[1];
        int yPos = mPos[0];

        if (inp.equals("up")) {
            yPos -= 1;
        } else if (inp.equals("down")) {
            yPos += 1;
        } else if (inp.equals("left")) {
            xPos -= 1;
        } else if (inp.equals("right")) {
            xPos += 1;
        }

        if (map[yPos][xPos].equals(wall)) {
            System.out.println("Wall in the way, try again");
            return false;
        } else {
            return true;
        }
    }

    public static String[][] UpdateMap(String[][] map, int[] mPos, String mChar) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j].equals(mChar)) {
                    map[i][j] = " ";
                }
            }
        }
        map[mPos[0]][mPos[1]] = mChar;
        return map;
    }

    public static int[] MoveMouse(String[][] map, String inp, int[] pos) {
        if (inp.equals("up")) {
            pos[0] -= 1;
        } else if (inp.equals("down")) {
            pos[0] += 1;
        } else if (inp.equals("left")) {
            pos[1] -= 1;
        } else if (inp.equals("right")) {
            pos[1] += 1;
        }
        return pos;
    }

    public static void PrintControls() {
        System.out.println("Movement uses WASD (not case sensitive)");
        System.out.println("|Up - W | Left - A | Right - D | Down - S |");
        System.out.println("Type EXIT to terminate program");
    }

    public static String InputString(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(question);
        String ans = scanner.nextLine();
        return ans;
    }

    public static String[][] InitializeBorder(int row, int column, String wall) {
        String[][] list = new String[row][column];
        for (int i = 0; i < row; i++) {
            if (i == 0 || i == row - 1) {
                for (int j = 0; j < column; j++) {
                    list[i][j] = wall;
                }
            } else {
                for (int j = 0; j < column; j++) {
                    if (j == 0 || j == column - 1) {
                        list[i][j] = wall;
                    } else {
                        list[i][j] = " ";
                    }
                }
            }
        }
        return list;
    }

    public static String[][] InitializeMouse(String[][] map, String mouse, int[] pos) {
        map[pos[0]][pos[1]] = mouse;
        return map;
    }

    public static void DisplayMap(String[][] map) {
        int row = map.length;
        int column = map[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println(" ");
        }
    }

    public static String[][] InitializeCheese(String[][] map, String cheese) {
        int row = map.length / 2;
        int column = map[0].length / 2;
        boolean cheesePlaced = false;
        while (!cheesePlaced) {
            Random r = new Random();
            int xPos = r.nextInt(row);
            int yPos = r.nextInt(column);
            if (map[xPos + row][yPos + column].equals(" ")) {
                map[xPos + row][yPos + column] = cheese;
                cheesePlaced = true;
            }
        }
        return map;
    }

    public static boolean checkIfWon(String[][] map, String cheese) {
        boolean won = true;
        int row = map.length;
        int column = map[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (map[i][j].equals(cheese)) {
                    won = false;
                }
            }
        }
        return won;
    }

    public static String[][] InitializeMaze(String[][] map, String wall) {
        int row = map.length;
        int column = map[0].length;

        node[] nodeArray = new node[(row / 2) * (column / 2)];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (i % 2 == 0 || j % 2 == 0) {
                    map[i][j] = wall;
                }
                if (map[i][j].equals(" ")) {
                    nodeArray = AddNodeToArray(nodeArray, i, j, false);
                }
            }
        }

        node startingNode = CreateNode(1, 1, false);
        map = DFS(nodeArray, startingNode, startingNode, map);
        return map;
    }

    public static String[][] DFS(node[] nodeArray, node currentNode, node previousNode, String[][] map) {
        map = Visit(map, previousNode, currentNode);
        nodeArray = Mark(currentNode, nodeArray);
        node[] neighborNodes = GetNeighbors(currentNode, nodeArray);
        for (int i = 0; i < neighborNodes.length; i++) {
            if (neighborNodes[i] != null && !GetMarked(neighborNodes[i])) {
                map = DFS(nodeArray, neighborNodes[i], currentNode, map);
            }
        }
        return map;
    }

    public static String[][] Visit(String[][] map, node pN, node cN) {
        if (GetXPos(pN) - 2 == GetXPos(cN) && GetYPos(pN) == GetYPos(cN)) {
            map[GetXPos(cN) + 1][GetYPos(cN)] = " ";
        } else if (GetXPos(pN) + 2 == GetXPos(cN) && GetYPos(pN) == GetYPos(cN)) {
            map[GetXPos(cN) - 1][GetYPos(cN)] = " ";
        } else if (GetXPos(pN) == GetXPos(cN) && GetYPos(pN) - 2 == GetYPos(cN)) {
            map[GetXPos(cN)][GetYPos(cN) + 1] = " ";
        } else if (GetXPos(pN) == GetXPos(cN) && GetYPos(pN) + 2 == GetYPos(cN)) {
            map[GetXPos(cN)][GetYPos(cN) - 1] = " ";
        }
        return map;
    }

    public static node[] GetNeighbors(node v, node[] array) {
        node[] neighbors = new node[4];
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (GetXPos(v) - 2 == GetXPos(array[i]) && GetYPos(v) == GetYPos(array[i])) {
                neighbors[count++] = array[i];
            } else if (GetXPos(v) + 2 == GetXPos(array[i]) && GetYPos(v) == GetYPos(array[i])) {
                neighbors[count++] = array[i];
            } else if (GetXPos(v) == GetXPos(array[i]) && GetYPos(v) - 2 == GetYPos(array[i])) {
                neighbors[count++] = array[i];
            } else if (GetXPos(v) == GetXPos(array[i]) && GetYPos(v) + 2 == GetYPos(array[i])) {
                neighbors[count++] = array[i];
            }
        }

        Random random = new Random();
        for (int i = 0; i < neighbors.length; i++) {
            int r = random.nextInt(count);
            node temp = neighbors[i];
            neighbors[i] = neighbors[r];
            neighbors[r] = temp;
        }
        return neighbors;
    }

    public static node[] Mark(node v, node[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                if (GetXPos(v) == GetXPos(array[i]) && GetYPos(v) == GetYPos(array[i])) {
                    array[i] = SetMarked(array[i], true);
                }
            }
        }
        return array;
    }

    public static node[] AddNodeToArray(node[] a, int xPos, int yPos, boolean marked) {
        boolean itemAdded = false;
        while (!itemAdded) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] == null) {
                    node item = CreateNode(xPos, yPos, marked);
                    a[i] = item;
                    i = a.length;
                    itemAdded = true;
                }
            }
            if (!itemAdded) {
                System.out.println("Array is full");
                itemAdded = true;
            }
        }
        return a;
    }

    public static node CreateNode(int x, int y, boolean bool) {
        node n = new node();
        n = SetXPos(n, x);
        n = SetYPos(n, y);
        n = SetMarked(n, bool);
        return n;
    }

    public static int GetXPos(node n) {
        return n.xPos;
    }

    public static int GetYPos(node n) {
        return n.yPos;
    }

    public static boolean GetMarked(node n) {
        return n.marked;
    }

    public static node SetMarked(node n, boolean mark) {
        n.marked = mark;
        return n;
    }

    public static node SetXPos(node n, int x) {
        n.xPos = x;
        return n;
    }

    public static node SetYPos(node n, int y) {
        n.yPos = y;
        return n;
    }
}