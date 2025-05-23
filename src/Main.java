import javax.swing.*;     // 引入 Swing 視窗元件
import java.awt.*;        // 引入繪圖相關類別（Graphics, Dimension 等）
import java.awt.event.KeyEvent; // 鍵盤事件所需類別
import java.awt.event.KeyListener; // 鍵盤事件監聽器介面
import java.io.File; // 用於檔案物件的建立
import java.io.FileNotFoundException; // 處理找不到檔案的例外
import java.io.FileWriter; // 用於將資料寫入檔案
import java.io.IOException; // 處理寫檔例外
import java.util.*;       // 引入常用集合類別（如 ArrayList）
import java.util.Timer;   // Java 的排程類別 Timer，可定時執行任務

// 主程式類別，繼承 JPanel 作為畫布，實作 KeyListener 處理鍵盤控制
public class Main extends JPanel implements KeyListener {

    // 每一格的像素大小，貪食蛇和果實都以此為單位移動
    public static final int CELL_SIZE = 20;

    // 畫面尺寸（單位：像素）
    public static int width = 600;
    public static int height = 600;

    // 可容納的列與行數（以格子為單位）
    public static int row = width / CELL_SIZE;
    public static int column = height / CELL_SIZE;

    // 遊戲物件：果實與貪食蛇
    private Fruit fruit;
    private Snake snake;

    // Swing 定時器：每固定時間觸發 repaint()
    private Timer t;

    // 目前貪食蛇的移動方向（Right / Left / Up / Down）
    private static String direction;

    // 蛇移動速度，越小越快（單位：毫秒）
    private int speed = 100;

    // 控制是否允許再次按下方向鍵（防止短時間內重複輸入）
    private boolean allowKeyPress;

    private int score;             // 本次遊戲得分
    private int highest_score;     // 最高得分（從 scores.txt 讀取）
    String destkop = System.getProperty("user.home") + "/Desktop/";
    String myFile = destkop + "scores.txt";

    // 建構子：初始化畫面、遊戲物件與監聽器
    public Main() {
        read_highest_score();   // 從檔案讀取最高分
        reset();                // 初始化遊戲狀態
        addKeyListener(this);  // 加入鍵盤事件監聽器
        setFocusable(true);    // 讓 JPanel 可獲得鍵盤焦點
        requestFocusInWindow(); // 要求焦點
    }

    // 啟動計時器，固定時間呼叫 repaint() 觸發 paintComponent()
    private void setTimer() {
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint(); // 每次排程都重繪畫面，推進遊戲進度
            }
        }, 0, speed);
    }

    // 重置遊戲狀態（新一輪遊戲）
    public void reset() {
        score = 0; // 分數歸零
        if (snake != null) {
            snake.getSnakeBody().clear(); // 清除舊蛇身
        }
        allowKeyPress = true;  // 允許方向鍵輸入
        direction = "Right";  // 初始方向往右
        snake = new Snake();  // 建立新蛇
        fruit = new Fruit();  // 建立新果實
        setTimer();           // 啟動計時器
    }

    // Swing 畫布繪製方法，每次 repaint() 時被呼叫
    @Override
    public void paintComponent(Graphics g) {
        // 判斷蛇頭是否撞到自己（Game Over 條件）
        ArrayList<Node> snake_body = snake.getSnakeBody();
        Node head = snake_body.get(0);
        for (int i = 1; i < snake_body.size(); i++) {
            if (snake_body.get(i).x == head.x && snake_body.get(i).y == head.y) {
                allowKeyPress = false;
                t.cancel();  // 停止定時器
                t.purge();   // 清空排程任務

                // 顯示 Game Over 視窗與詢問是否重來
                int response = JOptionPane.showOptionDialog(
                        this,
                        "Game Over!! Your Score is (" + score + ") The Highest Score was (" + highest_score +") Would you like to start over?",
                        "Game Over!",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null, null,
                        JOptionPane.YES_OPTION
                );

                write_highest_score(score); // 如果破紀錄則寫入檔案

                switch (response) {
                    case JOptionPane.CLOSED_OPTION:
                    case JOptionPane.NO_OPTION:
                        System.exit(0); // 離開遊戲
                        break;
                    case JOptionPane.YES_OPTION:
                        reset(); // 重設遊戲
                        return;
                }
            }
        }

        g.fillRect(0, 0, width, height); // 清空背景

        fruit.drawFruit(g); // 畫出果實
        snake.drawSnake(g); // 畫出貪食蛇

        // 取得當前蛇頭位置並根據方向決定新位置
        int snakeX = head.x;
        int snakeY = head.y;
        if (direction.equals("Right")) snakeX += CELL_SIZE;
        else if (direction.equals("Left")) snakeX -= CELL_SIZE;
        else if (direction.equals("Down")) snakeY += CELL_SIZE;
        else if (direction.equals("Up")) snakeY -= CELL_SIZE;

        // 建立新蛇頭物件
        Node newHead = new Node(snakeX, snakeY);

        // 檢查是否吃到果實
        if (head.x == fruit.getX() && head.y == fruit.getY()) {
            fruit.setNewLocation(snake); // 隨機生成新果實位置
            fruit.drawFruit(g);          // 非必要，可略
            score++;                     // 分數 +1
        } else {
            snake_body.remove(snake_body.size() - 1); // 沒吃到則移除尾端一格
        }

        snake_body.add(0, newHead); // 加入新蛇頭至最前端
        allowKeyPress = true;       // 完成一次移動，允許下次輸入
    }

    // Swing 要求的 preferredSize 方法，讓畫布大小與設定一致
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    // 遊戲進入點，建立視窗並啟動遊戲
    public static void main(String[] args) {
        JFrame window = new JFrame("Snake Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new Main());
        window.pack();
        window.setLocationRelativeTo(null); // 視窗置中
        window.setVisible(true);
        window.setResizable(false);        // 禁止調整視窗大小
    }

    // 處理鍵盤輸入（方向鍵控制）
    @Override
    public void keyPressed(KeyEvent e) {
        if (allowKeyPress) {
            if (e.getKeyCode() == 37 && !direction.equals("Right")) direction = "Left";
            else if (e.getKeyCode() == 38 && !direction.equals("Down")) direction = "Up";
            else if (e.getKeyCode() == 39 && !direction.equals("Left")) direction = "Right";
            else if (e.getKeyCode() == 40 && !direction.equals("Up")) direction = "Down";
            allowKeyPress = false;
        }
    }

    // Swing 要求實作的鍵盤事件方法（未使用）
    @Override public void keyTyped(KeyEvent e) { }
    @Override public void keyReleased(KeyEvent e) { }

    // 從檔案讀取最高分數（若無檔案則建立）
    public void read_highest_score() {
        try {
            File myObj = new File(myFile);
            Scanner myReader = new Scanner(myObj);
            highest_score = myReader.nextInt();
            myReader.close();
        } catch (FileNotFoundException e) {
            highest_score = 0; // 預設為 0
            try {
                File myObj = new File(myFile);
                if (myObj.createNewFile()) {
                    System.out.println("File created : " + myObj.getName());
                }
                FileWriter myWriter = new FileWriter(myObj.getName());
                myWriter.write("" + 0); // 寫入初始值
                myWriter.close();
            } catch (IOException err) {
                System.out.println("An error occurred");
                err.printStackTrace();
            }
        }
    }

    // 寫入最高分數（若目前分數超過最高分）
    public void write_highest_score(int score) {
        try {
            FileWriter myWriter = new FileWriter(myFile);
            if (score > highest_score) {
                myWriter.write("" + score);
                highest_score = score;
            } else {
                myWriter.write("" + highest_score);
            }
            myWriter.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}
