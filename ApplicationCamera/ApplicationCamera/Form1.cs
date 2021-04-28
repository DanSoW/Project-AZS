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
using Newtonsoft.Json;

namespace ApplicationCamera
{
    public partial class Form1 : Form
    {

        private int currentIndexImage = 0;                  //текущий индекс отображаемого элемента
        private List<string> filePath = new List<string>(); //пути к изображениям для выбора

        public Form1()
        {
            InitializeComponent();
            filePath.Add(@"C:\Server\ApplicationCamera\ApplicationCamera\UndefinedImage\E902НМ73.jpg");
            filePath.Add(@"C:\Server\ApplicationCamera\ApplicationCamera\UndefinedImage\А002АЕ154.jpg");
            filePath.Add(@"C:\Server\ApplicationCamera\ApplicationCamera\UndefinedImage\А095НМ177.jpg");
            filePath.Add(@"C:\Server\ApplicationCamera\ApplicationCamera\UndefinedImage\Е908ВО154.jpg");
            filePath.Add(@"C:\Server\ApplicationCamera\ApplicationCamera\UndefinedImage\Р241НУ177.jpg");
            filePath.Add(@"C:\Server\ApplicationCamera\ApplicationCamera\UndefinedImage\Р319ХМ197.png");
            filePath.Add(@"C:\Server\ApplicationCamera\ApplicationCamera\UndefinedImage\Р968НМ96.jpg");
            filePath.Add(@"C:\Server\ApplicationCamera\ApplicationCamera\UndefinedImage\С758ОВ777.jpg");
            filePath.Add(@"C:\Server\ApplicationCamera\ApplicationCamera\UndefinedImage\С880ОТ150.jpg");
            filePath.Add(@"C:\Server\ApplicationCamera\ApplicationCamera\UndefinedImage\Х136НЕ69.jpg");
        }

        private void addDataElementCameraInDataBase(DataElementCamera t)
        {
            var url = "http://localhost:8080/addDataCamera";

            var httpRequest = (HttpWebRequest)WebRequest.Create(url);
            httpRequest.Method = "POST";

            httpRequest.ContentType = "application/json";

            string data = JsonConvert.SerializeObject(t);

            using (var streamWriter = new StreamWriter(httpRequest.GetRequestStream()))
            {
                streamWriter.Write(data);
            }

            var httpResponse = (HttpWebResponse)httpRequest.GetResponse();
            using (var streamReader = new StreamReader(httpResponse.GetResponseStream()))
            {
                var result = streamReader.ReadToEnd();
            }

            Console.WriteLine(httpResponse.StatusCode);
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if(openFileDialog1.ShowDialog() != DialogResult.OK)
            {
                return;
            }

            string text = File.ReadAllText(openFileDialog1.FileName);
            text = text.Replace('\'', ' ');
            string[] textArray = text.Split(new char[] { '\n' });

            for(int i = 1; i < (textArray.Length-1); i ++)
            {
                string[] result = textArray[i].Split(new char[] { ',' });
                dataGridView1.Rows.Add(result[0], result[1], result[2], result[3]);

                if ((result[0].Length != 0) && (result[1].Length != 0) && (result[2].Length != 0)
                    && (result[3].Length != 0))
                    addDataElementCameraInDataBase(new DataElementCamera(result[0], Boolean.Parse(result[1].ToLower()), result[2], result[3]));
            }

            for(int i = 0; i < dataGridView1.Rows.Count; i++)
            {
                if(dataGridView1.Rows[i].Cells[2].Value.ToString().Length == 0)
                {
                    System.Console.Beep();
                    MessageBox.Show("Государственный номер не распознан, работа с накопительной картой невозможна!", "Ошибка!");
                    string[] r = filePath[currentIndexImage].Split(new char[] { '\\' });
                    pictureBox1.Image = Image.FromFile(filePath[currentIndexImage]);
                    textBox1.Text = r[r.Length-1];
                    break;
                }
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            if ((currentIndexImage + 1) >= filePath.Count)
                currentIndexImage = 0;
            else
                currentIndexImage++;

            string[] text = filePath[currentIndexImage].Split(new char[] { '\\' });
            pictureBox1.Image = Image.FromFile(filePath[currentIndexImage]);
            textBox1.Text = text[text.Length - 1];
        }

        private void button3_Click(object sender, EventArgs e)
        {
            if ((currentIndexImage - 1) < 0)
                currentIndexImage = (filePath.Count-1);
            else
                currentIndexImage--;

            string[] text = filePath[currentIndexImage].Split(new char[] { '\\' });
            pictureBox1.Image = Image.FromFile(filePath[currentIndexImage]);
            textBox1.Text = text[text.Length - 1];
        }

        private void button4_Click(object sender, EventArgs e)
        {
            if(dataGridView1.SelectedRows == null)
            {
                MessageBox.Show("Необходимо выбрать строку в таблице!", "Ошибка!");
                return;
            }

            int index = (-1);

            for (int i = 0; i < dataGridView1.Rows.Count; i++)
            {
                if (dataGridView1.Rows[i].Equals(dataGridView1.SelectedRows[0]))
                {
                    index = i;
                    break;
                }
            }

            if (index >= 0)
            {
                string[] text = filePath[currentIndexImage].Split(new char[] { '\\' });
                dataGridView1.Rows[index].Cells[2].Value = text[text.Length - 1].Split(new char[] { '.' })[0];
                dataGridView1.Rows[index].Cells[3].Value = text[text.Length - 1];
                addDataElementCameraInDataBase(new DataElementCamera(
                    dataGridView1.Rows[index].Cells[0].Value.ToString(),
                    Boolean.Parse(dataGridView1.Rows[index].Cells[1].Value.ToString().ToLower()),
                    dataGridView1.Rows[index].Cells[2].Value.ToString(),
                    dataGridView1.Rows[index].Cells[3].Value.ToString()));
            }
        }

        private void button5_Click(object sender, EventArgs e)
        {
            if (openFileDialog1.ShowDialog() != DialogResult.OK)
                return;

            filePath.Add(openFileDialog1.FileName);
            currentIndexImage = filePath.Count - 1;
            pictureBox1.Image = Image.FromFile(filePath[currentIndexImage]);
            string[] text = filePath[currentIndexImage].Split(new char[] { '\\' });
            textBox1.Text = text[text.Length - 1];
        }
    }

    public class DataElementCamera
    {
        public String date;
        public bool recognize;
        public String number;
        public String image;

        public DataElementCamera(String date, bool recognize, String number, String image)
        {
            this.date = date;
            this.recognize = recognize;
            this.number = number;
            this.image = image;
        }
    }
}
