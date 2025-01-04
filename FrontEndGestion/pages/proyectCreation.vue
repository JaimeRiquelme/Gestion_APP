<template>
    <div class="page-container">
      <AlertPopup
        :show="showAlert"
        :title="alertTitle"
        :message="alertMessage"
        :type="alertType"
        @confirm="handleAlertConfirm"
      />
  
      <main class="main-content">
        <div class="title-content">
          <h1 class="title-content-text">Crear Nuevo Proyecto</h1>
        </div>
        <div class="form-container">
          <form @submit.prevent="handleSubmit" class="project-form">
            <div class="form-group">
              <label for="nameProyect">Nombre del Proyecto *</label>
              <input
                id="nameProyect"
                v-model="formData.nameProyect"
                type="text"
                class="form-input"
                required
              />
            </div>
  
            <div class="form-group">
              <label for="organization">Organización *</label>
              <input
                id="organization"
                v-model="formData.organization"
                type="text"
                class="form-input"
                required
              />
            </div>
  
            <div class="form-group">
              <label for="description">Descripción *</label>
              <textarea
                id="description"
                v-model="formData.description"
                class="form-input form-textarea"
                rows="4"
                required
              ></textarea>
            </div>
  
            <div class="form-row">
              <div class="form-group">
                <label for="startDate">Fecha de Inicio *</label>
                <input
                  id="startDate"
                  v-model="formData.startDate"
                  type="date"
                  class="form-input"
                  :min="new Date().toISOString().split('T')[0]"
                  required
                />
              </div>
  
              <div class="form-group">
                <label for="estimatedEndDate">Fecha Estimada de Término *</label>
                <input
                  id="estimatedEndDate"
                  v-model="formData.estimatedEndDate"
                  type="date"
                  class="form-input"
                  :min="formData.startDate"
                  required
                />
              </div>       
            </div>
  
            <div class="form-actions">
              <button 
                type="button" 
                class="cancel-button"
                @click="navigateTo('/principalView')"
              >
                Cancelar
              </button>
              <button 
                type="submit" 
                class="submit-button"
                :disabled="loading"
              >
                {{ loading ? 'Creando...' : 'Crear Proyecto' }}
              </button>
            </div>
          </form>
  
          <div v-if="errorMessage" class="error-message">
            {{ errorMessage }}
          </div>
        </div>
      </main>
    </div>
  </template>
  
  <script setup>
  import { reactive, ref } from 'vue';
  import { useAuthStore } from '../stores/auth';
  import { useProjectStore } from '../stores/project';
  import AlertPopup from '../components/AlertPopup.vue';
  
  const loading = ref(false);
  const errorMessage = ref('');
  const AuthStore = useAuthStore();
  const ProjectStore = useProjectStore();
  
  const showAlert = ref(false);
  const alertTitle = ref('');
  const alertMessage = ref('');
  const alertType = ref('info');
  const pendingNavigation = ref('');
  
  const formData = reactive({
    nameProyect: '',
    description: '',
    organization: '',
    startDate: '',
    estimatedEndDate: '',
  });
  
  const handleAlertConfirm = () => {
    showAlert.value = false;
    if (pendingNavigation.value) {
      navigateTo(pendingNavigation.value);
      pendingNavigation.value = '';
    }
  };
  
  const handleSubmit = async () => {
    try {
      loading.value = true;
      errorMessage.value = '';
  
      const userId = AuthStore.userId;
      const token = AuthStore.token;
  
      if (!userId || !token) {
        alertTitle.value = 'Error de Sesión';
        alertMessage.value = '¡Sesión no iniciada!, redirigiendo a login...';
        alertType.value = 'error';
        showAlert.value = true;
        pendingNavigation.value = '/login';
        return;
      }
  
      const projectData = {
        ...formData,
        idUsuario: parseInt(userId),
      };
  
      const response = await fetch('http://localhost:8080/api/v1/proyect/create', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(projectData),
      });
  
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error al crear el proyecto');
      }
  
      const project = await response.json();
      ProjectStore.setProjectData({
        projectId: project.idProyecto,
        projectName: project.nameProyect,
      });
      
      alertTitle.value = 'Éxito';
      alertMessage.value = `Proyecto "${formData.nameProyect}" creado exitosamente, redirigiendo a Dashboard...`;
      alertType.value = 'success';
      showAlert.value = true;
      pendingNavigation.value = '/ConstitutionNotice';
  
    } catch (error) {
      console.error('Error creating project:', error);
      alertTitle.value = 'Error';
      alertMessage.value = error.message || 'Error al crear el proyecto. Por favor, intenta nuevamente.';
      alertType.value = 'error';
      showAlert.value = true;
    } finally {
      loading.value = false;
    }
  };
  </script>
  
  <style scoped>
  .page-container {
    min-height: 100vh;
    background-color: #f5f5f5;
  }
  
  .title-content {
    background-color: #f5f5f5;
    padding: 5.5rem 3rem;
  }
  
  .title-content-text {
    max-width: 800px;
    margin: 0 auto;
    color: #000000;
    display: flex;
    align-items: center;
    gap: 1rem;
  }
  
  .header-title {
    font-size: 1.5rem;
    margin: 0;
  }
  
  .main-content {
    padding: 2rem;
  }
  
  .form-container {
    max-width: 800px;
    width: 90%;
    margin: 2rem auto;
    background: white;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
  
  .project-form {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .form-row {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1rem;
  }
  
  label {
    font-weight: 500;
    color: #333;
  }
  
  .form-input {
    padding: 0.75rem;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 1rem;
    transition: border-color 0.3s;
    color: #000000;
    background-color: #ffffff;
  }
  
  input[type="date"].form-input {
    color: #000000;
    background-color: #ffffff;
    cursor: pointer;
    position: relative;
    padding: 0.75rem;
  }
  
  /* Color del ícono del calendario */
  input[type="date"]::-webkit-calendar-picker-indicator {
    opacity: 1;
    display: block;
    background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24' fill='none' stroke='%23666' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Crect x='3' y='4' width='18' height='18' rx='2' ry='2'%3E%3C/rect%3E%3Cline x1='16' y1='2' x2='16' y2='6'%3E%3C/line%3E%3Cline x1='8' y1='2' x2='8' y2='6'%3E%3C/line%3E%3Cline x1='3' y1='10' x2='21' y2='10'%3E%3C/line%3E%3C/svg%3E") no-repeat center;
    width: 20px;
    height: 20px;
    cursor: pointer;
  }
  
  .form-input:focus {
    outline: none;
    border-color: #00B8B0;
    box-shadow: 0 0 0 2px rgba(0, 184, 176, 0.1);
  }
  
  .form-textarea {
    resize: vertical;
    min-height: 100px;
  }
  
  .form-actions {
    display: flex;
    gap: 1rem;
    justify-content: flex-end;
    margin-top: 1rem;
  }
  
  .submit-button, .cancel-button {
    padding: 0.75rem 1.5rem;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: all 0.3s;
  }
  
  .submit-button {
    background-color: #00B8B0;
    color: white;
    border: none;
  }
  
  .submit-button:hover:not(:disabled) {
    background-color: #009B94;
  }
  
  .submit-button:disabled {
    opacity: 0.7;
    cursor: not-allowed;
  }
  
  .cancel-button {
    background-color: #f5f5f5;
    color: #666;
    border: 1px solid #ddd;
  }
  
  .cancel-button:hover {
    background-color: #eee;
  }
  
  .error-message {
    margin-top: 1rem;
    padding: 1rem;
    border-radius: 4px;
    background-color: #ffe6e6;
    color: #dc3545;
    text-align: center;
  }
  
  @media (max-width: 600px) {
    .form-container {
      padding: 1rem;
    }
    
    .form-row {
      grid-template-columns: 1fr;
    }
    
    .form-actions {
      flex-direction: column-reverse;
    }
    
    .submit-button, .cancel-button {
      width: 100%;
    }
  }
  </style>