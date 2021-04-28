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

namespace KolonkaApplication
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
            textBox4.Text = "0";
        }

        DataElementAZS currentData = null;

        bool startWork = false;

        private void button2_Click(object sender, EventArgs e)
        {
            if(textBox2.Text.Length == 0)
            {
                MessageBox.Show("Поле ID АЗС должно быть заполнено!", "Ошибка!");
                return;
            }

            try
            {
                if((short.Parse(textBox2.Text) > 99) || (short.Parse(textBox2.Text) == 0))
                {
                    MessageBox.Show("Поле ID АЗС должно быть из диапазона [1; 99]", "Ошибка!");
                    return;
                }
            }
            catch (Exception)
            {
                MessageBox.Show("Не корректные входные данные!", "Ошибка!");
                return;
            }

            dataGridView1.Rows.Clear();

            var url = "http://localhost:8080/getStationInfo?id=";
            url += textBox2.Text;

            var httpRequest = (HttpWebRequest)WebRequest.Create(url);

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

            richTextBox1.Text = currentData.address;

            url = "http://localhost:8080/getFuelInfo/all/?id=";
            url += textBox2.Text;

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

            startWork = true;
        }

        private void button3_Click(object sender, EventArgs e)
        {
            if (!startWork)
            {
                MessageBox.Show("Необходимо начать работу с АЗС!", "Ошибка!");
                return;
            }

            for (int i = 0; i < dataGridView1.Rows.Count; i++)
            {
                if((dataGridView1.Rows[i].Cells[1].Value == null) || (dataGridView1.Rows[i].Cells[1].Value.ToString().Length == 0))
                {
                    MessageBox.Show("Все поля должны быть заполнены!", "Ошибка!");
                    return;
                }
            }

            var url = "http://localhost:8080/updateDataOil/all";

            var httpRequest = (HttpWebRequest)WebRequest.Create(url);
            httpRequest.Method = "POST";

            httpRequest.ContentType = "application/json";


            List<DataElementOil> oils = new List<DataElementOil>();
            for (int i = 0; i < dataGridView1.Rows.Count; i++)
            {
                oils.Add(
                    new DataElementOil(dataGridView1.Rows[i].Cells[0].Value.ToString(),
                    float.Parse(dataGridView1.Rows[i].Cells[1].Value.ToString()),
                    int.Parse(dataGridView1.Rows[i].Cells[2].Value.ToString()),
                    short.Parse(textBox2.Text)));
            }

            string result = JsonConvert.SerializeObject(oils);

            using (var streamWriter = new StreamWriter(httpRequest.GetRequestStream()))
            {
                streamWriter.Write(result);
            }


            var httpResponse = (HttpWebResponse)httpRequest.GetResponse();
            using (var streamReader = new StreamReader(httpResponse.GetResponseStream()))
            {
                result = streamReader.ReadToEnd();
            }
        }


        private void button1_Click(object sender, EventArgs e)
        {
            if (!startWork)
            {
                MessageBox.Show("Необходимо начать работу с АЗС!", "Ошибка!");
                return;
            }

            if(int.Parse(textBox4.Text) >= 500)
            {
                MessageBox.Show("У вас полный бак!", "Ошибка!");
                return;
            }

            int index = (-1);
            for(int i = 0; i < dataGridView1.Rows.Count; i++)
            {
                if (dataGridView1.Rows[i].Cells[0].Value.ToString().Equals(comboBox1.Text))
                {
                    index = i;
                    break;
                }
            }

            if(index < 0)
            {
                MessageBox.Show("Не выбрано топливо для заправки!", "Ошибка!");
                return;
            }

            if(comboBox3.Text.Equals("кредитная карта"))
            {
                if (maskedTextBox1.Text.Length != 19)
                {
                    MessageBox.Show("Необходимо ввести 16-ти значный номер кредитной карты!");
                    return;
                }

                if(textBox1.Text.Split(new char[] { ' ' }).Length != 2)
                {
                    MessageBox.Show("Необходимо ввести фамилию и имя держателя карты!", "Ошибка!");
                    return;
                }

                if (comboBox2.Text.Equals("на определённую сумму"))
                {
                    if(textBox3.Text.Length == 0)
                    {
                        MessageBox.Show("Не введена сумма для заправки!", "Ошибка!");
                        return;
                    }

                    float sum = int.Parse(textBox3.Text);

                    if((sum - float.Parse(dataGridView1.Rows[index].Cells[1].Value.ToString())) < 0)
                    {
                        MessageBox.Show("Недостаточно средств на карте!", "Ошибка!");
                        return;
                    }

                    while ((sum > 0) && ((sum - float.Parse(dataGridView1.Rows[index].Cells[1].Value.ToString())) >= 0))
                    {
                        if (int.Parse(dataGridView1.Rows[index].Cells[2].Value.ToString()) == 0)
                        {
                            MessageBox.Show("На заправке закончилось топливо!", "Информация");
                            return;
                        }

                        if ((int.Parse(textBox4.Text) + 1) >= 500)
                            break;

                        sum -= float.Parse(dataGridView1.Rows[index].Cells[1].Value.ToString());
                        textBox4.Text = (int.Parse(textBox4.Text) + 1).ToString();
                        dataGridView1.Rows[index].Cells[2].Value = (int.Parse(dataGridView1.Rows[index].Cells[2].Value.ToString()) - 1).ToString();
                    }

                    textBox3.Text = ((int)sum).ToString();

                    MessageBox.Show("Заправка окончена!", "Информация");
                    button3_Click(sender, e);
                    return;

                }else if(comboBox2.Text.Equals("определённое количество литров") || comboBox2.Text.Equals("до полного бака c ограничением по объёму"))
                {
                    if (textBox3.Text.Length == 0)
                    {
                        MessageBox.Show("Не введено количество литров для заправки!", "Ошибка!");
                        return;
                    }

                    int count = int.Parse(textBox3.Text);
                    if ((int.Parse(textBox4.Text) + count) > 500)
                    {
                        MessageBox.Show("Бак автомобиля не вместит такое количество топлива!", "Ошибка!");
                        return;
                    }

                    dataGridView1.Rows[index].Cells[2].Value = (int.Parse(dataGridView1.Rows[index].Cells[2].Value.ToString()) - count).ToString();
                    textBox4.Text = (int.Parse(textBox4.Text) + count).ToString();

                    MessageBox.Show("Заправка окончена!", "Информация");
                    button3_Click(sender, e);
                    return;
                }
            }
        }

        private void button4_Click(object sender, EventArgs e)
        {
            if (!startWork)
            {
                MessageBox.Show("Необходимо начать работу с АЗС!", "Ошибка!");
                return;
            }

            textBox4.Text = "0";
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
        public short variable;

        public DataElementOil(String name, float price, int amountOfFuel, short variable)
        {
            this.name = name;
            this.price = price;
            this.amountOfFuel = amountOfFuel;
            this.variable = variable;
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
