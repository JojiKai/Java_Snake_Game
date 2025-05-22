import java.awt.*; // 匯入 AWT 繪圖用的類別（Graphics、Color 等）
import java.util.ArrayList; // 匯入動態陣列類別

// 貪食蛇類別，負責管理蛇的資料與畫圖
public class Snake {

    // 儲存蛇身每一節的位置（每節是一個 Node：包含 x, y）
    private ArrayList<Node> snakeBody;

    /*
     預設的 snakeBody 結構會像這樣：
     [
       Node(x=80, y=0),  // 蛇頭
       Node(x=60, y=0),
       Node(x=40, y=0),
       Node(x=20, y=0)   // 蛇尾
     ]
     */

    // 建構子：建立初始蛇身（橫向排列）
    public Snake() {
        snakeBody = new ArrayList<>(); // 建立新的陣列用來儲存蛇身每一節

        // 預設蛇身由右至左，每節間隔一格 CELL_SIZE
        // 每一節都是一個 Node(x, y)，座標是像素單位
        snakeBody.add(new Node(80, 0));  // 蛇頭（最前面）
        snakeBody.add(new Node(60, 0));
        snakeBody.add(new Node(40, 0));
        snakeBody.add(new Node(20, 0));  // 蛇尾（最後面）
    }

    // 回傳目前蛇的身體列表（提供外部使用）
    public ArrayList<Node> getSnakeBody() {
        return snakeBody;
    }

    // 畫出蛇的所有身體節點
    public void drawSnake(Graphics g) {
        // 對 snakeBody 中的每一節進行繪圖
        for (int i = 0; i < snakeBody.size(); i++) {
            // 若是第一節（蛇頭），用黃色表示
            if (i == 0) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.GREEN); // 其他節（蛇身）用綠色
            }

            Node n = snakeBody.get(i); // 取得目前節點

            // 以下是「邊界檢查」：
            // 若超出畫布邊界，就讓它從另一側出現（環繞邏輯）

            if (n.x >= Main.width) {
                n.x = 0; // 超出右邊 → 回到最左邊
            }
            if (n.x < 0) {
                n.x = Main.width - Main.CELL_SIZE; // 超出左邊 → 回到最右一格
            }
            if (n.y >= Main.height) {
                n.y = 0; // 超出下方 → 回到最上方
            }
            if (n.y < 0) {
                n.y = Main.height - Main.CELL_SIZE; // 超出上方 → 回到底部
            }

            // 用圓形畫出每一節蛇的身體（或蛇頭）
            g.fillOval(n.x, n.y, Main.CELL_SIZE, Main.CELL_SIZE);
        }
    }
}
