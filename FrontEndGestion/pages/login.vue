<template>
  <div class="container">

    <!-- Form Section -->
    <section class="form-section">
      <div class="form-container">
        <h1 class="form-title">Inicia Sesión</h1>

        <form @submit.prevent="handleSubmit" class="form">
          <input v-model="formData.email" type="email" placeholder="Correo electrónico" class="input" required="true" />

          <input v-model="formData.password" type="password" placeholder="Contraseña" class="input" required="true" />

          <button type="submit" class="submit-button">
            Ingresar
          </button>
        </form>
        <p v-if="errorMessage" class="error-message" :class="{ 'warning-message': isWarning }">
          {{ errorMessage }}
        </p>

        <div class="register-prompt">
          <span>¿No tienes una cuenta en esta aplicación?</span><br>
          <a href="/register" class="register-link"> Crea tu cuenta aquí</a>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import { useConstitutionFormStore } from '../stores/ConstitutionForm';
import { useExitStore } from '../stores/Exit';
import { useManagementsStore } from '../stores/Managements';
import { useProcessStore } from '../stores/Process';
import { useProjectStore } from '../stores/project';



const formData = reactive({
  email: '',
  password: '',
});

const errorMessage = ref('');
const isWarning = ref(false);
const router = useRouter();
const AuthStore = useAuthStore();
const constitutionStore = useConstitutionFormStore();
const exitStore = useExitStore();
const managementsStore = useManagementsStore();
const processStore = useProcessStore();
const projectStore = useProjectStore();

const handleSubmit = async () => {
  try {
    errorMessage.value = '';
    isWarning.value = false;

    const response = await fetch('http://localhost:8080/api/v1/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(formData),
    });

    const data = await response.json();

    if (!response.ok) {
      isWarning.value = response.status === 423; // Locked status

      switch (response.status) {
        case 400: // Bad Request
          throw new Error(data.message || 'Por favor, verifica los datos ingresados.');
        case 401: // Unauthorized
          throw new Error(data.message || 'Credenciales incorrectas.');
        case 423: // Locked
          throw new Error(data.message || 'Tu cuenta está temporalmente bloqueada.');
        case 403: // Forbidden
          throw new Error(data.message || 'Tu cuenta está desactivada.');
        default:
          throw new Error('Error al iniciar sesión. Por favor, intenta más tarde.');
      }
    }

    // Save user data in the store
    AuthStore.setAuthData({
      token: data.accessToken,
      userId: data.userId,
      names: data.names,
      refreshToken: data.refreshToken,
    })

    constitutionStore.clearConstitutionData();
    exitStore.clearExitData();
    managementsStore.clearManagementData();
    processStore.clearProcessData();
    projectStore.clearProjectData();

    router.push('/principalView');

  } catch (error) {
    console.error('Login error:', error);
    errorMessage.value = error.message;
  }
}

</script>

<style scoped>
/* Ensure no extra space causes overflow */
html,
body {
  margin: 0;
  padding: 0;
  overflow: hidden;
  /* Prevent scrollbars */
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

.error-message {
  color: #dc3545;
  text-align: center;
  margin-top: 10px;
  padding: 10px;
  border-radius: 5px;
  background-color: #ffe6e6;
}

.warning-message {
  color: #856404;
  background-color: #fff3cd;
  border-color: #ffeeba;
}
</style>