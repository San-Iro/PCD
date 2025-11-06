using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;

namespace Variant5
{
    internal class Program
    {
        static int[] a;
        static List<int> evenPosElems = new List<int>();

        // synchronization objects
        static ManualResetEvent doneTh1 = new(false);
        static ManualResetEvent doneTh2 = new(false);
        static ManualResetEvent doneTh3 = new(false);
        static ManualResetEvent doneTh4 = new(false);

        static void Main(string[] args)
        {
            Console.OutputEncoding = System.Text.Encoding.UTF8;
            Random rnd = new Random();
            int N = 20; // array length
            a = new int[N];
            Console.WriteLine("Array elements:");
            for (int i = 0; i < N; i++)
            {
                a[i] = rnd.Next(1, 100);
                Console.Write(a[i] + " ");
            }
            Console.WriteLine("\n---");

            // get even-position elements (positions counted from 1)
            for (int i = 0; i < a.Length; i++)
            {
                if ((i + 1) % 2 == 0)
                    evenPosElems.Add(a[i]);
            }

            Console.WriteLine("Even-position elements: " + string.Join(", ", evenPosElems));

            Thread th1 = new Thread(Th1);
            Thread th2 = new Thread(Th2);
            Thread th3 = new Thread(Th3);
            Thread th4 = new Thread(Th4);

            th1.Start();
            th2.Start();
            th3.Start();
            th4.Start();

            // print order: Th2 → Th4 → Th1 → Th3
            doneTh2.WaitOne();
            PrintWithDelay("ИмяСтудента");
            doneTh4.WaitOne();
            PrintWithDelay("Группа123");
            doneTh1.WaitOne();
            PrintWithDelay("Фамилия");
            doneTh3.WaitOne();
            PrintWithDelay("Параллельные и распределённые вычисления");

            Console.WriteLine("\nВсе потоки завершены.");
            Console.ReadKey();
        }

        static void Th1()
        {
            Console.WriteLine("\nTh1: прямой обход — произведения пар с четных позиций");
            long sum = 0;
            for (int i = 0; i + 1 < evenPosElems.Count; i += 2)
            {
                int prod = evenPosElems[i] * evenPosElems[i + 1];
                sum += prod;
                Console.WriteLine($"Th1 пара ({evenPosElems[i]}, {evenPosElems[i + 1]}) → {prod}, сумма={sum}");
                Thread.Sleep(50);
            }
            Console.WriteLine($"Th1 результат (сумма произведений) = {sum}");
            doneTh1.Set();
        }

        static void Th2()
        {
            Console.WriteLine("\nTh2: обратный обход — произведения пар с четных позиций");
            long sum = 0;
            for (int i = evenPosElems.Count - 1; i - 1 >= 0; i -= 2)
            {
                int prod = evenPosElems[i] * evenPosElems[i - 1];
                sum += prod;
                Console.WriteLine($"Th2 пара ({evenPosElems[i]}, {evenPosElems[i - 1]}) → {prod}, сумма={sum}");
                Thread.Sleep(50);
            }
            Console.WriteLine($"Th2 результат (сумма произведений) = {sum}");
            doneTh2.Set();
        }

        static void Th3()
        {
            Console.WriteLine("\nTh3: проход по интервалу [567, 1002] вперёд");
            for (int i = 567; i <= 1002; i++)
            {
                if (i < 577 || i > 992) Console.Write(i + " ");
                else if (i == 577) Console.Write("... ");
                Thread.Sleep(1);
            }
            Console.WriteLine("\nTh3 завершён");
            doneTh3.Set();
        }

        static void Th4()
        {
            Console.WriteLine("\nTh4: проход по интервалу [567, 1100] назад");
            for (int i = 1100; i >= 567; i--)
            {
                if (i > 1090 || i < 577) Console.Write(i + " ");
                else if (i == 1090) Console.Write("... ");
                Thread.Sleep(1);
            }
            Console.WriteLine("\nTh4 завершён");
            doneTh4.Set();
        }

        static void PrintWithDelay(string text)
        {
            foreach (char c in text)
            {
                Console.Write(c);
                Thread.Sleep(100); // 100 ms per character
            }
            Console.WriteLine();
        }
    }
}
