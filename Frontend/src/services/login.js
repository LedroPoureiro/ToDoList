import axios from 'axios'
// const baseUrl = '/api'

const API_URL = import.meta.env.DEV 
  ? 'http://localhost:8080/api'
  : 'https://todolist-2f9q.onrender.com/api';

const login = async credentials => {
  console.log(API_URL)
  const response = await axios.post(`${API_URL}/login`, credentials)
  return response.data
}

const register = async credentials => {
  const response = await axios.post(`${API_URL}/register`, credentials)
  return response.data
}

export default { register, login }