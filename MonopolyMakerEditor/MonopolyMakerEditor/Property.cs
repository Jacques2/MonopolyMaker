using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MonopolyMakerEditor
{
    public class Property
    {
        public string name;
        public int index;
        public int x;
        public int y;

        public int upgradeCost;
        public int[] rents;
        public int value;

        public HouseVisual houseVisual;

        public Property(string name, int index, int x, int y, int upgradeCost, int[] rents, int value, HouseVisual visual)
        {
            this.name = name;
            this.index = index;
            this.x = x;
            this.y = y;
            this.upgradeCost = upgradeCost;
            this.rents = rents;
            this.value = value;
            this.houseVisual = visual;
        }
    }
}
