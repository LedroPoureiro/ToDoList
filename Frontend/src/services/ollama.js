import axios from 'axios'
const baseUrl = 'http://localhost:8080/message'

let token = null

const setToken = newToken => {
    token = `Bearer ${newToken}`
}

const getTask = async () => {
    const config = {
        headers: { Authorization: token }
    }
    const response = await axios.get(baseUrl, config )
    return response.data
}


export default { getTask, setToken }