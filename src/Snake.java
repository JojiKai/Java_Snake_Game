import java.awt.*;
import java.util.ArrayList;

// 貪食蛇類別，負責紀錄身體與繪製自己
public class Snake {

    // 使用 ArrayList 存放蛇的每一個節點（身體座標）
    private ArrayList<Node> snakeBody;

//    snakeBody = [
//    Node(x=80, y=0),
//    Node(x=60, y=0),
//    Node(x=40, y=0),
//    Node(x=20, y=0)
//]

    // 建構子：初始化蛇的身體（固定幾個節點，橫向排列）
    public Snake() {
        snakeBody = new ArrayList<>();

        // 預設蛇頭在右邊（從左到右排列）
        snakeBody.add(new Node(80, 0));  // 蛇頭
        snakeBody.add(new Node(60, 0));
        snakeBody.add(new Node(40, 0));
        snakeBody.add(new Node(20, 0));  // 蛇尾
    }

    public ArrayList<Node> getSnakeBody() {
        return snakeBody;
    }

    // 畫出整條蛇的方法
    public void drawSnake(Graphics g) {
        g.setColor(Color.GREEN); // 設定蛇的顏色
        for (Node n : snakeBody) {
            // 使用圓形表示每一節蛇的身體
            g.fillOval(n.x, n.y, Main.CELL_SIZE, Main.CELL_SIZE);
        }
    }
}
