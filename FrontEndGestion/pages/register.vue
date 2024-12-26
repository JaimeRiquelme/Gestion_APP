<template>
    <div class="container">
 
      <section class="form-section">
        <div class="form-container">
          <h1 class="form-title">Registrarse</h1>
          
          <form @submit.prevent="handleSubmit" class="form">
            <input
              v-model="formData.names"
              type="text"
              placeholder="Nombres"
              class="input"
              required="true"
            />

            <input
              v-model="formData.secondNames"
              type="text"
              placeholder="Apellidos"
              class="input"
              required="true"
            />
  
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
  
            <input
              v-model="formData.phoneNumber"
              type="tel"
              placeholder="Número de Telefono"
              class="input"
              required="true"
            />
  
            <button type="submit" class="submit-button">
              Registrarse
            </button>
          </form>
  
          <div class="login-prompt">
            <span>¿Ya tienes una cuenta en esta aplicación?</span>
            <a href="/login" class="login-link"> Inicia sesión aquí</a>
          </div>
        </div>
      </section>
    </div>
  </template>
  
  <script setup>
  import { reactive } from 'vue'
  
  const formData = reactive({
    names: '',
    secondNames: '',
    email: '',
    password: '',
    phoneNumber: ''
  })

  const url = "http://localhost:8080/api/v1/auth";
  
  const handleSubmit = async () => {
    try {
      console.log('Form submitted:', formData)
      try {
        const response = await $fetch(url + '/register', {
          method: 'POST',
          body: { 
            names: formData.names,
            secondNames: formData.secondNames,
            email: formData.email,
            password: formData.password,
            phoneNumber: formData.phoneNumber
          }
        })
        console.log('Registered successfully');

        // Redirect to login on successful register
        alert('Registro exitoso. Inicie sesión para continuar');
        await navigateTo('/login')
      } catch (error) {
        console.error('Connection error:', error);
      }
    } catch (error) {
      console.error('Registration error:', error)
    }
  }
  </script>
  
<style scoped>


.container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    background-color: #ffffff;
    overflow: auto;
}

.form-section {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
}

.form-container {
    background-color: #d9d9d9;
    border-radius: 10px;
    width: 100%;
    max-width: 600px;
    padding: 2rem;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    text-align: center;
    overflow: auto;
}

.form-title {
    font-size: 1.5rem;
    font-weight: bold;
    color: #000000;
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

.login-prompt {
    margin-top: 1rem;
    font-size: 0.875rem;
    color: #666;
}

.login-link {
    color: #00B8B0;
    text-decoration: none;
    font-weight: bold;
}

.login-link:hover {
    text-decoration: underline;
}
</style>
