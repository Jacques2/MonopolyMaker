using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MonopolyMakerEditor
{
    public class Card
    {
        public string description;
        public int eventType;

        public int[] param;

        [NonSerialized]
        public string param1;

        [NonSerialized]
        public string param2;

        [NonSerialized]
        public Guid g = Guid.NewGuid();

        public Card(string description) { this.description = description; }

        public override string ToString()
        {
            return description;
        }
    }
}
