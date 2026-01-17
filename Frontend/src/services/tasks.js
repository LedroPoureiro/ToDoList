import axios from 'axios'
const baseUrl = '/api/tasks'

let token = null

const setToken = newToken => {
    token = `Bearer ${newToken}`
}

const getAll = async () => {
    const config = {
        headers: { Authorization: token }
    }
    const response = await axios.get(baseUrl, config)
    return response.data
}

const create = async (newTask) => {
    const config = {
        headers: { Authorization: token }
    }
    const response = await axios.post(baseUrl, newTask, config)
    return response.data
}

const updateTask = async (task, id) => {
    const config = {
        headers: { Authorization: token }
    }
    const response = await axios.patch(`${baseUrl}/${id}`, task, config)
    return response.data
}

const deleteTaskById = async (taskID) => {
    const config = {
        headers: { Authorization: token }
    }
    await axios.delete(`${baseUrl}/${taskID}`, config)
}

export default { getAll, create, updateTask, deleteTaskById, setToken }