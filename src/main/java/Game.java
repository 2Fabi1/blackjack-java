import Enums.CardRank;
import Enums.CardSuit;
import Gamepad.gamepadListener;
import Gamepad.gamepadManagerWithListener;
import Karty.Talia;
import Karty.karta;
import Panele.gamePanel;
import Panele.mainMenuPanel;
import Panele.rekaPanel;
import Panele.rewersPanel;
import Rece.DealerHand;
import Rece.Hand;
import Rece.PlayerHand;
import Rece.Reka;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.Random;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

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
    private int chips = 100;
    private int bet = 0;
    private boolean holeRevealed = false;

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

        System.out.println("RESTART!");

        gamePanel.resetHands();

        askForBet();
        setupGame();
        setupUI();

        gamePanel.hitButton.setEnabled(true);
        gamePanel.standButton.setEnabled(true);
        gamePanel.doubleButton.setEnabled(true);
    }

    private void setupGame() {
        gracz.dobierz(talia.dobierzKarte());
        dealer.dobierz(talia.dobierzKarte());
        gracz.dobierz(talia.dobierzKarte());
        dealer.dobierz(talia.dobierzKarte());
        holeRevealed = false;
    }

    private void setupUI() {
        rekaGracza = new rekaPanel(gracz.getReka());
        Reka rekaD = new Reka();
        rekaD.dobierz(dealer.getReka().getKarta(0));
        rekaD.dobierz(new karta(CardRank.EMPTY, CardSuit.EMPTY));
        rekaDealera = new rekaPanel(rekaD);
        rekaGracza.setBounds((int) ((width-handWidth)/2), (int)(0.7 * height), (int) handWidth,  (int) handHeight);
        gamePanel.addHand(rekaGracza);

        rekaDealera.setBounds((int) ((width-handWidth)/2), 30, (int) handWidth, (int) handHeight);
        gamePanel.addHand(rekaDealera);
    }

    private void setupActions() {
        gamePanel.hitButton.addActionListener(e -> hit());
        gamePanel.standButton.addActionListener(e -> stand());
        gamePanel.doubleButton.addActionListener(e -> doubleDown());
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
        };

        gamepadManagerWithListener manager = new gamepadManagerWithListener(listener);
        manager.startPolling();
        Runtime.getRuntime().addShutdownHook(new Thread(manager::stop));

        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
    }

    private void hit() {
        gamePanel.hitButton.setEnabled(false);
        playerTurn();
    }

    private void playerTurn() {
        animateCard(rekaGracza, gracz, () -> {
            if (gracz.policzWynik() > 21) {
                endGame("Przegrałeś!");
                gamePanel.chPanel.setChips(chips);
                gamePanel.pPanel.setChips(0);
                restart();
            } else {
                gamePanel.hitButton.setEnabled(true);
            }
        });
    }

    private void stand() {
        gamePanel.standButton.setEnabled(false);
        dealerTurn();
    }

    private void dealerTurn() {

        if (!holeRevealed) {
            holeRevealed = true;
            Timer t = new Timer(500 + new Random().nextInt(200), e -> {});
            t.setRepeats(false);
            t.start();
            rekaDealera.odswiez(dealer.getReka());
            dealerTurn();
            t.start();
            return;
        }

        if (dealer.shouldDraw(gracz.policzWynik())) {
            animateCard(rekaDealera, dealer, this::dealerTurn);
        } else {
            resolveGame();
        }
    }

    private void doubleDown(){
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
        } else {
            JOptionPane.showMessageDialog(null, "Nie stać cie biedaku!! Masz: " + chips + "$");
        }

    }

    private void resolveGame() {
        int dealerScore = dealer.policzWynik();
        int playerScore = gracz.policzWynik();

        if (dealerScore > 21 || (playerScore > dealerScore && playerScore <= 21)) {
            chips += 2 * bet;
            if (playerScore == 21 && rekaGracza.getKarty().size() == 2){
                chips += bet / 2;
            }
            endGame("Wygrałeś!");
        } else if (playerScore == dealerScore) {
            chips += bet;
            endGame("Remis!");
        }
        else{
            endGame("Przegrałeś!");
        }
        gamePanel.chPanel.setChips(chips);
        gamePanel.pPanel.setChips(0);
        restart();
    }

    private void endGame(String wynik) {
        JOptionPane.showMessageDialog(null, wynik);

        gamePanel.hitButton.setEnabled(false);
        gamePanel.standButton.setEnabled(false);
        gamePanel.doubleButton.setEnabled(false);
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
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
    public void animateCard(rekaPanel reka, Hand gracz, Runnable onFinish){
        rewersPanel animCard = new rewersPanel();

        Point start = new Point((int)(width * 0.9), (int)((height * 7) / 16));
        Point end = new Point(
                reka.getX() + reka.getWidth()/2 + 50 * reka.getKarty().size(),
                reka.getY() + (reka.getX() > height / 2 ? -20 : 20)
        );
        animCard.setSize(new Dimension(width / 24, height / 8));
        animCard.setLocation(start);

        gamePanel.setLayout(null);
        gamePanel.add(animCard);
        gamePanel.setComponentZOrder(animCard, 0);
        gamePanel.repaint();
        Timer timer = animationLoop(start, end, animCard, reka, gracz, onFinish);

        timer.start();
    }

    private Timer animationLoop(Point start, Point end, rewersPanel animCard, rekaPanel reka, Hand gracz, Runnable onFinish) {
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

                karta nowa = talia.dobierzKarte();
                gracz.dobierz(nowa);
                reka.dobierz(nowa);
                reka.aktualizujWynik();
                onFinish.run();
            }
        });
    }
}