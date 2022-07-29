using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MonopolyMakerEditor
{
    public class EventTile
    {
        public string name;
        public int index;
        public int x;
        public int y;

        public int eventType;
        public string param1;
        public string param2;

        public EventTile(string name, int index, int x, int y, int eventType, string param1, string param2)
        {
            this.name = name;
            this.index = index;
            this.x = x;
            this.y = y;
            this.eventType = eventType;
            this.param1 = param1;
            this.param2 = param2;
        }
    }
}
