package com.erick;

import com.erick.Game.Cell;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    private Canvas canvas;
    private Game game;
    private Thread gameThread;
    private volatile boolean isRunning = false;

    @Override
    public void start(Stage primaryStage) {
        // Wikipedia example
        // game = new Game(100, new Cell[] { new Cell(50, 50), new Cell(50, 51), new
        // Cell(50, 52), new Cell(49, 52),
        // new Cell(51, 50), new Cell(51, 49), new Cell(52, 49) });

        // Blinker
        // game = new Game(10, new Cell[]{new Cell(5, 5), new Cell(5, 6), new Cell(5,
        // 7)});

        // Beacon
        game = new Game(100, new Cell[] { new Cell(50, 50), new Cell(50, 49), new Cell(49, 50), new Cell(49, 49),
                new Cell(51, 51), new Cell(51, 52), new Cell(52, 51), new Cell(52, 52) });

        Label generationLabel = new Label("Generation: 0");

        Label speedLabel = new Label("Animation speed:");
        TextField speedInput = new TextField("1");
        speedInput.setPrefWidth(50);

        Button startButton = new Button("Start");
        Button resetButton = new Button("Reset");
        Button stopButton = new Button("Stop");
        ToolBar menuBar = new ToolBar(generationLabel, speedLabel, speedInput, startButton, stopButton,
                resetButton);

        canvas = new Canvas(600, 600);

        startButton.setOnAction(e -> {
            double animationSpeed;
            try {
                animationSpeed = Double.parseDouble(speedInput.getText());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
                return;
            }

            startButton.setDisable(true);
            stopButton.setDisable(false);

            isRunning = true;

            gameThread = new Thread(() -> {
                while (isRunning) {
                    game.calculateNextGen();

                    Platform.runLater(() -> {
                        drawGrid(game.getGrid());
                        generationLabel.setText("Generation: " + game.getGeneration());
                    });

                    try {
                        Thread.sleep(Math.round(100 / animationSpeed));
                    } catch (InterruptedException ex) {
                        break;
                    }
                }

                Platform.runLater(() -> {
                    startButton.setDisable(false);
                    stopButton.setDisable(true);
                    isRunning = false;
                });
            });

            gameThread.setDaemon(true);
            gameThread.start();
        });

        resetButton.setOnAction(e -> {
            if (isRunning) {
                isRunning = false;
            }

            game.reset();
            drawGrid(game.getGrid());
            generationLabel.setText("Generation: " + game.getGeneration());
        });

        stopButton.setOnAction(e -> {
            isRunning = false;
        });

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(canvas);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Game Of Life");
        primaryStage.setScene(scene);
        primaryStage.show();

        drawGrid(game.getGrid());
    }

    public void drawGrid(boolean[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        double cellWidth = canvas.getWidth() / cols;
        double cellHeight = canvas.getHeight() / rows;

        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (grid[r][c]) {
                    gc.setFill(Color.BLACK);
                } else {
                    gc.setFill(Color.WHITE);
                }

                gc.fillRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);

                gc.setStroke(Color.LIGHTGRAY);
                gc.strokeRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}