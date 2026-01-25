import axios from 'axios'
// const baseUrl = '/api'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const login = async credentials => {
  const response = await axios.post(`${API_URL}/login`, credentials)
  return response.data
}

const register = async credentials => {
  const response = await axios.post(`${API_URL}/register`, credentials)
  return response.data
}

export default { register, login }