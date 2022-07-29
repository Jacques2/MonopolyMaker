using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MonopolyMakerEditor
{
    public class Station
    {
        public string name;
        public int index;
        public int x;
        public int y;

        public int[] rents;
        public int value;

        public Station(string name, int index, int x, int y, int[] rents, int value)
        {
            this.name = name;
            this.index = index;
            this.x = x;
            this.y = y;
            this.rents = rents;
            this.value = value;
        }
    }
}
