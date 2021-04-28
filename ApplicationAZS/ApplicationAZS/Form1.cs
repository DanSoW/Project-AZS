using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ApplicationAZS
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        //кэш
        DataElementAZS currentData = null;

        private void button1_Click(object sender, EventArgs e)
        {
            var url = "http://localhost:8080/getStationInfo?id=";
            url += textBox1.Text;

            var httpRequest = (;)WebRequest.Create(url);

            var httpResponse = (HttpWebResponse)httpRequest.GetResponse();
            using (var streamReader = new StreamReader(httpResponse.GetResponseStream()))
            {
                var result = streamReader.ReadToEnd();
                currentData = JsonConvert.DeserializeObject<DataElementAZS>(result.ToString());
            }

            if (currentData == null)
            {
                MessageBox.Show("Ошибка! Автозаправочной станции с данным идентификатором нет в базе данных!");
                return;
            }

            this.richTextBox1.Text = this.richTextBox1.Text + "Идентификатор заправочной станции: " + currentData.station_Id + "\nАдрес заправочной станции: " + currentData.address;

            url = "http://localhost:8080/getFuelInfo/all/?id=";
            url += textBox1.Text;

            httpRequest = (HttpWebRequest)WebRequest.Create(url);
            httpResponse = (HttpWebResponse)httpRequest.GetResponse();

            List<DataElementOil> dataOil = new List<DataElementOil>();
            using (var streamReader = new StreamReader(httpResponse.GetResponseStream()))
            {
                var result = streamReader.ReadToEnd();
                dataOil = JsonConvert.DeserializeObject<List<DataElementOil>>(result.ToString());
            }

            for (int i = 0; i < dataOil.Count; i++)
            {
                dataGridView1.Rows.Add(
                    dataOil[i].name, dataOil[i].price, dataOil[i].amountOfFuel);
            }
        }

        private bool validName(string name)
        {
            string[] names = new string[]
            {
                "92", "95", "98", "DT", "Disel Fuel"
            };

            for (int i = 0; i < names.Length; i++)
                if (name.Equals(names[i]))
                    return true;
            return false;
        }

        private void button3_Click(object sender, EventArgs e)
        {
            if ((!validName(textBox2.Text)) || (textBox3.Text.Length == 0) || (textBox4.Text.Length == 0))
            {
                MessageBox.Show("Ошибка!", "Не корректные входные данные!");
                return;
            }

            try
            {
                float.Parse(textBox3.Text);
                int.Parse(textBox4.Text);
            }
            catch (Exception)
            {
                MessageBox.Show("Ошибка!", "Не корректные входные данные!");
                return;
            }

            for(int i = 0; i < dataGridView1.Rows.Count; i++)
            {
                if (dataGridView1.Rows[i].Cells[0].Value.ToString().Equals(textBox2.Text))
                {
                    MessageBox.Show("Ошибка!", "Данная запись уже присутствует в таблице!");
                    return;
                }
            }

            dataGridView1.Rows.Add(textBox2.Text, textBox3.Text, textBox4.Text);
        }

        private void button4_Click(object sender, EventArgs e)
        {
            if ((!validName(textBox2.Text)) || (textBox3.Text.Length == 0) || (textBox4.Text.Length == 0))
            {
                MessageBox.Show("Ошибка!", "Не корректные входные данные!");
                return;
            }

            try
            {
                float.Parse(textBox3.Text);
                int.Parse(textBox4.Text);
            }
            catch (Exception)
            {
                MessageBox.Show("Ошибка!", "Не корректные входные данные!");
                return;
            }

            for(int i = 0; i < dataGridView1.Rows.Count; i++)
            {
                if (dataGridView1.Rows[i].Cells[0].Value.ToString().Equals(textBox2.Text))
                {
                    dataGridView1.Rows[i].Cells[1].Value = textBox3.Text;
                    dataGridView1.Rows[i].Cells[2].Value = textBox4.Text;
                    break;
                }
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            var url = "http://localhost:8080/updateStation";

            var httpRequest = (HttpWebRequest)WebRequest.Create(url);
            httpRequest.Method = "POST";

            httpRequest.ContentType = "application/json";


            List<DataElementOilW> oils = new List<DataElementOilW>();
            for(int i = 0; i < dataGridView1.Rows.Count; i++)
            {
                oils.Add(
                    new DataElementOilW(dataGridView1.Rows[i].Cells[0].Value.ToString(),
                    float.Parse(dataGridView1.Rows[i].Cells[1].Value.ToString()),
                    int.Parse(dataGridView1.Rows[i].Cells[2].Value.ToString())));
            }

            DataElementAZSInfo data = new DataElementAZSInfo(currentData.address, currentData.station_Id, oils);
            string result = JsonConvert.SerializeObject(data);

            using (var streamWriter = new StreamWriter(httpRequest.GetRequestStream()))
            {
                MessageBox.Show(result);
                streamWriter.Write(result);
            }


            var httpResponse = (HttpWebResponse)httpRequest.GetResponse();
            using (var streamReader = new StreamReader(httpResponse.GetResponseStream()))
            {
                result = streamReader.ReadToEnd();
            }

            Console.WriteLine(httpResponse.StatusCode);

        }
    }

    public class DataElementAZSInfo
    {
        public String address;
        public short station_Id;
        public List<DataElementOilW> data;

        public DataElementAZSInfo(String address, short station_Id, List<DataElementOilW> data)
        {
            this.station_Id = station_Id;
            this.address = address;
            this.data = data;
        }
    }
    public class DataElementOilW
    {
        public String name;
        public float price;
        public int amountOfFuel;

        public DataElementOilW(String name, float price, int amountOfFuel)
        {
            this.name = name;
            this.price = price;
            this.amountOfFuel = amountOfFuel;
        }
    }

    public class DataElementOil
    {
        public String name;
        public float price;
        public int amountOfFuel;
        public int station_Id;

        public DataElementOil(String name, float price, int amountOfFuel, int fkStationId)
        {
            this.name = name;
            this.price = price;
            this.amountOfFuel = amountOfFuel;
            this.station_Id = fkStationId;
        }
    }

    public class DataElementAZS
    {
        public short station_Id;
        public String address;

        public DataElementAZS(short station_Id, String address)
        {
            this.station_Id = station_Id;
            this.address = address;
        }
    }
}
