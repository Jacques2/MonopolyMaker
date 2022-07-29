using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MonopolyMakerEditor
{
    public class Parameters
    {
        public string param1 = "";
        public string param2 = "";

        /*
         * Events
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
        public static int[] toParam(int eventType, string param1, string param2)
        {
            if (param1 == null) param1 = "";
            if (param2 == null) param2 = "";
            if (eventType == 5 || eventType == 6)
            {
                return new int[0];
            }
            else if (eventType == 1 || eventType == 3 || eventType == 4 || eventType == 8 || eventType == 9)
            {
                try
                {
                    int param = int.Parse(param1);
                    return new int[1] { param };
                }
                catch (Exception)
                {
                    return new int[1] { 0};
                }
            }
            else if (eventType == 7)
            {
                try
                {
                    int firstparam = int.Parse(param1);
                    int secondparam = int.Parse(param1);
                    return new int[2] { firstparam, secondparam };
                }
                catch (Exception)
                {
                    return new int[2] { 0, 0 };
                }
            }
            else if(eventType == 2)
            {
                List<int> intList = new List<int>();
                try
                {
                    int firstparam = int.Parse(param1);
                    intList.Add(firstparam);
                }
                catch
                {
                    intList.Add(1);
                }
                int[] secondparam;
                try
                {
                    secondparam = param2.Split(',').Select(s => Convert.ToInt32(s)).ToArray();
                    foreach (int element in secondparam)
                    {
                        try
                        {
                            intList.Add(element);
                        }
                        catch (Exception)
                        {
                            continue;
                        }
                    }
                }
                catch (Exception ex)
                {
                    int ws = 1;

                }

                return intList.ToArray();
            }
            return new int[0];
        }
        public static Parameters unpackParams(int[] param)
        {
            Parameters result = new Parameters();
            if (param == null)
            {
                result.param1 = "5";
                return result;
            }
            if (param.Length == 0)
            {
                return result;
            }
            else if (param.Length == 1)
            {
                result.param1 = param.ElementAt(0).ToString();
                return result;
            }
            else if (param.Length == 2)
            {
                result.param1 = param.ElementAt(0).ToString();
                result.param2 = param.ElementAt(1).ToString();
                return result;
            }
            else if (param.Length >= 3)
            {
                result.param1 = param.ElementAt(0).ToString();
                int[] secondparam = new int[param.Length-1];
                for (int i = 1; i < param.Length; i++)
                {
                    secondparam[i-1] = param[i];
                }
                result.param2 = ArrayToString(secondparam);
                return result;
            }
            return result;
        }

        public static string ArrayToString(int[] intArray)
        {
            string str = "";
            foreach (int item in intArray)
            {
                str += item.ToString() + ",";
            }
            str = str.Remove(str.Length - 1, 1);
            return str;
        }
    }
}
