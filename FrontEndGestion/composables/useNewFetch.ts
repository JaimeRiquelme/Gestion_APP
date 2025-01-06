// composables/useNewFetch.ts
import { useAuthStore } from '~/stores/auth'

export const useFetchWithAuth = () => {
  const authStore = useAuthStore()

  const fetchWithAuth = async (url: string, options: RequestInit = {}) => {
    // Configurar headers básicos
    const headers = new Headers(options.headers || {})
    
    if (authStore.token) {
      headers.set('Authorization', `Bearer ${authStore.token}`)
    }

    // Hacer la petición inicial
    let response = await fetch(url, {
      ...options,
      headers
    })
    
    // Si es 403 y tenemos refresh token, intentar refrescar el token
    if (response.status === 403 && authStore.refreshToken) {  
      try {
        const refreshResponse = await fetch('http://localhost:8080/api/v1/auth/refresh-token', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            refreshToken: authStore.refreshToken
          })
        })

        if (refreshResponse.ok) {
          const refreshData = await refreshResponse.json()
          
          // Actualizar tokens en el store
          authStore.setAuthData({
            token: refreshData.accessToken,
            userId: authStore.userId || '',
            names: authStore.names || '',
            refreshToken: refreshData.refreshToken
          })

          // Reintentar la petición original con el nuevo token
          headers.set('Authorization', `Bearer ${refreshData.accessToken}`)
          response = await fetch(url, {
            ...options,
            headers
          })
          
        } else {
          authStore.clearAuthData()
          navigateTo('/login')
          throw new Error('Sesión expirada')
        }
      } catch (error) {
        authStore.clearAuthData()
        navigateTo('/login')
        throw new Error('Error al renovar la sesión')
      }
    }

    // Crear un objeto de respuesta mejorado que maneje múltiples tipos
    const enhancedResponse = {
      ...response,
      ok: response.ok,
      status: response.status,
      json: async () => {
        const clone = response.clone()
        try {
          return await clone.json()
        } catch (error) {
          console.error('Error parsing JSON response:', error)
          throw new Error('Error al procesar la respuesta JSON')
        }
      },
      text: async () => {
        const clone = response.clone()
        try {
          return await clone.text()
        } catch (error) {
          console.error('Error getting text response:', error)
          throw new Error('Error al procesar la respuesta de texto')
        }
      },
      blob: async () => {
        const clone = response.clone()
        try {
          return await clone.blob()
        } catch (error) {
          console.error('Error getting blob response:', error)
          throw new Error('Error al procesar el archivo')
        }
      },
      // Método para determinar el tipo de contenido
      getContentType: () => {
        return response.headers.get('content-type')
      }
    }

    return enhancedResponse
  }

  return {
    fetch: fetchWithAuth
  }
}