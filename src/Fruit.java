import javax.swing.*; // 匯入 Swing 視窗元件（如 ImageIcon）
import java.awt.*; // 匯入繪圖用類別（Graphics、Color 等）
import java.awt.image.BufferedImage; // 用來儲存圖片的緩衝影像類別
import java.io.IOException; // 處理圖片讀取錯誤
import java.util.ArrayList; // 用於儲存蛇身體節點
import javax.imageio.ImageIO; // 用於載入圖片的工具類別

// 果實（食物）類別，用來生成並畫出一顆果實
public class Fruit {

    // 果實的位置座標（以像素為單位）
    private int x; // 水平位置
    private int y; // 垂直位置

    // 用於存放果實圖片（BufferedImage 可以用來畫在 JPanel 上）
    private BufferedImage img;

    // 建構子：隨機產生果實的位置，並嘗試載入圖片
    public Fruit() {
        // 隨機產生 column 欄位內的某一格，乘上 CELL_SIZE 讓它對齊格子
        this.x = (int)(Math.floor(Math.random() * Main.column) * Main.CELL_SIZE);
        this.y = (int)(Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);

        try {
            // 正確方式：從 classpath 根目錄載入 fruit.png 為 BufferedImage
            // 圖片必須放在 src 根目錄，否則會出現 NullPointerException
            img = ImageIO.read(getClass().getResource("/fruit.png"));
        } catch (IOException e) {
            // 若圖片載入失敗，顯示錯誤訊息並印出堆疊追蹤
            System.out.println("無法載入圖片 fruit.png");
            e.printStackTrace();
        }
    }

    // 回傳 x 座標（果實的水平位置）
    public int getX() {
        return this.x;
    }

    // 回傳 y 座標（果實的垂直位置）
    public int getY() {
        return this.y;
    }

    // 繪製果實：若圖片存在則畫圖，否則畫紅色圓形
    public void drawFruit(Graphics g) {
        if (img != null) {
            // 畫出圖片，並自動縮放成一格大小（CELL_SIZE x CELL_SIZE）
            g.drawImage(img, this.x, this.y, Main.CELL_SIZE, Main.CELL_SIZE, null);
        } else {
            // 若圖片載入失敗，仍以紅色圓形表示果實位置
            g.setColor(Color.RED);
            g.fillOval(this.x, this.y, Main.CELL_SIZE, Main.CELL_SIZE);
        }
    }

    // 當果實被吃掉時，重新產生新位置，避免與蛇身重疊
    public void setNewLocation(Snake s) {
        int new_x; // 新的 x 座標
        int new_y; // 新的 y 座標
        boolean overlapping; // 是否與蛇重疊的旗標

        do {
            // 隨機生成新位置（以格子為單位）
            new_x = (int)(Math.floor(Math.random() * Main.column) * Main.CELL_SIZE);
            new_y = (int)(Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);

            // 檢查是否與蛇身重疊
            overlapping = check_overlap(new_x, new_y, s);

        } while (overlapping); // 若重疊則重新生成

        // 設定新的果實位置
        this.x = new_x;
        this.y = new_y;
    }

    // 檢查新位置 (x, y) 是否與蛇身的任一節重疊
    private boolean check_overlap(int x, int y, Snake s) {
        ArrayList<Node> snake_body = s.getSnakeBody(); // 取得蛇身節點

        for (int j = 0; j < snake_body.size(); j++) {
            // 如果位置重疊（座標相同），回傳 true
            if (x == snake_body.get(j).x && y == snake_body.get(j).y) {
                return true;
            }
        }
        // 無任何重疊，回傳 false
        return false;
    }
}
