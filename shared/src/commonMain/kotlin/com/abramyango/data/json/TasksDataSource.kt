package com.abramyango.data.json

import com.abramyango.domain.model.*

/**
 * Источник данных для задач
 * Задачи адаптированы из задачника М.Э. Абрамяна "1000 задач по программированию"
 */
class TasksDataSource {
    
    private val tasks: Map<String, List<Task>> = mapOf(
        Worlds.VALLEY_OF_BEGINNINGS to createValleyTasks(),
        Worlds.FIELDS_OF_TRUTH to createFieldsTasks()
    )
    
    fun getTasksForWorld(worldId: String): List<Task> {
        return tasks[worldId] ?: emptyList()
    }
    
    fun getTask(taskId: String): Task? {
        return tasks.values.flatten().find { it.id == taskId }
    }
    
    /**
     * Задачи для Мира 1: Долина Начинаний (Begin + Integer)
     * Темы: переменные, формулы, ввод-вывод, целые числа
     */
    private fun createValleyTasks(): List<Task> = listOf(
        // Begin1 - Периметр квадрата
        Task(
            id = "valley_begin_001",
            worldId = Worlds.VALLEY_OF_BEGINNINGS,
            mechanic = TaskMechanic.FILL_BLANKS,
            difficulty = 1,
            code = MultiLanguageCode(
                python = "a = float(input())\nP = ___ * a\nprint(P)",
                javascript = "let a = parseFloat(prompt());\nlet P = ___ * a;\nconsole.log(P);",
                kotlin = "val a = readLine()!!.toDouble()\nval P = ___ * a\nprintln(P)",
                csharp = "double a = double.Parse(Console.ReadLine());\ndouble P = ___ * a;\nConsole.WriteLine(P);"
            ),
            storyContextKey = "task_begin_001_story",
            xpReward = 10,
            order = 1,
            abramyanId = "Begin1"
        ),
        
        // Begin2 - Площадь квадрата
        Task(
            id = "valley_begin_002",
            worldId = Worlds.VALLEY_OF_BEGINNINGS,
            mechanic = TaskMechanic.DRAG_DROP,
            difficulty = 1,
            code = MultiLanguageCode(
                python = "a = float(input())\nS = a * a\nprint(S)",
                javascript = "let a = parseFloat(prompt());\nlet S = a * a;\nconsole.log(S);",
                kotlin = "val a = readLine()!!.toDouble()\nval S = a * a\nprintln(S)",
                csharp = "double a = double.Parse(Console.ReadLine());\ndouble S = a * a;\nConsole.WriteLine(S);"
            ),
            storyContextKey = "task_begin_002_story",
            xpReward = 10,
            order = 2,
            abramyanId = "Begin2"
        ),
        
        // Begin3 - Площадь и периметр прямоугольника
        Task(
            id = "valley_begin_003",
            worldId = Worlds.VALLEY_OF_BEGINNINGS,
            mechanic = TaskMechanic.OUTPUT_PREDICTION,
            difficulty = 1,
            code = MultiLanguageCode(
                python = "a = 5\nb = 3\nS = a * b\nP = 2 * (a + b)\nprint(S, P)",
                javascript = "let a = 5;\nlet b = 3;\nlet S = a * b;\nlet P = 2 * (a + b);\nconsole.log(S, P);",
                kotlin = "val a = 5\nval b = 3\nval S = a * b\nval P = 2 * (a + b)\nprintln(\"\${'$'}S \${'$'}P\")",
                csharp = "int a = 5;\nint b = 3;\nint S = a * b;\nint P = 2 * (a + b);\nConsole.WriteLine($\"{S} {P}\");"
            ),
            storyContextKey = "task_begin_003_story",
            xpReward = 15,
            order = 3,
            abramyanId = "Begin3"
        ),
        
        // Begin7 - Длина окружности и площадь круга
        Task(
            id = "valley_begin_007",
            worldId = Worlds.VALLEY_OF_BEGINNINGS,
            mechanic = TaskMechanic.FILL_BLANKS,
            difficulty = 2,
            code = MultiLanguageCode(
                python = "R = float(input())\npi = 3.14\nL = 2 * ___ * R\nS = pi * R * ___\nprint(L, S)",
                javascript = "let R = parseFloat(prompt());\nlet pi = 3.14;\nlet L = 2 * ___ * R;\nlet S = pi * R * ___;\nconsole.log(L, S);",
                kotlin = "val R = readLine()!!.toDouble()\nval pi = 3.14\nval L = 2 * ___ * R\nval S = pi * R * ___\nprintln(\"\${'$'}L \${'$'}S\")",
                csharp = "double R = double.Parse(Console.ReadLine());\ndouble pi = 3.14;\ndouble L = 2 * ___ * R;\ndouble S = pi * R * ___;\nConsole.WriteLine($\"{L} {S}\");"
            ),
            storyContextKey = "task_begin_007_story",
            xpReward = 20,
            order = 4,
            abramyanId = "Begin7"
        ),
        
        // Begin8 - Среднее арифметическое
        Task(
            id = "valley_begin_008",
            worldId = Worlds.VALLEY_OF_BEGINNINGS,
            mechanic = TaskMechanic.BUG_HUNT,
            difficulty = 1,
            code = MultiLanguageCode(
                python = "a = float(input())\nb = float(input())\nmean = a + b / 2  # Bug: should be (a + b) / 2\nprint(mean)",
                javascript = "let a = parseFloat(prompt());\nlet b = parseFloat(prompt());\nlet mean = a + b / 2;  // Bug\nconsole.log(mean);",
                kotlin = "val a = readLine()!!.toDouble()\nval b = readLine()!!.toDouble()\nval mean = a + b / 2  // Bug\nprintln(mean)",
                csharp = "double a = double.Parse(Console.ReadLine());\ndouble b = double.Parse(Console.ReadLine());\ndouble mean = a + b / 2;  // Bug\nConsole.WriteLine(mean);"
            ),
            storyContextKey = "task_begin_008_story",
            xpReward = 20,
            order = 5,
            abramyanId = "Begin8"
        ),
        
        // Begin12 - Гипотенуза треугольника
        Task(
            id = "valley_begin_012",
            worldId = Worlds.VALLEY_OF_BEGINNINGS,
            mechanic = TaskMechanic.DRAG_DROP,
            difficulty = 2,
            code = MultiLanguageCode(
                python = "import math\na = float(input())\nb = float(input())\nc = math.sqrt(a*a + b*b)\nP = a + b + c\nprint(c, P)",
                javascript = "let a = parseFloat(prompt());\nlet b = parseFloat(prompt());\nlet c = Math.sqrt(a*a + b*b);\nlet P = a + b + c;\nconsole.log(c, P);",
                kotlin = "import kotlin.math.sqrt\nval a = readLine()!!.toDouble()\nval b = readLine()!!.toDouble()\nval c = sqrt(a*a + b*b)\nval P = a + b + c\nprintln(\"\${'$'}c \${'$'}P\")",
                csharp = "double a = double.Parse(Console.ReadLine());\ndouble b = double.Parse(Console.ReadLine());\ndouble c = Math.Sqrt(a*a + b*b);\ndouble P = a + b + c;\nConsole.WriteLine($\"{c} {P}\");"
            ),
            storyContextKey = "task_begin_012_story",
            xpReward = 25,
            order = 6,
            abramyanId = "Begin12"
        ),
        
        // Begin22 - Обмен значений переменных
        Task(
            id = "valley_begin_022",
            worldId = Worlds.VALLEY_OF_BEGINNINGS,
            mechanic = TaskMechanic.CODE_TRACE,
            difficulty = 2,
            code = MultiLanguageCode(
                python = "A = 5\nB = 3\ntemp = A\nA = B\nB = temp\nprint(A, B)",
                javascript = "let A = 5;\nlet B = 3;\nlet temp = A;\nA = B;\nB = temp;\nconsole.log(A, B);",
                kotlin = "var A = 5\nvar B = 3\nval temp = A\nA = B\nB = temp\nprintln(\"\${'$'}A \${'$'}B\")",
                csharp = "int A = 5;\nint B = 3;\nint temp = A;\nA = B;\nB = temp;\nConsole.WriteLine($\"{A} {B}\");"
            ),
            storyContextKey = "task_begin_022_story",
            xpReward = 25,
            order = 7,
            abramyanId = "Begin22"
        ),
        
        // Begin27 - Степени числа (A^8)
        Task(
            id = "valley_begin_027",
            worldId = Worlds.VALLEY_OF_BEGINNINGS,
            mechanic = TaskMechanic.CODE_TRACE,
            difficulty = 2,
            code = MultiLanguageCode(
                python = "A = 2\nA2 = A * A\nA4 = A2 * A2\nA8 = A4 * A4\nprint(A2, A4, A8)",
                javascript = "let A = 2;\nlet A2 = A * A;\nlet A4 = A2 * A2;\nlet A8 = A4 * A4;\nconsole.log(A2, A4, A8);",
                kotlin = "val A = 2\nval A2 = A * A\nval A4 = A2 * A2\nval A8 = A4 * A4\nprintln(\"\${'$'}A2 \${'$'}A4 \${'$'}A8\")",
                csharp = "int A = 2;\nint A2 = A * A;\nint A4 = A2 * A2;\nint A8 = A4 * A4;\nConsole.WriteLine($\"{A2} {A4} {A8}\");"
            ),
            storyContextKey = "task_begin_027_story",
            xpReward = 25,
            order = 8,
            abramyanId = "Begin27"
        ),
        
        // Integer3 - Килобайты из байтов
        Task(
            id = "valley_int_003",
            worldId = Worlds.VALLEY_OF_BEGINNINGS,
            mechanic = TaskMechanic.FILL_BLANKS,
            difficulty = 1,
            code = MultiLanguageCode(
                python = "bytes = int(input())\nkb = bytes ___ 1024\nprint(kb)",
                javascript = "let bytes = parseInt(prompt());\nlet kb = Math.floor(bytes ___ 1024);\nconsole.log(kb);",
                kotlin = "val bytes = readLine()!!.toInt()\nval kb = bytes ___ 1024\nprintln(kb)",
                csharp = "int bytes = int.Parse(Console.ReadLine());\nint kb = bytes ___ 1024;\nConsole.WriteLine(kb);"
            ),
            storyContextKey = "task_int_003_story",
            xpReward = 15,
            order = 9,
            abramyanId = "Integer3"
        ),
        
        // Integer6 - Разбор двузначного числа
        Task(
            id = "valley_int_006",
            worldId = Worlds.VALLEY_OF_BEGINNINGS,
            mechanic = TaskMechanic.OUTPUT_PREDICTION,
            difficulty = 2,
            code = MultiLanguageCode(
                python = "N = 57\ntens = N // 10\nunits = N % 10\nprint(tens, units)",
                javascript = "let N = 57;\nlet tens = Math.floor(N / 10);\nlet units = N % 10;\nconsole.log(tens, units);",
                kotlin = "val N = 57\nval tens = N / 10\nval units = N % 10\nprintln(\"\${'$'}tens \${'$'}units\")",
                csharp = "int N = 57;\nint tens = N / 10;\nint units = N % 10;\nConsole.WriteLine($\"{tens} {units}\");"
            ),
            storyContextKey = "task_int_006_story",
            xpReward = 20,
            order = 10,
            abramyanId = "Integer6"
        ),
        
        // Integer8 - Перестановка цифр двузначного числа
        Task(
            id = "valley_int_008",
            worldId = Worlds.VALLEY_OF_BEGINNINGS,
            mechanic = TaskMechanic.DRAG_DROP,
            difficulty = 2,
            code = MultiLanguageCode(
                python = "N = int(input())\ntens = N // 10\nunits = N % 10\nresult = units * 10 + tens\nprint(result)",
                javascript = "let N = parseInt(prompt());\nlet tens = Math.floor(N / 10);\nlet units = N % 10;\nlet result = units * 10 + tens;\nconsole.log(result);",
                kotlin = "val N = readLine()!!.toInt()\nval tens = N / 10\nval units = N % 10\nval result = units * 10 + tens\nprintln(result)",
                csharp = "int N = int.Parse(Console.ReadLine());\nint tens = N / 10;\nint units = N % 10;\nint result = units * 10 + tens;\nConsole.WriteLine(result);"
            ),
            storyContextKey = "task_int_008_story",
            xpReward = 25,
            order = 11,
            abramyanId = "Integer8"
        ),
        
        // Integer11 - Сумма цифр трёхзначного числа
        Task(
            id = "valley_int_011",
            worldId = Worlds.VALLEY_OF_BEGINNINGS,
            mechanic = TaskMechanic.BUG_HUNT,
            difficulty = 3,
            code = MultiLanguageCode(
                python = "N = 345\nhundreds = N // 100\ntens = (N // 10) % 10\nunits = N % 10\nsum = hundreds + tens  # Bug: forgot units\nprint(sum)",
                javascript = "let N = 345;\nlet hundreds = Math.floor(N / 100);\nlet tens = Math.floor(N / 10) % 10;\nlet units = N % 10;\nlet sum = hundreds + tens;  // Bug\nconsole.log(sum);",
                kotlin = "val N = 345\nval hundreds = N / 100\nval tens = (N / 10) % 10\nval units = N % 10\nval sum = hundreds + tens  // Bug\nprintln(sum)",
                csharp = "int N = 345;\nint hundreds = N / 100;\nint tens = (N / 10) % 10;\nint units = N % 10;\nint sum = hundreds + tens;  // Bug\nConsole.WriteLine(sum);"
            ),
            storyContextKey = "task_int_011_story",
            xpReward = 30,
            order = 12,
            abramyanId = "Integer11"
        ),
        
        // Integer20 - Количество часов с начала суток
        Task(
            id = "valley_int_020",
            worldId = Worlds.VALLEY_OF_BEGINNINGS,
            mechanic = TaskMechanic.FILL_BLANKS,
            difficulty = 2,
            code = MultiLanguageCode(
                python = "N = int(input())  # секунды с начала суток\nhours = N // ___\nprint(hours)",
                javascript = "let N = parseInt(prompt());  // секунды\nlet hours = Math.floor(N / ___);\nconsole.log(hours);",
                kotlin = "val N = readLine()!!.toInt()  // секунды\nval hours = N / ___\nprintln(hours)",
                csharp = "int N = int.Parse(Console.ReadLine());  // секунды\nint hours = N / ___;\nConsole.WriteLine(hours);"
            ),
            storyContextKey = "task_int_020_story",
            xpReward = 20,
            order = 13,
            abramyanId = "Integer20"
        )
    )
    
    /**
     * Задачи для Мира 2: Поля Истины (Boolean)
     * Темы: логические выражения, условия
     */
    private fun createFieldsTasks(): List<Task> = listOf(
        // Boolean1 - Число положительное?
        Task(
            id = "fields_bool_001",
            worldId = Worlds.FIELDS_OF_TRUTH,
            mechanic = TaskMechanic.OUTPUT_PREDICTION,
            difficulty = 1,
            code = MultiLanguageCode(
                python = "A = -5\nresult = A > 0\nprint(result)",
                javascript = "let A = -5;\nlet result = A > 0;\nconsole.log(result);",
                kotlin = "val A = -5\nval result = A > 0\nprintln(result)",
                csharp = "int A = -5;\nbool result = A > 0;\nConsole.WriteLine(result);"
            ),
            storyContextKey = "task_bool_001_story",
            xpReward = 10,
            order = 1,
            abramyanId = "Boolean1"
        ),
        
        // Boolean2 - Число нечётное?
        Task(
            id = "fields_bool_002",
            worldId = Worlds.FIELDS_OF_TRUTH,
            mechanic = TaskMechanic.FILL_BLANKS,
            difficulty = 1,
            code = MultiLanguageCode(
                python = "A = int(input())\nis_odd = A % 2 ___ 0\nprint(is_odd)",
                javascript = "let A = parseInt(prompt());\nlet isOdd = A % 2 ___ 0;\nconsole.log(isOdd);",
                kotlin = "val A = readLine()!!.toInt()\nval isOdd = A % 2 ___ 0\nprintln(isOdd)",
                csharp = "int A = int.Parse(Console.ReadLine());\nbool isOdd = A % 2 ___ 0;\nConsole.WriteLine(isOdd);"
            ),
            storyContextKey = "task_bool_002_story",
            xpReward = 15,
            order = 2,
            abramyanId = "Boolean2"
        ),
        
        // Boolean6 - Двойное неравенство A < B < C
        Task(
            id = "fields_bool_006",
            worldId = Worlds.FIELDS_OF_TRUTH,
            mechanic = TaskMechanic.OUTPUT_PREDICTION,
            difficulty = 2,
            code = MultiLanguageCode(
                python = "A = 3\nB = 5\nC = 7\nresult = A < B and B < C\nprint(result)",
                javascript = "let A = 3;\nlet B = 5;\nlet C = 7;\nlet result = A < B && B < C;\nconsole.log(result);",
                kotlin = "val A = 3\nval B = 5\nval C = 7\nval result = A < B && B < C\nprintln(result)",
                csharp = "int A = 3;\nint B = 5;\nint C = 7;\nbool result = A < B && B < C;\nConsole.WriteLine(result);"
            ),
            storyContextKey = "task_bool_006_story",
            xpReward = 20,
            order = 3,
            abramyanId = "Boolean6"
        ),
        
        // Boolean7 - B между A и C
        Task(
            id = "fields_bool_007",
            worldId = Worlds.FIELDS_OF_TRUTH,
            mechanic = TaskMechanic.BUG_HUNT,
            difficulty = 2,
            code = MultiLanguageCode(
                python = "A = 10\nB = 5\nC = 1\n# B между A и C?\nresult = A < B < C  # Bug: should check both directions\nprint(result)",
                javascript = "let A = 10;\nlet B = 5;\nlet C = 1;\n// B между A и C?\nlet result = A < B && B < C;  // Bug\nconsole.log(result);",
                kotlin = "val A = 10\nval B = 5\nval C = 1\n// B между A и C?\nval result = A < B && B < C  // Bug\nprintln(result)",
                csharp = "int A = 10;\nint B = 5;\nint C = 1;\n// B между A и C?\nbool result = A < B && B < C;  // Bug\nConsole.WriteLine(result);"
            ),
            storyContextKey = "task_bool_007_story",
            xpReward = 25,
            order = 4,
            abramyanId = "Boolean7"
        ),
        
        // Boolean10 - Ровно одно нечётное
        Task(
            id = "fields_bool_010",
            worldId = Worlds.FIELDS_OF_TRUTH,
            mechanic = TaskMechanic.DRAG_DROP,
            difficulty = 3,
            code = MultiLanguageCode(
                python = "A = 4\nB = 7\na_odd = A % 2 != 0\nb_odd = B % 2 != 0\nresult = a_odd != b_odd\nprint(result)",
                javascript = "let A = 4;\nlet B = 7;\nlet aOdd = A % 2 !== 0;\nlet bOdd = B % 2 !== 0;\nlet result = aOdd !== bOdd;\nconsole.log(result);",
                kotlin = "val A = 4\nval B = 7\nval aOdd = A % 2 != 0\nval bOdd = B % 2 != 0\nval result = aOdd != bOdd\nprintln(result)",
                csharp = "int A = 4;\nint B = 7;\nbool aOdd = A % 2 != 0;\nbool bOdd = B % 2 != 0;\nbool result = aOdd != bOdd;\nConsole.WriteLine(result);"
            ),
            storyContextKey = "task_bool_010_story",
            xpReward = 30,
            order = 5,
            abramyanId = "Boolean10"
        ),
        
        // Boolean24 - Дискриминант квадратного уравнения
        Task(
            id = "fields_bool_024",
            worldId = Worlds.FIELDS_OF_TRUTH,
            mechanic = TaskMechanic.CODE_TRACE,
            difficulty = 3,
            code = MultiLanguageCode(
                python = "A = 1\nB = 4\nC = 3\nD = B*B - 4*A*C\nhas_roots = D >= 0\nprint(D, has_roots)",
                javascript = "let A = 1;\nlet B = 4;\nlet C = 3;\nlet D = B*B - 4*A*C;\nlet hasRoots = D >= 0;\nconsole.log(D, hasRoots);",
                kotlin = "val A = 1\nval B = 4\nval C = 3\nval D = B*B - 4*A*C\nval hasRoots = D >= 0\nprintln(\"\${'$'}D \${'$'}hasRoots\")",
                csharp = "int A = 1;\nint B = 4;\nint C = 3;\nint D = B*B - 4*A*C;\nbool hasRoots = D >= 0;\nConsole.WriteLine($\"{D} {hasRoots}\");"
            ),
            storyContextKey = "task_bool_024_story",
            xpReward = 30,
            order = 6,
            abramyanId = "Boolean24"
        ),
        
        // Boolean29 - Точка внутри прямоугольника
        Task(
            id = "fields_bool_029",
            worldId = Worlds.FIELDS_OF_TRUTH,
            mechanic = TaskMechanic.FILL_BLANKS,
            difficulty = 3,
            code = MultiLanguageCode(
                python = "x, y = 3, 4\nx1, y1 = 1, 5  # верхний левый угол\nx2, y2 = 6, 2  # нижний правый угол\ninside = x1 ___ x ___ x2 and y2 ___ y ___ y1\nprint(inside)",
                javascript = "let x = 3, y = 4;\nlet x1 = 1, y1 = 5;\nlet x2 = 6, y2 = 2;\nlet inside = x1 ___ x && x ___ x2 && y2 ___ y && y ___ y1;\nconsole.log(inside);",
                kotlin = "val x = 3; val y = 4\nval x1 = 1; val y1 = 5\nval x2 = 6; val y2 = 2\nval inside = x1 ___ x && x ___ x2 && y2 ___ y && y ___ y1\nprintln(inside)",
                csharp = "int x = 3, y = 4;\nint x1 = 1, y1 = 5;\nint x2 = 6, y2 = 2;\nbool inside = x1 ___ x && x ___ x2 && y2 ___ y && y ___ y1;\nConsole.WriteLine(inside);"
            ),
            storyContextKey = "task_bool_029_story",
            xpReward = 35,
            order = 7,
            abramyanId = "Boolean29"
        ),
        
        // Boolean33 - Существует ли треугольник?
        Task(
            id = "fields_bool_033",
            worldId = Worlds.FIELDS_OF_TRUTH,
            mechanic = TaskMechanic.OUTPUT_PREDICTION,
            difficulty = 3,
            code = MultiLanguageCode(
                python = "a = 3\nb = 4\nc = 10\nexists = a + b > c and a + c > b and b + c > a\nprint(exists)",
                javascript = "let a = 3;\nlet b = 4;\nlet c = 10;\nlet exists = a + b > c && a + c > b && b + c > a;\nconsole.log(exists);",
                kotlin = "val a = 3\nval b = 4\nval c = 10\nval exists = a + b > c && a + c > b && b + c > a\nprintln(exists)",
                csharp = "int a = 3;\nint b = 4;\nint c = 10;\nbool exists = a + b > c && a + c > b && b + c > a;\nConsole.WriteLine(exists);"
            ),
            storyContextKey = "task_bool_033_story",
            xpReward = 30,
            order = 8,
            abramyanId = "Boolean33"
        ),
        
        // Boolean36 - Ход ладьи
        Task(
            id = "fields_bool_036",
            worldId = Worlds.FIELDS_OF_TRUTH,
            mechanic = TaskMechanic.FILL_BLANKS,
            difficulty = 2,
            code = MultiLanguageCode(
                python = "x1, y1 = 1, 1\nx2, y2 = 1, 5\ncan_move = x1 ___ x2 or y1 ___ y2\nprint(can_move)",
                javascript = "let x1 = 1, y1 = 1;\nlet x2 = 1, y2 = 5;\nlet canMove = x1 ___ x2 || y1 ___ y2;\nconsole.log(canMove);",
                kotlin = "val x1 = 1; val y1 = 1\nval x2 = 1; val y2 = 5\nval canMove = x1 ___ x2 || y1 ___ y2\nprintln(canMove)",
                csharp = "int x1 = 1, y1 = 1;\nint x2 = 1, y2 = 5;\nbool canMove = x1 ___ x2 || y1 ___ y2;\nConsole.WriteLine(canMove);"
            ),
            storyContextKey = "task_bool_036_story",
            xpReward = 25,
            order = 9,
            abramyanId = "Boolean36"
        ),
        
        // Boolean40 - Ход коня
        Task(
            id = "fields_bool_040",
            worldId = Worlds.FIELDS_OF_TRUTH,
            mechanic = TaskMechanic.BUG_HUNT,
            difficulty = 4,
            code = MultiLanguageCode(
                python = "x1, y1 = 1, 1\nx2, y2 = 2, 3\ndx = abs(x2 - x1)\ndy = abs(y2 - y1)\ncan_move = dx == 2 and dy == 1  # Bug: missing second case\nprint(can_move)",
                javascript = "let x1 = 1, y1 = 1;\nlet x2 = 2, y2 = 3;\nlet dx = Math.abs(x2 - x1);\nlet dy = Math.abs(y2 - y1);\nlet canMove = dx === 2 && dy === 1;  // Bug\nconsole.log(canMove);",
                kotlin = "val x1 = 1; val y1 = 1\nval x2 = 2; val y2 = 3\nval dx = kotlin.math.abs(x2 - x1)\nval dy = kotlin.math.abs(y2 - y1)\nval canMove = dx == 2 && dy == 1  // Bug\nprintln(canMove)",
                csharp = "int x1 = 1, y1 = 1;\nint x2 = 2, y2 = 3;\nint dx = Math.Abs(x2 - x1);\nint dy = Math.Abs(y2 - y1);\nbool canMove = dx == 2 && dy == 1;  // Bug\nConsole.WriteLine(canMove);"
            ),
            storyContextKey = "task_bool_040_story",
            xpReward = 40,
            order = 10,
            abramyanId = "Boolean40"
        )
    )
}
