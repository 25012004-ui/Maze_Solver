import javax.swing.*; import java.awt.*;
import java.awt.event.MouseAdapter; import java.awt.event.MouseEvent;

public class MazeSolver extends JPanel { private static final int PATH = 0; private static final int WALL = 1; private static final int VISITED = 2; private static final int SOLUTION = 3;

private int[][] maze;

private int rows; private int cols;
private boolean solving;


public MazeSolver(int rows, int cols) { this.rows = rows;
this.cols = cols;
this.maze = new int[rows][cols]; this.solving = false;

this.setPreferredSize(new Dimension(600, 600)); this.addMouseListener(new MouseAdapter() {
@Override
public void mousePressed(MouseEvent e) { if (!solving) {
int cellSize = getWidth() / cols; int col = e.getX() / cellSize;
int row = e.getY() / cellSize;


if (row >= 0 && row < rows && col >= 0 && col < cols) { maze[row][col] = (maze[row][col] == PATH) ? WALL : PATH; repaint();
}
}
}
});
}


public void startSolving() { solving = true;
new Thread(() -> {

if (solve(0, 0)) { System.out.println("Path found!");
} else {
System.out.println("No path found.");
}
solving = false;
}).start();
}


public void resetMaze() {
for (int row = 0; row < rows; row++) { for (int col = 0; col < cols; col++) {
maze[row][col] = PATH;
}
}
solving = false; repaint();
}


private boolean solve(int row, int col) {
if (row < 0 || col < 0 || row >= rows || col >= cols || maze[row][col] != PATH) { return false;
}


maze[row][col] = VISITED; repaint();
try {
Thread.sleep(50); // delay for visualization
} catch (InterruptedException e) { e.printStackTrace();
}


if (row == rows - 1 && col == cols - 1) { maze[row][col] = SOLUTION; repaint();
return true;
}


int[] dRow = {1, -1, 0, 0};
int[] dCol = {0, 0, 1, -1};


for (int i = 0; i < 4; i++) {
if (solve(row + dRow[i], col + dCol[i])) { maze[row][col] = SOLUTION; repaint();
return true;
}
}


maze[row][col] = PATH; // backtrack repaint();
try {
Thread.sleep(50); // delay for visualization
} catch (InterruptedException e) { e.printStackTrace();
}
return false;
}
protected void paintComponent(Graphics g) { super.paintComponent(g);
int cellSize = getWidth() / cols;

for (int row = 0; row < rows; row++) { for (int col = 0; col < cols; col++) {
switch (maze[row][col]) { case PATH:
g.setColor(Color.WHITE); break;
case WALL: g.setColor(Color.BLACK); break;
case VISITED: g.setColor(Color.BLUE); break;
case SOLUTION: g.setColor(Color.GREEN); break;
}
g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize); g.setColor(Color.GRAY);
g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
}
}
}


public static void main(String[] args) {
JFrame frame = new JFrame("Maze Solver"); frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

JPanel controlPanel = new JPanel(); JTextField rowsField = new JTextField(5); JTextField colsField = new JTextField(5);
JButton setGridButton = new JButton("Set Grid");

JButton startButton = new JButton("Start"); JButton resetButton = new JButton("Reset");

controlPanel.add(new JLabel("Rows:")); controlPanel.add(rowsField); controlPanel.add(new JLabel("Cols:")); controlPanel.add(colsField); controlPanel.add(setGridButton); controlPanel.add(startButton); controlPanel.add(resetButton);

frame.setLayout(new BorderLayout()); frame.add(controlPanel, BorderLayout.NORTH);

MazeSolver[] mazeSolver = {new MazeSolver(20, 20)}; frame.add(mazeSolver[0], BorderLayout.CENTER);

setGridButton.addActionListener(e -> { try {
int rows = Integer.parseInt(rowsField.getText()); int cols = Integer.parseInt(colsField.getText()); frame.remove(mazeSolver[0]);
mazeSolver[0] = new MazeSolver(rows, cols); frame.add(mazeSolver[0], BorderLayout.CENTER); frame.validate();
frame.repaint();
} catch (NumberFormatException ex) {
JOptionPane.showMessageDialog(frame, "Invalid input! Please enter valid integers for rows and columns.");
}
});

startButton.addActionListener(e -> mazeSolver[0].startSolving()); resetButton.addActionListener(e -> mazeSolver[0].resetMaze());

frame.pack(); frame.setVisible(true);
}
}
