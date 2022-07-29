using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MonopolyMakerEditor
{
    public class Utility
    {
        public string name;
        public int index;
        public int x;
        public int y;

        public int value;

        public Utility(string name, int index, int x, int y, int value)
        {
            this.name = name;
            this.index = index;
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }
}
