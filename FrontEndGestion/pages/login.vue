<template>
  <div class="container">

    <!-- Form Section -->
    <section class="form-section">
      <div class="form-container">
        <h1 class="form-title">Inicia Sesión</h1>
        
        <form @submit.prevent="handleSubmit" class="form">
          <input
            v-model="formData.email"
            type="email"
            placeholder="Correo electrónico"
            class="input"
            required="true"
          />

          <input
            v-model="formData.password"
            type="password"
            placeholder="Contraseña"
            class="input"
            required="true"
          />

          <button type="submit" class="submit-button">
            Ingresar
          </button>
        </form>

        <div class="register-prompt">
          <span>¿No tienes una cuenta en esta aplicación?</span><br>
          <a href="/register" class="register-link"> Crea tu cuenta aquí</a>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const formData = reactive({
  email: '',
  password: ''
})

const handleSubmit = async () => {
  try {
    console.log('Form submitted:', formData)
    // Add your login logic here
  } catch (error) {
    console.error('Login error:', error)
  }
}
</script>

<style scoped>
/* Ensure no extra space causes overflow */
html, body {
  margin: 0;
  padding: 0;
  overflow: hidden; /* Prevent scrollbars */
}

/* Apply border-box for consistent sizing */
* {
  box-sizing: border-box;
}

.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  background-color: #00B8B0;
}

.form-section {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}

.form-container {
  background-color: #f0f0f0;
  border-radius: 10px;
  width: 100%;
  max-width: 600px;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.form-title {
  font-size: 1.5rem;
  font-weight: bold;
  color: #009B94;
  margin-bottom: 1rem;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #fff;
  font-size: 1rem;
  color: rgb(0, 0, 0);
}

.input:focus {
  outline: none;
  border-color: #00B8B0;
  box-shadow: 0 0 0 2px rgba(0, 184, 176, 0.3);
}

.input::placeholder {
  color: #bdbdbd;
}

.submit-button {
  background-color: #00B8B0;
  color: #fff;
  border: none;
  padding: 0.75rem;
  border-radius: 5px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background-color: #009B94;
}

.register-prompt {
  margin-top: 1rem;
  font-size: 0.875rem;
  color: #666;
}

.register-link {
  color: #00B8B0;
  text-decoration: none;
  font-weight: bold;
}

.register-link:hover {
  text-decoration: underline;
}
</style>