using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Text.Json;
using System.IO;
using Newtonsoft.Json;
using System.IO.Compression;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;

namespace MonopolyMakerEditor
{
    public partial class FormMain : Form
    {
        public static bool DEBUG_MODE = true;

        BoardData board = new BoardData();
        Assets assets = new Assets();

        public FormMain()
        {
            InitializeComponent();
        }

        private void radioButtonPrefix_CheckedChanged(object sender, EventArgs e)
        {
            board.currencyOrder = 0;
            textEdited();
        }
        private void radioButtonSuffix_CheckedChanged(object sender, EventArgs e)
        {
            board.currencyOrder = 1;
            textEdited();
        }

        private void textBoxBoardName_TextChanged(object sender, EventArgs e)
        {
            board.boardName = textBoxBoardName.Text;
        }

        private void textBoxCurrencyName_TextChanged(object sender, EventArgs e)
        {
            board.currencyName = textBoxCurrencyName.Text;
            textEdited();
        }

        private void textBoxCurrencySymbol_TextChanged(object sender, EventArgs e)
        {
            board.currencySymbol = textBoxCurrencySymbol.Text;
            textEdited();
        }

        private void textBoxBoardSize_TextChanged(object sender, EventArgs e)
        {
            try
            {
                board.boardSize = int.Parse(textBoxBoardSize.Text);
            }
            catch (Exception)
            {

            }
        }

        private void textBoxJailSquare_TextChanged(object sender, EventArgs e)
        {
            try
            {
                board.jailSquare = int.Parse(textBoxJailSquare.Text);

            }
            catch (Exception)
            {

            }
        }

        private void textEdited()
        {
            if (board.currencyOrder == 0)
            {
                labelExampleText.Text = "Players are paid in " + board.currencyName + "\nPlayer 1 passed GO and recieved " + board.currencySymbol + "200";
            }
            else
            {
                labelExampleText.Text = "Players are paid in " + board.currencyName + "\nPlayer 1 passed GO and recieved 200" + board.currencySymbol;
            }
        }

        private void saveProjectToolStripMenuItem_Click(object sender, EventArgs e)
        {
            saveFileDialogBoard.ShowDialog();
        }
        private void saveFileDialogBoard_FileOk(object sender, CancelEventArgs e)
        {
            saveProject();
        }


        private void saveProject()
        {
            //convert tiles to classes
            board.clearAll();
            foreach (Tile tile in board.tiles)
            {
                //properties
                if (tile.tileType == 0)
                {
                    int[] rents = new int[0];
                    try
                    {
                        rents = tile.rents.Split(',').Select(s => Convert.ToInt32(s)).ToArray();
                    }
                    catch (Exception)
                    {
                    }
                    board.properties.Add(new Property(tile.name, tile.index, tile.x, tile.y, tile.upgradeCost, rents, tile.value, tile.houseVisual));
                }
                else if (tile.tileType == 1)
                {
                    int[] rents = new int[0];
                    try
                    {
                        rents = tile.rents.Split(',').Select(s => Convert.ToInt32(s)).ToArray();
                    }
                    catch (Exception)
                    {
                    }
                    board.stations.Add(new Station(tile.name, tile.index, tile.x, tile.y, rents, tile.value));
                }
                else if (tile.tileType == 2)
                {
                    board.utilities.Add(new Utility(tile.name, tile.index, tile.x, tile.y, tile.value));
                }
                else if (tile.tileType == 3)
                {
                    board.moneyTiles.Add(new MoneyTile(tile.name, tile.index, tile.x, tile.y, tile.moneyModifier));
                }
                else if (tile.tileType == 4)
                {
                    board.eventTiles.Add(new EventTile(tile.name, tile.index, tile.x, tile.y, tile.eventType,tile.param1,tile.param2));
                }
                else if (tile.tileType == 5)
                {
                    board.cardTiles.Add(new CardTile(tile.name, tile.index, tile.x, tile.y));
                }
            }

            //card events to proper param
            foreach (CardDeck deck in board.cardDecks)
            {
                deck.constructParam();
            }

            //set tiles to ints
            foreach (Set set in board.setData)
            {
                set.tilesToProperties();
            }

            //write object to json file
            string fileName = "export.json";
            string jsonString = JsonConvert.SerializeObject(board,Formatting.Indented);
            File.WriteAllText(fileName, jsonString);

            //saving file
            try
            {
                ZipArchive zipfile = ZipFile.Open(saveFileDialogBoard.FileName, ZipArchiveMode.Create);
                writeImage(zipfile, boardimage, "board.png");
                writeText(zipfile, jsonString, "data.json");

                zipfile.Dispose();
            }
            catch (Exception ex)
            {
                MessageBox.Show("There was an error saving the file");
                if (DEBUG_MODE)
                {
                    throw;
                }
                return;
            }
            MessageBox.Show("Saved!");

        }

        public void writeImage(ZipArchive zipfile, Image image, string filename)
        {
            ZipArchiveEntry boarddataimagefile = zipfile.CreateEntry(filename);
            Stream entryStream = boarddataimagefile.Open();
            BinaryWriter sw = new BinaryWriter(entryStream);
            ImageConverter _imageConverter = new ImageConverter();
            byte[] xByte = (byte[])_imageConverter.ConvertTo(image, typeof(byte[]));
            sw.Write(xByte);
            sw.Close();
        }

        public void writeText(ZipArchive zipfile, string text, string filename)
        {
            ZipArchiveEntry boarddataimagefile = zipfile.CreateEntry(filename);
            Stream entryStream = boarddataimagefile.Open();
            StreamWriter sw = new StreamWriter(entryStream);
            sw.Write(text);
            sw.Close();
        }

        private void textBoxBoardSize_KeyPress(object sender, KeyPressEventArgs e)
        {
            filterForNumber(e);
        }

        private void textBoxJailSquare_KeyPress(object sender, KeyPressEventArgs e)
        {
            filterForNumber(e);
        }

        private void filterForNumber(KeyPressEventArgs e)
        {
            if (char.IsDigit(e.KeyChar) || e.KeyChar == (char)Keys.Back)
            {

            }
            else
            {
                e.Handled = true;
            }
        }

        private void listBoxSkillList_SelectedIndexChanged(object sender, EventArgs e)
        {

        }

        private void skillListToolStripTextBox_TextChanged(object sender, EventArgs e)
        {

        }

        private void skillListToolStripButtonAddRecord_Click(object sender, EventArgs e)
        {

        }

        private void skillListToolStripButtonRemovePag_Click(object sender, EventArgs e)
        {

        }

        private void skillListToolStripButtonSave_Click(object sender, EventArgs e)
        {

        }

        private void pagListToolStripTextBox_TextChanged(object sender, EventArgs e)
        {

        }

        private void pagListToolStripButtonAddRecord_Click(object sender, EventArgs e)
        {

        }

        private void pagListToolStripButtonRemovePag_Click(object sender, EventArgs e)
        {

        }

        private void pagListToolStripButtonSave_Click(object sender, EventArgs e)
        {

        }

        private void listBoxPagList_SelectedIndexChanged(object sender, EventArgs e)
        {

        }

        private void buttonGotoCoordsTab_Click(object sender, EventArgs e)
        {
            tabControl.SelectedIndex = 5;
        }

        Tile currentTile = new Tile();

        private void pagListToolStripButtonAddRecord_Click_1(object sender, EventArgs e)
        {
            board.tiles.Add(new Tile("New Tile"));
            sortAndDisplay();
        }

        bool propertyLock = false;
        private void listBoxPropertyList_SelectedIndexChanged(object sender, EventArgs e)
        {
            propertyLock = true;
            foreach (Tile t in board.tiles)
            {
                if (t.listIndex == listBoxPropertyList.SelectedIndex)
                {
                    currentTile = t;
                    tileSelected();
                    break;
                }
            }
            propertyLock = false;
        }

        private void sortAndDisplay()
        {
            if (propertyLock) return;
            board.tiles.Sort();
            listBoxPropertyList.Items.Clear();
            int index = -1;

            try
            {
                foreach (Tile t in board.tiles)
                {

                    index++;
                    t.listIndex = index;
                    listBoxPropertyList.Items.Add(t.name);
                    if (currentTile != null && t.g == currentTile.g)
                    {
                        listBoxPropertyList.SelectedIndex = index;
                    }
                }
            }
            catch
            {

            }

        }

        private void tileSelected()
        {
            ToolStripTextBoxCurrentlySelectedTile.Text = currentTile.name;

            textBoxPropertyName.Text = currentTile.name.ToString();
            textBoxPropertyIndex.Text = currentTile.index.ToString();
            textBoxPropertyX.Text = currentTile.x.ToString();
            textBoxPropertyY.Text = currentTile.y.ToString();

            textBoxPropertyUpgradeCost.Text = currentTile.upgradeCost.ToString();
            textBoxPropertyRent.Text = currentTile.rents.ToString();
            textBoxPropertyValue.Text = currentTile.value.ToString();
            textBoxPropertyMoneyModifier.Text = currentTile.moneyModifier.ToString();
            comboBoxEventType.SelectedIndex = currentTile.eventType;
            if (currentTile.param1 == null) currentTile.param1 = "";
            if (currentTile.param2 == null) currentTile.param2 = "";
            textBoxPropertyEventParam1.Text = currentTile.param1.ToString();
            textBoxPropertyEventParam2.Text = currentTile.param2.ToString();

            /*
                PROPERTY_TILE = 0;
                STATION_TILE = 1;
                UTILITY_TILE = 2;
                MONEY_TILE = 3;
                EVENT_TILE = 4;
                CARD_TILE = 5;  
            */		
            
            switch (currentTile.tileType)
            {
                case 0:
                    radioButtonTileProperty.Checked = true;
                    break;
                case 1:
                    radioButtonTileStation.Checked = true;
                    break ;
                case 2:
                    radioButtonTileUtility.Checked = true;
                    break;
                case 3:
                    radioButtonTileMoney.Checked = true;
                    break;
                case 4:
                    radioButtonTileEvent.Checked = true;
                    break;
                case 5:
                    radioButtonTileCard.Checked = true;
                    break;
                default:
                    break;
            }
        }

        /*
        Events
        1 - Advance to square (square number)
        2 - Advance to first square in list (rent multiplier, list of squares)
        3 - Change relative position (spaces)
        4 - Modify money (money)
        5 - Get out of Jail Card ()
        6 - Go to jail ()
        7 - Property Development (house price, hotel price)
        8 - Pay everyone (amount)
        9 - Receive money from everyone (amount)
        */

        public void eventModified(int eventtype, Label paramlabel1, Label paramlabel2, TextBox paramtext1, TextBox paramtext2)
        {

            if (eventtype == 0)
            {
                paramlabel1.Text = "Event Param 1:";
                paramlabel2.Text = "Event Param 2:";
                paramtext1.Enabled = false;
                paramtext2.Enabled = false;
            }
            else if (eventtype == 1)
            {
                paramlabel1.Text = "Square Number:";
                paramlabel2.Text = "N/A:";
                paramtext1.Enabled = true;
                paramtext2.Enabled = false;
            }
            else if (eventtype == 2)
            {
                paramlabel1.Text = "Rent Multiplier:";
                paramlabel2.Text = "List of squares:";
                paramtext1.Enabled = true;
                paramtext2.Enabled = true;
            }
            else if (eventtype == 3)
            {
                paramlabel1.Text = "Number of spaces:";
                paramlabel2.Text = "N/A";
                paramtext1.Enabled = true;
                paramtext2.Enabled = false;
            }
            else if (eventtype == 4)
            {
                paramlabel1.Text = "Money Amount:";
                paramlabel2.Text = "N/A";
                paramtext1.Enabled = true;
                paramtext2.Enabled = false;
            }
            else if (eventtype == 5)
            {
                paramlabel1.Text = "N/A";
                paramlabel2.Text = "N/A";
                paramtext1.Enabled = false;
                paramtext2.Enabled = false;
            }
            else if (eventtype == 6)
            {
                paramlabel1.Text = "N/A";
                paramlabel2.Text = "N/A";
                paramtext1.Enabled = false;
                paramtext2.Enabled = false;
            }
            else if (eventtype == 7)
            {
                paramlabel1.Text = "House Price:";
                paramlabel2.Text = "Hotel Price:";
                paramtext1.Enabled = true;
                paramtext2.Enabled = true;
            }
            else if (eventtype == 8)
            {
                paramlabel1.Text = "Amount:";
                paramlabel2.Text = "N/A:";
                paramtext1.Enabled = true;
                paramtext2.Enabled = false;
            }
            else if (eventtype == 9)
            {
                paramlabel1.Text = "Amount:";
                paramlabel2.Text = "N/A:";
                paramtext1.Enabled = true;
                paramtext2.Enabled = false;
            }
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {

            currentTile.eventType = comboBoxEventType.SelectedIndex;
            eventModified(currentTile.eventType, labelEventParam1, labelEventParam2, textBoxPropertyEventParam1, textBoxPropertyEventParam2);

        }

        private void groupBox3_Enter(object sender, EventArgs e)
        {

        }

        private void textBoxPropertyIndex_TextChanged(object sender, EventArgs e)
        {
            if (textBoxPropertyIndex.Text == "")
            {
                currentTile.index = 0;
            }
            else
            {
                currentTile.index = int.Parse(textBoxPropertyIndex.Text);
            }
            sortAndDisplay();
        }

        private void textBoxPropertyName_TextChanged(object sender, EventArgs e)
        {
            currentTile.name = textBoxPropertyName.Text;
            ToolStripTextBoxCurrentlySelectedTile.Text = textBoxPropertyName.Text;
            sortAndDisplay();
        }

        private void textBoxPropertyX_TextChanged(object sender, EventArgs e)
        {
            if (textBoxPropertyX.Text == "")
            {
                currentTile.x = 0;
            }
            else
            {
                currentTile.x = int.Parse(textBoxPropertyX.Text);
            }
        }

        private void textBoxPropertyY_TextChanged(object sender, EventArgs e)
        {
            if (textBoxPropertyY.Text == "")
            {
                currentTile.y = 0;
            }
            else
            {
                currentTile.y = int.Parse(textBoxPropertyY.Text);
            }
        }

        private void textBoxPropertyUpgradeCost_TextChanged(object sender, EventArgs e)
        {
            if (textBoxPropertyUpgradeCost.Text == "")
            {
                currentTile.upgradeCost = 0;
            }
            else
            {
                currentTile.upgradeCost = int.Parse(textBoxPropertyUpgradeCost.Text);
            }
        }

        private void textBoxPropertyRent_TextChanged(object sender, EventArgs e)
        {
            //currentTile.rents = textBoxPropertyRent.Text.Split(',').Select(s => Convert.ToInt32(s)).ToArray();
            currentTile.rents = textBoxPropertyRent.Text;
        }

        private void textBoxPropertyValue_TextChanged(object sender, EventArgs e)
        {
            if (textBoxPropertyValue.Text == "")
            {
                currentTile.value = 0;
            }
            else
            {
                currentTile.value = int.Parse(textBoxPropertyValue.Text);
            }
        }

        private void textBoxPropertyMoneyModifier_TextChanged(object sender, EventArgs e)
        {
            if (textBoxPropertyMoneyModifier.Text == "")
            {
                currentTile.moneyModifier = 0;
            }
            else
            {
                currentTile.moneyModifier = int.Parse(textBoxPropertyMoneyModifier.Text);
            }
        }
        private void textBoxPropertyEventParam1_TextChanged(object sender, EventArgs e)
        {
            currentTile.param1 = textBoxPropertyEventParam1.Text;
        }

        private void textBoxPropertyEventParam2_TextChanged(object sender, EventArgs e)
        {
            currentTile.param2 = textBoxPropertyEventParam2.Text;
        }

        private void textBoxPropertyName_Enter(object sender, EventArgs e)
        {
            
        }

        private void radioButtonTileProperty_CheckedChanged(object sender, EventArgs e)
        {
            currentTile.tileType = 0;
            alterTileBoxes();
        }

        private void radioButtonTileStation_CheckedChanged(object sender, EventArgs e)
        {
            currentTile.tileType = 1;
            alterTileBoxes();
        }

        private void radioButtonTileUtility_CheckedChanged(object sender, EventArgs e)
        {
            currentTile.tileType = 2;
            alterTileBoxes();
        }

        private void radioButtonTileMoney_CheckedChanged(object sender, EventArgs e)
        {
            currentTile.tileType = 3;
            alterTileBoxes();
        }

        private void radioButtonTileEvent_CheckedChanged(object sender, EventArgs e)
        {
            currentTile.tileType = 4;
            alterTileBoxes();
        }

        private void radioButtonTileCard_CheckedChanged(object sender, EventArgs e)
        {
            currentTile.tileType = 5;
            alterTileBoxes();
        }

        /*
            PROPERTY_TILE = 0;
            STATION_TILE = 1;
            UTILITY_TILE = 2;
            MONEY_TILE = 3;
            EVENT_TILE = 4;
            CARD_TILE = 5;  
        */

        private void alterTileBoxes()
        {
            textBoxPropertyUpgradeCost.Enabled = false;
            textBoxPropertyRent.Enabled = false;
            textBoxPropertyValue.Enabled = false;
            textBoxPropertyMoneyModifier.Enabled = false;
            comboBoxEventType.Enabled = false;

            int val = currentTile.tileType;
            if (val == 0)
            {
                textBoxPropertyUpgradeCost.Enabled = true;
            }
            if (val == 1 || val == 0)
            {
                textBoxPropertyRent.Enabled = true;
            }
            if (val == 1 || val == 0 || val == 2)
            {
                textBoxPropertyValue.Enabled = true;
            }
            if (val == 3)
            {
                textBoxPropertyMoneyModifier.Enabled = true;
            }
            if (val == 4)
            {
                comboBoxEventType.Enabled = true;
            }
        }

        Image boardimage;
        private void openFileDialogBoard_FileOk(object sender, CancelEventArgs e)
        {
            //loading file
            try
            {
                ZipArchive zipfile = ZipFile.Open(openFileDialogBoard.FileName, ZipArchiveMode.Read);
                ZipArchiveEntry boarddatafile = zipfile.GetEntry("data.json");
                StreamReader boarddatareader = new StreamReader(boarddatafile.Open());
                string file = boarddatareader.ReadToEnd();
                board = JsonConvert.DeserializeObject<BoardData>(file);

                //visual board
                ZipArchiveEntry boardimagefile = zipfile.GetEntry("board.png");
                Stream boardimagereader = boardimagefile.Open();
                boardimage = Image.FromStream(boardimagereader);
                refreshImages();
            }
            catch (Exception ex)
            {
                MessageBox.Show("There was an error reading the file");
                if (DEBUG_MODE)
                {
                    throw;
                }
                return;
            }

            //initial bits
            textBoxBoardName.Text = board.boardName;
            textBoxCurrencyName.Text = board.currencyName;
            textBoxCurrencySymbol.Text = board.currencySymbol;
            if (board.currencyOrder == 0)
            {
                radioButtonPrefix.Checked = true;
            }
            else
            {
                radioButtonSuffix.Checked = true;
            }
            textBoxBoardSize.Text = board.boardSize.ToString();
            textBoxJailSquare.Text = board.jailSquare.ToString();

            //tiles
            board.buildTiles();

            //cards
            foreach (CardDeck deck in board.cardDecks)
            {
                deck.deconstructParam();
            }
            listBoxCards.Items.Clear();

            //tiles
            foreach (Set set in board.setData)
            {
                set.propertiesToTiles(board.tiles);
            }
            refreshSets();
            refreshSetTiles();

            //assets
            labelSelectedBoard.Text = "Selected File: None";

            sortAndDisplay();
            cardScreenRefresh();

        }

        public void refreshImages()
        {
            pictureBoxBoardPlot.Image = boardimage;
            pictureBoxBoardImagePreview.Image = boardimage;
        }

        private void loadProjectToolStripMenuItem_Click(object sender, EventArgs e)
        {
            openFileDialogBoard.ShowDialog();
        }

        private void pagListToolStripButtonRemovePag_Click_1(object sender, EventArgs e)
        {
            board.tiles.Remove(currentTile);
            currentTile = null;
            sortAndDisplay();
        }

        private void tableLayoutPanelPagSkillList_Paint(object sender, PaintEventArgs e)
        {

        }

        CardDeck currentDeck = null;
        bool cardLock = false;

        public void cardScreenRefresh()
        {
            if (cardLock) return;
            //card deck
            listBoxCardDecks.Items.Clear();
            int index = -1;
            foreach (CardDeck deck in board.cardDecks)
            {
                index++;
                listBoxCardDecks.Items.Add(deck.deckName);
                if (currentDeck != null && currentDeck.g == deck.g)
                {
                    listBoxCardDecks.SelectedIndex = index;
                }
            }

            listBoxCards.Items.Clear();

            //end here if currentdeck null
            if (currentDeck == null)
            {
                return;
            }

            cardRefresh();

        }

        public void cardRefresh()
        {
            //loading cards from card deck
            listBoxCards.Items.Clear();
            int index = -1;
            foreach (Card card in currentDeck.cardDeck)
            {
                index++;
                listBoxCards.Items.Add(card);
                if (currentCard != null && currentCard.g == card.g)
                {
                    listBoxCards.SelectedIndex = index;
                }
            }
        }

        public void cardTextRefresh()
        {
            if (currentCard == null)
            {
                textBoxCardEventParam1.Text = "";
                textBoxCardEventParam2.Text = "";
                textBoxCardEventDescription.Text = "";
                comboBoxCardEventType.SelectedIndex = 0;
            }
            textBoxCardEventDescription.Text = currentCard.description;
            comboBoxCardEventType.SelectedIndex = currentCard.eventType;
            textBoxCardEventParam1.Text = currentCard.param1;
            textBoxCardEventParam2.Text = currentCard.param2;
        }

        private void toolStripButton1_Click(object sender, EventArgs e)
        {
            board.cardDecks.Add(new CardDeck("New Deck"));
            cardScreenRefresh();
        }

        private void toolStripButtonRemoveCardDeck_Click(object sender, EventArgs e)
        {
            if (currentDeck == null) return;
            board.cardDecks.Remove(currentDeck);
            cardScreenRefresh();
        }

        private void listBoxCardDecks_SelectedIndexChanged(object sender, EventArgs e)
        {
            cardLock = true;
            currentCard = null;
            listBoxCards.Items.Clear();
            if (listBoxCardDecks.SelectedIndex == -1) return;
            currentDeck = board.cardDecks.ElementAt(listBoxCardDecks.SelectedIndex);
            toolStripTextBoxCardDeckName.Enabled = true;
            if (toolStripTextBoxCardDeckName.Text != currentDeck.deckName)
            {
                toolStripTextBoxCardDeckName.Text = currentDeck.deckName;
            }
            cardLock = false;
            cardRefresh();
        }

        private void toolStripTextBoxCardDeckName_Click(object sender, EventArgs e)
        {

        }

        private void toolStripTextBoxCardDeckName_KeyPress(object sender, KeyPressEventArgs e)
        {

        }

        private void toolStripTextBoxCardDeckName_TextChanged(object sender, EventArgs e)
        {
            if (currentDeck != null)
            {
                currentDeck.deckName = toolStripTextBoxCardDeckName.Text;
                cardScreenRefresh();
            }
        }

        Card currentCard = null;

        private void toolStripButtonAddCard_Click(object sender, EventArgs e)
        {
            if (currentDeck == null) 
            {
                MessageBox.Show("You must have a card deck selected!");
                return;

            }
            currentDeck.cardDeck.Add(new Card("New Card"));
            cardRefresh();
        }

        private void listBoxCards_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (listBoxCards.SelectedIndex == -1) return;
            currentCard = (Card)listBoxCards.SelectedItem;
            cardTextRefresh();
        }

        private void toolStripButtonRemoveCard_Click(object sender, EventArgs e)
        {
            currentDeck.cardDeck.Remove(currentCard);
            currentCard = null;
            cardRefresh();
        }

        private void comboBoxCardEventType_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (currentCard == null)
            {
                return;
            }
            currentCard.eventType = comboBoxCardEventType.SelectedIndex;
            eventModified(currentCard.eventType,labelCardEventParam1, labelCardEventParam2,textBoxCardEventParam1, textBoxCardEventParam2);
        }

        private void textBoxCardEventDescription_TextChanged(object sender, EventArgs e)
        {
            if (currentCard != null)
            {
                currentCard.description = textBoxCardEventDescription.Text;
                cardRefresh();
            }
        }

        private void textBoxCardEventParam1_TextChanged(object sender, EventArgs e)
        {
            if (currentCard != null)
            {
                currentCard.param1 = textBoxCardEventParam1.Text;
            }
        }

        private void textBoxCardEventParam2_TextChanged(object sender, EventArgs e)
        {
            if (currentCard != null)
            {
                currentCard.param2 = textBoxCardEventParam2.Text;
            }
        }

        private void listBox1_SelectedIndexChanged(object sender, EventArgs e)
        {

        }

        private void checkBox1_CheckedChanged(object sender, EventArgs e)
        {
            if (currentSet != null)
            {
                currentSet.allowHouses = checkBoxAllowHouses.Checked;
            }
        }

        private void buttonChooseColor_Click(object sender, EventArgs e)
        {
            colorDialog.ShowDialog();
            if (colorDialog.Color == null)
            {
                colorDialog.Color = Color.Black;
            }
            if (currentSet != null)
            {
                currentSet.setColor(colorDialog.Color);
            }
            refreshColour();
        }

        public void refreshColour()
        {
            textBoxColor.BackColor = colorDialog.Color;
            int red = colorDialog.Color.R;
            int green = colorDialog.Color.G;
            int blue = colorDialog.Color.B;
            if ((red * 0.299 + green * 0.587 + blue * 0.114) > 186)
            {
                textBoxColor.ForeColor = Color.Black;
            }
            else
            {
                textBoxColor.ForeColor = Color.White;
            }
            textBoxColor.Text = "#" + Set.colorToHex(colorDialog.Color);
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            pictureBoxBoardPlot.SizeMode = PictureBoxSizeMode.StretchImage;
        }

        

        private void listBoxSets_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (listBoxSets.SelectedIndex == -1)
            {
                currentSet = null;
                return;
            }
            currentSet = null;
            uncheckTiles();
            currentSet = (Set)listBoxSets.SelectedItem;

            textBoxSetName.Text = currentSet.identifier;
            checkBoxAllowHouses.Checked = currentSet.allowHouses;
            colorDialog.Color = currentSet.getColor();
            refreshColour();

            for (int i = 0; i < checkedListBoxTiles.Items.Count; i++)
            {
                if (currentSet.tiles.Contains(checkedListBoxTiles.Items[i]))
                {
                    checkedListBoxTiles.SetItemChecked(i, true);
                }
            }
        }

        Set currentSet;
        private void toolStripButtonAddSet_Click(object sender, EventArgs e)
        {
            Set newSet = new Set("New Set");
            board.setData.Add(newSet);
            listBoxSets.Items.Add(newSet);
        }

        private void toolStripButtonRemoveSet_Click(object sender, EventArgs e)
        {
            if (currentSet != null)
            {
                board.setData.Remove(currentSet);
                listBoxSets.Items.Remove(currentSet);
                currentSet = null;
            }
        }

        private void textBoxSetName_TextChanged(object sender, EventArgs e)
        {
            if (currentSet != null)
            {
                currentSet.identifier = textBoxSetName.Text;
            }
        }

        public void refreshSets()
        {
            listBoxSets.Items.Clear();
            foreach (Set set in board.setData)
            {
                set.populateColor();
                listBoxSets.Items.Add(set);
            }
        }

        private void tabControl_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (tabControl.SelectedIndex == 3 || tabControl.SelectedIndex == 5)
            {
                refreshSetTiles();
            }
        }

        public void refreshSetTiles()
        {
            checkedListBoxTiles.Items.Clear();
            listBoxCoordinateTiles.Items.Clear();
            board.tiles.Sort();
            foreach (Tile tile in board.tiles)
            {
                checkedListBoxTiles.Items.Add(tile);
                listBoxCoordinateTiles.Items.Add(tile);
            }
            uncheckTiles();
        }

        public void uncheckTiles()
        {
            for (int i = 0; i < checkedListBoxTiles.Items.Count; i++)
            {
                checkedListBoxTiles.SetItemChecked(i, false);
            }
        }

        private void checkedListBoxTiles_SelectedIndexChanged(object sender, EventArgs e)
        {

        }

        private void checkedListBoxTiles_ItemCheck(object sender, ItemCheckEventArgs e)
        {
            Tile selectedTile = (Tile)checkedListBoxTiles.Items[e.Index];
            if (selectedTile == null) return;
            if (currentSet == null) return;
            if (e.NewValue == CheckState.Checked)
            {
                currentSet.tiles.Add(selectedTile);
            }
            else if (e.NewValue == CheckState.Unchecked)
            {
                currentSet.tiles.Remove(selectedTile);
            }
        }

        private void pictureBoxBoardPlot_Click(object sender, EventArgs e)
        {
        }

        int MAXWIDTH = 1000;
        int MAXHEIGHT = 1000;

        int BLOT = 6;


        float xPercent;
        float yPercent;

        bool accumLock = false;
        private void pictureBoxBoardPlot_MouseUp(object sender, MouseEventArgs e)
        {
            float xCoordinate;
            float yCoordinate;
            xCoordinate = e.X;
            yCoordinate = e.Y;
            float width = pictureBoxBoardPlot.Width;
            float height = pictureBoxBoardPlot.Height;
            labelCoords.Text = xCoordinate + ", " + yCoordinate + ", " + pictureBoxBoardPlot.Height;
            xPercent = (e.X / width);
            yPercent = (e.Y / height);
            int adjustedX = (int)(MAXWIDTH * xPercent);
            int adjustedY = (int)(MAXHEIGHT * yPercent);
            labelCoords.Text = adjustedX + ", " + adjustedY;

            if (radioButtonTile.Checked)
            {
                numericUpDownX.Value = adjustedX;
                numericUpDownY.Value = adjustedY;
            }
            else if (radioButtonHouse.Checked)
            {
                numericUpDownHouseX.Value = adjustedX;
                numericUpDownHouseY.Value = MAXHEIGHT-adjustedY;
            }

            if (checkBoxAutoMove.Checked && listBoxCoordinateTiles.SelectedIndex < listBoxCoordinateTiles.Items.Count-1)
            {
                accumLock = true;
                do
                {
                    listBoxCoordinateTiles.SelectedIndex++;
                } while (radioButtonHouse.Checked && listBoxCoordinateTiles.SelectedItem is Tile t && t.tileType != 0);
            }
            accumLock = false;

            pictureBoxBoardPlot.Invalidate();
            timer1.Start();

        }

        private void FormMain_Paint(object sender, PaintEventArgs e)
        {
            Application.DoEvents();
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            timer1.Stop();
            paintBlot();
        }

        public void paintBlot()
        {
            Graphics g = pictureBoxBoardPlot.CreateGraphics();
            Pen pen = new Pen(Color.Red);
            pen.Width = BLOT;

            if (currentCoordTile == null)
            {
                return;
            }

            g.DrawEllipse(pen, new Rectangle((int)(pictureBoxBoardPlot.Width * xPercent) - (BLOT / 2), (int)(pictureBoxBoardPlot.Height * yPercent) - (BLOT / 2), BLOT, BLOT));
            int houseX = (int)(((float)currentCoordTile.houseVisual.x / (float)MAXWIDTH) * pictureBoxBoardPlot.Width);
            int houseY = (int)(((float)(MAXHEIGHT-currentCoordTile.houseVisual.y) / (float)MAXHEIGHT) * pictureBoxBoardPlot.Height);
            int height = (int)(((float)pictureBoxBoardPlot.Height / (float)MAXHEIGHT) * (float)currentCoordTile.houseVisual.height);
            int width = (int)(((float)pictureBoxBoardPlot.Width / (float)MAXWIDTH) * (float)currentCoordTile.houseVisual.width);
            Rectangle r = new Rectangle(houseX-(width/2), houseY-(height/2), width,height);
            //g.DrawRectangle(pen, r);
            RotateRectangle(g, r, currentCoordTile.houseVisual.rotation);
            g.ResetTransform();
        }

        public void RotateRectangle(Graphics g, Rectangle r, float angle)
        {
            using (Matrix m = new Matrix())
            {
                m.RotateAt(angle, new PointF(r.Left + (r.Width / 2),r.Top + (r.Height / 2)));
                g.Transform = m;
                g.DrawRectangle(Pens.Red, r);
                g.ResetTransform();
            }
        }

        private void FormMain_Resize(object sender, EventArgs e)
        {
            
        }

        private void FormMain_ResizeEnd(object sender, EventArgs e)
        {
            paintBlot();
        }

        private void FormMain_SizeChanged(object sender, EventArgs e)
        {
            paintBlot();

        }

        Tile currentCoordTile;
        private void listBoxCoordinateTiles_SelectedIndexChanged(object sender, EventArgs e)
        {
            currentCoordTile = (Tile)listBoxCoordinateTiles.SelectedItem;
            if (currentCoordTile == null) return;
            numericUpDownX.Value = currentCoordTile.x;
            numericUpDownY.Value = currentCoordTile.y;
            if (accumLock)
            {
                return;
            }
            if (currentCoordTile.tileType == 0)
            {
                if (selectedCordMode == 0)
                {
                    radioButtonTile.Checked = true;
                }
                else if (selectedCordMode == 1)
                {
                    radioButtonHouse.Checked = true;
                }
                numericUpDownHouseX.Value = currentCoordTile.houseVisual.x;
                numericUpDownHouseY.Value = currentCoordTile.houseVisual.y;
                numericUpDownHouseX.Enabled = true;
                numericUpDownHouseY.Enabled = true;
                trackBarRotation.Enabled = true;
                textBoxRotation.Enabled = true;
                textBoxRotation.Text = currentCoordTile.houseVisual.rotation.ToString();
            }
            else
            {
                radioButtonTile.Checked = true;
                numericUpDownHouseX.Value = 0;
                numericUpDownHouseY.Value = 0;
                numericUpDownHouseX.Enabled = false;
                numericUpDownHouseY.Enabled = false;
                trackBarRotation.Value = 0;
                trackBarRotation.Enabled = false;
                textBoxRotation.Text = "";
            }
        }

        private void numericUpDownX_ValueChanged(object sender, EventArgs e)
        {
            if (currentCoordTile != null)
            {
                currentCoordTile.x = (int)numericUpDownX.Value;
                updateBlot();
            }
        }

        public void updateBlot()
        {
            xPercent = ((float)currentCoordTile.x/ (float)MAXWIDTH);
            yPercent = ((float)currentCoordTile.y/ (float)MAXHEIGHT);

            pictureBoxBoardPlot.Invalidate();
            timer1.Start();
        }

        private void numericUpDownY_ValueChanged(object sender, EventArgs e)
        {
            if (currentCoordTile != null)
            {
                currentCoordTile.y = (int)numericUpDownY.Value;
                updateBlot();
            }
        }

        private void trackBar1_Scroll(object sender, EventArgs e)
        {
            textBoxRotation.Text = (trackBarRotation.Value * 45).ToString();
        }

        private void textBoxRotation_TextChanged(object sender, EventArgs e)
        {
            if (currentCoordTile != null)
            {
                if (textBoxRotation.Text != "")
                {
                    currentCoordTile.houseVisual.rotation = int.Parse(textBoxRotation.Text);
                }
                else
                {
                    currentCoordTile.houseVisual.rotation = 0;
                }
                updateBlot();
            }
        }

        private void numericUpDownHouseX_ValueChanged(object sender, EventArgs e)
        {
            if (currentCoordTile != null)
            {
                currentCoordTile.houseVisual.x = (int)numericUpDownHouseX.Value;
                updateBlot();
            }
        }

        private void numericUpDownHouseY_ValueChanged(object sender, EventArgs e)
        {
            if (currentCoordTile != null)
            {
                currentCoordTile.houseVisual.y = (int)numericUpDownHouseY.Value;
                updateBlot();
            }
        }

        private void textBoxRotation_KeyPress(object sender, KeyPressEventArgs e)
        {
            filterForNumber(e);
        }

        private void numericUpDownX_KeyPress(object sender, KeyPressEventArgs e)
        {
            filterForNumber(e);
        }

        int selectedCordMode = 0;
        private void radioButtonTile_CheckedChanged(object sender, EventArgs e)
        {

        }

        private void radioButtonHouse_CheckedChanged(object sender, EventArgs e)
        {

        }

        private void radioButtonTile_Click(object sender, EventArgs e)
        {
            if (radioButtonTile.Checked)
            {
                selectedCordMode = 0;
            }
        }

        private void radioButtonHouse_Click(object sender, EventArgs e)
        {
            if (radioButtonHouse.Checked)
            {
                selectedCordMode = 1;
            }
        }

        private void buttonBrowseImage_Click(object sender, EventArgs e)
        {
            openFileDialogBoardImage.ShowDialog();
        }

        private void openFileDialogBoardImage_FileOk(object sender, CancelEventArgs e)
        {
            try
            {
                boardimage = Image.FromFile(openFileDialogBoardImage.FileName);
                labelSelectedBoard.Text = openFileDialogBoardImage.FileName;
                refreshImages();
            }
            catch (Exception)
            {

                throw;
            }
        }

        private void textBoxPropertyIndex_KeyPress(object sender, KeyPressEventArgs e)
        {
            filterForNumber(e);
        }
    }
}
