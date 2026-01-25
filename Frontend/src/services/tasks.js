import axios from 'axios'
// const baseUrl = '/api/tasks'

const API_URL = import.meta.env.DEV 
  ? 'http://localhost:8080/api/tasks'
  : 'https://todolist-2f9q.onrender.com/api/tasks';

let token = null

const setToken = newToken => {
    token = `Bearer ${newToken}`
}

const getAll = async () => {
    const config = {
        headers: { Authorization: token }
    }
    const response = await axios.get(API_URL, config)
    return response.data
}

const create = async (newTask) => {
    const config = {
        headers: { Authorization: token }
    }
    const response = await axios.post(API_URL, newTask, config)
    return response.data
}

const updateTask = async (task, id) => {
    const config = {
        headers: { Authorization: token }
    }
    const response = await axios.patch(`${API_URL}/${id}`, task, config)
    return response.data
}

const deleteTaskById = async (taskID) => {
    const config = {
        headers: { Authorization: token }
    }
    await axios.delete(`${API_URL}/${taskID}`, config)
}

export default { getAll, create, updateTask, deleteTaskById, setToken }