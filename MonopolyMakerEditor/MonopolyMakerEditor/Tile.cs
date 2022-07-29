using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MonopolyMakerEditor
{

    public class Tile : IComparable<Tile>
    {
        public int listIndex;
        public int tileType = 0;
        public Guid g = Guid.NewGuid();

        //properties
        public int upgradeCost = 0;
        public HouseVisual houseVisual = new HouseVisual();

        //properties, stations
        public String rents = "";

        //properties, stations, utilities
        public int value = 0;

        //moneytile
        public int moneyModifier = 0;

        //eventtile
        public int eventType = 0;
        public string param1 = "";
        public string param2 = "";

        //all tiles
        public String name = "New Tile";
        public int index = 0;
        public int x = 0;
        public int y = 0;

        public Tile()
        {
        }

        public Tile(String name)
        {
            this.name = name;
        }

        public Tile(string name, int index, int x, int y, int tileType, int upgradeCost = 0, string rents = "", int value = 0, int moneyModifier = 0, int eventType = 0, string param1 = "", string param2 = "", HouseVisual visual = null)
        {
            this.tileType = tileType;
            this.upgradeCost = upgradeCost;
            this.rents = rents;
            this.value = value;
            this.moneyModifier = moneyModifier;
            this.eventType = eventType;
            this.param1 = param1;
            this.param2 = param2;
            this.name = name;
            this.index = index;
            this.x = x;
            this.y = y;
            if (visual != null)
            {
                this.houseVisual = visual;
            }
            else
            {
                this.houseVisual = new HouseVisual();
            }
        }

        public int CompareTo(Tile other)
        {
            if (other == null)
            {
                return 1;
            }
            else if (index == other.index)
            {
                if (g.CompareTo(other.g) < 0)
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            }
            else if (index > other.index)
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
        public override string ToString()
        {
            return name;
        }
    }
}
