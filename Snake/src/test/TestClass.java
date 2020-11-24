import com.snake.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.Assert;
import org.junit.Test;

public class TestClass {
    Canvas canvas = new Canvas();
    GraphicsContext gc = canvas.getGraphicsContext2D();

    @Test
    public void testMap() {
        Map map1 = new Map("src/com/snake/Maps/map1.txt");
        Assert.assertTrue(map1.drawMap(gc));
        Map map2 = new Map("src/com/snake/Maps/map2.txt");
        Assert.assertTrue(map2.drawMap(gc));
    }
}