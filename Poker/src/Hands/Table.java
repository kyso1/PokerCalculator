package Hands;

import java.util.ArrayList;
import java.util.List;
import Cards.Card;

public class Table {
    private List<Card> tableCards = new ArrayList<>();
    
    public void addTableCard(Card card) {
        if (tableCards.size() < 5) {
            tableCards.add(card);
        }
    }

    public List<Card> getTableCards() {
        return tableCards;
    }
}
