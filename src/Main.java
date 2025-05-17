import javax.swing.*;     // 引入 Swing 視窗元件
import java.awt.*;        // 引入繪圖相關類別
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;       // 引入常用集合類別
import java.util.Timer;   // 引入 java.util.Timer 類別（排程用）

public class Main extends JPanel implements KeyListener {

    // 每一格的像素大小，貪食蛇和果實都以此為單位移動
    public static final int CELL_SIZE = 20;

    // 遊戲畫面寬高（以像素計）
    public static int width = 600;
    public static int height = 600;

    // 計算格子數（行數與列數）
    public static int row = width / CELL_SIZE;
    public static int column = height / CELL_SIZE;

    // 遊戲中的果實物件
    private Fruit fruit;
    // 遊戲中的貪食蛇物件
    private Snake snake;
    // 用於定時呼叫 repaint() 的定時器
    private Timer t;
    // 目前蛇的移動方向
    private static String direction;
    // 蛇移動速度（單位：毫秒）
    private int speed = 100;

    private  boolean allowKeyPress;

    // 建構子：初始化遊戲內容
    public Main() {
        snake = new Snake();     // 建立蛇物件
        fruit = new Fruit();     // 建立果實物件

        // 建立定時器，每 100 毫秒重繪一次畫面
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();       // 觸發 Swing 重繪，會呼叫 paintComponent()
            }
        }, 0, speed);

        direction = "Right";     // 初始方向向右
        addKeyListener(this);
        setFocusable(true);            // 讓 JPanel 可取得焦點
        requestFocusInWindow();        // 請求鍵盤焦點
        allowKeyPress=true;
    }


    // 繪圖方法（Swing 框架會自動呼叫）
    @Override
    public void paintComponent(Graphics g) {
//        System.out.println("We are calling paintComponent...");

        // 清除背景（未指定顏色，使用預設）
        g.fillRect(0, 0, width, height);

        // 畫出蛇與果實
        snake.drawSnake(g);
        fruit.drawFruit(g);

        // 取得目前蛇頭的位置
        int snakeX = snake.getSnakeBody().get(0).x;
        int snakeY = snake.getSnakeBody().get(0).y;

        // 根據目前方向計算新的蛇頭位置
        if (direction.equals("Right")) {
            snakeX += CELL_SIZE;
        } else if (direction.equals("Left")) {
            snakeX -= CELL_SIZE;
        } else if (direction.equals("Down")) {
            snakeY += CELL_SIZE;
        } else if (direction.equals("Up")) {
            snakeY -= CELL_SIZE;
        }

        // 建立新蛇頭節點，加入到 snake 的身體最前端
        Node newHead = new Node(snakeX, snakeY);
        snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1); // 移除尾巴
        snake.getSnakeBody().add(0, newHead); // 加入新頭
        allowKeyPress = true;
    }

    // 回傳畫布建議尺寸，提供 JFrame 調整用
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    // 主方法，程式進入點
    public static void main(String[] args) {
        JFrame window = new JFrame("Sanke Game");               // 建立視窗
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 關閉視窗即結束程式
        window.setContentPane(new Main());                      // 放入我們的遊戲面板
        window.pack();                                          // 自動調整大小
        window.setLocationRelativeTo(null);                     // 視窗置中
        window.setVisible(true);                                // 顯示視窗
        window.setResizable(false);                             // 禁止改變大小
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 不能180度掉頭
        if(allowKeyPress){
            if(e.getKeyCode() == 37 && !direction.equals("Right")) {
                direction = "Left";
            }else if(e.getKeyCode() == 38 && !direction.equals("Down")) {
                direction = "Up";
            }else if(e.getKeyCode() == 39 && !direction.equals("Left")) {
                direction="Right";
            }else if(e.getKeyCode() == 40 && !direction.equals("Up")) {
                direction = "Down";
            }
            allowKeyPress = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
