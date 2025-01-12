package fr.uge.view;

import fr.uge.data.util.Constants;
import fr.uge.controler.TerminalSquare;

public final class MainMenu {
  /*
   * Main menu contains, bottoms: Play and Settings (to choose number of players
   * and Wildlife Scoring Cards)
   **/
  private final int version;

  // public MainMenu() {
  // Application.run(Color.BLACK, context -> {
  // int buttonWidth = 300;
  // int buttonHeight = 300;
  // int buttonX = context.getScreenInfo().width() / 2 - buttonWidth / 2;
  // int buttonY = context.getScreenInfo().height() / 2 - buttonHeight / 2;
  // context.renderFrame(graphics -> {
  // displayVersion(context, buttonX, buttonY);
  // });
  // while (true) {
  // Event event = context.pollEvent();
  // if (event instanceof PointerEvent pointerEvent) {
  // PointerEvent.Action action = pointerEvent.action();
  // PointerEvent.Location location = pointerEvent.location();
  // checkClickVersion(context, action, location, buttonX, buttonY);
  // }}});}

  public MainMenu(int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.ILLEGAL_VERSION);
    }
    this.version = version;
    // this.numberOfPlayers = 0; // to do later
    // this.isIntermediateScoringCard = false; // to do, later, 0 is Family Card, 1
    // is Intermediate Card

    if (this.version == Constants.VERSION_SQUARE) {
      var squareGame = new TerminalSquare();
      squareGame.playSquareTerminal();
    } else if (this.version == Constants.VERSION_HEXAGONAL) {
      // playHexagonal(); // to do later
    }
    // gameLoopV2();
  }

  // sort by x and y
  // listCells.sort((cell1, cell2) -> {
  // if (cell1.getCoordinates().x() == cell2.getCoordinates().x()){
  // return cell1.getCoordinates().y() - cell2.getCoordinates().y();
  // }
  // return cell1.getCoordinates().x() - cell2.getCoordinates().x();
  // });

  // private List<Integer> calculateListOfScores(Game game){
  // Objects.requireNonNull(game);
  // var listOfPlayerScores = new ArrayList<Integer>();
  // for (var i = 0; i < game.getPlayerCount(); ++i){
  // var player = game.turnManager().getPlayerByIndex(i);
  // var score = player.calculateScore();
  // listOfPlayerScores.add(score);
  // }
  // return listOfPlayerScores;
  // }

  /**
   * 2-player game: 2 point bonus to the player with the largest habitat corridor
   * in each of the habitat types. If tied, 1 bonus point each. No bonus points
   * for second largest.
   */
  // private twoPlayerGameScoring(){
  // // to do later
  // }

  /**********************************************************
   ********************************************************** GRAPHICS ************************
   **********************************************************/
  /*
   * public void displayVersion(ApplicationContext context, int buttonX, int
   * buttonY) { context.renderFrame(graphics -> { graphics.setColor(Color.WHITE);
   * graphics.fillRect(buttonX / 2, buttonY, 300, 300); graphics.fillRect(buttonX,
   * buttonY, 300, 300); graphics.fillRect((int) (buttonX * 1.5), buttonY, 300,
   * 300); graphics.setColor(Color.BLACK); graphics.drawString("Version 1", (int)
   * (buttonX / 1.53), (int) (buttonY * 1.1));
   * graphics.drawString("With square and terminal display", (int) (buttonX /
   * 1.74), (int) (buttonY * 1.2)); graphics.setColor(Color.RED);
   * graphics.drawString("Version 2", (int) (buttonX * 1.15), (int) (buttonY *
   * 1.1)); graphics.drawString("With square and graphic display", (int) (buttonX
   * * 1.075), (int) (buttonY * 1.2)); graphics.setColor(Color.BLUE);
   * graphics.drawString("Version 3", (int) (buttonX * 1.65), (int) (buttonY *
   * 1.1)); graphics.drawString("With hexagon and graphic display", (int) (buttonX
   * * 1.57), (int) (buttonY * 1.2)); }); } public void
   * displayNumberPlayer(ApplicationContext context, int buttonX, int buttonY){
   * context.renderFrame(graphics -> { graphics.setColor(Color.WHITE);
   * graphics.fillRect(buttonX / 2, buttonY, 300, 300); graphics.fillRect(buttonX,
   * buttonY, 300, 300); graphics.fillRect((int) (buttonX * 1.5), buttonY, 300,
   * 300); graphics.setColor(Color.BLACK); graphics.drawString("2 Players", (int)
   * (buttonX / 1.53), (int) (buttonY * 1.1)); graphics.setColor(Color.RED);
   * graphics.drawString("3 Players", (int) (buttonX * 1.15), (int) (buttonY *
   * 1.1)); graphics.setColor(Color.BLUE); graphics.drawString("4 Players", (int)
   * (buttonX * 1.65), (int) (buttonY * 1.1)); }); } public void
   * displayVariantsChoice1(ApplicationContext context, int buttonX, int buttonY)
   * { context.renderFrame(graphics -> { graphics.setColor(Color.GRAY);
   * graphics.fillRect(buttonX / 2, buttonY, 300, 300);
   * graphics.setColor(Color.BLACK); graphics.drawString("Family Variants", (int)
   * (buttonX / 1.57), (int) (buttonY * 1.1)); }); } public void
   * displayVariantsChoice2(ApplicationContext context, int buttonX, int buttonY)
   * { context.renderFrame(graphics -> { graphics.setColor(Color.GRAY);
   * graphics.fillRect(buttonX, buttonY, 300, 300); graphics.setColor(Color.RED);
   * graphics.drawString("Card Variants", (int) (buttonX * 1.14), (int) (buttonY *
   * 1.1)); }); } public void displayVariantsChoice3(ApplicationContext context,
   * int buttonX, int buttonY) { context.renderFrame(graphics -> {
   * graphics.setColor(Color.GRAY); graphics.fillRect((int) (buttonX * 1.5),
   * buttonY, 300, 300); graphics.setColor(Color.WHITE);
   * graphics.drawString("Intermediate Variants", (int) (buttonX * 1.62), (int)
   * (buttonY * 1.1)); }); } public void displayCardA(ApplicationContext context,
   * int buttonX, int buttonY, WildlifeType animal) { context.renderFrame(graphics
   * -> { graphics.setColor(Color.DARK_GRAY); graphics.fillRect(buttonX / 3,
   * buttonY, 300, 300); graphics.setColor(Color.BLACK);
   * graphics.drawString("Card A", (int) (buttonX * 0.48), (int) (buttonY * 1.1));
   * makeCardMorePretty(graphics, animal); graphics.fillRect((int) (buttonX*1.01),
   * (int) (buttonY /2.5), 300, 100); graphics.setColor(Color.BLACK);
   * graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY
   * /1.9)); }); } public void displayCardB(ApplicationContext context, int
   * buttonX, int buttonY, WildlifeType animal) { context.renderFrame(graphics ->
   * { graphics.setColor(Color.DARK_GRAY); graphics.fillRect((int) (buttonX *
   * 0.8), buttonY, 300, 300); graphics.setColor(Color.RED);
   * graphics.drawString("Card B", (int) (buttonX * 0.96), (int) (buttonY * 1.1));
   * makeCardMorePretty(graphics, animal); graphics.fillRect((int) (buttonX*1.01),
   * (int) (buttonY /2.5), 300, 100); graphics.setColor(Color.BLACK);
   * graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY
   * /1.9)); }); } public void displayCardC(ApplicationContext context, int
   * buttonX, int buttonY, WildlifeType animal) { context.renderFrame(graphics ->
   * { graphics.setColor(Color.DARK_GRAY); graphics.fillRect((int) (buttonX *
   * 1.23), buttonY, 300, 300); graphics.setColor(Color.BLUE);
   * graphics.drawString("Card C", (int) (buttonX * 1.38), (int) (buttonY * 1.1));
   * makeCardMorePretty(graphics, animal); graphics.fillRect((int) (buttonX*1.01),
   * (int) (buttonY /2.5), 300, 100); graphics.setColor(Color.BLACK);
   * graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY
   * /1.9)); }); } public void displayCardD(ApplicationContext context, int
   * buttonX, int buttonY, WildlifeType animal) { context.renderFrame(graphics ->
   * { graphics.setColor(Color.DARK_GRAY); graphics.fillRect((int) (buttonX *
   * 1.7), buttonY, 300, 300); graphics.setColor(Color.WHITE);
   * graphics.drawString("Card D", (int) (buttonX * 1.86), (int) (buttonY * 1.1));
   * makeCardMorePretty(graphics, animal); graphics.fillRect((int) (buttonX*1.01),
   * (int) (buttonY /2.5), 300, 100); graphics.setColor(Color.BLACK);
   * graphics.drawString("" + animal, (int) (buttonX * 1.17), (int) (buttonY
   * /1.9)); }); } public void displayPreviousChoice(ApplicationContext context,
   * int bottomY, int bottomButtonHeight, int bottomButtonWidth, final int
   * version, ArrayList<String> cardAnimalChoice, final int playerNumber){
   * context.renderFrame(graphics -> { ArrayList<String> buttons = new
   * ArrayList<>(Arrays.asList("Version", "Salmon", "Bear", "Elk", "Hawk", "Fox",
   * "Player")); ArrayList<String> texts = new
   * ArrayList<>(Arrays.asList(String.valueOf(version),
   * cardAnimalChoice.getFirst(), cardAnimalChoice.get(1),
   * cardAnimalChoice.get(2), cardAnimalChoice.get(3),
   * cardAnimalChoice.get(4),String.valueOf(playerNumber))); for (int i = 0; i <
   * 7; i++) { int x = (int) (context.getScreenInfo().width() * (0.07 + i *
   * 0.14)); graphics.setColor(Color.DARK_GRAY); graphics.fillRect(x, bottomY,
   * bottomButtonWidth, bottomButtonHeight); graphics.setColor(Color.WHITE);
   * graphics.drawString(buttons.get(i), (int) (x + bottomButtonWidth * 0.25),
   * (int) (bottomY + bottomButtonHeight * 0.4));
   * graphics.drawString(texts.get(i), (int) (x + bottomButtonWidth * 0.25), (int)
   * (bottomY + bottomButtonHeight * 0.75)); } }); } public void
   * displayEndMenu(ApplicationContext context, int buttonX, int buttonY, final
   * int version, ArrayList<String> cardAnimalChoice, final int playerNumber) {
   * context.renderFrame(graphics -> { graphics.setColor(Color.BLACK);
   * graphics.fillRect(0, 0, context.getScreenInfo().width(),
   * context.getScreenInfo().height()); graphics.setColor(Color.GRAY);
   * graphics.fillRect(buttonX, buttonY, 300, 300);
   * graphics.setColor(Color.WHITE); graphics.drawString("Start", (int) (buttonX *
   * 1.14), (int) (buttonY * 1.1)); graphics.fillRect((int) (buttonX*1.01), (int)
   * (buttonY /2.5), 300, 100); graphics.setColor(Color.BLACK);
   * graphics.drawString("Reset", (int) (buttonX * 1.17), (int) (buttonY /1.9));
   * int bottomY = (int) (context.getScreenInfo().height() * 0.8);
   * displayPreviousChoice(context, bottomY, 120, 120, version, cardAnimalChoice,
   * playerNumber); }); } public void checkStartOrReset(ApplicationContext
   * context, PointerEvent.Action action, PointerEvent.Location location, int
   * buttonX, int buttonY, final int version, ArrayList<String> cardAnimalChoice,
   * final int playerNumber) { if (clickInRange(action, location, buttonX,
   * buttonY, 1)) { // besoin de 7 choses, type de version (square ou hexagonal) /
   * 5 card (Wildlife type ou famille ou intermediaire) / int nombre de joueurs
   * System.out.println(version); System.out.println(cardAnimalChoice.get(0));
   * System.out.println(cardAnimalChoice.get(1));
   * System.out.println(cardAnimalChoice.get(2));
   * System.out.println(cardAnimalChoice.get(3));
   * System.out.println(cardAnimalChoice.get(4));
   * System.out.println(playerNumber); } if (action ==
   * PointerEvent.Action.POINTER_UP && location.x() >= (int) (buttonX * 1.01) &&
   * location.x() <= (int) (buttonX * 1.01) + 300 && location.y() >= (int)
   * (buttonY / 2.5) && location.y() <= (int) (buttonY / 2.5) + 100) { new
   * MainMenu(); } } public boolean clickInRange(PointerEvent.Action action,
   * PointerEvent.Location location, int buttonX, int buttonY, double position){
   * return action == PointerEvent.Action.POINTER_UP && location.x() >= buttonX *
   * position && location.x() <= buttonX * position + 300 && location.y() >=
   * buttonY && location.y() <= buttonY + 300; } public void
   * checkCard(ApplicationContext context, PointerEvent.Action action,
   * PointerEvent.Location location, int buttonX, int buttonY, WildlifeType
   * animal, final int version, ArrayList<String> cardAnimalChoice) { if
   * (clickInRange(action, location, buttonX, buttonY, 0.33)) {
   * cardAnimalChoice.add("Card A"); } if (clickInRange(action, location, buttonX,
   * buttonY, 0.8)) { cardAnimalChoice.add("Card B"); } if (clickInRange(action,
   * location, buttonX, buttonY, 1.23)) { cardAnimalChoice.add("Card C"); } if
   * (clickInRange(action, location, buttonX, buttonY, 1.7)) {
   * cardAnimalChoice.add("Card D"); } switchAnimalDisplay(context, action,
   * location, buttonX, buttonY, animal, version, cardAnimalChoice); } public void
   * switchAnimalDisplay(ApplicationContext context, PointerEvent.Action action,
   * PointerEvent.Location location, int buttonX, int buttonY, WildlifeType
   * animal, final int version, ArrayList<String> cardAnimalChoice) { if (action
   * == PointerEvent.Action.POINTER_UP) { switch (animal) { case
   * WildlifeType.SALMON -> CardsBearChoiceMenu(context, buttonX, buttonY,
   * version, cardAnimalChoice); case WildlifeType.BEAR ->
   * CardsElkChoiceMenu(context, buttonX, buttonY, version, cardAnimalChoice);
   * case WildlifeType.ELK -> CardsHawkChoiceMenu(context, buttonX, buttonY,
   * version, cardAnimalChoice); case WildlifeType.HAWK ->
   * CardsFoxChoiceMenu(context, buttonX, buttonY, version, cardAnimalChoice);
   * default -> PlayersChoiceMenu(context, buttonX, buttonY, version,
   * cardAnimalChoice); } } } public void makeCardMorePretty( Graphics2D graphics,
   * WildlifeType animal) { switch (animal) { case WildlifeType.SALMON ->
   * graphics.setColor(Color.PINK); case WildlifeType.BEAR ->
   * graphics.setColor(Color.CYAN); case WildlifeType.ELK ->
   * graphics.setColor(Color.RED); case WildlifeType.HAWK ->
   * graphics.setColor(Color.BLUE); default -> graphics.setColor(Color.ORANGE); }
   * } public void checkVariants(ApplicationContext context, PointerEvent.Action
   * action, PointerEvent.Location location, int buttonX, int buttonY, boolean
   * player, final int version){ ArrayList<String> cardAnimalChoice = new
   * ArrayList<>(); if (clickInRange(action, location, buttonX, buttonY, 0.5)) {
   * for(int i=0; i<5; i++) { cardAnimalChoice.add("Families"); } if(player)
   * PlayersChoiceMenu(context, buttonX, buttonY, version, cardAnimalChoice); } if
   * (clickInRange(action, location, buttonX, buttonY, 1)) {
   * CardsSalmonChoiceMenu(context, buttonX, buttonY, version, cardAnimalChoice);
   * } if (clickInRange(action, location, buttonX, buttonY, 1.5)) { for(int i=0;
   * i<5; i++) { cardAnimalChoice.add("Intermediate"); } if(player)
   * PlayersChoiceMenu(context, buttonX, buttonY, version, cardAnimalChoice); } }
   * public void checkClickVersion(ApplicationContext context, PointerEvent.Action
   * action, PointerEvent.Location location, int buttonX, int buttonY) { if
   * (clickInRange(action, location, buttonX, buttonY, 0.5)) {
   * VariantChoiceMenuVersion1And2(context, action, location,buttonX,buttonY, 1);
   * } if (clickInRange(action, location, buttonX, buttonY, 1)) {
   * VariantChoiceMenuVersion1And2(context, action, location,buttonX,buttonY, 2);
   * } if (clickInRange(action, location, buttonX, buttonY, 1.5)) {
   * VariantChoiceMenuVersion3(context, buttonX,buttonY, 3); } } public void
   * checkClickPlayer(ApplicationContext context, PointerEvent.Action action,
   * PointerEvent.Location location, int buttonX, int buttonY, final int version,
   * ArrayList<String> cardAnimalChoice) { if (clickInRange(action, location,
   * buttonX, buttonY, 0.5)) { StartOrResetMenu(context, buttonX, buttonY,
   * version, cardAnimalChoice, NB_PLAYERS_SQUARE); // must do this call in a if,
   * otherwise, it makes problem } if (clickInRange(action, location, buttonX,
   * buttonY, 1)) { StartOrResetMenu(context, buttonX, buttonY, version,
   * cardAnimalChoice, 3); } if (clickInRange(action, location, buttonX, buttonY,
   * 1.5)) { StartOrResetMenu(context, buttonX, buttonY, version,
   * cardAnimalChoice, 4); } } public void
   * VariantChoiceMenuVersion1And2(ApplicationContext context, PointerEvent.Action
   * action, PointerEvent.Location location, int buttonX, int buttonY, final int
   * version) { context.renderFrame(graphics -> { graphics.setColor(Color.BLACK);
   * graphics.fillRect(0, 0, context.getScreenInfo().width(),
   * context.getScreenInfo().height()); displayVariantsChoice1(context, buttonX,
   * buttonY); displayVariantsChoice3(context, buttonX, buttonY); }); while (true)
   * { Event event = context.pollEvent(); if (event instanceof PointerEvent
   * pointerEvent) { PointerEvent.Action action2 = pointerEvent.action();
   * PointerEvent.Location location2 = pointerEvent.location();
   * checkVariants(context, action2, location2,buttonX,buttonY, false, version);
   * }}} public void VariantChoiceMenuVersion3(ApplicationContext context, int
   * buttonX, int buttonY, final int version){ context.renderFrame(graphics -> {
   * graphics.setColor(Color.BLACK); graphics.fillRect(0, 0,
   * context.getScreenInfo().width(), context.getScreenInfo().height());
   * displayVariantsChoice1(context, buttonX, buttonY);
   * displayVariantsChoice2(context, buttonX, buttonY);
   * displayVariantsChoice3(context, buttonX, buttonY); }); while (true) { Event
   * event = context.pollEvent(); if (event instanceof PointerEvent pointerEvent)
   * { PointerEvent.Action action2 = pointerEvent.action(); PointerEvent.Location
   * location2 = pointerEvent.location(); checkVariants(context, action2,
   * location2,buttonX,buttonY, true, version); }}} public void
   * StartOrResetMenu(ApplicationContext context, int buttonX, int buttonY, final
   * int version, ArrayList<String> cardAnimalChoice, final int playerNumber){
   * context.renderFrame(graphics -> { graphics.setColor(Color.BLACK);
   * graphics.fillRect(0, 0, context.getScreenInfo().width(),
   * context.getScreenInfo().height()); displayEndMenu(context, buttonX, buttonY,
   * version, cardAnimalChoice, playerNumber); }); while (true) { Event event =
   * context.pollEvent(); if (event instanceof PointerEvent pointerEvent) {
   * PointerEvent.Action action5 = pointerEvent.action(); PointerEvent.Location
   * location5 = pointerEvent.location(); checkStartOrReset(context, action5,
   * location5, buttonX, buttonY, version, cardAnimalChoice, playerNumber); }}}
   * public void PlayersChoiceMenu(ApplicationContext context, int buttonX, int
   * buttonY, final int version, ArrayList<String> cardAnimalChoice){
   * context.renderFrame(graphics -> { graphics.setColor(Color.BLACK);
   * graphics.fillRect(0, 0, context.getScreenInfo().width(),
   * context.getScreenInfo().height()); displayNumberPlayer(context, buttonX,
   * buttonY); }); while (true) { Event event = context.pollEvent(); if (event
   * instanceof PointerEvent pointerEvent) { PointerEvent.Action action3 =
   * pointerEvent.action(); PointerEvent.Location location3 =
   * pointerEvent.location(); checkClickPlayer(context, action3,
   * location3,buttonX,buttonY, version, cardAnimalChoice); }}} public void
   * CardsSalmonChoiceMenu(ApplicationContext context, int buttonX, int buttonY,
   * final int version, ArrayList<String> cardAnimalChoice){
   * context.renderFrame(graphics -> { graphics.setColor(Color.BLACK);
   * graphics.fillRect(0, 0, context.getScreenInfo().width(),
   * context.getScreenInfo().height()); displayCardA(context, buttonX, buttonY,
   * WildlifeType.SALMON); displayCardB(context, buttonX, buttonY,
   * WildlifeType.SALMON); displayCardC(context, buttonX, buttonY,
   * WildlifeType.SALMON); displayCardD(context, buttonX, buttonY,
   * WildlifeType.SALMON); }); while (true) { Event event = context.pollEvent();
   * if (event instanceof PointerEvent pointerEvent) { PointerEvent.Action action4
   * = pointerEvent.action(); PointerEvent.Location location4 =
   * pointerEvent.location(); checkCard(context, action4,
   * location4,buttonX,buttonY, WildlifeType.SALMON, version, cardAnimalChoice);
   * }}} public void CardsBearChoiceMenu(ApplicationContext context, int buttonX,
   * int buttonY, final int version, ArrayList<String> cardAnimalChoice){
   * context.renderFrame(graphics -> { graphics.setColor(Color.BLACK);
   * graphics.fillRect(0, 0, context.getScreenInfo().width(),
   * context.getScreenInfo().height()); displayCardA(context, buttonX, buttonY,
   * WildlifeType.BEAR); displayCardB(context, buttonX, buttonY,
   * WildlifeType.BEAR); displayCardC(context, buttonX, buttonY,
   * WildlifeType.BEAR); displayCardD(context, buttonX, buttonY,
   * WildlifeType.BEAR); }); while (true) { Event event = context.pollEvent(); if
   * (event instanceof PointerEvent pointerEvent) { PointerEvent.Action action4 =
   * pointerEvent.action(); PointerEvent.Location location4 =
   * pointerEvent.location(); checkCard(context, action4,
   * location4,buttonX,buttonY, WildlifeType.BEAR, version, cardAnimalChoice); }}}
   * public void CardsElkChoiceMenu(ApplicationContext context, int buttonX, int
   * buttonY, final int version, ArrayList<String> cardAnimalChoice){
   * context.renderFrame(graphics -> { graphics.setColor(Color.BLACK);
   * graphics.fillRect(0, 0, context.getScreenInfo().width(),
   * context.getScreenInfo().height()); displayCardA(context, buttonX, buttonY,
   * WildlifeType.ELK); displayCardB(context, buttonX, buttonY, WildlifeType.ELK);
   * displayCardC(context, buttonX, buttonY, WildlifeType.ELK);
   * displayCardD(context, buttonX, buttonY, WildlifeType.ELK); }); while (true) {
   * Event event = context.pollEvent(); if (event instanceof PointerEvent
   * pointerEvent) { PointerEvent.Action action4 = pointerEvent.action();
   * PointerEvent.Location location4 = pointerEvent.location(); checkCard(context,
   * action4, location4,buttonX,buttonY, WildlifeType.ELK, version,
   * cardAnimalChoice); }}} public void CardsHawkChoiceMenu(ApplicationContext
   * context, int buttonX, int buttonY, final int version, ArrayList<String>
   * cardAnimalChoice){ context.renderFrame(graphics -> {
   * graphics.setColor(Color.BLACK); graphics.fillRect(0, 0,
   * context.getScreenInfo().width(), context.getScreenInfo().height());
   * displayCardA(context, buttonX, buttonY, WildlifeType.HAWK);
   * displayCardB(context, buttonX, buttonY, WildlifeType.HAWK);
   * displayCardC(context, buttonX, buttonY, WildlifeType.HAWK);
   * displayCardD(context, buttonX, buttonY, WildlifeType.HAWK); }); while (true)
   * { Event event = context.pollEvent(); if (event instanceof PointerEvent
   * pointerEvent) { PointerEvent.Action action4 = pointerEvent.action();
   * PointerEvent.Location location4 = pointerEvent.location(); checkCard(context,
   * action4, location4,buttonX,buttonY, WildlifeType.HAWK, version,
   * cardAnimalChoice); }}} public void CardsFoxChoiceMenu(ApplicationContext
   * context, int buttonX, int buttonY, final int version, ArrayList<String>
   * cardAnimalChoice){ context.renderFrame(graphics -> {
   * graphics.setColor(Color.BLACK); graphics.fillRect(0, 0,
   * context.getScreenInfo().width(), context.getScreenInfo().height());
   * displayCardA(context, buttonX, buttonY, WildlifeType.FOX);
   * displayCardB(context, buttonX, buttonY, WildlifeType.FOX);
   * displayCardC(context, buttonX, buttonY, WildlifeType.FOX);
   * displayCardD(context, buttonX, buttonY, WildlifeType.FOX); }); while (true) {
   * Event event = context.pollEvent(); if (event instanceof PointerEvent
   * pointerEvent) { PointerEvent.Action action4 = pointerEvent.action();
   * PointerEvent.Location location4 = pointerEvent.location(); checkCard(context,
   * action4, location4,buttonX,buttonY, WildlifeType.FOX, version,
   * cardAnimalChoice); }}} private void gameLoopV2() {
   * Application.run(Color.BLACK, context -> { int buttonX =
   * context.getScreenInfo().width() / 2 - buttonWidth / 2; int buttonY =
   * context.getScreenInfo().height() / 2 - buttonHeight / 2;
   * context.renderFrame(graphics -> { displayVersion(context, buttonX, buttonY);
   * }); while (true) { Event event = context.pollEvent(); if (event instanceof
   * PointerEvent pointerEvent) { PointerEvent.Action action =
   * pointerEvent.action(); PointerEvent.Location location =
   * pointerEvent.location(); checkClickVersion(context, action, location,
   * buttonX, buttonY); }}}); } public static void main(String[] args) { new
   * MainMenu().gameLoopV2(); }
   */

}
