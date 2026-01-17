import { useState } from "react";

const Task = ({ task, onDelete, onFinish }) => {

  return (
    <div className="task">
      <div className="taskName">
        {task.title || "No title"}
        <button onClick={() => onDelete(task.id)}>X</button>
      </div>
      <div>
        {task.description || "No description"}
      </div>
      <div className="taskInfo">
        <span>Due: {task.dueDate || "No due date"}</span>
        <span>Status: {task.completed ? "Completed" : "Pending"}</span>
        <button onClick={() => onFinish({completed:true},task.id)}>Finish</button>
      </div>

    </div>
  );
};

export default Task