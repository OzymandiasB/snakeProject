import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

public class Board extends JPanel implements Runnable, KeyListener {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int PIXELS = 25;

    private static final Color APPLE = Color.RED;
    private int xApple;
    private int yApple;

    private Body head;
    private Body tail;
    private ArrayList<Body> SNAKE;
    private int score;

    private int xPos = 250, yPos = 250;
    private boolean UP = false, DOWN = false, LEFT = false, RIGHT = false;
    private int counter = 0;

    public boolean gameOver;

    public Board() {
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        head = new Body(xPos, xPos, PIXELS);
        tail = head;
        SNAKE = new ArrayList<Body>();
        SNAKE.add(head);
        score = 0;
        UP = false;
        DOWN = false;
        LEFT = false;
        RIGHT = false;

        xApple = randomNum();
        yApple = randomNum();

        addKeyListener(this);
    }

    //Move the snake
    public void slither() {
        counter++;
        if (counter > 1250000) {
            if (UP)
                yPos -= PIXELS;
            if (DOWN)
                yPos += PIXELS;
            if(LEFT)
                xPos -= PIXELS;
            if(RIGHT)
                xPos += PIXELS;
            counter = 0;

            update();
        }
    }

    //Update snake's position
    public void update() {
        if (length() > 1) {
            for (int i = length() - 1; i > 0; i--) {
                SNAKE.get(i).setxPos(SNAKE.get(i - 1).getxPos());
                SNAKE.get(i).setyPos(SNAKE.get(i - 1).getyPos());
            }
        }
        head.setxPos(xPos);
        head.setyPos(yPos);
    }

    public int randomNum() {
        Random rand = new Random();
        return rand.nextInt(20) * 25;
    }

    public int length() {
        return SNAKE.size();
    }

    //Place snake on grid
    public void spawnSnake(Graphics g) {
        for (int i = 0; i < length(); i++) {
            SNAKE.get(i).paintComponent(g);
        }
    }

    //Place apple on grid
    public void spawnApple(Graphics g) {
        while (!checkGrid(xApple, yApple)) {
            xApple = randomNum();
            yApple = randomNum();
        }
        g.setColor(APPLE);
        g.fillRect(xApple, yApple, PIXELS, PIXELS);
    }

    //Gameover conditions
    public void collision(){
        //Out of bound check
        if (xPos < 0 || yPos < 0 || xPos > 475 || yPos > 475) {
            gameOver = true;
        }

        //Body collision check
        for(int x = 1; x < SNAKE.size(); x++){
            if(head.getxPos() == SNAKE.get(x).getxPos() && head.getyPos() == SNAKE.get(x).getyPos()){
                gameOver = true;
            }
        }
    }

    public boolean checkGrid(int x, int y) {
        elongate();
        collision();
        for (int i = 0; i < length(); i++) {
            if ((SNAKE.get(i).getxPos() == x) && (SNAKE.get(i).getyPos() == y)) {
                return false;
            }
        }
        return true;
    }

    //Adds on to the tail
    public void elongate(){
        if(head.getxPos() == xApple && head.getyPos() == yApple) {
            //if head going right
            if (RIGHT) {
                tail = new Body(tail.getxPos() - 25, tail.getyPos(), PIXELS);
                SNAKE.add(tail);
            }
            //if head goes left
            if (LEFT) {
                tail = new Body(tail.getxPos() + 25, tail.getyPos(), PIXELS);
                SNAKE.add(tail);
            }
            //if head goes up
            if (UP) {
                tail = new Body(tail.getxPos(), tail.getyPos() + 25, PIXELS);
                SNAKE.add(tail);
            }
            //if head goes down
            if (DOWN) {
                tail = new Body(tail.getxPos(), tail.getyPos() - 25, PIXELS);
                SNAKE.add(tail);
            }
        }
    }

    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //Draw 20 x 20 grid
        for (int i = 0; i < WIDTH / 25; i++) {
            g.drawLine(i * 25, 0, i * 25, HEIGHT);
        }
        for (int i = 0; i < HEIGHT / 25; i++) {
            g.drawLine(0, i * 25, HEIGHT, i * 25);
        }

        spawnSnake(g);
        spawnApple(g);
    }

    @Override
    public void run() {
        while (!gameOver) {
            slither();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W && !DOWN) {
            UP = true;
            DOWN = false;
            LEFT = false;
            RIGHT = false;
        }
        if (key == KeyEvent.VK_S && !UP) {
            UP = false;
            DOWN = true;
            LEFT = false;
            RIGHT = false;
        }
        if (key == KeyEvent.VK_A && !RIGHT) {
            UP = false;
            DOWN = false;
            LEFT = true;
            RIGHT = false;
        }
        if (key == KeyEvent.VK_D && !LEFT) {
            UP = false;
            DOWN = false;
            LEFT = false;
            RIGHT = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
