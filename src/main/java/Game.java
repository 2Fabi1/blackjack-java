import Enums.CardRank;
import Enums.CardSuit;
import Gamepad.gamepadListener;
import Gamepad.gamepadManagerWithListener;
import Karty.Talia;
import Karty.karta;
import Panele.*;
import Rece.DealerHand;
import Rece.Hand;
import Rece.PlayerHand;
import Rece.Reka;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.Random;

public class Game {
    private gamePanel gamePanel;
    private Talia talia;
    private Hand gracz;
    private Hand dealer;
    private Stol stol;
    private rekaPanel rekaGracza;
    private rekaPanel rekaDealera;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width;
    int height = screenSize.height;
    final double handWidth = width / 1.92;
    final double handHeight = height / 5.6;
    private int chips = 10000;
    private int bet = 0;
    private boolean holeRevealed = false;
    private Hand gracz2 = null;        // split fields
    private boolean isSplitActive = false;
    private boolean playingFirstHand = true;
    private rekaPanel rekaGracza2;
    private boolean isBusy = false;
    private boolean didHit = false;
    private boolean didStand = false;
    private boolean didDouble = false;
    private dimPanel disabled;
    private JLayer<JComponent> dimLayer;
    Rectangle playerHandBounds1 = new Rectangle((int) (width-handWidth/2), (int)(0.7 * height), (int) handWidth,  (int) handHeight);
    Rectangle playerHandBounds2 = new Rectangle((int) ((width-handWidth)/2), (int)(0.7 * height), (int) handWidth / 3,  (int) handHeight);
    Rectangle dealerHandBounds = new Rectangle((int) ((width-handWidth)/2), 30, (int) handWidth, (int) handHeight);

    public void start() {
        talia = new Talia();
        gracz = new PlayerHand();
        dealer = new DealerHand();

        gamePanel = new gamePanel();
        stol.setContentPane(gamePanel);

        askForBet();
        setupGame();
        setupUI();
        setupActions();

        gamePanel.revalidate();
        gamePanel.repaint();
        gamePanel.requestFocusInWindow();
    }
    private void restart(){
        talia = new Talia();
        gracz = new PlayerHand();
        dealer = new DealerHand();
        isSplitActive = false;
        playingFirstHand = true;
        gracz2 = null;
        rekaGracza2 = null;

        System.out.println("RESTART!");

        gamePanel.resetHands();

        askForBet();
        setupGame();
        setupUI();

        gamePanel.hitButton.setEnabled(true);
        gamePanel.standButton.setEnabled(true);
        gamePanel.doubleButton.setEnabled(true);
        gamePanel.splitButton.setEnabled(canSplit());
    }

    private void setupGame() {
        gracz.dobierz(talia.dobierzKarte());
        dealer.dobierz(talia.dobierzKarte());
        gracz.dobierz(talia.dobierzKarte());
        dealer.dobierz(talia.dobierzKarte());
        holeRevealed = false;

        if (gracz.policzWynik() == 21){
            JOptionPane.showMessageDialog(null, "Blackjack!");
            resolveGame();
        }
    }

    private void setupUI() {
        rekaGracza = new rekaPanel(gracz.getReka());
        Reka rekaD = new Reka();
        rekaD.dobierz(dealer.getReka().getKarta(0));
        rekaD.dobierz(new karta(CardRank.EMPTY, CardSuit.EMPTY));
        rekaDealera = new rekaPanel(rekaD);
        rekaGracza.setBounds(playerHandBounds1);
        gamePanel.addHand(rekaGracza);

        rekaDealera.setBounds(dealerHandBounds);
        gamePanel.addHand(rekaDealera);
    }

    private boolean canSplit() {
        Reka r = gracz.getReka();
        return r.getKarty().size() == 2
                && r.getKarta(0).getRank() == r.getKarta(1).getRank()
                && chips >= bet;
    }

    private void setupActions() {
        gamePanel.hitButton.addActionListener(e -> hit());
        gamePanel.standButton.addActionListener(e -> stand());
        gamePanel.doubleButton.addActionListener(e -> doubleDown());
        gamePanel.splitButton.addActionListener(e -> split());
        gamePanel.plusButton.addActionListener(e -> {
            chips += 10;
            gamePanel.chPanel.setChips(chips);
        });


        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_H -> hit();
                    case KeyEvent.VK_S -> stand();
                    case KeyEvent.VK_D -> doubleDown();
                    case KeyEvent.VK_P -> split();
                }
            }
        });

        gamepadListener listener = new gamepadListener() {
            @Override
            public void onAPressed() { hit(); }
            @Override
            public void onBPressed() { stand(); }
            @Override
            public void onXPressed() { doubleDown(); }
            @Override
            public void onYPressed() { split(); }
        };

        gamepadManagerWithListener manager = new gamepadManagerWithListener(listener);
        manager.startPolling();
        Runtime.getRuntime().addShutdownHook(new Thread(manager::stop));


        ControllerManager controllers = manager.getControllers();
        ControllerState currState = controllers.getState(0);

        if(!currState.isConnected) {
            manager.stop();
        }

        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
    }

    private void hit() {
        if (isBusy || didDouble) return;   // prevent spamming hit button
        isBusy = true;
        didHit = true;

        karta nowa = talia.dobierzKarte();
        gamePanel.doubleButton.setEnabled(false);
        gamePanel.splitButton.setEnabled(false);
        if (isSplitActive && playingFirstHand)
            animateCard(rekaGracza, gracz, nowa, () -> {
                if (gracz.policzWynik() > 21) {
                    switchToSecondHand();
                }
                isBusy = false;
            });
        else if (isSplitActive)
            animateCard(rekaGracza2, gracz2, nowa, () -> {
                if (gracz2.policzWynik() > 21) {
                    dealerTurn();
                }
                isBusy = false;
            });
        else {
            playerTurn();
        }
    }

    private void stand() {
        if (didStand || didDouble){
            return;
        }

        didStand = true;
        gamePanel.doubleButton.setEnabled(false);
        gamePanel.splitButton.setEnabled(false);
        if (isSplitActive && playingFirstHand)
            switchToSecondHand();
        else {
            gamePanel.hitButton.setEnabled(false);
            gamePanel.standButton.setEnabled(false);
            gamePanel.doubleButton.setEnabled(false);
            gamePanel.splitButton.setEnabled(false);
            dealerTurn();
        }
    }

    private void switchToSecondHand() {
        playingFirstHand = false;
        rekaGracza.setBounds(playerHandBounds2);
        rekaGracza2.setBounds(playerHandBounds1);
    }

    private void playerTurn() {
        karta nowa = talia.dobierzKarte();
        gamePanel.hitButton.setEnabled(false);
        animateCard(rekaGracza,  gracz, nowa, () -> {
            if (gracz.policzWynik() > 21) {
                JOptionPane.showMessageDialog(null, "Przegrałeś!");
                gamePanel.chPanel.setChips(chips);
                gamePanel.pPanel.setChips(0);
                gracz = null;
                restart();
            } else {
                isBusy = false;
                gamePanel.hitButton.setEnabled(true);
            }
        });
    }

    private void dealerTurn() {
        // If hole card hasn't been revealed, reveal it
        if (!holeRevealed) {
            holeRevealed = true;
            Timer t = new Timer(450, e -> {
                revealHoleCard();
            });
            t.setRepeats(false);
            t.start();
        } else {
            // Now check if the dealer needs to draw or stand
            int dealerScore = dealer.policzWynik();
            if (dealer.shouldDraw(gracz.policzWynik())) {
                // Dealer should draw a card
                drawCardForDealer();
            } else {
                // Dealer stands, resolve game
                resolveGame();
            }
        }
    }

    private void revealHoleCard() {
        // Reveal the hole card using animation
        karta holeCard = talia.dobierzKarte();

        // Remove the placeholder card from rekaDealera
        rekaDealera.getReka().removeKarta(1);

        // Add the actual hole card to rekaDealera (since it's their hand, not dealer's directly)
        rekaDealera.getReka().dobierz(holeCard);

        // Update the dealer's score (assumed 'dealer' should reflect the actual hand)
        dealer.getReka().removeKarta(1);
        dealer.dobierz(holeCard);  // Only add it to the dealer's hand here

        rekaDealera.odswiez();  // Refresh the hand for display or update
        System.out.println("Dealer's hand: " + rekaDealera.getReka().policzWynik());
        System.out.println(dealer.getReka());
        System.out.println("Dealer score before action: " + dealer.policzWynik());
        System.out.println("Hole card: " + holeCard);

        dealerTurn();  // Continue dealer's turn
    }

    private void drawCardForDealer() {
        // Animate drawing of a new card for the dealer
        karta nowa = talia.dobierzKarte();
        animateCard(rekaDealera, dealer, nowa, () -> {
            rekaDealera.dobierz(nowa);
            dealerTurn();  // Continue dealer's turn after card animation
        });
    }

    private void doubleDown(){
        if(didHit || didStand){
            return;
        }
        if (bet <= chips) {
            chips -= bet;
            bet *= 2;

            gamePanel.chPanel.setChips(chips);
            gamePanel.pPanel.setChips(bet);

            gamePanel.hitButton.setEnabled(false);
            gamePanel.standButton.setEnabled(false);
            gamePanel.doubleButton.setEnabled(false);
            hit();
            stand();

            didDouble = true;
        } else {
            JOptionPane.showMessageDialog(null, "Nie stać cie biedaku!! Masz: " + chips + "$");
        }
    }

    private void split() {
        chips -= bet;
        isSplitActive = true;
        playingFirstHand = true;
        didHit = false;
        didStand = false;
        didDouble = false;

        gracz2 = new PlayerHand();

        // Move second card to second hand
        karta secondCard = gracz.getReka().getKarta(1);
        gracz.getReka().removeKarta(1);
        gracz2.dobierz(secondCard);

        // Deal one new card to each hand
        gracz.dobierz(talia.dobierzKarte());
        gracz2.dobierz(talia.dobierzKarte());

        // Render second hand panel
        rekaGracza2 = new rekaPanel(gracz2.getReka());
        rekaGracza2.setBounds(playerHandBounds2);            // position it beside the first hand
        disabled = new dimPanel();
        dimLayer = new JLayer<>(rekaGracza2, disabled);
        dimLayer.setBounds(playerHandBounds2);

        gamePanel.add(dimLayer);
        gamePanel.addHand(rekaGracza2);

        rekaGracza.odswiez();
        gamePanel.splitButton.setEnabled(false);
    }

    private void resolveGame() {
        int dealerScore = dealer.policzWynik();
        resolveHand(gracz.policzWynik(), dealerScore, "Ręka 1");
        if (isSplitActive)
            resolveHand(gracz2.policzWynik(), dealerScore, "Ręka 2");
        gamePanel.chPanel.setChips(chips);
        gamePanel.pPanel.setChips(0);
        didStand = false;
        didDouble = false;

        restart();
    }

    private void resolveHand(int playerScore, int dealerScore, String label) {
        if (dealerScore > 21) {
            // Dealer busts
            chips += 2 * bet;
            JOptionPane.showMessageDialog(null, (isSplitActive ? label + ":" : "") + "Wygrałeś! Dealer przekroczył 21.");
        } else if (playerScore > 21) {
            // Player busts
            JOptionPane.showMessageDialog(null, (isSplitActive ? label + ":" : "") + "Przegrałeś! Ty przekroczyłeś 21.");
        } else if (playerScore > dealerScore && (playerScore != 21 && rekaGracza.getReka().getKarty().size() != 2)) {
            // Player wins with a higher score
            chips += 2 * bet;
            JOptionPane.showMessageDialog(null, (isSplitActive ? label + ":" : "") + "Wygrałeś!");
        } else if (playerScore == dealerScore) {
            // Draw (push)
            chips += bet;
            JOptionPane.showMessageDialog(null, (isSplitActive ? label + ":" : "") + "Remis!");
        } else {
            // Dealer wins with a higher score
            JOptionPane.showMessageDialog(null, (isSplitActive ? label + ":" : "") + "Przegrałeś! Dealer wygrał.");
        }
    }
    public void showMainMenu() {
        if (stol == null) {
            stol = new Stol();
        }

        mainMenuPanel menu = new mainMenuPanel(this::start, chips);

        stol.setContentPane(menu);
        stol.revalidate();
        stol.repaint();
    }

    private void askForBet() {
        while (true) {
            if (chips <= 0) {
                showMainMenu();
                break;
            }

            String input = JOptionPane.showInputDialog("Podaj stawkę:");

            if (input == null) {
                System.exit(0); // user cancelled
            }

            try {
                int value = Integer.parseInt(input);

                if (value > 0 && value <= chips) {
                    if (value == 67){
                        JOptionPane.showMessageDialog(null, "bruh", "haha... 67", JOptionPane.INFORMATION_MESSAGE);
                    }
                    bet = value;
                    chips -= bet;
                    gamePanel.chPanel.setChips(chips);
                    gamePanel.pPanel.setChips(bet);

                    if (chips < bet){
                        gamePanel.doubleButton.setEnabled(false);
                    }
                    break;
                } else if (value <= 0) {
                    JOptionPane.showMessageDialog(null, "Stawka musi być większa od 0!");
                } else {
                    JOptionPane.showMessageDialog(null, "Nie stać cie biedaku!! Masz: " + chips + "$");
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Podaj poprawną liczbę!");
            }
        }
    }
    public void animateCard(rekaPanel reka, Hand gracz, karta nowa, Runnable onFinish){
        rewersPanel animCard = new rewersPanel();

        Point start = new Point((int)(gamePanel.deckX), (int) gamePanel.deckY);
        Point end = new Point(
                reka.getX() + reka.getWidth()/2 + 50 * reka.getKartySize(),
                reka.getY() + (reka.getY() > height / 2 ? -20 : 20)
        );
        animCard.setSize(new Dimension(gamePanel.deckW, gamePanel.deckH));
        animCard.setLocation(start);

        gamePanel.setLayout(null);
        gamePanel.add(animCard);
        gamePanel.setComponentZOrder(animCard, 0);
        gamePanel.repaint();

        Timer timer = animationLoop(start, end, animCard, reka, gracz, nowa, onFinish);

        timer.start();
    }

    private Timer animationLoop(Point start, Point end, rewersPanel animCard, rekaPanel reka, Hand gracz, karta nowa, Runnable onFinish) {
        final int[] step = {0};

        return new Timer(15, e -> {
            double t = step[0] / 30.0;

            int x = (int)(start.x + t * (end.x - start.x));
            int y = (int)(start.y + t * (end.y - start.y));

            animCard.setLocation(x, y);

            step[0]++;

            if (step[0] >= 30) {
                ((Timer)e.getSource()).stop();

                gamePanel.remove(animCard);
                gamePanel.repaint();

                gracz.dobierz(nowa);
                reka.odswiez();

                onFinish.run();
            }
        });
    }
}