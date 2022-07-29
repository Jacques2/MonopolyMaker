using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MonopolyMakerEditor
{
    public class BoardData
    {
        public String boardName;
        public String currencyName;
        public String currencySymbol;
        public int currencyOrder;
        public int boardSize;
        public int jailSquare;

        public List<Property> properties = new List<Property>();
        public List<Station> stations = new List<Station>();
        public List<Utility> utilities = new List<Utility>();
        public List<MoneyTile> moneyTiles = new List<MoneyTile>();
        public List<EventTile> eventTiles = new List<EventTile>();
        public List<CardTile> cardTiles = new List<CardTile>();

        public List<Set> setData = new List<Set>();

        [NonSerialized]
        public List<Tile> tiles = new List<Tile>();

        public List<CardDeck> cardDecks = new List<CardDeck>();

        public void buildTiles()
        {
            tiles.Clear();
            foreach (Property p in properties)
            {
                tiles.Add(new Tile(p.name, p.index, p.x, p.y, 0, p.upgradeCost, Parameters.ArrayToString(p.rents), p.value, visual: p.houseVisual));
            }
            foreach (Station p in stations)
            {
                tiles.Add(new Tile(p.name, p.index, p.x, p.y, 1, rents: Parameters.ArrayToString(p.rents), value: p.value));
            }
            foreach (Utility p in utilities)
            {
                tiles.Add(new Tile(p.name, p.index, p.x, p.y, 2, value: p.value));
            }
            foreach (MoneyTile p in moneyTiles)
            {
                tiles.Add(new Tile(p.name, p.index, p.x, p.y, 3, moneyModifier: p.moneyModifier));
            }
            foreach (EventTile p in eventTiles)
            {
                tiles.Add(new Tile(p.name, p.index, p.x, p.y, 4, eventType: p.eventType, param1: p.param1, param2: p.param2));
            }
            foreach (CardTile p in cardTiles)
            {
                tiles.Add(new Tile(p.name, p.index, p.x, p.y, 5));
            }
        }

        public void clearAll()
        {
            properties.Clear();
            stations.Clear();
            utilities.Clear();
            moneyTiles.Clear();
            eventTiles.Clear();
            cardTiles.Clear();
        }
    }
}
