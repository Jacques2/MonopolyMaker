using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MonopolyMakerEditor
{
    public class CardDeck
    {
        public string deckName;
        public List<Card> cardDeck = new List<Card> ();

        [NonSerialized]
        public Guid g = Guid.NewGuid();

        public CardDeck(string deckName)
        {
            this.deckName = deckName;
        }

        public void constructParam()
        {
            foreach (Card card in cardDeck)
            {
                card.param = Parameters.toParam(card.eventType, card.param1, card.param2);
            }
        }

        public void deconstructParam()
        {
            foreach (Card card in cardDeck)
            {
                Parameters p = Parameters.unpackParams(card.param);
                card.param1 = p.param1;
                card.param2 = p.param2;
            }
        }
    }
}
