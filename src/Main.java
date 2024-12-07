    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Задача №1", "Поспать", Status.IN_PROGRESS);
        manager.addTask(task1);
        Task task2 = new Task("Задача №2", "Еще поспать", Status.NEW);
        manager.addTask(task2);

        Epic epic1 = new Epic("Эпик №1", "Съездить в отпуск");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Эпик №1, Подзадача №1", "Найти тур", Status.DONE, epic1.getId());
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Эпик №1, Подзадача №2", "Оплата", Status.IN_PROGRESS, epic1.getId());
        manager.addSubtask(subtask12);

        Epic epic2 = new Epic("Эпик №2", "Подумать о смысле жизни");
        manager.addEpic(epic2);
        Subtask subtask21 = new Subtask("Эпик №2, Подзадача №1", "Думать", Status.IN_PROGRESS, epic2.getId());
        manager.addSubtask(subtask21);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        task1.setStatus(Status.DONE);
        task2.setStatus(Status.IN_PROGRESS);
        subtask11.setStatus(Status.IN_PROGRESS);
        subtask12.setStatus(Status.DONE);
        subtask21.setStatus(Status.DONE);

        manager.updateTask(task1);
        manager.updateTask(task2);
        manager.updateSubtask(subtask11);
        manager.updateSubtask(subtask12);
        manager.updateSubtask(subtask21);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        manager.deleteTaskById(task1.getId());
        manager.deleteEpicById(epic2.getId());

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getSubtaskById(4));

        System.out.println(manager.getHistory());
    }

