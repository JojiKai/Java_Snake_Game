import javax.swing.*;
import java.awt.*;

public class Main extends JPanel {

    // 每一格的大小（像素），貪食蛇和食物會以這個單位為基礎移動
    public static final int CELL_SIZE = 20;

    // 遊戲畫面寬與高（像素）
    public static int width = 600;
    public static int height = 600;

    // 計算可以容納的行與列數（以格子計）
    public static int row = width / CELL_SIZE;
    public static int column = height / CELL_SIZE;

    // 果實的實例（由 fruit 類別管理）
    private Fruit fruit;

    // 貪食蛇的實例（由 Snake 類別管理）
    private Snake snake;  // 宣告一個snake類別成員變數

    // 建構子：初始化蛇
    public Main() {
        snake = new Snake(); // 建立一條新蛇
        fruit = new Fruit();
    }


    // 畫面重繪方法，JPanel 會在需要時自動呼叫
    @Override
    public void paintComponent(Graphics g) {
        // 填滿背景（可自行加上顏色設定）
        g.fillRect(0, 0, width, height);
        // 呼叫蛇自己的畫圖方法
        snake.drawSnake(g);
        fruit.drawFruit(g);
    }

    // 告訴 JFrame 我們這個 JPanel 希望的畫面大小
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    // 主程式入口點
    public static void main(String[] args) {
        JFrame window = new JFrame("Sanke Game"); // 建立視窗並設定標題
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 點右上角關閉即結束程式
        window.setContentPane(new Main()); // 將我們自訂的 JPanel 放進視窗中
        window.pack(); // 根據 getPreferredSize() 調整視窗大小
        window.setLocationRelativeTo(null); // 將視窗置中顯示
        window.setVisible(true); // 顯示視窗
        window.setResizable(false); // 禁止使用者改變視窗大小
    }
}
