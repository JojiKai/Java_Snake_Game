import java.awt.*;

// 果實（食物）類別，用來生成並畫出一顆果實
public class Fruit {

    // 果實的位置座標（以像素為單位）
    private int x;
    private int y;

    // 建構子：隨機產生果實的位置
    public Fruit() {
        // 隨機產生 column 欄位內的某一格，乘上 CELL_SIZE 讓它對齊格子
        this.x = (int)(Math.floor(Math.random() * Main.column) * Main.CELL_SIZE);
        this.y = (int)(Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);
    }

    // 回傳 x 座標
    public int getX() {
        return this.x;
    }

    // 回傳 y 座標
    public int getY() {
        return this.y;
    }

    // 畫出果實：用紅色圓形表示
    public void drawFruit(Graphics g) {
        g.setColor(Color.RED); // 設定畫筆顏色為紅色
        g.fillOval(this.x, this.y, Main.CELL_SIZE, Main.CELL_SIZE); // 畫出圓形果實
    }
}
