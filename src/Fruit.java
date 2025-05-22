import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

// 果實（食物）類別，用來生成並畫出一顆果實
public class Fruit {

    // 果實的位置座標（以像素為單位）
    private int x;
    private int y;

    // 用於存放果實圖片
    private BufferedImage img;

    // 建構子：隨機產生果實的位置
    public Fruit() {
        // 隨機產生 column 欄位內的某一格，乘上 CELL_SIZE 讓它對齊格子
        this.x = (int)(Math.floor(Math.random() * Main.column) * Main.CELL_SIZE);
        this.y = (int)(Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);

        try {
            // 載入圖片
            img = ImageIO.read(new File("fruit.png"));  // 圖片放在專案根目錄下
        } catch (IOException e) {
            System.out.println("無法載入圖片 fruit.png");
            e.printStackTrace();
        }
    }

    // 回傳 x 座標
    public int getX() {
        return this.x;
    }

    // 回傳 y 座標
    public int getY() {
        return this.y;
    }

    // 畫出果實：若有圖片則顯示圖片，否則用紅色圓形表示
    public void drawFruit(Graphics g) {
//        g.setColor(Color.RED); // 設定畫筆顏色為紅色
//        g.fillOval(this.x, this.y, Main.CELL_SIZE, Main.CELL_SIZE); // 畫出圓形果實
        if (img != null) {
            // 畫出圖片，並自動縮放成一格大小（20x20）
            g.drawImage(img, this.x, this.y, Main.CELL_SIZE, Main.CELL_SIZE, null);
        } else {
            // 若圖片載入失敗，仍以紅色圓形替代
            g.setColor(Color.RED);
            g.fillOval(this.x, this.y, Main.CELL_SIZE, Main.CELL_SIZE);
        }
    }

    // 當果實被吃掉時，重新產生不與蛇重疊的新位置
    public void setNewLocation(Snake s) {
        int new_x;
        int new_y;
        boolean overlapping;

        do {
            // 隨機生成新位置（以格子為單位）
            new_x = (int)(Math.floor(Math.random() * Main.column) * Main.CELL_SIZE);
            new_y = (int)(Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);

            // 檢查是否與蛇身重疊
            overlapping = check_overlap(new_x, new_y, s);

        } while (overlapping); // 若重疊則重新生成

        // 設定新位置
        this.x = new_x;
        this.y = new_y;
    }

    // 檢查 (x, y) 是否與蛇身重疊
    private boolean check_overlap(int x, int y, Snake s) {
        ArrayList<Node> snake_body = s.getSnakeBody();

        for (int j = 0; j < snake_body.size(); j++) {
            // 如果座標與蛇身任一節重疊，回傳 true
            if (x == snake_body.get(j).x && y == snake_body.get(j).y) {
                return true;
            }
        }
        // 沒有重疊，回傳 false
        return false;
    }
}
