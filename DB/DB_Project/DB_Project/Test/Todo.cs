using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DB_Project
{
    class Todo
    {
        public long Id { get; set; }
        public string Content { get; set; }
        public int Order { get; set; }
        public bool Done { get; set; }
    }

    class Todo2
    {
        public long Id { get; set; }
        public string Content { get; set; }
        public string Order { get; set; }
        public bool Done { get; set; }
    }
}
