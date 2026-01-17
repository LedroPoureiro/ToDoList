import { useEffect, useState } from 'react'
import { Routes, Route, Navigate, useNavigate } from 'react-router-dom'

import Task from './components/Task'
import CreateForm from './components/CreateForm'
import LoginForm from './components/LoginForm'
import RegisterForm from './components/RegisterForm'

import taskService from './services/tasks'
import loginService from './services/login'
import llmService from './services/ollama'

const App = () => {
  const [tasks, setTasks] = useState([])
  const [visible, setVisible] = useState(false)
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [user, setUser] = useState('')
  const [llmSuggestion, setLlmSuggestion] = useState(null)

  const navigate = useNavigate()

  useEffect(() => {
    if (user) {
      const loadedTasks = async () => {
        const tasks = await taskService.getAll()
        setTasks(tasks)
      }
      loadedTasks()
    }
  }, [user])

  useEffect(() => {
    const loggedUserJWT = window.localStorage.getItem('loggedUser')
    const loggedUserName = window.localStorage.getItem('loggedName')
    if (loggedUserJWT) {
      const token = JSON.parse(loggedUserJWT)
      setUser(loggedUserName)
      taskService.setToken(token.jwt)
      llmService.setToken(token.jwt)
    }
  }, [])

  const deleteTask = async (id) => {
    try {
      await taskService.deleteTaskById(id)
      setTasks(prevTasks => prevTasks.filter(task => task.id !== id))
    }
    catch {
      console.log("Error deleting task")
    }
  }

  const createTask = async task => {
    try {
      const newTask = await taskService.create(task)
      setTasks(prev => prev.concat(newTask))
      setVisible(false)
      navigate('/tasks', { replace: true })
    }
    catch {
      console.log("Error creating task")
    }
  }

  const handleFinish = async (taskObject, id) => {
    try {
      const updatedTask = await taskService.updateTask(taskObject, id)
      setTasks((prevTasks) =>
        prevTasks.map((task) =>
          task.id === updatedTask.id ? updatedTask : task
        )
      );
      setVisible(false)
    }
    catch {
      console.log("Error updating task")
    }
  }

  const handleLogin = async event => {
    event.preventDefault()

    try {
      const user = await loginService.login({ username, password })
      window.localStorage.setItem('loggedUser', JSON.stringify(user))
      window.localStorage.setItem('loggedName', username)
      taskService.setToken(user.jwt)
      llmService.setToken(user.jwt)
      setUser(username)
      setUsername('')
      setPassword('')
    } catch {
      console.log("error logging in")
    }
  }

  const handleRegister = async event => {
    event.preventDefault()

    try {
      const user = await loginService.register({ username, password })
      console.log(user)
      setUsername('')
      setPassword('')
      navigate('/login', { replace: true })
    } catch {
      console.log("error registering")
    }
  }

  const handleLogout = () => {
    window.localStorage.removeItem(
      'loggedUser'
    )
    window.localStorage.removeItem(
      'loggedName'
    )
    setUser(null)
    taskService.setToken(null)
    llmService.setToken(null)
  }

  const registerForm = () => (
    <RegisterForm
      username={username}
      password={password}
      handleUsernameChange={({ target }) => setUsername(target.value)}
      handlePasswordChange={({ target }) => setPassword(target.value)}
      handleSubmit={handleRegister}
    />
  )

  const loginForm = () => (
    <LoginForm
      username={username}
      password={password}
      handleUsernameChange={({ target }) => setUsername(target.value)}
      handlePasswordChange={({ target }) => setPassword(target.value)}
      handleSubmit={handleLogin}
    />
  )

  const askOllama = async () => {
    try {
      const response = await llmService.getTask()
      setLlmSuggestion(response)
    } catch {
      console.log("Error getting LLM suggestion")
    }
  }

  return (
    <div className="app-container">
      <Routes>
        <Route
          path="/login"
          element={
            !user ? (
              loginForm()
            ) : (
              <Navigate to="/tasks" replace />
            )
          }
        />

        <Route
          path="/register"
          element={
            !user ? (
              registerForm()
            ) : (
              <Navigate to="/tasks" replace />
            )
          }
        />

        <Route
          path="/create"
          element={
            user ? (
              <CreateForm 
                onCreate={createTask} 
                onClickAI={askOllama} 
                llmSuggestion={llmSuggestion}
                onClearSuggestion={() => setLlmSuggestion(null)}
              />
            ) : (
              <Navigate to="/login" replace />
            )
          }
        />

        <Route
          path="/tasks"
          element={
            user ? (
              <>
                <div className="center-screen">
                  <div className="tasks">
                    <div className="tasks-header">
                      <div className="left-side">
                        <button className="add-button" onClick={() => navigate('/create')}>Add new task</button>
                      </div>
                      <h2>Todo List</h2>

                      <div>
                        Welcome {user || 'User'}!
                        <button onClick={handleLogout}>Logout</button>
                      </div>
                    </div>

                    {tasks.map((task) => (
                      <Task key={task.id} task={task} onDelete={deleteTask} onFinish={handleFinish} />
                    ))}
                  </div>
                </div>
              </>
            ) : (
              <Navigate to="/login" replace />
            )
          }
        />

        <Route
          path="/"
          element={<Navigate to={user ? "/tasks" : "/login"} replace />}
        />

        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </div>
  )
}

export default App
