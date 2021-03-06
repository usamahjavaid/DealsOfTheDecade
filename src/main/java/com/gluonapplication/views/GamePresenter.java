package com.gluonapplication.views;

import com.gluonapplication.GameController;
import com.gluonapplication.data.Connect;
import com.gluonapplication.model.Game;
import com.gluonapplication.model.choice.Choice;
import com.gluonapplication.model.company.GameObserver;
import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;
import static java.time.temporal.ChronoUnit.DAYS;

public class GamePresenter extends GameObserver {
    //START PAGE NODES
    private final Button startGame = new Button();
    private final Button newGame = new Button();
    private final TextField companyNameField = new TextField("Company name..");
    private final AtomicBoolean choiceMade = new AtomicBoolean(false);
    private final ToggleButton difficultyEasyToggle = new ToggleButton();
    private final ToggleButton difficultyMediumToggle = new ToggleButton();
    private final ToggleButton difficultyHardToggle = new ToggleButton();
    private final ToggleGroup difficulty = new ToggleGroup();

    //DECISION PAGE NODES
    private final Label businessDecision = new Label(),scenarioDescription = new Label(), summary = new Label(),
            gameDescription = new Label();
    private final ProgressBar progressBar = new ProgressBar();
    private final Button choiceOne = new Button(), choiceTwo = new Button(), choiceThree = new Button(),
            choiceFour = new Button(), summaryContinue = new Button();

    private final ArrayList<Control> decisionNodes = new ArrayList<>();

    private final Label endGameTextField = new Label();

    @FXML
    private View secondary;
    @FXML
    private AnchorPane anchorPane;
    //GRAPHICS

    private final ImageView cityGraphics = new ImageView();
    @FXML
    private ImageView reputationImage;
    private final ImageView companyImage = new ImageView();

    private final ImageView moonImage = new ImageView();

    private final ImageView overlay = new ImageView();
    @FXML
    private Label budgetField;
    @FXML
    private Label dateField;
    private GameController gameController;
    private Game game;

    public void initialize() {
        MobileApplication.getInstance().getGlassPane().setPrefHeight(200);
        MobileApplication.getInstance().getGlassPane().setPrefWidth(200);
        secondary.setShowTransitionFactory(BounceInRightTransition::new);
        secondary.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        MobileApplication.getInstance().getDrawer().open()));
                appBar.setTitleText("Deals of The Decade");
            }
        });
        difficulty.getToggles().add(difficultyEasyToggle);
        difficulty.getToggles().add(difficultyMediumToggle);
        difficulty.getToggles().add(difficultyHardToggle);
        constructButtonsAndLabels();
        buildStartPage();
    }

    private void buildStartPage() {

        File moonFile = new File("src/main/resources/graphics/events/regularMoon.png");
        Image imageMoon = new Image(moonFile.toURI().toString());
        moonImage.setImage(imageMoon);
        moonImage.setLayoutY(50);
        moonImage.setLayoutX(340);
        moonImage.setFitHeight(30);
        moonImage.setFitWidth(30);
        moonImage.setId("moonImage");
        moonImage.toFront();
        anchorPane.getChildren().add(moonImage);

        File fileCity = new File("src/main/resources/graphics/city/city.png");
        Image imageCity = new Image(fileCity.toURI().toString());

        dateField.toFront();
        cityGraphics.setFitHeight(249.0);
        cityGraphics.setFitWidth(350.0);
        cityGraphics.setLayoutX(29.0);
        cityGraphics.setImage(imageCity);
        cityGraphics.setPickOnBounds(true);
        cityGraphics.toBack();
        cityGraphics.setId("cityGraphics");
        anchorPane.getChildren().add(cityGraphics);

        File fileCompany = new File("src/main/resources/graphics/company/smallCompany.png");
        Image imageCompany = new Image(fileCompany.toURI().toString());
        companyImage.setLayoutX(200.);
        companyImage.setLayoutY(200);
        companyImage.setFitWidth(82.);
        companyImage.setFitHeight(43.);
        companyImage.setImage((imageCompany));
        companyImage.toFront();
        anchorPane.getChildren().add(companyImage);

        gameDescription.setText("                Welcome to Deals Of The Decade!\n\nIn this game you'll be asked to make\nsome hard hitting decisions\n\nDon't let your budget hit zero, or you lose");
        gameDescription.setLayoutX(41.5);
        gameDescription.setLayoutY(255.0);
        anchorPane.getChildren().add(gameDescription);

        difficultyEasyToggle.setLayoutY(430);
        difficultyEasyToggle.setLayoutX(41.5);
        difficultyEasyToggle.setText("Easy");
        difficultyEasyToggle.setPrefHeight(35);
        difficultyEasyToggle.setUserData("SMALL");

        difficultyMediumToggle.setLayoutY(430);
        difficultyMediumToggle.setLayoutX(170);
        difficultyMediumToggle.setText("Medium");
        difficultyMediumToggle.setPrefHeight(35);
        difficultyMediumToggle.setUserData("MEDIUM");

        difficultyHardToggle.setLayoutY(430);
        difficultyHardToggle.setLayoutX(312.5);
        difficultyHardToggle.setText("Hard");
        difficultyHardToggle.setPrefHeight(35);
        difficultyHardToggle.setUserData("HUGE");

        difficultyEasyToggle.setOnAction(e -> {
            if (difficultyEasyToggle.isSelected()) {
                difficultyEasyToggle.getStyleClass().add("selected");
                difficultyMediumToggle.getStyleClass().add("unselected");
                difficultyHardToggle.getStyleClass().add("unselected");
            } else {
                difficultyEasyToggle.getStyleClass().add("unselected");
            }
        });

        difficultyMediumToggle.setOnAction(e -> {
            if (difficultyMediumToggle.isSelected()) {
                difficultyMediumToggle.getStyleClass().add("selected");
                difficultyEasyToggle.getStyleClass().add("unselected");
                difficultyHardToggle.getStyleClass().add("unselected");
            } else {
                difficultyEasyToggle.getStyleClass().add("unselected");
            }
        });

        difficultyMediumToggle.setOnAction(e -> {
            if (difficultyMediumToggle.isSelected()) {
                difficultyMediumToggle.getStyleClass().add("selected");
                difficultyEasyToggle.getStyleClass().add("unselected");
                difficultyHardToggle.getStyleClass().add("unselected");
            } else {
                difficultyMediumToggle.getStyleClass().add("unselected");
            }
        });
        difficultyHardToggle.setOnAction(e -> {
            if (difficultyHardToggle.isSelected()) {
                difficultyHardToggle.getStyleClass().add("selected");
                difficultyEasyToggle.getStyleClass().add("unselected");
                difficultyMediumToggle.getStyleClass().add("unselected");
            } else {
                difficultyHardToggle.getStyleClass().add("unselected");
            }
        });

        anchorPane.getChildren().add(difficultyEasyToggle);
        anchorPane.getChildren().add(difficultyMediumToggle);
        anchorPane.getChildren().add(difficultyHardToggle);


        companyNameField.setLayoutY(390);
        companyNameField.setLayoutX(42.5);
        anchorPane.getChildren().add(companyNameField);

        startGame.setText("Start Game!");
        startGame.getStyleClass().add("selected");
        startGame.setLayoutX(41.5);
        startGame.setLayoutY(469.0);
        startGame.setPrefHeight(70.0);
        startGame.setPrefWidth(325.0);
        startGame.setOnAction((event) -> initializeGame());

        anchorPane.getChildren().add(startGame);
    }

    private void removeStartPage() {
        anchorPane.getChildren().remove(gameDescription);
        anchorPane.getChildren().remove(startGame);
        anchorPane.getChildren().remove(companyNameField);
        anchorPane.getChildren().remove(difficultyEasyToggle);
        anchorPane.getChildren().remove(difficultyMediumToggle);
        anchorPane.getChildren().remove(difficultyHardToggle);
        anchorPane.getChildren().remove(moonImage);
    }

    private void initializeGame() {


        budgetField.setStyle("-fx-text-fill: black");
        Toggle difficultySelectedToggle = difficulty.getSelectedToggle();

        if (difficultySelectedToggle == null) {
            gameController = new GameController(companyNameField.getText(), "SMALL");
        } else gameController = new GameController(companyNameField.getText(), (String) difficultySelectedToggle.getUserData());

        this.game = gameController.getGame();
        game.attach(this);

        removeStartPage();

        decisionNodes.add(businessDecision);
        decisionNodes.add(scenarioDescription);
        decisionNodes.add(choiceOne);
        decisionNodes.add(choiceTwo);
        decisionNodes.add(choiceThree);
        decisionNodes.add(choiceFour);
        showDecisionPage();

        update();
        spawnTimer();
        Thread t2 = new Thread(() -> {
            while (true) {
                try {
                    sleep(1000);
                    update();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t2.start();
    }




    private void constructButtonsAndLabels() {

        //Decision page nodes
        businessDecision.setLayoutX(26.0);
        businessDecision.setLayoutY(366.0);
        businessDecision.setPrefHeight(18.0);
        businessDecision.setPrefWidth(126.0);

        scenarioDescription.setLayoutX(41.5);
        scenarioDescription.setLayoutY(255.0);
        scenarioDescription.setPrefHeight(100.0);
        scenarioDescription.setPrefWidth(350.0);

        choiceOne.setId("choiceOne");
        choiceOne.setOnAction(this::makeBusinessDecision);
        choiceOne.setLayoutX(51.0);
        choiceOne.setLayoutY(388.0);
        choiceOne.setPrefHeight(70.0);
        choiceOne.setPrefWidth(139.0);

        choiceTwo.setId("choiceTwo");
        choiceTwo.setOnAction(this::makeBusinessDecision);
        choiceTwo.setLayoutX(213.0);
        choiceTwo.setLayoutY(388.0);
        choiceTwo.setPrefHeight(70.0);
        choiceTwo.setPrefWidth(139.0);

        choiceThree.setId("choiceThree");
        choiceThree.setOnAction(this::makeBusinessDecision);
        choiceThree.setLayoutX(51.0);
        choiceThree.setLayoutY(469.0);
        choiceThree.setPrefHeight(70.0);
        choiceThree.setPrefWidth(139.0);

        choiceFour.setId("choiceFour");
        choiceFour.setOnAction(this::makeBusinessDecision);
        choiceFour.setLayoutX(213.0);
        choiceFour.setLayoutY(469.0);
        choiceFour.setPrefHeight(70.0);
        choiceFour.setPrefWidth(139.0);

        progressBar.setLayoutX(51.0);
        progressBar.setLayoutY(557.0);
        progressBar.setPrefHeight(20.0);
        progressBar.setPrefWidth(303.0);
        progressBar.setProgress(0);

        //Summary page nodes
        summary.setLayoutX(41.5);
        summary.setLayoutY(255.0);
        summary.setPrefHeight(100.0);
        summary.setPrefWidth(350.0);

        summaryContinue.setOnAction(this::clickSummaryContinue);
        summaryContinue.setText("Continue");
        summaryContinue.setLayoutX(41.5);
        summaryContinue.setLayoutY(469.0);
        summaryContinue.setPrefHeight(70.0);
        summaryContinue.setPrefWidth(325.0);

        // Game Over nodes
        endGameTextField.setLayoutX(41.5);
        endGameTextField.setLayoutY(255.0);
        endGameTextField.setPrefHeight(100.0);
        endGameTextField.setPrefWidth(350.0);

        newGame.setLayoutX(41.5);
        newGame.setLayoutY(469.0);
        newGame.setPrefHeight(70.0);
        newGame.setPrefWidth(325.0);
        newGame.setOnAction(this::clickNextGame);
        newGame.setText("New game!");
    }

    public void createImageAndSet(String imageName, String nodeID, double layoutX, double layoutY, double prefWidth, double prefheight) {

        ImageView imageView = new ImageView();
        File file = new File("src/main/resources/graphics/events/" + imageName);
        javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
        imageView.setImage(image);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitWidth(prefWidth);
        imageView.setFitHeight(prefheight);
        imageView.setId(nodeID);

        anchorPane.getChildren().add(imageView);
    }

    public void createImageAndSet(String imageName, String nodeID) {

        anchorPane.getChildren().forEach(i -> {
            if (i.getId() != null) {
                if (i.getId().equals(nodeID)) {
                    ImageView imageView = (ImageView) i;
                    File file = new File("src/main/resources/graphics/events/" + imageName);
                    javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
                    imageView.setImage(image);
                }
            }
        });
    }

    private void flipChoiceMade() {
        choiceMade.set(!choiceMade.get());
    }

    public void makeDefaultBusinessDecision() {
        var game = gameController.getGame();
        var company = game.getCompany();

        game.flipIsDecisionRound();
        flipChoiceMade();
        company.makeBusinessDecision("choiceFour", game);
        removeDecisionPage();
        showSummaryPage("choiceFour");
    }

    @FXML
    public void makeBusinessDecision(Event event) {

        ///DONT DO THIS

        final Node source = (Node) event.getSource();
        var id = source.getId();
        var game = gameController.getGame();
        var company = game.getCompany();

        game.flipIsDecisionRound();
        flipChoiceMade();
        removeDecisionPage();
        showSummaryPage(id);
        Choice choice;

        switch (id) {
            case "choiceOne":
                choice = game.getCurrentScenario().getChoices().get(0);
                break;
            case "choiceTwo":
                choice = game.getCurrentScenario().getChoices().get(1);
                break;
            case "choiceThree":
                choice = game.getCurrentScenario().getChoices().get(2);
                break;
            default:
                choice = game.getCurrentScenario().getChoices().get(3);
                break;
        }

        if (choice.getImageName() != null && choice.getLayoutX() == null) {
            createImageAndSet(choice.getImageName(), choice.getNodeID());
        }
        if (choice.getImageName() != null & choice.getLayoutX() !=  null) {
            createImageAndSet(   choice.getImageName(), choice.getNodeID(),
                    choice.getLayoutX(),
                    choice.getLayoutY(),
                    choice.getPrefWidth(),
                    choice.getPrefHeight());
        }



        company.makeBusinessDecision(id, game);
    }

    public void showDecisionPage() {

        updateScenarioDescription();
        updateChoiceButtons();
        anchorPane.getChildren().add(scenarioDescription);
        anchorPane.getChildren().add(choiceOne);
        anchorPane.getChildren().add(choiceTwo);
        anchorPane.getChildren().add(choiceThree);
        anchorPane.getChildren().add(choiceFour);
    }

    private void showSummaryPage(String id) {
        Platform.runLater(() -> {
            anchorPane.getChildren().add(summary);
            anchorPane.getChildren().add(summaryContinue);
        });

        if (id.equals("choiceOne")) {
            summary.setText(getSummary(0));
            System.out.println(getSummary(0));
            System.out.println("ChoiceOne");
        }
        if (id.equals("choiceTwo")) {
            summary.setText(getSummary(1));
            System.out.println(getSummary(1));
            System.out.println("ChoiceTwo");
        }
        if (id.equals("choiceThree")) {
            summary.setText(getSummary(2));
            System.out.println(getSummary(2));
            System.out.println("ChoiceThree");
        }
        if (id.equals("choiceFour")) {
            summary.setText(getSummary(3));
            System.out.println(getSummary(3));
            System.out.println("ChoiceFour");
        }

    }

    private void showGameOverPage() {

        LocalDate startDate = LocalDate.of(2020,1,1);
        LocalDate endDate = game.getDate();

        long daysBetween = DAYS.between(startDate, endDate);
        String endGameText = "Game is over!\n The Company managed to survive until " + endDate.toString()
                + ",\n for a total of " + daysBetween + " days";

        endGameTextField.setText(endGameText);
        anchorPane.getChildren().add(endGameTextField);
        anchorPane.getChildren().add(newGame);
        saveToDB((int)daysBetween);
    }

    public void saveToDB(int days) {
        String companyName = companyNameField.getText();
        Connect.insert(companyName, days);
    }
    private void clickNextGame(ActionEvent event) {
        tearDown();
        buildStartPage();
    }

    private void tearDown() {
        flipChoiceMade();
        anchorPane.getChildren().removeIf(i -> i.getId() == null);
        anchorPane.getChildren().remove(cityGraphics);
    }

    private void removeDecisionPage() {

        Platform.runLater(() -> {
            anchorPane.getChildren().remove(businessDecision);
            anchorPane.getChildren().remove(scenarioDescription);
            anchorPane.getChildren().remove(choiceOne);
            anchorPane.getChildren().remove(choiceTwo);
            anchorPane.getChildren().remove(choiceThree);
            anchorPane.getChildren().remove(choiceFour);
            anchorPane.getChildren().remove(progressBar);
        });
    }



    public void clickSummaryContinue(Event event) {
        /* DONT DO THIS
        /* DONT DO THIS
        /* DONT DO THIS
        /* DONT DO THIS
        /* DONT DO THIS
        /* DONT DO THIS
        /* DONT DO THIS
        /* DONT DO THIS
        }*/
        checkIfVirus();
        gameController.getGame().flipIsDecisionRound();
        removeSummaryPage();

        if (game.gameDone()) {
            showGameOverPage();
            return;
        }

        for (Control node : this.decisionNodes) {

            if (!anchorPane.getChildren().contains(node)) {
                anchorPane.getChildren().add(node);
            }
        }

        flipChoiceMade();
        spawnTimer();
        updateScenarioDescription();
        updateChoiceButtons();
    }

    private void checkIfVirus() {

        if (game.getCurrentScenario().getScenarioIDForImage() != null && !checkGameDone()) {
            if (game.getCurrentScenario().getScenarioIDForImage().equals("2")) {
                File file = new File("src/main/resources/graphics/MatrixShit.png");
                Image image = new Image(file.toURI().toString());

                overlay.setLayoutX(29.);
                overlay.setFitWidth(cityGraphics.getFitWidth());
                overlay.setFitHeight(cityGraphics.getFitHeight());
                overlay.toFront();
                overlay.setImage(image);

                if (!anchorPane.getChildren().contains(overlay)) {
                    anchorPane.getChildren().add(overlay);
                }
            }
        } else {

            overlay.setImage(null);
        }
    }

    private void removeSummaryPage() {

        anchorPane.getChildren().remove(summary);
        anchorPane.getChildren().remove(summaryContinue);
        anchorPane.getChildren().remove(scenarioDescription);
    }
    private void spawnTimer() {
        var progress = new AtomicReference<>((double) 0);


        progress.set(0.);

        if (checkGameDone()) {

            return;
        }
        Thread t = new Thread(() -> {

            Platform.runLater(() -> anchorPane.getChildren().add(progressBar));

            while (true) {
                if (choiceMade.get()) {
                    System.out.println(choiceMade.get());
                    Platform.runLater(() ->
                            anchorPane.getChildren().remove(progressBar));
                    break;
                }
                try {
                    sleep(50);
                    progress.updateAndGet(v -> v + 0.005/3.0);
                    progressBar.setProgress(progress.get());

                    if (progress.get() >= 1) {
                        Platform.runLater(() ->
                                anchorPane.getChildren().remove(progressBar));
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!choiceMade.get()) {
                makeDefaultBusinessDecision();
            }
        });
        t.start();
    }

    @Override
    public void update() {

        Platform.runLater(() -> {
            updateBudgetField();
            updateRepIcon();
            updateDateField();
            updateCompanyImage();
        });
    }

    private Boolean checkGameDone() {
        return game.gameDone();
    }

    private void updateCompanyImage() {

        double sizeRatioHeight = cityGraphics.getFitHeight()/526;
        double sizeRatioWidth  = cityGraphics.getFitWidth()/526;


        if (gameController.getGame().getCompany().getBudgetConstant() <= 1.1) {

            File file = new File("src/main/resources/graphics/company/smallCompany.png");
            Image image = new Image(file.toURI().toString());
            companyImage.setImage(image);
            companyImage.setFitWidth(82. * sizeRatioWidth);
            companyImage.setFitHeight(43. * sizeRatioHeight);

        } else if (gameController.getGame().getCompany().getBudgetConstant() <= 1.2) {

            File file = new File("src/main/resources/graphics/company/smallNiceCompany.png");
            Image image = new Image(file.toURI().toString());
            companyImage.setImage(image);
            companyImage.setFitWidth(82. * sizeRatioWidth);
            companyImage.setFitHeight(43. * sizeRatioHeight);

        } else if (gameController.getGame().getCompany().getBudgetConstant() <= 1.3) {

            File file = new File("src/main/resources/graphics/company/mediumCompany.png");
            Image image = new Image(file.toURI().toString());
            companyImage.setImage(image);
            companyImage.setFitWidth(120. * sizeRatioWidth);
            companyImage.setFitHeight(74. * sizeRatioHeight);

        } else if (gameController.getGame().getCompany().getBudgetConstant() <= 1.4) {

            File file = new File("src/main/resources/graphics/company/largeCompany.png");
            Image image = new Image(file.toURI().toString());
            companyImage.setImage(image);
            companyImage.setFitWidth(120. * sizeRatioWidth);
            companyImage.setFitHeight(101. * sizeRatioHeight);
        }
    }

    private void updateDateField() {
        dateField.toFront();
        dateField.setText(gameController.getGame().getDate().toString());

    }

    private void updateRepIcon() {

        System.out.println("update rep icon");
        reputationImage.toFront();
        double reputation = gameController.getGame().getCompany().getReputation();
        if (reputation <= 1.0) {
            File file = new File("src/main/resources/graphics/reputation/veryangry.png");
            System.out.println("reputation");
            Image image = new Image(file.toURI().toString());
            reputationImage.setImage(image);
        } else if (reputation <= 2.0) {
            File file = new File("src/main/resources/graphics/reputation/angry.png");
            Image image = new Image(file.toURI().toString());
            reputationImage.setImage(image);
        } else if (reputation <= 3.0) {
            File file = new File("src/main/resources/graphics/reputation/neutral.png");
            Image image = new Image(file.toURI().toString());
            reputationImage.setImage(image);
        } else if (reputation <= 4.0) {
            File file = new File("src/main/resources/graphics/reputation/happy.png");
            Image image = new Image(file.toURI().toString());
            reputationImage.setImage(image);
        } else if (reputation <= 5.0) {
            File file = new File("src/main/resources/graphics/reputation/loved.png");
            Image image = new Image(file.toURI().toString());
            reputationImage.setImage(image);
        }
    }

    private void updateBudgetField() {
        budgetField.toFront();
        budgetField.setText(game.getCompany().getBudget().get() + "$");
    }

    private void updateScenarioDescription() {

        scenarioDescription.setText(getScenarioText());
    }

    private void updateChoiceButtons() {

        choiceOne.setText(getChoiceDescription(0));
        choiceTwo.setText(getChoiceDescription(1));
        choiceThree.setText(getChoiceDescription(2));
        choiceFour.setText(getChoiceDescription(3));
    }

    private String getScenarioText() {
        return gameController.getGame().getCurrentScenario().getScenarioText();
    }

    private String getChoiceDescription(int i) {
        return gameController.getGame().getCurrentScenario().getChoices().get(i).getDescription();
    }

    private String getSummary(int i) {
        return gameController.getGame().getCurrentScenario().getChoices().get(i).getSummary();
    }
}
