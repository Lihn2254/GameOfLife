package com.erick;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {
    private Game game;
    private Thread gameThread;
    private volatile boolean isRunning = false;
    private Scene scene;
    private Canvas canvas;
    private Label generationLabel, speedLabel, presetLabel;
    private TextField speedInput;
    private Button startButton, resetButton, stopButton, cleanButton;
    private ComboBox<String> presetSelector;
    private ToolBar menuBar;
    private BorderPane root;
    private Alert alert;
    private HBox speedBox, actionButtons, presetBox;

    @Override
    public void start(Stage stage) {
        Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
        stage.getIcons().add(icon);
        game = new Game(80);

        generationLabel = new Label("Generation: 0");

        int itemsSpacing = 5;
        Pos position = Pos.CENTER_LEFT;

        speedLabel = new Label("Animation speed:");
        speedInput = new TextField("1");
        speedInput.setPrefWidth(50);
        speedBox = new HBox(speedLabel, speedInput);
        speedBox.setAlignment(position);
        speedBox.setSpacing(itemsSpacing);

        startButton = new Button("Start");
        resetButton = new Button("Reset");
        stopButton = new Button("Stop");
        cleanButton = new Button("Clean grid");
        actionButtons = new HBox(startButton, resetButton, stopButton, cleanButton);
        actionButtons.setAlignment(position);
        actionButtons.setSpacing(itemsSpacing);
        
        presetLabel = new Label("Load preset:");
        presetSelector = new ComboBox<String>();
        presetSelector.getItems().addAll(PresetRegistry.getPresetNames());
        presetBox = new HBox(presetLabel, presetSelector);
        presetBox.setAlignment(position);
        
        menuBar = new ToolBar(generationLabel, speedBox, actionButtons, presetLabel, presetSelector);
        menuBar.setStyle("-fx-spacing: 15px;");

        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Preset Error");

        canvas = new Canvas(700, 700);

        canvas.setOnMouseClicked(event -> {
            boolean[][] currentGrid = game.getGrid();
            int rows = currentGrid.length;
            int cols = currentGrid[0].length;

            double cellWidth = canvas.getWidth() / cols;
            double cellHeight = canvas.getHeight() / rows;

            int clickedCol = (int) (event.getX() / cellWidth);
            int clickedRow = (int) (event.getY() / cellHeight);

            game.toggleCell(clickedRow, clickedCol);

            drawGrid(game.getGrid());
        });

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
            cleanButton.setDisable(true);

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
                    cleanButton.setDisable(false);
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

            try {
                game.reset();
                drawGrid(game.getGrid());
                generationLabel.setText("Generation: " + game.getGeneration());
            } catch (Exception ex) {
                alert.setHeaderText("Failed to reset the game");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });

        stopButton.setOnAction(e -> {
            isRunning = false;
        });

        cleanButton.setOnAction(e -> {
            try {
                game.setBlankGrid();
                drawGrid(game.getGrid());
                presetSelector.setValue(null);
                generationLabel.setText("Generation: " + game.getGeneration());
            } catch (Exception ex) {
                alert.setHeaderText("Failed clean the grid");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });
        
        presetSelector.setOnAction(e -> {
            if (presetSelector.getValue() == null) {
                return; 
            }

            try {
                game.loadPreset(presetSelector.getValue());
                game.reset();
                drawGrid(game.getGrid());
            } catch (Exception ex) {
                alert.setHeaderText("Preset not found");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });

        root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(canvas);

        scene = new Scene(root);
        stage.setTitle("Game Of Life");
        stage.setScene(scene);
        stage.show();

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