import java.awt.*;
import java.util.ArrayList;

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
        snakeBody = new ArrayList<>();

        // 預設蛇身由右至左，每節間隔一格 CELL_SIZE
        snakeBody.add(new Node(80, 0));  // 蛇頭
        snakeBody.add(new Node(60, 0));
        snakeBody.add(new Node(40, 0));
        snakeBody.add(new Node(20, 0));  // 蛇尾
    }

    // 回傳目前蛇的身體列表（提供外部使用）
    public ArrayList<Node> getSnakeBody() {
        return snakeBody;
    }

    // 畫出蛇的所有身體節點
    public void drawSnake(Graphics g) {
        g.setColor(Color.GREEN); // 設定畫蛇的顏色
        for (Node n : snakeBody) {
            // 用圓形畫出每一節蛇的身體
            g.fillOval(n.x, n.y, Main.CELL_SIZE, Main.CELL_SIZE);
        }
    }
}
