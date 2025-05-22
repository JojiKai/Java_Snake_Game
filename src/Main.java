import javax.swing.*;     // 引入 Swing 視窗元件
import java.awt.*;        // 引入繪圖相關類別（Graphics, Dimension 等）
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;       // 引入常用集合類別（如 ArrayList）
import java.util.Timer;   // 引入 java.util.Timer 類別（排程用）

// 主程式類別，繼承 JPanel（作為畫布），並實作 KeyListener 來處理鍵盤事件
public class Main extends JPanel implements KeyListener {

    // 每一格的像素大小，貪食蛇和果實都以此為單位移動
    public static final int CELL_SIZE = 20;

    // 遊戲畫面寬高（以像素計）
    public static int width = 600;
    public static int height = 600;

    // 計算格子數（行數與列數），方便隨機定位果實
    public static int row = width / CELL_SIZE;
    public static int column = height / CELL_SIZE;

    // 遊戲中的果實物件
    private Fruit fruit;

    // 遊戲中的貪食蛇物件
    private Snake snake;

    // 用於定時呼叫 repaint() 的定時器
    private Timer t;

    // 目前蛇的移動方向（Right / Left / Up / Down）
    private static String direction;

    // 蛇移動速度（單位：毫秒），數字越小越快
    private int speed = 100;

    // 控制方向鍵不能連續按（避免同一次 repaint 被觸發多次鍵盤事件）
    private boolean allowKeyPress;

    // 建構子：初始化遊戲內容
    public Main() {
        snake = new Snake();     // 建立蛇物件
        fruit = new Fruit();     // 建立果實物件

        // 建立定時器，每隔固定時間觸發 repaint() 更新畫面與邏輯
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();       // 觸發 Swing 重繪，會呼叫 paintComponent()
            }
        }, 0, speed);            // 初始延遲 0ms，每隔 speed 毫秒執行一次

        direction = "Right";     // 初始方向向右

        // 註冊鍵盤監聽器
        addKeyListener(this);
        setFocusable(true);            // 讓 JPanel 可取得焦點
        requestFocusInWindow();        // 請求鍵盤焦點
        allowKeyPress = true;          // 開放第一次方向變更
    }

    // 繪圖方法（Swing 框架會自動呼叫），每次 repaint() 時執行
    @Override
    public void paintComponent(Graphics g) {
        // 清除背景（填滿整個畫布）
        g.fillRect(0, 0, width, height);

        // 畫出果實與蛇
        fruit.drawFruit(g);
        snake.drawSnake(g);

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

        // 檢查蛇是否吃到果實（頭的位置與果實一致）
        if (snake.getSnakeBody().get(0).x == fruit.getX() &&
                snake.getSnakeBody().get(0).y == fruit.getY()) {
            // 1. 重新產生果實位置（不與蛇身重疊）
            fruit.setNewLocation(snake);

            // 2. 果實重畫（非必要，下一次 repaint 會畫）
            fruit.drawFruit(g);

            // 3. 不移除蛇尾，達成「變長」的效果
        } else {
            // 沒吃到果實 → 去掉尾巴 → 達成移動的效果（蛇長度不變）
            snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
        }

        // 無論是否吃到果實，都要加入新的蛇頭
        snake.getSnakeBody().add(0, newHead);

        // 重啟鍵盤允許（每次 repaint 完才能再按鍵換方向）
        allowKeyPress = true;
    }

    // 回傳畫布建議尺寸，提供 JFrame 調整用
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    // 主方法，程式進入點
    public static void main(String[] args) {
        JFrame window = new JFrame("Sanke Game");               // 建立視窗（打錯應為 Snake Game）
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 關閉視窗即結束程式
        window.setContentPane(new Main());                      // 放入我們的遊戲面板
        window.pack();                                          // 自動調整大小
        window.setLocationRelativeTo(null);                     // 視窗置中
        window.setVisible(true);                                // 顯示視窗
        window.setResizable(false);                             // 禁止改變大小
    }

    // 鍵盤輸入事件（按下某鍵時）
    @Override
    public void keyPressed(KeyEvent e) {
        // 為避免在一次移動內重複按方向鍵導致掉頭，使用 allowKeyPress 控制
        if (allowKeyPress) {
            if (e.getKeyCode() == 37 && !direction.equals("Right")) {
                direction = "Left";     // 左鍵，且當前不是右 → 可轉向
            } else if (e.getKeyCode() == 38 && !direction.equals("Down")) {
                direction = "Up";       // 上鍵，非下
            } else if (e.getKeyCode() == 39 && !direction.equals("Left")) {
                direction = "Right";    // 右鍵，非左
            } else if (e.getKeyCode() == 40 && !direction.equals("Up")) {
                direction = "Down";     // 下鍵，非上
            }

            // 禁止重複按鍵，直到下一次 repaint 結束再允許
            allowKeyPress = false;
        }
    }

    // 未使用的鍵盤事件（必要實作但可留空）
    @Override public void keyTyped(KeyEvent e) { }
    @Override public void keyReleased(KeyEvent e) { }
}
