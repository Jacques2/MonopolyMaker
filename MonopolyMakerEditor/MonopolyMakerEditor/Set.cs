using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MonopolyMakerEditor
{
    public class Set
    {
        public string identifier;
        public bool allowHouses = true;

        public string hex;

        [NonSerialized]
        private Color color;

        [NonSerialized]
        public List<Tile> tiles = new List<Tile>();

        public List<int> properties = new List<int>();

        public Set(string identifier)
        {
            this.identifier = identifier;
            setColor(Color.Aqua);
        }

        public static string colorToHex(Color c)
        {
            return c.R.ToString("X2") + c.G.ToString("X2") + c.B.ToString("X2");
        }

        public static Color hexToColor(String extendedhex)
        {
            return ColorTranslator.FromHtml("#" + extendedhex.Substring(0, 6));
        }

        public void setColor(Color c)
        {
            color = c;
            hex = colorToHex(color) + "FF";
        }

        public void populateColor()
        {
            color = hexToColor(hex);
        }

        public Color getColor()
        {
            color = hexToColor(hex);
            return color;
        }

        public void tilesToProperties()
        {
            properties.Clear();
            foreach (Tile tile in tiles)
            {
                if (!properties.Contains(tile.index)) properties.Add(tile.index);
            }
        }
        public void propertiesToTiles(List<Tile> tileList)
        {
            tiles.Clear();
            List<int> tempProperties = new List<int>();
            foreach (int item in properties)
            {
                tempProperties.Add(item);
            }
            foreach (Tile tile in tileList)
            {
                if (tempProperties.Contains(tile.index))
                {
                    tiles.Add(tile);
                    tempProperties.Remove(tile.index);
                }
            }
        }

        public override string ToString()
        {
            return identifier;
        }
    }
}
