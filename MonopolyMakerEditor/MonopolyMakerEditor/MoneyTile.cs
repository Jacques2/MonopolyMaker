using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MonopolyMakerEditor
{
    public class MoneyTile
    {
        public string name;
        public int index;
        public int x;
        public int y;

        public int moneyModifier = 0;

        public MoneyTile(string name, int index, int x, int y, int moneyModifier)
        {
            this.name = name;
            this.index = index;
            this.x = x;
            this.y = y;
            this.moneyModifier = moneyModifier;
        }
    }
}
