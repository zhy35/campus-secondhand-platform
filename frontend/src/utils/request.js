const baseURL = '/api'

export const request = {
  async get(url) {
    const token = localStorage.getItem('token')
    const response = await fetch(`${baseURL}${url}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    return response.json()
  },

  async post(url, data) {
    const token = localStorage.getItem('token')
    const response = await fetch(`${baseURL}${url}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(data)
    })
    return response.json()
  },

  async put(url, data) {
    const token = localStorage.getItem('token')
    const response = await fetch(`${baseURL}${url}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(data)
    })
    return response.json()
  },

  async delete(url) {
    const token = localStorage.getItem('token')
    const response = await fetch(`${baseURL}${url}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    return response.json()
  }
}