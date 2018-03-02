using ServiceStack.Redis;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace DB_Project
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();

            //Thread-safe client factory
            var redisManager = new PooledRedisClientManager("localhost:6379");

            redisManager.ExecAs<Todo>(redisTodos => {
                var todo = new Todo
                {
                    Id = redisTodos.GetNextSequence(),
                    Content = "Learn Redis",
                    Order = 1,
                };


                redisTodos.Store(todo);

                Todo savedTodo = redisTodos.GetById(todo.Id);
                savedTodo.Done = true;

                redisTodos.Store(savedTodo);

 //               redisTodos.DeleteById(savedTodo.Id);

                var allTodos = redisTodos.GetAll();

            });
        }

    }
    }
