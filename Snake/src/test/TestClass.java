import com.snake.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
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

    @Test
    public void testRecords() throws InterruptedException {
        Thread thread = new Thread(() -> {
            new JFXPanel(); //инициализация JavaFx Platform
            Platform.runLater(() -> {
                try {
                    Main main = new Main();
                    main.start(new Stage());
                    Menu menu = new Menu(main.getStage());
                    Records records1 = new Records(menu, new Settings(menu), new Group(), 10000);
                    Assert.assertTrue(records1.setPlayerName("x"));
                    new Records(menu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        thread.start();
        Thread.sleep(5000); //время на тестирование
    }

    @Test
    public void testFruitBonusSnake() throws InterruptedException {
        Thread thread = new Thread(() -> {
            new JFXPanel();
            Platform.runLater(() -> {
                try {
                    Main main = new Main();
                    main.start(new Stage());
                    Menu menu = new Menu(main.getStage());
                    Fruit fruit = new Fruit(gc);
                    Map map1 = new Map("src/com/snake/Maps/map1.txt");
                    map1.drawMap(gc);
                    Snake snake = new Snake(gc, 256, 256, map1.getTileMap(), new Settings(menu));
                    Assert.assertNotNull(snake);
                    fruit.generateCoords(snake, null);
                    Assert.assertTrue(fruit.compareCoords(snake));
                    Bonus bonus = new Bonus(gc);
                    bonus.generateCoords(snake, null, fruit);
                    Assert.assertTrue(bonus.compareCoords(snake));
                    snake.eat(fruit, null);
                    snake.eat(null, bonus);
                    Assert.assertEquals(3, snake.getFruitsEaten());
                    snake.grow();
                    Assert.assertEquals(4, snake.getSnakeSize());
                    snake.setDirection("up");
                    snake.move(null);
                    Assert.assertEquals(snake.getX(0), 256);
                    Assert.assertEquals(snake.getY(0), 240);
                    snake.setDirection("right");
                    snake.move(null);
                    Assert.assertEquals(snake.getX(0), 272);
                    Assert.assertEquals(snake.getY(0), 240);
                    snake.setDirection("down");
                    snake.move(null);
                    Assert.assertEquals(snake.getX(0), 272);
                    Assert.assertEquals(snake.getY(0), 256);
                    snake.setDirection("left");
                    snake.move(null);
                    Assert.assertEquals(snake.getX(0), 256);
                    Assert.assertEquals(snake.getY(0), 256);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        thread.start();
        Thread.sleep(1000);
    }
}