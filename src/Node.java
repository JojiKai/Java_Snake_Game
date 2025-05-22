// Node 類別：用來表示貪食蛇身體的每一節（即一個點的座標）
public class Node {
    int x; // 節點的 x 座標（像素）
    int y; // 節點的 y 座標（像素）

    // 建構子：初始化座標
    public Node(int x, int y) {
        this.x = x; // 設定該節點的 x 座標
        this.y = y; // 設定該節點的 y 座標
    }
}
