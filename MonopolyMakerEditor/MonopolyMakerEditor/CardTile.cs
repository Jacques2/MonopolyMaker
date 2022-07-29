using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MonopolyMakerEditor
{
    public class CardTile
    {
        public string name;
        public int index;
        public int x;
        public int y;

        public CardTile(string name, int index, int x, int y)
        {
            this.name = name;
            this.index = index;
            this.x = x;
            this.y = y;
        }
    }
}
